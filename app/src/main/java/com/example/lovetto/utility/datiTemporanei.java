package com.example.lovetto.utility;

import android.util.Log;

public class datiTemporanei {
        private static boolean incubataCreata;
        public static boolean getIncubataCreata() {return incubataCreata;}
        public static void setIncubataCreata(boolean creata) {
                datiTemporanei.incubataCreata = creata;
                Log.d("creata","creata");
        }
}
