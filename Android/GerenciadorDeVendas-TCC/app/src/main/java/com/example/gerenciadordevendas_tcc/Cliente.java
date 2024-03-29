package com.example.gerenciadordevendas_tcc;

public class Cliente {

    private int     id;
    private String  nome;
    private String  telefone;

    public Cliente (){

    }

    public Cliente (int _id, String _nome, String _telefone){
        this.id         = _id;
        this.nome       = _nome;
        this.telefone   = _telefone;
    }

    public Cliente (String _nome, String _telefone) {
        this.nome       = _nome;
        this.telefone   = _telefone;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}

