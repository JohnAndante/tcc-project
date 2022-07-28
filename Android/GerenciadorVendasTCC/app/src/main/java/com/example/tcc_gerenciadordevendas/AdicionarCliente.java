package com.example.tcc_gerenciadordevendas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AdicionarCliente extends AppCompatActivity {

    private Button Salvar, Cancelar;
    private EditText Nome, Telefone;
    int posicao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);

        initButtonsCfg();
        initButtonsOnclick();

        Intent intent = getIntent();

        if (intent.hasExtra("posicao")) {
            posicao = intent.getIntExtra("posicao", 0);
            Nome.setText(intent.getStringExtra("nome"));
            Telefone.setText(intent.getStringExtra("telefone"));
        }


    }

    private void initButtonsCfg(){
        Salvar = (Button) findViewById(R.id.btClienteSalvar);
        Cancelar = (Button) findViewById(R.id.btClienteCancelar);

        Nome = (EditText) findViewById(R.id.editNomeCliente);
        Telefone = (EditText) findViewById(R.id.editTelefoneCliente);
    }

    private void initButtonsOnclick(){
        Salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                if (posicao > 0)
                    bundle.putInt("posicao", posicao);

                bundle.putString("nome", Nome.getText().toString());
                bundle.putString("telefone", Telefone.getText().toString());

                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}