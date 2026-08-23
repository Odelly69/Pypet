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

    // Same world coordinate system used by LivingWorldView.
    private static final float WORLD_HALF_W = 1600f;
    private static final float WORLD_HALF_H = 2100f;
    private static final float ROAD_HALF = 145f; // road + sidewalk exclusion
    private static final float EDGE = 90f;

    private WorldPlacementManager() {}

    /** Place an object in a buildable lot. If a drag/drop position lands on a road,
     * automatically move it to the nearest safe roadside position instead. */
    public static void place(Context c, String id, float x, float y, float scale, float rotation) {
        float[] safe = safePosition(x, y);
        SharedPreferences.Editor e = prefs(c).edit();
        int index = prefs(c).getInt(COUNT, 0);
        e.putString(PREFIX + index, encode(id, safe[0], safe[1], saneScale(scale), rotation));
        e.putInt(COUNT, index + 1).apply();
    }

    public static void update(Context c, int index, String id, float x, float y, float scale, float rotation) {
        float[] safe = safePosition(x, y);
        prefs(c).edit().putString(PREFIX + index, encode(id, safe[0], safe[1], saneScale(scale), rotation)).apply();
    }

    public static List<Placement> all(Context c) {
        int count = prefs(c).getInt(COUNT, 0);
        List<Placement> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Placement p = decode(prefs(c).getString(PREFIX + i, null));
            if (p != null) {
                float[] safe = safePosition(p.x, p.y);
                result.add(new Placement(p.id, safe[0], safe[1], saneScale(p.scale), p.rotation));
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static boolean hasTrophyDisplay(Context c, String trophyId) {
        return prefs(c).getBoolean(TROPHY_PREFIX + trophyId, false);
    }

    public static void setTrophyDisplay(Context c, String trophyId, boolean displayed) {
        prefs(c).edit().putBoolean(TROPHY_PREFIX + trophyId, displayed).apply();
    }

    /** Keep decorative objects on land and off both roads. */
    private static float[] safePosition(float x, float y) {
        x = Math.max(-WORLD_HALF_W + EDGE, Math.min(WORLD_HALF_W - EDGE, x));
        y = Math.max(-WORLD_HALF_H + EDGE, Math.min(WORLD_HALF_H - EDGE, y));
        if (Math.abs(x) < ROAD_HALF && Math.abs(y) < ROAD_HALF) {
            // At the central intersection choose the closest of four roadside lots.
            if (Math.abs(x) >= Math.abs(y)) x = x >= 0 ? ROAD_HALF + EDGE : -ROAD_HALF - EDGE;
            else y = y >= 0 ? ROAD_HALF + EDGE : -ROAD_HALF - EDGE;
        } else if (Math.abs(x) < ROAD_HALF) {
            x = x >= 0 ? ROAD_HALF + EDGE : -ROAD_HALF - EDGE;
        } else if (Math.abs(y) < ROAD_HALF) {
            y = y >= 0 ? ROAD_HALF + EDGE : -ROAD_HALF - EDGE;
        }
        return new float[]{x, y};
    }

    private static float saneScale(float scale) { return Math.max(.35f, Math.min(2.5f, scale)); }

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
