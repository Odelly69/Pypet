package com.odelly.pypet;

import android.app.Application;

/** App-level initialization for persistent routines and gentle notifications. */
public final class PypetApplication extends Application {
    @Override public void onCreate(){
        super.onCreate();
        PypetNotificationManager.ensureChannel(this);
        PypetReminderScheduler.schedule(this);
        PetRoutineManager.tick(this);
    }
}
