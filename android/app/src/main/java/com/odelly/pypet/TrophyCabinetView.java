package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;

/** Fancy locally-rendered HD-style trophy gallery. Calm, static presentation with no flashing. */
public final class TrophyCabinetView {
    private TrophyCabinetView() {}

    public static void show(Activity a) {
        LinearLayout root = new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL);
        TextView title = new TextView(a); title.setText("🏆 Trophy Hall"); title.setTextSize(28); title.setGravity(Gravity.CENTER); title.setTextColor(Color.rgb(69,52,102)); title.setPadding(8,18,8,4); root.addView(title);
        TextView sub = new TextView(a); sub.setText("Your achievements are displayed here permanently. Earned trophies become full-color display pieces."); sub.setGravity(Gravity.CENTER); sub.setPadding(18,0,18,12); root.addView(sub);
        TrophyCanvas gallery = new TrophyCanvas(a); root.addView(gallery,new LinearLayout.LayoutParams(-1,0,1));
        Button close=new Button(a); close.setText("Close Trophy Hall"); close.setAllCaps(false); root.addView(close);
        AlertDialog d=new AlertDialog.Builder(a).setView(root).create(); close.setOnClickListener(v->d.dismiss()); d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.rgb(250,248,255)));d.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels*.96f),(int)(a.getResources().getDisplayMetrics().heightPixels*.88f));}
    }

    private static final class TrophyCanvas extends View {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG), t=new Paint(Paint.ANTI_ALIAS_FLAG); private final Activity a; private final PypetAchievementManager.Trophy[] trophies;
        TrophyCanvas(Activity a){super(a);this.a=a;trophies=PypetAchievementManager.trophies();t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        protected void onDraw(Canvas c){float w=getWidth();float cardW=w*.44f, cardH=Math.max(145,getHeight()*.28f), gap=w*.04f; for(int i=0;i<trophies.length;i++){int col=i%2,row=i/2;float l=gap+col*(cardW+gap),top=12+row*(cardH+18);drawCard(c,l,top,cardW,cardH,trophies[i]);}}
        private void drawCard(Canvas c,float l,float top,float w,float h,PypetAchievementManager.Trophy tr){boolean owned=PypetAchievementManager.hasTrophy(a,tr.id);p.setColor(Color.argb(35,30,20,50));c.drawRoundRect(l+5,top+7,l+w+5,top+h+7,22,22,p);p.setColor(owned?Color.rgb(255,250,226):Color.rgb(235,233,241));c.drawRoundRect(l,top,l+w,top+h,22,22,p);
            float cx=l+w/2, cy=top+58; p.setStyle(Paint.Style.FILL);p.setColor(owned?Color.rgb(247,194,63):Color.rgb(150,150,160));Path shield=new Path();shield.moveTo(cx-30,cy-28);shield.lineTo(cx+30,cy-28);shield.lineTo(cx+25,cy+15);shield.quadTo(cx,cy+40,cx-25,cy+15);shield.close();c.drawPath(shield,p);p.setColor(owned?Color.rgb(255,232,135):Color.rgb(195,195,202));c.drawCircle(cx,cy-3,17,p);p.setColor(owned?Color.rgb(255,247,205):Color.rgb(225,225,230));c.drawCircle(cx,cy-3,10,p);
            t.setTextAlign(Paint.Align.CENTER);t.setColor(owned?Color.rgb(86,63,22):Color.rgb(100,100,110));t.setTextSize(14);c.drawText(owned?tr.name:"Locked Trophy",cx,top+100,t);t.setTextSize(10);t.setColor(Color.rgb(85,82,94));String desc=owned?tr.description:"Complete the achievement to display this trophy.";if(desc.length()>48)desc=desc.substring(0,45)+"...";c.drawText(desc,cx,top+118,t);t.setTextSize(9);t.setColor(Color.rgb(111,91,40));c.drawText("+"+tr.coins+" Pypet Coins",cx,top+h-12,t);
        }
    }
}
