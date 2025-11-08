package com.reccurt.reccurt.dto;

import java.time.LocalDate;

public class FeriadoCreateRequest {
    private LocalDate data;
    private String nome;
    private String tipo;
    private String regiao;

    public FeriadoCreateRequest() {}

    public FeriadoCreateRequest(LocalDate data, String nome, String tipo, String regiao) {
        this.data = data;
        this.nome = nome;
        this.tipo = tipo;
        this.regiao = regiao;
    }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getRegiao() { return regiao; }
    public void setRegiao(String regiao) { this.regiao = regiao; }
}