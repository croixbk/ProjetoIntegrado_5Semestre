package com.example.admin.projetointegrado_5semestre;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;

public class MainActivity extends AppCompatActivity {

    SimpleCursorAdapter adapter;
    Spinner spinnerA;
    Spinner spinnerB;

    Cursor spinnerCursor;
    int verticeA;
    int verticeB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseOpenHelper doh = new DatabaseOpenHelper(getApplicationContext());
        SQLiteDatabase db = doh.getReadableDatabase();
        try{
            spinnerCursor = db.rawQuery("SELECT nome_pops,id_pops _id FROM pops",null);
            adapter = new SimpleCursorAdapter(getApplicationContext(),
                    android.R.layout.simple_spinner_item,
                    spinnerCursor,
                    new String[]{"nome_pops"},
                    new int[]{android.R.id.text1},
                    SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerA = ((Spinner)findViewById(R.id.spinVertA));
            spinnerB = ((Spinner)findViewById(R.id.spinVertB));
            spinnerA.setAdapter(adapter);
            spinnerB.setAdapter(adapter);


        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            doh.close();
            db.close();
        }
        spinnerA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                verticeA = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerB.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                verticeB = position+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void imprimeGrafo(View view){
      //  spinnerA.setSelection(2,false);

        Grafo grafo = new Grafo(gerarMatriz("nr_distancia"));

        grafo.caminhoMinimo(verticeA);

        ((TextView)findViewById(R.id.txtResul)).setText(grafo.imprimirRota(verticeB)+"\n" +
                "Distancia: [ "+grafo.getDistancia(verticeB)+" ]");

    }

    public float[][] gerarMatriz(String opt){
        float[][] resul = null;
        DatabaseOpenHelper doh = new DatabaseOpenHelper(getApplicationContext());
        SQLiteDatabase db = doh.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query("enlaces", new String[]{"id_enlaces_a","id_enlaces_b",opt}, null, null, null, null,null);
            resul = new float[20][20];
            while(cursor.moveToNext()){
                resul[cursor.getInt(0)][cursor.getInt(1)] = cursor.getInt(2);
            }
            for(int i = 0; i < 20; i++){
                for(int j = 0; j < 20; j++){
                    if (resul[i][j] <= 0)
                        resul[i][j] = Float.POSITIVE_INFINITY;
                }
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {
            cursor.close();
            db.close();
            doh.close();
        }

        return resul;
    }
}
