package com.wlksilvestre.gerenciadordevendas;

public class Usuario {

    private int id;
    private String nome;
    private String email;
    private String telefone;
    private String uid;

    public Usuario() {
    }

    public Usuario(String nome, String email, String telefone, String uid) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.uid = uid;
    }

    public Usuario(int id, String nome, String email, String telefone, String uid) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
