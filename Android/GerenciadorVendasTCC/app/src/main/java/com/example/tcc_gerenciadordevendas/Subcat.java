package com.example.tcc_gerenciadordevendas;

public class Subcat {

    private int id;
    private String descricao;
    private Categoria categoria;

    public Subcat () {

    }

    public Subcat (int _id, String _descricao, Categoria _categoria) {
        this.id = _id;
        this.descricao = _descricao;
        this.categoria = _categoria;
    }

    public Subcat (String _descricao, Categoria _categoria) {
        this.descricao = _descricao;
        this.categoria = _categoria;
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
