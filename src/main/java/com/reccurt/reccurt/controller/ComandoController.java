package com.reccurt.reccurt.controller;

import com.reccurt.reccurt.dto.ComandoValidateRequest;
import com.reccurt.reccurt.dto.ComandoValidateResponse;
import com.reccurt.reccurt.service.ComandoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/commands")
@Tag(name = "Comandos", description = "APIs para validação de comandos de corte e religação")
public class ComandoController {

    private final ComandoService comandoService;

    public ComandoController(ComandoService comandoService) {
        this.comandoService = comandoService;
    }

    @Operation(summary = "Validar comando", description = "Valida um comando de corte ou religação conforme regras da ANEEL")
    @PostMapping("/validate")
    public ResponseEntity<ComandoValidateResponse> validarComando(
            @Valid @RequestBody ComandoValidateRequest request) {

        ComandoValidateResponse response = comandoService.validarComando(request);
        return ResponseEntity.ok(response);
    }
}