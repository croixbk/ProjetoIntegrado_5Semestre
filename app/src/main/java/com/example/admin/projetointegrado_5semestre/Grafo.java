package com.example.admin.projetointegrado_5semestre;

import java.util.LinkedHashSet;

/**
 * Created by Yuri on 29/04/2016.
 */

public class Grafo {

    int nVertices;
    float[][] matrizAdjacencia;
    float [] distancias;
    float [] distanciasDescrescente;
    float [] rota;
    float [] rotaDecrescente;
    int verticeA;
    int verticeExcluido;

    public Grafo(float[][] matrizAdjacencia){
        //-1 pois no inicio dos tempos nenhum vertice sera excluido
        verticeExcluido = -1;
        this.matrizAdjacencia = matrizAdjacencia;
        this.nVertices = matrizAdjacencia.length;
    }

    protected void caminhoMinimo(int verticeA, int verticeB , int verticeExcluido){
                caminhoMinimo(verticeA,verticeB, verticeExcluido,true);
    }

    protected void caminhoMinimo(int verticeA, int verticeB, int verticeExcluido, boolean crescente){
        this.verticeA = verticeA;
        this.verticeExcluido = verticeExcluido;
        verticeA = verticeA - 1;//-1 para funcionar com os vetores(0...)
        verticeExcluido = verticeExcluido -1;
        boolean decrescenteExecutado = false;

        float[][] matriz = new float[nVertices][nVertices]; //Copia matriz para possiveis mudanças
        for (int j = 0; j < nVertices; j++) {
            for (int i = 0; i < nVertices; i++) {
                matriz[i][j] = matrizAdjacencia[i][j];
            }
        }

        if(verticeExcluido > -1) {//se o vertice excluido é valido(ou existe)
            //todos os vertices que incidem ou são incididos por ele recebem infinito
            //anulando assim seu uso como caminho minimo
            for (int j = 0; j < nVertices; j++) {
                if(matriz[verticeExcluido][j] < Float.POSITIVE_INFINITY)
                    matriz[verticeExcluido][j] = Float.POSITIVE_INFINITY;
            }
            for (int i = 0; i < nVertices; i++) {
                if(matriz[i][verticeExcluido] < Float.POSITIVE_INFINITY)
                    matriz[i][verticeExcluido] = Float.POSITIVE_INFINITY;
            }

        }


        if(crescente) {
            distancias = new float[nVertices];//inicializacao
            rota = new float[nVertices];
        }
        distanciasDescrescente = new float[nVertices];//inicializacao
        rotaDecrescente = new float[nVertices];
        LinkedHashSet<Float> abertos = new LinkedHashSet<>();
        LinkedHashSet<Float> s = new LinkedHashSet<>();
        //s.add((float)verticeA);
        for (int i = 0; i < nVertices; i++) {
            if (crescente)
                distancias[i] = Float.POSITIVE_INFINITY;//coloca infinito em todas as dist
            distanciasDescrescente[i] = Float.POSITIVE_INFINITY;//coloca infinito em todas as dist
            abertos.add((float) i);//adiciona de 0..n de vertices que representam sua posicao no vetor

        }

        distancias[verticeA] = 0;//adiciona 0 a distancia do vertice que sera pego a distancia
        distanciasDescrescente[verticeA] = 0;//adiciona 0 a distancia do vertice que sera pego a distancia

        while (!abertos.isEmpty()) {//enquanto ainda tiverem abertos
            float r = Float.POSITIVE_INFINITY;
            Float[] temp = abertos.toArray(new Float[abertos.size()]);//transforma os abertos em array para comparacao

            if(crescente) {
                for (int i = 0; i < temp.length; i++) {
                    if(r != Float.POSITIVE_INFINITY && r == distancias[temp[i].intValue()]) {
                        caminhoMinimo(this.verticeA, verticeB, this.verticeExcluido, false);
                        decrescenteExecutado = true;
                    }
                    if (r != Math.min(r, distancias[temp[i].intValue()])) {//a vertice com menor distancia dentre os abertos eh adicionado a r
                        r = temp[i].intValue();
                    }
                }
            }
            else {
                for (int i = abertos.size() - 1; i >= 0; i--) {
                    if (r != Math.min(r, distanciasDescrescente[temp[i].intValue()]))//a vertice com menor distancia dentre os abertos eh adicionado a r
                        r = temp[i].intValue();
                }
            }

            //se o r for infinito os abertos só contem um vertice
            //e ele é o vertice excluido,
            //todos os outros foram verificados logo fim do algoritmo
            if(r == Float.POSITIVE_INFINITY)
                break;

            abertos.remove(r);

            s = new LinkedHashSet<Float>(abertos);
            s.retainAll(getVizinhos((int) r,matriz));//deixa somente os vizinhos de r no vetor s

            if(crescente) {
                for (Float i : s) {//para cada i em s
                    float p = Math.min(distancias[i.intValue()], (distancias[(int) r] + matriz[(int) r][i.intValue()]));
                    if (p < distancias[i.intValue()]) {
                        distancias[i.intValue()] = p;
                        rota[i.intValue()] = r + 1;//+1 para manter a integridade 1....n
                    }
                }
            }
            else {
                for (Float i : s) {//para cada i em s
                    float p = Math.min(distanciasDescrescente[i.intValue()], (distanciasDescrescente[(int) r] + matriz[(int) r][i.intValue()]));
                    if (p < distanciasDescrescente[i.intValue()]) {
                        distanciasDescrescente[i.intValue()] = p;
                        rotaDecrescente[i.intValue()] = r + 1;//+1 para manter a integridade 1....n
                    }
                }
            }
        }

        if(decrescenteExecutado){
            if(distancias[verticeB-1] > distanciasDescrescente[verticeB-1]){
                for(int i = 0; i < rota.length ; i++){
                    rota[i] = rotaDecrescente[i];
                    distancias[i] = distanciasDescrescente[i];
                }
            }
        }
    }

    //retorna a distancia para um vertice
    public int getDistancia(int verticeB){
        return (int) distancias[verticeB-1];
    }

    //retorna uma lista hash com o caminho para o vertice b
    public LinkedHashSet getRotaTo(int verticeB){
        LinkedHashSet<Integer> temp = new LinkedHashSet<>();
        temp.add(verticeB);
        for(int i = verticeB-1; i != verticeA-1; ){
            i = (int) rota[i]-1;
            temp.add((i+1));
        }
        return temp;

    }

    //imprime a rota no formato [1,2,3....] onde os numeros são as
    //posiçoes dos vertices a partir de 1(não 0)
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

    //retorna o vetor de rotas como lista hash
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
            //faz duas vezes pois e um grafo nao direcionado logo deve se checar coluna e linha
            //(trivial na maioria das vezes)
            if (matriz[vertice][i] != Float.POSITIVE_INFINITY && !resul.contains(i))//adiciona somente se ele jã não existir
                resul.add((float) i);
            if (matriz[i][vertice] != Float.POSITIVE_INFINITY && !resul.contains(i))
                resul.add((float) i);
        }
        return resul;
    }

}
