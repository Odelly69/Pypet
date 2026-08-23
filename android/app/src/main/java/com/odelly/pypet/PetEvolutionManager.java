package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Persistent virtual-pet profile: random name, traditional care, lessons and multi-path evolution. */
public final class PetEvolutionManager {
    public static final class PetVariant { public final String id,species,displayName,emoji,description; public final int level; PetVariant(String i,String s,String n,String e,String d,int l){id=i;species=s;displayName=n;emoji=e;description=d;level=l;} }
    public static final class PetFood { public final String id,name,emoji; public final int hunger,happiness,health; PetFood(String i,String n,String e,int h,int a,int l){id=i;name=n;emoji=e;hunger=h;happiness=a;health=l;} }
    private static final String PREFS="pypet_pet",NAME="name",HUNGER="hunger",HAPPY="happiness",HEALTH="health",LESSONS="lessons",PLAY="play",CARE="care",CREATED="created";
    private static final Random RANDOM=new Random();
    private static final String[] NAMES={"Pip","Mochi","Noodle","Biscuit","Pebble","Sunny","Clover","Pixel","Waffles","Sprout","Toby","Mango"};
    private static final List<PetFood> FOODS=Collections.unmodifiableList(Arrays.asList(new PetFood("berry","Berry Bites","🍓",8,4,2),new PetFood("apple","Apple Snack","🍎",12,2,3),new PetFood("carrot","Crunchy Carrot","🥕",10,3,5),new PetFood("cake","Celebration Treat","🍰",5,12,1),new PetFood("fish","Happy Fish","🐟",15,8,2)));
    private PetEvolutionManager(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    private static void init(Context c){if(p(c).getBoolean(CREATED,false))return;p(c).edit().putBoolean(CREATED,true).putString(NAME,NAMES[RANDOM.nextInt(NAMES.length)]).putInt(HUNGER,75).putInt(HAPPY,75).putInt(HEALTH,90).apply();}
    public static String name(Context c){init(c);return p(c).getString(NAME,"Pip");} public static List<PetFood> foods(){return FOODS;}
    public static int hunger(Context c){init(c);return p(c).getInt(HUNGER,75);} public static int happiness(Context c){init(c);return p(c).getInt(HAPPY,75);} public static int health(Context c){init(c);return p(c).getInt(HEALTH,90);} public static int lessons(Context c){init(c);return p(c).getInt(LESSONS,0);} public static int play(Context c){init(c);return p(c).getInt(PLAY,0);} public static int care(Context c){init(c);return p(c).getInt(CARE,0);}
    private static int clamp(int x){return Math.max(0,Math.min(100,x));}
    private static void stats(Context c,int dh,int da,int dl){init(c);p(c).edit().putInt(HUNGER,clamp(hunger(c)+dh)).putInt(HAPPY,clamp(happiness(c)+da)).putInt(HEALTH,clamp(health(c)+dl)).apply();}
    public static void feed(Context c,PetFood f){stats(c,f.hunger,f.happiness,f.health);p(c).edit().putInt(CARE,care(c)+1).apply();}
    public static void playWith(Context c){stats(c,-2,10,0);p(c).edit().putInt(PLAY,play(c)+1).apply();}
    public static void care(Context c){stats(c,0,3,10);p(c).edit().putInt(CARE,care(c)+1).apply();}
    public static void completeLesson(Context c){stats(c,0,5,2);p(c).edit().putInt(LESSONS,lessons(c)+1).apply();}
    /** Evolution is shaped by care, hunger/health balance, happiness, play and lessons. */
    public static PetVariant current(Context c){init(c);int total=lessons(c)+play(c)+care(c),avg=(hunger(c)+happiness(c)+health(c))/3;String id;if(total<5)id="hatchling";else if(avg>=85&&lessons(c)>=12&&care(c)>=12)id="scholar_unicorn";else if(avg>=85&&play(c)>=12)id="joy_sprite";else if(health(c)>=85&&care(c)>=12)id="guardian_fox";else if(lessons(c)>=8)id="code_dragon";else if(play(c)>=8)id="playful_cat";else id="young_pup";return variant(id);}
    private static PetVariant variant(String id){switch(id){case "scholar_unicorn":return new PetVariant(id,"Unicorn Scholar","Starlight Scholar","🦄","Balanced care and deep learning shaped this companion.",6);case "joy_sprite":return new PetVariant(id,"Joy Sprite","Giggle Sprite","🧚","Play and happiness shaped this cheerful variant.",5);case "guardian_fox":return new PetVariant(id,"Guardian Fox","Clover Guardian","🦊","Strong health and consistent care shaped this guardian.",5);case "code_dragon":return new PetVariant(id,"Code Dragon","Byte Dragon","🐉","Python lessons shaped this curious dragon.",5);case "playful_cat":return new PetVariant(id,"Playful Cat","Pixel Pouncer","🐱","Games and interaction shaped this playful variant.",4);case "young_pup":return new PetVariant(id,"Young Pet","Bright Buddy","🐶","A growing companion learning your routines.",3);default:return new PetVariant("hatchling","Hatchling","Little Sprout","🐣","Your new companion is beginning its adventure.",1);}}
    public static List<PetVariant> worldPets(Context c){return Arrays.asList(current(c),variant("playful_cat"),variant("guardian_fox"),variant("code_dragon"),variant("joy_sprite"),variant("scholar_unicorn"));}
}
