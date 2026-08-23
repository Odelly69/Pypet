package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.concurrent.TimeUnit;

/** Real-time pet routine. Needs change gradually with elapsed wall-clock time, not per screen refresh. */
public final class PetRoutineManager {
    private static final String PREFS="pypet_routine";
    private static final String LAST="last_tick";
    private PetRoutineManager() {}

    public static void tick(Context c) {
        SharedPreferences p=c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        long now=System.currentTimeMillis(), last=p.getLong(LAST,0);
        if(last==0){p.edit().putLong(LAST,now).apply();return;}
        long minutes=TimeUnit.MILLISECONDS.toMinutes(now-last);
        if(minutes<5)return;
        // A gentle simulation: hunger and happiness decline with elapsed time;
        // health only declines when basic needs have been neglected for a sustained period.
        int hunger=PetEvolutionManager.hunger(c);
        int happy=PetEvolutionManager.happiness(c);
        int health=PetEvolutionManager.health(c);
        int steps=(int)Math.min(12,minutes/30);
        if(steps>0){
            for(int i=0;i<steps;i++){
                if(hunger>0) hunger--;
                if(happy>0) happy--;
                if(hunger<25 && health>0) health--;
            }
            SharedPreferences pet=c.getSharedPreferences("pypet_pet",Context.MODE_PRIVATE);
            pet.edit().putInt("hunger",hunger).putInt("happiness",happy).putInt("health",health).apply();
            p.edit().putLong(LAST,now).apply();
        }
    }
}
