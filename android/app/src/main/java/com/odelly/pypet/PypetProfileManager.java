package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

/** Persistent player identity used by the World, town and Academy progression. */
public final class PypetProfileManager {
    private static final String PREF="pypet_profile";
    private PypetProfileManager() {}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREF,Context.MODE_PRIVATE);}
    public static String playerName(Context c){return p(c).getString("player_name","");}
    public static String townName(Context c){return p(c).getString("town_name","");}
    public static String avatar(Context c){return p(c).getString("avatar","🐾");}
    public static boolean complete(Context c){return !playerName(c).trim().isEmpty()&&!townName(c).trim().isEmpty();}
    public static void save(Context c,String player,String town,String avatar){p(c).edit().putString("player_name",player.trim()).putString("town_name",town.trim()).putString("avatar",avatar==null?"🐾":avatar).apply();}
    public static void ensureDefaults(Context c){if(!complete(c))save(c,"Player","My Pypet Town","🐾");}
}
