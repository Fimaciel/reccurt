package com.reccurt.reccurt.service;

import com.reccurt.reccurt.dto.ComandoValidateRequest;
import com.reccurt.reccurt.dto.ComandoValidateResponse;
import com.reccurt.reccurt.model.*;
import com.reccurt.reccurt.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComandoServiceTest {

    @Mock
    private UnidadeConsumidoraRepository unidadeConsumidoraRepository;

    @Mock
    private FeriadoRepository feriadoRepository;

    @Mock
    private ComandoRepository comandoRepository;

    @Mock
    private LogValidacaoRepository logValidacaoRepository;

    @InjectMocks
    private ComandoService comandoService;

    private UnidadeConsumidora ucResidencial;
    private UnidadeConsumidora ucEssencial;

    @BeforeEach
    void setUp() {
        // Setup UCs para testes
        ucResidencial = new UnidadeConsumidora("UC001", "João Silva", UnidadeConsumidora.Tipo.residencial, "fortaleza");
        ucEssencial = new UnidadeConsumidora("UC002", "Hospital Regional", UnidadeConsumidora.Tipo.essencial, "fortaleza");
    }

    @Test
    void corteEmHorarioPermitido_DeveAprovar() {
        // Arrange
        ComandoValidateRequest request = new ComandoValidateRequest();
        request.setUcId("UC001");
        request.setTipoUc("residencial");
        request.setTipoComando("corte");
        request.setTimestamp(LocalDateTime.of(2025, 11, 10, 10, 0, 0)); // Segunda-feira 10h
        request.setRegiao("fortaleza");
        request.setSolicitante("sistema_comercial");

        when(unidadeConsumidoraRepository.findById("UC001")).thenReturn(Optional.of(ucResidencial));
        when(feriadoRepository.findByDataERegiao(any(), any())).thenReturn(List.of());
        when(comandoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logValidacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ComandoValidateResponse response = comandoService.validarComando(request);

        // Assert
        assertTrue(response.getAprovado());
        assertNull(response.getMotivo());
        assertNull(response.getPrazoExecucao());

        verify(unidadeConsumidoraRepository).findById("UC001");
        verify(comandoRepository).save(any(Comando.class));
        verify(logValidacaoRepository).save(any(LogValidacao.class));
    }

    @Test
    void corteEmHorarioProibido_DeveBloquear() {
        // Arrange
        ComandoValidateRequest request = new ComandoValidateRequest();
        request.setUcId("UC001");
        request.setTipoUc("residencial");
        request.setTipoComando("corte");
        request.setTimestamp(LocalDateTime.of(2025, 11, 10, 7, 0, 0)); // Segunda-feira 7h
        request.setRegiao("fortaleza");
        request.setSolicitante("sistema_comercial");

        when(unidadeConsumidoraRepository.findById("UC001")).thenReturn(Optional.of(ucResidencial));
        when(comandoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logValidacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ComandoValidateResponse response = comandoService.validarComando(request);

        // Assert
        assertFalse(response.getAprovado());
        assertEquals("Corte fora do horário permitido (8h-18h em dias úteis)", response.getMotivo());
        assertNull(response.getPrazoExecucao());
    }

    @Test
    void corteEmFeriadoNacional_DeveBloquear() {
        // Arrange
        ComandoValidateRequest request = new ComandoValidateRequest();
        request.setUcId("UC001");
        request.setTipoUc("residencial");
        request.setTipoComando("corte");
        request.setTimestamp(LocalDateTime.of(2025, 11, 15, 10, 0, 0)); // 15/11 - Proclamação da República
        request.setRegiao("fortaleza");
        request.setSolicitante("sistema_comercial");

        Feriado feriado = new Feriado(LocalDate.of(2025, 11, 15), "Proclamação da República", Feriado.TipoFeriado.nacional, null);

        when(unidadeConsumidoraRepository.findById("UC001")).thenReturn(Optional.of(ucResidencial));
        when(feriadoRepository.findByDataERegiao(LocalDate.of(2025, 11, 15), "fortaleza")).thenReturn(List.of(feriado));
        when(comandoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logValidacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ComandoValidateResponse response = comandoService.validarComando(request);

        // Assert
        assertFalse(response.getAprovado());
        assertEquals("Corte não permitido em feriado: Proclamação da República", response.getMotivo());
        assertNull(response.getPrazoExecucao());
    }

    @Test
    void corteEmUcEssencial_DeveBloquear() {
        // Arrange
        ComandoValidateRequest request = new ComandoValidateRequest();
        request.setUcId("UC002");
        request.setTipoUc("essencial");
        request.setTipoComando("corte");
        request.setTimestamp(LocalDateTime.of(2025, 11, 10, 10, 0, 0)); // Horário permitido
        request.setRegiao("fortaleza");
        request.setSolicitante("sistema_comercial");

        when(unidadeConsumidoraRepository.findById("UC002")).thenReturn(Optional.of(ucEssencial));
        when(comandoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logValidacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ComandoValidateResponse response = comandoService.validarComando(request);

        // Assert
        assertFalse(response.getAprovado());
        assertEquals("Corte não permitido para unidades consumidoras essenciais (Hospital Regional)", response.getMotivo());
        assertNull(response.getPrazoExecucao());
    }

    @Test
    void religacaoResidencial_DeveAprovarComPrazo24h() {
        // Arrange
        ComandoValidateRequest request = new ComandoValidateRequest();
        request.setUcId("UC001");
        request.setTipoUc("residencial");
        request.setTipoComando("religacao");
        request.setTimestamp(LocalDateTime.of(2025, 11, 10, 10, 0, 0));
        request.setRegiao("fortaleza");
        request.setSolicitante("sistema_comercial");

        LocalDateTime timestampEsperado = LocalDateTime.of(2025, 11, 11, 10, 0, 0); // +24 horas

        when(unidadeConsumidoraRepository.findById("UC001")).thenReturn(Optional.of(ucResidencial));
        when(comandoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(logValidacaoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ComandoValidateResponse response = comandoService.validarComando(request);

        // Assert
        assertTrue(response.getAprovado());
        assertNull(response.getMotivo());
        assertNotNull(response.getPrazoExecucao());
        assertEquals(timestampEsperado, response.getPrazoExecucao());
    }
}