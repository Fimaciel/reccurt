package com.reccurt.reccurt.dto;

import java.time.LocalDateTime;

public class ComandoHistoryResponse {
    private Integer total;
    private java.util.List<ComandoHistoryItem> comandos;

    public ComandoHistoryResponse() {}

    public ComandoHistoryResponse(Integer total, java.util.List<ComandoHistoryItem> comandos) {
        this.total = total;
        this.comandos = comandos;
    }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public java.util.List<ComandoHistoryItem> getComandos() { return comandos; }
    public void setComandos(java.util.List<ComandoHistoryItem> comandos) { this.comandos = comandos; }

    public static class ComandoHistoryItem {
        private Long id;
        private String ucId;
        private String tipoComando;
        private LocalDateTime timestamp;
        private Boolean aprovado;
        private String motivo;

        public ComandoHistoryItem() {}

        public ComandoHistoryItem(Long id, String ucId, String tipoComando,
                                  LocalDateTime timestamp, Boolean aprovado, String motivo) {
            this.id = id;
            this.ucId = ucId;
            this.tipoComando = tipoComando;
            this.timestamp = timestamp;
            this.aprovado = aprovado;
            this.motivo = motivo;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getUcId() { return ucId; }
        public void setUcId(String ucId) { this.ucId = ucId; }

        public String getTipoComando() { return tipoComando; }
        public void setTipoComando(String tipoComando) { this.tipoComando = tipoComando; }

        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

        public Boolean getAprovado() { return aprovado; }
        public void setAprovado(Boolean aprovado) { this.aprovado = aprovado; }

        public String getMotivo() { return motivo; }
        public void setMotivo(String motivo) { this.motivo = motivo; }
    }
}