package com.example.tcc_gerenciadordevendas;

public class Produto {

    private int id;
    private String descricao;
    private double valor;
    private Linha linha;

    public Produto () {

    }

    public Produto (int _id, String _descricao, double _valor, Linha _linha) {
        this.id =_id;
        this.descricao = _descricao;
        this.valor = _valor;
        this.linha = _linha;
    }

    public Produto (String _descricao, double _valor, Linha _linha) {
        this.descricao = _descricao;
        this.valor = _valor;
        this.linha = _linha;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Linha getLinha() {
        return linha;
    }

    public void setLinha(Linha linha) {
        this.linha = linha;
    }
}
