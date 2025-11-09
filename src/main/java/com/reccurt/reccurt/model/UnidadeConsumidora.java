package com.reccurt.reccurt.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "unidade_consumidora")
public class UnidadeConsumidora implements Serializable {

    public enum Tipo {
        residencial, comercial, industrial, essencial, emergencia
    }

    @Id
    @Column(name = "id", length = 20)
    private String id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private Tipo tipo;

    @Column(name = "regiao", nullable = false)
    private String regiao;

    public UnidadeConsumidora() {}

    public UnidadeConsumidora(String id, String nome, Tipo tipo, String regiao) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.regiao = regiao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Tipo getTipo() {
        return this.tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }
}
