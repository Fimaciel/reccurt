package com.reccurt.reccurt.controller;

import com.reccurt.reccurt.dto.ComandoHistoryResponse;
import com.reccurt.reccurt.service.LogValidacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/commands")
@Tag(name = "Histórico", description = "APIs para consulta de histórico de validações")
public class LogValidacaoController {

    private final LogValidacaoService logValidacaoService;

    public LogValidacaoController(LogValidacaoService logValidacaoService) {
        this.logValidacaoService = logValidacaoService;
    }

    @Operation(summary = "Consultar histórico", description = "Consulta o histórico de validações por UC e/ou período")
    @GetMapping("/history")
    public ResponseEntity<ComandoHistoryResponse> consultarHistorico(
            @RequestParam(required = false) String ucId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dataFim) {

        ComandoHistoryResponse response = logValidacaoService.consultarHistorico(ucId, dataInicio, dataFim);
        return ResponseEntity.ok(response);
    }
}