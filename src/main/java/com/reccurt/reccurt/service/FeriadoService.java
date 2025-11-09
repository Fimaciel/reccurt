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
        if (request.getNome() == null || request.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do feriado é obrigatório");
        }
        if (request.getTipo() == null || request.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do feriado é obrigatório");
        }

        Feriado.TipoFeriado tipoFeriado = validarTipo(request.getTipo());

        validarRegiao(tipoFeriado, request.getRegiao());


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
                feriadoSalvo.getTipo().name().toLowerCase()
        );
    }

    private Feriado.TipoFeriado validarTipo(String tipo) {
        String tipoNormalizado = tipo.trim().toLowerCase();

        if (!tipoNormalizado.equals("nacional") && !tipoNormalizado.equals("regional")) {
            throw new IllegalArgumentException("Tipo de feriado inválido. Valores permitidos: 'nacional' ou 'regional'");
        }

        try {
            return Feriado.TipoFeriado.valueOf(tipoNormalizado.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de feriado inválido: " + tipo);
        }
    }

    private void validarRegiao(Feriado.TipoFeriado tipo, String regiao) {
        if (tipo == Feriado.TipoFeriado.regional) {
            if (regiao == null || regiao.trim().isEmpty()) {
                throw new IllegalArgumentException("Região é obrigatória para feriados regionais");
            }
        } else {
            if (regiao != null && !regiao.trim().isEmpty()) {
                throw new IllegalArgumentException("Região deve ser nula para feriados nacionais");
            }
        }
    }

    public List<Feriado> listarTodosFeriados() {
        return feriadoRepository.findAll();
    }
}