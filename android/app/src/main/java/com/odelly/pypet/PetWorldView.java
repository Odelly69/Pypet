package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;

/** Illustrated Pypet world with tappable landmarks and a dedicated Trophy Hall. */
public final class PetWorldView {
    private PetWorldView() {}
    public static void show(Activity a) {
        WorldCanvas world=new WorldCanvas(a);
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);
        TextView title=new TextView(a);title.setText("🐾 "+PetEvolutionManager.name(a)+"'s World");title.setTextSize(25);title.setGravity(Gravity.CENTER);title.setTextColor(Color.rgb(69,52,102));title.setPadding(8,12,8,5);root.addView(title);
        TextView hint=new TextView(a);hint.setText("Tap a building or area • Python learning grows the world");hint.setGravity(Gravity.CENTER);hint.setTextSize(14);root.addView(hint);
        root.addView(world,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout buttons=new LinearLayout(a);buttons.setOrientation(LinearLayout.HORIZONTAL);
        Button trophies=new Button(a);trophies.setText("🏆 Trophy Hall");trophies.setAllCaps(false);Button close=new Button(a);close.setText("Back");close.setAllCaps(false);buttons.addView(trophies,new LinearLayout.LayoutParams(0,-2,1));buttons.addView(close,new LinearLayout.LayoutParams(0,-2,1));root.addView(buttons);
        AlertDialog d=new AlertDialog.Builder(a).setView(root).create();trophies.setOnClickListener(v->TrophyCabinetView.show(a));close.setOnClickListener(v->d.dismiss());d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));d.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels*.96f),(int)(a.getResources().getDisplayMetrics().heightPixels*.90f));}
    }
    private static final class WorldCanvas extends View {
        private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG),t=new Paint(Paint.ANTI_ALIAS_FLAG);private final Activity a;private final RectF[] z=new RectF[8];private float petX=.50f,petY=.61f;
        private final String[] names={"Home","Pypet Academy","Hatchery","Garden","Play Park","Library","Workshop","Market"};
        WorldCanvas(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();
            p.setColor(Color.rgb(205,233,255));c.drawRect(0,0,w,h*.45f,p);p.setColor(Color.rgb(126,193,116));Path hill=new Path();hill.moveTo(0,h*.48f);hill.quadTo(w*.25f,h*.34f,w*.52f,h*.48f);hill.quadTo(w*.78f,h*.32f,w,h*.47f);hill.lineTo(w,h);hill.lineTo(0,h);hill.close();c.drawPath(hill,p);
            p.setColor(Color.rgb(255,211,94));c.drawCircle(w*.84f,h*.12f,w*.055f,p);cloud(c,w*.15f,h*.13f,.055f);cloud(c,w*.55f,h*.08f,.045f);
            p.setColor(Color.rgb(232,201,145));Path path=new Path();path.moveTo(w*.50f,h*.55f);path.cubicTo(w*.42f,h*.66f,w*.58f,h*.76f,w*.50f,h);c.drawPath(path,p);
            p.setColor(Color.rgb(109,191,213));Path creek=new Path();creek.moveTo(w*.03f,h*.73f);creek.cubicTo(w*.27f,h*.66f,w*.65f,h*.84f,w*.98f,h*.72f);creek.lineTo(w*.98f,h*.81f);creek.cubicTo(w*.65f,h*.93f,w*.28f,h*.76f,w*.03f,h*.82f);creek.close();c.drawPath(creek,p);
            tree(c,w*.08f,h*.54f,.045f);tree(c,w*.91f,h*.52f,.05f);tree(c,w*.26f,h*.88f,.055f);tree(c,w*.78f,h*.88f,.05f);
            zone(c,0,w*.05f,h*.51f,w*.25f,h*.70f,Color.rgb(239,167,124),"Home","♥");zone(c,1,w*.32f,h*.48f,w*.54f,h*.62f,Color.rgb(126,151,215),"Academy","λ");zone(c,2,w*.68f,h*.47f,w*.91f,h*.62f,Color.rgb(220,143,179),"Hatchery","E");zone(c,3,w*.08f,h*.83f,w*.27f,h*.97f,Color.rgb(126,184,113),"Garden","✿");zone(c,4,w*.31f,h*.79f,w*.49f,h*.96f,Color.rgb(245,190,84),"Play Park","★");zone(c,5,w*.56f,h*.80f,w*.72f,h*.95f,Color.rgb(174,141,205),"Library","≡");zone(c,6,w*.77f,h*.78f,w*.94f,h*.94f,Color.rgb(125,172,178),"Workshop","W");zone(c,7,w*.40f,h*.60f,w*.62f,h*.70f,Color.rgb(237,143,98),"Market","$");
            float bob=(float)Math.sin(System.currentTimeMillis()/1100.0)*3f;PetEvolutionManager.PetVariant pet=PetEvolutionManager.current(a);float px=w*petX,py=h*petY+bob;p.setColor(Color.argb(55,0,0,0));c.drawOval(px-w*.035f,py+h*.018f,px+w*.035f,py+h*.032f,p);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(Math.max(30,w*.075f));t.setColor(Color.DKGRAY);c.drawText(pet.emoji,px,py,t);t.setTextSize(Math.max(12,w*.032f));c.drawText(PetEvolutionManager.name(a),px,py+h*.045f,t);
            p.setColor(Color.argb(215,255,255,255));c.drawRoundRect(w*.03f,h*.03f,w*.46f,h*.105f,18,18,p);t.setTextAlign(Paint.Align.LEFT);t.setTextSize(Math.max(11,w*.026f));t.setColor(Color.DKGRAY);c.drawText("Development "+PetEvolutionManager.balancedDevelopmentScore(a)+"%",w*.055f,h*.075f,t);postInvalidateDelayed(120);
        }
        private void zone(Canvas c,int i,float l,float top,float r,float b,int color,String label,String icon){z[i]=new RectF(l,top,r,b);p.setColor(Color.argb(45,0,0,0));c.drawRoundRect(l+4,top+6,r+4,b+6,18,18,p);p.setColor(color);c.drawRoundRect(l,top,r,b,18,18,p);p.setColor(Color.argb(55,255,255,255));c.drawRoundRect(l+5,top+5,r-5,top+(b-top)*.30f,13,13,p);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(Math.max(18,getWidth()*.045f));t.setColor(Color.WHITE);c.drawText(icon,(l+r)/2,top+(b-top)*.53f,t);t.setTextSize(Math.max(10,getWidth()*.024f));c.drawText(label,(l+r)/2,b-8,t);}
        private void tree(Canvas c,float x,float y,float s){p.setColor(Color.rgb(116,76,48));c.drawRect(x-s*.16f,y,x+s*.16f,y+s*1.5f,p);p.setColor(Color.rgb(65,145,82));c.drawCircle(x,y-s*.25f,s,p);c.drawCircle(x-s*.55f,y,s*.65f,p);c.drawCircle(x+s*.55f,y,s*.65f,p);}
        private void cloud(Canvas c,float x,float y,float s){p.setColor(Color.argb(190,255,255,255));c.drawCircle(x,y,s,p);c.drawCircle(x+s*.7f,y+s*.1f,s*.72f,p);c.drawCircle(x-s*.7f,y+s*.12f,s*.62f,p);}
        public boolean onTouchEvent(MotionEvent e){if(e.getAction()!=MotionEvent.ACTION_UP)return true;float x=e.getX(),y=e.getY();for(int i=0;i<z.length;i++)if(z[i]!=null&&z[i].contains(x,y)){visit(i);return true;}petX=Math.max(.12f,Math.min(.88f,x/getWidth()));petY=Math.max(.15f,Math.min(.72f,y/getHeight()));invalidate();return true;}
        private void visit(int i){String m;switch(i){case 0:m="Care, rest, decorate and watch your pet grow.";break;case 1:m="Write real Python lessons and turn what you learn into world development.";break;case 2:m="Hatch a randomized egg and discover another lineage.";break;case 3:m="Explore and collect peaceful cosmetic world items.";break;case 4:m="Play activities improve development and happiness.";break;case 5:m="Explore Python knowledge and unlock advanced lessons.";break;case 6:m="Build and place your collected world decorations.";break;default:m="Spend Pypet Coins on optional cosmetic world items.";}new AlertDialog.Builder(a).setTitle(names[i]).setMessage(m).setPositiveButton("OK",null).show();}
    }
}
