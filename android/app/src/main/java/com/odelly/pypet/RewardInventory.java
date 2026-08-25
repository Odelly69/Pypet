package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/** Local Pypet Coin wallet and earned-item inventory. */
public final class RewardInventory {
    private static final String PREFS = "pypet_rewards";
    private static final String OWNED = "owned_items";
    private static final String COINS = "pypet_coins";
    private static final String TASK_PREFIX = "task_";

    private RewardInventory() {}

    public static int coins(Context context) {
        return Math.max(0, prefs(context).getInt(COINS, 0));
    }

    /** Add currency after a qualifying, app-controlled reward event. */
    public static void addCoins(Context context, int amount) {
        if (amount <= 0) return;
        addCoinsInternal(context, amount);
    }

    /** Spend currency atomically when a player buys an eligible in-game item/upgrade. */
    public static boolean spendCoins(Context context, int amount) {
        if (amount <= 0) return false;
        SharedPreferences p = prefs(context);
        int current = coins(context);
        if (current < amount) return false;
        return p.edit().putInt(COINS, current - amount).commit();
    }

    /** Award a task once per task ID. Returns false if it was already completed. */
    public static boolean completeTask(Context context, String taskId, int rewardCoins) {
        if (taskId == null || taskId.trim().isEmpty() || rewardCoins <= 0) return false;
        SharedPreferences p = prefs(context);
        String key = TASK_PREFIX + taskId;
        if (p.getBoolean(key, false)) return false;
        addCoinsInternal(context, rewardCoins);
        p.edit().putBoolean(key, true).apply();
        return true;
    }

    public static boolean taskCompleted(Context context, String taskId) {
        return taskId != null && prefs(context).getBoolean(TASK_PREFIX + taskId, false);
    }

    private static void addCoinsInternal(Context context, int amount) {
        int current = coins(context);
        long total = (long) current + amount;
        prefs(context).edit().putInt(COINS, (int) Math.min(Integer.MAX_VALUE, total)).apply();
    }

    public static boolean owns(Context context, String itemId) {
        return prefs(context).getStringSet(OWNED, new HashSet<>()).contains(itemId);
    }

    /** Purchase an item using earned Pypet Coins. */
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
