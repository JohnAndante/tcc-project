package com.wlksilvestre.gerenciadordevendas;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdapterProdutoVenda2 extends ArrayAdapter {

    private final Context context;
    private final ArrayList<ProdVenda> data;

    public AdapterProdutoVenda2 (Context _context, int layoutId, ArrayList<ProdVenda> _data) {
        super(_context, layoutId, _data);
        this.context = _context;
        this.data = _data;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_produto_venda_2, null);

        ImageView imageIconeProduto = view.findViewById(R.id.ivIconeProduto);
        TextView textNomeProduto = view.findViewById(R.id.tvNomeProduto);
        TextView textValorProduto = view.findViewById(R.id.tvValorProduto);
        TextView textQtdUnitProduto = view.findViewById(R.id.tvQtdUnitProdVenda);

        // Definir bg conforme a categoria do produto
        //imageIconeProduto.setBackground();
        textNomeProduto.setText(data.get(position).getProduto().getDescricao());
        textValorProduto.setText(MaskEditUtil.doubleToMoneyValue(data.get(position).getValor_unit()));
        textQtdUnitProduto.setText(String.valueOf(data.get(position).getQtd()));

        return view;
    }
}
