package com.example.gerenciadordevendas_tcc;

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

public class AdapterFormaPgto extends ArrayAdapter {
    private Context context;
    private ArrayList<FormaPgto> data;


    public AdapterFormaPgto (Context context, int layoutId, ArrayList<FormaPgto> list) {
        super (context, layoutId, list);
        this.context = context;
        this.data = list;
    }

    @NonNull
    @Override
    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_forma_pgto, null);

        TextView tvNomeFormaPgto = (TextView) view.findViewById(R.id.tvNomeFormaPgto);
        tvNomeFormaPgto.setText(data.get(position).getDescricao());

        return view;
    }
}
