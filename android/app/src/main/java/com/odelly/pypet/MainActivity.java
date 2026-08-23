package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

/** Main Pypet screen with conservative sensory-safety presentation defaults. */
public class MainActivity extends Activity {
    TextView status, rewardStatus, petView, taskStatus, streakStatus, trophyStatus;
    EditText editor;
    Python py;
    RewardAdManager rewardAdManager;
    Spinner rewardSpinner;
    Button earnButton, buyButton;
    PypetAudio audio;
    PypetSafetyGuard safety;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));
        py = Python.getInstance(); safety = new PypetSafetyGuard(this); audio = new PypetAudio();
        MobileAds.initialize(this, initializationStatus -> {}); rewardAdManager = new RewardAdManager(this); rewardAdManager.preload();
        ScrollView scroll = new ScrollView(this); LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,28,28,28); root.setGravity(Gravity.CENTER_HORIZONTAL); scroll.addView(root);
        TextView title = new TextView(this); title.setText("🐾 Pypet Python Academy"); title.setTextSize(26); title.setTextColor(Color.DKGRAY); root.addView(title);
        status = new TextView(this); status.setText("Meet Pip. Learn Python by teaching Pip new tricks."); status.setTextSize(18); root.addView(status);
        petView = new TextView(this); petView.setText("🐶"); petView.setTextSize(72); petView.setGravity(Gravity.CENTER); root.addView(petView,new LinearLayout.LayoutParams(-1,150)); safety.suppressHaptics(petView); animatePet();
        LinearLayout petButtons = new LinearLayout(this); Button feed=button("Feed Pip"), play=button("Play"), learn=button("Learn"); petButtons.addView(feed);petButtons.addView(play);petButtons.addView(learn);root.addView(petButtons); safety.suppressHaptics(feed);safety.suppressHaptics(play);safety.suppressHaptics(learn);
        Button music=button("♫ Music: ON");root.addView(music);music.setOnClickListener(v->{audio.setEnabled(!audio.isEnabled());music.setText(audio.isEnabled()?"♫ Music: ON":"♫ Music: OFF");if(audio.isEnabled())audio.start();});safety.suppressHaptics(music);audio.start();
        Button motion=button(safety.reducedMotion()?"Animation: OFF":"Animation: ON");root.addView(motion);motion.setOnClickListener(v->{safety.setReducedMotion(!safety.reducedMotion());motion.setText(safety.reducedMotion()?"Animation: OFF":"Animation: ON");petView.clearAnimation();status.setText(safety.reducedMotion()?"Reduced-motion mode enabled.":"Gentle animation enabled.");if(!safety.reducedMotion())animatePet();});safety.suppressHaptics(motion);
        TextView safetyNote=new TextView(this);safetyNote.setText("Safety mode: no intentional flashing, strobing, screen shake, rapid animation, or reward haptics. Animation can be turned off.");root.addView(safetyNote);
        editor=new EditText(this);editor.setGravity(Gravity.TOP|Gravity.START);editor.setText("answer = 2 + 3\nprint(answer)");editor.setHint("Write Python here...");editor.setMinLines(8);root.addView(editor,new LinearLayout.LayoutParams(-1,0,1));Button run=button("Run Python Lesson");root.addView(run);safety.suppressHaptics(run);
        root.addView(sectionTitle("🏆 Trophies & Streak"));
        streakStatus=new TextView(this);trophyStatus=new TextView(this);root.addView(streakStatus);root.addView(trophyStatus);
        Button trophyButton=button("View Trophy Cabinet");root.addView(trophyButton);safety.suppressHaptics(trophyButton);trophyButton.setOnClickListener(v->showTrophies());
        root.addView(sectionTitle("✨ Treasure Trove"));TextView treasureInfo=new TextView(this);treasureInfo.setText("Optional cosmetic treasures for your Pypet world. Core lessons and pets remain available without purchase.");root.addView(treasureInfo);Button treasureStore=button("✨ Open Treasure Trove");root.addView(treasureStore);treasureStore.setOnClickListener(v->TreasureStore.show(this));safety.suppressHaptics(treasureStore);
        root.addView(sectionTitle("🪙 Pypet Coins & World Collection"));TextView coinExplanation=new TextView(this);coinExplanation.setText("Pypet Coins are original in-game currency. Earn them by caring for Pip, learning Python, completing activities, maintaining streaks, and optionally watching rewarded ads. Coins buy cosmetic world items; they never unlock lessons or essential care.");root.addView(coinExplanation);rewardStatus=new TextView(this);root.addView(rewardStatus);
        root.addView(sectionTitle("Today's Activities"));taskStatus=new TextView(this);taskStatus.setText("Complete each activity once to collect its coin reward.");root.addView(taskStatus);Button feedTask=button("Care for Pip • +10 coins"),learnTask=button("Complete a Python lesson • +25 coins"),playTask=button("Play with Pip • +10 coins");root.addView(feedTask);root.addView(learnTask);root.addView(playTask);safety.suppressHaptics(feedTask);safety.suppressHaptics(learnTask);safety.suppressHaptics(playTask);
        feedTask.setOnClickListener(v->completeTask("care_pip",10,"Care task complete!"));learnTask.setOnClickListener(v->completeTask("python_lesson",25,"Learning task complete!"));playTask.setOnClickListener(v->completeTask("play_pip",10,"Play task complete!"));
        root.addView(sectionTitle("🎁 Earnable World Rewards"));TextView rewardExplanation=new TextView(this);rewardExplanation.setText("Choose a cosmetic item and spend your earned Pypet Coins. Watching an ad is always optional.");root.addView(rewardExplanation);earnButton=button("Watch optional ad: +"+RewardAdManager.COINS_PER_REWARDED_AD+" Pypet Coins");root.addView(earnButton);safety.suppressHaptics(earnButton);
        List<String> rewardNames=new ArrayList<>();for(RewardCatalog.Item item:RewardCatalog.all())rewardNames.add(item.name+" — "+item.priceCoins+" Pypet Coins ["+item.tier+"]");rewardSpinner=new Spinner(this);rewardSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,rewardNames));root.addView(rewardSpinner);buyButton=button("Unlock selected reward");root.addView(buyButton);safety.suppressHaptics(buyButton);TextView policyNote=new TextView(this);policyNote.setText("Coins and earned cosmetics are local to this game profile and non-transferable. No task requires an advertisement or payment.");root.addView(policyNote);
        refreshRewardStatus();refreshAchievementStatus();
        feed.setOnClickListener(v->{status.setText("Pip is happily eating. +15 hunger");audio.petSound(330);completeTask("care_pip",10,"Pip is cared for! +10 coins");});play.setOnClickListener(v->{status.setText("Pip wants to play. +10 happiness");audio.petSound(392);completeTask("play_pip",10,"Play activity complete! +10 coins");});learn.setOnClickListener(v->{status.setText("Lesson progress recorded. Next: variables and expressions.");audio.petSound(523);completeTask("python_lesson",25,"Python lesson complete! +25 coins");});run.setOnClickListener(v->runCode());earnButton.setOnClickListener(v->earnCoins());buyButton.setOnClickListener(v->purchaseSelectedItem());setContentView(scroll);
    }

    private void completeTask(String id,int coins,String message){if(RewardInventory.completeTask(this,id,coins)){int streak=PypetAchievementManager.recordDailyActivity(this);PypetAchievementManager.awardTrophy(this,"first_step",10);if("python_lesson".equals(id))PypetAchievementManager.awardTrophy(this,"python_starter",25);if(PypetAchievementManager.awardStreakMilestone(this,streak))status.setText("🏆 Streak reward unlocked! Day "+streak+" • bonus coins earned.");else status.setText(message);taskStatus.setText(message+" • Day "+streak+" streak");refreshRewardStatus();refreshAchievementStatus();audio.petSound(523);animatePet();}else taskStatus.setText("That activity has already been completed for this cycle.");}
    private void animatePet(){if(petView==null||!safety.allowAnimation())return;ScaleAnimation bounce=new ScaleAnimation(.98f,1.02f,.98f,1.02f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,1f);bounce.setDuration(safety.safeAnimationDuration(750));bounce.setRepeatCount(1);bounce.setRepeatMode(Animation.REVERSE);petView.startAnimation(bounce);}
    private TextView sectionTitle(String text){TextView v=new TextView(this);v.setText(text);v.setTextSize(22);v.setTextColor(Color.DKGRAY);v.setPadding(0,24,0,8);return v;} private Button button(String text){Button b=new Button(this);b.setText(text);return b;}
    private void refreshAchievementStatus(){streakStatus.setText("🔥 Current streak: "+PypetAchievementManager.streak(this)+" days • Best: "+PypetAchievementManager.bestStreak(this));trophyStatus.setText("🏆 Trophies earned: "+PypetAchievementManager.trophyCount(this)+" / "+PypetAchievementManager.trophies().length);}
    private void showTrophies(){StringBuilder s=new StringBuilder();for(PypetAchievementManager.Trophy t:PypetAchievementManager.trophies())s.append(PypetAchievementManager.hasTrophy(this,t.id)?"🏆 ":"🔒 ").append(t.name).append(" — ").append(t.description).append(" (+").append(t.coins).append(" coins)\n\n");new AlertDialog.Builder(this).setTitle("🏆 Pypet Trophy Cabinet").setMessage(s.toString()).setPositiveButton("Close",null).show();}
    private void earnCoins(){earnButton.setEnabled(false);rewardAdManager.show(this,new RewardAdManager.RewardListener(){public void onCoinsGranted(int coins){refreshRewardStatus();status.setText("You earned "+coins+" Pypet Coins.");earnButton.setEnabled(true);}public void onAdUnavailable(String message){status.setText(message);earnButton.setEnabled(true);}});}
    private void purchaseSelectedItem(){RewardCatalog.Item item=RewardCatalog.all().get(rewardSpinner.getSelectedItemPosition());if(RewardInventory.owns(this,item.id)){status.setText("You already own "+item.name+".");return;}int balance=RewardInventory.coins(this);if(balance<item.priceCoins){new AlertDialog.Builder(this).setTitle("Not enough Pypet Coins").setMessage(item.name+" costs "+item.priceCoins+" coins. You have "+balance+".").setPositiveButton("Earn Coins",(d,w)->earnCoins()).setNegativeButton("Cancel",null).show();return;}new AlertDialog.Builder(this).setTitle("Unlock "+item.name+"?").setMessage(item.description+"\n\nPrice: "+item.priceCoins+" Pypet Coins.").setPositiveButton("Unlock",(d,w)->{if(RewardInventory.purchase(this,item.id)){try{py.getModule("world_api").callAttr("place_exclusive_item",item.id,"home");}catch(Exception ignored){}refreshRewardStatus();if(RewardInventory.count(this)>=5)PypetAchievementManager.awardTrophy(this,"world_builder",100);refreshAchievementStatus();status.setText(item.name+" has been added to your world.");}else status.setText("Purchase could not be completed.");}).setNegativeButton("No thanks",null).show();}
    private void refreshRewardStatus(){rewardStatus.setText("Pypet Coins: "+RewardInventory.coins(this)+" | World items: "+RewardInventory.count(this)+" / "+RewardCatalog.all().size());}
    private void runCode(){try{String result=py.getModule("pypet_engine").callAttr("run_lesson",editor.getText().toString()).toString();status.setText(result);audio.petSound(523);animatePet();}catch(Exception e){status.setText("Lesson error: "+e.getMessage());audio.petSound(196);}}
    @Override protected void onPause(){super.onPause();audio.stop();}@Override protected void onResume(){super.onResume();if(audio!=null&&audio.isEnabled())audio.start();}@Override protected void onDestroy(){audio.stop();super.onDestroy();}
}
