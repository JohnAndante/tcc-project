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

public class AdapterEstado extends ArrayAdapter {
    private Context context;
    private ArrayList<Estado> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterEstado (Context context, int layoutId, ArrayList<Estado> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_estado, null);

        TextView tvNomeEstado = (TextView) view.findViewById(R.id.tvNomeEstado);
        tvNomeEstado.setText(data.get(position).getNome());

        return view;
    }
}
