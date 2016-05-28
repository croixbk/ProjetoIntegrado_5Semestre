package com.example.admin.projetointegrado_5semestre;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FragCaminhoMinimo extends Fragment {

    SimpleCursorAdapter adapter;
    Spinner spinnerA;
    Spinner spinnerB;
    Spinner spinnerExcluir;
    RadioGroup radioGroup;

    Cursor spinnerCursor;
    int verticeA;
    int verticeB;
    int verticeCancelado;
    String metrica;
    Button btnImprimirGrafo;

    caminhoMinimoCallBackListener callBackCaminhoMinimo;

    //valor padrao dos vertices caso não informados
    public FragCaminhoMinimo(){
        verticeA = 0;
        verticeB = 0;
        verticeCancelado = 4;

    }

    //opcionalmente podem ser inseridos para mudar o valor default nos spinners
    public void carregarDados(int vertA, int vertB, int vertExclu){
        verticeA = vertA;
        verticeB = vertB;
        verticeCancelado = vertExclu;
    }

    public interface caminhoMinimoCallBackListener{
        public void updateMapaInfo(Integer[] rota, int dist, String disText);
        public void salvarDados(int vertA, int vertB, int vertExclu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callBackCaminhoMinimo = (caminhoMinimoCallBackListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement the Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento_custo_minimo, container, false);

    }



    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        metrica = "nr_hop";
        loadSpinners();
        radioGroup = ((RadioGroup)getView().findViewById(R.id.radioGroup_custos));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radio_hops)
                    metrica = "nr_hop";
                else if(checkedId == R.id.radio_distancia)
                    metrica = "nr_distancia";
                else if(checkedId == R.id.radio_custo)
                    metrica = "nr_custo";
            }
        });
        //não colocar o onClick no XML, se colocar
        //o onClick vai procurar o metodo na activity não no fragment
        btnImprimirGrafo = ((Button)getView().findViewById(R.id.btn1));
        btnImprimirGrafo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imprimeGrafo(v);
            }
        });
    }

    //carrega os tres spinners com a informação do banco
    void loadSpinners(){
        DatabaseOpenHelper doh = new DatabaseOpenHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = doh.getReadableDatabase();
        try{
            //apelida o id_pops de "_id" pois o cursorAdapter precisa
            //obrigatoriamente do campo _id para funcionar
            spinnerCursor = db.rawQuery("SELECT nome_pops,id_pops _id FROM pops",null);
            adapter = new SimpleCursorAdapter(getContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerCursor,
                    new String[]{"nome_pops"},
                    new int[]{android.R.id.text1},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerA = ((Spinner)getView().findViewById(R.id.spinVertA));
            spinnerB = ((Spinner)getView().findViewById(R.id.spinVertB));
            spinnerExcluir = ((Spinner)getView().findViewById(R.id.spinVertExcluido));
            spinnerA.setAdapter(adapter);
            spinnerB.setAdapter(adapter);
            spinnerExcluir.setAdapter(adapter);
            spinnerExcluir.setSelection(verticeCancelado-1);
            spinnerA.setSelection(verticeA-1);
            spinnerB.setSelection(verticeB-1);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            doh.close();
            db.close();
        }
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //+1 pois a posicão dos spinners são 0....
                verticeA = position+1;
                if(verticeCancelado == verticeA || verticeCancelado == verticeB){
                    verticeCancelado = -1;
                    //String auto explicativa
                    Snackbar.make(getView(), "Vertices com falha na origem ou destino não serão considerados", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                if(verticeCancelado == -1)
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,spinnerExcluir.getSelectedItemPosition()+1);
                else
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,verticeCancelado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                verticeB = position+1;
                if(verticeCancelado == verticeA || verticeCancelado == verticeB){
                    verticeCancelado = -1;
                    Snackbar.make(getView(), "Vertices com falha na origem ou destino não serão considerados", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                if(verticeCancelado == -1)
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,spinnerExcluir.getSelectedItemPosition()+1);
                else
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,verticeCancelado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerExcluir.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                verticeCancelado = position+1;
                if(verticeCancelado == verticeA || verticeCancelado == verticeB){
                    //invalida o vertice se ele for igual a origem ou destino
                    verticeCancelado = -1;
                    Snackbar.make(getView(), "Vertices com falha na origem ou destino não serão considerados", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                if(verticeCancelado == -1)
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,spinnerExcluir.getSelectedItemPosition()+1);
                else
                    callBackCaminhoMinimo.salvarDados(verticeA,verticeB,verticeCancelado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void imprimeGrafo(View view){
        String distText = "";
        Grafo grafo = new Grafo(gerarMatriz(metrica));

        if(verticeCancelado == verticeA || verticeCancelado == verticeB){
            //invalida o vertice se ele for igual a origem ou destino
            verticeCancelado = -1;
            Snackbar.make(getView(), "Vertices com falha na origem ou destino não serão considerados", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        callBackCaminhoMinimo.salvarDados(verticeA,verticeB,spinnerExcluir.getSelectedItemPosition()+1);
        grafo.caminhoMinimo(verticeA,verticeCancelado);

        //prepara o texto de exibição de acordo com a metrica
        if(metrica.equals("nr_hop") )
            distText = "Numero de Hops: ";
        else if(metrica.equals("nr_distancia"))
            distText = "Distancia total: ";
        else
            distText = "Custo Total: ";

        //basicamente retorna o caminho adiquirido para activity principal

        callBackCaminhoMinimo.updateMapaInfo(
                (Integer[]) grafo.getRotaTo(verticeB).toArray(new Integer [grafo.getRotaTo(verticeB).size()])
                , grafo.getDistancia(verticeB)
                , distText );

        //pega os nomes dos pops no banco e prepara a string de resultado
        String resul = "";
        Cursor cursor;
        DatabaseOpenHelper dboh = new DatabaseOpenHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = dboh.getReadableDatabase();
        try{
            cursor = db.query("pops",null,null,null,null,null,null);
            Integer[] rota = (Integer[]) grafo.getRotaTo(verticeB).toArray(new Integer[grafo.getRotaTo(verticeB).size()]);
            Integer[] temp = new Integer[rota.length];
            int j = 0;
            for(int i = temp.length-1; i >= 0; i--){
                temp[j] = rota[i];
                j++;
            }

            j = 0;//para reuso da variavel
            cursor.moveToFirst();
            while (j < temp.length){
                if(cursor.getInt(1)+1 == temp[j]){
                    if(j == temp.length-1)
                        resul += cursor.getString(0);
                    else
                        resul += cursor.getString(0)+",";
                    j++;
                    cursor.moveToFirst();
                }else
                    cursor.moveToNext();

            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            dboh.close();
            db.close();
        }


        ((TextView)getView().findViewById(R.id.txtResul)).setText(resul+"\n" +
                distText+ "[ "+grafo.getDistancia(verticeB)+" ]");

    }

    public float[][] gerarMatriz(String opt){
        float[][] resul = null;
        DatabaseOpenHelper doh = new DatabaseOpenHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;
        try{
            //pega todos os vertices do banco e seus valor de metrica
            cursor = db.query("enlaces", new String[]{"id_enlaces_a","id_enlaces_b",opt}, null, null, null, null,null);
            resul = new float[20][20];
            while(cursor.moveToNext()){
                //ambos os vertices devem receber o custo pois é um grafo bidirecional(ou não direcionado)
                resul[cursor.getInt(0)][cursor.getInt(1)] = cursor.getInt(2);
                resul[cursor.getInt(1)][cursor.getInt(0)] = cursor.getInt(2);
            }
            //todos que não tem valor definido recebem infinito
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    if (resul[i][j] <= 0)
                        resul[i][j] = Float.POSITIVE_INFINITY;
                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            cursor.close();
            db.close();
            doh.close();
        }
        return resul;
    }

}
