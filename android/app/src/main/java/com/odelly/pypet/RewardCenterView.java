package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import java.util.List;

/** Player-facing optional rewarded-ad economy and exclusive World decoration shop. */
public final class RewardCenterView {
    private RewardCenterView() {}

    public static void show(Activity a) {
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(24,18,24,18);
        TextView title=new TextView(a); title.setText("🎁 PyPet Reward Center"); title.setTextSize(26); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView wallet=new TextView(a); wallet.setTextSize(18); wallet.setGravity(Gravity.CENTER); root.addView(wallet);
        Button ad=new Button(a); ad.setAllCaps(false); ad.setText("📺 Watch an optional rewarded ad • +25 Pypet Coins"); root.addView(ad);
        TextView note=new TextView(a); note.setText("Ads are optional. They only accelerate coins for exclusive decorations; they never gate Python lessons, ordinary pets, the unicorn, core care, or saved progress."); note.setTextColor(Color.DKGRAY); note.setPadding(0,8,0,14); root.addView(note);
        ScrollView scroll=new ScrollView(a); LinearLayout items=new LinearLayout(a); items.setOrientation(LinearLayout.VERTICAL); scroll.addView(items); root.addView(scroll,new LinearLayout.LayoutParams(-1,0,1));
        Button back=new Button(a); back.setAllCaps(false); back.setText("🌎 Return to PyPet World"); root.addView(back);
        RewardAdManager ads=new RewardAdManager(a); ads.preload();
        Runnable refresh=()->wallet.setText("🪙 Pypet Coins: "+RewardInventory.coins(a)+"   •   🎨 Owned: "+RewardInventory.count(a));
        refresh.run();
        ad.setOnClickListener(v->ads.show(a,new RewardAdManager.RewardListener(){@Override public void onCoinsGranted(int coins){refresh.run();Toast.makeText(a,"+"+coins+" Pypet Coins earned!",Toast.LENGTH_SHORT).show();} @Override public void onAdUnavailable(String message){Toast.makeText(a,message,Toast.LENGTH_SHORT).show();}}));
        List<RewardCatalog.Item> catalog=RewardCatalog.all(); int index=0;
        for(RewardCatalog.Item item:catalog){
            if(!item.tier.equals("COIN")&&!item.tier.equals("AD_COIN"))continue;
            final RewardCatalog.Item x=item; final int slot=index++;
            Button buy=new Button(a); buy.setAllCaps(false); buy.setText((x.tier.equals("AD_COIN")?"📺 ":"✨ ")+x.name+" • "+x.priceCoins+" coins\n"+x.description);
            if(RewardInventory.owns(a,x.id))buy.setEnabled(false);
            buy.setOnClickListener(v->{
                if(RewardInventory.purchase(a,x.id)){
                    float px=-1000+(slot%8)*250f, py=1120+(slot/8)*125f;
                    WorldPlacementManager.placeUnique(a,x.id,px,py,1f,0f);
                    refresh(); buy.setEnabled(false); Toast.makeText(a,"Added to your World — go show it off!",Toast.LENGTH_SHORT).show();
                } else Toast.makeText(a,"Earn more Pypet Coins to unlock this decoration.",Toast.LENGTH_SHORT).show();
            });
            items.addView(buy,new LinearLayout.LayoutParams(-1,-2));
        }
        back.setOnClickListener(v->LivingWorldView.show(a));
        a.setContentView(root);
    }
}
