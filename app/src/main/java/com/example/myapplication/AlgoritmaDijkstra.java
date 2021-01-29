package com.example.myapplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class AlgoritmaDijkstra {
    static int jumlNode = 42;
    static int src = 0;

    int minDistance(int dist[], Boolean sptSet[]){
        int min = Integer.MAX_VALUE, min_index = 0;

        for (int v = 0; v < jumlNode; v++) {
            if (sptSet[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
        }
        return min_index;
    }

    void printSolution(int dist[], int n) {
        for (int i = 0; i < jumlNode; i++) {
            System.out.print(dist[i] + ",");
        }
    }

    void dijkstra(int graph[][], int src) {
        int dist[] = new int[jumlNode]; // array yang berfungsi untuk menyimpan
        // jalur terpendek dari source ke i

        // sptSet[i] akan bernilai true jika jarak terpendek dari node src
        // ke node i sudah final
        Boolean sptSet[] = new Boolean[jumlNode];

        // Initialize all distances as INFINITE and stpSet[] as false
        for (int i = 0; i < jumlNode; i++) {
            dist[i] = Integer.MAX_VALUE;
            sptSet[i] = false;
        }

        // jarak dari node sumber ke node yang sama selalu bernilai 0
        dist[src] = 0;

        // Temukan jalur terpendek dari node sumber ke semua node
        for (int count = 0; count < jumlNode - 1; count++) {
            // Pilih jalur terpendek ke semua node
            // Pada iterasi pertama, u selalu sama dengan source
            int u = minDistance(dist, sptSet);

            // Tandai node yang telah diproses
            sptSet[u] = true;

            // update nilai dist
            for (int v = 0; v < jumlNode; v++)
            // Update dist[v] jika node terkait belum masuk sptSet
            // Terdapat jalur dari u ke v, dan terdapat total jarak dari
            // src ke v melalui u
            {
                if (!sptSet[v] && graph[u][v] != 0
                        && dist[u] != Integer.MAX_VALUE
                        && dist[u] + graph[u][v] < dist[v]) {
                    dist[v] = dist[u] + graph[u][v];
                }
            }
        }
        // print the constructed distance array
        printSolution(dist, jumlNode);
    }

    static int[][] arrNode = new int[jumlNode][jumlNode];

    public static void main(String[] args) throws FileNotFoundException, IOException, URISyntaxException {

        String thisLine;
        BufferedReader nodeMatrix = null;
        try {
            InputStream is = AlgoritmaDijkstra.class.getClassLoader().getResourceAsStream("res/raw/matriks_jarak_titik_terhubung.csv");
            nodeMatrix = new BufferedReader(new InputStreamReader(is));
        }catch (Exception e) {
            throw e;
        }


        /*BufferedReader nodeMatrix = new BufferedReader
                (new FileReader("F:/punya Shinta/TugasAkhirImad/TugasAkhirImad/data/matriks_jarak_titik_terhubung.csv"));
*/
        ArrayList<String[]> lines = new ArrayList<>();
        while ((thisLine = nodeMatrix.readLine()) != null) {
            lines.add(thisLine.split(","));
        }

        //Convert our list to a String array
        String[][] array = new String[lines.size()][0];
        lines.toArray(array);

        //Convert String array to Integer array
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                arrNode[i][j] = Integer.parseInt(array[i][j]);
            }
        }

        AlgoritmaDijkstra t = new AlgoritmaDijkstra();
        for (int i = 0; i < jumlNode; i++) {
            t.dijkstra(arrNode, i);
            System.out.println();
        }

    }
}
