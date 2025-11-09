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
            tipoComando = Comando.TipoComando.valueOf(request.getTipoComando().toLowerCase());
        } catch (IllegalArgumentException e) {
            return criarRespostaBloqueio("Tipo de comando inválido: " + request.getTipoComando() + ". Valores permitidos: 'corte' ou 'religacao'");
        }

        Comando comando = new Comando(uc, tipoComando, request.getTimestamp(), request.getSolicitante());

        if (tipoComando == Comando.TipoComando.corte) {
            String validacaoHorario = validarHorarioPermitido(request.getTimestamp());
            if (validacaoHorario != null) {
                return processarComandoBloqueado(comando, validacaoHorario, request.getSolicitante());
            }
        }

        if (tipoComando == Comando.TipoComando.corte) {
            String validacaoFeriado = validarFeriadoEVespera(request.getTimestamp(), request.getRegiao());
            if (validacaoFeriado != null) {
                return processarComandoBloqueado(comando, validacaoFeriado, request.getSolicitante());
            }
        }

        if (tipoComando == Comando.TipoComando.corte && uc.getTipo() == UnidadeConsumidora.Tipo.essencial) {
            return processarComandoBloqueado(comando,
                    "Corte não permitido para unidades consumidoras essenciais (" + uc.getNome() + ")",
                    request.getSolicitante());
        }

        LocalDateTime prazoExecucao = null;
        if (tipoComando == Comando.TipoComando.religacao) {
            prazoExecucao = calcularPrazoReligacao(uc.getTipo(), request.getTimestamp());
        }

        return processarComandoAprovado(comando, prazoExecucao, request.getSolicitante());
    }

    private String validarHorarioPermitido(LocalDateTime timestamp) {
        LocalTime hora = timestamp.toLocalTime();
        DayOfWeek diaSemana = timestamp.getDayOfWeek();

        if (diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY) {
            return "Corte não permitido em finais de semana";
        }

        if (hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            return "Corte fora do horário permitido (8h-18h em dias úteis)";
        }

        return null;
    }

    private String validarFeriadoEVespera(LocalDateTime timestamp, String regiao) {
        LocalDate data = timestamp.toLocalDate();

        List<Feriado> feriados = feriadoRepository.findByDataERegiao(data, regiao);
        if (!feriados.isEmpty()) {
            return "Corte não permitido em feriado: " + feriados.get(0).getNome();
        }

        LocalTime hora = timestamp.toLocalTime();
        if (hora.isAfter(LocalTime.of(12, 0))) {
            LocalDate amanha = data.plusDays(1);
            List<Feriado> feriadosAmanha = feriadoRepository.findByDataERegiao(amanha, regiao);
            if (!feriadosAmanha.isEmpty()) {
                return "Corte não permitido na véspera de feriado (após 12h): " + feriadosAmanha.get(0).getNome();
            }
        }

        return null;
    }

    private LocalDateTime calcularPrazoReligacao(UnidadeConsumidora.Tipo tipoUc, LocalDateTime timestampRecebimento) {
        switch (tipoUc) {
            case residencial:
                return timestampRecebimento.plusHours(24);
            case comercial:
            case industrial:
                return timestampRecebimento.plusHours(8);
            case emergencia:
                return timestampRecebimento.plusMinutes(30);
            default:
                return timestampRecebimento.plusHours(24);
        }
    }

    private ComandoValidateResponse processarComandoAprovado(Comando comando, LocalDateTime prazoExecucao, String solicitante) {
        comando.setAprovado(true);
        comando.setPrazoExecucao(prazoExecucao);
        Comando comandoSalvo = comandoRepository.save(comando);

        LogValidacao log = new LogValidacao(comandoSalvo, true, "Comando aprovado",
                comando.getTimestamp(), solicitante);
        logValidacaoRepository.save(log);

        return new ComandoValidateResponse(true, null, prazoExecucao);
    }

    private ComandoValidateResponse processarComandoBloqueado(Comando comando, String motivo, String solicitante) {
        comando.setAprovado(false);
        comando.setMotivo(motivo);
        Comando comandoSalvo = comandoRepository.save(comando);

        LogValidacao log = new LogValidacao(comandoSalvo, false, motivo,
                comando.getTimestamp(), solicitante);
        logValidacaoRepository.save(log);

        return new ComandoValidateResponse(false, motivo, null);
    }

    private ComandoValidateResponse criarRespostaBloqueio(String motivo) {
        return new ComandoValidateResponse(false, motivo, null);
    }
}