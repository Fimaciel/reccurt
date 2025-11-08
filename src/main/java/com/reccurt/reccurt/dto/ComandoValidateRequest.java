package com.reccurt.reccurt.dto;

import java.time.LocalDateTime;

public class ComandoValidateRequest {
    private String ucId;
    private String tipoUc;
    private String tipoComando;
    private LocalDateTime timestamp;
    private String regiao;
    private String solicitante;

    public ComandoValidateRequest() {}

    public ComandoValidateRequest(String ucId, String tipoUc, String tipoComando,
                                  LocalDateTime timestamp, String regiao, String solicitante) {
        this.ucId = ucId;
        this.tipoUc = tipoUc;
        this.tipoComando = tipoComando;
        this.timestamp = timestamp;
        this.regiao = regiao;
        this.solicitante = solicitante;
    }

    public String getUcId() { return ucId; }
    public void setUcId(String ucId) { this.ucId = ucId; }

    public String getTipoUc() { return tipoUc; }
    public void setTipoUc(String tipoUc) { this.tipoUc = tipoUc; }

    public String getTipoComando() { return tipoComando; }
    public void setTipoComando(String tipoComando) { this.tipoComando = tipoComando; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }

    public String getSolicitante() { return solicitante; }
    public void setSolicitante(String solicitante) { this.solicitante = solicitante; }
}