package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Tampil extends AppCompatActivity {

    TextView Tberangkat, Tberhenti, Twaktu;
    String tampil_berangkat, tampil_berhenti, tampil_waktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil);

        Tberangkat = (TextView)findViewById(R.id.tampilBerangkat);
        Tberhenti = (TextView)findViewById(R.id.tampilBerhenti);
        Twaktu = (TextView)findViewById(R.id.tampilWaktu);
        Bundle b = getIntent().getExtras();


        tampil_berangkat = b.getString("parse_tAwal");
        tampil_berhenti = b.getString("parse_tAkhir");
        tampil_waktu = b.getString("parse_waktu");

        Tberangkat.setText(""+tampil_berangkat);
        Tberhenti.setText(""+tampil_berhenti);
        Twaktu.setText(""+tampil_waktu);




    }
}