package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

/** Prevents marathon study sessions and gives the learner deliberate recovery time. */
public final class LearningWellnessManager {
    private static final String PREFS="pypet_learning_wellness";
    private static final String START="session_start";
    private static final String LAST="last_lesson";
    private static final String COUNT="session_lessons";
    private static final String BREAK_UNTIL="break_until";
    public static final long RECOMMENDED_SESSION_MS=25L*60L*1000L;
    public static final long BREAK_MS=5L*60L*1000L;
    public static final long LONG_BREAK_MS=15L*60L*1000L;
    private LearningWellnessManager(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    public static void start(Context c){if(p(c).getLong(START,0)==0)p(c).edit().putLong(START,System.currentTimeMillis()).putInt(COUNT,0).apply();}
    public static long sessionMinutes(Context c){long s=p(c).getLong(START,0);return s==0?0:(System.currentTimeMillis()-s)/60000;}
    public static boolean breakDue(Context c){long until=p(c).getLong(BREAK_UNTIL,0);return until>System.currentTimeMillis();}
    public static long breakRemainingMs(Context c){return Math.max(0,p(c).getLong(BREAK_UNTIL,0)-System.currentTimeMillis());}
    public static boolean shouldSuggestBreak(Context c){start(c);return sessionMinutes(c)>=25 || p(c).getInt(COUNT,0)>=4;}
    public static void lessonCompleted(Context c){start(c);int count=p(c).getInt(COUNT,0)+1;long breakMs=count%4==0?LONG_BREAK_MS:BREAK_MS;p(c).edit().putInt(COUNT,count).putLong(LAST,System.currentTimeMillis()).putLong(BREAK_UNTIL,System.currentTimeMillis()+breakMs).apply();}
    public static void resetSession(Context c){p(c).edit().putLong(START,System.currentTimeMillis()).putInt(COUNT,0).putLong(BREAK_UNTIL,0).apply();}
}
