package com.example.tcc_gerenciadordevendas;

import android.database.sqlite.SQLiteDatabase;

public class Telefone {

    int     id;
    String  num;
    Cliente cliente;

    public Telefone () {

    }

    public Telefone (String _num, Cliente _cliente) {
        this.num        = _num;
        this.cliente    = _cliente;
    }

    public Telefone (int _id, String _num, Cliente _cliente) {
        this.id         = _id;
        this.num        = _num;
        this.cliente    = _cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
