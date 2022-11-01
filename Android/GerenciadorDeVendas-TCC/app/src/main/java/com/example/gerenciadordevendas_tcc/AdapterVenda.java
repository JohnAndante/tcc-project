package com.example.gerenciadordevendas_tcc;

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

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdapterVenda extends ArrayAdapter {

    private Context context = null;
    private final ArrayList<Venda> data;

    BancoDadosCliente db = new BancoDadosCliente(context);

    public AdapterVenda (Context _context, int _layout, ArrayList<Venda> _data) {
        super (_context, _layout, _data);
        this.context = _context;
        this.data = _data;
    }

    public View getView (int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.card_venda, null);

        TextView tvNomeCliente = view.findViewById(R.id.tvVendaNomeCliente);
        TextView tvVendaQtdProdutos = view.findViewById(R.id.tvVendaQtdProdutos);
        TextView tvVendaValor = view.findViewById(R.id.tvVendaValor);

        tvNomeCliente.setText(data.get(position).getCliente().getNome());
        tvVendaQtdProdutos.setText(" ? " + " Produtos");
        tvVendaValor.setText("R$ " + data.get(position).getValor().toString());

        return view;
    }
}
