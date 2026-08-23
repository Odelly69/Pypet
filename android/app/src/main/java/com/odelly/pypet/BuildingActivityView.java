package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import java.util.List;

/** Real in-world activities. No building-description dialogs: each building performs a gameplay action. */
public final class BuildingActivityView {
    private BuildingActivityView() {}

    private static LinearLayout root(Activity a,String title,String subtitle){
        LinearLayout r=new LinearLayout(a); r.setOrientation(LinearLayout.VERTICAL); r.setPadding(28,24,28,24);
        ScrollView scroll=new ScrollView(a); scroll.addView(r);
        TextView h=new TextView(a); h.setText(title); h.setTextSize(27); h.setTextColor(Color.DKGRAY); h.setGravity(Gravity.CENTER); r.addView(h);
        TextView s=new TextView(a); s.setText(subtitle); s.setTextSize(15); s.setGravity(Gravity.CENTER); s.setPadding(0,8,0,20); r.addView(s);
        a.setContentView(scroll); return r;
    }
    private static Button button(Activity a,LinearLayout r,String text){Button b=new Button(a);b.setText(text);b.setAllCaps(false);r.addView(b,new LinearLayout.LayoutParams(-1,-2));return b;}
    private static void back(Activity a,LinearLayout r){Button b=button(a,r,"🌎 Return to PyPet World");b.setOnClickListener(v->LivingWorldView.show(a));}
    private static void activity(Activity a,String id,int coins){
        int streak=PypetAchievementManager.recordDailyActivity(a); RewardInventory.completeTask(a,"activity_"+id,coins);
        PypetAchievementManager.awardTrophy(a,"first_step",10); PypetAchievementManager.awardStreakMilestone(a,streak);
    }
    private static void status(Activity a,TextView out){out.setText("❤️ Health "+PetEvolutionManager.health(a)+"   🍎 Hunger "+PetEvolutionManager.hunger(a)+"   😊 Happiness "+PetEvolutionManager.happiness(a)+"\n🧼 Hygiene "+PetCareSystem.hygiene(a)+"   🪙 "+RewardInventory.coins(a)+" Pypet Coins");}

    public static void home(Activity a){
        PetCareSystem.tick(a); activity(a,"home",5);
        LinearLayout r=root(a,"🏠 HOME","Care for your pet and keep its everyday needs balanced.");
        TextView stats=new TextView(a);stats.setTextSize(17);r.addView(stats);status(a,stats);
        for(PetEvolutionManager.PetFood f:PetEvolutionManager.foods()){Button b=button(a,r,f.emoji+" Feed "+f.name);b.setOnClickListener(v->{PetEvolutionManager.feed(a,f);activity(a,"feed_"+f.id,2);status(a,stats);Toast.makeText(a,"Your pet enjoyed "+f.name+".",Toast.LENGTH_SHORT).show();});}
        Button bath=button(a,r,"🛁 Bathe pet");bath.setOnClickListener(v->{PetCareSystem.bathe(a);activity(a,"bath",3);status(a,stats);});
        Button clean=button(a,r,"🧹 Clean up waste");clean.setOnClickListener(v->{PetCareSystem.cleanWaste(a);activity(a,"clean",3);status(a,stats);});
        Button care=button(a,r,"❤️ Complete care routine");care.setOnClickListener(v->{PetEvolutionManager.performCare(a);PetEvolutionManager.performRoutine(a);activity(a,"care",5);if(PetEvolutionManager.care(a)>=5)PypetAchievementManager.awardTrophy(a,"caring_friend",50);status(a,stats);});
        back(a,r);
    }

    public static void market(Activity a){
        activity(a,"market",5); LinearLayout r=root(a,"🛒 MARKET","Spend earned Pypet Coins on real cosmetic inventory. Purchases persist and can be displayed in your World.");
        TextView wallet=new TextView(a);wallet.setTextSize(18);r.addView(wallet); Runnable refresh=()->wallet.setText("🪙 Pypet Coins: "+RewardInventory.coins(a)+"   🧺 Owned cosmetics: "+RewardInventory.count(a)); refresh.run();
        List<RewardCatalog.Item> items=RewardCatalog.all();
        for(RewardCatalog.Item item:items){ if(item.tier.equals("COIN")||item.tier.equals("AD_COIN")){ final RewardCatalog.Item x=item; Button b=button(a,r,x.name+" • "+x.priceCoins+" coins\n"+x.description); if(RewardInventory.owns(a,x.id))b.setEnabled(false); b.setOnClickListener(v->{if(RewardInventory.purchase(a,x.id)){WorldPlacementManager.place(a,x.id,-1100+(RewardInventory.count(a)%7)*300,1350+(RewardInventory.count(a)/7)*130,1f,0f);activity(a,"purchase_"+x.id,1);refresh.run();b.setEnabled(false);Toast.makeText(a,"Purchased and added to your World inventory.",Toast.LENGTH_SHORT).show();}else Toast.makeText(a,"Earn more Pypet Coins to unlock this item.",Toast.LENGTH_SHORT).show();});}}
        back(a,r);
    }

    public static void workshop(Activity a){
        activity(a,"workshop",5); LinearLayout r=root(a,"🔧 WORKSHOP","Build, place and show off what you have accomplished. Every object is persisted in the World.");
        TextView placed=new TextView(a);placed.setTextSize(16);r.addView(placed);Runnable refresh=()->placed.setText("🏘 Town decorations: "+WorldPlacementManager.all(a).size()+"\n🏆 Trophies earned: "+PypetAchievementManager.trophyCount(a));refresh.run();
        String[] ids={"bench","flower_bed","lamp","tree","trophy_stand","garden_sign"};String[] labels={"🪑 Build bench","🌷 Plant flower bed","💡 Build street lamp","🌳 Plant tree","🏆 Build trophy stand","🪧 Build garden sign"};
        for(int i=0;i<ids.length;i++){final String id=ids[i];final int n=i;Button b=button(a,r,labels[i]);b.setOnClickListener(v->{WorldPlacementManager.place(a,id,-900+n*260,1150,1f,0f);activity(a,"build_"+id,2);if(WorldPlacementManager.all(a).size()>=5)PypetAchievementManager.awardTrophy(a,"world_builder",100);refresh.run();Toast.makeText(a,"Built and placed in your town.",Toast.LENGTH_SHORT).show();});}
        for(PypetAchievementManager.Trophy t:PypetAchievementManager.trophies()){if(PypetAchievementManager.hasTrophy(a,t.id)){final PypetAchievementManager.Trophy trophy=t;Button b=button(a,r,"🏆 Display "+trophy.name);b.setOnClickListener(v->{WorldPlacementManager.setTrophyDisplay(a,trophy.id,true);WorldPlacementManager.place(a,"trophy_"+trophy.id,-700+(PypetAchievementManager.trophyCount(a)%6)*260,1500,1f,0f);refresh.run();});}}
        back(a,r);
    }

    public static void park(Activity a){
        PetCareSystem.tick(a);activity(a,"park",5);LinearLayout r=root(a,"🌳 PARK","Play, explore and take a healthy break with your pet. These actions affect real pet wellbeing and progress.");
        TextView score=new TextView(a);score.setTextSize(17);score.setGravity(Gravity.CENTER);r.addView(score);Runnable refresh=()->score.setText("😊 Happiness "+PetEvolutionManager.happiness(a)+"\n🎾 Play sessions "+PetEvolutionManager.play(a)+"   🗺 Explorations "+PetEvolutionManager.explore(a)+"\n🪙 "+RewardInventory.coins(a)+" Pypet Coins");refresh.run();
        Button play=button(a,r,"🎾 Play in the playground");play.setOnClickListener(v->{PetEvolutionManager.playWith(a);activity(a,"play",4);refresh.run();});
        Button explore=button(a,r,"🦋 Explore the park");explore.setOnClickListener(v->{PetEvolutionManager.performExplore(a);activity(a,"explore",4);refresh.run();});
        Button breakBtn=button(a,r,"🌤 Take a healthy break");breakBtn.setOnClickListener(v->{PetEvolutionManager.performRoutine(a);PetCareSystem.tick(a);activity(a,"healthy_break",4);refresh.run();Toast.makeText(a,"Healthy break completed.",Toast.LENGTH_SHORT).show();});
        back(a,r);
    }

    public static void library(Activity a){
        activity(a,"library",5);LinearLayout r=root(a,"📚 LIBRARY","Study and review Python and job-readiness knowledge through actual questions and hands-on practice.");
        TextView progress=new TextView(a);progress.setTextSize(17);r.addView(progress);Runnable refresh=()->progress.setText("Lessons mastered: "+PetEvolutionManager.lessons(a)+"\nSchool sessions: "+PetEvolutionManager.school(a)+"\n🪙 Coins: "+RewardInventory.coins(a));refresh.run();
        TextView question=new TextView(a);question.setTextSize(19);question.setPadding(0,25,0,15);question.setText("What does len([1, 2, 3]) return?");r.addView(question);
        String[] choices={"2","3","4","It prints the list"};for(String c:choices){Button b=button(a,r,c);b.setOnClickListener(v->{if(((Button)v).getText().toString().equals("3")){PetEvolutionManager.completeLesson(a);RewardInventory.completeTask(a,"library_len_lists",10);PypetAchievementManager.awardTrophy(a,"python_starter",25);refresh.run();Toast.makeText(a,"Correct — you demonstrated a Python concept.",Toast.LENGTH_SHORT).show();}else Toast.makeText(a,"Not quite. Review lists and len(), then try again.",Toast.LENGTH_SHORT).show();});}
        Button next=button(a,r,"🐍 Open hands-on Python Academy");next.setOnClickListener(v->PypetAcademyActivityView.show(a));back(a,r);
    }
}
