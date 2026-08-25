package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Persistent town placement rules: parks, roads, lots and existing objects are collision-aware. */
public final class WorldPlacementManager {
    public static final class Placement {
        public final String id; public final float x,y,scale,rotation;
        public Placement(String id,float x,float y,float scale,float rotation){this.id=id;this.x=x;this.y=y;this.scale=scale;this.rotation=rotation;}
    }
    private static final String PREFS="pypet_world_layout",COUNT="placement_count",PREFIX="placement_",TROPHY_PREFIX="trophy_";
    private static final float EDGE=70f,ROAD_BLOCK=205f,OBJECT_PAD=60f;
    // Exact Park footprint rendered by LivingWorldView: lower-left recreation land, away from all buildings.
    private static final float PARK_L=-1080f,PARK_T=720f,PARK_R=-220f,PARK_B=1090f;
    private WorldPlacementManager(){}
    private static float half(Context c){return WorldExpansionManager.halfSize(c);}
    private static SharedPreferences prefs(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    public static boolean canPlace(Context c,String id,float x,float y,float scale){return findSafePosition(c,id,x,y,saneScale(scale),-1)!=null;}
    public static void place(Context c,String id,float x,float y,float scale,float rotation){
        if(id==null||id.trim().isEmpty())return; float s=saneScale(scale),safe[]=findSafePosition(c,id,x,y,s,-1); if(safe==null)return;
        int index=prefs(c).getInt(COUNT,0); prefs(c).edit().putString(PREFIX+index,encode(id,safe[0],safe[1],s,rotation)).putInt(COUNT,index+1).apply();
    }
    public static void placeUnique(Context c,String id,float x,float y,float scale,float rotation){for(Placement p:allRaw(c))if(p.id.equals(id))return;place(c,id,x,y,scale,rotation);}
    public static boolean update(Context c,int index,String id,float x,float y,float scale,float rotation){List<Placement> raw=allRaw(c);if(index<0||index>=raw.size())return false;float s=saneScale(scale),safe[]=findSafePosition(c,id,x,y,s,index);if(safe==null)return false;prefs(c).edit().putString(PREFIX+index,encode(id,safe[0],safe[1],s,rotation)).apply();return true;}
    public static List<Placement> all(Context c){return Collections.unmodifiableList(new ArrayList<>(allRaw(c)));}
    public static boolean hasTrophyDisplay(Context c,String trophyId){return prefs(c).getBoolean(TROPHY_PREFIX+trophyId,false);}
    public static void setTrophyDisplay(Context c,String trophyId,boolean displayed){prefs(c).edit().putBoolean(TROPHY_PREFIX+trophyId,displayed).apply();}
    private static float[] findSafePosition(Context c,String id,float x,float y,float scale,int ignoreIndex){
        float radius=footprint(id,scale),worldHalf=half(c);x=clamp(x,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);y=clamp(y,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);
        if(isValid(c,id,x,y,radius,ignoreIndex))return new float[]{x,y};
        for(float ring=60;ring<=1000;ring+=60){int points=Math.max(12,(int)(ring/28));for(int i=0;i<points;i++){double a=(Math.PI*2*i)/points;float nx=clamp(x+(float)Math.cos(a)*ring,-worldHalf+EDGE+radius,worldHalf-EDGE-radius),ny=clamp(y+(float)Math.sin(a)*ring,-worldHalf+EDGE+radius,worldHalf-EDGE-radius);if(isValid(c,id,nx,ny,radius,ignoreIndex))return new float[]{nx,ny};}}
        return null;
    }
    private static boolean isValid(Context c,String id,float x,float y,float radius,int ignoreIndex){
        float worldHalf=half(c);if(Math.abs(x)+radius>worldHalf-EDGE||Math.abs(y)+radius>worldHalf-EDGE)return false;
        if(Math.abs(x)<ROAD_BLOCK+radius||Math.abs(y)<ROAD_BLOCK+radius)return false;
        if(inBuilding(x,y,-850,-940,-420,-620,radius)||inBuilding(x,y,420,-940,850,-620,radius)||inBuilding(x,y,420,290,850,600,radius)||inBuilding(x,y,-850,290,-420,600,radius)||inBuilding(x,y,420,760,850,1060,radius))return false;
        if(inRect(x,y,PARK_L,PARK_T,PARK_R,PARK_B,radius)&&!parkAllowed(id))return false;
        if(isBuilding(id)&&!inBuildingLot(x,y,radius))return false;
        List<Placement> existing=allRaw(c);for(int i=0;i<existing.size();i++){if(i==ignoreIndex)continue;Placement other=existing.get(i);float minDistance=radius+footprint(other.id,saneScale(other.scale))+OBJECT_PAD;if(Math.hypot(x-other.x,y-other.y)<minDistance)return false;}
        return true;
    }
    private static boolean isBuilding(String id){if(id==null)return false;String s=id.toLowerCase();return s.contains("building")||s.contains("workshop")||s.contains("academy")||s.contains("market")||s.contains("library")||s.contains("home")||s.contains("hatchery")||s.contains("trophy_hall")||s.contains("castle")||s.contains("tower")||s.contains("palace");}
    private static boolean inBuildingLot(float x,float y,float pad){return inRect(x,y,-910,-990,-250,-560,pad)||inRect(x,y,250,-990,910,-560,pad)||inRect(x,y,-910,250,-250,670,pad)||inRect(x,y,250,250,910,670,pad)||inRect(x,y,-910,720,-250,1090,pad)||inRect(x,y,250,720,910,1090,pad);}
    private static boolean parkAllowed(String id){if(id==null)return false;return id.equals("bench")||id.equals("flower_bed")||id.equals("tree")||id.equals("garden_sign")||id.equals("lamp")||id.equals("fountain")||id.equals("garden")||id.equals("garden_patch");}
    private static boolean inBuilding(float x,float y,float l,float t,float r,float b,float pad){return x>=l-pad&&x<=r+pad&&y>=t-pad&&y<=b+pad;}
    private static boolean inRect(float x,float y,float l,float t,float r,float b,float pad){return x>=l-pad&&x<=r+pad&&y>=t-pad&&y<=b+pad;}
    private static float footprint(String id,float scale){float base=65f;if(isBuilding(id))base=105f;if(id!=null&&(id.contains("trophy")||id.contains("premium")||id.contains("exclusive")))base=80f;if(id!=null&&id.equals("tree"))base=48f;return base*scale;}
    private static float clamp(float v,float lo,float hi){return Math.max(lo,Math.min(hi,v));}
    private static float saneScale(float scale){return Math.max(.35f,Math.min(2.5f,scale));}
    private static String encode(String id,float x,float y,float scale,float rotation){return id+"|"+x+"|"+y+"|"+scale+"|"+rotation;}
    private static Placement decode(String value){try{if(value==null)return null;String[] p=value.split("\\|",-1);if(p.length!=5)return null;return new Placement(p[0],Float.parseFloat(p[1]),Float.parseFloat(p[2]),Float.parseFloat(p[3]),Float.parseFloat(p[4]));}catch(Exception ignored){return null;}}
    private static List<Placement> allRaw(Context c){int count=prefs(c).getInt(COUNT,0);List<Placement> result=new ArrayList<>();for(int i=0;i<count;i++){Placement p=decode(prefs(c).getString(PREFIX+i,null));if(p!=null)result.add(p);}return result;}
}
