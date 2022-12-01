package com.wlksilvestre.gerenciadordevendas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SyncData extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireDB;
    private DocumentReference docUsuarios;
    private Usuario usuarioLogado;

    private final BancoDadosCliente db = new BancoDadosCliente(SyncData.this);

    private int CODE_USER = 0;

    private TextView tvStatusUsuario;

    @Override
    public void onStart() {
        super.onStart();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_data);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();
        usuarioLogado = new Usuario();

        initTextViews();

        CODE_USER = verificaUsuariosDB();

        AsyncRecuperaUsuarioFireDB recuperaUsuarioFireDB = new AsyncRecuperaUsuarioFireDB();
        recuperaUsuarioFireDB.execute(CODE_USER);
    }

    private int verificaUsuariosDB() {
        usuarioLogado = new Usuario();
        currentUser = mAuth.getCurrentUser();

        if (db.selectCountUsuarios() > 0) {
            try {
                usuarioLogado = db.selectUsuarioByUID(currentUser.getUid());
                Log.e("INFO verificaUsuariosDB", "usuario recuperado do banco de dados - " + usuarioLogado.getNome());

                if (db.selectCountUsuarios() > 1)
                    return 3;
                return 2;
            } catch (Exception e) {
                Log.e("INFO verificaUsuariosDB - Recupera usuario atual", e.getMessage());
            }

            if (usuarioLogado == null) {
                Log.e("INFO verificaUsuariosDB", "usuario nulo");
                Log.e("INFO verificaUsuariosDB", "chamando recuperaUsuarioFireDB");
                return 4;
            }
        } else {
            Log.e("INFO verificaUsuariosDB", "Não existem usuarios cadastrados no DB");
            return 1;
        }

        return 1;
    }

    private class AsyncRecuperaUsuarioFireDB extends AsyncTask<Integer, String, String> {

        private String msg;
        private int CODE_FIN = 0;

        @Override
        protected String doInBackground(Integer... _code) {
            publishProgress("Verificando informações...");
            Log.e("INFO", _code[0] + " // " + CODE_USER);

            if (CODE_USER == 2) {
                msg = "Cadastro do usuário validado.";
                Log.e("INFO", "Cadastro Validado... 2");
                return msg;
            }

            try {
                currentUser = mAuth.getCurrentUser();
                fireDB = FirebaseFirestore.getInstance();
                usuarioLogado = new Usuario();

                publishProgress("Recuperando dados do usuário...");

                DocumentReference documentReference = fireDB.collection("usuarios").document(currentUser.getUid());
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {

                            usuarioLogado.setId(value.getLong("id").intValue());
                            usuarioLogado.setNome(value.getString("nome"));
                            usuarioLogado.setEmail(value.getString("email"));
                            usuarioLogado.setTelefone(value.getString("telefone"));
                            usuarioLogado.setUid(value.getString("uid"));

                        } else {
                            Log.e("INFO ERRO RECUPERAR DADOS", error.getMessage());
                            publishProgress("Erro ao recuperar dados...");
                            msg = "0";
                        }
                    }
                });
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage());
            }

            if (msg == "0") {
                publishProgress("Erro ao recuperar dados.");
                Log.e("INFO", "Erro ao recuperar dados... 0");
                return msg;
            }
            publishProgress("msg");

            if (CODE_USER == 3 ) {
                // Tem o usuário desejado e outros usuários
                // irá deletar todos os outros usuários

                publishProgress("Limpando dados desnecessários... 3");

                List<Usuario> usuariosDB = new ArrayList<>();

                usuariosDB = db.listUsuarios();

                for (Usuario userdb : usuariosDB) {
                    //if (!userdb.getUid().equals(usuarioLogado.getUid()))
                        db.deleteUsuario(userdb, null);
                        Log.e("INFO", "deletando usuario " + userdb.getNome() + " ... 3");
                }
            }

            if (CODE_USER == 1) {
                // Não tem nenhum dado

                db.clearUsuarios();
                publishProgress("Gravando dados recuperados...");
                Log.e("INFO", "Gravando dados recuperados... 1");
                db.addUsuario(usuarioLogado);

            }

            if (_code[0] == '4') {
                // Não tem nenhum dado

                publishProgress("Gravando dados recuperados...");
                Log.e("INFO", "Gravando dados recuperados... 2");

                //db.addUsuario(usuarioLogado);
            }

            publishProgress("Verificação e cadastros finalizados.");

            msg = "10";
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            if (msg.equals("10")) {
                // deu bom bora pro app
                if (CODE_USER != 2 ) {
                    Log.e("INFO", "Vai gravar os dados do mlk...");
                    db.addUsuario(usuarioLogado);
                    Log.e("INFO", "Gravou mano ggg...");
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tvStatusUsuario.setText(values[0]);
        }
    }

    private void cleanAllTables () {
    }

    private void arecuperaUsuarioFireDB () {
        currentUser = mAuth.getCurrentUser();
        fireDB = FirebaseFirestore.getInstance();

        DocumentReference docUsuarios = fireDB.collection("usuarios").document(currentUser.getUid());
        docUsuarios.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.e("INFO recoverUserData", "DocumentSnapshot data: " + document.getData());
                                // Deu bom recuperar a informação do usuário no firebase

                            } else {
                                Log.e("INFO recoverUserData", "No such document");
                                // Erro ao recuperar a infornação do usuário no firebase
                            }

                        } else {
                            Log.e("INFO recoverUserData", "get failed with ", task.getException());
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.e("INFO recoverUserData", "DOCUMENT GET CANCELED");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("INFO recoverUserData", "DOCUMENT GET FAILED");
                    }
                });
    }

    /*
    // Função pra recuperar em lista usuários cadastrados no firestore
    fireDB.collection("Usuarios").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                for (QueryDocumentSnapshot value : task.getResult()) {
                                    Usuario userDB = new Usuario();
                                    userDB.setId(Integer.parseInt(value.getString("id")));
                                    userDB.setNome(value.getString("nome"));
                                    userDB.setEmail(value.getString("email"));
                                    userDB.setTelefone(value.getString("telefone"));
                                    userDB.setUid(value.getString("uid"));

                                    usuariosFire.add(userDB);
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
     */

    private void initTextViews() {
        tvStatusUsuario = findViewById(R.id.tvStatusUsuario);
    }
}