package com.reccurt.reccurt.dto;

import java.time.LocalDate;

public class FeriadoCreateResponse {
    private Long id;
    private LocalDate data;
    private String nome;
    private String tipo;

    public FeriadoCreateResponse() {}

    public FeriadoCreateResponse(Long id, LocalDate data, String nome, String tipo) {
        this.id = id;
        this.data = data;
        this.nome = nome;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}