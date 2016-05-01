package com.example.admin.projetointegrado_5semestre;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
        ((TextView)findViewById(R.id.txtResul)).setText(grafo.caminhoMinimo(1,4));

    }
}
