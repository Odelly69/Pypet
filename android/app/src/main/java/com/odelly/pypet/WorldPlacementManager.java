package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persists player-placed World objects inside the current 2400x2400 town and keeps them on sensible lots. */
public final class WorldPlacementManager {
    public static final class Placement { public final String id; public final float x,y,scale,rotation; public Placement(String id,float x,float y,float scale,float rotation){this.id=id;this.x=x;this.y=y;this.scale=scale;this.rotation=rotation;} }
    private static final String PREFS="pypet_world_layout",COUNT="placement_count",PREFIX="placement_",TROPHY_PREFIX="trophy_";
    private static final float WORLD_HALF_W=1200f,WORLD_HALF_H=1200f,ROAD_HALF=160f,EDGE=55f,BUILDING_PAD=45f;
    private WorldPlacementManager(){}
    public static void place(Context c,String id,float x,float y,float scale,float rotation){if(id==null||id.trim().isEmpty())return;float[] safe=safePosition(x,y);SharedPreferences.Editor e=prefs(c).edit();int index=prefs(c).getInt(COUNT,0);e.putString(PREFIX+index,encode(id,safe[0],safe[1],saneScale(scale),rotation)).putInt(COUNT,index+1).apply();}
    public static void placeUnique(Context c,String id,float x,float y,float scale,float rotation){for(Placement p:all(c))if(p.id.equals(id))return;place(c,id,x,y,scale,rotation);}
    public static void update(Context c,int index,String id,float x,float y,float scale,float rotation){float[] safe=safePosition(x,y);prefs(c).edit().putString(PREFIX+index,encode(id,safe[0],safe[1],saneScale(scale),rotation)).apply();}
    public static List<Placement> all(Context c){int count=prefs(c).getInt(COUNT,0);List<Placement> result=new ArrayList<>();for(int i=0;i<count;i++){Placement p=decode(prefs(c).getString(PREFIX+i,null));if(p!=null){float[] safe=safePosition(p.x,p.y);result.add(new Placement(p.id,safe[0],safe[1],saneScale(p.scale),p.rotation));}}return Collections.unmodifiableList(result);}
    public static boolean hasTrophyDisplay(Context c,String trophyId){return prefs(c).getBoolean(TROPHY_PREFIX+trophyId,false);}
    public static void setTrophyDisplay(Context c,String trophyId,boolean displayed){prefs(c).edit().putBoolean(TROPHY_PREFIX+trophyId,displayed).apply();}

    /** Keep decorations on buildable land, inside the map, away from roads and out of building footprints. */
    private static float[] safePosition(float x,float y){
        x=Math.max(-WORLD_HALF_W+EDGE,Math.min(WORLD_HALF_W-EDGE,x));y=Math.max(-WORLD_HALF_H+EDGE,Math.min(WORLD_HALF_H-EDGE,y));
        if(Math.abs(x)<ROAD_HALF)x=x>=0?ROAD_HALF+EDGE:-ROAD_HALF-EDGE;
        if(Math.abs(y)<ROAD_HALF)y=y>=0?ROAD_HALF+EDGE:-ROAD_HALF-EDGE;
        for(int i=0;i<2;i++){
            if(inBuilding(x,y,-850,-940,-420,-620)||inBuilding(x,y,420,-940,850,-620)
                    ||inBuilding(x,y,420,290,850,600)||inBuilding(x,y,-850,290,-420,600)
                    ||inBuilding(x,y,420,760,850,1050)){
                x = x<0 ? -1080 : 1080;
                y = y<0 ? -1080 : 720;
            }
            if(Math.abs(x)<ROAD_HALF)x=x>=0?ROAD_HALF+EDGE:-ROAD_HALF-EDGE;
            if(Math.abs(y)<ROAD_HALF)y=y>=0?ROAD_HALF+EDGE:-ROAD_HALF-EDGE;
        }
        return new float[]{x,y};
    }
    private static boolean inBuilding(float x,float y,float l,float t,float r,float b){return x>=l-BUILDING_PAD&&x<=r+BUILDING_PAD&&y>=t-BUILDING_PAD&&y<=b+BUILDING_PAD;}
    private static float saneScale(float scale){return Math.max(.35f,Math.min(2.5f,scale));}
    private static String encode(String id,float x,float y,float scale,float rotation){return id+"|"+x+"|"+y+"|"+scale+"|"+rotation;}
    private static Placement decode(String value){if(value==null)return null;try{String[] p=value.split("\\|",-1);if(p.length!=5)return null;return new Placement(p[0],Float.parseFloat(p[1]),Float.parseFloat(p[2]),Float.parseFloat(p[3]),Float.parseFloat(p[4]));}catch(Exception ignored){return null;}}
    private static SharedPreferences prefs(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
}
