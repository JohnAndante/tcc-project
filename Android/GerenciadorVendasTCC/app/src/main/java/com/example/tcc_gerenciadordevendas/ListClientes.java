package com.example.tcc_gerenciadordevendas;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListClientes extends AppCompatActivity {

    private Button btClienteVoltar;
    private Button btClienteAdicionar;
    private ImageButton imgbtIconeNovoCliente;
    private LinearLayout llAdicionarCliente;
    private ListView listViewClientes;
    private AdapterCliente adapter;
    private GestureDetector gestureDetector;
    private ArrayList<Cliente> listaDinamicaClientes;
    private ArrayList<String> arrayList;

    private static final int LIMITE_SWIPE = 100;
    private static final int LIMITE_VELOCIDADE = 100;

    BancoDadosCliente db = new BancoDadosCliente(this);

    public static final int NOVO_CLIENTE = 101;
    public static final int ALTERAR_CLIENTE = 102;
    public static final int CONSULTAR_CLIENTE = 103;
    public static final int RESULT_ALT_CLIENTE = 202;

    private int viewCounter = 0;

    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        // Método do Swipe
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diferencaX = e2.getX() - e1.getX();
            if(Math.abs(diferencaX) > LIMITE_SWIPE && Math.abs(velocityX) > LIMITE_VELOCIDADE){
                if(diferencaX > 0){
                    Log.i("MOVIMENTO", "Movimento para a direita");
                }
                else{
                    Log.i("MOVIMENTO", "Movimento para a esquerda");
                }
            }
        return true;
        }
    };

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_clients_zero);

        initButtonsHub();
        listClientes();

        imgbtIconeNovoCliente = (ImageButton) findViewById(R.id.imgbtIconeNovoCliente);
        imgbtIconeNovoCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

        llAdicionarCliente = (LinearLayout) findViewById(R.id.llAdicionarCliente);
        llAdicionarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewCliente();
            }
        });

        addDefaultEstados();
        addDefaultCidades();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //-- Criando novos dados
        if ((requestCode == NOVO_CLIENTE) && (resultCode == RESULT_OK)) {

            Cliente clienteMax = new Cliente();

            try {
                clienteMax = db.selectMaxCliente();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            db.close();
            listaDinamicaClientes.add(clienteMax);
            adapter.notifyDataSetChanged();

        } else

        //-- Alterando dados existentes
        if ((requestCode == CONSULTAR_CLIENTE) && (resultCode == RESULT_ALT_CLIENTE)) {
            listClientes();
            adapter.notifyDataSetChanged();
        }
    }

    private void initButtonsHub(){
        btClienteVoltar = findViewById(R.id.btClienteVoltar);

        btClienteVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListClientes.this, MainActivity.class));
            }
        });


    }

    private void addNewCliente() {
        Intent intent = new Intent(getApplication(), AddCliente.class);
        startActivityForResult(intent, NOVO_CLIENTE);
    }

    private void listClientes(){

        List<Cliente> clientes = db.listAllClientes();
        listaDinamicaClientes = new ArrayList<Cliente>();
        gestureDetector = new GestureDetector(this, gestureListener);

        if (!clientes.isEmpty()) {
            for (Cliente c : clientes)
                listaDinamicaClientes.add(c);
        }

        adapter = new AdapterCliente(this, 0, listaDinamicaClientes);

        listViewClientes = (ListView) findViewById(R.id.listVClientes);
        listViewClientes.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        listViewClientes.setAdapter(adapter);
        listViewClientes.setOnTouchListener(touchListener);

        listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Cliente c = (Cliente) listViewClientes.getItemAtPosition(i);
                    openClienteData(c);
                } catch (Exception e) {
                    Log.e("ERROR", e.getMessage().toString());
                }
            }
        });

    }

    private void openClienteData(Cliente c) {

        Intent intent = new Intent(ListClientes.this, ViewCliente.class);
        Bundle bundle = new Bundle();

        bundle.putInt("ID", c.getId());
        intent.putExtras(bundle);

        startActivityForResult(intent, CONSULTAR_CLIENTE);
    }

    private void addDefaultEstados() {

        // Verificando e adicionando estados
        List<Estado> estados = db.listAllEstados();
        //Log.i("INFO ESTADOS LISTCLIENTES ONCREATE", String.valueOf(estados.size()));
        if (estados.isEmpty()) {
            db.listDataEstadosCidades();

            db.addEstado(new Estado(1, "Acre", "AC"));
            db.addEstado(new Estado(2, "Alagoas", "AL"));
            db.addEstado(new Estado(3, "Amazonas", "AM"));
            db.addEstado(new Estado(4, "Amapá", "AP"));
            db.addEstado(new Estado(5, "Bahia", "BA"));
            db.addEstado(new Estado(6, "Ceará", "CE"));
            db.addEstado(new Estado(7, "Distrito Federal", "DF"));
            db.addEstado(new Estado(8, "Espírito Santo", "ES"));
            db.addEstado(new Estado(9, "Goiás", "GO"));
            db.addEstado(new Estado(10, "Maranhão", "MA"));
            db.addEstado(new Estado(11, "Minas Gerais", "MG"));
            db.addEstado(new Estado(12, "Mato Grosso do Sul", "MS"));
            db.addEstado(new Estado(13, "Mato Grosso", "MT"));
            db.addEstado(new Estado(14, "Pará", "PA"));
            db.addEstado(new Estado(15, "Paraíba", "PB"));
            db.addEstado(new Estado(16, "Pernambuco", "PE"));
            db.addEstado(new Estado(17, "Piauí", "PI"));
            db.addEstado(new Estado(18, "Paraná", "PR"));
            db.addEstado(new Estado(19, "Rio de Janeiro", "RJ"));
            db.addEstado(new Estado(20, "Rio Grande do Norte", "RN"));
            db.addEstado(new Estado(21, "Rondônia", "RO"));
            db.addEstado(new Estado(22, "Roraima", "RR"));
            db.addEstado(new Estado(23, "Rio Grande do Sul", "RS"));
            db.addEstado(new Estado(24, "Santa Catarina", "SC"));
            db.addEstado(new Estado(25, "Sergipe", "SE"));
            db.addEstado(new Estado(26, "São Paulo", "SP"));
            db.addEstado(new Estado(27, "Tocantins", "TO"));

            estados = db.listAllEstados();
            Log.i("INFO ESTADOS LISTCLIENTES ONCREATE POS DATA", String.valueOf(estados.size()));
        }
    }

    private void addDefaultCidades () {

        int i = 0;
        // Verificando e adicionando cidades
        List<Cidade> cidades = db.listAllCidades();
        //Log.i("INFO CIDADES LISTCLIENTES ONCREATE PRE DATA", String.valueOf(cidades.size()));
        if (cidades.isEmpty()) {
            db.listDataEstadosCidades();
            //db.addCidade(new Cidade(9999, "Cidade Teste", db.selectEstado(4)));
        }

        // Adicionando cidades do estado de Acre - ID 1
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(1));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(79, "Acrelândia", db.selectEstado(1)));
                db.addCidade(new Cidade(80, "Assis Brasil", db.selectEstado(1)));
                db.addCidade(new Cidade(81, "Brasiléia", db.selectEstado(1)));
                db.addCidade(new Cidade(82, "Bujari", db.selectEstado(1)));
                db.addCidade(new Cidade(83, "Capixaba", db.selectEstado(1)));
                db.addCidade(new Cidade(84, "Cruzeiro do Sul", db.selectEstado(1)));
                db.addCidade(new Cidade(85, "Epitaciolândia", db.selectEstado(1)));
                db.addCidade(new Cidade(86, "Feijó", db.selectEstado(1)));
                db.addCidade(new Cidade(87, "Jordão", db.selectEstado(1)));
                db.addCidade(new Cidade(88, "Mâncio Lima", db.selectEstado(1)));
                db.addCidade(new Cidade(89, "Manoel Urbano", db.selectEstado(1)));
                db.addCidade(new Cidade(90, "Marechal Thaumaturgo", db.selectEstado(1)));
                db.addCidade(new Cidade(91, "Plácido de Castro", db.selectEstado(1)));
                db.addCidade(new Cidade(92, "Porto Acre", db.selectEstado(1)));
                db.addCidade(new Cidade(93, "Porto Walter", db.selectEstado(1)));
                db.addCidade(new Cidade(94, "Rio Branco", db.selectEstado(1)));
                db.addCidade(new Cidade(95, "Rodrigues Alves", db.selectEstado(1)));
                db.addCidade(new Cidade(96, "Santa Rosa do Purus", db.selectEstado(1)));
                db.addCidade(new Cidade(97, "Sena Madureira", db.selectEstado(1)));
                db.addCidade(new Cidade(98, "Senador Guiomard", db.selectEstado(1)));
                db.addCidade(new Cidade(99, "Tarauacá", db.selectEstado(1)));
                db.addCidade(new Cidade(100, "Xapuri", db.selectEstado(1)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }

        // Adicionando cidades do estado de Alagoas - ID 2
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(2));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(101, "Água Branca", db.selectEstado(2)));
                db.addCidade(new Cidade(102, "Anadia", db.selectEstado(2)));
                db.addCidade(new Cidade(103, "Arapiraca", db.selectEstado(2)));
                db.addCidade(new Cidade(104, "Atalaia", db.selectEstado(2)));
                db.addCidade(new Cidade(105, "Barra de Santo Antônio", db.selectEstado(2)));
                db.addCidade(new Cidade(106, "Barra de São Miguel", db.selectEstado(2)));
                db.addCidade(new Cidade(107, "Batalha", db.selectEstado(2)));
                db.addCidade(new Cidade(108, "Belém", db.selectEstado(2)));
                db.addCidade(new Cidade(109, "Belo Monte", db.selectEstado(2)));
                db.addCidade(new Cidade(110, "Boca da Mata", db.selectEstado(2)));
                db.addCidade(new Cidade(111, "Branquinha", db.selectEstado(2)));
                db.addCidade(new Cidade(112, "Cacimbinhas", db.selectEstado(2)));
                db.addCidade(new Cidade(113, "Cajueiro", db.selectEstado(2)));
                db.addCidade(new Cidade(114, "Campestre", db.selectEstado(2)));
                db.addCidade(new Cidade(115, "Campo Alegre", db.selectEstado(2)));
                db.addCidade(new Cidade(116, "Campo Grande", db.selectEstado(2)));
                db.addCidade(new Cidade(117, "Canapi", db.selectEstado(2)));
                db.addCidade(new Cidade(118, "Capela", db.selectEstado(2)));
                db.addCidade(new Cidade(119, "Carneiros", db.selectEstado(2)));
                db.addCidade(new Cidade(120, "Chã Preta", db.selectEstado(2)));
                db.addCidade(new Cidade(121, "Coité do Nóia", db.selectEstado(2)));
                db.addCidade(new Cidade(122, "Colônia Leopoldina", db.selectEstado(2)));
                db.addCidade(new Cidade(123, "Coqueiro Seco", db.selectEstado(2)));
                db.addCidade(new Cidade(124, "Coruripe", db.selectEstado(2)));
                db.addCidade(new Cidade(125, "Craíbas", db.selectEstado(2)));
                db.addCidade(new Cidade(126, "Delmiro Gouveia", db.selectEstado(2)));
                db.addCidade(new Cidade(127, "Dois Riachos", db.selectEstado(2)));
                db.addCidade(new Cidade(128, "Estrela de Alagoas", db.selectEstado(2)));
                db.addCidade(new Cidade(129, "Feira Grande", db.selectEstado(2)));
                db.addCidade(new Cidade(130, "Feliz Deserto", db.selectEstado(2)));
                db.addCidade(new Cidade(131, "Flexeiras", db.selectEstado(2)));
                db.addCidade(new Cidade(132, "Girau do Ponciano", db.selectEstado(2)));
                db.addCidade(new Cidade(133, "Ibateguara", db.selectEstado(2)));
                db.addCidade(new Cidade(134, "Igaci", db.selectEstado(2)));
                db.addCidade(new Cidade(135, "Igreja Nova", db.selectEstado(2)));
                db.addCidade(new Cidade(136, "Inhapi", db.selectEstado(2)));
                db.addCidade(new Cidade(137, "Jacaré dos Homens", db.selectEstado(2)));
                db.addCidade(new Cidade(138, "Jacuípe", db.selectEstado(2)));
                db.addCidade(new Cidade(139, "Japaratinga", db.selectEstado(2)));
                db.addCidade(new Cidade(140, "Jaramataia", db.selectEstado(2)));
                db.addCidade(new Cidade(141, "Jequiá da Praia", db.selectEstado(2)));
                db.addCidade(new Cidade(142, "Joaquim Gomes", db.selectEstado(2)));
                db.addCidade(new Cidade(143, "Jundiá", db.selectEstado(2)));
                db.addCidade(new Cidade(144, "Junqueiro", db.selectEstado(2)));
                db.addCidade(new Cidade(145, "Lagoa da Canoa", db.selectEstado(2)));
                db.addCidade(new Cidade(146, "Limoeiro de Anadia", db.selectEstado(2)));
                db.addCidade(new Cidade(147, "Maceió", db.selectEstado(2)));
                db.addCidade(new Cidade(148, "Major Isidoro", db.selectEstado(2)));
                db.addCidade(new Cidade(149, "Mar Vermelho", db.selectEstado(2)));
                db.addCidade(new Cidade(150, "Maragogi", db.selectEstado(2)));
                db.addCidade(new Cidade(151, "Maravilha", db.selectEstado(2)));
                db.addCidade(new Cidade(152, "Marechal Deodoro", db.selectEstado(2)));
                db.addCidade(new Cidade(153, "Maribondo", db.selectEstado(2)));
                db.addCidade(new Cidade(154, "Mata Grande", db.selectEstado(2)));
                db.addCidade(new Cidade(155, "Matriz de Camaragibe", db.selectEstado(2)));
                db.addCidade(new Cidade(156, "Messias", db.selectEstado(2)));
                db.addCidade(new Cidade(157, "Minador do Negrão", db.selectEstado(2)));
                db.addCidade(new Cidade(158, "Monteirópolis", db.selectEstado(2)));
                db.addCidade(new Cidade(159, "Murici", db.selectEstado(2)));
                db.addCidade(new Cidade(160, "Novo Lino", db.selectEstado(2)));
                db.addCidade(new Cidade(161, "Olho d`Água das Flores", db.selectEstado(2)));
                db.addCidade(new Cidade(162, "Olho d`Água do Casado", db.selectEstado(2)));
                db.addCidade(new Cidade(163, "Olho d`Água Grande", db.selectEstado(2)));
                db.addCidade(new Cidade(164, "Olivença", db.selectEstado(2)));
                db.addCidade(new Cidade(165, "Ouro Branco", db.selectEstado(2)));
                db.addCidade(new Cidade(166, "Palestina", db.selectEstado(2)));
                db.addCidade(new Cidade(167, "Palmeira dos Índios", db.selectEstado(2)));
                db.addCidade(new Cidade(168, "Pão de Açúcar", db.selectEstado(2)));
                db.addCidade(new Cidade(169, "Pariconha", db.selectEstado(2)));
                db.addCidade(new Cidade(170, "Paripueira", db.selectEstado(2)));
                db.addCidade(new Cidade(171, "Passo de Camaragibe", db.selectEstado(2)));
                db.addCidade(new Cidade(172, "Paulo Jacinto", db.selectEstado(2)));
                db.addCidade(new Cidade(173, "Penedo", db.selectEstado(2)));
                db.addCidade(new Cidade(174, "Piaçabuçu", db.selectEstado(2)));
                db.addCidade(new Cidade(175, "Pilar", db.selectEstado(2)));
                db.addCidade(new Cidade(176, "Pindoba", db.selectEstado(2)));
                db.addCidade(new Cidade(177, "Piranhas", db.selectEstado(2)));
                db.addCidade(new Cidade(178, "Poço das Trincheiras", db.selectEstado(2)));
                db.addCidade(new Cidade(179, "Porto Calvo", db.selectEstado(2)));
                db.addCidade(new Cidade(180, "Porto de Pedras", db.selectEstado(2)));
                db.addCidade(new Cidade(181, "Porto Real do Colégio", db.selectEstado(2)));
                db.addCidade(new Cidade(182, "Quebrangulo", db.selectEstado(2)));
                db.addCidade(new Cidade(183, "Rio Largo", db.selectEstado(2)));
                db.addCidade(new Cidade(184, "Roteiro", db.selectEstado(2)));
                db.addCidade(new Cidade(185, "Santa Luzia do Norte", db.selectEstado(2)));
                db.addCidade(new Cidade(186, "Santana do Ipanema", db.selectEstado(2)));
                db.addCidade(new Cidade(187, "Santana do Mundaú", db.selectEstado(2)));
                db.addCidade(new Cidade(188, "São Brás", db.selectEstado(2)));
                db.addCidade(new Cidade(189, "São José da Laje", db.selectEstado(2)));
                db.addCidade(new Cidade(190, "São José da Tapera", db.selectEstado(2)));
                db.addCidade(new Cidade(191, "São Luís do Quitunde", db.selectEstado(2)));
                db.addCidade(new Cidade(192, "São Miguel dos Campos", db.selectEstado(2)));
                db.addCidade(new Cidade(193, "São Miguel dos Milagres", db.selectEstado(2)));
                db.addCidade(new Cidade(194, "São Sebastião", db.selectEstado(2)));
                db.addCidade(new Cidade(195, "Satuba", db.selectEstado(2)));
                db.addCidade(new Cidade(196, "Senador Rui Palmeira", db.selectEstado(2)));
                db.addCidade(new Cidade(197, "Tanque d`Arca", db.selectEstado(2)));
                db.addCidade(new Cidade(198, "Taquarana", db.selectEstado(2)));
                db.addCidade(new Cidade(199, "Teotônio Vilela", db.selectEstado(2)));
                db.addCidade(new Cidade(200, "Traipu", db.selectEstado(2)));
                db.addCidade(new Cidade(201, "União dos Palmares", db.selectEstado(2)));
                db.addCidade(new Cidade(202, "Viçosa", db.selectEstado(2)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }

        // Adicionando cidades do estado de Amazonas - ID 3
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(3));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(219, "Alvarães", db.selectEstado(3)));
                db.addCidade(new Cidade(220, "Amaturá", db.selectEstado(3)));
                db.addCidade(new Cidade(221, "Anamã", db.selectEstado(3)));
                db.addCidade(new Cidade(222, "Anori", db.selectEstado(3)));
                db.addCidade(new Cidade(223, "Apuí", db.selectEstado(3)));
                db.addCidade(new Cidade(224, "Atalaia do Norte", db.selectEstado(3)));
                db.addCidade(new Cidade(225, "Autazes", db.selectEstado(3)));
                db.addCidade(new Cidade(226, "Barcelos", db.selectEstado(3)));
                db.addCidade(new Cidade(227, "Barreirinha", db.selectEstado(3)));
                db.addCidade(new Cidade(228, "Benjamin Constant", db.selectEstado(3)));
                db.addCidade(new Cidade(229, "Beruri", db.selectEstado(3)));
                db.addCidade(new Cidade(230, "Boa Vista do Ramos", db.selectEstado(3)));
                db.addCidade(new Cidade(231, "Boca do Acre", db.selectEstado(3)));
                db.addCidade(new Cidade(232, "Borba", db.selectEstado(3)));
                db.addCidade(new Cidade(233, "Caapiranga", db.selectEstado(3)));
                db.addCidade(new Cidade(234, "Canutama", db.selectEstado(3)));
                db.addCidade(new Cidade(235, "Carauari", db.selectEstado(3)));
                db.addCidade(new Cidade(236, "Careiro", db.selectEstado(3)));
                db.addCidade(new Cidade(237, "Careiro da Várzea", db.selectEstado(3)));
                db.addCidade(new Cidade(238, "Coari", db.selectEstado(3)));
                db.addCidade(new Cidade(239, "Codajás", db.selectEstado(3)));
                db.addCidade(new Cidade(240, "Eirunepé", db.selectEstado(3)));
                db.addCidade(new Cidade(241, "Envira", db.selectEstado(3)));
                db.addCidade(new Cidade(242, "Fonte Boa", db.selectEstado(3)));
                db.addCidade(new Cidade(243, "Guajará", db.selectEstado(3)));
                db.addCidade(new Cidade(244, "Humaitá", db.selectEstado(3)));
                db.addCidade(new Cidade(245, "Ipixuna", db.selectEstado(3)));
                db.addCidade(new Cidade(246, "Iranduba", db.selectEstado(3)));
                db.addCidade(new Cidade(247, "Itacoatiara", db.selectEstado(3)));
                db.addCidade(new Cidade(248, "Itamarati", db.selectEstado(3)));
                db.addCidade(new Cidade(249, "Itapiranga", db.selectEstado(3)));
                db.addCidade(new Cidade(250, "Japurá", db.selectEstado(3)));
                db.addCidade(new Cidade(251, "Juruá", db.selectEstado(3)));
                db.addCidade(new Cidade(252, "Jutaí", db.selectEstado(3)));
                db.addCidade(new Cidade(253, "Lábrea", db.selectEstado(3)));
                db.addCidade(new Cidade(254, "Manacapuru", db.selectEstado(3)));
                db.addCidade(new Cidade(255, "Manaquiri", db.selectEstado(3)));
                db.addCidade(new Cidade(256, "Manaus", db.selectEstado(3)));
                db.addCidade(new Cidade(257, "Manicoré", db.selectEstado(3)));
                db.addCidade(new Cidade(258, "Maraã", db.selectEstado(3)));
                db.addCidade(new Cidade(259, "Maués", db.selectEstado(3)));
                db.addCidade(new Cidade(260, "Nhamundá", db.selectEstado(3)));
                db.addCidade(new Cidade(261, "Nova Olinda do Norte", db.selectEstado(3)));
                db.addCidade(new Cidade(262, "Novo Airão", db.selectEstado(3)));
                db.addCidade(new Cidade(263, "Novo Aripuanã", db.selectEstado(3)));
                db.addCidade(new Cidade(264, "Parintins", db.selectEstado(3)));
                db.addCidade(new Cidade(265, "Pauini", db.selectEstado(3)));
                db.addCidade(new Cidade(266, "Presidente Figueiredo", db.selectEstado(3)));
                db.addCidade(new Cidade(267, "Rio Preto da Eva", db.selectEstado(3)));
                db.addCidade(new Cidade(268, "Santa Isabel do Rio Negro", db.selectEstado(3)));
                db.addCidade(new Cidade(269, "Santo Antônio do Içá", db.selectEstado(3)));
                db.addCidade(new Cidade(270, "São Gabriel da Cachoeira", db.selectEstado(3)));
                db.addCidade(new Cidade(271, "São Paulo de Olivença", db.selectEstado(3)));
                db.addCidade(new Cidade(272, "São Sebastião do Uatumã", db.selectEstado(3)));
                db.addCidade(new Cidade(273, "Silves", db.selectEstado(3)));
                db.addCidade(new Cidade(274, "Tabatinga", db.selectEstado(3)));
                db.addCidade(new Cidade(275, "Tapauá", db.selectEstado(3)));
                db.addCidade(new Cidade(276, "Tefé", db.selectEstado(3)));
                db.addCidade(new Cidade(277, "Tonantins", db.selectEstado(3)));
                db.addCidade(new Cidade(278, "Uarini", db.selectEstado(3)));
                db.addCidade(new Cidade(279, "Urucará", db.selectEstado(3)));
                db.addCidade(new Cidade(280, "Urucurituba", db.selectEstado(3)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }

        // Adicionando cidades do estado de Amapá - ID 4
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(4));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(203, "Amapá", db.selectEstado(4)));
                db.addCidade(new Cidade(204, "Calçoene", db.selectEstado(4)));
                db.addCidade(new Cidade(205, "Cutias", db.selectEstado(4)));
                db.addCidade(new Cidade(206, "Ferreira Gomes", db.selectEstado(4)));
                db.addCidade(new Cidade(207, "Itaubal", db.selectEstado(4)));
                db.addCidade(new Cidade(208, "Laranjal do Jari", db.selectEstado(4)));
                db.addCidade(new Cidade(209, "Macapá", db.selectEstado(4)));
                db.addCidade(new Cidade(210, "Mazagão", db.selectEstado(4)));
                db.addCidade(new Cidade(211, "Oiapoque", db.selectEstado(4)));
                db.addCidade(new Cidade(212, "Pedra Branca do Amaparí", db.selectEstado(4)));
                db.addCidade(new Cidade(213, "Porto Grande", db.selectEstado(4)));
                db.addCidade(new Cidade(214, "Pracuúba", db.selectEstado(4)));
                db.addCidade(new Cidade(215, "Santana", db.selectEstado(4)));
                db.addCidade(new Cidade(216, "Serra do Navio", db.selectEstado(4)));
                db.addCidade(new Cidade(217, "Tartarugalzinho", db.selectEstado(4)));
                db.addCidade(new Cidade(218, "Vitória do Jari", db.selectEstado(4)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }

        // Adicionando cidades do estado de Espírito Santo - ID 8
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(8));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(1, "Afonso Cláudio", db.selectEstado(8)));
                db.addCidade(new Cidade(2, "Água Doce do Norte", db.selectEstado(8)));
                db.addCidade(new Cidade(3, "Águia Branca", db.selectEstado(8)));
                db.addCidade(new Cidade(4, "Alegre", db.selectEstado(8)));
                db.addCidade(new Cidade(5, "Alfredo Chaves", db.selectEstado(8)));
                db.addCidade(new Cidade(6, "Alto Rio Novo", db.selectEstado(8)));
                db.addCidade(new Cidade(7, "Anchieta", db.selectEstado(8)));
                db.addCidade(new Cidade(8, "Apiacá", db.selectEstado(8)));
                db.addCidade(new Cidade(9, "Aracruz", db.selectEstado(8)));
                db.addCidade(new Cidade(10, "Atilio Vivacqua", db.selectEstado(8)));
                db.addCidade(new Cidade(11, "Baixo Guandu", db.selectEstado(8)));
                db.addCidade(new Cidade(12, "Barra de São Francisco", db.selectEstado(8)));
                db.addCidade(new Cidade(13, "Boa Esperança", db.selectEstado(8)));
                db.addCidade(new Cidade(14, "Bom Jesus do Norte", db.selectEstado(8)));
                db.addCidade(new Cidade(15, "Brejetuba", db.selectEstado(8)));
                db.addCidade(new Cidade(16, "Cachoeiro de Itapemirim", db.selectEstado(8)));
                db.addCidade(new Cidade(17, "Cariacica", db.selectEstado(8)));
                db.addCidade(new Cidade(18, "Castelo", db.selectEstado(8)));
                db.addCidade(new Cidade(19, "Colatina", db.selectEstado(8)));
                db.addCidade(new Cidade(20, "Conceição da Barra", db.selectEstado(8)));
                db.addCidade(new Cidade(21, "Conceição do Castelo", db.selectEstado(8)));
                db.addCidade(new Cidade(22, "Divino de São Lourenço", db.selectEstado(8)));
                db.addCidade(new Cidade(23, "Domingos Martins", db.selectEstado(8)));
                db.addCidade(new Cidade(24, "Dores do Rio Preto", db.selectEstado(8)));
                db.addCidade(new Cidade(25, "Ecoporanga", db.selectEstado(8)));
                db.addCidade(new Cidade(26, "Fundão", db.selectEstado(8)));
                db.addCidade(new Cidade(27, "Governador Lindenberg", db.selectEstado(8)));
                db.addCidade(new Cidade(28, "Guaçuí", db.selectEstado(8)));
                db.addCidade(new Cidade(29, "Guarapari", db.selectEstado(8)));
                db.addCidade(new Cidade(30, "Ibatiba", db.selectEstado(8)));
                db.addCidade(new Cidade(31, "Ibiraçu", db.selectEstado(8)));
                db.addCidade(new Cidade(32, "Ibitirama", db.selectEstado(8)));
                db.addCidade(new Cidade(33, "Iconha", db.selectEstado(8)));
                db.addCidade(new Cidade(34, "Irupi", db.selectEstado(8)));
                db.addCidade(new Cidade(35, "Itaguaçu", db.selectEstado(8)));
                db.addCidade(new Cidade(36, "Itapemirim", db.selectEstado(8)));
                db.addCidade(new Cidade(37, "Itarana", db.selectEstado(8)));
                db.addCidade(new Cidade(38, "Iúna", db.selectEstado(8)));
                db.addCidade(new Cidade(39, "Jaguaré", db.selectEstado(8)));
                db.addCidade(new Cidade(40, "Jerônimo Monteiro", db.selectEstado(8)));
                db.addCidade(new Cidade(41, "João Neiva", db.selectEstado(8)));
                db.addCidade(new Cidade(42, "Laranja da Terra", db.selectEstado(8)));
                db.addCidade(new Cidade(43, "Linhares", db.selectEstado(8)));
                db.addCidade(new Cidade(44, "Mantenópolis", db.selectEstado(8)));
                db.addCidade(new Cidade(45, "Marataízes", db.selectEstado(8)));
                db.addCidade(new Cidade(46, "Marechal Floriano", db.selectEstado(8)));
                db.addCidade(new Cidade(47, "Marilândia", db.selectEstado(8)));
                db.addCidade(new Cidade(48, "Mimoso do Sul", db.selectEstado(8)));
                db.addCidade(new Cidade(49, "Montanha", db.selectEstado(8)));
                db.addCidade(new Cidade(50, "Mucurici", db.selectEstado(8)));
                db.addCidade(new Cidade(51, "Muniz Freire", db.selectEstado(8)));
                db.addCidade(new Cidade(52, "Muqui", db.selectEstado(8)));
                db.addCidade(new Cidade(53, "Nova Venécia", db.selectEstado(8)));
                db.addCidade(new Cidade(54, "Pancas", db.selectEstado(8)));
                db.addCidade(new Cidade(55, "Pedro Canário", db.selectEstado(8)));
                db.addCidade(new Cidade(56, "Pinheiros", db.selectEstado(8)));
                db.addCidade(new Cidade(57, "Piúma", db.selectEstado(8)));
                db.addCidade(new Cidade(58, "Ponto Belo", db.selectEstado(8)));
                db.addCidade(new Cidade(59, "Presidente Kennedy", db.selectEstado(8)));
                db.addCidade(new Cidade(60, "Rio Bananal", db.selectEstado(8)));
                db.addCidade(new Cidade(61, "Rio Novo do Sul", db.selectEstado(8)));
                db.addCidade(new Cidade(62, "Santa Leopoldina", db.selectEstado(8)));
                db.addCidade(new Cidade(63, "Santa Maria de Jetibá", db.selectEstado(8)));
                db.addCidade(new Cidade(64, "Santa Teresa", db.selectEstado(8)));
                db.addCidade(new Cidade(65, "São Domingos do Norte", db.selectEstado(8)));
                db.addCidade(new Cidade(66, "São Gabriel da Palha", db.selectEstado(8)));
                db.addCidade(new Cidade(67, "São José do Calçado", db.selectEstado(8)));
                db.addCidade(new Cidade(68, "São Mateus", db.selectEstado(8)));
                db.addCidade(new Cidade(69, "São Roque do Canaã", db.selectEstado(8)));
                db.addCidade(new Cidade(70, "Serra", db.selectEstado(8)));
                db.addCidade(new Cidade(71, "Sooretama", db.selectEstado(8)));
                db.addCidade(new Cidade(72, "Vargem Alta", db.selectEstado(8)));
                db.addCidade(new Cidade(73, "Venda Nova do Imigrante", db.selectEstado(8)));
                db.addCidade(new Cidade(74, "Viana", db.selectEstado(8)));
                db.addCidade(new Cidade(75, "Vila Pavão", db.selectEstado(8)));
                db.addCidade(new Cidade(76, "Vila Valério", db.selectEstado(8)));
                db.addCidade(new Cidade(77, "Vila Velha", db.selectEstado(8)));
                db.addCidade(new Cidade(78, "Vitória", db.selectEstado(8)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }

        // Adicionando cidades do estado de Paraná - ID 18
        i++;
        cidades = db.listAllCidadesByEstado(db.selectEstado(18));
        //Log.e("ERRO INSERT CIDADE ???", "PRE INSERT TENTATIVA " + i);
        //Log.e("ERRO INSERT CIDADE ???", "RESULTADO DA QUERY " + cidades.size());
        if (cidades.isEmpty()) {
            try {
                //Log.i("INFO CIDADES TRY CIDADES", "INICIANDO INSERT Nº " + i);

                db.addCidade(new Cidade(2784, "Abatiá", db.selectEstado(18)));
                db.addCidade(new Cidade(2785, "Adrianópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2786, "Agudos do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2787, "Almirante Tamandaré", db.selectEstado(18)));
                db.addCidade(new Cidade(2788, "Altamira do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(2789, "Alto Paraíso", db.selectEstado(18)));
                db.addCidade(new Cidade(2790, "Alto Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(2791, "Alto Piquiri", db.selectEstado(18)));
                db.addCidade(new Cidade(2792, "Altônia", db.selectEstado(18)));
                db.addCidade(new Cidade(2793, "Alvorada do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2794, "Amaporã", db.selectEstado(18)));
                db.addCidade(new Cidade(2795, "Ampére", db.selectEstado(18)));
                db.addCidade(new Cidade(2796, "Anahy", db.selectEstado(18)));
                db.addCidade(new Cidade(2797, "Andirá", db.selectEstado(18)));
                db.addCidade(new Cidade(2798, "Ângulo", db.selectEstado(18)));
                db.addCidade(new Cidade(2799, "Antonina", db.selectEstado(18)));
                db.addCidade(new Cidade(2800, "Antônio Olinto", db.selectEstado(18)));
                db.addCidade(new Cidade(2801, "Apucarana", db.selectEstado(18)));
                db.addCidade(new Cidade(2802, "Arapongas", db.selectEstado(18)));
                db.addCidade(new Cidade(2803, "Arapoti", db.selectEstado(18)));
                db.addCidade(new Cidade(2804, "Arapuã", db.selectEstado(18)));
                db.addCidade(new Cidade(2805, "Araruna", db.selectEstado(18)));
                db.addCidade(new Cidade(2806, "Araucária", db.selectEstado(18)));
                db.addCidade(new Cidade(2807, "Ariranha do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(2808, "Assaí", db.selectEstado(18)));
                db.addCidade(new Cidade(2809, "Assis Chateaubriand", db.selectEstado(18)));
                db.addCidade(new Cidade(2810, "Astorga", db.selectEstado(18)));
                db.addCidade(new Cidade(2811, "Atalaia", db.selectEstado(18)));
                db.addCidade(new Cidade(2812, "Balsa Nova", db.selectEstado(18)));
                db.addCidade(new Cidade(2813, "Bandeirantes", db.selectEstado(18)));
                db.addCidade(new Cidade(2814, "Barbosa Ferraz", db.selectEstado(18)));
                db.addCidade(new Cidade(2815, "Barra do Jacaré", db.selectEstado(18)));
                db.addCidade(new Cidade(2816, "Barracão", db.selectEstado(18)));
                db.addCidade(new Cidade(2817, "Bela Vista da Caroba", db.selectEstado(18)));
                db.addCidade(new Cidade(2818, "Bela Vista do Paraíso", db.selectEstado(18)));
                db.addCidade(new Cidade(2819, "Bituruna", db.selectEstado(18)));
                db.addCidade(new Cidade(2820, "Boa Esperança", db.selectEstado(18)));
                db.addCidade(new Cidade(2821, "Boa Esperança do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2822, "Boa Ventura de São Roque", db.selectEstado(18)));
                db.addCidade(new Cidade(2823, "Boa Vista da Aparecida", db.selectEstado(18)));
                db.addCidade(new Cidade(2824, "Bocaiúva do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2825, "Bom Jesus do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2826, "Bom Sucesso", db.selectEstado(18)));
                db.addCidade(new Cidade(2827, "Bom Sucesso do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2828, "Borrazópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2829, "Braganey", db.selectEstado(18)));
                db.addCidade(new Cidade(2830, "Brasilândia do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2831, "Cafeara", db.selectEstado(18)));
                db.addCidade(new Cidade(2832, "Cafelândia", db.selectEstado(18)));
                db.addCidade(new Cidade(2833, "Cafezal do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2834, "Califórnia", db.selectEstado(18)));
                db.addCidade(new Cidade(2835, "Cambará", db.selectEstado(18)));
                db.addCidade(new Cidade(2836, "Cambé", db.selectEstado(18)));
                db.addCidade(new Cidade(2837, "Cambira", db.selectEstado(18)));
                db.addCidade(new Cidade(2838, "Campina da Lagoa", db.selectEstado(18)));
                db.addCidade(new Cidade(2839, "Campina do Simão", db.selectEstado(18)));
                db.addCidade(new Cidade(2840, "Campina Grande do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2841, "Campo Bonito", db.selectEstado(18)));
                db.addCidade(new Cidade(2842, "Campo do Tenente", db.selectEstado(18)));
                db.addCidade(new Cidade(2843, "Campo Largo", db.selectEstado(18)));
                db.addCidade(new Cidade(2844, "Campo Magro", db.selectEstado(18)));
                db.addCidade(new Cidade(2845, "Campo Mourão", db.selectEstado(18)));
                db.addCidade(new Cidade(2846, "Cândido de Abreu", db.selectEstado(18)));
                db.addCidade(new Cidade(2847, "Candói", db.selectEstado(18)));
                db.addCidade(new Cidade(2848, "Cantagalo", db.selectEstado(18)));
                db.addCidade(new Cidade(2849, "Capanema", db.selectEstado(18)));
                db.addCidade(new Cidade(2850, "Capitão Leônidas Marques", db.selectEstado(18)));
                db.addCidade(new Cidade(2851, "Carambeí", db.selectEstado(18)));
                db.addCidade(new Cidade(2852, "Carlópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2853, "Cascavel", db.selectEstado(18)));
                db.addCidade(new Cidade(2854, "Castro", db.selectEstado(18)));
                db.addCidade(new Cidade(2855, "Catanduvas", db.selectEstado(18)));
                db.addCidade(new Cidade(2856, "Centenário do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2857, "Cerro Azul", db.selectEstado(18)));
                db.addCidade(new Cidade(2858, "Céu Azul", db.selectEstado(18)));
                db.addCidade(new Cidade(2859, "Chopinzinho", db.selectEstado(18)));
                db.addCidade(new Cidade(2860, "Cianorte", db.selectEstado(18)));
                db.addCidade(new Cidade(2861, "Cidade Gaúcha", db.selectEstado(18)));
                db.addCidade(new Cidade(2862, "Clevelândia", db.selectEstado(18)));
                db.addCidade(new Cidade(2863, "Colombo", db.selectEstado(18)));
                db.addCidade(new Cidade(2864, "Colorado", db.selectEstado(18)));
                db.addCidade(new Cidade(2865, "Congonhinhas", db.selectEstado(18)));
                db.addCidade(new Cidade(2866, "Conselheiro Mairinck", db.selectEstado(18)));
                db.addCidade(new Cidade(2867, "Contenda", db.selectEstado(18)));
                db.addCidade(new Cidade(2868, "Corbélia", db.selectEstado(18)));
                db.addCidade(new Cidade(2869, "Cornélio Procópio", db.selectEstado(18)));
                db.addCidade(new Cidade(2870, "Coronel Domingos Soares", db.selectEstado(18)));
                db.addCidade(new Cidade(2871, "Coronel Vivida", db.selectEstado(18)));
                db.addCidade(new Cidade(2872, "Corumbataí do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2873, "Cruz Machado", db.selectEstado(18)));
                db.addCidade(new Cidade(2874, "Cruzeiro do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2875, "Cruzeiro do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2876, "Cruzeiro do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2877, "Cruzmaltina", db.selectEstado(18)));
                db.addCidade(new Cidade(2878, "Curitiba", db.selectEstado(18)));
                db.addCidade(new Cidade(2879, "Curiúva", db.selectEstado(18)));
                db.addCidade(new Cidade(2880, "Diamante d`Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2881, "Diamante do Norte", db.selectEstado(18)));
                db.addCidade(new Cidade(2882, "Diamante do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2883, "Dois Vizinhos", db.selectEstado(18)));
                db.addCidade(new Cidade(2884, "Douradina", db.selectEstado(18)));
                db.addCidade(new Cidade(2885, "Doutor Camargo", db.selectEstado(18)));
                db.addCidade(new Cidade(2886, "Doutor Ulysses", db.selectEstado(18)));
                db.addCidade(new Cidade(2887, "Enéas Marques", db.selectEstado(18)));
                db.addCidade(new Cidade(2888, "Engenheiro Beltrão", db.selectEstado(18)));
                db.addCidade(new Cidade(2889, "Entre Rios do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2890, "Esperança Nova", db.selectEstado(18)));
                db.addCidade(new Cidade(2891, "Espigão Alto do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2892, "Farol", db.selectEstado(18)));
                db.addCidade(new Cidade(2893, "Faxinal", db.selectEstado(18)));
                db.addCidade(new Cidade(2894, "Fazenda Rio Grande", db.selectEstado(18)));
                db.addCidade(new Cidade(2895, "Fênix", db.selectEstado(18)));
                db.addCidade(new Cidade(2896, "Fernandes Pinheiro", db.selectEstado(18)));
                db.addCidade(new Cidade(2897, "Figueira", db.selectEstado(18)));
                db.addCidade(new Cidade(2898, "Flor da Serra do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2899, "Floraí", db.selectEstado(18)));
                db.addCidade(new Cidade(2900, "Floresta", db.selectEstado(18)));
                db.addCidade(new Cidade(2901, "Florestópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2902, "Flórida", db.selectEstado(18)));
                db.addCidade(new Cidade(2903, "Formosa do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2904, "Foz do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2905, "Foz do Jordão", db.selectEstado(18)));
                db.addCidade(new Cidade(2906, "Francisco Alves", db.selectEstado(18)));
                db.addCidade(new Cidade(2907, "Francisco Beltrão", db.selectEstado(18)));
                db.addCidade(new Cidade(2908, "General Carneiro", db.selectEstado(18)));
                db.addCidade(new Cidade(2909, "Godoy Moreira", db.selectEstado(18)));
                db.addCidade(new Cidade(2910, "Goioerê", db.selectEstado(18)));
                db.addCidade(new Cidade(2911, "Goioxim", db.selectEstado(18)));
                db.addCidade(new Cidade(2912, "Grandes Rios", db.selectEstado(18)));
                db.addCidade(new Cidade(2913, "Guaíra", db.selectEstado(18)));
                db.addCidade(new Cidade(2914, "Guairaçá", db.selectEstado(18)));
                db.addCidade(new Cidade(2915, "Guamiranga", db.selectEstado(18)));
                db.addCidade(new Cidade(2916, "Guapirama", db.selectEstado(18)));
                db.addCidade(new Cidade(2917, "Guaporema", db.selectEstado(18)));
                db.addCidade(new Cidade(2918, "Guaraci", db.selectEstado(18)));
                db.addCidade(new Cidade(2919, "Guaraniaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2920, "Guarapuava", db.selectEstado(18)));
                db.addCidade(new Cidade(2921, "Guaraqueçaba", db.selectEstado(18)));
                db.addCidade(new Cidade(2922, "Guaratuba", db.selectEstado(18)));
                db.addCidade(new Cidade(2923, "Honório Serpa", db.selectEstado(18)));
                db.addCidade(new Cidade(2924, "Ibaiti", db.selectEstado(18)));
                db.addCidade(new Cidade(2925, "Ibema", db.selectEstado(18)));
                db.addCidade(new Cidade(2926, "Ibiporã", db.selectEstado(18)));
                db.addCidade(new Cidade(2927, "Icaraíma", db.selectEstado(18)));
                db.addCidade(new Cidade(2928, "Iguaraçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2929, "Iguatu", db.selectEstado(18)));
                db.addCidade(new Cidade(2930, "Imbaú", db.selectEstado(18)));
                db.addCidade(new Cidade(2931, "Imbituva", db.selectEstado(18)));
                db.addCidade(new Cidade(2932, "Inácio Martins", db.selectEstado(18)));
                db.addCidade(new Cidade(2933, "Inajá", db.selectEstado(18)));
                db.addCidade(new Cidade(2934, "Indianópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2935, "Ipiranga", db.selectEstado(18)));
                db.addCidade(new Cidade(2936, "Iporã", db.selectEstado(18)));
                db.addCidade(new Cidade(2937, "Iracema do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2938, "Irati", db.selectEstado(18)));
                db.addCidade(new Cidade(2939, "Iretama", db.selectEstado(18)));
                db.addCidade(new Cidade(2940, "Itaguajé", db.selectEstado(18)));
                db.addCidade(new Cidade(2941, "Itaipulândia", db.selectEstado(18)));
                db.addCidade(new Cidade(2942, "Itambaracá", db.selectEstado(18)));
                db.addCidade(new Cidade(2943, "Itambé", db.selectEstado(18)));
                db.addCidade(new Cidade(2944, "Itapejara d`Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2945, "Itaperuçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2946, "Itaúna do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2947, "Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(2948, "Ivaiporã", db.selectEstado(18)));
                db.addCidade(new Cidade(2949, "Ivaté", db.selectEstado(18)));
                db.addCidade(new Cidade(2950, "Ivatuba", db.selectEstado(18)));
                db.addCidade(new Cidade(2951, "Jaboti", db.selectEstado(18)));
                db.addCidade(new Cidade(2952, "Jacarezinho", db.selectEstado(18)));
                db.addCidade(new Cidade(2953, "Jaguapitã", db.selectEstado(18)));
                db.addCidade(new Cidade(2954, "Jaguariaíva", db.selectEstado(18)));
                db.addCidade(new Cidade(2955, "Jandaia do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2956, "Janiópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2957, "Japira", db.selectEstado(18)));
                db.addCidade(new Cidade(2958, "Japurá", db.selectEstado(18)));
                db.addCidade(new Cidade(2959, "Jardim Alegre", db.selectEstado(18)));
                db.addCidade(new Cidade(2960, "Jardim Olinda", db.selectEstado(18)));
                db.addCidade(new Cidade(2961, "Jataizinho", db.selectEstado(18)));
                db.addCidade(new Cidade(2962, "Jesuítas", db.selectEstado(18)));
                db.addCidade(new Cidade(2963, "Joaquim Távora", db.selectEstado(18)));
                db.addCidade(new Cidade(2964, "Jundiaí do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2965, "Juranda", db.selectEstado(18)));
                db.addCidade(new Cidade(2966, "Jussara", db.selectEstado(18)));
                db.addCidade(new Cidade(2967, "Kaloré", db.selectEstado(18)));
                db.addCidade(new Cidade(2968, "Lapa", db.selectEstado(18)));
                db.addCidade(new Cidade(2969, "Laranjal", db.selectEstado(18)));
                db.addCidade(new Cidade(2970, "Laranjeiras do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2971, "Leópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2972, "Lidianópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2973, "Lindoeste", db.selectEstado(18)));
                db.addCidade(new Cidade(2974, "Loanda", db.selectEstado(18)));
                db.addCidade(new Cidade(2975, "Lobato", db.selectEstado(18)));
                db.addCidade(new Cidade(2976, "Londrina", db.selectEstado(18)));
                db.addCidade(new Cidade(2977, "Luiziana", db.selectEstado(18)));
                db.addCidade(new Cidade(2978, "Lunardelli", db.selectEstado(18)));
                db.addCidade(new Cidade(2979, "Lupionópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2980, "Mallet", db.selectEstado(18)));
                db.addCidade(new Cidade(2981, "Mamborê", db.selectEstado(18)));
                db.addCidade(new Cidade(2982, "Mandaguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(2983, "Mandaguari", db.selectEstado(18)));
                db.addCidade(new Cidade(2984, "Mandirituba", db.selectEstado(18)));
                db.addCidade(new Cidade(2985, "Manfrinópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2986, "Mangueirinha", db.selectEstado(18)));
                db.addCidade(new Cidade(2987, "Manoel Ribas", db.selectEstado(18)));
                db.addCidade(new Cidade(2988, "Marechal Cândido Rondon", db.selectEstado(18)));
                db.addCidade(new Cidade(2989, "Maria Helena", db.selectEstado(18)));
                db.addCidade(new Cidade(2990, "Marialva", db.selectEstado(18)));
                db.addCidade(new Cidade(2991, "Marilândia do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(2992, "Marilena", db.selectEstado(18)));
                db.addCidade(new Cidade(2993, "Mariluz", db.selectEstado(18)));
                db.addCidade(new Cidade(2994, "Maringá", db.selectEstado(18)));
                db.addCidade(new Cidade(2995, "Mariópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(2996, "Maripá", db.selectEstado(18)));
                db.addCidade(new Cidade(2997, "Marmeleiro", db.selectEstado(18)));
                db.addCidade(new Cidade(2998, "Marquinho", db.selectEstado(18)));
                db.addCidade(new Cidade(2999, "Marumbi", db.selectEstado(18)));
                db.addCidade(new Cidade(3000, "Matelândia", db.selectEstado(18)));
                db.addCidade(new Cidade(3001, "Matinhos", db.selectEstado(18)));
                db.addCidade(new Cidade(3002, "Mato Rico", db.selectEstado(18)));
                db.addCidade(new Cidade(3003, "Mauá da Serra", db.selectEstado(18)));
                db.addCidade(new Cidade(3004, "Medianeira", db.selectEstado(18)));
                db.addCidade(new Cidade(3005, "Mercedes", db.selectEstado(18)));
                db.addCidade(new Cidade(3006, "Mirador", db.selectEstado(18)));
                db.addCidade(new Cidade(3007, "Miraselva", db.selectEstado(18)));
                db.addCidade(new Cidade(3008, "Missal", db.selectEstado(18)));
                db.addCidade(new Cidade(3009, "Moreira Sales", db.selectEstado(18)));
                db.addCidade(new Cidade(3010, "Morretes", db.selectEstado(18)));
                db.addCidade(new Cidade(3011, "Munhoz de Melo", db.selectEstado(18)));
                db.addCidade(new Cidade(3012, "Nossa Senhora das Graças", db.selectEstado(18)));
                db.addCidade(new Cidade(3013, "Nova Aliança do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3014, "Nova América da Colina", db.selectEstado(18)));
                db.addCidade(new Cidade(3015, "Nova Aurora", db.selectEstado(18)));
                db.addCidade(new Cidade(3016, "Nova Cantu", db.selectEstado(18)));
                db.addCidade(new Cidade(3017, "Nova Esperança", db.selectEstado(18)));
                db.addCidade(new Cidade(3018, "Nova Esperança do Sudoeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3019, "Nova Fátima", db.selectEstado(18)));
                db.addCidade(new Cidade(3020, "Nova Laranjeiras", db.selectEstado(18)));
                db.addCidade(new Cidade(3021, "Nova Londrina", db.selectEstado(18)));
                db.addCidade(new Cidade(3022, "Nova Olímpia", db.selectEstado(18)));
                db.addCidade(new Cidade(3023, "Nova Prata do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3024, "Nova Santa Bárbara", db.selectEstado(18)));
                db.addCidade(new Cidade(3025, "Nova Santa Rosa", db.selectEstado(18)));
                db.addCidade(new Cidade(3026, "Nova Tebas", db.selectEstado(18)));
                db.addCidade(new Cidade(3027, "Novo Itacolomi", db.selectEstado(18)));
                db.addCidade(new Cidade(3028, "Ortigueira", db.selectEstado(18)));
                db.addCidade(new Cidade(3029, "Ourizona", db.selectEstado(18)));
                db.addCidade(new Cidade(3030, "Ouro Verde do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3031, "Paiçandu", db.selectEstado(18)));
                db.addCidade(new Cidade(3032, "Palmas", db.selectEstado(18)));
                db.addCidade(new Cidade(3033, "Palmeira", db.selectEstado(18)));
                db.addCidade(new Cidade(3034, "Palmital", db.selectEstado(18)));
                db.addCidade(new Cidade(3035, "Palotina", db.selectEstado(18)));
                db.addCidade(new Cidade(3036, "Paraíso do Norte", db.selectEstado(18)));
                db.addCidade(new Cidade(3037, "Paranacity", db.selectEstado(18)));
                db.addCidade(new Cidade(3038, "Paranaguá", db.selectEstado(18)));
                db.addCidade(new Cidade(3039, "Paranapoema", db.selectEstado(18)));
                db.addCidade(new Cidade(3040, "Paranavaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3041, "Pato Bragado", db.selectEstado(18)));
                db.addCidade(new Cidade(3042, "Pato Branco", db.selectEstado(18)));
                db.addCidade(new Cidade(3043, "Paula Freitas", db.selectEstado(18)));
                db.addCidade(new Cidade(3044, "Paulo Frontin", db.selectEstado(18)));
                db.addCidade(new Cidade(3045, "Peabiru", db.selectEstado(18)));
                db.addCidade(new Cidade(3046, "Perobal", db.selectEstado(18)));
                db.addCidade(new Cidade(3047, "Pérola", db.selectEstado(18)));
                db.addCidade(new Cidade(3048, "Pérola d`Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3049, "Piên", db.selectEstado(18)));
                db.addCidade(new Cidade(3050, "Pinhais", db.selectEstado(18)));
                db.addCidade(new Cidade(3051, "Pinhal de São Bento", db.selectEstado(18)));
                db.addCidade(new Cidade(3052, "Pinhalão", db.selectEstado(18)));
                db.addCidade(new Cidade(3053, "Pinhão", db.selectEstado(18)));
                db.addCidade(new Cidade(3054, "Piraí do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(3055, "Piraquara", db.selectEstado(18)));
                db.addCidade(new Cidade(3056, "Pitanga", db.selectEstado(18)));
                db.addCidade(new Cidade(3057, "Pitangueiras", db.selectEstado(18)));
                db.addCidade(new Cidade(3058, "Planaltina do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3059, "Planalto", db.selectEstado(18)));
                db.addCidade(new Cidade(3060, "Ponta Grossa", db.selectEstado(18)));
                db.addCidade(new Cidade(3061, "Pontal do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3062, "Porecatu", db.selectEstado(18)));
                db.addCidade(new Cidade(3063, "Porto Amazonas", db.selectEstado(18)));
                db.addCidade(new Cidade(3064, "Porto Barreiro", db.selectEstado(18)));
                db.addCidade(new Cidade(3065, "Porto Rico", db.selectEstado(18)));
                db.addCidade(new Cidade(3066, "Porto Vitória", db.selectEstado(18)));
                db.addCidade(new Cidade(3067, "Prado Ferreira", db.selectEstado(18)));
                db.addCidade(new Cidade(3068, "Pranchita", db.selectEstado(18)));
                db.addCidade(new Cidade(3069, "Presidente Castelo Branco", db.selectEstado(18)));
                db.addCidade(new Cidade(3070, "Primeiro de Maio", db.selectEstado(18)));
                db.addCidade(new Cidade(3071, "Prudentópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(3072, "Quarto Centenário", db.selectEstado(18)));
                db.addCidade(new Cidade(3073, "Quatiguá", db.selectEstado(18)));
                db.addCidade(new Cidade(3074, "Quatro Barras", db.selectEstado(18)));
                db.addCidade(new Cidade(3075, "Quatro Pontes", db.selectEstado(18)));
                db.addCidade(new Cidade(3076, "Quedas do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3077, "Querência do Norte", db.selectEstado(18)));
                db.addCidade(new Cidade(3078, "Quinta do Sol", db.selectEstado(18)));
                db.addCidade(new Cidade(3079, "Quitandinha", db.selectEstado(18)));
                db.addCidade(new Cidade(3080, "Ramilândia", db.selectEstado(18)));
                db.addCidade(new Cidade(3081, "Rancho Alegre", db.selectEstado(18)));
                db.addCidade(new Cidade(3082, "Rancho Alegre d`Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3083, "Realeza", db.selectEstado(18)));
                db.addCidade(new Cidade(3084, "Rebouças", db.selectEstado(18)));
                db.addCidade(new Cidade(3085, "Renascença", db.selectEstado(18)));
                db.addCidade(new Cidade(3086, "Reserva", db.selectEstado(18)));
                db.addCidade(new Cidade(3087, "Reserva do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3088, "Ribeirão Claro", db.selectEstado(18)));
                db.addCidade(new Cidade(3089, "Ribeirão do Pinhal", db.selectEstado(18)));
                db.addCidade(new Cidade(3090, "Rio Azul", db.selectEstado(18)));
                db.addCidade(new Cidade(3091, "Rio Bom", db.selectEstado(18)));
                db.addCidade(new Cidade(3092, "Rio Bonito do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3093, "Rio Branco do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3094, "Rio Branco do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(3095, "Rio Negro", db.selectEstado(18)));
                db.addCidade(new Cidade(3096, "Rolândia", db.selectEstado(18)));
                db.addCidade(new Cidade(3097, "Roncador", db.selectEstado(18)));
                db.addCidade(new Cidade(3098, "Rondon", db.selectEstado(18)));
                db.addCidade(new Cidade(3099, "Rosário do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3100, "Sabáudia", db.selectEstado(18)));
                db.addCidade(new Cidade(3101, "Salgado Filho", db.selectEstado(18)));
                db.addCidade(new Cidade(3102, "Salto do Itararé", db.selectEstado(18)));
                db.addCidade(new Cidade(3103, "Salto do Lontra", db.selectEstado(18)));
                db.addCidade(new Cidade(3104, "Santa Amélia", db.selectEstado(18)));
                db.addCidade(new Cidade(3105, "Santa Cecília do Pavão", db.selectEstado(18)));
                db.addCidade(new Cidade(3106, "Santa Cruz de Monte Castelo", db.selectEstado(18)));
                db.addCidade(new Cidade(3107, "Santa Fé", db.selectEstado(18)));
                db.addCidade(new Cidade(3108, "Santa Helena", db.selectEstado(18)));
                db.addCidade(new Cidade(3109, "Santa Inês", db.selectEstado(18)));
                db.addCidade(new Cidade(3110, "Santa Isabel do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3111, "Santa Izabel do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3112, "Santa Lúcia", db.selectEstado(18)));
                db.addCidade(new Cidade(3113, "Santa Maria do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3114, "Santa Mariana", db.selectEstado(18)));
                db.addCidade(new Cidade(3115, "Santa Mônica", db.selectEstado(18)));
                db.addCidade(new Cidade(3116, "Santa Tereza do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3117, "Santa Terezinha de Itaipu", db.selectEstado(18)));
                db.addCidade(new Cidade(3118, "Santana do Itararé", db.selectEstado(18)));
                db.addCidade(new Cidade(3119, "Santo Antônio da Platina", db.selectEstado(18)));
                db.addCidade(new Cidade(3120, "Santo Antônio do Caiuá", db.selectEstado(18)));
                db.addCidade(new Cidade(3121, "Santo Antônio do Paraíso", db.selectEstado(18)));
                db.addCidade(new Cidade(3122, "Santo Antônio do Sudoeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3123, "Santo Inácio", db.selectEstado(18)));
                db.addCidade(new Cidade(3124, "São Carlos do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3125, "São Jerônimo da Serra", db.selectEstado(18)));
                db.addCidade(new Cidade(3126, "São João", db.selectEstado(18)));
                db.addCidade(new Cidade(3127, "São João do Caiuá", db.selectEstado(18)));
                db.addCidade(new Cidade(3128, "São João do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3129, "São João do Triunfo", db.selectEstado(18)));
                db.addCidade(new Cidade(3130, "São Jorge d`Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3131, "São Jorge do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3132, "São Jorge do Patrocínio", db.selectEstado(18)));
                db.addCidade(new Cidade(3133, "São José da Boa Vista", db.selectEstado(18)));
                db.addCidade(new Cidade(3134, "São José das Palmeiras", db.selectEstado(18)));
                db.addCidade(new Cidade(3135, "São José dos Pinhais", db.selectEstado(18)));
                db.addCidade(new Cidade(3136, "São Manoel do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3137, "São Mateus do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(3138, "São Miguel do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3139, "São Pedro do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3140, "São Pedro do Ivaí", db.selectEstado(18)));
                db.addCidade(new Cidade(3141, "São Pedro do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3142, "São Sebastião da Amoreira", db.selectEstado(18)));
                db.addCidade(new Cidade(3143, "São Tomé", db.selectEstado(18)));
                db.addCidade(new Cidade(3144, "Sapopema", db.selectEstado(18)));
                db.addCidade(new Cidade(3145, "Sarandi", db.selectEstado(18)));
                db.addCidade(new Cidade(3146, "Saudade do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3147, "Sengés", db.selectEstado(18)));
                db.addCidade(new Cidade(3148, "Serranópolis do Iguaçu", db.selectEstado(18)));
                db.addCidade(new Cidade(3149, "Sertaneja", db.selectEstado(18)));
                db.addCidade(new Cidade(3150, "Sertanópolis", db.selectEstado(18)));
                db.addCidade(new Cidade(3151, "Siqueira Campos", db.selectEstado(18)));
                db.addCidade(new Cidade(3152, "Sulina", db.selectEstado(18)));
                db.addCidade(new Cidade(3153, "Tamarana", db.selectEstado(18)));
                db.addCidade(new Cidade(3154, "Tamboara", db.selectEstado(18)));
                db.addCidade(new Cidade(3155, "Tapejara", db.selectEstado(18)));
                db.addCidade(new Cidade(3156, "Tapira", db.selectEstado(18)));
                db.addCidade(new Cidade(3157, "Teixeira Soares", db.selectEstado(18)));
                db.addCidade(new Cidade(3158, "Telêmaco Borba", db.selectEstado(18)));
                db.addCidade(new Cidade(3159, "Terra Boa", db.selectEstado(18)));
                db.addCidade(new Cidade(3160, "Terra Rica", db.selectEstado(18)));
                db.addCidade(new Cidade(3161, "Terra Roxa", db.selectEstado(18)));
                db.addCidade(new Cidade(3162, "Tibagi", db.selectEstado(18)));
                db.addCidade(new Cidade(3163, "Tijucas do Sul", db.selectEstado(18)));
                db.addCidade(new Cidade(3164, "Toledo", db.selectEstado(18)));
                db.addCidade(new Cidade(3165, "Tomazina", db.selectEstado(18)));
                db.addCidade(new Cidade(3166, "Três Barras do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3167, "Tunas do Paraná", db.selectEstado(18)));
                db.addCidade(new Cidade(3168, "Tuneiras do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3169, "Tupãssi", db.selectEstado(18)));
                db.addCidade(new Cidade(3170, "Turvo", db.selectEstado(18)));
                db.addCidade(new Cidade(3171, "Ubiratã", db.selectEstado(18)));
                db.addCidade(new Cidade(3172, "Umuarama", db.selectEstado(18)));
                db.addCidade(new Cidade(3173, "União da Vitória", db.selectEstado(18)));
                db.addCidade(new Cidade(3174, "Uniflor", db.selectEstado(18)));
                db.addCidade(new Cidade(3175, "Uraí", db.selectEstado(18)));
                db.addCidade(new Cidade(3176, "Ventania", db.selectEstado(18)));
                db.addCidade(new Cidade(3177, "Vera Cruz do Oeste", db.selectEstado(18)));
                db.addCidade(new Cidade(3178, "Verê", db.selectEstado(18)));
                db.addCidade(new Cidade(3179, "Virmond", db.selectEstado(18)));
                db.addCidade(new Cidade(3180, "Vitorino", db.selectEstado(18)));
                db.addCidade(new Cidade(3181, "Wenceslau Braz", db.selectEstado(18)));
                db.addCidade(new Cidade(3182, "Xambrê", db.selectEstado(18)));

                cidades = db.listAllCidades();
                //Log.i("INFO CIDADES TRY CIDADES", "FINALIZADO INSERT Nº " + i);
                //Log.i("INFO CIDADES LISTCIDADES", String.valueOf(cidades.size()));
            } catch (Exception e) {
                //Log.e("INFO INSERT CIDADES Nº " + i, e.getMessage());
            }
        }


    }
}
