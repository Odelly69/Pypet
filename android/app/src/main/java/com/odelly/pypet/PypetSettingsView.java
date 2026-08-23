package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** Player-facing settings, audio/safety controls, and avatar selection. */
public final class PypetSettingsView {
    private static final String[] AVATARS = {"🧑","👩","👨","🧒","👩🏽","👨🏽","🧑🏽","🧑🏻","🧑🏿","🧙","🧑‍🚀","🧑‍🎨"};
    private PypetSettingsView() {}
    public static void show(Activity a, PypetAudio audio, PypetSafetyGuard safety) {
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(32,24,32,24);
        TextView title=new TextView(a);title.setText("⚙️ Pypet Settings");title.setTextSize(28);title.setGravity(Gravity.CENTER);root.addView(title);
        Button music=new Button(a);music.setText(audio.isMusicEnabled()?"🎵 Music: ON":"🎵 Music: OFF");root.addView(music);music.setOnClickListener(v->{audio.setMusicEnabled(!audio.isMusicEnabled());music.setText(audio.isMusicEnabled()?"🎵 Music: ON":"🎵 Music: OFF");});
        Button sfx=new Button(a);sfx.setText(audio.isSfxEnabled()?"🔊 SFX: ON":"🔇 SFX: OFF");root.addView(sfx);sfx.setOnClickListener(v->{audio.setSfxEnabled(!audio.isSfxEnabled());sfx.setText(audio.isSfxEnabled()?"🔊 SFX: ON":"🔇 SFX: OFF");});
        Button motion=new Button(a);motion.setText(safety.reducedMotion()?"♿ Reduced Motion: ON":"♿ Reduced Motion: OFF");root.addView(motion);motion.setOnClickListener(v->{safety.setReducedMotion(!safety.reducedMotion());motion.setText(safety.reducedMotion()?"♿ Reduced Motion: ON":"♿ Reduced Motion: OFF");});
        Button avatar=new Button(a);avatar.setText("🧑 Player Icon");root.addView(avatar);avatar.setOnClickListener(v->chooseAvatar(a));
        Button profile=new Button(a);profile.setText("👤 Edit Profile & Town");root.addView(profile);profile.setOnClickListener(v->PypetProfileView.show(a));
        TextView safetyNote=new TextView(a);safetyNote.setText("Safety controls avoid intentional flashing, strobing, rapid animation and screen shake. Music and SFX can be controlled independently.");safetyNote.setTextColor(Color.DKGRAY);root.addView(safetyNote);
        new AlertDialog.Builder(a).setView(root).setPositiveButton("Done",null).show();
    }
    private static void chooseAvatar(Activity a){
        new AlertDialog.Builder(a).setTitle("Choose your player icon").setItems(AVATARS,(d,which)->{PypetProfileManager.setAvatar(a,AVATARS[which]);Toast.makeText(a,"Player icon updated",Toast.LENGTH_SHORT).show();}).show();
    }
}