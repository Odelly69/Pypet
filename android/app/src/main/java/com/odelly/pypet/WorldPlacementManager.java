package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persists World objects and validates placement against roads, sidewalks and fixed building footprints before saving. */
public final class WorldPlacementManager {
    public static final class Placement { public final String id; public final float x,y,scale,rotation; public Placement(String id,float x,float y,float scale,float rotation){this.id=id;this.x=x;this.y=y;this.scale=scale;this.rotation=rotation;} }
    private static final String PREFS="pypet_world_layout",COUNT="placement_count",PREFIX="placement_",TROPHY_PREFIX="trophy_";
    private static final float EDGE=70f,ROAD_BLOCK=205f,BUILDING_PAD=60f;
    private WorldPlacementManager(){}
    private static float half(Context c){return WorldExpansionManager.halfSize(c);}
    public static void place(Context c,String id,float x,float y,float scale,float rotation){if(id==null||id.trim().isEmpty())return;float s=saneScale(scale);float[] safe=findSafePosition(c,id,x,y,s,-1);SharedPreferences.Editor e=prefs(c).edit();int index=prefs(c).getInt(COUNT,0);e.putString(PREFIX+index,encode(id,safe[0],safe[1],s,rotation)).putInt(COUNT,index+1).apply();}
    public static void placeUnique(Context c,String id,float x,float y,float scale,float rotation){for(Placement p:allRaw(c))if(p.id.equals(id))return;place(c,id,x,y,scale,rotation);}
    public static void update(Context c,int index,String id,float x,float y,float scale,float rotation){float s=saneScale(scale);float[] safe=findSafePosition(c,id,x,y,s,index);prefs(c).edit().putString(PREFIX+index,encode(id,safe[0],safe[1],s,rotation)).apply();}
    public static List<Placement> all(Context c){List<Placement> result=new ArrayList<>();List<Placement> raw=allRaw(c);for(int i=0;i<raw.size();i++){Placement p=raw.get(i);float[] safe=findSafePosition(c,p.id,p.x,p.y,saneScale(p.scale),i);result.add(new Placement(p.id,safe[0],safe[1],saneScale(p.scale),p.rotation));}return Collections.unmodifiableList(result);}
    public static boolean hasTrophyDisplay(Context c,String trophyId){return prefs(c).getBoolean(TROPHY_PREFIX+trophyId,false);}
    public static void setTrophyDisplay(Context c,String trophyId,boolean displayed){prefs(c).edit().putBoolean(TROPHY_PREFIX+trophyId,displayed).apply();}
    /** Test the requested point first; if it collides, search nearby for the nearest legal point. */
    private static float[] findSafePosition(Context c,String id,float x,float y,float scale,int ignoreIndex){float radius=footprint(id,scale);float worldHalf=half(c);x=clamp(x,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);y=clamp(y,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);if(isValid(c,id,x,y,radius,ignoreIndex))return new float[]{x,y};for(float ring=80;ring<=720;ring+=80){int points=Math.max(8,(int)(ring/35));for(int i=0;i<points;i++){double a=(Math.PI*2*i)/points;float nx=clamp(x+(float)Math.cos(a)*ring,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);float ny=clamp(y+(float)Math.sin(a)*ring,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);if(isValid(c,id,nx,ny,radius,ignoreIndex))return new float[]{nx,ny};}}return new float[]{id!=null&&id.startsWith("trophy_")?-700f:520f,id!=null&&id.startsWith("trophy_")?-430f:760f};}
    private static boolean isValid(Context c,String id,float x,float y,float radius,int ignoreIndex){float worldHalf=half(c);if(Math.abs(x)+radius>worldHalf-EDGE||Math.abs(y)+radius>worldHalf-EDGE)return false;if(Math.abs(x)<ROAD_BLOCK+radius||Math.abs(y)<ROAD_BLOCK+radius)return false;if(inBuilding(x,y,-850,-940,-420,-620,radius)||inBuilding(x,y,420,-940,850,-620,radius)||inBuilding(x,y,420,290,850,600,radius)||inBuilding(x,y,-850,290,-420,600,radius)||inBuilding(x,y,420,760,850,1050,radius))return false;if(!parkAllowed(id)&&inRect(x,y,-1060,180,-220,1060,radius))return false;return true;}
    private static boolean parkAllowed(String id){return id!=null&&(id.equals("bench")||id.equals("flower_bed")||id.equals("tree")||id.equals("garden_sign")||id.equals("lamp"));}
    private static boolean inBuilding(float x,float y,float l,float t,float r,float b,float pad){return x>=l-pad&&x<=r+pad&&y>=t-pad&&y<=b+pad;}
    private static boolean inRect(float x,float y,float l,float t,float r,float b,float pad){return x>=l-pad&&x<=r+pad&&y>=t-pad&&y<=b+pad;}
    private static float footprint(String id,float scale){float base=65f;if(id!=null&&(id.contains("trophy")||id.contains("premium")||id.contains("exclusive")))base=80f;if(id!=null&&id.equals("tree"))base=48f;return base*scale;}
    private static float clamp(float v,float lo,float hi){return Math.max(lo,Math.min(hi,v));}
    private static float saneScale(float scale){return Math.max(.35f,Math.min(2.5f,scale));}
    private static String encode(String id,float x,float y,float scale,float rotation){return id+"|"+x+"|"+y+"|"+scale+"|"+rotation;}
    private static Placement decode(String value){if(value==null)return null;try{String[] p=value.split("\\|",-1);if(p.length!=5)return null;return new Placement(p[0],Float.parseFloat(p[1]),Float.parseFloat(p[2]),Float.parseFloat(p[3]),Float.parseFloat(p[4]));}catch(Exception ignored){return null;}}
    private static List<Placement> allRaw(Context c){int count=prefs(c).getInt(COUNT,0);List<Placement> result=new ArrayList<>();for(int i=0;i<count;i++){Placement p=decode(prefs(c).getString(PREFIX+i,null));if(p!=null)result.add(p);}return result;}
    private static SharedPreferences prefs(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
}
