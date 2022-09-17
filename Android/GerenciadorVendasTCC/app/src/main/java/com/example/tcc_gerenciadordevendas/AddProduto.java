package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddProduto extends AppCompatActivity {

    private LinearLayout llButtons;
    private Button Salvar;
    private Button Cancelar;

    private EditText editTextDescricao;
    private EditText editTextValor;
    private TextView textMarca;
    private TextView textLinha;
    private TextView textCategoria;

    public final int deviceHeight   = Resources.getSystem().getDisplayMetrics().heightPixels;
    public final int deviceWidth    = Resources.getSystem().getDisplayMetrics().widthPixels;

    private Boolean buttonsVisibility = true;
    private Boolean isEditTextFocused = false;

    BancoDadosCliente db = new BancoDadosCliente(this);

    int id_produto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_produto);

        llButtons = (LinearLayout) findViewById(R.id.llBotoesProduto);

        initButtonsCfg();
        initButtonsOnClick();
        initEditTexts();
        changellButtons();
        initEditOnFocus();

        Intent intent = getIntent();

        if (intent.hasExtra("ID")) {
            Salvar.setText("Alterar");
            id_produto = intent.getIntExtra("ID", 0);

            Produto p = db.selectProduto(id_produto);
            Linha l = db.selectLinha(p.getLinha().getId());
            Marca m = db.selectMarca(l.getMarca().getId());
            List<ProdSubcat> ps = db.listProdSubcatsByProduto(p);
            Categoria c = db.selectFirstProdSubcatByProd(p).getSubcat().getCategoria();

            editTextDescricao.setText(p.getDescricao());
            editTextValor.setText(p.getValor());



        }
    }
}
