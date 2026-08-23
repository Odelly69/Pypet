package com.odelly.pypet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/** Receives gentle scheduled reminders without requiring the app to stay open. */
public final class PypetReminderReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String kind=intent==null?"pet":intent.getStringExtra("kind");
        if("break".equals(kind)) PypetNotificationManager.remind(context,"Pypet study break","Nice work. Take a few minutes away from the screen before your next lesson.",2102);
        else PypetNotificationManager.remind(context,"Pypet care time","Your pet may be ready for food, play, or a little care.",2101);
    }
}
