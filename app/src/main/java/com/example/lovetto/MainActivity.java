package com.example.lovetto;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.app.PendingIntent.getActivity;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private Intent intent;
    private Context context;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Nascondo la actionbar
        getSupportActionBar().hide();

        context=this;
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

    }

    public void lanciaCreaIncubata(View view) {
        intent = new Intent(this, activity_crea_incubata.class);
        startActivity(intent);
    }

    public void mostraTutorial(View view) {
        intent = new Intent(this, activity_tutorial.class);
        startActivity(intent);
    }



    public void OnRequestPermissionsResultCallback () {}

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod = new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected (@NonNull MenuItem item){
                switch (item.getItemId()) {
                    case R.id.home:
                        //Toast.makeText(context, "Sei nella Home!", Toast.LENGTH_LONG).show();
                        break;
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

                }
                return false;
            }
    };
}
