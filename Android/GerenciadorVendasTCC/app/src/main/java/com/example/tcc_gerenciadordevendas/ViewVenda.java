package com.example.tcc_gerenciadordevendas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ViewVenda extends AppCompatActivity {

    private Button Exportar;
    private Button NovaVenda;
    private Button Voltar;

    private TextView textData;
    private TextView textHora;
    private TextView tvNomeCliente;
    private TextView tvTelefoneCliente;
    private TextView tvValorTotal;
    private TextView tvFormaPgto;
    private TextView tvQtdParcelas;
    private TextView tvValorParcela;

    private ListView lvProdutos;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idVenda;
    private Venda venda;
    private int posicao;
    private ArrayList<ProdVenda> arrayListProdVenda;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_venda);

        Cliente cliente = new Cliente();
        FormaPgto formaPgto = new FormaPgto();

    }
}
