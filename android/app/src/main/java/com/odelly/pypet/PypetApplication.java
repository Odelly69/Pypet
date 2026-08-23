package com.odelly.pypet;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

/** App-level initialization for persistent routines, reminders and notification permission. */
public final class PypetApplication extends Application {
    @Override public void onCreate(){
        super.onCreate();
        PypetNotificationManager.ensureChannel(this);
        PypetReminderScheduler.schedule(this);
        PetRoutineManager.tick(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks(){
            boolean asked=false;
            @Override public void onActivityResumed(Activity a){
                if(!asked && Build.VERSION.SDK_INT>=33 && a instanceof MainActivity && a.checkSelfPermission("android.permission.POST_NOTIFICATIONS")!=0){
                    asked=true;
                    a.requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"},4201);
                }
            }
            @Override public void onActivityCreated(Activity a,Bundle b){} @Override public void onActivityStarted(Activity a){} @Override public void onActivityPaused(Activity a){} @Override public void onActivityStopped(Activity a){} @Override public void onActivitySaveInstanceState(Activity a,Bundle b){} @Override public void onActivityDestroyed(Activity a){}
        });
    }
}
