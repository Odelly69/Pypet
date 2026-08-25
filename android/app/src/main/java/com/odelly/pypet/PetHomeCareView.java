package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/** Home-only pet care station. Python learning remains exclusively in Academy. */
public final class PetHomeCareView {
    private PetHomeCareView() {}

    public static void show(Activity a){
        PetCareSystem.tick(a);
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,16,20,16);
        TextView title=text(a,"🏠 HOME • PET CARE",25,true); title.setGravity(Gravity.CENTER); root.addView(title);
        TextView intro=text(a,"Home is where you care for your pet: feed, bathe, clean and complete daily care. Drag food into the bowl to feed. Python learning and curriculum progress remain exclusively in PyPet Academy.",15,false); intro.setPadding(0,8,0,12); root.addView(intro);

        TextView stats=text(a,"",17,true); root.addView(stats); refreshStats(a,stats);
        TextView pet=text(a,PetEvolutionManager.isHatched(a)?"🐾 "+PetEvolutionManager.name(a)+" is home and ready for care.":"🥚 Your egg is home. Hatch it before feeding or bathing.",17,true); pet.setGravity(Gravity.CENTER); pet.setPadding(0,12,0,12); root.addView(pet);

        Button hatch=button(a,"🥚 Open Hatchery"); root.addView(hatch); hatch.setOnClickListener(v->PypetHatcheryView.show(a));
        TextView foodTitle=text(a,"🍽️ FOOD PANTRY • DRAG FOOD TO THE BOWL",19,true); foodTitle.setPadding(0,14,0,8); root.addView(foodTitle);

        FrameLayout bowlArea=new FrameLayout(a); bowlArea.setMinimumHeight(150); bowlArea.setBackgroundColor(Color.rgb(232,220,190)); root.addView(bowlArea,new LinearLayout.LayoutParams(-1,150));
        TextView bowl=text(a,"🥣\nDROP FOOD HERE",19,true); bowl.setGravity(Gravity.CENTER); bowl.setTextColor(Color.DKGRAY); bowlArea.addView(bowl,new FrameLayout.LayoutParams(-1,-1));
        bowl.setOnDragListener((v,event)->{
            if(event.getAction()==DragEvent.ACTION_DRAG_STARTED)return event.getClipDescription()!=null&&event.getClipDescription().hasMimeType("text/plain");
            if(event.getAction()==DragEvent.ACTION_DROP){String id=event.getClipData().getItemAt(0).getText().toString();feed(a,id,stats,bowl);return true;}
            return true;
        });

        addCategory(a,root,"🍓 FRUIT",new String[]{"berry","apple"},new String[]{"🍓 Berry Bites","🍎 Apple Snack"});
        addCategory(a,root,"🥕 VEGETABLES",new String[]{"carrot"},new String[]{"🥕 Crunchy Carrot"});
        addCategory(a,root,"🐟 PROTEIN / FISH",new String[]{"fish"},new String[]{"🐟 Happy Fish"});
        addCategory(a,root,"🍰 TREATS",new String[]{"cake"},new String[]{"🍰 Celebration Treat"});

        TextView careTitle=text(a,"🛁 CARE",19,true); careTitle.setPadding(0,16,0,8); root.addView(careTitle);
        Button bath=button(a,"🛁 Bathe pet");root.addView(bath);bath.setOnClickListener(v->{if(requirePet(a)){PetCareSystem.bathe(a);RewardInventory.completeTask(a,"bath",3);refreshStats(a,stats);Toast.makeText(a,"🛁 Bath time complete!",Toast.LENGTH_SHORT).show();}});
        Button clean=button(a,"🧹 Clean up waste");root.addView(clean);clean.setOnClickListener(v->{if(requirePet(a)){PetCareSystem.cleanWaste(a);RewardInventory.completeTask(a,"clean",3);refreshStats(a,stats);Toast.makeText(a,"🧹 Home is clean!",Toast.LENGTH_SHORT).show();}});
        Button routine=button(a,"❤️ Complete daily care routine");root.addView(routine);routine.setOnClickListener(v->{if(requirePet(a)){PetEvolutionManager.performCare(a);PetEvolutionManager.performRoutine(a);RewardInventory.completeTask(a,"care",5);refreshStats(a,stats);Toast.makeText(a,"❤️ Daily care complete!",Toast.LENGTH_SHORT).show();}});
        Button back=button(a,"🌎 Return to PyPet World");root.addView(back);back.setOnClickListener(v->LivingWorldView.show(a));
        ScrollView scroll=new ScrollView(a);scroll.addView(root);a.setContentView(scroll);
    }

    private static void addCategory(Activity a,LinearLayout root,String title,String[] ids,String[] labels){
        TextView h=text(a,title,17,true);h.setPadding(0,10,0,4);root.addView(h);
        LinearLayout row=new LinearLayout(a);row.setOrientation(LinearLayout.HORIZONTAL);root.addView(row);
        for(int i=0;i<ids.length;i++){
            Button food=button(a,labels[i]);final String id=ids[i];
            food.setOnLongClickListener(v->{if(!requirePet(a))return true;ClipData data=ClipData.newPlainText("food",id);v.startDragAndDrop(data,new View.DragShadowBuilder(v),null,0);return true;});
            food.setOnClickListener(v->{if(requirePet(a))Toast.makeText(a,"Drag this food to the bowl 🥣",Toast.LENGTH_SHORT).show();});
            row.addView(food,new LinearLayout.LayoutParams(0,-2,1));
        }
    }
    private static void feed(Activity a,String id,TextView stats,TextView bowl){
        for(PetEvolutionManager.PetFood f:PetEvolutionManager.foods())if(f.id.equals(id)){
            PetEvolutionManager.feed(a,f);RewardInventory.completeTask(a,"feed_"+id,2);refreshStats(a,stats);bowl.setText(f.emoji+"\n"+f.name+"\nYum! 🐾");Toast.makeText(a,"🍽️ "+f.name+" fed to "+PetEvolutionManager.name(a)+".",Toast.LENGTH_SHORT).show();return;
        }
    }
    private static boolean requirePet(Activity a){if(!PetEvolutionManager.isHatched(a)){Toast.makeText(a,"🥚 Hatch your egg first.",Toast.LENGTH_SHORT).show();return false;}return true;}
    private static void refreshStats(Activity a,TextView out){PetCareSystem.tick(a);out.setText("❤️ Health "+PetEvolutionManager.health(a)+"   🍎 Hunger "+PetEvolutionManager.hunger(a)+"   😊 Happiness "+PetEvolutionManager.happiness(a)+"   🧼 Hygiene "+PetCareSystem.hygiene(a)+"\n🪙 "+RewardInventory.coins(a)+" Pypet Coins");}
    private static TextView text(Activity a,String s,float size,boolean bold){TextView v=new TextView(a);v.setText(s);v.setTextSize(size);v.setTextColor(Color.rgb(45,55,45));if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;}
    private static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}
}
