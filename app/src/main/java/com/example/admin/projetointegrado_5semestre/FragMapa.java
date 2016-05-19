package com.example.admin.projetointegrado_5semestre;

import android.content.Context;
import android.graphics.Canvas;
import java.lang.Object;
import java.util.ArrayList;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragMapa.OnMapPressedListener} interface
 * to handle interaction events.
 */
public class FragMapa extends Fragment implements OnMapReadyCallback {

    LatLng[] lugares = new LatLng[20];

    private LatLng[] linesToDraw;
    private GoogleMap mMap;
    private OnMapPressedListener mListener;
    private String distText;
    private int totalDist;

    public FragMapa() {
        totalDist = -1;
        distText = "";
        LatLng manaus = new LatLng(-3.124538, -60.024442);
        LatLng belem = new LatLng(-1.459480, -48.490740);
        LatLng natal = new LatLng(-5.781082, -35.200252);
        LatLng recife = new LatLng(-8.059795, -34.882527);
        LatLng cuiaba = new LatLng(-15.603246, -56.097675);
        LatLng brasilia = new LatLng(-15.797615, -47.882234);
        LatLng beloHorizonte = new LatLng(-19.920916, -43.936929);
        LatLng riodejaneiro = new LatLng(-22.908038, -43.173439);
        LatLng saopaulo = new LatLng(-23.560112, -46.637225);
        LatLng campogrande = new LatLng(-20.483455, -54.623750);
        LatLng portoalegre = new LatLng(-30.036870, -51.218883);
        LatLng florianopolis = new LatLng(-27.597848, -48.548556);
        LatLng blumenau = new LatLng(-26.921410, -49.072415);
        LatLng londrina = new LatLng(-23.305532, -51.169759);
        LatLng curitba = new LatLng(-25.431754, -49.268769);
        LatLng saojosedoscampos = new LatLng(-23.230195, -45.904557);
        LatLng bauru = new LatLng(-22.321467, -49.059660);
        LatLng ribeiraopreto = new LatLng(-21.178157, -47.811963);
        LatLng campinas = new LatLng(-22.913652, -47.062925);
        LatLng salvador = new LatLng(-12.980613, -38.502740);

        lugares[14] = manaus;
        lugares[15] = belem;
        lugares[17] = natal;
        lugares[18] = recife;
        lugares[13] = cuiaba;
        lugares[16] = brasilia;
        lugares[8] = beloHorizonte;
        lugares[7] = riodejaneiro;
        lugares[5] = saopaulo;
        lugares[12] = campogrande;
        lugares[0] = portoalegre;
        lugares[1] = florianopolis;
        lugares[2] = blumenau;
        lugares[4] = londrina;
        lugares[3] = curitba;
        lugares[6] = saojosedoscampos;
        lugares[11] = bauru;
        lugares[10] = ribeiraopreto;
        lugares[9] = campinas;
        lugares[19] = salvador;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_maps, container, false);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.OnMapPressed(uri);
        }
    }


    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        //chamara o onMapReady quando ele estiver pronto (child pois o fragmento esta dentro da activity)
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMapPressedListener) {
            mListener = (OnMapPressedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for(int i = 0; i <lugares.length; i++){
            mMap.addMarker(new MarkerOptions().position(lugares[i]).title("Ponto Legal"));//adiciona todos os vertices
        }

        if(linesToDraw != null)//se existe uma rota para desenhar
            mMap.addPolyline(new PolylineOptions().add(linesToDraw).width(6).color(Color.BLACK).geodesic(true));//adiciona as rotas em ordem

        //se tem uma distancia para mostrar
        if(totalDist != -1)
            Snackbar.make(getView(), ""+distText+totalDist, Snackbar.LENGTH_INDEFINITE).setAction("Action",null).show();//mostra a distancia total em uma snackBar

        mMap.moveCamera(CameraUpdateFactory.newLatLng(lugares[0]));
    }

    public void setLinesToDraw(Integer[] rota) {
        linesToDraw = new LatLng[rota.length];
        for(Integer i=0; i< rota.length; i++){
            linesToDraw[i] = lugares[rota[i]-1];//preenche o vetor de rotas com as coordenadas dos vertices (-1 pois os numeros são passados de 1,2...)
        }
    }

    public void setDistText(String distText) {
        this.distText = distText;
    }

    public void setTotalDist(int totalDist) {
        this.totalDist = totalDist;
    }

    public interface OnMapPressedListener {
        // TODO: Update argument type and name
        void OnMapPressed(Uri uri);
    }

}

