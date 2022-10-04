package com.example.tcc_gerenciadordevendas;

public class Pgto {

    private int id;
    private Cliente cliente;
    private FormaPgto formaPgto;
    private Double valor;
    private String data;
    private int parcelas;

    public Pgto () {

    }

    public Pgto (int _id, Cliente _cliente, FormaPgto _formaPgto, Double _valor, String _data, int _parcelas) {
        this.id = _id;
        this.cliente = _cliente;
        this.formaPgto = _formaPgto;
        this.valor = _valor;
        this.data = _data;
        this.parcelas = _parcelas;
    }

    public Pgto (Cliente _cliente, FormaPgto _formaPgto, Double _valor, String _data, int _parcelas) {
        this.cliente = _cliente;
        this.formaPgto = _formaPgto;
        this.valor = _valor;
        this.data = _data;
        this.parcelas = _parcelas;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public Cliente getCliente () {
        return cliente;
    }

    public void setCliente (Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPgto getFormaPgto () {
        return formaPgto;
    }

    public void setFormaPgto (FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }

    public Double getValor () {
        return valor;
    }

    public void setValor (Double valor) {
        this.valor = valor;
    }

    public String getData () {
        return data;
    }

    public void setData (String data) {
        this.data = data;
    }

    public int getParcelas () {
        return parcelas;
    }

    public void setParcelas (int parcelas) {
        this.parcelas = parcelas;
    }
}
