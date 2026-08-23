package com.odelly.pypet;

import android.app.*;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** Player profile, avatar identity, and player-created town setup. */
public final class PypetProfileView {
    private PypetProfileView() {}
    public static void show(Activity a, boolean firstRun, Runnable afterSave){
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,22,28,22);
        TextView title=new TextView(a); title.setText(firstRun?"🐾 CREATE YOUR PYPET WORLD":"👤 PYPET PROFILE"); title.setTextSize(25); title.setTextColor(Color.DKGRAY); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView intro=new TextView(a); intro.setText("Name yourself and your town. Your town becomes part of the World and develops as you learn."); intro.setTextSize(16); intro.setPadding(0,10,0,14); root.addView(intro);
        EditText player=new EditText(a); player.setHint("Your name"); player.setSingleLine(); player.setText(PypetProfileManager.playerName(a)); root.addView(player);
        EditText town=new EditText(a); town.setHint("Name your town"); town.setSingleLine(); town.setText(PypetProfileManager.townName(a)); root.addView(town);
        TextView avatar=new TextView(a); avatar.setText("🐾   🐶   🐱   🦊   🐰"); avatar.setTextSize(26); avatar.setGravity(Gravity.CENTER); avatar.setPadding(0,14,0,14); root.addView(avatar);
        Button save=new Button(a); save.setText(firstRun?"CREATE MY TOWN":"SAVE PROFILE"); root.addView(save);
        AlertDialog d=new AlertDialog.Builder(a).setView(root).setCancelable(!firstRun).create();
        save.setOnClickListener(v->{String n=player.getText().toString().trim(),t=town.getText().toString().trim();if(n.isEmpty()){player.setError("Enter your name");return;}if(t.isEmpty()){town.setError("Name your town");return;}PypetProfileManager.save(a,n,t,"🐾");d.dismiss();if(afterSave!=null)afterSave.run();});
        d.show();
    }
}
