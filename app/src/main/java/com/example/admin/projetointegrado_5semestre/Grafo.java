package com.example.admin.projetointegrado_5semestre;

import java.util.LinkedHashSet;

/**
 * Created by Yuri on 29/04/2016.
 */
public class Grafo {

    int nVertices;
    int nArestas;
    float[][] matrizAdjacencia ={
            {Float.POSITIVE_INFINITY, 1 , Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 1},
            {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 1, 2, Float.POSITIVE_INFINITY},
            {Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 4, 2},
            {3, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY},
            {2, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, 1, Float.POSITIVE_INFINITY}

    };

    public Grafo(){
        nVertices = 5;
        nArestas = 9;

    }

    protected String caminhoMinimo(int verticeA, int verticeB){
        verticeA = verticeA-1;//-1 para funcionar com os vetores
        String resultado = "";

        float[][] matriz = new float[nVertices][nVertices]; //Copia matriz(trivial)
        for(int j = 0; j < nVertices; j++){
            for(int i = 0; i< nVertices ; i++){
                matriz [i][j] = matrizAdjacencia[i][j];
            }
        }

        float[] distancias = new float[nVertices];//inicializacao
        float[] rota = new float[nVertices];
        LinkedHashSet<Float> abertos = new LinkedHashSet<>();
        LinkedHashSet<Float> s = new LinkedHashSet<>();
        //s.add((float)verticeA);
        for (int i = 0; i < nVertices ; i++){
            distancias[i] = Float.POSITIVE_INFINITY;//coloca infinito em todas as dist
            abertos.add((float)i);//adiciona de 0..n de vertices que representam sua posicao no vetor
            rota[i] = 0;
        }
        distancias[verticeA] = 0;//adiciona 0 a distancia do vertice que sera pego a distancia
        while (!abertos.isEmpty()){//enquanto ainda tiverem abertos
            float r = Float.POSITIVE_INFINITY;
            Float[] temp = abertos.toArray(new Float[abertos.size()]);//transforma os abertos em array para comparacao
            for(int i = 0; i < abertos.size(); i++){
                if(r != Math.min(r,distancias[temp[i].intValue()]))//a vertice com menor distancia dentre os abertos eh adicionado a r
                    r = temp[i].intValue();

            }
            abertos.remove(r);

            s = new LinkedHashSet<Float>(abertos);
            s.retainAll(getIncidents((int)r));//deixa somente os adjacentes de r no vetor s

            for (Float i: s) {//para cada i em s
                float p = Math.min(distancias[i.intValue()],(distancias[(int)r] + matrizAdjacencia[(int)r][i.intValue()]));
                if(p < distancias[i.intValue()]) {
                    distancias[i.intValue()] = p;
                    rota[i.intValue()] = r+1;//+1 para manter a integridade 1....n
                }
            }
        }


        LinkedHashSet<Integer> temp = new LinkedHashSet<>();
        temp.add(verticeB);
        resultado += "Rota: [ ";
        int valorDist = (int)distancias[verticeB-1];
        for(int i = verticeB-1; i != verticeA; ){
            i = (int) rota[i]-1;
            temp.add((i+1));
        }
        Integer[] resultRota = temp.toArray(new Integer[abertos.size()]);
        for(int i = resultRota.length-1; i >= 0; i--){
            if(i == 0)
                resultado += resultRota[i];
            else
                resultado += resultRota[i]+", ";

        }

        resultado += " ]\nDistancia: [ "+valorDist+" ]";
        return resultado;
    }

    public LinkedHashSet<Float> getIncidents(int vertice){
        LinkedHashSet<Float> resul = new LinkedHashSet<Float>();
        for (int i = 0; i < nVertices; i++){
            if(matrizAdjacencia[vertice][i] != Float.POSITIVE_INFINITY)
                resul.add((float)i);
        }
        return resul;
    }
}
