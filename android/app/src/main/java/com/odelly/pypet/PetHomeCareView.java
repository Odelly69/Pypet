package com.odelly.pypet;

import android.app.Activity;
import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/** Home is exclusively for care. Three rooms are swipeable: main room, kitchen and bathroom. */
public final class PetHomeCareView {
    private PetHomeCareView() {}
    public static void show(Activity a){
        PetCareSystem.tick(a);
        LinearLayout outer=new LinearLayout(a);outer.setOrientation(LinearLayout.VERTICAL);outer.setPadding(12,10,12,8);outer.setBackgroundColor(Color.rgb(245,241,222));
        TextView title=text(a,"🏠 HOME • CARE",25,true);title.setGravity(Gravity.CENTER);outer.addView(title);
        TextView hint=text(a,"Swipe left/right to rotate rooms • Home is for care only • Python learning happens at the Academy",14,false);hint.setGravity(Gravity.CENTER);outer.addView(hint);
        TextView stats=text(a,"",16,true);stats.setGravity(Gravity.CENTER);stats.setPadding(0,8,0,8);outer.addView(stats);refreshStats(a,stats);
        ViewFlipper rooms=new ViewFlipper(a);rooms.setInAnimation(a,android.R.anim.slide_in_left);rooms.setOutAnimation(a,android.R.anim.slide_out_right);outer.addView(rooms,new LinearLayout.LayoutParams(-1,0,1));
        rooms.addView(mainRoom(a,stats));rooms.addView(kitchen(a,stats));rooms.addView(bathroom(a,stats));
        float[] down={0};rooms.setOnTouchListener((v,e)->{if(e.getAction()==MotionEvent.ACTION_DOWN){down[0]=e.getX();return true;}if(e.getAction()==MotionEvent.ACTION_UP){float dx=e.getX()-down[0];if(Math.abs(dx)>90){if(dx<0)rooms.showNext();else rooms.showPrevious();}return true;}return true;});
        TextView roomLabel=text(a,"MAIN ROOM   •   KITCHEN   •   BATHROOM",13,true);roomLabel.setGravity(Gravity.CENTER);outer.addView(roomLabel);
        Button back=button(a,"🌎 Return to PyPet World");outer.addView(back);back.setOnClickListener(v->LivingWorldView.show(a));
        ScrollView scroll=new ScrollView(a);scroll.addView(outer);a.setContentView(scroll);
    }
    private static LinearLayout mainRoom(Activity a,TextView stats){
        LinearLayout r=room(a,"🛋️ MAIN ROOM","Your pet's living space. Care status, rest and daily routine live here.");
        TextView pet=text(a,PetEvolutionManager.isHatched(a)?"🐾 "+PetEvolutionManager.name(a)+"\n\n❤️ Your pet is home.\nGive care here, then rotate to the kitchen or bathroom.":"🥚 Your egg is home.\nOpen the Hatchery before care.",21,true);pet.setGravity(Gravity.CENTER);pet.setPadding(10,40,10,40);r.addView(pet);
        Button hatch=button(a,"🥚 Open Hatchery");r.addView(hatch);hatch.setOnClickListener(v->PypetHatcheryView.show(a));
        Button routine=button(a,"❤️ Complete daily care routine");r.addView(routine);routine.setOnClickListener(v->{if(requirePet(a)){PetEvolutionManager.performCare(a);PetEvolutionManager.performRoutine(a);RewardInventory.completeTask(a,"care",5);refreshStats(a,stats);Toast.makeText(a,"❤️ Care routine complete!",Toast.LENGTH_SHORT).show();}});
        Button clean=button(a,"🧹 Clean up waste");r.addView(clean);clean.setOnClickListener(v->{if(requirePet(a)){PetCareSystem.cleanWaste(a);RewardInventory.completeTask(a,"clean",3);refreshStats(a,stats);}});
        return r;
    }
    private static LinearLayout kitchen(Activity a,TextView stats){
        LinearLayout r=room(a,"🍳 KITCHEN","Choose a food category, then long-press a food and drag it into the bowl.");
        FrameLayout bowlArea=new FrameLayout(a);bowlArea.setMinimumHeight(150);bowlArea.setBackgroundColor(Color.rgb(232,220,190));TextView bowl=text(a,"🥣\nDROP FOOD HERE",19,true);bowl.setGravity(Gravity.CENTER);bowlArea.addView(bowl,new FrameLayout.LayoutParams(-1,-1));r.addView(bowlArea,new LinearLayout.LayoutParams(-1,150));
        bowl.setOnDragListener((v,event)->{if(event.getAction()==DragEvent.ACTION_DRAG_STARTED)return event.getClipDescription()!=null&&event.getClipDescription().hasMimeType("text/plain");if(event.getAction()==DragEvent.ACTION_DROP){String id=event.getClipData().getItemAt(0).getText().toString();feed(a,id,stats,bowl);return true;}return true;});
        addCategory(a,r,"🍓 FRUIT",new String[]{"berry","apple"},new String[]{"🍓 Berry Bites","🍎 Apple Snack"});
        addCategory(a,r,"🥕 VEGETABLES",new String[]{"carrot"},new String[]{"🥕 Crunchy Carrot"});
        addCategory(a,r,"🐟 PROTEIN / FISH",new String[]{"fish"},new String[]{"🐟 Happy Fish"});
        addCategory(a,r,"🍰 TREATS",new String[]{"cake"},new String[]{"🍰 Celebration Treat"});
        return r;
    }
    private static LinearLayout bathroom(Activity a,TextView stats){
        LinearLayout r=room(a,"🚿 BATHROOM","Drag 🧼 SOAP across your pet to make suds. Move the animated 🚿 SHOWER over the soapy pet to rinse the suds away.");
        FrameLayout showerRoom=new FrameLayout(a);showerRoom.setMinimumHeight(500);showerRoom.setBackgroundColor(Color.rgb(220,236,238));
        TextView pet=text(a,PetEvolutionManager.isHatched(a)?"🐾\nPET SHOWER ZONE":"🥚 Hatch your pet first",24,true);pet.setGravity(Gravity.CENTER);FrameLayout.LayoutParams pp=new FrameLayout.LayoutParams(250,250,Gravity.CENTER);pp.topMargin=80;showerRoom.addView(pet,pp);
        TextView suds=text(a,"🫧🫧🫧\nSOAP SUDS",22,true);suds.setGravity(Gravity.CENTER);suds.setVisibility(View.GONE);FrameLayout.LayoutParams sp=new FrameLayout.LayoutParams(230,120,Gravity.CENTER);sp.topMargin=100;showerRoom.addView(suds,sp);
        final boolean[] soaped={false};
        TextView soap=text(a,"🧼",44,true);soap.setGravity(Gravity.CENTER);FrameLayout.LayoutParams sop=new FrameLayout.LayoutParams(100,90);sop.leftMargin=430;sop.topMargin=20;showerRoom.addView(soap,sop);
        final float[] soapStart={430,20};
        soap.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){soapStart[0]=v.getX();soapStart[1]=v.getY();return true;}
            if(e.getAction()==MotionEvent.ACTION_MOVE){
                v.setX(e.getRawX()-v.getWidth()/2f);v.setY(e.getRawY()-v.getHeight()-35f);v.setRotation((float)Math.sin(e.getRawX()/30f)*7f);
                if(requirePet(a) && overlaps(v,pet)){
                    if(!soaped[0]){
                        soaped[0]=true;suds.setVisibility(View.VISIBLE);suds.setAlpha(0f);suds.setScaleX(.7f);suds.setScaleY(.7f);
                        suds.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(350).start();
                        Toast.makeText(a,"🧼 Scrub! Soap suds are building up.",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
            if(e.getAction()==MotionEvent.ACTION_UP){
                if(soaped[0]) Toast.makeText(a,"🧼 Great scrub! Now rinse with the shower.",Toast.LENGTH_SHORT).show();
                soap.animate().x(soapStart[0]).y(soapStart[1]).rotation(0).setDuration(250).start();return true;
            }
            return true;
        });
        TextView head=text(a,"🚿",46,true);head.setGravity(Gravity.CENTER);FrameLayout.LayoutParams hp=new FrameLayout.LayoutParams(100,90);hp.leftMargin=40;hp.topMargin=20;showerRoom.addView(head,hp);
        final float[] showerStart={40,20};
        head.setOnTouchListener((v,e)->{
            if(e.getAction()==MotionEvent.ACTION_DOWN){showerStart[0]=v.getX();showerStart[1]=v.getY();return true;}
            if(e.getAction()==MotionEvent.ACTION_MOVE){v.setX(e.getRawX()-v.getWidth()/2f);v.setY(e.getRawY()-v.getHeight()-35f);v.setRotation((float)Math.sin(e.getRawX()/35f)*5f);return true;}
            if(e.getAction()==MotionEvent.ACTION_UP){
                if(requirePet(a) && soaped[0] && overlaps(v,pet)){
                    head.animate().rotation(0).setDuration(180).start();
                    suds.animate().alpha(0f).scaleX(1.4f).scaleY(1.4f).setDuration(450).withEndAction(()->{suds.setVisibility(View.GONE);suds.setAlpha(1f);suds.setScaleX(1f);suds.setScaleY(1f);}).start();
                    soaped[0]=false;PetCareSystem.bathe(a);RewardInventory.completeTask(a,"bath",3);refreshStats(a,stats);
                    Toast.makeText(a,"🚿 Rinse complete! The soap suds are washed away.",Toast.LENGTH_SHORT).show();
                } else if(requirePet(a) && !soaped[0]) Toast.makeText(a,"🧼 Scrub your pet with soap first.",Toast.LENGTH_SHORT).show();
                head.animate().x(showerStart[0]).y(showerStart[1]).rotation(0).setDuration(250).start();return true;
            }
            return true;
        });
        TextView instruction=text(a,"1️⃣ DRAG SOAP ACROSS PET → 🫧 SUDS  •  2️⃣ DRAG SHOWER OVER PET → RINSE",14,true);instruction.setGravity(Gravity.CENTER);FrameLayout.LayoutParams ip=new FrameLayout.LayoutParams(-1,70,Gravity.BOTTOM);showerRoom.addView(instruction,ip);
        r.addView(showerRoom,new LinearLayout.LayoutParams(-1,500));
        return r;
    }
    private static boolean overlaps(View a,View b){Rect ar=new Rect(),br=new Rect();a.getHitRect(ar);b.getHitRect(br);return Rect.intersects(ar,br);}
    private static LinearLayout room(Activity a,String name,String description){LinearLayout r=new LinearLayout(a);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(8,14,8,14);TextView h=text(a,name,23,true);h.setGravity(Gravity.CENTER);r.addView(h);TextView d=text(a,description,15,false);d.setGravity(Gravity.CENTER);d.setPadding(4,8,4,16);r.addView(d);return r;}
    private static void addCategory(Activity a,LinearLayout root,String title,String[] ids,String[] labels){TextView h=text(a,title,17,true);h.setPadding(0,10,0,4);root.addView(h);LinearLayout row=new LinearLayout(a);root.addView(row);for(int i=0;i<ids.length;i++){Button food=button(a,labels[i]);final String id=ids[i];food.setOnLongClickListener(v->{if(!requirePet(a))return true;ClipData data=ClipData.newPlainText("food",id);v.startDragAndDrop(data,new View.DragShadowBuilder(v),null,0);return true;});food.setOnClickListener(v->{if(requirePet(a))Toast.makeText(a,"Long-press then drag to 🥣",Toast.LENGTH_SHORT).show();});row.addView(food,new LinearLayout.LayoutParams(0,-2,1));}}
    private static void feed(Activity a,String id,TextView stats,TextView bowl){for(PetEvolutionManager.PetFood f:PetEvolutionManager.foods())if(f.id.equals(id)){PetEvolutionManager.feed(a,f);RewardInventory.completeTask(a,"feed_"+id,2);refreshStats(a,stats);bowl.setText(f.emoji+"\n"+f.name+"\nYum! 🐾");return;}}
    private static boolean requirePet(Activity a){if(!PetEvolutionManager.isHatched(a)){Toast.makeText(a,"🥚 Hatch your pet first.",Toast.LENGTH_SHORT).show();return false;}return true;}
    private static void refreshStats(Activity a,TextView out){PetCareSystem.tick(a);out.setText("❤️ "+PetEvolutionManager.health(a)+"   🍎 "+PetEvolutionManager.hunger(a)+"   😊 "+PetEvolutionManager.happiness(a)+"   🧼 "+PetCareSystem.hygiene(a)+"   🪙 "+RewardInventory.coins(a));}
    private static TextView text(Activity a,String s,float size,boolean bold){TextView v=new TextView(a);v.setText(s);v.setTextSize(size);v.setTextColor(Color.rgb(45,55,45));if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;}
    private static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}
}
