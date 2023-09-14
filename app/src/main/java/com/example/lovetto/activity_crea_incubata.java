package com.example.lovetto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lovetto.utility.datiTemporanei;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_crea_incubata extends AppCompatActivity {
    private Intent intent;
    private Context context;
    private BottomNavigationView bottomNavigationView;

    private RadioButton rQuaglia, rGallina, rPappagallo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_incubata);

        // Nascondo la actionbar
        getSupportActionBar().hide();

        context = this;
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        rGallina = findViewById(R.id.radio_gallina);
        rQuaglia = findViewById(R.id.radio_quaglia);
        rPappagallo = findViewById(R.id.radio_pappagallo);
    }


    public void lanciaDettagliIncubatore(View view) {
        intent = new Intent(this, activity_dettagli_incubatore.class);
        startActivity(intent);
    }

    public void inserisciIncubata(View view) {
        datiTemporanei.setIncubataCreata(true);
        intent = new Intent(this, activity_list.class);
        startActivity(intent);
        Toast.makeText(this, "Incubata creata!",
                Toast.LENGTH_LONG).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    boolean creata = datiTemporanei.getIncubataCreata();
                    Intent intent1;
                    if (creata) {
                        intent1 = new Intent(context, activity_oggi.class);
                    } else{
                        intent1 = new Intent(context, MainActivity.class);
                    }
                    startActivity(intent1);
                case R.id.listaIncubata:
                    Intent intent2 = new Intent(context, activity_list.class);
                    startActivity(intent2);
                    break;
                case R.id.vendite:
                    Intent intent3 = new Intent(context, activity_vendita.class);
                    startActivity(intent3);
                    break;
                case R.id.tutorial:
                    Intent intent4 = new Intent(context, activity_tutorial.class);
                    startActivity(intent4);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return false;
        }
    };

    public void sceltaUovo(View view) {
        if (rQuaglia.isChecked()) {
            rQuaglia.setTextColor(Color.WHITE);


            rPappagallo.setTextColor(Color.BLACK);
            rGallina.setTextColor(Color.BLACK);
            rPappagallo.setChecked(false);
            rGallina.setChecked(false);
        }

        if (rPappagallo.isChecked()) {
            rPappagallo.setTextColor(Color.WHITE);

            rQuaglia.setTextColor(Color.BLACK);
            rGallina.setTextColor(Color.BLACK);
            rQuaglia.setChecked(false);
            rGallina.setChecked(false);
        }
        if (rGallina.isChecked()) {
            rGallina.setTextColor(Color.WHITE);

            rPappagallo.setTextColor(Color.BLACK);
            rQuaglia.setTextColor(Color.BLACK);
            rPappagallo.setChecked(false);
            rQuaglia.setChecked(false);
        }
    }
}
