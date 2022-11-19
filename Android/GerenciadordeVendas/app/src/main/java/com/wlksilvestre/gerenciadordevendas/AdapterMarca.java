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

public class AdapterMarca extends ArrayAdapter {

    private Context context;
    private ArrayList<Marca> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterMarca (Context context, int layoutId, ArrayList<Marca> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_marca, null);

        TextView tvDescMarca = (TextView) view.findViewById(R.id.tvDescMarca);
        tvDescMarca.setText(data.get(position).getDescricao());

        return view;
    }
}
