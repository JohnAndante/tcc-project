package com.example.tcc_gerenciadordevendas;

public class Estado {

    private int id;
    private String nome;
    private String uf;

    public Estado () {

    }

    public Estado (int _id, String _nome, String _uf) {
        this.id = _id;
        this.nome = _nome;
        this.uf = _uf;
    }

    public Estado (String _nome, String _uf) {
        this.nome = _nome;
        this.uf = _uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
}
