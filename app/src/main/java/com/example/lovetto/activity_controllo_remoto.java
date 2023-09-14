package com.example.lovetto;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lovetto.utility.AnimazioneRidimensionamento;
import com.example.lovetto.utility.datiTemporanei;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_controllo_remoto extends AppCompatActivity implements View.OnTouchListener {

    private RelativeLayout sliderTemperatura;
    private RelativeLayout sliderUmidita;
    private TextView valoreTemperaturaTv;
    private TextView valoreUmiditaTv;
    private View barraUmidita;
    private View barraTemperatura;
    private Animation animazioneScala;
    private Animation animazioneIngrandisci;
    private Animation animazioneRimpicciolisci;
    private ObjectAnimator animBiancoNeroTemp;
    private ObjectAnimator animNeroBiancoTemp;
    private ObjectAnimator animBiancoNeroUmi;
    private ObjectAnimator animNeroBiancoUmi;
    private int altezzaSlider;
    private int sliderTopMargin;
    private int yInizioPan = 0;
    private int altezzaBarraInizioPan = 0;
    private float offsetIncrementoTemperatura;
    private float offsetIncrementoUmidita;
    private float maxTemp = 50;
    private float minTemp = 30;
    private float incrementoTemperatura = 0.5f;
    private long durataAnimazioneIncremento = 150;
    private Intent intent;

    private Context context;
    private BottomNavigationView bottomNavigationView;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controllo_remoto);

        // Nascondo la actionbar
        getSupportActionBar().hide();

        // Ottengo le views
        sliderTemperatura = findViewById(R.id.sliderTemperatura);
        sliderUmidita = findViewById(R.id.sliderUmidita);
        valoreTemperaturaTv = findViewById(R.id.valoreTemperatura);
        valoreUmiditaTv = findViewById(R.id.valoreUmidita);
        barraUmidita = findViewById(R.id.barraUmi);
        barraTemperatura = findViewById(R.id.barraTemp);

        caricaAnimazioni();

        // Imposto che i bounds degli slider combacino con quelli della forma arrotondata assegnata come sfondo,
        // in modo che la barra di riempimento faccia da maschera alla barra di sfondo
        sliderTemperatura.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        sliderTemperatura.setClipToOutline(true);
        sliderUmidita.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        sliderUmidita.setClipToOutline(true);

        // Ottengo l'altezza degli slider
        altezzaSlider = sliderTemperatura.getLayoutParams().height;
        ViewGroup.MarginLayoutParams sliderParametriMargine = (ViewGroup.MarginLayoutParams) sliderTemperatura.getLayoutParams();
        sliderTopMargin = sliderParametriMargine.topMargin;

        // Calcolo quanto la barra della temperatura deve crescere dopo un incremento/decremento
        offsetIncrementoTemperatura = (altezzaSlider / ((maxTemp - minTemp) * 2));
        System.out.println("altezza slider = " + altezzaSlider + "\noffsetTemp ="+ offsetIncrementoTemperatura + "\nmargine slider =" + sliderTopMargin);

        // Calcolo quanto la barra dell'umidita deve crescere dopo un incremento/decremento
        offsetIncrementoUmidita = (altezzaSlider / 100.0f);
        System.out.println("offset incremento umidita " + offsetIncrementoUmidita);

        // Imposto la corretta altezza di default degli slider
        String umiString = rimuoviUltimoCarattere(valoreUmiditaTv.getText().toString());
        int valoreUmi = Integer.parseInt(umiString);
        ViewGroup.LayoutParams parametriBarra = barraUmidita.getLayoutParams();
        parametriBarra.height = (int) (valoreUmi * offsetIncrementoUmidita);
        barraUmidita.setLayoutParams(parametriBarra);

        String tempString = rimuoviUltimoCarattere(valoreTemperaturaTv.getText().toString());
        float valoreTemp = Float.parseFloat(tempString);
        ViewGroup.LayoutParams parametriBarraTemp = barraTemperatura.getLayoutParams();
        parametriBarraTemp.height = (int) (((valoreTemp - minTemp) / incrementoTemperatura) * offsetIncrementoTemperatura);;
        barraTemperatura.setLayoutParams(parametriBarraTemp);

        // Assegno il listener per i due slider
        sliderTemperatura.setOnTouchListener(this);
        sliderUmidita.setOnTouchListener(this);


        context=this;
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int Y = (int) event.getRawY();
        ViewGroup.LayoutParams parametriBarra;
        switch (view.getId()) {
            case R.id.sliderTemperatura:
                parametriBarra = barraTemperatura.getLayoutParams();
                break;
            case R.id.sliderUmidita:
                parametriBarra = barraUmidita.getLayoutParams();
                break;
            default:
                throw new IllegalStateException("Pan su view non valida: " + view.getId());
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                view.startAnimation(animazioneScala);
                yInizioPan = Y;
                altezzaBarraInizioPan = parametriBarra.height;
                switch (view.getId()) {
                    case R.id.sliderTemperatura:
                        valoreTemperaturaTv.startAnimation(animazioneIngrandisci);
                        break;
                    case R.id.sliderUmidita:
                        valoreUmiditaTv.startAnimation(animazioneIngrandisci);
                        break;
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (view.getId()) {
                    case R.id.sliderTemperatura:
                        valoreTemperaturaTv.startAnimation(animazioneRimpicciolisci);
                        break;
                    case R.id.sliderUmidita:
                        valoreUmiditaTv.startAnimation(animazioneRimpicciolisci);
                        break;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                int nuovaAltezza = altezzaBarraInizioPan - (Y - yInizioPan);
                if (nuovaAltezza > altezzaSlider) {
                    nuovaAltezza = altezzaSlider;
                } else if (nuovaAltezza < 0) {
                    nuovaAltezza = 0;
                }
                parametriBarra.height = nuovaAltezza;
                switch (view.getId()) {
                    case R.id.sliderTemperatura:
                        barraTemperatura.setLayoutParams(parametriBarra);
                        int nuovaTemp = (int) ((nuovaAltezza / offsetIncrementoTemperatura) * incrementoTemperatura + minTemp);
                        settaTemperatura(nuovaTemp);
                        break;
                    case R.id.sliderUmidita:
                        barraUmidita.setLayoutParams(parametriBarra);
                        int nuovaUmidita = (int) ((nuovaAltezza / offsetIncrementoUmidita));
                        settaUmidita(nuovaUmidita);
                        break;
                }
                break;
        }
        view.invalidate();
        return true;
    }

    public void tornaIndietro(View view) {
        view.startAnimation(animazioneScala);
        onBackPressed();
    }

    public void settaTemperatura(float temp) {
        valoreTemperaturaTv.setText(temp + "°");
        int coloreAttuale = valoreTemperaturaTv.getCurrentTextColor();
        if (temp < 34.5 && (coloreAttuale != Color.BLACK) && !animBiancoNeroTemp.isRunning()) {
            animBiancoNeroTemp.start();
        } else if (temp >= 35 && coloreAttuale != Color.WHITE && !animNeroBiancoTemp.isRunning()) {
            animNeroBiancoTemp.start();
        }
    }

    public void settaUmidita(int umidita) {
        valoreUmiditaTv.setText(umidita + "%");
        int coloreAttuale = valoreUmiditaTv.getCurrentTextColor();
        if (umidita < 23 && (coloreAttuale != Color.BLACK) && !animBiancoNeroUmi.isRunning()) {
            animBiancoNeroUmi.start();
        } else if (umidita >= 24 && coloreAttuale != Color.WHITE && !animNeroBiancoUmi.isRunning()) {
            animNeroBiancoUmi.start();
        }
    }

    public void incrementaTemperatura(View view) {
        view.startAnimation(animazioneScala);
        String tempString = rimuoviUltimoCarattere(valoreTemperaturaTv.getText().toString());
        float valoreTemp = Float.parseFloat(tempString);
        if (valoreTemp < maxTemp) {
            valoreTemp += incrementoTemperatura;
        }
        settaTemperatura(valoreTemp);
        ViewGroup.LayoutParams parametriBarra = barraTemperatura.getLayoutParams();
        int altezzaBarra = parametriBarra.height;
        int nuovaAltezza = altezzaBarra;
        if (altezzaBarra < altezzaSlider - offsetIncrementoTemperatura) {
            nuovaAltezza = (int) (((valoreTemp - minTemp) / incrementoTemperatura) * offsetIncrementoTemperatura);
        } else {
            nuovaAltezza = altezzaSlider;
        }
        // Mancano un paio di pixel extra quando la barra viene incrementata al massimo, in questo modo, all'ultimo incremento viene riempita
        if (valoreTemp == maxTemp) {
            nuovaAltezza = altezzaSlider;
        }
        AnimazioneRidimensionamento animazioneIncremento = new AnimazioneRidimensionamento(
                barraTemperatura,
                nuovaAltezza,
                parametriBarra.height
        );
        animazioneIncremento.setDuration(durataAnimazioneIncremento);
        barraTemperatura.startAnimation(animazioneIncremento);
    }

    public void incrementaUmidita(View view) {
        view.startAnimation(animazioneScala);
        String umiString = rimuoviUltimoCarattere(valoreUmiditaTv.getText().toString());
        int valoreUmi = Integer.parseInt(umiString);
        if (valoreUmi < 100) {
            valoreUmi += 1;
        }
        settaUmidita(valoreUmi);
        ViewGroup.LayoutParams parametriBarra = barraUmidita.getLayoutParams();
        int altezzaBarra = parametriBarra.height;
        int nuovaAltezza = altezzaBarra;
        if (altezzaBarra < altezzaSlider - offsetIncrementoUmidita) {
            nuovaAltezza = (int) (valoreUmi * offsetIncrementoUmidita);
        } else {
            nuovaAltezza = altezzaSlider;
        }
        // Mancano un paio di pixel extra quando la barra viene incrementata al massimo, in questo modo, all'ultimo incremento viene riempita
        if (valoreUmi == 100) {
            nuovaAltezza = altezzaSlider;
        }
        AnimazioneRidimensionamento animazioneIncremento = new AnimazioneRidimensionamento(
                barraUmidita,
                nuovaAltezza,
                parametriBarra.height
        );
        animazioneIncremento.setDuration(durataAnimazioneIncremento);
        barraUmidita.startAnimation(animazioneIncremento);
    }

    public void goBack(View v) {
        finish();
    }

    public void decrementaTemperatura(View view) {
        view.startAnimation(animazioneScala);
        String tempString = rimuoviUltimoCarattere(valoreTemperaturaTv.getText().toString());
        float valoreTemp = Float.parseFloat(tempString);
        if (valoreTemp > minTemp) {
            valoreTemp -= incrementoTemperatura;
        }
        settaTemperatura(valoreTemp);
        ViewGroup.LayoutParams parametriBarra = barraTemperatura.getLayoutParams();
        int altezzaBarra = parametriBarra.height;
        int nuovaAltezza = altezzaBarra;
        if (altezzaBarra > offsetIncrementoTemperatura) {
            nuovaAltezza = (int) (((valoreTemp - minTemp) / incrementoTemperatura) * offsetIncrementoTemperatura);
        } else {
            nuovaAltezza =  0;
        }
        // Rimangono un paio di pixel extra quando la barra viene decrementata fino alla fine, in questo modo, all'ultimo decremento viene nascosta
        if (valoreTemp == minTemp) {
            nuovaAltezza = 0;
        }
        AnimazioneRidimensionamento animazioneIncremento = new AnimazioneRidimensionamento(
                barraTemperatura,
                nuovaAltezza,
                parametriBarra.height
        );
        animazioneIncremento.setDuration(durataAnimazioneIncremento);
        barraTemperatura.startAnimation(animazioneIncremento);
    }

    public void decrementaUmidita(View view) {
        view.startAnimation(animazioneScala);
        String umiString = rimuoviUltimoCarattere(valoreUmiditaTv.getText().toString());
        int valoreUmi = Integer.parseInt(umiString);
        if (valoreUmi > 0) {
            valoreUmi -= 1;
        }
        settaUmidita(valoreUmi);
        ViewGroup.LayoutParams parametriBarra = barraUmidita.getLayoutParams();
        int altezzaBarra = parametriBarra.height;
        int nuovaAltezza = altezzaBarra;
        if (altezzaBarra > offsetIncrementoUmidita) {
            nuovaAltezza = (int) (valoreUmi * offsetIncrementoUmidita);
        } else {
            nuovaAltezza = 0;
        }
        // Rimangono un paio di pixel extra quando la barra viene decrementata fino alla fine, in questo modo, all'ultimo decremento viene nascosta
        if (valoreUmi == 0) {
            nuovaAltezza = 0;
        }
        AnimazioneRidimensionamento animazioneIncremento = new AnimazioneRidimensionamento(
                barraUmidita,
                nuovaAltezza,
                parametriBarra.height
        );
        animazioneIncremento.setDuration(durataAnimazioneIncremento);
        barraUmidita.startAnimation(animazioneIncremento);
    }

    private static String rimuoviUltimoCarattere(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void caricaAnimazioni() {
        // Carico le animazioni
        animazioneScala = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animazione_scala_elemento_premuto);
        animazioneScala.setInterpolator(getApplicationContext(), android.R.interpolator.decelerate_cubic);
        animazioneIngrandisci = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.ingrandisci);
        animazioneIngrandisci.setInterpolator(getApplicationContext(), android.R.interpolator.accelerate_decelerate);
        animazioneRimpicciolisci = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rimpicciolisci);
        animazioneRimpicciolisci.setInterpolator(getApplicationContext(), android.R.interpolator.accelerate_decelerate);

        // Animazioni colore
        animBiancoNeroTemp = ObjectAnimator.ofInt(valoreTemperaturaTv, "textColor",
                Color.WHITE, Color.BLACK);
        animBiancoNeroTemp.setEvaluator(new ArgbEvaluator());
        animBiancoNeroTemp.setDuration(100);
        animNeroBiancoTemp = ObjectAnimator.ofInt(valoreTemperaturaTv, "textColor",
                Color.BLACK, Color.WHITE);
        animNeroBiancoTemp.setEvaluator(new ArgbEvaluator());
        animNeroBiancoTemp.setDuration(100);
        animBiancoNeroUmi = ObjectAnimator.ofInt(valoreUmiditaTv, "textColor",
                Color.WHITE, Color.BLACK);
        animBiancoNeroUmi.setEvaluator(new ArgbEvaluator());
        animBiancoNeroUmi.setDuration(100);
        animNeroBiancoUmi = ObjectAnimator.ofInt(valoreUmiditaTv, "textColor",
                Color.BLACK, Color.WHITE);
        animNeroBiancoUmi.setEvaluator(new ArgbEvaluator());
        animNeroBiancoUmi.setDuration(100);
    }

    public void tornaDettagliIncubata(View view){
        intent= new Intent(this, activity_dettagli_incubata.class);
        startActivity(intent);
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
