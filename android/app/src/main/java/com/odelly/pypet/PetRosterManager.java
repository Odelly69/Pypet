package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

/** Persistent pet roster. Each pet owns its own needs/development; world interactions are separate. */
public final class PetRosterManager {
    public static final class Resident {
        public final String id,name,form,emoji; public final int generation,health,hunger,happiness,lessons,play,care,school,explore,routine;
        Resident(String i,String n,String f,String e,int g,int h,int hu,int ha,int l,int p,int c,int s,int x,int r){id=i;name=n;form=f;emoji=e;generation=g;health=h;hunger=hu;happiness=ha;lessons=l;play=p;care=c;school=s;explore=x;routine=r;}
    }
    private static final String PREFS="pypet_roster";
    private static final String IDS="ids",ACTIVE="active",SEP="|";
    private PetRosterManager(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    private static List<String> ids(Context c){String s=p(c).getString(IDS,"");return s.isEmpty()?new ArrayList<>():new ArrayList<>(Arrays.asList(s.split("\\|")));}
    public static void ensurePrimary(Context c){if(!ids(c).isEmpty())return;String id="pet-primary";p(c).edit().putString(IDS,id).putString(ACTIVE,id).apply();save(c,id,new Resident(id,PetEvolutionManager.name(c),PetEvolutionManager.current(c).displayName,PetEvolutionManager.current(c).emoji,1,PetEvolutionManager.health(c),PetEvolutionManager.hunger(c),PetEvolutionManager.happiness(c),PetEvolutionManager.lessons(c),PetEvolutionManager.play(c),PetEvolutionManager.care(c),PetEvolutionManager.school(c),PetEvolutionManager.explore(c),PetEvolutionManager.routine(c)));}
    public static void save(Context c,String id,Resident r){p(c).edit().putString("n."+id,r.name).putString("f."+id,r.form).putString("e."+id,r.emoji).putInt("g."+id,r.generation).putInt("h."+id,r.health).putInt("hu."+id,r.hunger).putInt("ha."+id,r.happiness).putInt("l."+id,r.lessons).putInt("pl."+id,r.play).putInt("ca."+id,r.care).putInt("sc."+id,r.school).putInt("ex."+id,r.explore).putInt("ro."+id,r.routine).apply();}
    public static List<Resident> residents(Context c){ensurePrimary(c);List<Resident> out=new ArrayList<>();for(String id:ids(c))out.add(read(c,id));return out;}
    private static Resident read(Context c,String id){return new Resident(id,p(c).getString("n."+id,"Pypet"),p(c).getString("f."+id,"Pet"),p(c).getString("e."+id,"🐾"),p(c).getInt("g."+id,1),p(c).getInt("h."+id,90),p(c).getInt("hu."+id,75),p(c).getInt("ha."+id,75),p(c).getInt("l."+id,0),p(c).getInt("pl."+id,0),p(c).getInt("ca."+id,0),p(c).getInt("sc."+id,0),p(c).getInt("ex."+id,0),p(c).getInt("ro."+id,0));}
    public static String activeId(Context c){ensurePrimary(c);return p(c).getString(ACTIVE,"pet-primary");}
    public static Resident active(Context c){ensurePrimary(c);return read(c,activeId(c));}
    public static void setActive(Context c,String id){ensurePrimary(c);if(ids(c).contains(id))p(c).edit().putString(ACTIVE,id).apply();}
    /** Grown/evolved pets remain residents; this creates the next egg opportunity without replacing the resident. */
    public static String grantNextEgg(Context c){ensurePrimary(c);String egg=PetEvolutionManager.newEgg().id;String key="eggQueue";String old=p(c).getString(key,"");p(c).edit().putString(key,old.isEmpty()?egg:old+SEP+egg).apply();return egg;}
    public static int pendingEggs(Context c){String s=p(c).getString("eggQueue","");return s.isEmpty()?0:s.split("\\|").length;}
    /** World interactions are intentionally not routed through these per-pet stats. */
    public static String interactionRule(){return "World interactions are shared; care/needs/development remain scoped to the active pet.";}
}
