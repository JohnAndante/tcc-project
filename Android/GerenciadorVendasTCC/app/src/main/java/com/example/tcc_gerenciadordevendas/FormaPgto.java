package com.example.tcc_gerenciadordevendas;

public class FormaPgto {

    private int id;
    private String descricao;
    private int prazo;
    private int parcelavel;

    public FormaPgto () {

    }

    public FormaPgto (int _id, String _descricao, int _prazo, int _parcelavel) {
        this.id = _id;
        this.descricao = _descricao;
        this.prazo = _prazo;
        this.parcelavel = _parcelavel;
    }

    public FormaPgto (String _descricao, int _prazo, int _parcelavel) {
        this.descricao = _descricao;
        this.prazo = _prazo;
        this.parcelavel = _parcelavel;
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

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public int getParcelavel() {
        return parcelavel;
    }

    public void setParcelavel(int parcelavel) {
        this.parcelavel = parcelavel;
    }
}
