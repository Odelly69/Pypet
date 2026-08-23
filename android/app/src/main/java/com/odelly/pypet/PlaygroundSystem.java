package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

/**
 * Persistent, player-built playground inventory.
 *
 * Acquisition routes are intentionally distinct: progression, earned rewards,
 * optional purchases, and optional rewarded-ad rewards expose different item
 * families. No route is required for the core Python curriculum.
 */
public final class PlaygroundSystem {
    public enum Acquisition { PROGRESSION, EARNED, PURCHASE, REWARDED_AD }

    public static final class Equipment {
        public final String id, name, icon, description, animation;
        public final Acquisition acquisition;
        public final int unlockScore, fun, confidence, learning, price;

        Equipment(String i, String n, String ic, String d, Acquisition a,
                  int s, int f, int c, int l, int price, String anim) {
            id=i; name=n; icon=ic; description=d; acquisition=a;
            unlockScore=s; fun=f; confidence=c; learning=l; this.price=price; animation=anim;
        }
    }

    private static final String PREFS="pypet_playground";
    private static final String OWNED="owned";
    private static final String CURRENCY="currency";
    private static final String AD_CURRENCY="ad_currency";

    private static final List<Equipment> ITEMS=Collections.unmodifiableList(Arrays.asList(
        // Progression family: practical world-development equipment.
        new Equipment("bench","Picnic Bench","🪑","A calm social/rest space.",Acquisition.PROGRESSION,40,3,2,0,0,"sit_idle_social"),
        new Equipment("spring_rider","Spring Rider","🐴","Gentle rocking play.",Acquisition.PROGRESSION,45,7,3,0,0,"spring_rock_cycle"),
        new Equipment("swing_set","Swing Set","🎠","A classic hanging swing set.",Acquisition.PROGRESSION,50,10,4,0,0,"chain_swing_arc"),
        new Equipment("slide","Slide","🛝","Climb, sit, slide and land.",Acquisition.PROGRESSION,55,12,3,1,0,"climb_slide_land"),
        new Equipment("seesaw","Seesaw","⚖️","Cooperative balance play for two pets.",Acquisition.PROGRESSION,60,12,5,1,0,"seesaw_counterweight"),
        new Equipment("merry_go_round","Merry-Go-Round","🎡","A multi-pet rotating social ride.",Acquisition.PROGRESSION,65,15,6,1,0,"spinner_slow_rotation"),
        new Equipment("climber","Climbing Frame","🧗","A multi-route climbing structure.",Acquisition.PROGRESSION,70,16,6,3,0,"climb_route_pause"),
        new Equipment("monkey_bars","Overhead Bars","〰️","A coordination and persistence route.",Acquisition.PROGRESSION,75,17,7,4,0,"hand_over_hand_traverse"),
        new Equipment("play_tower","Play Tower","🏰","Connected climbing, bridge and slide sections.",Acquisition.PROGRESSION,80,20,8,5,0,"tower_climb_bridge_slide"),
        new Equipment("nature_play","Nature Play Area","🌳","Logs, stepping stones and discovery stations.",Acquisition.PROGRESSION,85,20,10,8,0,"balance_step_discover"),

        // Earned family: accomplishments create distinctive equipment.
        new Equipment("achievement_story_bench","Storytelling Bench","📖","A keepsake bench earned through learning milestones.",Acquisition.EARNED,0,6,8,6,0,"sit_story_gesture"),
        new Equipment("coding_climber","Logic Climber","💻","A coding-themed climbing puzzle earned through Python mastery.",Acquisition.EARNED,0,15,10,15,0,"climb_pattern_pause"),
        new Equipment("maker_spring","Maker Spring Rider","🔧","A builder-themed spring rider earned through projects.",Acquisition.EARNED,0,12,9,8,0,"spring_rock_mechanical"),
        new Equipment("explorer_balance","Explorer Balance Trail","🧭","A discovery trail earned through exploration.",Acquisition.EARNED,0,14,12,5,0,"balance_step_discover"),

        // Purchase family: optional premium/customizable equipment.
        new Equipment("garden_swing","Garden Canopy Swing","🌿","A customizable premium swing area.",Acquisition.PURCHASE,0,14,6,0,500,"canopy_swing_arc"),
        new Equipment("castle_slide","Castle Slide","🏰","A themed premium slide structure.",Acquisition.PURCHASE,0,18,7,1,750,"castle_climb_slide_land"),
        new Equipment("carousel_garden","Garden Carousel","🎠","A decorative multi-pet carousel.",Acquisition.PURCHASE,0,20,8,1,1000,"carousel_slow_rotation"),

        // Rewarded-ad family: exclusive novelty items; non-essential to education.
        new Equipment("pump_swing","Vintage Pump Swing","↔️","Old-school rigid-arm mechanical swing inspired by historic playground equipment.",Acquisition.REWARDED_AD,0,16,9,3,0,"rigid_arm_pump_swing"),
        new Equipment("star_spinner","Star Spinner","✦","An exclusive gentle spinner with a distinctive silhouette.",Acquisition.REWARDED_AD,0,17,9,3,0,"spinner_slow_rotation"),
        new Equipment("discovery_tunnel","Discovery Tunnel","◌","An exclusive crawl-through discovery feature.",Acquisition.REWARDED_AD,0,15,10,6,0,"crawl_through_discover")
    ));

    private PlaygroundSystem() {}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    private static Set<String> owned(Context c){return new HashSet<>(p(c).getStringSet(OWNED,Collections.emptySet()));}

    public static List<Equipment> all(){return ITEMS;}
    public static boolean owns(Context c,String id){return owned(c).contains(id);}
    public static int ownedCount(Context c){return owned(c).size();}
    public static Equipment find(String id){for(Equipment e:ITEMS)if(e.id.equals(id))return e;return null;}

    /** Progression items are unlocked by the existing balanced-development path. */
    public static void unlockEligible(Context c){
        int score=PetEvolutionManager.balancedDevelopmentScore(c); Set<String> set=owned(c);
        for(Equipment e:ITEMS)
            if(e.acquisition==Acquisition.PROGRESSION && score>=e.unlockScore) set.add(e.id);
        p(c).edit().putStringSet(OWNED,set).apply();
    }

    /** Earn an item explicitly through achievements/projects/exploration. */
    public static boolean earn(Context c,String id){
        Equipment e=find(id); if(e==null || e.acquisition!=Acquisition.EARNED)return false;
        Set<String> set=owned(c); set.add(id); p(c).edit().putStringSet(OWNED,set).apply(); return true;
    }

    /** Purchase only the items belonging to the purchase family. */
    public static boolean buy(Context c,String id){
        Equipment e=find(id); if(e==null || e.acquisition!=Acquisition.PURCHASE || e.price<=0)return false;
        int balance=p(c).getInt(CURRENCY,0); if(balance<e.price)return false;
        Set<String> set=owned(c); set.add(id);
        p(c).edit().putInt(CURRENCY,balance-e.price).putStringSet(OWNED,set).apply(); return true;
    }

    /** Rewarded-ad grants use the separate AdMob-earned in-game currency. */
    public static boolean redeemRewarded(Context c,String id,int cost){
        Equipment e=find(id); if(e==null || e.acquisition!=Acquisition.REWARDED_AD || cost<0)return false;
        int balance=p(c).getInt(AD_CURRENCY,0); if(balance<cost)return false;
        Set<String> set=owned(c); set.add(id);
        p(c).edit().putInt(AD_CURRENCY,balance-cost).putStringSet(OWNED,set).apply(); return true;
    }

    public static void addCurrency(Context c,int amount){if(amount>0)p(c).edit().putInt(CURRENCY,p(c).getInt(CURRENCY,0)+amount).apply();}
    public static void addAdCurrency(Context c,int amount){if(amount>0)p(c).edit().putInt(AD_CURRENCY,p(c).getInt(AD_CURRENCY,0)+amount).apply();}
    public static int currency(Context c){return p(c).getInt(CURRENCY,0);}
    public static int adCurrency(Context c){return p(c).getInt(AD_CURRENCY,0);}

    public static List<Equipment> available(Context c){unlockEligible(c);List<Equipment> out=new ArrayList<>();for(Equipment e:ITEMS)if(owns(c,e.id))out.add(e);return out;}
    public static List<Equipment> catalog(Acquisition a){List<Equipment> out=new ArrayList<>();for(Equipment e:ITEMS)if(e.acquisition==a)out.add(e);return out;}
    public static String summary(Context c){unlockEligible(c);return "Playground pieces: "+ownedCount(c)+" / "+ITEMS.size()+" • currency "+currency(c)+" • rewarded currency "+adCurrency(c);}
}
