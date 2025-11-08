package com.reccurt.reccurt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comando")
public class Comando {

    public enum TipoComando {
        CORTE, RELIGACAO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "uc_id", nullable = false)
    private UnidadeConsumidora unidadeConsumidora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComando tipoComando;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String solicitante;

    @Column
    private Boolean aprovado;

    @Column
    private String motivo;

    @Column
    private LocalDateTime prazoExecucao;

    public Comando() {}

    public Comando(UnidadeConsumidora unidadeConsumidora, TipoComando tipoComando,
                   LocalDateTime timestamp, String solicitante) {
        this.unidadeConsumidora = unidadeConsumidora;
        this.tipoComando = tipoComando;
        this.timestamp = timestamp;
        this.solicitante = solicitante;
        this.aprovado = null;
        this.motivo = null;
    }


    public Long getId() {
        return id;
    }

    public UnidadeConsumidora getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(UnidadeConsumidora unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    public TipoComando getTipoComando() {
        return tipoComando;
    }

    public void setTipoComando(TipoComando tipoComando) {
        this.tipoComando = tipoComando;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public Boolean getAprovado() {return this.aprovado;}

    public void setAprovado(Boolean aprovado) { this.aprovado = aprovado;}

    public String getMotivo() {return this.motivo;}

    public void setMotivo(String motivo) {this.motivo = motivo;}

    public LocalDateTime getPrazoExecucao() {return this.prazoExecucao;}

    public  void setPrazoExecucao(LocalDateTime prazoExecucao) {this.prazoExecucao = prazoExecucao;}
}

