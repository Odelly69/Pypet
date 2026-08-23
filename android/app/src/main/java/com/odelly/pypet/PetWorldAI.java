package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

/** Independent, low-pressure pet movement model for the living World. */
public final class PetWorldAI {
    public static final class State {
        public float x,y,heading;
        public State(float x,float y,float heading){this.x=x;this.y=y;this.heading=heading;}
    }
    private static final String PREF="pypet_world_ai";
    private static final String X="x",Y="y",H="h",LAST="last",TARGET_X="tx",TARGET_Y="ty",NEXT="next";
    private static final Random RNG=new Random();
    private PetWorldAI(){}

    /** Advance using real elapsed time. Pets choose new destinations instead of following the player. */
    public static State tick(Context c,float minX,float maxX,float minY,float maxY){
        SharedPreferences p=c.getSharedPreferences(PREF,Context.MODE_PRIVATE);
        long now=System.currentTimeMillis(), last=p.getLong(LAST,0);
        float x=p.getFloat(X,0), y=p.getFloat(Y,170), h=p.getFloat(H,0);
        float tx=p.getFloat(TARGET_X,Float.NaN), ty=p.getFloat(TARGET_Y,Float.NaN);
        long next=p.getLong(NEXT,0);
        if(Float.isNaN(tx)||now>=next){
            tx=minX+RNG.nextFloat()*(maxX-minX);
            ty=minY+RNG.nextFloat()*(maxY-minY);
            next=now+3500L+RNG.nextInt(6500);
        }
        if(last==0){
            x=Math.max(minX,Math.min(maxX,x)); y=Math.max(minY,Math.min(maxY,y));
            last=now;
        }
        float dt=Math.min(0.25f,Math.max(0f,(now-last)/1000f));
        float dx=tx-x,dy=ty-y,dist=(float)Math.sqrt(dx*dx+dy*dy);
        if(dist>8){
            h=(float)Math.atan2(dy,dx);
            float speed=38f;
            x+=dx/dist*speed*dt; y+=dy/dist*speed*dt;
        }
        x=Math.max(minX,Math.min(maxX,x)); y=Math.max(minY,Math.min(maxY,y));
        p.edit().putFloat(X,x).putFloat(Y,y).putFloat(H,h).putFloat(TARGET_X,tx).putFloat(TARGET_Y,ty).putLong(NEXT,next).putLong(LAST,now).apply();
        return new State(x,y,h);
    }

    public static void reset(Context c,float x,float y){c.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit().putFloat(X,x).putFloat(Y,y).remove(TARGET_X).remove(TARGET_Y).putLong(LAST,System.currentTimeMillis()).apply();}
}
