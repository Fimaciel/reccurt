package com.reccurt.reccurt.dto;

import java.time.LocalDateTime;

public class ComandoValidateResponse {
    private Boolean aprovado;
    private String motivo;
    private LocalDateTime prazoExecucao;

    public ComandoValidateResponse() {}

    public ComandoValidateResponse(Boolean aprovado, String motivo, LocalDateTime prazoExecucao) {
        this.aprovado = aprovado;
        this.motivo = motivo;
        this.prazoExecucao = prazoExecucao;
    }

    public Boolean getAprovado() { return aprovado; }
    public void setAprovado(Boolean aprovado) { this.aprovado = aprovado; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public LocalDateTime getPrazoExecucao() { return prazoExecucao; }
    public void setPrazoExecucao(LocalDateTime prazoExecucao) { this.prazoExecucao = prazoExecucao; }
}