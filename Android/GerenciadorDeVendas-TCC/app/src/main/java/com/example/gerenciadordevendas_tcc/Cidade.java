package com.example.gerenciadordevendas_tcc;

public class Cidade {

    private int id;
    private String nome;
    private Estado estado;

    public Cidade () {}

    public Cidade (int _id, String _nome, Estado _estado) {
        this.id = _id;
        this.nome = _nome;
        this.estado = _estado;
    }

    public Cidade (String _nome, Estado _estado) {
        this.nome = _nome;
        this.estado = _estado;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
