package com.odelly.pypet;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/** Gentle local reminders; no vibration, flashing, or rapid visual effects. */
public final class PypetNotificationManager {
    private static final String CHANNEL="pypet_routine";
    private PypetNotificationManager(){}
    public static void ensureChannel(Context c){
        if(Build.VERSION.SDK_INT>=26){NotificationManager n=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);if(n!=null){NotificationChannel ch=new NotificationChannel(CHANNEL,"Pypet reminders",NotificationManager.IMPORTANCE_DEFAULT);ch.setVibrationPattern(new long[]{0});ch.enableVibration(false);n.createNotificationChannel(ch);}}
    }
    public static void remind(Context c,String title,String text,int id){
        ensureChannel(c);
        if(Build.VERSION.SDK_INT>=33 && c.checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=0)return;
        Intent i=new Intent(c,MainActivity.class);i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi=PendingIntent.getActivity(c,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(c,CHANNEL):new Notification.Builder(c);
        b.setSmallIcon(R.drawable.ic_pypet).setContentTitle(title).setContentText(text).setContentIntent(pi).setAutoCancel(true).setDefaults(0).setOngoing(false);
        NotificationManager n=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);if(n!=null)n.notify(id,b.build());
    }
}
