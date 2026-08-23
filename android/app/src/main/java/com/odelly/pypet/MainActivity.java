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

public class MainActivity extends Activity {
    TextView status, rewardStatus, petView;
    EditText editor;
    Python py;
    RewardAdManager rewardAdManager;
    Spinner rewardSpinner;
    Button earnButton, buyButton;
    PypetAudio audio;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));
        py = Python.getInstance();
        audio = new PypetAudio();
        MobileAds.initialize(this, initializationStatus -> {});
        rewardAdManager = new RewardAdManager(this);
        rewardAdManager.preload();

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,28,28,28);
        root.setGravity(Gravity.CENTER_HORIZONTAL); scroll.addView(root);

        TextView title = new TextView(this); title.setText("🐾 Pypet Python Academy"); title.setTextSize(26); title.setTextColor(Color.DKGRAY); root.addView(title);
        status = new TextView(this); status.setText("Meet Pip. Learn Python by teaching Pip new tricks."); status.setTextSize(18); root.addView(status);
        petView = new TextView(this); petView.setText("🐶"); petView.setTextSize(72); petView.setGravity(Gravity.CENTER); root.addView(petView, new LinearLayout.LayoutParams(-1,150)); animatePet();

        LinearLayout petButtons = new LinearLayout(this);
        Button feed=button("Feed Pip"), play=button("Play"), learn=button("Learn"); petButtons.addView(feed); petButtons.addView(play); petButtons.addView(learn); root.addView(petButtons);
        Button music=button("♫ Music: ON"); root.addView(music); music.setOnClickListener(v->{ audio.setEnabled(!audio.isEnabled()); music.setText(audio.isEnabled()?"♫ Music: ON":"♫ Music: OFF"); if(audio.isEnabled()) audio.start(); }); audio.start();

        editor=new EditText(this); editor.setGravity(Gravity.TOP|Gravity.START); editor.setText("answer = 2 + 3\nprint(answer)"); editor.setHint("Write Python here..."); editor.setMinLines(8); root.addView(editor,new LinearLayout.LayoutParams(-1,0,1));
        Button run=button("Run Python Lesson"); root.addView(run);
        TextView note=new TextView(this); note.setText("Calm mode: no flashing, strobing, screen shake, or reward haptics. Animations are gentle and non-flashing."); root.addView(note);

        root.addView(sectionTitle("✨ Treasure Trove"));
        TextView treasureInfo=new TextView(this); treasureInfo.setText("Special cosmetic treasures for your Pypet world. Your Python lessons, normal pets, core gameplay and unicorn never require a purchase."); root.addView(treasureInfo);
        Button treasureStore=button("✨ Open Treasure Trove"); root.addView(treasureStore); treasureStore.setOnClickListener(v->TreasureStore.show(this));

        root.addView(sectionTitle("🎁 Earnable World Rewards"));
        TextView rewardExplanation=new TextView(this); rewardExplanation.setText("Optional sponsored world items use Pypet Coins. Watch an optional rewarded ad to earn coins. Ads never gate lessons or core pets."); root.addView(rewardExplanation);
        rewardStatus=new TextView(this); root.addView(rewardStatus);
        earnButton=button("Watch optional ad: +"+RewardAdManager.COINS_PER_REWARDED_AD+" Pypet Coins"); root.addView(earnButton);
        List<String> rewardNames=new ArrayList<>(); for(RewardCatalog.Item item:RewardCatalog.all()) rewardNames.add(item.name+" — "+item.priceCoins+" Pypet Coins");
        rewardSpinner=new Spinner(this); rewardSpinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,rewardNames)); root.addView(rewardSpinner);
        buyButton=button("Unlock selected reward"); root.addView(buyButton);
        TextView policyNote=new TextView(this); policyNote.setText("You choose whether to watch each rewarded ad. Skipping never blocks normal gameplay. Coins and earned items are non-transferable."); root.addView(policyNote);

        refreshRewardStatus();
        feed.setOnClickListener(v->{status.setText("Pip is happily eating. +15 hunger");audio.petSound(330);animatePet();});
        play.setOnClickListener(v->{status.setText("Pip wants to play. +10 happiness");audio.petSound(392);animatePet();});
        learn.setOnClickListener(v->{status.setText("Lesson progress recorded. Next: variables and expressions.");audio.petSound(523);animatePet();});
        run.setOnClickListener(v->runCode()); earnButton.setOnClickListener(v->earnCoins()); buyButton.setOnClickListener(v->purchaseSelectedItem());
        setContentView(scroll);
    }

    private void animatePet(){ if(petView==null)return; ScaleAnimation b=new ScaleAnimation(.96f,1.04f,.96f,1.04f,Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF,1f);b.setDuration(650);b.setRepeatCount(1);b.setRepeatMode(Animation.REVERSE);petView.startAnimation(b); }
    private TextView sectionTitle(String text){TextView v=new TextView(this);v.setText(text);v.setTextSize(22);v.setTextColor(Color.DKGRAY);v.setPadding(0,24,0,8);return v;}
    private Button button(String text){Button b=new Button(this);b.setText(text);return b;}

    private void earnCoins(){earnButton.setEnabled(false);rewardAdManager.show(this,new RewardAdManager.RewardListener(){public void onCoinsGranted(int coins){refreshRewardStatus();status.setText("You earned "+coins+" Pypet Coins.");animatePet();audio.petSound(523);earnButton.setEnabled(true);}public void onAdUnavailable(String message){status.setText(message);earnButton.setEnabled(true);}});}
    private void purchaseSelectedItem(){RewardCatalog.Item item=RewardCatalog.all().get(rewardSpinner.getSelectedItemPosition());if(RewardInventory.owns(this,item.id)){status.setText("You already own "+item.name+".");return;}int balance=RewardInventory.coins(this);if(balance<item.priceCoins){new AlertDialog.Builder(this).setTitle("Not enough Pypet Coins").setMessage(item.name+" costs "+item.priceCoins+" coins. You have "+balance+".").setPositiveButton("Earn Coins",(d,w)->earnCoins()).setNegativeButton("Cancel",null).show();return;}new AlertDialog.Builder(this).setTitle("Unlock "+item.name+"?").setMessage(item.description+"\n\nPrice: "+item.priceCoins+" Pypet Coins.").setPositiveButton("Unlock",(d,w)->{if(RewardInventory.purchase(this,item.id)){try{py.getModule("world_api").callAttr("place_exclusive_item",item.id,"home");}catch(Exception ignored){}refreshRewardStatus();status.setText(item.name+" has been added to your world.");animatePet();audio.petSound(440);}else status.setText("Purchase could not be completed.");}).setNegativeButton("No thanks",null).show();}
    private void refreshRewardStatus(){rewardStatus.setText("Pypet Coins: "+RewardInventory.coins(this)+" | Earned world items: "+RewardInventory.count(this)+" / "+RewardCatalog.all().size());}
    private void runCode(){try{String result=py.getModule("pypet_engine").callAttr("run_lesson",editor.getText().toString()).toString();status.setText(result);audio.petSound(523);animatePet();}catch(Exception e){status.setText("Lesson error: "+e.getMessage());audio.petSound(196);}}
    @Override protected void onPause(){super.onPause();audio.stop();}
    @Override protected void onResume(){super.onResume();if(audio!=null&&audio.isEnabled())audio.start();}
    @Override protected void onDestroy(){audio.stop();super.onDestroy();}
}
