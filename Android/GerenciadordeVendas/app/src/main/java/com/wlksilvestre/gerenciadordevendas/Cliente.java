package com.wlksilvestre.gerenciadordevendas;

public class Cliente {

    private int     id;
    private String  nome;
    private String  uid;
    private String  dataUpdate;

    public Cliente (){
    }

    public Cliente (int _id, String _nome, String _uid, String _dataUpdate){
        this.id         = _id;
        this.nome       = _nome;
        this.uid        = _uid;
        this.dataUpdate = _dataUpdate;
    }

    public Cliente (String _nome, String _uid, String _dataUpdate) {
        this.nome       = _nome;
        this.uid        = _uid;
        this.dataUpdate = _dataUpdate;
    }

    public Cliente (String _nome) {
        this.nome = _nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(String dataUpdate) {
        this.dataUpdate = dataUpdate;
    }
}

