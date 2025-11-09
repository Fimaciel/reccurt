package com.reccurt.reccurt.controller;

import com.reccurt.reccurt.dto.FeriadoCreateRequest;
import com.reccurt.reccurt.dto.FeriadoCreateResponse;
import com.reccurt.reccurt.model.Feriado;
import com.reccurt.reccurt.service.FeriadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/holidays")
@Tag(name = "Feriados", description = "APIs para gerenciamento de feriados")
public class FeriadoController {

    private final FeriadoService feriadoService;

    public FeriadoController(FeriadoService feriadoService) {
        this.feriadoService = feriadoService;
    }

    @Operation(summary = "Cadastrar feriado", description = "Cadastra um novo feriado nacional ou regional")
    @PostMapping
    public ResponseEntity<FeriadoCreateResponse> criarFeriado(
            @Valid @RequestBody FeriadoCreateRequest request) {

        FeriadoCreateResponse response = feriadoService.criarFeriado(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar feriados", description = "Retorna todos os feriados cadastrados")
    @GetMapping
    public ResponseEntity<List<Feriado>> listarFeriados() {
        List<Feriado> feriados = feriadoService.listarTodosFeriados();
        return ResponseEntity.ok(feriados);
    }
}