package com.odelly.pypet;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.ads.MobileAds;

/** PyPet starts directly in the living World. Optional systems are isolated from the UI. */
public class MainActivity extends Activity {
    PypetAudio audio; PypetSafetyGuard safety;
    @Override public void onCreate(Bundle state){super.onCreate(state);safety=new PypetSafetyGuard(this);initializeOptionalSystems();enterWorld();}
    private void initializeOptionalSystems(){try{if(!Python.isStarted())Python.start(new AndroidPlatform(this));}catch(Throwable t){android.util.Log.e("PYPET","Python startup failed",t);}try{audio=new PypetAudio();}catch(Throwable t){android.util.Log.e("PYPET","Audio construction failed",t);audio=null;}try{MobileAds.initialize(this,s->{});}catch(Throwable t){android.util.Log.e("PYPET","Ads startup failed",t);}try{PetCareSystem.tick(this);}catch(Throwable t){android.util.Log.e("PYPET","Pet startup failed",t);}if(audio!=null)try{audio.start();}catch(Throwable t){android.util.Log.e("PYPET","Audio start failed",t);}}
    private void enterWorld(){try{LivingWorldView.show(this);}catch(Throwable t){android.util.Log.e("PYPET","World startup failed",t);TextView fallback=new TextView(this);fallback.setText("🐾 PyPet\n\nYour World could not be opened.\nYour saved world is protected.\nPlease try again.");fallback.setTextSize(20);fallback.setGravity(17);fallback.setPadding(30,30,30,30);setContentView(fallback);}}
    @Override protected void onPause(){super.onPause();if(audio!=null)try{audio.pauseForLifecycle();}catch(Throwable ignored){}}
    @Override protected void onResume(){super.onResume();if(audio!=null)try{audio.resumeForLifecycle();}catch(Throwable ignored){}}
    @Override protected void onDestroy(){if(audio!=null)try{audio.stop();}catch(Throwable ignored){}super.onDestroy();}
    @Override public void onConfigurationChanged(Configuration newConfig){super.onConfigurationChanged(newConfig);enterWorld();}
}
