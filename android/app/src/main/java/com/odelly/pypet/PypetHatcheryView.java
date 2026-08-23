package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** Hatchery location: every new egg is independently randomized and becomes a new pet journey. */
public final class PypetHatcheryView {
    private PypetHatcheryView(){}
    public static void show(Activity a){
        PetEvolutionManager.Egg e=PetEvolutionManager.currentEgg(a);
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setGravity(Gravity.CENTER_HORIZONTAL);root.setPadding(30,25,30,25);
        TextView title=new TextView(a);title.setText("🥚 Hatchery");title.setTextSize(30);title.setTextColor(Color.DKGRAY);root.addView(title);
        TextView egg=new TextView(a);egg.setText(e.emoji+"\n\nUnique Egg\n"+e.pattern+" pattern\n"+e.rarity+" discovery");egg.setTextSize(24);egg.setGravity(Gravity.CENTER);root.addView(egg,new LinearLayout.LayoutParams(-1,260));
        TextView info=new TextView(a);info.setText("This egg has its own randomized lineage and identity. Hatch it to begin a new pet journey. Previous pet forms remain collectible.");info.setGravity(Gravity.CENTER);root.addView(info);
        Button hatch=new Button(a);hatch.setText("🐣 Hatch New Pet");root.addView(hatch);hatch.setOnClickListener(v->{PetEvolutionManager.Egg next=PetEvolutionManager.hatchNewEgg(a);PetEvolutionManager.PetVariant p=PetEvolutionManager.current(a);new AlertDialog.Builder(a).setTitle("✨ New pet discovered!").setMessage(next.emoji+"  "+PetEvolutionManager.name(a)+"\n\nLineage: "+p.species+"\nEgg pattern: "+next.pattern+"\n\nRaise your new companion through care, play, school, Python learning and exploration. Every lineage has its own rare forms.").setPositiveButton("Begin adventure",null).show();});
        TextView collection=new TextView(a);collection.setText("\n🥚 Egg journeys started: "+PetEvolutionManager.eggCount(a)+"\n🐾 Collected pet/egg records: "+PetEvolutionManager.collectionCount(a)+"\n🧬 Evolutions on active journey: "+PetEvolutionManager.evolutionCount(a));collection.setTextSize(18);collection.setGravity(Gravity.CENTER);root.addView(collection);
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Back",null).show();
    }
}
