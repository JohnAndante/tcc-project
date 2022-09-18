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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AdapterCategoria extends ArrayAdapter {

    private Context context;
    private ArrayList<Categoria> data;

    BancoDadosCliente db = new BancoDadosCliente(null);

    public AdapterCategoria (Context context, int layoutId, ArrayList<Categoria> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_categoria, null);

        TextView tvDescCategoria = (TextView) view.findViewById(R.id.tvDescCategoria);
        tvDescCategoria.setText(data.get(position).getDescricao());

        return view;
    }
}
