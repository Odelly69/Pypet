package com.odelly.pypet;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** Profile/town setup and profile display. Designed for eventual onboarding and World access. */
public final class PypetProfileView {
    private PypetProfileView() {}
    public static void show(Activity a){
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,22,28,22);
        TextView title=new TextView(a); title.setText("👤 PYPET PROFILE"); title.setTextSize(25); title.setTextColor(Color.DKGRAY); title.setGravity(Gravity.CENTER); root.addView(title);
        EditText player=new EditText(a); player.setHint("Your name"); player.setSingleLine(); player.setText(PypetProfileManager.playerName(a)); root.addView(player);
        EditText town=new EditText(a); town.setHint("Name your town"); town.setSingleLine(); town.setText(PypetProfileManager.townName(a)); root.addView(town);
        TextView note=new TextView(a); note.setText("Your town name becomes part of the World and can grow as you learn. You can change it later."); root.addView(note);
        Button save=new Button(a); save.setText("Save Profile & Town"); root.addView(save);
        save.setOnClickListener(v->{String n=player.getText().toString().trim(),t=town.getText().toString().trim();if(n.isEmpty()||t.isEmpty()){Toast.makeText(a,"Enter both your name and town name.",Toast.LENGTH_SHORT).show();return;}PypetProfileManager.save(a,n,t,"🐾");Toast.makeText(a,"Welcome to "+t+", "+n+"!",Toast.LENGTH_SHORT).show();});
        new AlertDialog.Builder(a).setView(root).setPositiveButton("Done",null).show();
    }
}
