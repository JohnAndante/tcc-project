package com.wlksilvestre.gerenciadordevendas;

public class ProdSubcat {

    private Produto produto;
    private Subcat subcat;

    public ProdSubcat () {

    }

    public ProdSubcat (Produto _produto, Subcat _subcat) {
        this.produto = _produto;
        this.subcat = _subcat;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Subcat getSubcat() {
        return subcat;
    }

    public void setSubcat(Subcat subcat) {
        this.subcat = subcat;
    }
}
