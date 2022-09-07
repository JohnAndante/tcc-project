package com.example.tcc_gerenciadordevendas;

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

public class AdapterCidade extends ArrayAdapter {
    private Context context;
    private ArrayList<Cidade> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterCidade(Context context, int layoutId, ArrayList<Cidade> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_cidade, null);

        TextView tvNomeCidade = (TextView) view.findViewById(R.id.tvNomeCidade);
        tvNomeCidade.setText(data.get(position).getNome());

        return view;
    }
}
