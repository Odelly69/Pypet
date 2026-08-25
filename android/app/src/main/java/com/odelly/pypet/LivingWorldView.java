package com.odelly.pypet;

import android.app.Activity;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import java.util.List;

/** Full-screen, road-planned living town. Buildings are real activity entry points and the park is visibly a playground. */
public final class LivingWorldView {
    private LivingWorldView() {}

    public static void show(Activity a) {
        final World world = new World(a);
        FrameLayout root = new FrameLayout(a);
        root.setBackgroundColor(Color.rgb(151,196,119));
        root.addView(world,new FrameLayout.LayoutParams(-1,-1));
        TextView title=overlay(a,"🌎 "+PypetProfileManager.townName(a)+"  •  PyPet WORLD",19);
        root.addView(title,new FrameLayout.LayoutParams(-1,48,Gravity.TOP));
        TextView help=overlay(a,"Pinch to zoom • drag to explore • tap a building to DO the activity",12);
        FrameLayout.LayoutParams hp=new FrameLayout.LayoutParams(-1,34,Gravity.TOP);hp.topMargin=48;root.addView(help,hp);
        LinearLayout controls=new LinearLayout(a);controls.setGravity(Gravity.CENTER);controls.setPadding(5,3,5,3);controls.setBackgroundColor(Color.argb(225,250,247,232));
        Button out=btn(a,"−"),reset=btn(a,"🌎 Reset"),in=btn(a,"+"),profile=btn(a,"👤");
        controls.addView(out,new LinearLayout.LayoutParams(58,56));controls.addView(reset,new LinearLayout.LayoutParams(0,56,1));controls.addView(in,new LinearLayout.LayoutParams(58,56));controls.addView(profile,new LinearLayout.LayoutParams(66,56));
        root.addView(controls,new FrameLayout.LayoutParams(-1,62,Gravity.BOTTOM));
        out.setOnClickListener(v->world.zoomBy(.86f));in.setOnClickListener(v->world.zoomBy(1.16f));reset.setOnClickListener(v->world.reset());profile.setOnClickListener(v->PypetProfileView.show(a,false,()->show(a)));
        a.setContentView(root);
    }
    private static TextView overlay(Activity a,String text,float size){TextView v=new TextView(a);v.setText(text);v.setTextSize(size);v.setGravity(Gravity.CENTER);v.setTextColor(Color.rgb(39,53,42));v.setBackgroundColor(Color.argb(225,250,247,232));return v;}
    private static Button btn(Activity a,String text){Button b=new Button(a);b.setText(text);b.setAllCaps(false);return b;}

    private static final class World extends View {
        private static final float W=2400f,H=2400f,MIN=.62f,MAX=3f;
        private final Activity a;private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG),t=new Paint(Paint.ANTI_ALIAS_FLAG);
        private final RectF[] hit=new RectF[6];private final String[] names={"HOME","PYTHON ACADEMY","MARKET","WORKSHOP","PARK","LIBRARY"};
        private float zoom=1f,ox,oy,lastX,lastY,downX,downY,lastDist;private boolean moved;
        World(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void reset(){zoom=1f;ox=oy=0;PetWorldAI.reset(a,260,420);invalidate();}
        void zoomBy(float factor){zoom=Math.max(MIN,Math.min(MAX,zoom*factor));invalidate();}
        /** Cover the entire device like a real map; never letterbox the town with an empty blue area. */
        @Override protected void onDraw(Canvas c){
            c.drawColor(Color.rgb(151,196,119));
            float fit=Math.max(getWidth()/W,getHeight()/H)*zoom;
            c.save();c.translate(getWidth()/2f+ox,getHeight()/2f+oy);c.scale(fit,fit);drawWorld(c);c.restore();postInvalidateDelayed(120);
        }
        private void drawWorld(Canvas c){
            p.setStyle(Paint.Style.FILL);p.setColor(Color.rgb(151,196,119));c.drawRect(-W/2,-H/2,W/2,H/2,p);
            drawTerrain(c);drawRoads(c);drawLots(c);drawSidewalks(c);
            drawPark(c,-1060,180,-220,1060,4);drawBuildings(c);drawTrees(c);drawTrophies(c);drawPlacements(c);drawPet(c);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(50);t.setColor(Color.rgb(43,70,45));c.drawText(PypetProfileManager.townName(a),0,-1090,t);
            t.setTextSize(24);c.drawText("Your accomplishments live here",0,-1038,t);
        }
        private void drawTerrain(Canvas c){
            p.setColor(Color.rgb(151,196,119));c.drawRect(-1200,-1200,1200,1200,p);
            p.setColor(Color.rgb(174,207,139));for(int x=-1100;x<=1100;x+=220)for(int y=-1120;y<=1120;y+=260)c.drawCircle(x+((y/260)%2)*35,y,4,p);
            p.setColor(Color.rgb(196,214,157));for(int x=-1080;x<=1080;x+=360)c.drawCircle(x,1120,18,p);
        }
        private void drawRoads(Canvas c){
            p.setColor(Color.rgb(216,205,177));c.drawRect(-160,-1200,160,1200,p);c.drawRect(-1200,-160,1200,160,p);
            p.setColor(Color.rgb(58,59,57));c.drawRect(-98,-1200,98,1200,p);c.drawRoundRect(-1200,-98,1200,98,24,24,p);
            p.setColor(Color.rgb(229,219,181));
            for(int y=-1120;y<=1120;y+=170)if(Math.abs(y)>135)c.drawRoundRect(-5,y,5,y+92,3,3,p);
            for(int x=-1120;x<=1120;x+=170)if(Math.abs(x)>135)c.drawRoundRect(x,-5,x+92,5,3,3,p);
            p.setColor(Color.rgb(190,185,165));c.drawRect(-185,-1200,-160,1200,p);c.drawRect(160,-1200,185,1200,p);c.drawRect(-1200,-185,1200,-160,p);c.drawRect(-1200,160,1200,185,p);
        }
        private void drawLots(Canvas c){
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.argb(105,255,255,255));
            lot(c,-910,-990,-250,-560);lot(c,250,-990,910,-560);lot(c,-910,250,-250,670);lot(c,250,250,910,670);lot(c,250,720,910,1080);lot(c,-1080,180,-220,1080);p.setStyle(Paint.Style.FILL);
        }
        private void lot(Canvas c,float l,float top,float r,float bot){c.drawRoundRect(l,top,r,bot,28,28,p);}
        private void drawSidewalks(Canvas c){
            p.setColor(Color.rgb(205,198,174));
            c.drawRoundRect(-250,-630,-160,-585,10,10,p);c.drawRoundRect(160,-630,250,-585,10,10,p);
            c.drawRoundRect(-250,275,-160,320,10,10,p);c.drawRoundRect(160,275,250,320,10,10,p);
            c.drawRoundRect(160,745,250,790,10,10,p);c.drawRoundRect(-245,420,-160,465,10,10,p);
        }
        private void drawBuildings(Canvas c){
            house(c,-850,-940,-420,-620,"HOME",0,Color.rgb(164,83,63));
            academy(c,420,-940,850,-620,1);market(c,420,290,850,600,2);workshop(c,-850,290,-420,600,3);library(c,420,760,850,1050,5);
        }
        private void baseBuilding(Canvas c,float l,float top,float r,float bot,int roof,String label,int index){
            hit[index]=new RectF(l,top,r,bot);float m=(l+r)/2f,hh=bot-top;
            p.setShadowLayer(14,0,7,Color.argb(70,0,0,0));p.setColor(Color.rgb(246,241,225));c.drawRoundRect(l,top,r,bot,18,18,p);p.clearShadowLayer();
            p.setColor(roof);Path q=new Path();q.moveTo(l-18,top+8);q.lineTo(m,top-hh*.28f);q.lineTo(r+18,top+8);q.close();c.drawPath(q,p);
            frontDoor(c,m,bot);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(18);t.setColor(Color.rgb(45,52,47));c.drawText(label,m,top-hh*.31f,t);
        }
        /** Real-world cue: every building has a front door, threshold, frame and knob. */
        private void frontDoor(Canvas c,float x,float bottom){
            p.setColor(Color.rgb(103,74,54));c.drawRoundRect(x-24,bottom-68,x+24,bottom,6,6,p);
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(4);p.setColor(Color.rgb(72,52,39));c.drawRoundRect(x-24,bottom-68,x+24,bottom,6,6,p);p.setStyle(Paint.Style.FILL);
            p.setColor(Color.rgb(232,204,117));c.drawCircle(x+11,bottom-34,3.5f,p);
            p.setColor(Color.rgb(203,195,171));c.drawRect(x-34,bottom,x+34,bottom+9,p);
        }
        private void window(Canvas c,float x,float y,float w,float h){
            p.setColor(Color.rgb(71,133,166));c.drawRoundRect(x,y,x+w,y+h,5,5,p);
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(3);p.setColor(Color.rgb(238,241,224));c.drawRect(x,y,x+w,y+h,p);c.drawLine(x+w/2,y,x+w/2,y+h,p);c.drawLine(x,y+h/2,x+w,y+h/2,p);p.setStyle(Paint.Style.FILL);
        }
        private void house(Canvas c,float l,float top,float r,float bot,String label,int index,int roof){baseBuilding(c,l,top,r,bot,roof,label,index);window(c,l+55,top+70,70,58);window(c,r-125,top+70,70,58);}
        private void academy(Canvas c,float l,float top,float r,float bot,int index){baseBuilding(c,l,top,r,bot,Color.rgb(64,95,147),"PYTHON ACADEMY",index);for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+25+col*82,top+38+row*62,48,38);p.setColor(Color.rgb(74,106,150));c.drawRoundRect(l+28,bot-105,r-28,bot-75,10,10,p);t.setTextSize(14);t.setColor(Color.WHITE);c.drawText("CODE • LEARN • BUILD",(l+r)/2,bot-84,t);}
        private void market(Canvas c,float l,float top,float r,float bot,int index){baseBuilding(c,l,top,r,bot,Color.rgb(209,143,55),"MARKET",index);for(int x=0;x<3;x++)window(c,l+45+x*120,top+60,60,45);p.setColor(Color.rgb(222,83,63));c.drawRect(l+40,bot-105,r-40,bot-80,p);}
        private void workshop(Canvas c,float l,float top,float r,float bot,int index){baseBuilding(c,l,top,r,bot,Color.rgb(48,116,137),"WORKSHOP",index);for(int x=0;x<2;x++)window(c,l+55+x*180,top+55,75,52);p.setColor(Color.rgb(122,88,55));c.drawRect(l+50,top+135,r-50,top+165,p);for(int x=0;x<3;x++){p.setColor(Color.rgb(216,171,72));c.drawCircle(l+90+x*125,top+118,16,p);}}
        private void library(Canvas c,float l,float top,float r,float bot,int index){baseBuilding(c,l,top,r,bot,Color.rgb(103,74,137),"LIBRARY",index);for(int x=0;x<3;x++)window(c,l+48+x*112,top+50,64,46);for(int x=0;x<4;x++){p.setColor(Color.rgb(101,65,42));c.drawRect(l+45+x*90,top+125,l+70+x*90,bot-78,p);p.setColor(Color.rgb(215,174,84));c.drawRect(l+75+x*90,top+135,l+95+x*90,bot-78,p);}}

        private void drawPark(Canvas c,float l,float top,float r,float bot,int index){
            hit[index]=new RectF(l,top,r,bot);p.setShadowLayer(10,0,5,Color.argb(60,0,0,0));p.setColor(Color.rgb(83,166,86));c.drawRoundRect(l,top,r,bot,40,40,p);p.clearShadowLayer();
            p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(13);p.setColor(Color.rgb(237,224,178));c.drawRoundRect(l+18,top+18,r-18,bot-18,30,30,p);p.setStyle(Paint.Style.FILL);
            p.setColor(Color.rgb(224,210,171));c.drawRoundRect(l+55,top+390,r-55,top+455,20,20,p);c.drawRoundRect(l+390,top+65,l+455,bot-65,20,20,p);
            bench(c,l+125,top+330);bench(c,l+600,top+520);drawPond(c,l+90,bot-310);drawPlayground(c,r-410,top+150);
            for(int j=0;j<5;j++)tree(c,l+105+j*150,top+90+(j%2)*610,42);for(int j=0;j<9;j++)flower(c,l+90+j*88,top+665+(j%2)*30);
            p.setColor(Color.rgb(244,231,180));c.drawRoundRect((l+r)/2-170,top+28,(l+r)/2+170,top+105,12,12,p);t.setTextSize(28);t.setColor(Color.rgb(48,95,48));c.drawText("🌳 PARK • PLAYGROUND",(l+r)/2,top+79,t);
        }
        private void drawPond(Canvas c,float x,float y){p.setColor(Color.rgb(58,155,198));c.drawOval(x,y,x+280,y+220,p);p.setColor(Color.rgb(150,214,232));c.drawOval(x+45,y+45,x+180,y+125,p);p.setColor(Color.rgb(86,137,71));c.drawCircle(x+225,y+35,18,p);}
        private void drawPlayground(Canvas c,float x,float y){
            p.setColor(Color.rgb(210,78,64));c.drawRoundRect(x,y,x+125,y+30,8,8,p);c.drawRect(x+12,y+25,x+28,y+125,p);c.drawRect(x+97,y+25,x+113,y+75,p);c.drawRoundRect(x+100,y+68,x+220,y+90,12,12,p);
            p.setColor(Color.rgb(103,73,51));p.setStrokeWidth(12);c.drawLine(x+20,y+175,x+160,y+175,p);c.drawLine(x+30,y+175,x,y+270,p);c.drawLine(x+150,y+175,x+180,y+270,p);p.setStrokeWidth(4);c.drawLine(x+55,y+178,x+55,y+250,p);c.drawLine(x+120,y+178,x+120,y+250,p);p.setColor(Color.rgb(219,116,57));c.drawRoundRect(x+35,y+250,x+75,y+262,5,5,p);c.drawRoundRect(x+100,y+250,x+140,y+262,5,5,p);
            p.setColor(Color.rgb(224,190,112));c.drawRoundRect(x+225,y+185,x+365,y+285,18,18,p);p.setColor(Color.rgb(238,214,153));c.drawCircle(x+295,y+235,48,p);p.setColor(Color.rgb(224,69,69));c.drawCircle(x+275,y+330,28,p);p.setColor(Color.WHITE);c.drawCircle(x+285,y+320,6,p);c.drawCircle(x+265,y+340,6,p);
            p.setColor(Color.rgb(110,78,52));c.drawRect(x+205,y+360,x+225,y+410,p);p.setColor(Color.rgb(231,143,55));c.drawRoundRect(x+145,y+350,x+285,y+365,8,8,p);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(22);t.setColor(Color.rgb(55,83,48));c.drawText("PLAY",x+205,y+445,t);
        }
        private void bench(Canvas c,float x,float y){p.setColor(Color.rgb(118,78,47));c.drawRoundRect(x-65,y,x+65,y+18,5,5,p);c.drawRect(x-48,y+18,x-35,y+48,p);c.drawRect(x+35,y+18,x+48,y+48,p);}
        private void tree(Canvas c,float x,float y,float r){p.setColor(Color.rgb(113,76,49));c.drawRect(x-8,y+r*.35f,x+8,y+r*1.2f,p);p.setColor(Color.rgb(47,127,57));c.drawCircle(x,y,r,p);p.setColor(Color.rgb(78,154,75));c.drawCircle(x-r*.35f,y-r*.2f,r*.55f,p);}
        private void flower(Canvas c,float x,float y){p.setColor(Color.rgb(67,143,65));c.drawRect(x-2,y,x+2,y+25,p);p.setColor(Color.rgb(235,113,139));c.drawCircle(x,y,7,p);p.setColor(Color.rgb(248,216,86));c.drawCircle(x,y,3,p);}
        private void drawTrees(Canvas c){for(int i=-5;i<=5;i++)tree(c,i*280,-360+i*115,31);}
        private void drawTrophies(Canvas c){int k=0;for(PypetAchievementManager.Trophy tr:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,tr.id))continue;float x=-1050+(k%7)*300,y=950+(k/7)*90;trophy(c,x,y,tr.name);k++;}}
        private void trophy(Canvas c,float x,float y,String label){p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-24,y-30,x+24,y+12,7,7,p);c.drawCircle(x,y-8,15,p);p.setColor(Color.rgb(117,81,43));c.drawRect(x-5,y+10,x+5,y+31,p);c.drawRect(x-35,y+29,x+35,y+38,p);t.setTextSize(12);t.setColor(Color.rgb(255,250,224));c.drawText(label,x,y+60,t);}
        private void drawPlacements(Canvas c){List<WorldPlacementManager.Placement> list=WorldPlacementManager.all(a);for(WorldPlacementManager.Placement q:list){c.save();c.translate(q.x,q.y);c.rotate(q.rotation);c.scale(q.scale,q.scale);drawItem(c,q.id);c.restore();}}
        private void drawItem(Canvas c,String id){if(id.contains("bench")){bench(c,0,0);return;}if(id.contains("flower")){for(int i=-2;i<=2;i++)flower(c,i*22,0);return;}if(id.contains("lamp")){p.setColor(Color.rgb(80,80,78));c.drawRect(-4,-5,4,52,p);p.setColor(Color.rgb(244,220,112));c.drawCircle(0,-12,15,p);return;}if(id.contains("tree")){tree(c,0,0,34);return;}if(id.contains("trophy")){trophy(c,0,0,"🏆");return;}if(id.contains("garden_sign")){p.setColor(Color.rgb(112,78,49));c.drawRect(-5,-5,5,48,p);p.setColor(Color.rgb(244,231,180));c.drawRoundRect(-58,-30,58,4,8,8,p);t.setTextSize(12);t.setColor(Color.rgb(48,95,48));c.drawText("GARDEN",0,-10,t);return;}if(id.contains("fountain")){p.setColor(Color.rgb(80,157,190));c.drawCircle(0,5,34,p);p.setColor(Color.rgb(226,226,220));c.drawRoundRect(-45,-20,45,0,10,10,p);return;}if(id.contains("castle")||id.contains("tower")||id.contains("palace")){p.setColor(Color.rgb(179,157,205));c.drawRoundRect(-42,-40,42,42,8,8,p);p.setColor(Color.rgb(125,93,155));c.drawRect(-52,-58,-22,-18,p);c.drawRect(22,-58,52,-18,p);return;}if(id.contains("garden")||id.contains("patch")){p.setColor(Color.rgb(90,161,80));c.drawOval(-52,-30,52,30,p);for(int i=-2;i<=2;i++)flower(c,i*20,-4);return;}p.setColor(Color.rgb(190,151,91));c.drawRoundRect(-36,-28,36,28,10,10,p);p.setColor(Color.rgb(246,224,153));c.drawCircle(0,-3,12,p);}
        private void drawPet(Canvas c){
            PetWorldAI.State s=PetWorldAI.tick(a,-1080,1080,-1080,1080);float x=s.x,ground=s.y;
            // Ground contact: shadow, feet, and body are anchored to the same groundY so the pet cannot float.
            p.setColor(Color.argb(55,0,0,0));c.drawOval(x-34,ground-3,x+34,ground+12,p);
            String species=PetEvolutionManager.current(a).displayName.toLowerCase();
            c.save();c.translate(x,ground);if(Math.cos(s.heading)<0)c.scale(-1,1);
            if(species.contains("frog"))frogGrounded(c,s);else genericGrounded(c,s);
            c.restore();
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(18);t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,ground+92,t);
        }
        private void frogGrounded(Canvas c,PetWorldAI.State s){
            float bob=s.walking?(float)Math.sin(s.stepPhase)*2:0;
            p.setColor(Color.rgb(76,164,70));c.drawOval(-34,-48+bob,34,-3+bob,p);c.drawOval(-30,-5+bob,-11,16,p);c.drawOval(11,-5+bob,30,16,p);
            p.setColor(Color.rgb(93,190,80));c.drawCircle(-21,-55+bob,15,p);c.drawCircle(21,-55+bob,15,p);p.setColor(Color.WHITE);c.drawCircle(-21,-56+bob,7,p);c.drawCircle(21,-56+bob,7,p);p.setColor(Color.BLACK);c.drawCircle(-21,-56+bob,3,p);c.drawCircle(21,-56+bob,3,p);
        }
        private void genericGrounded(Canvas c,PetWorldAI.State s){
            float bob=s.walking?(float)Math.sin(s.stepPhase)*2.5f:0;PetBodyModel b=PetBodyModel.forVariant(PetEvolutionManager.current(a));
            float bodyW=34*b.bodyWidth,bodyH=45*b.height,head=28*b.headScale;
            p.setColor(b.bodyTone);c.drawOval(-bodyW,-bodyH+bob,bodyW,-8+bob,p);
            // Four little legs contact the shadow instead of hovering above it.
            p.setStrokeWidth(7);p.setColor(b.accentTone);float stride=s.walking?(float)Math.sin(s.stepPhase)*5:0;c.drawLine(-bodyW*.55f,-10+bob,-bodyW*.62f,0+stride,p);c.drawLine(-bodyW*.15f,-8+bob,-bodyW*.05f,0-stride,p);c.drawLine(bodyW*.15f,-8+bob,bodyW*.05f,0+stride,p);c.drawLine(bodyW*.55f,-10+bob,bodyW*.62f,0-stride,p);
            c.drawCircle(0,-bodyH-head*.55f+bob,head,p);
            if(b.hasEars){p.setColor(b.accentTone);Path ear1=new Path();ear1.moveTo(-head*.75f,-bodyH-head*.7f);ear1.lineTo(-head*.35f,-bodyH-head*1.45f);ear1.lineTo(-head*.05f,-bodyH-head*.65f);ear1.close();c.drawPath(ear1,p);Path ear2=new Path();ear2.moveTo(head*.75f,-bodyH-head*.7f);ear2.lineTo(head*.35f,-bodyH-head*1.45f);ear2.lineTo(head*.05f,-bodyH-head*.65f);ear2.close();c.drawPath(ear2,p);}
            p.setColor(Color.rgb(65,45,35));c.drawCircle(-head*.35f,-bodyH-head*.62f,4*b.eyeScale,p);c.drawCircle(head*.35f,-bodyH-head*.62f,4*b.eyeScale,p);
            if(b.hasTail){p.setColor(b.accentTone);p.setStyle(Paint.Style.STROKE);p.setStrokeWidth(7);RectF tail=new RectF(bodyW*.35f,-bodyH*.65f,bodyW*1.5f,5);c.drawArc(tail,-35,125,false,p);p.setStyle(Paint.Style.FILL);}
        }
        @Override public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=distance(e);if(lastDist>0)zoom=Math.max(MIN,Math.min(MAX,zoom*d/lastDist));lastDist=d;invalidate();return true;}lastDist=0;switch(e.getActionMasked()){case MotionEvent.ACTION_DOWN:lastX=downX=e.getX();lastY=downY=e.getY();moved=false;return true;case MotionEvent.ACTION_MOVE:float dx=e.getX()-lastX,dy=e.getY()-lastY;if(Math.abs(e.getX()-downX)>10||Math.abs(e.getY()-downY)>10)moved=true;ox+=dx;oy+=dy;lastX=e.getX();lastY=e.getY();invalidate();return true;case MotionEvent.ACTION_UP:if(!moved)tap(e.getX(),e.getY());return true;}return true;}
        private float distance(MotionEvent e){float dx=e.getX(1)-e.getX(0),dy=e.getY(1)-e.getY(0);return(float)Math.sqrt(dx*dx+dy*dy);}
        private void tap(float sx,float sy){float fit=Math.max(getWidth()/W,getHeight()/H)*zoom;float x=(sx-getWidth()/2f-ox)/fit,y=(sy-getHeight()/2f-oy)/fit;for(int i=0;i<hit.length;i++)if(hit[i]!=null&&hit[i].contains(x,y)){BuildingEventManager.open(a,names[i]);return;}PetWorldAI.State s=PetWorldAI.tick(a,-1080,1080,-1080,1080);if(Math.hypot(x-s.x,y-s.y)<110)Toast.makeText(a,"🐾 "+PetEvolutionManager.name(a)+" is exploring the town.",Toast.LENGTH_SHORT).show();}
    }
}
