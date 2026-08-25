package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Random;

/**
 * Independent, low-pressure pet movement model for the living World.
 * Pets wander between safe destinations, pause naturally, turn smoothly,
 * and avoid roads/buildings rather than flying through structures.
 */
public final class PetWorldAI {
    public static final class State {
        public final float x,y,heading,speed,stepPhase;
        public final boolean walking;
        public State(float x,float y,float heading,float speed,float stepPhase,boolean walking){
            this.x=x;this.y=y;this.heading=heading;this.speed=speed;this.stepPhase=stepPhase;this.walking=walking;
        }
    }
    private static final String PREF="pypet_world_ai";
    private static final String X="x",Y="y",H="h",LAST="last",TARGET_X="tx",TARGET_Y="ty",NEXT="next",PHASE="phase";
    private static final Random RNG=new Random();
    private PetWorldAI(){}

    /** Advance using real elapsed time. Destinations are restricted to walkable town areas. */
    public static State tick(Context c,float minX,float maxX,float minY,float maxY){
        SharedPreferences p=c.getSharedPreferences(PREF,Context.MODE_PRIVATE);
        long now=System.currentTimeMillis(), last=p.getLong(LAST,0);
        float x=p.getFloat(X,0), y=p.getFloat(Y,170), h=p.getFloat(H,0);
        float tx=p.getFloat(TARGET_X,Float.NaN), ty=p.getFloat(TARGET_Y,Float.NaN);
        long next=p.getLong(NEXT,0);
        float phase=p.getFloat(PHASE,0);

        if(last==0){
            x=clamp(x,minX,maxX); y=clamp(y,minY,maxY);
            if(!walkable(x,y)){x=300;y=720;}
            last=now;
        }
        if(!walkable(x,y)){float[] spawn=randomWalkable(minX,maxX,minY,maxY);x=spawn[0];y=spawn[1];}

        if(Float.isNaN(tx)||now>=next||Math.hypot(tx-x,ty-y)<12){
            float[] target=randomWalkable(minX,maxX,minY,maxY);
            tx=target[0]; ty=target[1];
            next=now+3500L+RNG.nextInt(6500);
        }

        float dt=Math.min(0.20f,Math.max(0f,(now-last)/1000f));
        float dx=tx-x,dy=ty-y,dist=(float)Math.sqrt(dx*dx+dy*dy);
        boolean walking=dist>12;
        float speed=walking ? 32f+RNG.nextFloat()*8f : 0f;
        if(walking){
            float desired=(float)Math.atan2(dy,dx);
            h=turnToward(h,desired,Math.min(2.8f*dt,Math.abs(angleDelta(h,desired))));
            float nx=x+(float)Math.cos(h)*speed*dt;
            float ny=y+(float)Math.sin(h)*speed*dt;
            if(walkable(nx,ny)){x=nx;y=ny;}
            else{float[] detour=randomWalkable(minX,maxX,minY,maxY);tx=detour[0];ty=detour[1];next=now+1200L;}
            phase+=speed*dt*0.18f;
        }else{phase+=dt*0.7f;}
        x=clamp(x,minX,maxX); y=clamp(y,minY,maxY);
        p.edit().putFloat(X,x).putFloat(Y,y).putFloat(H,h).putFloat(TARGET_X,tx).putFloat(TARGET_Y,ty)
                .putLong(NEXT,next).putLong(LAST,now).putFloat(PHASE,phase).apply();
        return new State(x,y,h,speed,phase,walking);
    }

    /** Safe destinations include lawns, park space, and the sidewalks beside roads. */
    private static float[] randomWalkable(float minX,float maxX,float minY,float maxY){
        for(int i=0;i<80;i++){
            float x=minX+RNG.nextFloat()*(maxX-minX),y=minY+RNG.nextFloat()*(maxY-minY);
            if(walkable(x,y))return new float[]{x,y};
        }
        return new float[]{300,720};
    }

    private static boolean walkable(float x,float y){
        if(x<-1120||x>1120||y<-1120||y>1120)return false;
        if(Math.abs(x)<155||Math.abs(y)<155)return false;
        if(inRect(x,y,-875,-965,-395,-595)||inRect(x,y,395,-965,875,-595)
                ||inRect(x,y,395,260,875,630)||inRect(x,y,-875,260,-395,630)
                ||inRect(x,y,395,730,875,1080))return false;
        return true;
    }
    private static boolean inRect(float x,float y,float l,float t,float r,float b){return x>=l-25&&x<=r+25&&y>=t-25&&y<=b+25;}
    private static float clamp(float v,float lo,float hi){return Math.max(lo,Math.min(hi,v));}
    private static float angleDelta(float from,float to){float d=to-from;while(d>Math.PI)d-=2*Math.PI;while(d<-Math.PI)d+=2*Math.PI;return d;}
    private static float turnToward(float from,float to,float amount){float d=angleDelta(from,to);return from+Math.max(-amount,Math.min(amount,d));}

    public static void reset(Context c,float x,float y){
        if(!walkable(x,y)){x=300;y=720;}
        c.getSharedPreferences(PREF,Context.MODE_PRIVATE).edit().putFloat(X,x).putFloat(Y,y)
                .remove(TARGET_X).remove(TARGET_Y).putLong(LAST,System.currentTimeMillis()).putFloat(PHASE,0).apply();
    }
}
