package com.reccurt.reccurt.service;

import com.reccurt.reccurt.dto.ComandoHistoryResponse;
import com.reccurt.reccurt.model.LogValidacao;
import com.reccurt.reccurt.repository.LogValidacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogValidacaoService {

    private final LogValidacaoRepository logValidacaoRepository;

    public LogValidacaoService(LogValidacaoRepository logValidacaoRepository) {
        this.logValidacaoRepository = logValidacaoRepository;
    }

    public ComandoHistoryResponse consultarHistorico(String ucId, LocalDateTime dataInicio, LocalDateTime dataFim) {
        List<LogValidacao> logs;

        if (ucId != null && dataInicio != null && dataFim != null) {
            logs = logValidacaoRepository.findByUcId(ucId).stream()
                    .filter(log -> isDentroDoPeriodo(log.getDataHoraValidacao(), dataInicio, dataFim))
                    .sorted((a, b) -> b.getDataHoraValidacao().compareTo(a.getDataHoraValidacao()))
                    .collect(Collectors.toList());
        } else if (ucId != null) {
            logs = logValidacaoRepository.findByUcId(ucId);
        } else if (dataInicio != null && dataFim != null) {
            logs = logValidacaoRepository.findByDataHoraValidacaoBetween(dataInicio, dataFim)
                    .stream()
                    .sorted((a, b) -> b.getDataHoraValidacao().compareTo(a.getDataHoraValidacao()))
                    .collect(Collectors.toList());
        } else {
            logs = logValidacaoRepository.findAll().stream()
                    .sorted((a, b) -> b.getDataHoraValidacao().compareTo(a.getDataHoraValidacao()))
                    .limit(100)
                    .collect(Collectors.toList());
        }

        List<ComandoHistoryResponse.ComandoHistoryItem> itens = logs.stream()
                .map(this::converterParaHistoryItem)
                .collect(Collectors.toList());

        return new ComandoHistoryResponse(itens.size(), itens);
    }

    private boolean isDentroDoPeriodo(LocalDateTime dataHora, LocalDateTime inicio, LocalDateTime fim) {
        return !dataHora.isBefore(inicio) && !dataHora.isAfter(fim);
    }

    private ComandoHistoryResponse.ComandoHistoryItem converterParaHistoryItem(LogValidacao log) {
        return new ComandoHistoryResponse.ComandoHistoryItem(
                log.getComando().getId(),
                log.getComando().getUnidadeConsumidora().getId(),
                log.getComando().getTipoComando().name(),
                log.getComando().getTimestamp(),
                log.isAprovado(),
                log.getMotivo()
        );
    }
}