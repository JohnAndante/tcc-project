package com.example.tcc_gerenciadordevendas;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class AdapterVenda extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Venda> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterVenda (Context _context, int layout)
}
