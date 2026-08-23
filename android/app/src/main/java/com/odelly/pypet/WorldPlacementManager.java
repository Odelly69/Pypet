package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persists player-placed World objects and trophies for the town showcase. */
public final class WorldPlacementManager {
    public static final class Placement {
        public final String id;
        public final float x, y, scale, rotation;
        public Placement(String id, float x, float y, float scale, float rotation) {
            this.id = id; this.x = x; this.y = y; this.scale = scale; this.rotation = rotation;
        }
    }

    private static final String PREFS = "pypet_world_layout";
    private static final String COUNT = "placement_count";
    private static final String PREFIX = "placement_";
    private static final String TROPHY_PREFIX = "trophy_";

    private WorldPlacementManager() {}

    public static void place(Context c, String id, float x, float y, float scale, float rotation) {
        SharedPreferences.Editor e = prefs(c).edit();
        int index = prefs(c).getInt(COUNT, 0);
        e.putString(PREFIX + index, encode(id, x, y, scale, rotation));
        e.putInt(COUNT, index + 1).apply();
    }

    public static void update(Context c, int index, String id, float x, float y, float scale, float rotation) {
        prefs(c).edit().putString(PREFIX + index, encode(id, x, y, scale, rotation)).apply();
    }

    public static List<Placement> all(Context c) {
        int count = prefs(c).getInt(COUNT, 0);
        List<Placement> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Placement p = decode(prefs(c).getString(PREFIX + i, null));
            if (p != null) result.add(p);
        }
        return Collections.unmodifiableList(result);
    }

    public static boolean hasTrophyDisplay(Context c, String trophyId) {
        return prefs(c).getBoolean(TROPHY_PREFIX + trophyId, false);
    }

    public static void setTrophyDisplay(Context c, String trophyId, boolean displayed) {
        prefs(c).edit().putBoolean(TROPHY_PREFIX + trophyId, displayed).apply();
    }

    private static String encode(String id, float x, float y, float scale, float rotation) {
        return id + "|" + x + "|" + y + "|" + scale + "|" + rotation;
    }

    private static Placement decode(String value) {
        if (value == null) return null;
        try {
            String[] p = value.split("\\|", -1);
            if (p.length != 5) return null;
            return new Placement(p[0], Float.parseFloat(p[1]), Float.parseFloat(p[2]), Float.parseFloat(p[3]), Float.parseFloat(p[4]));
        } catch (Exception ignored) { return null; }
    }

    private static SharedPreferences prefs(Context c) { return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE); }
}
