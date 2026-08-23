package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

/** Persistent pet-care layer for hygiene and physical waste cleanup. */
public final class PetCareSystem {
    private static final String PREF="pypet_pet_care";
    private static final String HYGIENE="hygiene";
    private static final String WASTE="waste";
    private static final String LAST="last_waste";
    private static final Random RNG=new Random();
    private PetCareSystem(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREF,Context.MODE_PRIVATE);}
    public static int hygiene(Context c){return Math.max(0,Math.min(100,p(c).getInt(HYGIENE,100)));}
    public static boolean hasWaste(Context c){return p(c).getBoolean(WASTE,false);}
    public static void ensureRoutine(Context c){
        long now=System.currentTimeMillis(); long last=p(c).getLong(LAST,0);
        if(!hasWaste(c) && (last==0 || now-last>60000L) && RNG.nextInt(100)<45){
            p(c).edit().putBoolean(WASTE,true).putLong(LAST,now).apply();
        }
    }
    public static void tick(Context c){
        ensureRoutine(c);
        int h=hygiene(c);
        if(hasWaste(c)) h=Math.max(0,h-1);
        p(c).edit().putInt(HYGIENE,h).apply();
    }
    public static void cleanWaste(Context c){
        p(c).edit().putBoolean(WASTE,false).putInt(HYGIENE,Math.min(100,hygiene(c)+15)).apply();
    }
    public static void bathe(Context c){
        p(c).edit().putInt(HYGIENE,Math.min(100,hygiene(c)+25)).apply();
    }
}