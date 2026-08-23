package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/** Simple game-like Pet World screen: active pet plus other named residents. */
public final class PetWorldView {
    private PetWorldView(){}
    public static void show(Activity a){
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,24,28,24);root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a);title.setText("🌎 Pip's World");title.setTextSize(28);title.setTextColor(Color.DKGRAY);root.addView(title);
        PetEvolutionManager.PetVariant active=PetEvolutionManager.current(a);
        TextView activeView=new TextView(a);activeView.setText("YOUR PET\n"+active.emoji+"  "+PetEvolutionManager.name(a)+"\n"+active.displayName+" • Level "+active.level+"\n\n❤️ Health "+PetEvolutionManager.health(a)+"%   🍖 Hunger "+PetEvolutionManager.hunger(a)+"%   😊 Happiness "+PetEvolutionManager.happiness(a)+"%\n📚 Lessons "+PetEvolutionManager.lessons(a)+"   🎾 Play "+PetEvolutionManager.play(a)+"   🧼 Care "+PetEvolutionManager.care(a));activeView.setTextSize(18);activeView.setGravity(Gravity.CENTER);root.addView(activeView);
        TextView residents=new TextView(a);residents.setText("\nWORLD RESIDENTS");residents.setTextSize(21);root.addView(residents);
        List<PetEvolutionManager.PetVariant> pets=PetEvolutionManager.worldPets(a);for(int i=1;i<pets.size();i++){PetEvolutionManager.PetVariant v=pets.get(i);TextView row=new TextView(a);row.setText(v.emoji+"  "+residentName(i)+" — "+v.displayName+"\n     "+v.description);row.setTextSize(17);row.setPadding(12,12,12,12);root.addView(row);}
        Button food=button(a,"🍎 Food & Care");root.addView(food);food.setOnClickListener(v->showFood(a));
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Back",null).show();
    }
    private static void showFood(Activity a){String[] names=new String[PetEvolutionManager.foods().size()];for(int i=0;i<names.length;i++){PetEvolutionManager.PetFood f=PetEvolutionManager.foods().get(i);names[i]=f.emoji+" "+f.name+"  (🍖 +"+f.hunger+"  😊 +"+f.happiness+"  ❤️ +"+f.health+")";}new AlertDialog.Builder(a).setTitle("🍎 Feed your pet").setItems(names,(d,w)->{PetEvolutionManager.feed(a,PetEvolutionManager.foods().get(w));new AlertDialog.Builder(a).setTitle("Yum!").setMessage(PetEvolutionManager.name(a)+" enjoyed the food.\n\n🍖 Hunger: "+PetEvolutionManager.hunger(a)+"%\n😊 Happiness: "+PetEvolutionManager.happiness(a)+"%\n❤️ Health: "+PetEvolutionManager.health(a)+"%\n\nKeep caring, playing and learning to shape evolution!").setPositiveButton("Great!",null).show();}).setNegativeButton("Cancel",null).show();}
    private static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);return b;}
    private static String residentName(int i){String[] n={"Mochi","Clover","Pixel","Sunny","Waffles"};return n[(i-1)%n.length];}
}
