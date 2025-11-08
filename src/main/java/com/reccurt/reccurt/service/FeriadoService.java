package com.reccurt.reccurt.service;

import com.reccurt.reccurt.dto.FeriadoCreateRequest;
import com.reccurt.reccurt.dto.FeriadoCreateResponse;
import com.reccurt.reccurt.model.Feriado;
import com.reccurt.reccurt.repository.FeriadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeriadoService {

    private final FeriadoRepository feriadoRepository;

    public FeriadoService(FeriadoRepository feriadoRepository) {
        this.feriadoRepository = feriadoRepository;
    }

    public FeriadoCreateResponse criarFeriado(FeriadoCreateRequest request) {
        List<Feriado> feriadosExistentes = feriadoRepository.findByDataAndRegiaoOuNacional(
                request.getData(),
                request.getRegiao()
        );

        if (!feriadosExistentes.isEmpty()) {
            throw new IllegalArgumentException("Já existe um feriado cadastrado para esta data e região");
        }

        // Converter tipo para enum
        Feriado.TipoFeriado tipoFeriado;
        try {
            tipoFeriado = Feriado.TipoFeriado.valueOf(request.getTipo().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de feriado inválido: " + request.getTipo());
        }

        // Criar e salvar feriado
        Feriado feriado = new Feriado(
                request.getData(),
                request.getNome(),
                tipoFeriado,
                request.getRegiao()
        );

        Feriado feriadoSalvo = feriadoRepository.save(feriado);

        return new FeriadoCreateResponse(
                feriadoSalvo.getId(),
                feriadoSalvo.getData(),
                feriadoSalvo.getNome(),
                feriadoSalvo.getTipo().name()
        );
    }

    public List<Feriado> listarTodosFeriados() {
        return feriadoRepository.findAll();
    }
}