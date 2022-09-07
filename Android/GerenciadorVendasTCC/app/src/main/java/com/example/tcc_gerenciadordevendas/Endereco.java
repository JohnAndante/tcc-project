package com.example.tcc_gerenciadordevendas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Endereco {

    private int id;
    private String num;
    private String rua;
    private String bairro;
    private String comp;
    private String ref;
    private Cidade cidade;
    private Cliente cliente;

    public Endereco () {

    }

    public Endereco (@Nullable int _id,
                     @Nullable String _num,
                     @Nullable String _rua,
                     @Nullable String _bairro,
                     @Nullable String _comp,
                     @Nullable String _ref,
                     @Nullable Cidade _cidade,
                     Cliente _cliente) {
        this.id = _id;
        this.num = _num;
        this.rua = _rua;
        this.bairro = _bairro;
        this.comp = _comp;
        this.ref = _ref;
        this.cidade = _cidade;
        this.cliente = _cliente;
    }

    public Endereco (@Nullable String _num,
                     @Nullable String _rua,
                     @Nullable String _bairro,
                     @Nullable String _comp,
                     @Nullable String _ref,
                     @Nullable Cidade _cidade,
                     Cliente _cliente) {
        this.num = _num;
        this.rua = _rua;
        this.bairro = _bairro;
        this.comp = _comp;
        this.ref = _ref;
        this.cidade = _cidade;
        this.cliente = _cliente;
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

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}
