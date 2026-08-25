package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.*;

/** Core PyPet evolution rules: every pet begins as an egg and evolves only within that egg's lineage. */
public final class PetEvolutionManager {
    public static final class PetVariant { public final String id,species,displayName,emoji,description; public final int level; PetVariant(String i,String s,String n,String e,String d,int l){id=i;species=s;displayName=n;emoji=e;description=d;level=l;} }
    public static final class PetFood { public final String id,name,emoji; public final int hunger,happiness,health; PetFood(String i,String n,String e,int h,int a,int l){id=i;name=n;emoji=e;hunger=h;happiness=a;health=l;} }
    public static final class Egg { public final String id,lineage,emoji,pattern,rarity; Egg(String i,String l,String e,String p,String r){id=i;lineage=l;emoji=e;pattern=p;rarity=r;} }
    private static final String PREFS="pypet_pet";
    private static final String CREATED="created",HATCHED="hatched",NAME="name",HUNGER="hunger",HAPPY="happiness",HEALTH="health",LESSONS="lessons",PLAY="play",CARE="care",SCHOOL="school",EXPLORE="explore",ROUTINE="routine",LINEAGE="lineage",STAGE="stage",RARITY="rarity",EGG_ID="egg_id",EGG_PATTERN="egg_pattern",EVOLUTIONS="evolutions",EGGS="eggs",COLLECTION="collection";
    private static final Random RANDOM=new Random();
    private static final String[] NAMES={"Pip","Mochi","Noodle","Biscuit","Pebble","Sunny","Clover","Pixel","Waffles","Sprout","Toby","Mango","Poppy","Comet","Juniper","Maple"};
    private static final String[][] LINEAGES={{"fox","Fox","🦊"},{"unicorn","Unicorn","🦄"},{"dragon","Dragon","🐉"},{"cat","Cat","🐱"},{"wolf","Wolf","🐺"},{"bunny","Bunny","🐰"},{"turtle","Turtle","🐢"},{"deer","Deer","🦌"},{"bear","Bear","🐻"},{"frog","Frog","🐸"},{"bird","Bird","🐦"},{"fae","Fae","🧚"},{"spirit","Nature Spirit","🌿"}};
    private static final List<PetFood> FOODS=Collections.unmodifiableList(Arrays.asList(new PetFood("berry","Berry Bites","🍓",8,4,2),new PetFood("apple","Apple Snack","🍎",12,2,3),new PetFood("carrot","Crunchy Carrot","🥕",10,3,5),new PetFood("cake","Celebration Treat","🍰",5,12,1),new PetFood("fish","Happy Fish","🐟",15,8,2)));
    private PetEvolutionManager(){}
    private static SharedPreferences p(Context c){return c.getSharedPreferences(PREFS,Context.MODE_PRIVATE);}
    private static void init(Context c){if(p(c).getBoolean(CREATED,false))return;Egg e=newEgg();p(c).edit().putBoolean(CREATED,true).putBoolean(HATCHED,false).putString(NAME,NAMES[RANDOM.nextInt(NAMES.length)]).putInt(HUNGER,75).putInt(HAPPY,75).putInt(HEALTH,90).putString(LINEAGE,e.lineage).putInt(STAGE,0).putString(RARITY,e.rarity).putString(EGG_ID,e.id).putString(EGG_PATTERN,e.pattern).putInt(LESSONS,0).putInt(PLAY,0).putInt(CARE,0).putInt(SCHOOL,0).putInt(EXPLORE,0).putInt(ROUTINE,0).putInt(EVOLUTIONS,0).putInt(EGGS,1).putString(COLLECTION,"egg:"+e.id).apply();}
    public static String name(Context c){init(c);return p(c).getString(NAME,"Pip");} public static List<PetFood> foods(){return FOODS;}
    public static int hunger(Context c){init(c);return p(c).getInt(HUNGER,75);} public static int happiness(Context c){init(c);return p(c).getInt(HAPPY,75);} public static int health(Context c){init(c);return p(c).getInt(HEALTH,90);} public static int lessons(Context c){init(c);return p(c).getInt(LESSONS,0);} public static int play(Context c){init(c);return p(c).getInt(PLAY,0);} public static int care(Context c){init(c);return p(c).getInt(CARE,0);} public static int school(Context c){init(c);return p(c).getInt(SCHOOL,0);} public static int explore(Context c){init(c);return p(c).getInt(EXPLORE,0);} public static int routine(Context c){init(c);return p(c).getInt(ROUTINE,0);} public static int evolutionCount(Context c){init(c);return p(c).getInt(EVOLUTIONS,0);} public static int eggCount(Context c){init(c);return p(c).getInt(EGGS,1);} public static boolean isHatched(Context c){init(c);return p(c).getBoolean(HATCHED,false);}
    public static int collectionCount(Context c){init(c);String s=p(c).getString(COLLECTION,"");return s.isEmpty()?0:s.split("\\|").length;} public static String collection(Context c){init(c);return p(c).getString(COLLECTION,"");} private static void remember(Context c,String item){String old=collection(c);p(c).edit().putString(COLLECTION,old.isEmpty()?item:old+"|"+item).apply();}
    public static Egg currentEgg(Context c){init(c);String l=p(c).getString(LINEAGE,"fox");return new Egg(p(c).getString(EGG_ID,"egg-1"),l,lineageEmoji(l),p(c).getString(EGG_PATTERN,"ember"),p(c).getString(RARITY,"common"));}
    public static Egg newEgg(){String[] l=LINEAGES[RANDOM.nextInt(LINEAGES.length)];String id="egg-"+System.currentTimeMillis()+"-"+(1000+RANDOM.nextInt(9000));String[] patterns={"speckled","swirled","starred","leaf-marked","crystal","striped","pearl","clouded"};String pattern=patterns[RANDOM.nextInt(patterns.length)];int roll=RANDOM.nextInt(1000);String rarity=roll<5?"mythic":roll<35?"rare":roll<150?"uncommon":"common";return new Egg(id,l[0],l[2],pattern,rarity);}
    /** Hatch the exact egg currently shown by the Hatchery. The egg's lineage becomes the pet's permanent lineage. */
    public static Egg hatchCurrentEgg(Context c){
        init(c);
        if(isHatched(c))return currentEgg(c);
        Egg e=currentEgg(c);
        p(c).edit().putBoolean(HATCHED,true).putString(NAME,NAMES[RANDOM.nextInt(NAMES.length)])
            .putInt(HUNGER,75).putInt(HAPPY,75).putInt(HEALTH,90).putInt(LESSONS,0).putInt(PLAY,0)
            .putInt(CARE,0).putInt(SCHOOL,0).putInt(EXPLORE,0).putInt(ROUTINE,0).putInt(STAGE,0)
            .putInt(EVOLUTIONS,0).apply();
        remember(c,"pet:"+e.id+":"+name(c));
        return e;
    }
    /** Start a fresh egg journey after the previous pet is retired/collected. The new egg is NOT silently hatched. */
    public static Egg hatchNewEgg(Context c){
        init(c);
        if(!isHatched(c))return currentEgg(c);
        PetVariant old=current(c);remember(c,"pet:"+old.id+":"+name(c));
        Egg e=newEgg();
        p(c).edit().putBoolean(HATCHED,false).putString(NAME,"Eggling").putInt(HUNGER,75).putInt(HAPPY,75).putInt(HEALTH,90)
            .putInt(LESSONS,0).putInt(PLAY,0).putInt(CARE,0).putInt(SCHOOL,0).putInt(EXPLORE,0).putInt(ROUTINE,0)
            .putString(LINEAGE,e.lineage).putInt(STAGE,0).putString(RARITY,e.rarity).putString(EGG_ID,e.id).putString(EGG_PATTERN,e.pattern)
            .putInt(EVOLUTIONS,0).putInt(EGGS,eggCount(c)+1).apply();
        remember(c,"egg:"+e.id);return e;
    }
    private static int clamp(int x){return Math.max(0,Math.min(100,x));} private static void stats(Context c,int dh,int da,int dl){init(c);p(c).edit().putInt(HUNGER,clamp(hunger(c)+dh)).putInt(HAPPY,clamp(happiness(c)+da)).putInt(HEALTH,clamp(health(c)+dl)).apply();}
    public static void feed(Context c,PetFood f){stats(c,f.hunger,f.happiness,f.health);p(c).edit().putInt(CARE,care(c)+1).apply();} public static void playWith(Context c){stats(c,-2,10,0);p(c).edit().putInt(PLAY,play(c)+1).apply();} public static void performCare(Context c){stats(c,0,3,10);p(c).edit().putInt(CARE,care(c)+1).apply();}
    public static void completeLesson(Context c){stats(c,0,5,2);p(c).edit().putInt(LESSONS,lessons(c)+1).putInt(SCHOOL,school(c)+1).apply();} public static void attendSchool(Context c){stats(c,0,3,0);p(c).edit().putInt(SCHOOL,school(c)+1).apply();} public static void performExplore(Context c){p(c).edit().putInt(EXPLORE,explore(c)+1).putInt(HAPPY,clamp(happiness(c)+4)).apply();} public static void performRoutine(Context c){p(c).edit().putInt(ROUTINE,routine(c)+1).apply();}
    /** All development categories have equal weight; an unhatched egg cannot evolve. */
    public static int balancedDevelopmentScore(Context c){int[] x={hunger(c),happiness(c),health(c),normalized(lessons(c)),normalized(play(c)),normalized(care(c)),normalized(school(c)),normalized(explore(c)),normalized(routine(c))};int sum=0;for(int v:x)sum+=v;return sum/x.length;} private static int normalized(int n){return Math.min(100,n*8);}
    public static PetVariant current(Context c){init(c);if(!isHatched(c))return new PetVariant("egg-"+currentEgg(c).id,currentEgg(c).lineage,"Unhatched "+capitalize(currentEgg(c).lineage)+" Egg",currentEgg(c).emoji,"Your pet is still inside this egg. Hatch it to begin the same-lineage journey.",0);int score=balancedDevelopmentScore(c),stage=p(c).getInt(STAGE,0);if(stage==0&&score>=55)evolve(c,1);else if(stage==1&&score>=72)evolve(c,2);else if(stage==2&&score>=86)evolve(c,3);return variant(p(c).getString(LINEAGE,"fox"),p(c).getInt(STAGE,0),p(c).getString(RARITY,"common"));}
    private static void evolve(Context c,int next){int roll=RANDOM.nextInt(1000);String rarity=roll<5?"mythic":roll<35?"rare":roll<150?"uncommon":"common";p(c).edit().putInt(STAGE,next).putString(RARITY,rarity).putInt(EVOLUTIONS,evolutionCount(c)+1).apply();PetVariant v=variant(p(c).getString(LINEAGE,"fox"),next,rarity);remember(c,"pet:"+v.id+":"+name(c));}
    private static PetVariant variant(String lineage,int stage,String rarity){String n=capitalize(lineage),form=formName(lineage,stage),rare=rarity.equals("mythic")?"Mythic ":rarity.equals("rare")?"Rare ":rarity.equals("uncommon")?"Uncommon ":"";return new PetVariant(lineage+"-"+stage+"-"+rarity,n,rare+form,lineageEmoji(lineage),"A "+n+" lineage form shaped by balanced care, play, school, learning and exploration. Rarity: "+rarity,stage+1);}
    private static String formName(String l,int s){String[] f;switch(l){case "fox":f=new String[]{"Fox Kit","Ember Fox","Moon Fox","Astral Fox"};break;case "unicorn":f=new String[]{"Unicorn Foal","Meadow Unicorn","Rainbow Unicorn","Celestial Unicorn"};break;case "dragon":f=new String[]{"Dragon Whelp","Young Dragon","Code Dragon","Star Dragon"};break;case "cat":f=new String[]{"Kitten","Playful Cat","Pixel Cat","Astral Cat"};break;case "wolf":f=new String[]{"Wolf Pup","Frost Wolf","Moon Wolf","Aurora Wolf"};break;case "bunny":f=new String[]{"Bunny Kit","Garden Bunny","Rainbow Bunny","Moon Garden Bunny"};break;case "turtle":f=new String[]{"Turtle Hatchling","Moss Turtle","Garden Guardian","Ancient Shell Spirit"};break;case "deer":f=new String[]{"Fawn","Grove Deer","Forest Guardian","Celestial Stag"};break;case "bear":f=new String[]{"Bear Cub","Honey Bear","Forest Bear","Star Bear"};break;case "frog":f=new String[]{"Tadpole","Pond Frog","Garden Frog","Moonlight Frog"};break;case "bird":f=new String[]{"Chick","Songbird","Sky Bird","Celestial Phoenix"};break;case "fae":f=new String[]{"Fae Sprout","Giggle Sprite","Dream Sprite","Astral Fae"};break;default:f=new String[]{"Spirit Sprout","Grove Spirit","Nature Guardian","Ancient Nature Spirit"};}return f[Math.min(s,f.length-1)];}
    private static String lineageEmoji(String id){for(String[] x:LINEAGES)if(x[0].equals(id))return x[2];return "🐾";} private static String capitalize(String s){return s.length()==0?s:s.substring(0,1).toUpperCase()+s.substring(1);}
    public static List<PetVariant> worldPets(Context c){return Arrays.asList(current(c),new PetVariant("world-unicorn","Unicorn","Moon Meadow Unicorn","🦄","World resident",2),new PetVariant("world-fox","Fox","Ember Fox","🦊","World resident",2),new PetVariant("world-dragon","Dragon","Byte Dragon","🐉","World resident",2),new PetVariant("world-fae","Fae","Giggle Sprite","🧚","World resident",2));}
}
