package com.wlksilvestre.gerenciadordevendas;

public class VendaQtd {

    private Venda venda;
    private int qtd;

    public VendaQtd () {

    }

    public VendaQtd (Venda _venda, int _qtd) {
        this.venda = _venda;
        this.qtd = _qtd;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }
}
