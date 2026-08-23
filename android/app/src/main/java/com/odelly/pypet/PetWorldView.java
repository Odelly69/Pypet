package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

/** Game-like Pet World: the active pet plus distinct named residents. */
public final class PetWorldView {
    private PetWorldView(){}
    public static void show(Activity a){
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(28,24,28,24);root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a);title.setText("🌎 "+PetEvolutionManager.name(a)+"'s World");title.setTextSize(28);title.setTextColor(Color.DKGRAY);root.addView(title);
        String activeName=PetEvolutionManager.name(a);PetEvolutionManager.PetVariant active=PetEvolutionManager.current(a);
        TextView activeView=new TextView(a);activeView.setText("YOUR PET\n"+active.emoji+"  "+activeName+"\n"+active.displayName+" • Level "+active.level+"\n\n❤️ Health "+PetEvolutionManager.health(a)+"%   🍖 Hunger "+PetEvolutionManager.hunger(a)+"%   😊 Happiness "+PetEvolutionManager.happiness(a)+"%\n📚 Lessons "+PetEvolutionManager.lessons(a)+"   🎾 Play "+PetEvolutionManager.play(a)+"   🧼 Care "+PetEvolutionManager.care(a));activeView.setTextSize(18);activeView.setGravity(Gravity.CENTER);root.addView(activeView);
        TextView residents=new TextView(a);residents.setText("\nWORLD RESIDENTS");residents.setTextSize(21);root.addView(residents);
        List<PetEvolutionManager.PetVariant> pets=PetEvolutionManager.worldPets(a);for(int i=1;i<pets.size();i++){PetEvolutionManager.PetVariant v=pets.get(i);String name=uniqueResidentName(activeName,i);TextView row=new TextView(a);row.setText(v.emoji+"  "+name+" — "+v.displayName+"\n     "+v.description);row.setTextSize(17);row.setPadding(12,12,12,12);root.addView(row);}
        Button food=button(a,"🍎 Food & Care");root.addView(food);food.setOnClickListener(v->showFood(a));
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Back",null).show();
    }
    private static void showFood(Activity a){String[] names=new String[PetEvolutionManager.foods().size()];for(int i=0;i<names.length;i++){PetEvolutionManager.PetFood f=PetEvolutionManager.foods().get(i);names[i]=f.emoji+" "+f.name+"  (🍖 +"+f.hunger+"  😊 +"+f.happiness+"  ❤️ +"+f.health+")";}new AlertDialog.Builder(a).setTitle("🍎 Feed "+PetEvolutionManager.name(a)).setItems(names,(d,w)->{PetEvolutionManager.feed(a,PetEvolutionManager.foods().get(w));new AlertDialog.Builder(a).setTitle("Yum!").setMessage(PetEvolutionManager.name(a)+" enjoyed the food.\n\n🍖 Hunger: "+PetEvolutionManager.hunger(a)+"%\n😊 Happiness: "+PetEvolutionManager.happiness(a)+"%\n❤️ Health: "+PetEvolutionManager.health(a)+"%\n\nKeep caring, playing and learning to shape evolution!").setPositiveButton("Great!",null).show();}).setNegativeButton("Cancel",null).show();}
    private static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);return b;}
    private static String uniqueResidentName(String active,int index){String[] pool={"Mochi","Clover","Pixel","Sunny","Waffles","Noodle","Biscuit","Pebble","Sprout","Mango","Toby","Poppy"};int offset=0;for(int i=0;i<pool.length;i++)if(pool[i].equals(active)){offset=1;break;}return pool[(index-1+offset)%pool.length];}
}
