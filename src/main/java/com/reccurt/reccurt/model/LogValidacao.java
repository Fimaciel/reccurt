package com.reccurt.reccurt.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_validacao")
public class LogValidacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comando_id", nullable = false)
    private Comando comando;

    @Column(nullable = false)
    private boolean aprovado;

    @Column(length = 255)
    private String motivo;

    @Column(nullable = false)
    private LocalDateTime dataHoraValidacao;

    @Column(nullable = false)
    private String usuario;

    public LogValidacao() {}

    public LogValidacao(Comando comando, boolean aprovado, String motivo,
                        LocalDateTime dataHoraValidacao, String usuario) {
        this.comando = comando;
        this.aprovado = aprovado;
        this.motivo = motivo;
        this.dataHoraValidacao = dataHoraValidacao;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public Comando getComando() {
        return comando;
    }

    public void setComando(Comando comando) {
        this.comando = comando;
    }

    public boolean isAprovado() {
        return aprovado;
    }

    public void setAprovado(boolean aprovado) {
        this.aprovado = aprovado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataHoraValidacao() {
        return dataHoraValidacao;
    }

    public void setDataHoraValidacao(LocalDateTime dataHoraValidacao) {
        this.dataHoraValidacao = dataHoraValidacao;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
