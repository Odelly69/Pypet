package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

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
    public static boolean expand(Context c){
        int current=level(c);
        if(current>=MAX_LEVEL)return false;
        int coins=RewardInventory.coins(c);
        int cost=nextCost(c);
        if(coins<cost)return false;
        RewardInventory.spendCoins(c,cost);
        c.getSharedPreferences(PREFS,0).edit().putInt(LEVEL,current+1).apply();
        return true;
    }
    /** Maximum playable/buildable coordinate. The original town is level 0. */
    public static float halfSize(Context c){return 1200f + level(c)*300f;}

    public static String status(Context c){
        int l=level(c);
        if(l>=MAX_LEVEL)return "MAX TOWN SIZE";
        return "District "+(l+1)+" • "+nextCost(c)+" coins to expand";
    }
}
