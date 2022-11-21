package com.wlksilvestre.gerenciadordevendas;

public class ClienteTelefone {

    private Cliente cliente;
    private Telefone telefone;

    public ClienteTelefone() {
    }

    public ClienteTelefone(Cliente cliente, Telefone telefone) {
        this.cliente = cliente;
        this.telefone = telefone;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }
}
