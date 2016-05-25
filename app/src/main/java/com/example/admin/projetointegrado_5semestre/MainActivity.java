package com.example.admin.projetointegrado_5semestre;

import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements
        FragCaminhoMinimo.caminhoMinimoCallBackListener
        , NavigationView.OnNavigationItemSelectedListener
        , FragMapa.OnMapPressedListener{

    Integer[] rotaMapa;
    int dist;
    String distText;
    int oldVertA, oldVertB, oldVertExclu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        oldVertA = -1;
        oldVertB = -1;
        oldVertExclu = -1;

        //preara a toolbar(as tres linhas do lado superior esquerdo)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        toggle.syncState();

        //dentro do drawer layout podem ser usados outros tipos
        //além do navigation view, como o listView
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //adiciona o fragmento do mapa no container
        FragMapa fragMapa = new FragMapa();
        getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainer, fragMapa).commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_opt1){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragMapa fragMapa = new FragMapa();
            if(rotaMapa != null){//se existe uma para imprimir no mapa
                fragMapa.setLinesToDraw(rotaMapa);//antes de iniciar o fragmento prepara a rota que sera desenhada
                fragMapa.setTotalDist(dist);
                fragMapa.setDistText(distText);
            }
            transaction.replace(R.id.fragmentContainer, fragMapa);
            transaction.addToBackStack(null);//addToBackStack para poder voltar para tela anterior caso o botao voltar seja precionado
            transaction.commit();
            //fecha o drawer após selecionar a opção
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }else if(id == R.id.nav_opt2) {
            Snackbar temp = Snackbar.make(findViewById(R.id.fragmentContainer),"",Snackbar.LENGTH_SHORT);
            temp.show();
            temp.dismiss();
            //carrega o fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragCaminhoMinimo fragCaminhoMinimo = new FragCaminhoMinimo();
            if(oldVertA != -1)
                fragCaminhoMinimo.carregarDados(oldVertA,oldVertB,oldVertExclu);
            transaction.replace(R.id.fragmentContainer, fragCaminhoMinimo);
            transaction.addToBackStack(null);
            transaction.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        }else if(id == R.id.nav_sobre){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            FragSobre fragSobre = new FragSobre();
            transaction.replace(R.id.fragmentContainer, fragSobre);
            transaction.addToBackStack(null);
            transaction.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void OnMapPressed(Uri uri) {

    }

    @Override
    public void updateMapaInfo(Integer[] rota, int dist, String disText) {
        rotaMapa = rota;
        this.distText = disText;
        this.dist = dist;
    }

    @Override
    public void salvarDados(int vertA, int vertB, int vertExclu) {
        oldVertA = vertA;
        oldVertB = vertB;
        oldVertExclu = vertExclu;
    }
}
