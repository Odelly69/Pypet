package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/** Local achievement/trophy and consecutive-day reward tracking. */
public final class PypetAchievementManager {
    private static final String PREFS = "pypet_achievements";
    private static final String TROPHIES = "trophies";
    private static final String LAST_DAY = "last_activity_day";
    private static final String STREAK = "streak_days";
    private static final String BEST = "best_streak";

    public static final class Trophy {
        public final String id, name, description;
        public final int coins;
        public Trophy(String id, String name, String description, int coins) {
            this.id = id; this.name = name; this.description = description; this.coins = coins;
        }
    }

    private static final Trophy[] TROPHY_LIST = {
        new Trophy("first_step", "First Pawprint", "Complete your first activity.", 10),
        new Trophy("python_starter", "Python Sprout", "Complete your first Python lesson.", 25),
        new Trophy("caring_friend", "Caring Friend", "Complete five care activities.", 50),
        new Trophy("learning_path", "Learning Path", "Complete ten Python lessons.", 100),
        new Trophy("coin_collector", "Coin Collector", "Earn 250 Pypet Coins.", 75),
        new Trophy("world_builder", "World Builder", "Unlock five cosmetic world items.", 100),
        new Trophy("streak_7", "Seven-Day Spark", "Maintain a seven-day activity streak.", 100),
        new Trophy("streak_30", "Thirty-Day Trail", "Maintain a thirty-day activity streak.", 300),
        new Trophy("streak_100", "Century Companion", "Maintain a one-hundred-day activity streak.", 1000)
    };

    private PypetAchievementManager() {}

    public static Trophy[] trophies() { return TROPHY_LIST; }

    /** Record activity for today and return the updated consecutive-day streak. */
    public static int recordDailyActivity(Context context) {
        SharedPreferences p = prefs(context);
        String today = day();
        String last = p.getString(LAST_DAY, "");
        int streak = p.getInt(STREAK, 0);
        if (today.equals(last)) return streak;
        if (isYesterday(last)) streak++; else streak = 1;
        int best = Math.max(streak, p.getInt(BEST, 0));
        p.edit().putString(LAST_DAY, today).putInt(STREAK, streak).putInt(BEST, best).apply();
        return streak;
    }

    /** Award a streak milestone once, with coins. */
    public static boolean awardStreakMilestone(Context context, int streak) {
        int reward = streak == 7 ? 100 : streak == 30 ? 300 : streak == 100 ? 1000 : 0;
        if (reward == 0) return false;
        return awardTrophy(context, "streak_" + streak, reward);
    }

    public static boolean awardTrophy(Context context, String id, int fallbackCoins) {
        SharedPreferences p = prefs(context);
        Set<String> owned = new HashSet<>(p.getStringSet(TROPHIES, new HashSet<>()));
        if (!owned.add(id)) return false;
        p.edit().putStringSet(TROPHIES, owned).apply();
        if (fallbackCoins > 0) RewardInventory.addCoins(context, fallbackCoins);
        return true;
    }

    public static boolean hasTrophy(Context context, String id) {
        return prefs(context).getStringSet(TROPHIES, new HashSet<>()).contains(id);
    }

    public static int streak(Context context) { return prefs(context).getInt(STREAK, 0); }
    public static int bestStreak(Context context) { return prefs(context).getInt(BEST, 0); }
    public static int trophyCount(Context context) { return prefs(context).getStringSet(TROPHIES, new HashSet<>()).size(); }

    private static String day() { return new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date()); }
    private static boolean isYesterday(String last) {
        if (last == null || last.isEmpty()) return false;
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(last);
            long delta = new Date().getTime() - d.getTime();
            return delta >= 20L * 60L * 60L * 1000L && delta < 48L * 60L * 60L * 1000L;
        } catch (Exception ignored) { return false; }
    }
    private static SharedPreferences prefs(Context c) { return c.getSharedPreferences(PREFS, Context.MODE_PRIVATE); }
}
