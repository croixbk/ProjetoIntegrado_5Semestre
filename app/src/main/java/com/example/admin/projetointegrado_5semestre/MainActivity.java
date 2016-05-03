package com.example.admin.projetointegrado_5semestre;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedHashSet;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void imprimeGrafo(View view){
        Grafo grafo = new Grafo();
        int verticeA = Integer.parseInt(((EditText)findViewById(R.id.edtVertA)).getText().toString());
        int verticeB = Integer.parseInt(((EditText)findViewById(R.id.edtVertB)).getText().toString());

        ((TextView)findViewById(R.id.txtResul)).setText(grafo.caminhoMinimo(verticeA,verticeB));

    }

    public void consultarDB(View view){
        String resul = "";
        try {
            DatabaseOpenHelper doh = new DatabaseOpenHelper(getApplicationContext());
            SQLiteDatabase bd = doh.getReadableDatabase();
            Cursor cursor = bd.query("tabela_teste", null, null, null, null, null, null);
            while (cursor.moveToNext()) {
                resul += "ID: "+cursor.getInt(0) + " Nome: " + cursor.getString(1)+"\n";

            }
            cursor.close();
            bd.close();
            doh.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }finally {

        }
        ((TextView)findViewById(R.id.txtConsulta)).setText(resul);
    }
}
