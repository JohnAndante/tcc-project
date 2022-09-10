package com.example.tcc_gerenciadordevendas;

public class Marca {

    private int id;
    private String descricao;

    public Marca () {

    }

    public Marca (int _id, String _desc) {
        this.id = _id;
        this.descricao = _desc;
    }

    public Marca (String _desc) {
        this.descricao = _desc;
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
}
