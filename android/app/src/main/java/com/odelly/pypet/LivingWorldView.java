package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.view.*;
import android.widget.*;

/** Main living town surface with a full-screen, road-connected world. */
public final class LivingWorldView {
    private LivingWorldView() {}
    public static void show(Activity a){
        final World world=new World(a); FrameLayout root=new FrameLayout(a); root.addView(world,new FrameLayout.LayoutParams(-1,-1));
        TextView title=new TextView(a); title.setText("🌎 "+PypetProfileManager.townName(a)+" • PYPET WORLD"); title.setTextSize(20); title.setGravity(Gravity.CENTER); title.setBackgroundColor(Color.argb(235,250,247,232)); FrameLayout.LayoutParams tp=new FrameLayout.LayoutParams(-1,48,Gravity.TOP); root.addView(title,tp);
        TextView help=new TextView(a); help.setText("Pinch = zoom whole world • drag = move • tap a building = activity"); help.setTextSize(12); help.setGravity(Gravity.CENTER); help.setBackgroundColor(Color.argb(225,250,247,232)); FrameLayout.LayoutParams hp=new FrameLayout.LayoutParams(-1,38,Gravity.TOP); hp.topMargin=48; root.addView(help,hp);
        LinearLayout controls=new LinearLayout(a); controls.setGravity(Gravity.CENTER); controls.setPadding(5,3,5,3); controls.setBackgroundColor(Color.argb(235,250,247,232)); Button out=btn(a,"−"),reset=btn(a,"🌎 Reset"),in=btn(a,"+"),profile=btn(a,"👤"); controls.addView(out,new LinearLayout.LayoutParams(60,58)); controls.addView(reset,new LinearLayout.LayoutParams(0,58,1)); controls.addView(in,new LinearLayout.LayoutParams(60,58)); controls.addView(profile,new LinearLayout.LayoutParams(70,58)); root.addView(controls,new FrameLayout.LayoutParams(-1,64,Gravity.BOTTOM)); out.setOnClickListener(v->world.zoomBy(.86f)); in.setOnClickListener(v->world.zoomBy(1.16f)); reset.setOnClickListener(v->world.reset()); profile.setOnClickListener(v->safeProfile(a)); a.setContentView(root);
    }
    private static Button btn(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}
    private static void safeProfile(Activity a){try{PypetProfileView.show(a);}catch(Throwable t){new AlertDialog.Builder(a).setMessage("Profile is temporarily unavailable.").setPositiveButton("OK",null).show();}}
    private static final class World extends View {
        private static final float W=3200,H=4200,MIN=.34f,MAX=2.8f; private final Activity a; private final Paint p=new Paint(3),t=new Paint(3); private float zoom=1f,ox=0,oy=0,lastX,lastY,downX,downY,lastDist; private boolean moved; private final RectF[] b=new RectF[6]; private final String[] names={"HOME","PYTHON ACADEMY","MARKET","WORKSHOP","PARK","LIBRARY"};
        World(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);} void reset(){zoom=1f;ox=oy=0;invalidate();} void zoomBy(float m){zoom=Math.max(MIN,Math.min(MAX,zoom*m));invalidate();}
        @Override protected void onDraw(Canvas c){float fit=Math.max(getWidth()/W,getHeight()/H)*1.01f*zoom;c.drawColor(Color.rgb(137,203,230));c.save();c.translate(getWidth()/2f+ox,getHeight()/2f+oy);c.scale(fit,fit);drawWorld(c);c.restore();postInvalidateDelayed(120);}
        private void drawWorld(Canvas c){
            p.setColor(Color.rgb(124,184,99));c.drawRect(-W/2,-H/2,W/2,H/2,p);
            // Sidewalks + connected roads. Buildings are intentionally beside, never on, the roads.
            p.setColor(Color.rgb(202,195,165));c.drawRect(-135,-H/2,135,H/2,p);c.drawRect(-W/2,-135,W/2,135,p);
            p.setColor(Color.rgb(62,62,60));c.drawRect(-82,-H/2,82,H/2,p);c.drawRoundRect(-W/2,-82,W/2,82,25,25,p);
            p.setColor(Color.rgb(230,222,187));p.setStrokeWidth(5);for(int y=-1800;y<=1800;y+=420)c.drawLine(-35,y,35,y,p);for(int x=-1400;x<=1400;x+=420)c.drawLine(x,-35,x+70,-35,p);
            house(c,-760,-1380,-340,-1040,"HOME",Color.rgb(157,78,61),0);
            academy(c,260,-1420,760,-1020,1);
            house(c,270,-520,720,-170,"MARKET",Color.rgb(205,136,55),2);
            house(c,-760,-520,-300,-170,"WORKSHOP",Color.rgb(48,116,137),3);
            park(c,-1120,430,-250,1270,4);
            house(c,300,900,740,1260,"LIBRARY",Color.rgb(99,70,130),5);
            trees(c);drawTrophies(c);drawPlacements(c);drawPet(c);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(48);t.setColor(Color.rgb(43,70,45));c.drawText(PypetProfileManager.townName(a),0,-1830,t);t.setTextSize(24);c.drawText("Your accomplishments live here",0,-1760,t);
        }
        private void house(Canvas c,float l,float top,float r,float bot,String n,int roof,int i){b[i]=new RectF(l,top,r,bot);float m=(l+r)/2,ww=r-l,hh=bot-top;p.setShadowLayer(10,0,5,Color.argb(60,0,0,0));p.setColor(Color.rgb(244,238,221));c.drawRoundRect(l,top,r,bot,18,18,p);p.clearShadowLayer();p.setColor(roof);Path q=new Path();q.moveTo(l-15,top+6);q.lineTo(m,top-hh*.27f);q.lineTo(r+15,top+6);q.close();c.drawPath(q,p);window(c,l+ww*.12f,top+hh*.22f,ww*.20f,hh*.18f);window(c,r-ww*.32f,top+hh*.22f,ww*.20f,hh*.18f);p.setColor(Color.rgb(102,74,53));c.drawRoundRect(m-20,bot-58,m+20,bot,6,6,p);t.setTextSize(17);t.setColor(Color.rgb(45,52,47));c.drawText(n,m,top-hh*.30f,t);}
        private void academy(Canvas c,float l,float top,float r,float bot,int i){b[i]=new RectF(l,top,r,bot);float m=(l+r)/2,ww=r-l,hh=bot-top;p.setColor(Color.rgb(237,235,220));c.drawRoundRect(l,top,r,bot,18,18,p);p.setColor(Color.rgb(61,91,142));Path q=new Path();q.moveTo(l-12,top+5);q.lineTo(m,top-hh*.27f);q.lineTo(r+12,top+5);q.close();c.drawPath(q,p);for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+22+col*(ww-44)/5,top+30+row*65,25,32);p.setColor(Color.rgb(100,70,50));c.drawRoundRect(m-20,bot-58,m+20,bot,6,6,p);t.setTextSize(17);t.setColor(Color.rgb(43,52,57));c.drawText("PYTHON ACADEMY",m,top-hh*.30f,t);}
        private void window(Canvas c,float x,float y,float ww,float hh){p.setColor(Color.rgb(91,165,201));c.drawRoundRect(x,y,x+ww,y+hh,4,4,p);p.setColor(Color.rgb(224,234,225));p.setStrokeWidth(2);c.drawLine(x+ww/2,y,x+ww/2,y+hh,p);c.drawLine(x,y+hh/2,x+ww,y+hh/2,p);}
        private void park(Canvas c,float l,float top,float r,float bot,int i){
            b[i]=new RectF(l,top,r,bot);p.setShadowLayer(8,0,4,Color.argb(55,0,0,0));p.setColor(Color.rgb(92,170,91));c.drawRoundRect(l,top,r,bot,34,34,p);p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(10);p.setColor(Color.rgb(235,224,181));c.drawRoundRect(l+20,top+20,r-20,bot-20,26,26,p);p.setStyle(Paint.Style.FILL);
            // Walking paths, pond, playground, benches, trees, flowers and a clear entrance sign make this unmistakably a park.
            p.setColor(Color.rgb(222,208,169));c.drawRoundRect(l+55,top+380,r-55,top+450,18,18,p);c.drawRoundRect(l+370,top+70,l+430,bot-70,18,18,p);
            p.setColor(Color.rgb(66,166,199));c.drawOval(l+70,bot-260,l+330,bot-70,p);p.setColor(Color.rgb(146,216,231));c.drawOval(l+105,bot-225,l+225,bot-150,p);
            p.setColor(Color.rgb(194,113,63));c.drawRoundRect(r-300,top+110,r-115,top+285,18,18,p);p.setColor(Color.rgb(92,75,59));c.drawRect(r-280,top+160,r-265,top+330,p);c.drawRect(r-170,top+160,r-155,top+330,p);p.setStrokeWidth(9);c.drawLine(r-275,top+115,r-160,top+115,p);
            bench(c,l+110,top+330);bench(c,l+530,top+520);
            for(int j=0;j<5;j++)tree(c,l+105+j*145,top+85+(j%2)*610,42);for(int j=0;j<7;j++)flower(c,l+95+j*92,top+650+(j%2)*35);
            p.setColor(Color.rgb(118,82,50));c.drawRect((l+r)/2-75,bot-45,(l+r)/2-55,bot+10,p);c.drawRect((l+r)/2+55,bot-45,(l+r)/2+75,bot+10,p);c.drawRect((l+r)/2-75,bot-48,(l+r)/2+75,bot-30,p);
            p.setColor(Color.rgb(238,226,180));c.drawRoundRect((l+r)/2-115,top+28,(l+r)/2+115,top+100,12,12,p);t.setTextSize(26);t.setColor(Color.rgb(48,95,48));c.drawText("🌳 PARK",(l+r)/2,top+76,t);
        }
        private void bench(Canvas c,float x,float y){p.setColor(Color.rgb(118,78,47));c.drawRoundRect(x-65,y,x+65,y+18,5,5,p);c.drawRect(x-48,y+18,x-35,y+48,p);c.drawRect(x+35,y+18,x+48,y+48,p);}
        private void tree(Canvas c,float x,float y,float r){p.setColor(Color.rgb(113,76,49));c.drawRect(x-8,y+r*.35f,x+8,y+r*1.2f,p);p.setColor(Color.rgb(47,127,57));c.drawCircle(x,y,r,p);p.setColor(Color.rgb(78,154,75));c.drawCircle(x-r*.35f,y-r*.2f,r*.55f,p);}
        private void flower(Canvas c,float x,float y){p.setColor(Color.rgb(67,143,65));c.drawRect(x-2,y,x+2,y+25,p);p.setColor(Color.rgb(235,113,139));c.drawCircle(x,y,7,p);p.setColor(Color.rgb(248,216,86));c.drawCircle(x,y,3,p);}
        private void trees(Canvas c){for(int i=-5;i<=5;i++)tree(c,i*280,-350+i*115,31);}
        private void drawTrophies(Canvas c){int k=0;for(PypetAchievementManager.Trophy tr:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,tr.id))continue;float x=-1150+(k%8)*300,y=1450+(k/8)*100;p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-22,y-30,x+22,y+14,7,7,p);p.setColor(Color.rgb(255,239,159));c.drawCircle(x,y-10,14,p);t.setTextSize(12);t.setColor(Color.WHITE);c.drawText(tr.name,x,y+32,t);k++;}}
        private void drawPlacements(Canvas c){for(WorldPlacementManager.Placement q:WorldPlacementManager.all(a)){c.save();c.translate(q.x,q.y);c.rotate(q.rotation);c.scale(q.scale,q.scale);p.setColor(Color.rgb(196,151,82));c.drawRoundRect(-30,-22,30,22,7,7,p);c.restore();}}
        private void drawPet(Canvas c){PetWorldAI.State s=PetWorldAI.tick(a,-1250,1250,-1750,1750);float x=s.x,y=s.y;String species=PetEvolutionManager.current(a).displayName.toLowerCase();if(species.contains("frog"))frog(c,x,y);else generic(c,x,y);t.setTextSize(18);t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+82,t);}
        private void frog(Canvas c,float x,float y){p.setColor(Color.rgb(76,164,70));c.drawOval(x-34,y-9,x+34,y+44,p);c.drawOval(x-30,y+34,x-11,y+65,p);c.drawOval(x+11,y+34,x+30,y+65,p);p.setColor(Color.rgb(93,190,80));c.drawCircle(x-21,y-16,15,p);c.drawCircle(x+21,y-16,15,p);p.setColor(Color.WHITE);c.drawCircle(x-21,y-17,7,p);c.drawCircle(x+21,y-17,7,p);p.setColor(Color.BLACK);c.drawCircle(x-21,y-17,3,p);c.drawCircle(x+21,y-17,3,p);}
        private void generic(Canvas c,float x,float y){p.setColor(Color.rgb(181,139,94));c.drawOval(x-30,y-9,x+30,y+43,p);c.drawCircle(x,y-15,28,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(x-10,y-17,4,p);c.drawCircle(x+10,y-17,4,p);}
        @Override public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0)zoom=Math.max(MIN,Math.min(MAX,zoom*d/lastDist));lastDist=d;return true;}lastDist=0;switch(e.getActionMasked()){case MotionEvent.ACTION_DOWN:lastX=downX=e.getX();lastY=downY=e.getY();moved=false;return true;case MotionEvent.ACTION_MOVE:float dx=e.getX()-lastX,dy=e.getY()-lastY;if(Math.abs(e.getX()-downX)>10||Math.abs(e.getY()-downY)>10)moved=true;ox+=dx;oy+=dy;lastX=e.getX();lastY=e.getY();invalidate();return true;case MotionEvent.ACTION_UP:if(!moved)tap(e.getX(),e.getY());return true;}return true;}
        private float dist(MotionEvent e){float dx=e.getX(1)-e.getX(0),dy=e.getY(1)-e.getY(0);return(float)Math.sqrt(dx*dx+dy*dy);}
        private void tap(float sx,float sy){float fit=Math.max(getWidth()/W,getHeight()/H)*1.01f*zoom;float x=(sx-getWidth()/2f-ox)/fit,y=(sy-getHeight()/2f-oy)/fit;for(int i=0;i<b.length;i++)if(b[i]!=null&&b[i].contains(x,y)){buildingEvent(i);return;}PetWorldAI.State s=PetWorldAI.tick(a,-1250,1250,-1750,1750);if(Math.hypot(x-s.x,y-s.y)<110)petEvent();}
        private void buildingEvent(int i){invokeSafe(names[i]);}
        private void petEvent(){new AlertDialog.Builder(a).setTitle("🐾 "+PetEvolutionManager.name(a)).setMessage("Your pet is wandering independently. Let it explore, then care for it when its needs call for you.").setPositiveButton("OK",null).show();}
        private void invokeSafe(String building){try{BuildingEventManager.open(a,building);}catch(Throwable t){android.util.Log.e("PYPET","Building activity failed: "+building,t);Toast.makeText(a,building+" activity is temporarily unavailable.",Toast.LENGTH_SHORT).show();}}
    }
}
