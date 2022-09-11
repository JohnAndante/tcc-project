package com.example.tcc_gerenciadordevendas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ViewProduto extends AppCompatActivity {

    private Button Editar;
    private Button Voltar;

    private TextView textDesc;
    private TextView textValor;
    private TextView textMarca;
    private TextView textLinha;
    private TextView textCategoria;

    BancoDadosCliente db = new BancoDadosCliente(this);

    private int idProduto;


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static final int ALTERAR_PRODUTO = 102;
    public static final int RESULT_ALT_PRODUTO = 202;

    private boolean produto_alterado;

    private Produto produto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_produto);

        Marca marca = new Marca();
        Linha linha = new Linha();
        Categoria categoria = new Categoria();
        Subcat subcat = new Subcat();
        ProdSubcat prodSubcat = new ProdSubcat();

        initTexts();
        initButtons();
        initButtonsOnClick();

        Intent intent = getIntent();
        idProduto = 0;
        produto_alterado = false;

        if (intent.hasExtra("ID")) {
            idProduto = intent.getIntExtra("ID", 0);

            produto = db.selectProduto(idProduto);
            linha = produto.getLinha();
            marca = linha.getMarca();
            prodSubcat = db.selectFirstProdSubcatByProd(produto);
            subcat = prodSubcat.getSubcat();
            categoria = subcat.getCategoria();

            textDesc.setText(produto.getDescricao());
            textValor.setText("R$ " + df.format(produto.getValor()));
            textMarca.setText(marca.getDescricao());
            textLinha.setText(linha.getDescricao());
            textCategoria.setText(categoria.getDescricao());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Marca marca = new Marca();
        Linha linha = new Linha();
        Categoria categoria = new Categoria();
        Subcat subcat = new Subcat();
        ProdSubcat prodSubcat = new ProdSubcat();

        // Alterando dados
        if ((requestCode == ALTERAR_PRODUTO) && (resultCode == RESULT_OK)) {
            produto_alterado = true;

            produto = db.selectProduto(idProduto);
            linha = produto.getLinha();
            marca = linha.getMarca();
            prodSubcat = db.selectFirstProdSubcatByProd(produto);
            subcat = prodSubcat.getSubcat();
            categoria = subcat.getCategoria();

            textDesc.setText(produto.getDescricao());
            textValor.setText("R$ " + df.format(produto.getValor()));
            textMarca.setText(marca.getDescricao());
            textLinha.setText(linha.getDescricao());
            textCategoria.setText(categoria.getDescricao());

        }
    }

    private void initTexts () {
        textDesc        = (TextView) findViewById(R.id.tvDescricaoProduto);
        textValor       = (TextView) findViewById(R.id.tvValorProduto);
        textMarca       = (TextView) findViewById(R.id.tvMarcaProduto);
        textLinha       = (TextView) findViewById(R.id.tvLinhaProduto);
        textCategoria   = (TextView) findViewById(R.id.tvCategoriaProduto);
    }

    private void initButtons () {
        Editar = (Button) findViewById(R.id.btViewProdutoEditar);
        Voltar = (Button) findViewById(R.id.btViewProdutoVoltar);
    }

    private void initButtonsOnClick () {
        Editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewProduto.this, AddProduto.class);
                Bundle bundle = new Bundle();

                bundle.putInt("ID", idProduto);
                intent.putExtras(bundle);
                startActivityForResult(intent, ALTERAR_PRODUTO);
            }
        });

        Voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (produto_alterado) {
                    Intent intent2 = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID", produto.getId());
                    intent2.putExtras(bundle);

                    setResult(RESULT_ALT_PRODUTO, intent2);
                } else {
                    setResult(RESULT_CANCELED);
                }
                finish();
            }
        });
    }
}
