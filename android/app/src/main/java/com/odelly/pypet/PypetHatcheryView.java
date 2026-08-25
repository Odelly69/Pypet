package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/**
 * Hatchery lifecycle UI. The displayed egg is the actual source of the pet;
 * hatching never swaps in a different random egg, and later evolution keeps
 * the exact same lineage.
 */
public final class PypetHatcheryView {
    private PypetHatcheryView(){}

    public static void show(Activity a){
        PetEvolutionManager.Egg e=PetEvolutionManager.currentEgg(a);
        LinearLayout root=new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        root.setPadding(30,25,30,25);

        TextView title=new TextView(a); title.setText("🥚 Hatchery"); title.setTextSize(30); title.setTextColor(Color.DKGRAY); root.addView(title);
        TextView egg=new TextView(a); egg.setTextSize(22); egg.setGravity(Gravity.CENTER); root.addView(egg,new LinearLayout.LayoutParams(-1,300));
        TextView path=new TextView(a); path.setGravity(Gravity.CENTER); path.setTextSize(17); root.addView(path);
        Button hatch=button(a,"🐣 Hatch this egg"); root.addView(hatch);
        TextView collection=new TextView(a); collection.setTextSize(18); collection.setGravity(Gravity.CENTER); root.addView(collection);

        Runnable refresh=()->{
            PetEvolutionManager.Egg current=PetEvolutionManager.currentEgg(a);
            boolean hatched=PetEvolutionManager.isHatched(a);
            PetEvolutionManager.PetVariant pet=PetEvolutionManager.current(a);
            egg.setText(hatched
                ? pet.emoji+"\n\n🐣 Hatched pet\n"+pet.displayName+"\n\n🧬 Lineage: "+capitalize(current.lineage)+"\n🥚 Origin egg: "+current.pattern+" / "+current.rarity
                : current.emoji+"\n\nUnique Egg\n"+current.pattern+" pattern\n"+current.rarity+" rarity\n\n🧬 Lineage: "+capitalize(current.lineage));
            path.setText("EGG → HATCHLING → GROWING FORM → ADVANCED FORM → MASTER FORM\n\n"
                +"The egg is the origin of the journey. Every evolution stays on the SAME "+capitalize(current.lineage)+" lineage.\n"
                +(hatched?"\n🐾 This egg has hatched. Keep caring, playing and learning to evolve your pet.":"\nThe Academy, care, play, school, exploration and routines shape the pet after hatching."));
            hatch.setText(hatched?"🥚 Start a new egg journey":"🐣 Hatch this egg");
            hatch.setEnabled(true);
            collection.setText("\n🥚 Egg journeys started: "+PetEvolutionManager.eggCount(a)
                +"\n🐣 Current lineage: "+capitalize(current.lineage)
                +"\n🐾 Current form: "+pet.displayName
                +"\n🧬 Evolutions on this journey: "+PetEvolutionManager.evolutionCount(a)
                +"\n🏆 Collected pet/egg records: "+PetEvolutionManager.collectionCount(a));
        };
        refresh.run();
        hatch.setOnClickListener(v->{
            if(PetEvolutionManager.isHatched(a)){
                PetEvolutionManager.hatchNewEgg(a);
                refresh.run();
                Toast.makeText(a,"🥚 A new egg is ready. Hatch this exact egg when you're ready.",Toast.LENGTH_SHORT).show();
                return;
            }
            PetEvolutionManager.Egg hatched=PetEvolutionManager.hatchCurrentEgg(a);
            PetEvolutionManager.PetVariant newborn=PetEvolutionManager.current(a);
            refresh.run();
            new AlertDialog.Builder(a)
                .setTitle("✨ Egg hatched!")
                .setMessage(hatched.emoji+"  "+PetEvolutionManager.name(a)+"\n\n"
                    +"Lineage: "+capitalize(hatched.lineage)+"\n"
                    +"Egg pattern: "+hatched.pattern+"\n"
                    +"Rarity: "+hatched.rarity+"\n\n"
                    +"This hatchling will evolve through the SAME "+capitalize(hatched.lineage)+" lineage.\n\n"
                    +"Current form: "+newborn.displayName+"\n\n"
                    +"Care, play, Python learning, school, exploration and routines shape how it develops.")
                .setPositiveButton("Begin adventure",null).show();
        });
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Back",null).show();
    }

    private static Button button(Activity a,String text){Button b=new Button(a);b.setText(text);b.setAllCaps(false);return b;}
    private static String capitalize(String s){return s==null||s.isEmpty()?"Unknown":s.substring(0,1).toUpperCase()+s.substring(1);}
}
