package com.odelly.pypet;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;

/** Schedules low-frequency reminders; never uses vibration or flashing. */
public final class PypetReminderScheduler {
    private PypetReminderScheduler(){}
    public static void schedule(Context c){
        AlarmManager am=(AlarmManager)c.getSystemService(Context.ALARM_SERVICE); if(am==null)return;
        scheduleAt(am,c,"pet",9,0,2101);
        scheduleAt(am,c,"pet",18,0,2103);
        scheduleAt(am,c,"break",20,0,2102);
    }
    private static void scheduleAt(AlarmManager am,Context c,String kind,int hour,int minute,int id){
        Calendar cal=Calendar.getInstance();cal.set(Calendar.HOUR_OF_DAY,hour);cal.set(Calendar.MINUTE,minute);cal.set(Calendar.SECOND,0);cal.set(Calendar.MILLISECOND,0);
        if(cal.getTimeInMillis()<=System.currentTimeMillis())cal.add(Calendar.DAY_OF_YEAR,1);
        Intent i=new Intent(c,PypetReminderReceiver.class).putExtra("kind",kind);
        PendingIntent pi=PendingIntent.getBroadcast(c,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pi);
    }
}
