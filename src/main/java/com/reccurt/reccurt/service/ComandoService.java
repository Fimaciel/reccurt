package com.reccurt.reccurt.service;

import com.reccurt.reccurt.dto.ComandoValidateRequest;
import com.reccurt.reccurt.dto.ComandoValidateResponse;
import com.reccurt.reccurt.model.*;
import com.reccurt.reccurt.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComandoService {

    private final UnidadeConsumidoraRepository unidadeConsumidoraRepository;
    private final FeriadoRepository feriadoRepository;
    private final ComandoRepository comandoRepository;
    private final LogValidacaoRepository logValidacaoRepository;

    public ComandoService(UnidadeConsumidoraRepository unidadeConsumidoraRepository,
                          FeriadoRepository feriadoRepository,
                          ComandoRepository comandoRepository,
                          LogValidacaoRepository logValidacaoRepository) {
        this.unidadeConsumidoraRepository = unidadeConsumidoraRepository;
        this.feriadoRepository = feriadoRepository;
        this.comandoRepository = comandoRepository;
        this.logValidacaoRepository = logValidacaoRepository;
    }

    @Transactional
    public ComandoValidateResponse validarComando(ComandoValidateRequest request) {
        Optional<UnidadeConsumidora> ucOpt = unidadeConsumidoraRepository.findById(request.getUcId());
        if (ucOpt.isEmpty()) {
            return criarRespostaBloqueio("Unidade consumidora não encontrada: " + request.getUcId());
        }
        UnidadeConsumidora uc = ucOpt.get();

        Comando.TipoComando tipoComando;
        try {
            tipoComando = Comando.TipoComando.valueOf(request.getTipoComando().toUpperCase());
        } catch (IllegalArgumentException e) {
            return criarRespostaBloqueio("Tipo de comando inválido: " + request.getTipoComando());
        }

        Comando comando = new Comando(uc, tipoComando, request.getTimestamp(), request.getSolicitante());

        // RF001 - Validação de Horário Permitido (apenas para CORTE)
        if (tipoComando == Comando.TipoComando.CORTE) {
            String validacaoHorario = validarHorarioPermitido(request.getTimestamp());
            if (validacaoHorario != null) {
                return processarComandoBloqueado(comando, validacaoHorario, request.getSolicitante());
            }
        }

        // RF002 - Validação de Feriados e Vésperas (apenas para CORTE)
        if (tipoComando == Comando.TipoComando.CORTE) {
            String validacaoFeriado = validarFeriadoEVespera(request.getTimestamp().toLocalDate(), request.getRegiao());
            if (validacaoFeriado != null) {
                return processarComandoBloqueado(comando, validacaoFeriado, request.getSolicitante());
            }
        }

        // RF003 - Validação de Tipo de UC (apenas para CORTE)
        if (tipoComando == Comando.TipoComando.CORTE && uc.getTipo() == UnidadeConsumidora.Tipo.ESSENCIAL) {
            return processarComandoBloqueado(comando,
                    "Corte não permitido para unidades consumidoras essenciais (" + uc.getNome() + ")",
                    request.getSolicitante());
        }

        // RF004 - Cálculo de Prazo de Religação
        LocalDateTime prazoExecucao = null;
        if (tipoComando == Comando.TipoComando.RELIGACAO) {
            prazoExecucao = calcularPrazoReligacao(uc.getTipo(), request.getTimestamp());
        }

        return processarComandoAprovado(comando, prazoExecucao, request.getSolicitante());
    }

    // RF001 - Validação de Horário Permitido
    private String validarHorarioPermitido(LocalDateTime timestamp) {
        LocalTime hora = timestamp.toLocalTime();
        DayOfWeek diaSemana = timestamp.getDayOfWeek();

        // Verificar se é final de semana
        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            return "Corte não permitido em finais de semana";
        }

        // Verificar horário comercial (8h às 18h)
        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            return "Corte fora do horário permitido (8h-18h em dias úteis)";
        }

        return null; // Horário permitido
    }

    // RF002 - Validação de Feriados e Vésperas
    private String validarFeriadoEVespera(LocalDate data, String regiao) {
        // Verificar se é feriado
        List<Feriado> feriados = feriadoRepository.findByDataAndRegiaoOuNacional(data, regiao);
        if (!feriados.isEmpty()) {
            return "Corte não permitido em feriado: " + feriados.get(0).getNome();
        }

        // Verificar véspera de feriado (após 12h do dia anterior)
        LocalDate véspera = data.minusDays(1);
        List<Feriado> feriadosVéspera = feriadoRepository.findByDataAndRegiaoOuNacional(véspera, regiao);
        if (!feriadosVéspera.isEmpty()) {
            return "Corte não permitido na véspera de feriado: " + feriadosVéspera.get(0).getNome();
        }

        return null; // Não é feriado nem véspera
    }

    // RF004 - Cálculo de Prazo de Religação
    private LocalDateTime calcularPrazoReligacao(UnidadeConsumidora.Tipo tipoUc, LocalDateTime timestampRecebimento) {
        switch (tipoUc) {
            case RESIDENCIAL:
                return timestampRecebimento.plusHours(24);
            case COMERCIAL:
            case INDUSTRIAL:
                return timestampRecebimento.plusHours(8);
            case EMERGENCIA:
                return timestampRecebimento.plusMinutes(30); // "Imediato" - considerando 30min para execução
            default:
                return timestampRecebimento.plusHours(24); // Default
        }
    }

    private ComandoValidateResponse processarComandoAprovado(Comando comando, LocalDateTime prazoExecucao, String solicitante) {
        // Salvar comando aprovado
        comando.setAprovado(true);
        comando.setPrazoExecucao(prazoExecucao);
        Comando comandoSalvo = comandoRepository.save(comando);

        // RF005 - Auditoria
        LogValidacao log = new LogValidacao(comandoSalvo, true, "Comando aprovado",
                LocalDateTime.now(), solicitante);
        logValidacaoRepository.save(log);

        return new ComandoValidateResponse(true, null, prazoExecucao);
    }

    private ComandoValidateResponse processarComandoBloqueado(Comando comando, String motivo, String solicitante) {
        // Salvar comando bloqueado
        comando.setAprovado(false);
        comando.setMotivo(motivo);
        Comando comandoSalvo = comandoRepository.save(comando);

        // RF005 - Auditoria
        LogValidacao log = new LogValidacao(comandoSalvo, false, motivo,
                LocalDateTime.now(), solicitante);
        logValidacaoRepository.save(log);

        return new ComandoValidateResponse(false, motivo, null);
    }

    private ComandoValidateResponse criarRespostaBloqueio(String motivo) {
        return new ComandoValidateResponse(false, motivo, null);
    }
}