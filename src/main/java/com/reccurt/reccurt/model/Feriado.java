package com.reccurt.reccurt.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "feriado")
public class Feriado {

    public enum TipoFeriado {
        nacional, regional
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false, length = 100)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoFeriado tipo;


    @Column
    private String regiao;

    public Feriado() {}

    public Feriado(LocalDate data, String nome, TipoFeriado tipo, String regiao) {
        this.data = data;
        this.nome = nome;
        this.tipo = tipo;
        this.regiao = regiao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoFeriado getTipo() {
        return tipo;
    }

    public void setTipo(TipoFeriado tipo) {
        this.tipo = tipo;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }
}
