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

public class AdapterLinha extends ArrayAdapter {

    private Context context;
    private ArrayList<Linha> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterLinha (Context context, int layoutId, ArrayList<Linha> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_linha, null);

        TextView tvDescLInha = (TextView) view.findViewById(R.id.tvDescLinha);
        tvDescLInha.setText(data.get(position).getDescricao());

        return view;
    }
}
