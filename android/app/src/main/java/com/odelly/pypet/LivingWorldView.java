package com.odelly.pypet;

import android.app.Activity;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import java.util.List;

/** Full-screen PyPet town. Land use is explicit: Academy teaches Python; Home cares for pets; Park is recreation. */
public final class LivingWorldView {
    private LivingWorldView() {}

    public static void show(Activity a) {
        final World world=new World(a);
        FrameLayout root=new FrameLayout(a);
        root.setBackgroundColor(Color.rgb(151,196,119));
        root.addView(world,new FrameLayout.LayoutParams(-1,-1));
        TextView title=overlay(a,"🌎 "+PypetProfileManager.townName(a)+" • PyPet WORLD",19);
        root.addView(title,new FrameLayout.LayoutParams(-1,48,Gravity.TOP));
        TextView help=overlay(a,"Pinch to zoom • drag to explore • tap a building to do its activity",12);
        FrameLayout.LayoutParams hp=new FrameLayout.LayoutParams(-1,34,Gravity.TOP);hp.topMargin=48;root.addView(help,hp);
        LinearLayout controls=new LinearLayout(a);controls.setGravity(Gravity.CENTER);controls.setPadding(5,3,5,3);controls.setBackgroundColor(Color.argb(225,250,247,232));
        Button out=btn(a,"−"),reset=btn(a,"🌎 Reset"),in=btn(a,"+"),profile=btn(a,"👤");
        controls.addView(out,new LinearLayout.LayoutParams(58,56));controls.addView(reset,new LinearLayout.LayoutParams(0,56,1));controls.addView(in,new LinearLayout.LayoutParams(58,56));controls.addView(profile,new LinearLayout.LayoutParams(66,56));
        root.addView(controls,new FrameLayout.LayoutParams(-1,62,Gravity.BOTTOM));
        out.setOnClickListener(v->world.zoomBy(.86f));in.setOnClickListener(v->world.zoomBy(1.16f));reset.setOnClickListener(v->world.reset());profile.setOnClickListener(v->PypetProfileView.show(a,false,()->show(a)));
        a.setContentView(root);
    }
    private static TextView overlay(Activity a,String s,float z){TextView v=new TextView(a);v.setText(s);v.setTextSize(z);v.setGravity(Gravity.CENTER);v.setTextColor(Color.rgb(39,53,42));v.setBackgroundColor(Color.argb(225,250,247,232));return v;}
    private static Button btn(Activity a,String s){Button b=new Button(a);b.setText(s);b.setAllCaps(false);return b;}

    private static final class World extends View {
        private static final float W=2400f,H=2400f,MIN=.62f,MAX=3f;
        private final Activity a;private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG),t=new Paint(Paint.ANTI_ALIAS_FLAG);private final RectF[] hit=new RectF[6];
        private final String[] names={"HOME","PYTHON ACADEMY","MARKET","WORKSHOP","PARK","LIBRARY"};
        private float zoom=1f,ox,oy,lastX,lastY,downX,downY,lastDist;private boolean moved;
        World(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void reset(){zoom=1f;ox=oy=0;PetWorldAI.reset(a,260,420);invalidate();}
        void zoomBy(float f){zoom=Math.max(MIN,Math.min(MAX,zoom*f));invalidate();}
        @Override protected void onDraw(Canvas c){c.drawColor(Color.rgb(151,196,119));float fit=Math.max(getWidth()/W,getHeight()/H)*zoom;c.save();c.translate(getWidth()/2f+ox,getHeight()/2f+oy);c.scale(fit,fit);drawWorld(c);c.restore();postInvalidateDelayed(120);}
        private void drawWorld(Canvas c){p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(151,196,119));c.drawRect(-1200,-1200,1200,1200,p);terrain(c);roads(c);lots(c);park(c,-1080,720,-220,1090,4);buildings(c);trees(c);trophies(c);placements(c);pet(c);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(50);t.setColor(Color.rgb(43,70,45));c.drawText(PypetProfileManager.townName(a),0,-1090,t);t.setTextSize(24);c.drawText("Your accomplishments live here",0,-1038,t);}
        private void terrain(Canvas c){p.setColor(Color.rgb(174,207,139));for(int x=-1100;x<=1100;x+=220)for(int y=-1120;y<=1120;y+=260)c.drawCircle(x+((y/260)%2)*35,y,4,p);}
        private void roads(Canvas c){p.setColor(Color.rgb(216,205,177));c.drawRect(-160,-1200,160,1200,p);c.drawRect(-1200,-160,1200,160,p);p.setColor(Color.rgb(58,59,57));c.drawRect(-98,-1200,98,1200,p);c.drawRoundRect(-1200,-98,1200,98,24,24,p);p.setColor(Color.rgb(229,219,181));for(int y=-1120;y<=1120;y+=170)if(Math.abs(y)>135)c.drawRoundRect(-5,y,5,y+92,3,3,p);for(int x=-1120;x<=1120;x+=170)if(Math.abs(x)>135)c.drawRoundRect(x,-5,x+92,5,3,3,p);p.setColor(Color.rgb(190,185,165));c.drawRect(-185,-1200,-160,1200,p);c.drawRect(160,-1200,185,1200,p);c.drawRect(-1200,-185,1200,-160,p);c.drawRect(-1200,160,1200,185,p);}
        private void lots(Canvas c){p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.argb(105,255,255,255));lot(c,-910,-990,-250,-560);lot(c,250,-990,910,-560);lot(c,-910,250,-250,670);lot(c,250,250,910,670);lot(c,-910,720,-250,1090);lot(c,250,720,910,1090);p.setStyle(Paint.Style.FILL);}
        private void lot(Canvas c,float l,float top,float r,float bot){c.drawRoundRect(l,top,r,bot,28,28,p);}
        private void buildings(Canvas c){house(c,420,760,850,1060,"HOME",0,Color.rgb(164,83,63));academy(c,420,-940,850,-620,1);market(c,-850,290,-420,600,2);workshop(c,-850,-940,-420,-620,3);library(c,420,290,850,600,5);}
        private void base(Canvas c,float l,float top,float r,float bot,int roof,String label,int i){hit[i]=new RectF(l,top,r,bot);float m=(l+r)/2,hh=bot-top;p.setShadowLayer(14,0,7,Color.argb(70,0,0,0));p.setColor(Color.rgb(246,241,225));c.drawRoundRect(l,top,r,bot,18,18,p);p.clearShadowLayer();p.setColor(roof);Path q=new Path();q.moveTo(l-18,top+8);q.lineTo(m,top-hh*.28f);q.lineTo(r+18,top+8);q.close();c.drawPath(q,p);door(c,m,bot);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(18);t.setColor(Color.rgb(45,52,47));c.drawText(label,m,top-hh*.31f,t);}
        private void door(Canvas c,float x,float b){p.setColor(Color.rgb(103,74,54));c.drawRoundRect(x-24,b-68,x+24,b,6,6,p);p.setColor(Color.rgb(232,204,117));c.drawCircle(x+11,b-34,3.5f,p);p.setColor(Color.rgb(203,195,171));c.drawRect(x-34,b,x+34,b+9,p);}
        private void window(Canvas c,float x,float y,float w,float h){p.setColor(Color.rgb(71,133,166));c.drawRoundRect(x,y,x+w,y+h,5,5,p);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);p.setColor(Color.rgb(238,241,224));c.drawRect(x,y,x+w,y+h,p);c.drawLine(x+w/2,y,x+w/2,y+h,p);c.drawLine(x,y+h/2,x+w,y+h/2,p);p.setStyle(Paint.Style.FILL);}
        private void house(Canvas c,float l,float top,float r,float bot,String label,int i,int roof){base(c,l,top,r,bot,roof,label,i);window(c,l+55,top+70,70,58);window(c,r-125,top+70,70,58);}
        private void academy(Canvas c,float l,float top,float r,float bot,int i){base(c,l,top,r,bot,Color.rgb(64,95,147),"PYTHON ACADEMY",i);for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+25+col*82,top+38+row*62,48,38);p.setColor(Color.rgb(74,106,150));c.drawRoundRect(l+28,bot-105,r-28,bot-75,10,10,p);t.setTextSize(14);t.setColor(Color.WHITE);c.drawText("CODE • LEARN • BUILD",(l+r)/2,bot-84,t);}
        private void market(Canvas c,float l,float top,float r,float bot,int i){base(c,l,top,r,bot,Color.rgb(209,143,55),"MARKET",i);for(int x=0;x<3;x++)window(c,l+45+x*120,top+60,60,45);p.setColor(Color.rgb(222,83,63));c.drawRect(l+40,bot-105,r-40,bot-80,p);}
        private void workshop(Canvas c,float l,float top,float r,float bot,int i){base(c,l,top,r,bot,Color.rgb(48,116,137),"WORKSHOP",i);for(int x=0;x<2;x++)window(c,l+55+x*180,top+55,75,52);p.setColor(Color.rgb(122,88,55));c.drawRect(l+50,top+135,r-50,top+165,p);for(int x=0;x<3;x++){p.setColor(Color.rgb(216,171,72));c.drawCircle(l+90+x*125,top+118,16,p);}}
        private void library(Canvas c,float l,float top,float r,float bot,int i){base(c,l,top,r,bot,Color.rgb(103,74,137),"LIBRARY",i);for(int x=0;x<3;x++)window(c,l+48+x*112,top+50,64,46);}
        /** Dedicated recreation zone in the lower-left lot, completely separate from every building lot. */
        private void park(Canvas c,float l,float top,float r,float bot,int i){hit[i]=new RectF(l,top,r,bot);p.setShadowLayer(10,0,5,Color.argb(60,0,0,0));p.setColor(Color.rgb(83,166,86));c.drawRoundRect(l,top,r,bot,40,40,p);p.clearShadowLayer();p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(12);p.setColor(Color.rgb(237,224,178));c.drawRoundRect(l+15,top+15,r-15,bot-15,30,30,p);p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(224,210,171));c.drawRoundRect(l+50,top+115,r-50,top+170,18,18,p);c.drawRoundRect(l+330,top+50,l+385,bot-50,18,18,p);bench(c,l+130,top+215);bench(c,l+600,top+215);pond(c,l+85,top+250);playground(c,r-360,top+90);for(int j=0;j<5;j++)tree(c,l+100+j*155,top+70,30);t.setColor(Color.rgb(244,231,180));c.drawRoundRect((l+r)/2-155,top+20,(l+r)/2+155,top+75,12,12,p);t.setTextSize(23);t.setColor(Color.rgb(48,95,48));t.setTextAlign(Paint.Align.CENTER);c.drawText("🌳 PARK • PLAY",(l+r)/2,top+57,t);}
        private void bench(Canvas c,float x,float y){p.setColor(Color.rgb(118,78,47));c.drawRoundRect(x-55,y,x+55,y+15,5,5,p);c.drawRect(x-40,y+15,x-28,y+42,p);c.drawRect(x+28,y+15,x+40,y+42,p);}
        private void pond(Canvas c,float x,float y){p.setColor(Color.rgb(58,155,198));c.drawOval(x,y,x+230,y+110,p);p.setColor(Color.rgb(150,214,232));c.drawOval(x+35,y+25,x+135,y+75,p);}
        private void playground(Canvas c,float x,float y){p.setColor(Color.rgb(210,78,64));c.drawRoundRect(x,y,x+110,y+25,7,7,p);c.drawRect(x+12,y+20,x+25,y+95,p);c.drawRect(x+85,y+20,x+98,y+60,p);p.setColor(Color.rgb(103,73,51));p.setStrokeWidth(8);c.drawLine(x+15,y+120,x+125,y+120,p);c.drawLine(x+25,y+120,x,y+180,p);c.drawLine(x+115,y+120,x+140,y+180,p);p.setColor(Color.rgb(219,116,57));c.drawRoundRect(x+30,y+178,x+90,y+190,5,5,p);}
        private void trees(Canvas c){for(int i=-5;i<=5;i++)tree(c,i*280,-360+i*115,31);}
        private void tree(Canvas c,float x,float y,float r){p.setColor(Color.rgb(113,76,49));c.drawRect(x-7,y+r*.35f,x+7,y+r*1.2f,p);p.setColor(Color.rgb(47,127,57));c.drawCircle(x,y,r,p);p.setColor(Color.rgb(78,154,75));c.drawCircle(x-r*.35f,y-r*.2f,r*.55f,p);}
        private void trophies(Canvas c){int k=0;for(PypetAchievementManager.Trophy tr:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,tr.id))continue;float x=-1050+(k%7)*300,y=1130+(k/7)*80;trophy(c,x,y,tr.name);k++;}}
        private void trophy(Canvas c,float x,float y,String s){p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-24,y-30,x+24,y+12,7,7,p);c.drawCircle(x,y-8,15,p);p.setColor(Color.rgb(117,81,43));c.drawRect(x-5,y+10,x+5,y+31,p);c.drawRect(x-35,y+29,x+35,y+38,p);t.setTextSize(12);t.setColor(Color.rgb(255,250,224));t.setTextAlign(Paint.Align.CENTER);c.drawText(s,x,y+60,t);}
        private void placements(Canvas c){List<WorldPlacementManager.Placement> list=WorldPlacementManager.all(a);for(WorldPlacementManager.Placement q:list){c.save();c.translate(q.x,q.y);c.rotate(q.rotation);c.scale(q.scale,q.scale);item(c,q.id);c.restore();}}
        private void item(Canvas c,String id){if(id.contains("bench")){bench(c,0,0);return;}if(id.contains("tree")){tree(c,0,0,34);return;}if(id.contains("trophy")){trophy(c,0,0,"🏆");return;}if(id.contains("flower")){for(int i=-2;i<=2;i++){p.setColor(Color.rgb(67,143,65));c.drawRect(i*22-2,0,i*22+2,25,p);p.setColor(Color.rgb(235,113,139));c.drawCircle(i*22,0,7,p);}return;}p.setColor(Color.rgb(190,151,91));c.drawRoundRect(-36,-28,36,28,10,10,p);}
        private void pet(Canvas c){PetWorldAI.State s=PetWorldAI.tick(a,-1080,1080,-1080,1080);p.setColor(Color.argb(55,0,0,0));c.drawOval(s.x-34,s.y-3,s.x+34,s.y+12,p);c.save();c.translate(s.x,s.y);PetBodyModel b=PetBodyModel.forVariant(PetEvolutionManager.current(a));float bob=s.walking?(float)Math.sin(s.stepPhase)*2:0;p.setColor(b.bodyTone);c.drawOval(-34,-55+bob,34,-8+bob,p);p.setColor(b.accentTone);c.drawCircle(0,-75+bob,25,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(-9,-80+bob,3,p);c.drawCircle(9,-80+bob,3,p);p.setStrokeWidth(7);c.drawLine(-20,-10,-25,0,p);c.drawLine(20,-10,25,0,p);c.restore();t.setTextAlign(Paint.Align.CENTER);t.setTextSize(18);t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,s.x,s.y+92,t);}
        @Override public boolean onTouchEvent(MotionEvent e){if(e.getActionMasked()==MotionEvent.ACTION_DOWN){downX=lastX=e.getX();downY=lastY=e.getY();moved=false;return true;}if(e.getActionMasked()==MotionEvent.ACTION_MOVE){float dx=e.getX()-lastX,dy=e.getY()-lastY;if(Math.abs(e.getX()-downX)+Math.abs(e.getY()-downY)>18)moved=true;ox+=dx;oy+=dy;lastX=e.getX();lastY=e.getY();invalidate();return true;}if(e.getActionMasked()==MotionEvent.ACTION_UP){if(moved)return true;float fit=Math.max(getWidth()/W,getHeight()/H)*zoom;float wx=(e.getX()-getWidth()/2f-ox)/fit,wy=(e.getY()-getHeight()/2f-oy)/fit;for(int i=0;i<hit.length;i++)if(hit[i]!=null&&hit[i].contains(wx,wy)){BuildingEventManager.open(a,names[i]);return true;}return true;}return true;}
    }
}
