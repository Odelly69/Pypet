package com.odelly.pypet;

import android.app.*;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** Player profile, avatar identity, and player-created town setup. */
public final class PypetProfileView {
    private PypetProfileView() {}
    public static void show(Activity a){show(a,!PypetProfileManager.complete(a),a::recreate);}
    public static void show(Activity a,boolean firstRun,Runnable afterSave){
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,22,28,22);
        TextView title=new TextView(a);title.setText(firstRun?"🐾 CREATE YOUR PYPET WORLD":"👤 PYPET PROFILE");title.setTextSize(25);title.setTextColor(Color.DKGRAY);title.setGravity(Gravity.CENTER);root.addView(title);
        TextView intro=new TextView(a);intro.setText("Name yourself and your town. Your town becomes part of the World and develops as you learn.");intro.setTextSize(16);intro.setPadding(0,10,0,14);root.addView(intro);
        EditText player=new EditText(a);player.setHint("Your name");player.setSingleLine();player.setText(PypetProfileManager.playerName(a));root.addView(player);
        EditText town=new EditText(a);town.setHint("Name your town");town.setSingleLine();town.setText(PypetProfileManager.townName(a));root.addView(town);
        TextView avatar=new TextView(a);avatar.setText("Choose your avatar");avatar.setTextSize(16);avatar.setGravity(Gravity.CENTER);root.addView(avatar);
        final String[] choices={"🐾","🐶","🐱","🦊","🐰","🐲","🦄","🤖"};final String[] selected={PypetProfileManager.avatar(a)};
        LinearLayout avatars=new LinearLayout(a);avatars.setGravity(Gravity.CENTER);root.addView(avatars);
        for(String choice:choices){Button b=new Button(a);b.setText(choice);b.setTextSize(23);b.setAllCaps(false);b.setOnClickListener(v->{selected[0]=choice;avatar.setText("Avatar: "+choice);});avatars.addView(b,new LinearLayout.LayoutParams(0,60,1));}
        avatar.setText("Avatar: "+selected[0]);
        Button save=new Button(a);save.setText(firstRun?"CREATE MY TOWN":"SAVE PROFILE");root.addView(save);
        AlertDialog d=new AlertDialog.Builder(a).setView(root).setCancelable(!firstRun).create();
        save.setOnClickListener(v->{String n=player.getText().toString().trim(),t=town.getText().toString().trim();if(n.isEmpty()){player.setError("Enter your name");return;}if(t.isEmpty()){town.setError("Name your town");return;}PypetProfileManager.save(a,n,t,selected[0]);d.dismiss();if(afterSave!=null)afterSave.run();});
        d.show();
    }
}
