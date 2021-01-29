package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String Iberangkat, Iberhenti, Iwaktu;
    //EditText Inputwaktu;
    private TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Spinner titikAwalSpinner = findViewById(R.id.titikAwalSpinner);
    titikAwalSpinner.setOnItemSelectedListener(this);

    final Spinner titikAkhirSpinner = findViewById(R.id.titikAkhirSpinner);
    ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(
            this,
            R.array.Spinner_items,
            android.R.layout.simple_spinner_item
    );
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    titikAkhirSpinner.setAdapter(adapter);
    titikAkhirSpinner.setOnItemSelectedListener(this);

    EditText Inputwaktu = (EditText)findViewById(R.id.etWaktu);
    result = (TextView)findViewById(R.id.HasilPencarian);

    Button submit = (Button)findViewById(R.id.btnOK);
    submit.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

            Iberangkat = String.valueOf(titikAwalSpinner.getSelectedItem());
            Iberhenti = String.valueOf(titikAkhirSpinner.getSelectedItem());
            Iwaktu = Inputwaktu.getText().toString();

            AGHH.titikAwal = titikAwalSpinner.getSelectedItemPosition();
            AGHH.titikAkhir = titikAkhirSpinner.getSelectedItemPosition();
                   // getSelectedItemPosition() + 1;
            AGHH.tMax = Integer.parseInt(Inputwaktu.getText().toString());
            String []args = new String[0];
            Scanner s = new Scanner(System.in);
            try {
                AGHH.main(args);
            } catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }
            ArrayList<Integer> seleksi = AGHH.ArraySeleksi.get(0);
            System.out.println(seleksi.size());
            result.setText("");
            int j = 1;
            for (int i = 0; i < seleksi.size(); i++) {

                String namaTempat = titikAwalSpinner.getItemAtPosition(seleksi.get(i)-1).toString();
                        //getItemAt(seleksi.get(i)-1).toString();

                String text = String.valueOf(result.getText());
                if (i==0) {
                    result.setText(text + "●\n" +namaTempat + " (Titik Keberangkatan)");
                }
                else if (i==seleksi.size() - 1){
                    result.setText(text + "\n|\n" + "●\n" +namaTempat + " (Titik Pemberhentian)");
                }
                else{
                    result.setText(text + "\n|\n" + "●\n" + namaTempat);
                } j++;
            }

        /*StyledDocument doc = HasilPencarian.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);*/
            AGHH.reset();

            Intent i = null;
            i = new Intent(MainActivity.this, Tampil.class);
            Bundle b = new Bundle();
            b.putString("parse_tAwal",Iberangkat);
            b.putString("parse_tAkhir",Iberhenti);
            b.putString("parse_waktu",Iwaktu);

            i.putExtras(b);
            startActivity(i);
        }
    });
}
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}