package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

/** Player-facing town builder. Expansion and placed objects persist in the World. */
public final class WorldBuilderView {
    private WorldBuilderView() {}

    public static void attachToWorld(Activity a){
        FrameLayout content=a.findViewById(android.R.id.content);
        if(content==null)return;
        if(content.findViewWithTag("pypet_build_button")!=null)return;
        Button build=button(a,"🏗 BUILD");
        build.setTag("pypet_build_button");
        FrameLayout.LayoutParams lp=new FrameLayout.LayoutParams(150,58,Gravity.TOP|Gravity.END);
        lp.topMargin=86;lp.rightMargin=8;
        content.addView(build,lp);
        build.setOnClickListener(v->show(a));
    }

    public static void show(Activity a){
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(20,18,20,18);root.setBackgroundColor(Color.rgb(245,241,222));
        TextView title=text(a,"🏗 BUILD YOUR PyPet WORLD",25,true);title.setGravity(Gravity.CENTER);root.addView(title);
        TextView info=text(a,"Your town is persistent. Expand districts, then place accomplishments, parks, gardens and landmarks. Every placement is checked against roads and existing town structures.",16,false);info.setPadding(0,12,0,14);root.addView(info);
        TextView status=text(a,WorldExpansionManager.status(a)+"\nCurrent size: "+(int)WorldExpansionManager.halfSize(a)*2+" × "+(int)WorldExpansionManager.halfSize(a)*2+" map units\nCoins: "+RewardInventory.coins(a),15,true);status.setPadding(12,12,12,12);status.setBackgroundColor(Color.rgb(224,235,214));root.addView(status);

        Button expand=button(a,WorldExpansionManager.canExpand(a)?"🗺 EXPAND TOWN — UNLOCK NEXT DISTRICT":"🗺 TOWN FULL");root.addView(expand);expand.setEnabled(WorldExpansionManager.canExpand(a));
        expand.setOnClickListener(v->{if(WorldExpansionManager.expand(a)){status.setText(WorldExpansionManager.status(a)+"\nCurrent size: "+(int)WorldExpansionManager.halfSize(a)*2+" × "+(int)WorldExpansionManager.halfSize(a)*2+" map units\nCoins: "+RewardInventory.coins(a));Toast.makeText(a,"🎉 New district unlocked! More land is now buildable.",Toast.LENGTH_SHORT).show();} });

        TextView section=text(a,"BUILD / PLACE",18,true);section.setPadding(0,18,0,6);root.addView(section);
        addBuild(root,a,"🏠 HOME LOT","house",760,760);
        addBuild(root,a,"🏪 MARKET LOT","market_building",820,-760);
        addBuild(root,a,"🔧 WORKSHOP LOT","workshop_building",-760,760);
        addBuild(root,a,"🌳 PARK / GARDEN","garden",-760,-760);
        addBuild(root,a,"⛲ FOUNTAIN","fountain",-520,820);
        addBuild(root,a,"🏰 LANDMARK","castle",520,-820);
        addBuild(root,a,"🌷 FLOWER BED","flower_bed",-520,-820);
        addBuild(root,a,"🌲 TREE","tree",520,820);
        addBuild(root,a,"🏆 TROPHY DISPLAY","trophy_display",-680,900);

        TextView note=text(a,"Placement is automatic and collision-safe: if the selected spot is on a road, sidewalk conflict, park-restricted area, or building, PyPet finds the nearest legal location instead.",14,false);note.setPadding(0,14,0,10);root.addView(note);
        Button back=button(a,"🌎 RETURN TO WORLD");root.addView(back);back.setOnClickListener(v->{LivingWorldView.show(a);attachToWorld(a);});
        a.setContentView(root);
    }

    private static void addBuild(LinearLayout root,Activity a,String label,String id,float x,float y){
        Button b=button(a,label+"  •  BUILD");root.addView(b);b.setOnClickListener(v->{
            WorldPlacementManager.place(a,id,x,y,1f,0f);
            Toast.makeText(a,"✅ "+label+" added at the nearest safe buildable lot.",Toast.LENGTH_SHORT).show();
        });
    }
    private static TextView text(Activity a,String s,float size,boolean bold){TextView v=new TextView(a);v.setText(s);v.setTextSize(size);v.setTextColor(Color.rgb(40,54,43));if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);return v;}
    private static Button button(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}
}
