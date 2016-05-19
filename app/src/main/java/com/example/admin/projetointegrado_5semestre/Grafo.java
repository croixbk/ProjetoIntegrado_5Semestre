package com.example.admin.projetointegrado_5semestre;

import java.util.LinkedHashSet;

/**
 * Created by Yuri on 29/04/2016.
 */

public class Grafo {

    int nVertices;
    float[][] matrizAdjacencia;
    float [] distancias;
    float [] rota;
    int verticeA;
    int verticeExcluido;

    public Grafo(float[][] matrizAdjacencia){
        verticeExcluido = -1;
        this.matrizAdjacencia = matrizAdjacencia;
        this.nVertices = matrizAdjacencia.length;
    }

    protected void caminhoMinimo(int verticeA, int verticeExcluido){
            int nVerticesAtual = nVertices;
            this.verticeA = verticeA;
            this.verticeExcluido = verticeExcluido;
            verticeA = verticeA - 1;//-1 para funcionar com os vetores
            verticeExcluido = verticeExcluido -1;


            float[][] matriz = new float[nVerticesAtual][nVerticesAtual]; //Copia matriz trivial
            for (int j = 0; j < nVerticesAtual; j++) {
                for (int i = 0; i < nVerticesAtual; i++) {
                    matriz[i][j] = matrizAdjacencia[i][j];
                }
            }

            if(verticeExcluido > -1) {//se o vertice excluido é valido

                for (int j = 0; j < nVerticesAtual; j++) {
                    if(matriz[verticeExcluido][j] < Float.POSITIVE_INFINITY)
                        matriz[verticeExcluido][j] = Float.POSITIVE_INFINITY;
                }
                for (int i = 0; i < nVerticesAtual; i++) {
                    if(matriz[i][verticeExcluido] < Float.POSITIVE_INFINITY)
                        matriz[i][verticeExcluido] = Float.POSITIVE_INFINITY;
                }

            }


            distancias = new float[nVerticesAtual];//inicializacao
            rota = new float[nVertices];
            LinkedHashSet<Float> abertos = new LinkedHashSet<>();
            LinkedHashSet<Float> s = new LinkedHashSet<>();
            //s.add((float)verticeA);
            for (int i = 0; i < nVerticesAtual; i++) {
                distancias[i] = Float.POSITIVE_INFINITY;//coloca infinito em todas as dist
                abertos.add((float) i);//adiciona de 0..n de vertices que representam sua posicao no vetor

            }

            distancias[verticeA] = 0;//adiciona 0 a distancia do vertice que sera pego a distancia
            while (!abertos.isEmpty()) {//enquanto ainda tiverem abertos
                float r = Float.POSITIVE_INFINITY;
                Float[] temp = abertos.toArray(new Float[abertos.size()]);//transforma os abertos em array para comparacao
                for (int i = 0; i < abertos.size(); i++) {
                    if (r != Math.min(r, distancias[temp[i].intValue()]))//a vertice com menor distancia dentre os abertos eh adicionado a r
                        r = temp[i].intValue();

                }

                //se o r for infinito ele é o vertice excluido e todos os outros foram verificados então fim do algoritmo
                if(r == Float.POSITIVE_INFINITY)
                    break;

                abertos.remove(r);

                s = new LinkedHashSet<Float>(abertos);
                s.retainAll(getVizinhos((int) r,matriz));//deixa somente os vizinhos de r no vetor s

                for (Float i : s) {//para cada i em s
                    float p = Math.min(distancias[i.intValue()], (distancias[(int) r] + matriz[(int) r][i.intValue()]));
                    if (p < distancias[i.intValue()]) {
                        distancias[i.intValue()] = p;
                        rota[i.intValue()] = r + 1;//+1 para manter a integridade 1....n
                    }
                }

            }

    }

    public int getDistancia(int verticeB){
        return (int) distancias[verticeB-1];
    }

    public LinkedHashSet getRotaTo(int verticeB){
        LinkedHashSet<Integer> temp = new LinkedHashSet<>();
        temp.add(verticeB);
        for(int i = verticeB-1; i != verticeA-1; ){
            i = (int) rota[i]-1;
            temp.add((i+1));
        }
        return temp;

    }

    public String imprimirRota(int verticeB){
        return imprimirRota(getRotaTo(verticeB));

    }

    private String imprimirRota(LinkedHashSet temp){
        String resultado = "";
        resultado += "Rota: [ ";
        Integer[] resultRota = (Integer[]) temp.toArray(new Integer[temp.size()]);
        for(int i = resultRota.length-1; i >= 0; i--){
            if(i == 0)
                resultado += resultRota[i];
            else
                resultado += resultRota[i]+", ";

        }

        resultado += " ]";
        return  resultado;

    }

    public LinkedHashSet getRotaTotal(){
        LinkedHashSet<Integer> temp = new LinkedHashSet<>();
        for (int i = 0; i < rota.length; i++){
            temp.add((int)rota[i]+1);
        }
        return temp;
    }

    public LinkedHashSet<Float> getVizinhos(int vertice, float[][] matriz){
        LinkedHashSet<Float> resul = new LinkedHashSet<Float>();
        for (int i = 0; i < matriz.length; i++){
            if (matriz[vertice][i] != Float.POSITIVE_INFINITY && !resul.contains(i))
                resul.add((float) i);
            if (matriz[i][vertice] != Float.POSITIVE_INFINITY && !resul.contains(i))
                resul.add((float) i);
        }
        return resul;
    }

}
