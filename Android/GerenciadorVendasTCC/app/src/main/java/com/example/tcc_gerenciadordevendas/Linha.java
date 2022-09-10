package com.example.tcc_gerenciadordevendas;

public class Linha {
    private int id;
    private String descricao;
    private int idMarca;

    public Linha () {

    }

    public Linha (int _id, String _descricao, int _idMarca) {
        this.id = _id;
        this.descricao = _descricao;
        this.idMarca = _idMarca;
    }

    public Linha (String _descricao, int _idMarca) {
        this.descricao = _descricao;
        this.idMarca = _idMarca;
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

    public int getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(int idMarca) {
        this.idMarca = idMarca;
    }
}
