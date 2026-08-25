package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.LinkedHashMap;
import java.util.Map;

/** Persistent customization for the six core world structures. */
public final class WorldLayoutManager {
    public static final class Item {
        public final String id; public final float x,y,rotation,w,h;
        Item(String id,float x,float y,float rotation,float w,float h){this.id=id;this.x=x;this.y=y;this.rotation=rotation;this.w=w;this.h=h;}
    }
    private static final String PREF="pypet_world_custom_layout";
    private static final String[] IDS={"home","academy","market","workshop","park","library"};
    private static final float[] DX={635,635,-635,-635, -650,635};
    private static final float[] DY={910,-780,445,-780,905,445};
    private static final float[] W={430,430,430,430,860,430};
    private static final float[] H={300,320,310,320,370,310};
    private WorldLayoutManager(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREF,Context.MODE_PRIVATE);}
    public static Item get(Context c,String id){int i=index(id);if(i<0)return null;String k=IDS[i];return new Item(id,p(c).getFloat(k+"x",DX[i]),p(c).getFloat(k+"y",DY[i]),p(c).getFloat(k+"r",0),W[i],H[i]);}
    public static Map<String,Item> all(Context c){Map<String,Item> m=new LinkedHashMap<>();for(String id:IDS)m.put(id,get(c,id));return m;}
    public static boolean move(Context c,String id,float x,float y){Item cur=get(c,id);if(cur==null||!valid(c,id,x,y))return false;p(c).edit().putFloat(id+"x",x).putFloat(id+"y",y).apply();return true;}
    public static boolean rotate(Context c,String id,float degrees){Item cur=get(c,id);if(cur==null)return false;p(c).edit().putFloat(id+"r",normalize(cur.rotation+degrees)).apply();return true;}
    public static void reset(Context c){p(c).edit().clear().apply();}
    public static boolean valid(Context c,String id,float x,float y){Item cur=get(c,id);if(cur==null)return false;float r=Math.max(cur.w,cur.h)*.5f;float half=WorldExpansionManager.halfSize(c);if(Math.abs(x)+r>half-45||Math.abs(y)+r>half-45)return false;if(Math.abs(x)<205+r||Math.abs(y)<205+r)return false;
        if("park".equals(id)){for(Item o:all(c).values())if(!o.id.equals(id)&&overlap(x,y,r,o.x,o.y,Math.max(o.w,o.h)*.5f+35))return false;return true;}
        if(inPark(c,x,y,r))return false;
        for(Item o:all(c).values())if(!o.id.equals(id)&&!"park".equals(o.id)&&overlap(x,y,r,o.x,o.y,Math.max(o.w,o.h)*.5f+35))return false;
        return true;
    }
    private static boolean inPark(Context c,float x,float y,float r){Item park=get(c,"park");return overlap(x,y,r,park.x,park.y,Math.max(park.w,park.h)*.5f+20);}
    private static boolean overlap(float x,float y,float r,float ox,float oy,float or){return Math.hypot(x-ox,y-oy)<r+or;}
    private static int index(String id){for(int i=0;i<IDS.length;i++)if(IDS[i].equals(id))return i;return -1;}
    private static float normalize(float r){while(r>=360)r-=360;while(r<0)r+=360;return r;}
}
