package com.odelly.pypet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/** Gentle local reminders for pet care and learning recovery. */
public final class PypetNotificationManager {
    private static final String CHANNEL="pypet_routine";
    private PypetNotificationManager(){}
    public static void ensureChannel(Context c){
        if(Build.VERSION.SDK_INT>=26){NotificationManager n=(NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);if(n!=null)n.createNotificationChannel(new NotificationChannel(CHANNEL,"Pypet reminders",NotificationManager.IMPORTANCE_DEFAULT));}
    }
    public static void remind(Context c,String title,String text,int id){
        ensureChannel(c);
        Intent i=new Intent(c,MainActivity.class);i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi=PendingIntent.getActivity(c,id,i,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder b=new NotificationCompat.Builder(c,CHANNEL).setSmallIcon(com.odelly.pypet.R.drawable.ic_pypet).setContentTitle(title).setContentText(text).setContentIntent(pi).setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if(Build.VERSION.SDK_INT<33 || NotificationManagerCompat.from(c).areNotificationsEnabled())NotificationManagerCompat.from(c).notify(id,b.build());
    }
}
