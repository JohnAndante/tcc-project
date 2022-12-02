package com.wlksilvestre.gerenciadordevendas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterProdutoVenda extends ArrayAdapter {

    private final Context context;
    private final ArrayList<ProdVenda> data;

    public AdapterProdutoVenda (Context _context, int layoutId, ArrayList<ProdVenda> _data) {
        super(_context, layoutId, _data);
        this.context = _context;
        this.data = _data;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_produto_venda, null);

        TextView textDescProduto = view.findViewById(R.id.tvDescProdVenda);
        TextView textValorProduto = view.findViewById(R.id.tvValorProdVenda);
        TextView textQtdProduto = view.findViewById(R.id.tvQtdProdVenda);

        textDescProduto.setText(data.get(position).getProduto().getDescricao());
        textValorProduto.setText(MaskEditUtil.doubleToMoneyValue(data.get(position).getValor_unit()));
        textQtdProduto.setText(String.valueOf(data.get(position).getQtd()));

        return view;
    }
}
