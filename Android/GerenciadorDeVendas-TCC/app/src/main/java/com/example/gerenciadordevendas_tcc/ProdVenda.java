package com.example.gerenciadordevendas_tcc;

public class ProdVenda {

    private Venda venda;
    private Produto produto;
    private int qtd;
    private Double valor_unit;

    public ProdVenda () {

    }

    public ProdVenda (Venda _venda, Produto _produto, int _qtd, Double _valor_unit) {
        this.venda = _venda;
        this.produto = _produto;
        this.qtd = _qtd;
        this.valor_unit = _valor_unit;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public Double getValor_unit() {
        return valor_unit;
    }

    public void setValor_unit(Double valor_unit) {
        this.valor_unit = valor_unit;
    }
}

