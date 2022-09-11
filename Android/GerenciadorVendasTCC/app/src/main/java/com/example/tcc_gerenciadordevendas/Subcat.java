package com.example.tcc_gerenciadordevendas;

public class Subcat {

    private int id;
    private String descricao;
    private int idCategoria;

    public Subcat () {

    }

    public Subcat (int _id, String _descricao, int _idCategoria) {
        this.id = _id;
        this.descricao = _descricao;
        this.idCategoria = _idCategoria;
    }

    public Subcat (String _descricao, int _idCategoria) {
        this.descricao = _descricao;
        this.idCategoria = _idCategoria;
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

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}
