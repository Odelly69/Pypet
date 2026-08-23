package com.odelly.pypet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Species-aware, gentle locomotion model for the World pet actor.
 * This keeps movement deterministic and epilepsy-conscious: no flashing,
 * screen shake, or rapid camera motion.
 */
public final class PetMovementController {
    public enum Locomotion { WALK, RUN, HOP, SWIM, CRAWL, FLY, IDLE }

    private float x = .5f, y = .62f;
    private float targetX = x, targetY = y;
    private float vx, vy;
    private long lastMs;
    private boolean moving;

    public PetMovementController() { lastMs = System.currentTimeMillis(); }

    public void setTarget(float x, float y) {
        targetX = clamp(x); targetY = clamp(y); moving = true;
    }

    public void update(long now, Locomotion mode) {
        float dt = Math.min(.08f, Math.max(.001f, (now-lastMs)/1000f));
        lastMs = now;
        float dx = targetX-x, dy=targetY-y;
        float d = (float)Math.sqrt(dx*dx+dy*dy);
        if(d < .008f) { moving=false; vx*=.7f; vy*=.7f; return; }
        float speed = mode==Locomotion.RUN ? .42f : mode==Locomotion.HOP ? .34f : .20f;
        vx += (dx/d*speed-vx)*Math.min(1f,dt*5f);
        vy += (dy/d*speed-vy)*Math.min(1f,dt*5f);
        x=clamp(x+vx*dt); y=clamp(y+vy*dt);
    }

    public float x(){return x;} public float y(){return y;} public boolean moving(){return moving;}

    /** Draws a simple full-body actor with species-appropriate gait cues. */
    public void draw(Canvas c, Paint p, float cx, float cy, float scale, Locomotion mode, long now, boolean left) {
        float phase=(now%900L)/900f;
        if(mode==Locomotion.HOP) {
            float hop=(float)Math.sin(phase*Math.PI*2)*Math.max(0, (float)Math.sin(phase*Math.PI));
            cy-=hop*scale*.65f;
            p.setStyle(Paint.Style.FILL);
            c.drawOval(cx-scale*.38f,cy-scale*.05f,cx+scale*.38f,cy+scale*.20f,p);
            c.drawCircle(cx,cy-scale*.34f,scale*.30f,p);
            c.drawOval(cx-scale*.38f,cy-scale*.08f,cx-scale*.02f,cy+scale*.18f,p);
            c.drawOval(cx+scale*.02f,cy-scale*.08f,cx+scale*.38f,cy+scale*.18f,p);
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(Math.max(1,scale*.05f));
            c.drawLine(cx-scale*.18f,cy+scale*.08f,cx-scale*.42f,cy+scale*.30f,p);
            c.drawLine(cx+scale*.18f,cy+scale*.08f,cx+scale*.42f,cy+scale*.30f,p);
            p.setStyle(Paint.Style.FILL);
            return;
        }
        float stride=moving?(float)Math.sin(phase*Math.PI*2)*scale*.13f:0;
        c.drawOval(cx-scale*.28f,cy-scale*.05f,cx+scale*.28f,cy+scale*.45f,p);
        c.drawCircle(cx,cy-scale*.28f,scale*.28f,p);
        c.drawOval(cx-scale*.18f,cy-scale*.53f,cx-scale*.02f,cy-scale*.20f,p);
        c.drawOval(cx+scale*.02f,cy-scale*.53f,cx+scale*.18f,cy-scale*.20f,p);
        c.drawLine(cx-scale*.14f,cy+scale*.28f,cx-scale*.14f+stride,cy+scale*.58f,p);
        c.drawLine(cx+scale*.14f,cy+scale*.28f,cx+scale*.14f-stride,cy+scale*.58f,p);
        c.drawLine(cx-scale*.18f,cy+scale*.10f,cx-scale*.38f-stride,cy+scale*.30f,p);
        c.drawLine(cx+scale*.18f,cy+scale*.10f,cx+scale*.38f+stride,cy+scale*.30f,p);
        p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(Math.max(1,scale*.045f));
        Path tail=new Path();tail.moveTo(cx+scale*.20f,cy+scale*.10f);tail.quadTo(cx+scale*.60f,cy-scale*.02f,cx+scale*.48f,cy-scale*.22f);c.drawPath(tail,p);p.setStyle(Paint.Style.FILL);
    }

    public static Locomotion forSpecies(String displayName, String emoji) {
        String s=((displayName==null?"":displayName)+" "+(emoji==null?"":emoji)).toLowerCase();
        if(s.contains("frog") || s.contains("🐸")) return Locomotion.HOP;
        if(s.contains("fish") || s.contains("🐟") || s.contains("shark") || s.contains("🦈")) return Locomotion.SWIM;
        if(s.contains("bird") || s.contains("eagle") || s.contains("🦅") || s.contains("🐦")) return Locomotion.FLY;
        if(s.contains("snake") || s.contains("🐍")) return Locomotion.CRAWL;
        return Locomotion.WALK;
    }

    private static float clamp(float v){return Math.max(.05f,Math.min(.95f,v));}
}