package com.exemplo.model;

import java.util.Date;

public class Contato {
    private String nome;
    private String telefone;
    private String descricao;
    private Date dataNascimento;

    // Construtor padrão
    public Contato() {}

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
