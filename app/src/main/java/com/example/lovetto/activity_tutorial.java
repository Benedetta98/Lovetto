package com.example.lovetto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lovetto.utility.datiTemporanei;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_tutorial extends AppCompatActivity {

    private Intent intent;
    private Context context;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        // Nascondo la actionbar
        getSupportActionBar().hide();


        context = this;
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

    public void lanciaDettaglioTutorial(View view) {
//        intent = new Intent(this, activity_tutorial.class);
//        startActivity(intent);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
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
                case R.id.vendite:
                    Intent intent4 = new Intent(context, activity_vendita.class);
                    startActivity(intent4);
                case R.id.tutorial:
                    //Toast.makeText(context, "Sei nella sezione tutorial!", Toast.LENGTH_LONG).show();
                    break;
            }
            return false;
        }
    };
}
