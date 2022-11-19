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

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterProduto extends ArrayAdapter {

    private final Context context;
    private final ArrayList<Produto> data;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterProduto (Context _context, int layoutId, ArrayList<Produto> _data) {
        super (_context, layoutId, _data);
        this.context = _context;
        this.data = _data;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_produto, null);

        TextView tvNomeProduto = view.findViewById(R.id.tvNomeProduto);
        TextView tvValorProduto = view.findViewById(R.id.tvValorProduto);
        // Adicionar aqui a alteração de cor do ícone

        tvNomeProduto.setText(data.get(position).getDescricao());
        tvValorProduto.setText("R$ " + MaskEditUtil.doubleToMoneyValue(data.get(position).getValor()));

        return view;
    }
}
