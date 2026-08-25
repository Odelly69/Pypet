package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/**
 * Hatchery lifecycle UI. An egg is the beginning of the active pet journey;
 * hatching selects the pet's lineage, and every later evolution stays on that
 * exact lineage rather than replacing it with a different species.
 */
public final class PypetHatcheryView {
    private PypetHatcheryView(){}

    public static void show(Activity a){
        PetEvolutionManager.Egg e=PetEvolutionManager.currentEgg(a);
        PetEvolutionManager.PetVariant pet=PetEvolutionManager.current(a);
        LinearLayout root=new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(30,25,30,25);

        TextView title=new TextView(a);
        title.setText("🥚 Hatchery");
        title.setTextSize(30);
        title.setTextColor(Color.DKGRAY);
        root.addView(title);

        TextView egg=new TextView(a);
        egg.setText(e.emoji+"\n\nUnique Egg\n"+e.pattern+" pattern\n"+e.rarity+" rarity\n\n🧬 Lineage: "+capitalize(e.lineage));
        egg.setTextSize(22);
        egg.setGravity(Gravity.CENTER);
        root.addView(egg,new LinearLayout.LayoutParams(-1,300));

        TextView path=new TextView(a);
        path.setGravity(Gravity.CENTER);
        path.setTextSize(17);
        path.setText("EGG → HATCHLING → GROWING FORM → ADVANCED FORM → MASTER FORM\n\nYour pet's evolutions stay on the same "+capitalize(e.lineage)+" lineage.\nThe egg does not get replaced when the pet evolves.");
        root.addView(path);

        Button hatch=button(a,"🐣 Hatch this egg");
        root.addView(hatch);
        hatch.setOnClickListener(v->{
            PetEvolutionManager.Egg next=PetEvolutionManager.hatchNewEgg(a);
            PetEvolutionManager.PetVariant newborn=PetEvolutionManager.current(a);
            new AlertDialog.Builder(a)
                .setTitle("✨ Egg hatched!")
                .setMessage(next.emoji+"  "+PetEvolutionManager.name(a)+"\n\n"
                    +"Lineage: "+capitalize(next.lineage)+"\n"
                    +"Egg pattern: "+next.pattern+"\n"
                    +"Rarity: "+next.rarity+"\n\n"
                    +"This hatchling will evolve through the SAME "+capitalize(next.lineage)+" lineage.\n\n"
                    +"Current form: "+newborn.displayName+"\n\n"
                    +"Care, play, Python learning, school, exploration and routines shape how it develops.")
                .setPositiveButton("Begin adventure",null)
                .show();
            refresh(root,a);
        });

        TextView collection=new TextView(a);
        collection.setTextSize(18);
        collection.setGravity(Gravity.CENTER);
        root.addView(collection);
        refresh(root,a);

        new AlertDialog.Builder(a).setView(root).setNegativeButton("Back",null).show();
    }

    private static Button button(Activity a,String text){
        Button b=new Button(a); b.setText(text); b.setAllCaps(false); return b;
    }

    private static void refresh(LinearLayout root,Activity a){
        // The final TextView is the collection/status line.
        View v=root.getChildAt(root.getChildCount()-1);
        if(!(v instanceof TextView))return;
        PetEvolutionManager.Egg e=PetEvolutionManager.currentEgg(a);
        PetEvolutionManager.PetVariant p=PetEvolutionManager.current(a);
        ((TextView)v).setText("\n🥚 Egg journeys started: "+PetEvolutionManager.eggCount(a)
            +"\n🐣 Current hatch lineage: "+capitalize(e.lineage)
            +"\n🐾 Current form: "+p.displayName
            +"\n🧬 Evolutions on this journey: "+PetEvolutionManager.evolutionCount(a)
            +"\n🏆 Collected pet/egg records: "+PetEvolutionManager.collectionCount(a));
    }

    private static String capitalize(String s){
        return s==null||s.isEmpty()?"Unknown":s.substring(0,1).toUpperCase()+s.substring(1);
    }
}
