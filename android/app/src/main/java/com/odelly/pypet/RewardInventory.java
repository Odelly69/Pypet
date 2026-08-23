package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/** Local, non-transferable Pypet Coin wallet and exclusive-item inventory. */
public final class RewardInventory {
    private static final String PREFS = "pypet_rewards";
    private static final String OWNED = "owned_items";
    private static final String COINS = "pypet_coins";

    private RewardInventory() {}

    public static int coins(Context context) {
        return Math.max(0, prefs(context).getInt(COINS, 0));
    }

    /** Add currency only after a qualifying rewarded-ad callback. */
    public static void addCoins(Context context, int amount) {
        if (amount <= 0) return;
        int current = coins(context);
        long total = (long) current + amount;
        prefs(context).edit().putInt(COINS, (int) Math.min(Integer.MAX_VALUE, total)).apply();
    }

    public static boolean owns(Context context, String itemId) {
        return prefs(context).getStringSet(OWNED, new HashSet<>()).contains(itemId);
    }

    /** Purchase an exclusive item using Pypet Coins. */
    public static boolean purchase(Context context, String itemId) {
        RewardCatalog.Item item = RewardCatalog.byId(itemId);
        if (item == null || owns(context, itemId) || coins(context) < item.priceCoins) return false;

        Set<String> owned = new HashSet<>(prefs(context).getStringSet(OWNED, new HashSet<>()));
        owned.add(itemId);
        prefs(context).edit()
                .putStringSet(OWNED, owned)
                .putInt(COINS, coins(context) - item.priceCoins)
                .apply();
        return true;
    }

    public static int count(Context context) {
        return prefs(context).getStringSet(OWNED, new HashSet<>()).size();
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
