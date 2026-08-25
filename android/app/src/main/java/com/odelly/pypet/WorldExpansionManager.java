package com.odelly.pypet;

import android.content.Context;

/** Persistent town-development progression. Each expansion unlocks more buildable land. */
public final class WorldExpansionManager {
    private static final String PREFS="pypet_world_expansion";
    private static final String LEVEL="level";
    private static final int MAX_LEVEL=8;
    private WorldExpansionManager() {}

    public static int level(Context c){
        return Math.max(0,Math.min(MAX_LEVEL,c.getSharedPreferences(PREFS,0).getInt(LEVEL,0)));
    }
    public static int maxLevel(){return MAX_LEVEL;}
    public static boolean canExpand(Context c){return level(c)<MAX_LEVEL;}
    public static int nextCost(Context c){return 25 + level(c)*25;}
    /** Expansion is a progression unlock for the prototype; future economy balancing can charge the displayed target cost. */
    public static boolean expand(Context c){
        int current=level(c);
        if(current>=MAX_LEVEL)return false;
        c.getSharedPreferences(PREFS,0).edit().putInt(LEVEL,current+1).apply();
        return true;
    }
    public static float halfSize(Context c){return 1200f + level(c)*300f;}
    public static String status(Context c){
        int l=level(c);
        if(l>=MAX_LEVEL)return "MAX TOWN SIZE • 8 DISTRICTS";
        return "DISTRICT "+(l+1)+" READY • EXPAND TARGET: "+nextCost(c)+" COINS";
    }
}
