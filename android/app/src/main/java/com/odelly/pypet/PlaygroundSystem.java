package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

/** Persistent playground progression. Equipment unlocks through the same balanced development paths as the rest of Pypet. */
public final class PlaygroundSystem {
    public static final class Equipment {
        public final String id,name,icon,description; public final int unlockScore,fun,confidence,learning;
        Equipment(String i,String n,String ic,String d,int s,int f,int c,int l){id=i;name=n;icon=ic;description=d;unlockScore=s;fun=f;confidence=c;learning=l;}
    }
    private static final String PREFS="pypet_playground";
    private static final String OWNED="owned";
    private static final List<Equipment> ITEMS=Collections.unmodifiableList(Arrays.asList(
        new Equipment("bench","Picnic Bench","🪑","A calm social space for pets to rest and interact.",40,3,2,0),
        new Equipment("spring_rider","Spring Rider","🐴","A gentle rocking play station.",45,7,3,0),
        new Equipment("swing","Swing Set","🎠","Classic swings with calm, non-flashing animation.",50,10,4,0),
        new Equipment("slide","Slide","🛝","A small slide that builds playful exploration.",55,12,3,1),
        new Equipment("seesaw","Seesaw","⚖️","Cooperative balance play for two pets.",60,12,5,1),
        new Equipment("merry_go_round","Merry-Go-Round","🎡","A rotating social ride for multiple pets.",65,15,6,1),
        new Equipment("climber","Climbing Frame","🧗","A multi-route climbing structure.",70,16,6,3),
        new Equipment("monkey_bars","Overhead Bars","〰️","A coordination and persistence challenge.",75,17,7,4),
        new Equipment("play_tower","Play Tower","🏰","An expandable tower with climbing, bridge and slide sections.",80,20,8,5),
        new Equipment("nature_play","Nature Play Area","🌳","Logs, stepping stones and discovery stations.",85,20,10,8)
    ));
    private PlaygroundSystem(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    private static Set<String> owned(Context c){return new HashSet<>(p(c).getStringSet(OWNED,Collections.emptySet()));}
    public static List<Equipment> all(){return ITEMS;}
    public static boolean owns(Context c,String id){return owned(c).contains(id);}
    public static int ownedCount(Context c){return owned(c).size();}
    public static void unlockEligible(Context c){
        int score=PetEvolutionManager.balancedDevelopmentScore(c); Set<String> set=owned(c);
        for(Equipment e:ITEMS) if(score>=e.unlockScore) set.add(e.id);
        p(c).edit().putStringSet(OWNED,set).apply();
    }
    public static List<Equipment> available(Context c){unlockEligible(c);List<Equipment> out=new ArrayList<>();for(Equipment e:ITEMS)if(owns(c,e.id))out.add(e);return out;}
    public static String summary(Context c){unlockEligible(c);return "Playground equipment: "+ownedCount(c)+" / "+ITEMS.size()+" unlocked";}
}
