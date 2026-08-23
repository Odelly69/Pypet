package com.odelly.pypet;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.*;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.ads.MobileAds;

/** Main Pypet screen. Optional subsystems are isolated so startup cannot fail because of audio, ads or Python. */
public class MainActivity extends Activity {
    TextView status, petView, petStats, hygieneBar, wasteStatus;
    PypetAudio audio; PypetSafetyGuard safety;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        safety = new PypetSafetyGuard(this);
        buildMain();
        initializeOptionalSystems();
    }
    private void initializeOptionalSystems() {
        try { if (!Python.isStarted()) Python.start(new AndroidPlatform(this)); } catch (Throwable t) { android.util.Log.e("PYPET", "Python startup failed", t); }
        try { audio = new PypetAudio(); } catch (Throwable t) { android.util.Log.e("PYPET", "Audio construction failed", t); audio = null; }
        try { MobileAds.initialize(this, s -> {}); } catch (Throwable t) { android.util.Log.e("PYPET", "Ads startup failed", t); }
        try { PetCareSystem.tick(this); refreshPet(); } catch (Throwable t) { android.util.Log.e("PYPET", "Pet startup failed", t); }
        if (audio != null) { try { audio.start(); } catch (Throwable t) { android.util.Log.e("PYPET", "Audio start failed", t); } }
    }
    private void buildMain() {
        try {
            ScrollView scroll = new ScrollView(this);
            LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,28,28,28); root.setGravity(17); scroll.addView(root);
            TextView title = new TextView(this); title.setText("🐾 Pypet"); title.setTextSize(30); title.setGravity(17); root.addView(title);
            status = new TextView(this); status.setText("Welcome to your cute Pypet world."); status.setTextSize(18); status.setGravity(17); root.addView(status);
            petView = new TextView(this); petView.setText("🐾\nYour Pypet"); petView.setTextSize(64); petView.setGravity(17); root.addView(petView, new LinearLayout.LayoutParams(-1,190));
            petStats = new TextView(this); petStats.setGravity(17); root.addView(petStats);
            hygieneBar = new TextView(this); hygieneBar.setGravity(17); root.addView(hygieneBar);
            wasteStatus = new TextView(this); wasteStatus.setGravity(17); root.addView(wasteStatus);
            Button profile = new Button(this); profile.setText("👤 Profile & Town"); root.addView(profile); profile.setOnClickListener(v -> { try { PypetProfileView.show(this); } catch(Throwable t){ status.setText("Profile temporarily unavailable."); } });
            Button world = new Button(this); world.setText("🌎 ENTER THE 3D WORLD"); world.setTextSize(20); world.setTextColor(Color.WHITE); world.setBackgroundColor(Color.rgb(35,92,61)); root.addView(world,new LinearLayout.LayoutParams(-1,78)); world.setOnClickListener(v -> { try { ImmersiveWorldView.show(this); } catch(Throwable t){ status.setText("World temporarily unavailable."); } });
            Button refresh = new Button(this); refresh.setText("🔄 Refresh Pypet"); root.addView(refresh); refresh.setOnClickListener(v -> refreshPet());
            TextView note = new TextView(this); note.setText("Cute, cozy and safe. Pet needs progress with real time; learning includes healthy breaks."); note.setGravity(17); root.addView(note);
            setContentView(scroll); refreshPet();
        } catch(Throwable t) {
            TextView fallback = new TextView(this); fallback.setText("🐾 Pypet\n\nThe game could not load one optional feature.\nYour saved world is protected.\nPlease restart Pypet."); fallback.setTextSize(20); fallback.setGravity(17); fallback.setPadding(30,30,30,30); setContentView(fallback); android.util.Log.e("PYPET", "UI startup failed", t);
        }
    }
    @Override public void onConfigurationChanged(Configuration newConfig) { super.onConfigurationChanged(newConfig); buildMain(); }
    private void refreshPet() { try { PetCareSystem.tick(this); PetEvolutionManager.PetVariant p=PetEvolutionManager.current(this); petView.setText(p.emoji+"\n"+PetEvolutionManager.name(this)); petStats.setText(p.displayName+" • Level "+p.level+"\n❤️ Health "+PetEvolutionManager.health(this)+"%   🍖 Hunger "+PetEvolutionManager.hunger(this)+"%   😊 Happiness "+PetEvolutionManager.happiness(this)+"%"); hygieneBar.setText("🧼 Hygiene: "+PetCareSystem.hygiene(this)+"%"); wasteStatus.setText("💩 "+(PetCareSystem.hasWaste(this)?"Your pet has a mess to clean.":"No mess right now.")); } catch(Throwable t) { if(status!=null) status.setText("Your Pypet is safe. Some pet details are temporarily unavailable."); android.util.Log.e("PYPET", "Pet refresh failed", t); } }
}
