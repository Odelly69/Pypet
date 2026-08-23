package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.view.*;
import android.widget.*;

/** Main living town surface. The map is deliberately road-planned and fills the usable screen. */
public final class LivingWorldView {
    private LivingWorldView() {}

    public static void show(Activity a) {
        final World world = new World(a);
        FrameLayout root = new FrameLayout(a);
        root.setBackgroundColor(Color.rgb(124,184,99));
        root.addView(world, new FrameLayout.LayoutParams(-1,-1));

        TextView title = new TextView(a);
        title.setText("🌎 " + PypetProfileManager.townName(a) + " • PyPet WORLD");
        title.setTextSize(20); title.setGravity(Gravity.CENTER);
        title.setBackgroundColor(Color.argb(235,250,247,232));
        root.addView(title,new FrameLayout.LayoutParams(-1,48,Gravity.TOP));

        TextView help = new TextView(a);
        help.setText("Pinch = zoom whole world • drag = move • tap a building = activity");
        help.setTextSize(12); help.setGravity(Gravity.CENTER);
        help.setBackgroundColor(Color.argb(225,250,247,232));
        FrameLayout.LayoutParams hp=new FrameLayout.LayoutParams(-1,38,Gravity.TOP); hp.topMargin=48; root.addView(help,hp);

        LinearLayout controls=new LinearLayout(a); controls.setGravity(Gravity.CENTER); controls.setPadding(5,3,5,3); controls.setBackgroundColor(Color.argb(235,250,247,232));
        Button out=btn(a,"−"), reset=btn(a,"🌎 Reset"), in=btn(a,"+"), profile=btn(a,"👤");
        controls.addView(out,new LinearLayout.LayoutParams(60,58)); controls.addView(reset,new LinearLayout.LayoutParams(0,58,1)); controls.addView(in,new LinearLayout.LayoutParams(60,58)); controls.addView(profile,new LinearLayout.LayoutParams(70,58));
        root.addView(controls,new FrameLayout.LayoutParams(-1,64,Gravity.BOTTOM));
        out.setOnClickListener(v->world.zoomBy(.86f)); in.setOnClickListener(v->world.zoomBy(1.16f)); reset.setOnClickListener(v->world.reset()); profile.setOnClickListener(v->safeProfile(a));
        a.setContentView(root);
    }

    private static Button btn(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}
    private static void safeProfile(Activity a){try{PypetProfileView.show(a);}catch(Throwable t){new AlertDialog.Builder(a).setMessage("Profile is temporarily unavailable.").setPositiveButton("OK",null).show();}}

    private static final class World extends View {
        private static final float W=2400,H=3600,MIN=.55f,MAX=3.0f;
        private final Activity a; private final Paint p=new Paint(3),t=new Paint(3);
        private float zoom=1f,ox=0,oy=0,lastX,lastY,downX,downY,lastDist; private boolean moved;
        private final RectF[] b=new RectF[6];
        private final String[] names={"HOME","PYTHON ACADEMY","MARKET","WORKSHOP","PARK","LIBRARY"};
        World(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void reset(){zoom=1f;ox=oy=0;invalidate();}
        void zoomBy(float m){zoom=Math.max(MIN,Math.min(MAX,zoom*m));invalidate();}

        @Override protected void onDraw(Canvas c){
            // Fill the entire usable viewport first; the town is never a small island in blue space.
            c.drawColor(Color.rgb(124,184,99));
            float vw=getWidth(), vh=getHeight();
            // Cover the viewport, then crop the outer map edges naturally on tall/narrow phones.
            float fit=Math.max(vw/W,vh/H)*zoom;
            c.save(); c.translate(vw/2f+ox,vh/2f+oy); c.scale(fit,fit); drawWorld(c); c.restore();
            postInvalidateDelayed(160);
        }

        private void drawWorld(Canvas c){
            // Grass/map surface is intentionally larger than the viewport so no blue gutters appear.
            p.setColor(Color.rgb(124,184,99)); c.drawRect(-W/2,-H/2,W/2,H/2,p);
            drawRoads(c);
            drawLots(c);
            drawPark(c,-1080,300,-250,1280,4);
            drawTownBuildings(c);
            drawTrees(c);
            drawTrophies(c); drawPlacements(c); drawPet(c);
            t.setTextAlign(Paint.Align.CENTER); t.setTextSize(48); t.setColor(Color.rgb(43,70,45));
            c.drawText(PypetProfileManager.townName(a),0,-1640,t);
            t.setTextSize(24); c.drawText("Your accomplishments live here",0,-1570,t);
        }

        private void drawRoads(Canvas c){
            // Two connected streets with sidewalks; buildings are deliberately placed beside them.
            p.setColor(Color.rgb(205,197,168));
            c.drawRect(-145,-H/2,145,H/2,p); c.drawRect(-W/2,-145,W/2,145,p);
            p.setColor(Color.rgb(61,61,59));
            c.drawRect(-88,-H/2,88,H/2,p); c.drawRoundRect(-W/2,-88,W/2,88,25,25,p);
            p.setColor(Color.rgb(228,219,182)); p.setStrokeWidth(5);
            for(int y=-1600;y<=1600;y+=380){c.drawLine(-35,y,35,y,p);}
            for(int x=-1000;x<=1000;x+=380){c.drawLine(x,-35,x+65,-35,p);}
        }

        private void drawLots(Canvas c){
            p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(5); p.setColor(Color.argb(80,255,255,255));
            // Visible lot boundaries make the town planning intentional rather than random.
            lot(c,-1080,-1120,-180,-180); lot(c,180,-1120,1080,-180);
            lot(c,-1080,180,-180,1050); lot(c,180,180,1080,1050); p.setStyle(Paint.Style.FILL);
        }
        private void lot(Canvas c,float l,float top,float r,float bot){c.drawRoundRect(l,top,r,bot,25,25,p);}

        private void drawTownBuildings(Canvas c){
            // Each building has a short frontage/driveway toward the road, never sits on the asphalt.
            house(c,-1010,-1050,-550,-670,"HOME",Color.rgb(157,78,61),0);
            academy(c,550,-1050,1010,-650,1);
            house(c,550,190,1010,540,"MARKET",Color.rgb(205,136,55),2);
            house(c,-1010,190,-550,540,"WORKSHOP",Color.rgb(48,116,137),3);
            house(c,550,650,1010,1020,"LIBRARY",Color.rgb(99,70,130),5);
            // Park occupies the lower-left lot and has its own entrance facing the cross street.
        }

        private void house(Canvas c,float l,float top,float r,float bot,String n,int roof,int i){
            b[i]=new RectF(l,top,r,bot); float m=(l+r)/2,ww=r-l,hh=bot-top;
            p.setShadowLayer(10,0,5,Color.argb(60,0,0,0)); p.setColor(Color.rgb(244,238,221)); c.drawRoundRect(l,top,r,bot,18,18,p); p.clearShadowLayer();
            p.setColor(roof); Path q=new Path(); q.moveTo(l-15,top+6); q.lineTo(m,top-hh*.27f); q.lineTo(r+15,top+6); q.close(); c.drawPath(q,p);
            window(c,l+ww*.12f,top+hh*.22f,ww*.20f,hh*.18f); window(c,r-ww*.32f,top+hh*.22f,ww*.20f,hh*.18f);
            p.setColor(Color.rgb(102,74,53)); c.drawRoundRect(m-20,bot-58,m+20,bot,6,6,p);
            // Driveway/front path points toward the nearest street.
            p.setColor(Color.rgb(215,203,173)); if(bot<0)c.drawRect(m-18,bot,m+18,bot+70,p); else c.drawRect(m-18,top-70,m+18,top,p);
            t.setTextSize(17);t.setColor(Color.rgb(45,52,47));c.drawText(n,m,top-hh*.30f,t);
        }

        private void academy(Canvas c,float l,float top,float r,float bot,int i){
            b[i]=new RectF(l,top,r,bot); float m=(l+r)/2,ww=r-l,hh=bot-top;
            p.setColor(Color.rgb(237,235,220));c.drawRoundRect(l,top,r,bot,18,18,p); p.setColor(Color.rgb(61,91,142));
            Path q=new Path();q.moveTo(l-12,top+5);q.lineTo(m,top-hh*.27f);q.lineTo(r+12,top+5);q.close();c.drawPath(q,p);
            for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+22+col*(ww-44)/5,top+30+row*65,25,32);
            p.setColor(Color.rgb(100,70,50));c.drawRoundRect(m-20,bot-58,m+20,bot,6,6,p);
            p.setColor(Color.rgb(215,203,173));c.drawRect(m-18,bot,m+18,bot+70,p);
            t.setTextSize(17);t.setColor(Color.rgb(43,52,57));c.drawText("PYTHON ACADEMY",m,top-hh*.30f,t);
        }

        private void window(Canvas c,float x,float y,float ww,float hh){p.setColor(Color.rgb(91,165,201));c.drawRoundRect(x,y,x+ww,y+hh,4,4,p);p.setColor(Color.rgb(224,234,225));p.setStrokeWidth(2);c.drawLine(x+ww/2,y,x+ww/2,y+hh,p);c.drawLine(x,y+hh/2,x+ww,y+hh/2,p);}

        private void drawPark(Canvas c,float l,float top,float r,float bot,int i){
            b[i]=new RectF(l,top,r,bot); p.setShadowLayer(8,0,4,Color.argb(55,0,0,0));p.setColor(Color.rgb(91,169,89));c.drawRoundRect(l,top,r,bot,36,36,p);p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(12);p.setColor(Color.rgb(235,224,181));c.drawRoundRect(l+18,top+18,r-18,bot-18,28,28,p);p.setStyle(Paint.Style.FILL);
            // Paths
            p.setColor(Color.rgb(222,208,169));c.drawRoundRect(l+65,top+390,r-65,top+455,20,20,p);c.drawRoundRect(l+380,top+65,l+445,bot-65,20,20,p);
            // Pond
            p.setColor(Color.rgb(61,163,199));c.drawOval(l+75,bot-300,l+345,bot-75,p);p.setColor(Color.rgb(153,216,231));c.drawOval(l+120,bot-260,l+255,bot-175,p);
            // Playground + slide
            p.setColor(Color.rgb(194,113,63));c.drawRoundRect(r-315,top+110,r-105,top+285,18,18,p);p.setColor(Color.rgb(92,75,59));c.drawRect(r-295,top+165,r-280,top+335,p);c.drawRect(r-175,top+165,r-160,top+335,p);p.setStrokeWidth(9);c.drawLine(r-290,top+115,r-165,top+115,p);
            // Benches, trees, flowers
            bench(c,l+120,top+330);bench(c,l+520,top+510);for(int j=0;j<5;j++)tree(c,l+105+j*150,top+90+(j%2)*610,42);for(int j=0;j<8;j++)flower(c,l+100+j*90,top+665+(j%2)*30);
            // Gate facing the horizontal road + big sign.
            p.setColor(Color.rgb(118,82,50));c.drawRect((l+r)/2-75,bot-45,(l+r)/2-55,bot+15,p);c.drawRect((l+r)/2+55,bot-45,(l+r)/2+75,bot+15,p);c.drawRect((l+r)/2-75,bot-48,(l+r)/2+75,bot-30,p);
            p.setColor(Color.rgb(244,231,180));c.drawRoundRect((l+r)/2-135,top+28,(l+r)/2+135,top+105,12,12,p);t.setTextSize(28);t.setColor(Color.rgb(48,95,48));c.drawText("🌳 PARK",(l+r)/2,top+79,t);
        }
        private void bench(Canvas c,float x,float y){p.setColor(Color.rgb(118,78,47));c.drawRoundRect(x-65,y,x+65,y+18,5,5,p);c.drawRect(x-48,y+18,x-35,y+48,p);c.drawRect(x+35,y+18,x+48,y+48,p);}
        private void tree(Canvas c,float x,float y,float r){p.setColor(Color.rgb(113,76,49));c.drawRect(x-8,y+r*.35f,x+8,y+r*1.2f,p);p.setColor(Color.rgb(47,127,57));c.drawCircle(x,y,r,p);p.setColor(Color.rgb(78,154,75));c.drawCircle(x-r*.35f,y-r*.2f,r*.55f,p);}
        private void flower(Canvas c,float x,float y){p.setColor(Color.rgb(67,143,65));c.drawRect(x-2,y,x+2,y+25,p);p.setColor(Color.rgb(235,113,139));c.drawCircle(x,y,7,p);p.setColor(Color.rgb(248,216,86));c.drawCircle(x,y,3,p);}
        private void drawTrees(Canvas c){for(int i=-5;i<=5;i++)tree(c,i*280,-330+i*115,31);}
        private void drawTrophies(Canvas c){int k=0;for(PypetAchievementManager.Trophy tr:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,tr.id))continue;float x=-1050+(k%8)*300,y=1350+(k/8)*100;p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-22,y-30,x+22,y+14,7,7,p);p.setColor(Color.rgb(255,239,159));c.drawCircle(x,y-10,14,p);t.setTextSize(12);t.setColor(Color.WHITE);c.drawText(tr.name,x,y+32,t);k++;}}
        private void drawPlacements(Canvas c){for(WorldPlacementManager.Placement q:WorldPlacementManager.all(a)){c.save();c.translate(q.x,q.y);c.rotate(q.rotation);c.scale(q.scale,q.scale);p.setColor(Color.rgb(196,151,82));c.drawRoundRect(-30,-22,30,22,7,7,p);c.restore();}}
        private void drawPet(Canvas c){PetWorldAI.State s=PetWorldAI.tick(a,-1100,1100,-1500,1450);float x=s.x,y=s.y;String species=PetEvolutionManager.current(a).displayName.toLowerCase();if(species.contains("frog"))frog(c,x,y);else generic(c,x,y);t.setTextSize(18);t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+82,t);}
        private void frog(Canvas c,float x,float y){p.setColor(Color.rgb(76,164,70));c.drawOval(x-34,y-9,x+34,y+44,p);c.drawOval(x-30,y+34,x-11,y+65,p);c.drawOval(x+11,y+34,x+30,y+65,p);p.setColor(Color.rgb(93,190,80));c.drawCircle(x-21,y-16,15,p);c.drawCircle(x+21,y-16,15,p);p.setColor(Color.WHITE);c.drawCircle(x-21,y-17,7,p);c.drawCircle(x+21,y-17,7,p);p.setColor(Color.BLACK);c.drawCircle(x-21,y-17,3,p);c.drawCircle(x+21,y-17,3,p);}
        private void generic(Canvas c,float x,float y){p.setColor(Color.rgb(181,139,94));c.drawOval(x-30,y-9,x+30,y+43,p);c.drawCircle(x,y-15,28,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(x-10,y-17,4,p);c.drawCircle(x+10,y-17,4,p);}

        @Override public boolean onTouchEvent(MotionEvent e){
            if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0)zoom=Math.max(MIN,Math.min(MAX,zoom*d/lastDist));lastDist=d;return true;}lastDist=0;
            switch(e.getActionMasked()){
                case MotionEvent.ACTION_DOWN:lastX=downX=e.getX();lastY=downY=e.getY();moved=false;return true;
                case MotionEvent.ACTION_MOVE:float dx=e.getX()-lastX,dy=e.getY()-lastY;if(Math.abs(e.getX()-downX)>10||Math.abs(e.getY()-downY)>10)moved=true;ox+=dx;oy+=dy;lastX=e.getX();lastY=e.getY();invalidate();return true;
                case MotionEvent.ACTION_UP:if(!moved)tap(e.getX(),e.getY());return true;
            }return true;
        }
        private float dist(MotionEvent e){float dx=e.getX(1)-e.getX(0),dy=e.getY(1)-e.getY(0);return(float)Math.sqrt(dx*dx+dy*dy);}
        private void tap(float sx,float sy){float fit=Math.max(getWidth()/(float)W,getHeight()/(float)H)*zoom;float x=(sx-getWidth()/2f-ox)/fit,y=(sy-getHeight()/2f-oy)/fit;for(int i=0;i<b.length;i++)if(b[i]!=null&&b[i].contains(x,y)){invokeSafe(names[i]);return;}PetWorldAI.State s=PetWorldAI.tick(a,-1100,1100,-1500,1450);if(Math.hypot(x-s.x,y-s.y)<110)petEvent();}
        private void invokeSafe(String building){try{BuildingEventManager.open(a,building);}catch(Throwable t){android.util.Log.e("PYPET","Building activity failed: "+building,t);Toast.makeText(a,building+" activity is temporarily unavailable.",Toast.LENGTH_SHORT).show();}}
        private void petEvent(){new AlertDialog.Builder(a).setTitle("🐾 "+PetEvolutionManager.name(a)).setMessage("Your pet is wandering independently. Let it explore, then care for it when its needs call for you.").setPositiveButton("OK",null).show();}
    }
}
