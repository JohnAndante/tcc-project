package com.example.tcc_gerenciadordevendas;

import androidx.annotation.Nullable;

public class Categoria {

    private int id;
    private String descricao;
    private String cor;

    public Categoria () {

    }

    public Categoria (int _id, String _descricao, @Nullable String _cor) {
        this.id = _id;
        this.descricao = _descricao;
        this.cor = _cor;
    }

    public Categoria (String _descricao, @Nullable String _cor) {
        this.descricao = _descricao;
        this.cor = _cor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
