package com.example.tcc_gerenciadordevendas;

public class Venda {

    private int id;
    private String data;
    private Double valor;
    private Cliente cliente;
    private FormaPgto formaPgto;

    public Venda ()  {

    }

    public Venda (int _id, String _data, Double _valor, Cliente _cliente, FormaPgto _formaPgto) {
        this.id = _id;
        this.data = _data;
        this.valor = _valor;
        this.cliente = _cliente;
        this.formaPgto = _formaPgto;
    }

    public Venda (String _data, Double _valor, Cliente _cliente, FormaPgto _formaPgto) {
        this.data = _data;
        this.valor = _valor;
        this.cliente = _cliente;
        this.formaPgto = _formaPgto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public FormaPgto getFormaPgto() {
        return formaPgto;
    }

    public void setFormaPgto(FormaPgto formaPgto) {
        this.formaPgto = formaPgto;
    }
}
