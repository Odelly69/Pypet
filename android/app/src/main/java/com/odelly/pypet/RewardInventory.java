package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/** Local, non-transferable inventory for Pypet world items. */
public final class RewardInventory {
    private static final String PREFS = "pypet_rewards";
    private static final String OWNED = "owned_items";

    private RewardInventory() {}

    public static boolean owns(Context context, String itemId) {
        return prefs(context).getStringSet(OWNED, new HashSet<>()).contains(itemId);
    }

    public static boolean grant(Context context, String itemId) {
        if (RewardCatalog.byId(itemId) == null || owns(context, itemId)) return false;
        Set<String> owned = new HashSet<>(prefs(context).getStringSet(OWNED, new HashSet<>()));
        owned.add(itemId);
        prefs(context).edit().putStringSet(OWNED, owned).apply();
        return true;
    }

    public static int count(Context context) {
        return prefs(context).getStringSet(OWNED, new HashSet<>()).size();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
