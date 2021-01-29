package com.example.myapplication;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AGHH {
    static int titikAwal = 1; //Menetapkan titik awal keberangkatan bis kota
    static int titikAkhir = 41; //Menetapkan titik akhir perjalanan bis kota
    static int jumlahTitik = 42; //Jumlah titik (objek) yang digunakan
    static int tMax = 60; //Menetapkan nilai batasan waktu yang diperbolehkan (dalam satuan menit)
    static int jumlahPopulasi = 40; //Menetapkan jumlah populasi adalah 40
    static int jumlahGenerasi = 1000; //Menetapkan jumlah generasi/iterasi

    //Membuat array 2D dan 1D untuk waktu tempuh dan skor
    static int[][] nilaiWaktuTempuh = new int[jumlahTitik][jumlahTitik];
    static int[] nilaiSkor = new int[jumlahTitik];

    //Membuat array untuk penyimpanan populasi, hasil seleksi,
    //hasil kawin silang, dan hasil mutasi yang dilakukan.
    static ArrayList<ArrayList<Integer>> ArrayPopulasi
            = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> ArrayPopulasi_WaktuTempuh
            = new ArrayList<Integer>();
    static ArrayList<Integer> ArrayPopulasi_Fitness
            = new ArrayList<Integer>();

    static ArrayList<ArrayList<Integer>> ArraySeleksi
            = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> ArraySeleksi_WaktuTempuh
            = new ArrayList<Integer>();
    static ArrayList<Integer> ArraySeleksi_Fitness
            = new ArrayList<Integer>();

    static ArrayList<ArrayList<Integer>> ArrayKawinSilang
            = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> ArrayKawinSilang_WaktuTempuh
            = new ArrayList<Integer>();
    static ArrayList<Integer> ArrayKawinSilang_Fitness
            = new ArrayList<Integer>();

    static ArrayList<ArrayList<Integer>> ArrayHyper
            = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> ArrayHyper_WaktuTempuh
            = new ArrayList<Integer>();
    static ArrayList<Integer> ArrayHyper_Fitness
            = new ArrayList<Integer>();

    static ArrayList<ArrayList<Integer>> ArrayPenyimpan
            = new ArrayList<ArrayList<Integer>>();
    static ArrayList<Integer> ArrayPenyimpan_WaktuTempuh
            = new ArrayList<Integer>();
    static ArrayList<Integer> ArrayPenyimpan_Fitness
            = new ArrayList<Integer>();

    /**
     @param args the command line arguments
     @throws java.io.FileNotFoundException
     */

    //@SuppressWarnings("unchecked")
    public static void main(String[] args) throws FileNotFoundException,
            IOException, URISyntaxException {

        //TAHAP PERSIAPAN
        long start = System.nanoTime();
        //1. Mengkonversi file .csv Matriks Waktu Tempuh Keseluruhan menjadi
        //   list yang dipisahkan tanda koma (,) untuk waktu tempuh antar titik.
        String thisLine2D;
        //"F:/punya Shinta/TugasAkhirImad/TugasAkhirImad/data/matriks_jarak_keseluruhan.csv"
        ;
        BufferedReader bufferReader2D = null;
        try {
            //R.raw.MatriksJarakKeseluruhan;
            InputStream is = AGHH.class.getClassLoader().getResourceAsStream("res/raw/matriks_jarak_keseluruhan.csv");
            //URL url = AGHH.class.getClassLoader().getResource("res/raw/matriks_jarak_keseluruhan.csv");
            //URI uri = new URI(url.toString()); //Tadi yg error.
            //File file = new File(uri);
            bufferReader2D = new BufferedReader(new InputStreamReader(is));
        }catch (Exception e) {
            throw e;
        }


        ArrayList<String[]> lines = new ArrayList<String[]>();
        while ((thisLine2D = bufferReader2D.readLine()) != null) {
            lines.add(thisLine2D.split(","));
        }

        //1.1 Mengkonversi list menjadi String array
        String[][] stringarray = new String[lines.size()][0];
        lines.toArray(stringarray);

        //1.2. Mengkonversi String array menjadi Integer array dan memasukkan
        //     ke dalam array nilaiWaktuTempuh
        for (int i = 0; i < stringarray.length; i++) {
            for (int j = 0; j < stringarray.length; j++) {
                nilaiWaktuTempuh[i][j] = Integer.parseInt(stringarray[i][j]);
            }
        }

        //2. Mengkonversi file Matriks Skor menjadi
        //   Array 1D untuk skor tiap titik
        BufferedReader bufferReader1D = null;

        try {
            InputStream is = AGHH.class.getClassLoader().getResourceAsStream("res/raw/matriks_skor.csv");
            bufferReader1D = new BufferedReader(new InputStreamReader(is));
        }catch (Exception e) {
            throw e;
        }


        String thisLine1D;
        int o = 0;

        //2.1. Membaca setiap baris yang ada pada file .csv dan memasukkannya
        //     ke dalam array integer nilaiSkor
        while ((thisLine1D = bufferReader1D.readLine()) != null) {
            nilaiSkor[o] = Integer.parseInt(thisLine1D);
            o++;
        }

        //Mencetak hasil dari program yang dijalankan dalam bentuk .txt
        /*File file = new File("F:/punya Shinta/TugasAkhirImad/TugasAkhirImad/data/Hasil Running HH Ubah Titik.txt");
        FileWriter fw = new FileWriter(file);
        PrintWriter pw = new PrintWriter(fw);*/

        //----------------------------------------------------------------------

        //TAHAP PEMBANGKITAN AWAL
        //1. Membangkitkan populasi awal sesuai kriteria secara acak
        int populasi = 0; //Menetapkan inisiasi jumlah individu dalam populasi
        int loopPPA = 0; //Inisiasi looping pencarian populasi awal
        int loopMaksPPA = 100000; //Maksimal looping pencarian populasi awal

        do {

            //1.1. Menghasilkan nilai random, Rn , diantara indeks awal & akhir,
            //     digunakan untuk menentukan panjang solusi yang bervariasi.
            int RN = new Random().nextInt(jumlahTitik - 2) + 1;

            //1.2. Menghasilkan list Rl , diantara [1,N] secara random
            ArrayList<Integer> listRL = new ArrayList();
            //Array yang digunakan untuk mengisi titik random diantara titik awal dan akhir
            ArrayList<Integer> listRL_rand = new ArrayList();

            //1.3. Menggenerate isi listRL_rand diantara titik awal dan akhir
            for (int i = 1; i < jumlahTitik + 1; i++) {
                if (i == titikAwal || i == titikAkhir) {
                    continue; //Memastikan tidak ada titik awal dan akhir
                }             //yang muncul di tengah individu
                listRL_rand.add(i);
            }

            Collections.shuffle(listRL_rand); //Mengacak titik yang digenerate

            //1.4. Mengisi listRL
            listRL.add(titikAwal); //Merepresentasikan node awal
            for (int i = 0; i < listRL_rand.size(); i++) {
                listRL.add(listRL_rand.get(i)); //Representasi node indeks 2
            }                                   //ke N-1 yang telah dibuat acak
            listRL.add(titikAkhir); //Merepresentasikan node akhir

            //1.5. Menghasilkan sebuah sub list, Rs dengan ukuran
            //yang bervariasi sesuai dengan Rn.
            ArrayList<Integer> subListRS = new ArrayList();
            subListRS.add(listRL.get(0));
            for (int i = 1; i < (RN + 1); i++) {
                subListRS.add(listRL.get(i));
            }
            subListRS.add(listRL.get(listRL.size() - 1));

            //1.6. Hanya memasukkan subList Rs (individu) yang unique
            //dan tidak melebihi batasan waktu tMax ke dalam populasi.
            for (int i = 0; i < subListRS.size(); i++) {
                if (hitungWaktuTempuh(subListRS) <= tMax
                        && !ArrayPopulasi.contains(subListRS)) {
                    ArrayPopulasi.add(subListRS);
                    populasi++;
                }
            }

            loopPPA++;

            //Menghentikan pembangkitan individu.
            if (loopPPA == loopMaksPPA) {
                //Bila tidak ada individu yang memenuhi, maka akan keluar
                //pesan berikut hasil running.
                System.out.println("Tidak dapat menemukan solusi yang "
                        + "layak untuk tMax sebesar " + tMax);
                System.exit(0);
            }
        }

        //Menghentikan pembangkitan apabila sudah mencapai batas jumlah populasi.
        while (populasi < jumlahPopulasi);

        //1.7. Memasukkan waktu tempuh dan nilai fitness untuk setiap
        //     individu dari populasi awal yang dibangkitkan.
        for (int i = 0; i < ArrayPopulasi.size(); i++) {
            ArrayPopulasi_WaktuTempuh.add(
                    hitungWaktuTempuh(ArrayPopulasi.get(i)));
            ArrayPopulasi_Fitness.add(
                    hitungFitness(ArrayPopulasi.get(i)));
        }

        //1.8. Mencari individu terbaik dari proses pembangkitan
        //     populasi awal.
        int individuSolusiAwal = 0;
        int optimalFitnessAwal = ArrayPopulasi_Fitness.get(0);
        for (int i = 1; i < ArrayPopulasi.size(); i++) {
            if (ArrayPopulasi_Fitness.get(i) >= optimalFitnessAwal) {
                individuSolusiAwal = i;
                optimalFitnessAwal = ArrayPopulasi_Fitness.get(i);
            }
        }
        ArraySeleksi.add(ArrayPopulasi.get(individuSolusiAwal));

        //1.9. Mencetak hasil pembangkitan populasi awal
        /*pw.println("+++> MULAI ALGORITMA GENETIKA HYPER-HEURISTIC <+++");
        pw.println(" ");
        pw.println("Hasil Pembangkitan Populasi Awal :");
        for (int i = 0; i < ArrayPopulasi.size(); i++) {
            pw.println(ArrayPopulasi.get(i) + " ("
                    + ArrayPopulasi_WaktuTempuh.get(i) + ", "
                    + ArrayPopulasi_Fitness.get(i) + ")");
        }*/



        for (int i = 0; i < ArraySeleksi.size(); i++) {
            ArraySeleksi_WaktuTempuh.add(
                    hitungWaktuTempuh(ArraySeleksi.get(i)));
            ArraySeleksi_Fitness.add(
                    hitungFitness(ArraySeleksi.get(i)));
        }

        //1.10 Mencetak hasil individu terbaik dari Populasi Awal
        /*pw.println(" ");
        pw.println("Individu Terbaik Saat Pembangkitan Populasi Awal");
        pw.println("Individu ini akan menjadi input awal untuk operator"
                + " low-level heuristic");
        pw.println("Rute: "
                + ArraySeleksi.get(0) + " | Waktu Tempuh: "
                + ArraySeleksi_WaktuTempuh.get(0) + " | Fitness: "
                + ArraySeleksi_Fitness.get(0));
        pw.println(" ");*/

        //memulai iterasi hyper-heuristic
        for (int i = 0; i < ArraySeleksi.size(); i++) {
            int iterasiHH = 1;
            int cross = 0;
            int add = 0;
            int omit = 0;
            int exchange = 0;
            do {

                int randomOperator = new Random().nextInt(4) + 1;

                //pw.println(" ");
                //pw.println("Hasil Hyper-Heuristic Generasi ke : "
                //+ ArraySeleksi.get(0));

                switch (randomOperator) {


                    //---MENJALANKAN OPERATOR HYPER-HEURISTIC---

                    case 1:
                        //3. MELAKUKAN KAWIN SILANG DENGAN INJECTION CROSS-OVER
                        //   membuat array untuk menyimpan keturunan
                        List<Integer> keturunan = new ArrayList<Integer>();

                        if (ArraySeleksi.get(0).size()
                                <= ArraySeleksi.get(0).size()) {

                            //Membuat insertion point untuk Induk 1.
                            int insertionPoint = new Random().nextInt(
                                    ArraySeleksi.get(0).size() - 2) + 1;
                            //Mengambil gen sejumlah insertion point
                            //pada Induk 1 dimulai dari indeks 0.
                            List<Integer> head = ArraySeleksi.get(0).subList(0,
                                    insertionPoint);
                            //Merupakan list yang nantinya akan digunakan jika
                            //proses perkawinan silang tidak berhasil.
                            List<Integer> cdg = ArraySeleksi.get(0).subList(
                                    insertionPoint,
                                    ArraySeleksi.get(0).size() - 1);
                            //Mengambil gen sejumlah insertion point pada
                            //Induk 2 dimulai dari indeks sebelum node akhir.
                            List<Integer> tail = ArraySeleksi.get(0).subList(
                                    ArraySeleksi.get(0).size() - insertionPoint
                                            - 1, ArraySeleksi.get(0).size() - 1);

                            //Fungsi untuk menghasilkan keturunan. Jika proses
                            //perkawinan silang gagal, maka dihasilkan keturunan
                            //yang sama dengan induk 1.
                            if (head.size() + tail.size() + 1
                                    >= ArraySeleksi.get(0).size()) {
                                //Memasukkan semua gen dari head ke keturunan.
                                keturunan.addAll(head);
                                int index = 0;
                                //Memasukkan gen dari tail ke dalam keturunan
                                //dengan menyesuaikan ukuran Induk 1.
                                while (keturunan.size()
                                        < ArraySeleksi.get(0).size() - 1) {
                                    if (index < tail.size()) {
                                        if (!keturunan.contains(
                                                tail.get(index))) {
                                            keturunan.add(tail.get(index));
                                        }
                                        index++;
                                    } else {
                                        //Jika proses memasukkan tail gagal ke
                                        //dalam keturunan, maka dimasukkan gen
                                        //sisa dari induk 1.
                                        keturunan.addAll(cdg);
                                    }
                                }
                                //Memasukkan gen akhir
                                keturunan.add(titikAkhir);
                            } else {
                                keturunan.addAll(ArraySeleksi.get(0));
                            }

                        }

                        else if (ArraySeleksi.get(0).size()
                                > ArraySeleksi.get(0).size()) {

                            int insertionPoint = new Random().nextInt(
                                    ArraySeleksi.get(0).size() - 1) + 1;
                            List<Integer> head
                                    = ArraySeleksi.get(0).subList(0,
                                    insertionPoint);
                            List<Integer> cdg = ArraySeleksi.get(0).subList(
                                    insertionPoint,
                                    ArraySeleksi.get(0).size() - 1);
                            List<Integer> tail = ArraySeleksi.get(0).subList(
                                    ArraySeleksi.get(0).size() - insertionPoint
                                            - 1, ArraySeleksi.get(0).size() - 1);

                            if (head.size() + tail.size() + 1
                                    >= ArraySeleksi.get(0).size()) {
                                keturunan.addAll(head);
                                int index = 0;
                                while (keturunan.size()
                                        < ArraySeleksi.get(0).size() - 1) {
                                    if (index < tail.size()) {
                                        if (!keturunan.contains(
                                                tail.get(index))) {
                                            keturunan.add(tail.get(index));
                                        }
                                        index++;
                                    } else {
                                        keturunan.addAll(cdg);
                                    }
                                }
                                keturunan.add(titikAkhir);
                            } else {
                                keturunan.addAll(ArraySeleksi.get(0));
                            }
                        }

                        //3.3. Memasukkan keturunan hasil crossover ke dalam array
                        //yang unik dan memenuhi batasan tMax
                        if (!ArraySeleksi.contains(keturunan) && hitungWaktuTempuh(
                                (ArrayList<Integer>) keturunan) <= tMax) {
                            ArraySeleksi.add((ArrayList<Integer>) keturunan);


                        } else {

                        } cross++;

                        break;


                    case 2:
                        //4.1. Menjalankan Operator Exchange.

                        //memilih 2 titik pada individu yang akan di mutasi dengan exchange
                        int tukar1 = new Random().nextInt(ArraySeleksi.get(0).size() - 2) + 1;
                        int tukar2 = new Random().nextInt(ArraySeleksi.get(0).size() - 2) + 1;

                        //pw.println("Low-Level Heuristic = Exchange");

                        if (ArraySeleksi.get(0).size() > 3) {

                            //Menyimpan sementara nilai dari gen yang di tukar
                            int sementara  = ArraySeleksi.get(0).get(tukar1);
                            int sementara2 = ArraySeleksi.get(0).get(tukar2);

                            Collections.swap(ArraySeleksi.get(0), tukar1, tukar2);

                            //Menyimpan waktu tempuh & fitness hasil proses exchange
                            int currentWaktuTempuhS
                                    = hitungWaktuTempuh(ArraySeleksi.get(0));
                            int currentFitnessS
                                    = hitungFitness(ArraySeleksi.get(0));
                            if (currentWaktuTempuhS
                                    <= ArraySeleksi_WaktuTempuh.get(0)) {
                                ArraySeleksi_WaktuTempuh.set(0, currentWaktuTempuhS);
                                ArraySeleksi_Fitness.set(0, currentFitnessS);

                            } else {
                                //Jika tidak lebih baik, maka posisi gen
                                //yang ditukar kembali seperti semula
                                Collections.swap(ArraySeleksi.get(0), tukar2, tukar1);
                            }
                        } exchange++;
                        break;

                    case 3:
                        //4.2. Menjalankan Operator Add.
                        //menentukan posisi penambahan gen pada individu yang akan
                        //dimutasi dengan operator Add
                        int InsertRandom = new Random().nextInt(ArraySeleksi.get(0).size() - 2) + 1;
                        //menginisialisasi nilai random yang akan ditambahkan
                        int randomInsertion
                                = new Random().nextInt(jumlahTitik) + 1;
                        if (ArraySeleksi.get(0).contains(randomInsertion)
                                || randomInsertion == titikAwal
                                || randomInsertion == titikAkhir) {
                            continue; //Memastikan gen yang dimasukkan bukan
                        }             //merupakan titik awal dan titik akhir
                        ArraySeleksi.get(0).add(InsertRandom,randomInsertion);

                        //Menyimpan waktu tempuh dan fitness dari hasil proses
                        //add.
                        int currentWaktuTempuh
                                = hitungWaktuTempuh(ArraySeleksi.get(0));
                        int currentFitness
                                = hitungFitness(ArraySeleksi.get(0));

                        //Jika proses add menghasilkan waktu tempuh dan fitness
                        //yang lebih baik atau sama dengan sebelumnya, maka
                        //hasil mutasi akan disimpan menggantikan yang lama.
                        if (currentWaktuTempuh <= tMax
                                && currentFitness >= ArraySeleksi_Fitness.get(0)) {
                            ArraySeleksi_WaktuTempuh.set(0, currentWaktuTempuh);
                            ArraySeleksi_Fitness.set(0, currentFitness);

                        } else {
                            //Jika tidak lebih baik, maka penambahan dibatalkan dan
                            //individu dikembalikan seperti semula
                            ArraySeleksi.get(0).remove(InsertRandom);
                        } add++;
                        break;

                    //4.3. Menjalankan Operator Delete Random.
                    //int loopDeleteRandom = 0;
                    //int loopMaksimalDR = 1000;
                    case 4:
                        //memilih posisi gen pada individu yang akan di
                        //mutasi dengan operator omit
                        int DeleteRandom = new Random().nextInt(ArraySeleksi.get(0).size() - 2) + 1;
                        if (!ArraySeleksi.get(0).contains(DeleteRandom)
                                || DeleteRandom == titikAwal
                                || DeleteRandom == titikAkhir) {
                            continue; //Memastikan gen yang dimasukkan bukan
                        }
                        //Proses bisa dilakukan jika ukuran keturunan lebih dari 3
                        if (ArraySeleksi.get(0).size() > 3) {
                            //Menyimpan sementara nilai dari gen/node yang dihapus
                            int sementara = ArraySeleksi.get(0).get(DeleteRandom);
                            ArraySeleksi.get(0).remove(DeleteRandom);
                            if (DeleteRandom == titikAwal || DeleteRandom == titikAkhir)
                            {
                                continue; //Memastikan gen yang dimasukkan bukan
                            }
                            //Menyimpan waktu tempuh & fitness hasil proses delete
                            int currentWaktuTempuhD
                                    = hitungWaktuTempuh(ArraySeleksi.get(0));
                            int currentFitnessD
                                    = hitungFitness(ArraySeleksi.get(0));
                            if (currentWaktuTempuhD
                                    < ArraySeleksi_WaktuTempuh.get(0)
                                    && currentFitnessD >= ArraySeleksi_Fitness.get(0)
                            ) {
                                ArraySeleksi_WaktuTempuh.set(0, currentWaktuTempuhD);
                                ArraySeleksi_Fitness.set(0, currentFitnessD);
                                //loopDeleteRandom = 0; //looping dikembalikan ke awal

                                //pw.println("Setelah proses low-level heuristic Omit");



                            } else {
                                ArraySeleksi.get(0).add(DeleteRandom, sementara);
                            } //Jika tidak lebih baik, maka dikembalikan seperti semula
                        } omit++;
                        break;
                }

                /*pw.println("Hyper-Heuristic Putaran ke : " + iterasiHH);
                pw.print("Individu terbaik putaran ini adalah ");
                pw.println(ArraySeleksi.get(0)
                        + "(" + ArraySeleksi_WaktuTempuh.get(0)
                        + ", " + ArraySeleksi_Fitness.get(0) + ")");
                pw.println(" ");*/
                iterasiHH++;

            } while (iterasiHH <= jumlahGenerasi);
            //loopDeleteRandom++;

            /*pw.println("");
            pw.println("Jumlah Penggunaan Operator Kawin Silang Injection = "
                    + cross);
            pw.println("Jumlah Penggunaan Operator Mutasi Add = "
                    + add);
            pw.println("Jumlah Penggunaan Operator Mutasi Exchange = "
                    + exchange);
            pw.println("Jumlah Penggunaan Operator Mutasi Omit = "
                    + omit);*/

        }
        //} while (loopDeleteRandom < loopMaksimalDR);

        //}
        // iterasiHH++;

        //} while (iterasiHH < jumlahGenerasi);
        long elapsedTime = System.nanoTime() - start;
        double seconds = (double)elapsedTime / 1000000000.0;
        /*pw.println(" ");
        pw.println("Total Waktu Running Program = " + seconds);
        pw.close();*/

    }

    //Fungsi untuk menghitung waktu tempuh setiap individu.
    public static int hitungWaktuTempuh(ArrayList<Integer> varList) {
        //Log.e("test","======================= var list ========================");
        //Log.e("test", varList.toString());
        //Log.e("test","======================= var list end ========================");
        int wt = 0; //Inisiasi nilai awal wt adalah 0
        for (int i = 0; i < varList.size() - 1; i++) {
            int var = varList.get(i);
            //if(var > 0){
                int[] arr = nilaiWaktuTempuh[var - 1];
                wt = wt + arr[varList.get(i + 1) - 1];
            //}
        } //Loop di atas utk menghitung waktu tempuh tiap titik yg berdekatan
        return wt;
    }

    //Fungsi untuk menghitung fitness setiap individu.
    public static int hitungFitness(ArrayList<Integer> varList) {
        int fitness = 0; //Inisiasi nilai awal fit adalah 0
        for (int i = 0; i < varList.size(); i++) {
            fitness = fitness + nilaiSkor[varList.get(i) - 1];
        } //Loop di atas utk menghitung skor tiap titik
        return fitness;
    }

    //Fungsi untuk menghitung total fitness pada populasi.
    public static int totalFitness() {
        int tFit = 0; //Inisiasi nilai awal sum adalah 0
        for (int i = 0; i < ArrayPopulasi_Fitness.size(); i++) {
            tFit = tFit + ArrayPopulasi_Fitness.get(i);
        } //Loop di atas utk menghitung fitness tiap individu pd populasi
        return tFit;
    }

    //Fungsi untuk untuk melakukan seleksi individu pada populasi.
    public static int rouletteWheelSelection() {
        double i = new Random().nextDouble();
        int totFitness = totalFitness();
        int j = 0;
        double sum = (double) ArrayPopulasi_Fitness.get(j) / totFitness;
        while (sum < i) {
            j++;
            sum = sum + (double) ArrayPopulasi_Fitness.get(j) / totFitness;
        }
        return j;
    }

    public static void reset() {
        titikAwal = 6; //Menetapkan titik awal keberangkatan bis kota
        titikAkhir = 34; //Menetapkan titik akhir perjalanan bis kota
        jumlahTitik = 42; //Jumlah titik (objek) yang digunakan
        tMax = 60; //Menetapkan nilai batasan waktu yang diperbolehkan (dalam satuan menit)
        jumlahPopulasi = 40; //Menetapkan jumlah populasi adalah 40
        jumlahGenerasi = 1000; //Menetapkan jumlah generasi/iterasi

        //Membuat array 2D dan 1D untuk waktu tempuh dan skor
        nilaiWaktuTempuh = new int[jumlahTitik][jumlahTitik];
        nilaiSkor = new int[jumlahTitik];

        //Membuat array untuk penyimpanan populasi, hasil seleksi,
        //hasil kawin silang, dan hasil mutasi yang dilakukan.
        ArrayPopulasi
                = new ArrayList<ArrayList<Integer>>();
        ArrayPopulasi_WaktuTempuh
                = new ArrayList<Integer>();
        ArrayPopulasi_Fitness
                = new ArrayList<Integer>();

        ArraySeleksi
                = new ArrayList<ArrayList<Integer>>();
        ArraySeleksi_WaktuTempuh
                = new ArrayList<Integer>();
        ArraySeleksi_Fitness
                = new ArrayList<Integer>();

        ArrayKawinSilang
                = new ArrayList<ArrayList<Integer>>();
        ArrayKawinSilang_WaktuTempuh
                = new ArrayList<Integer>();
        ArrayKawinSilang_Fitness
                = new ArrayList<Integer>();

        ArrayHyper
                = new ArrayList<ArrayList<Integer>>();
        ArrayHyper_WaktuTempuh
                = new ArrayList<Integer>();
        ArrayHyper_Fitness
                = new ArrayList<Integer>();

        ArrayPenyimpan
                = new ArrayList<ArrayList<Integer>>();
        ArrayPenyimpan_WaktuTempuh
                = new ArrayList<Integer>();
        ArrayPenyimpan_Fitness
                = new ArrayList<Integer>();

    }

    /*@Override
    protected void solve(ProblemDomain problem) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
	 * this method must be implemented, to provide a different name for each hyper-heuristic
	 * @return a string representing the name of the hyper-heuristic

	 public String toString() {
		return "Example Hyper Heuristic One";
	 }*/
}
