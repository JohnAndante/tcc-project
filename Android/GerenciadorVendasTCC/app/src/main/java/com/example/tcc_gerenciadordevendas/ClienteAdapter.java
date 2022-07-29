package com.example.tcc_gerenciadordevendas;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ClienteAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Cliente> data;

    public ClienteAdapter (Context context, int layoutId, ArrayList<Cliente> list) {
        super(context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    @NonNull
    @Nullable
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_cliente, null);

        TextView tvNome = (TextView) view.findViewById(R.id.tvNomeCliente);
        TextView tvTelefone = (TextView) view.findViewById(R.id.tvTelefoneCliente);

        tvNome.setText(data.get(position).getNome());
        tvTelefone.setText(data.get(position).getTelefone());

        Log.i("INFO DB",  String.valueOf(data.get(position).getId()));
        Log.i("INFO DB",  String.valueOf(data.get(position).getNome()));
        Log.i("INFO DB",  String.valueOf(data.get(position).getTelefone()));

        Log.i("INFO LYT", (String) tvNome.getText());
        Log.i("INFO LYT", (String) tvTelefone.getText());

        return view;
    }
}
