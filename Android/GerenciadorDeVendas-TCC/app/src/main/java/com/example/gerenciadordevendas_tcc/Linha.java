package com.example.gerenciadordevendas_tcc;

public class Linha {
    private int id;
    private String descricao;
    private Marca marca;

    public Linha () {

    }

    public Linha (int _id, String _descricao, Marca _marca) {
        this.id = _id;
        this.descricao = _descricao;
        this.marca = _marca;
    }

    public Linha (String _descricao, Marca _marca) {
        this.descricao = _descricao;
        this.marca = _marca;
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

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }
}
