package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import com.chaquo.python.Python;
import com.chaquo.python.PyObject;

/** Player-facing explorable World with generous building interaction zones. */
public final class ImmersiveWorldView {
    private ImmersiveWorldView() {}

    public static void show(Activity a) {
        World3D canvas = new World3D(a);
        LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(235,242,232));
        TextView title = new TextView(a);
        title.setText("🌎 PYPET WORLD"); title.setTextSize(24); title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(45,55,48)); title.setPadding(8,10,8,4); root.addView(title);
        TextView help = new TextView(a);
        help.setText("Tap anywhere on a building or landmark to enter • tap ground to walk • your pet follows");
        help.setGravity(Gravity.CENTER); help.setTextSize(14); help.setPadding(8,2,8,8); root.addView(help);
        root.addView(canvas, new LinearLayout.LayoutParams(-1,0,1));
        Button trophies = new Button(a); trophies.setText("🏆 Trophy Hall"); trophies.setAllCaps(false); root.addView(trophies);
        AlertDialog dialog = new AlertDialog.Builder(a).setView(root).create();
        trophies.setOnClickListener(v -> TrophyCabinetView.show(a));
        dialog.setOnDismissListener(d -> canvas.stop());
        dialog.show();
        if(dialog.getWindow()!=null){
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels*.99f),(int)(a.getResources().getDisplayMetrics().heightPixels*.94f));
        }
    }

    private static final class World3D extends View {
        private final Activity a; private final Paint p=new Paint(Paint.ANTI_ALIAS_FLAG); private final Paint t=new Paint(Paint.ANTI_ALIAS_FLAG);
        private float playerX=.50f, playerY=.62f; private long started=System.currentTimeMillis(); private boolean running=true;
        private final RectF[] doors=new RectF[7];
        private final RectF[] zones=new RectF[7];
        World3D(Activity a){super(a);this.a=a;t.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.BOLD));setLayerType(View.LAYER_TYPE_SOFTWARE,null);setClickable(true);}
        void stop(){running=false;}
        @Override protected void onDraw(Canvas c){super.onDraw(c);float w=getWidth(),h=getHeight();
            drawSky(c,w,h); drawGround(c,w,h); drawRoads(c,w,h); drawTrees(c,w,h);
            drawHouse(c,w*.05f,h*.31f,w*.25f,h*.53f,"HOME",Color.rgb(202,112,94),0);
            drawSchool(c,w*.34f,h*.25f,w*.67f,h*.48f,"PYTHON ACADEMY",Color.rgb(74,119,177),1);
            drawHouse(c,w*.76f,h*.31f,w*.95f,h*.51f,"MARKET",Color.rgb(214,137,67),3);
            drawHouse(c,w*.08f,h*.66f,w*.30f,h*.85f,"WORKSHOP",Color.rgb(69,137,149),4);
            drawPark(c,w*.37f,h*.67f,w*.63f,h*.91f,"PARK",Color.rgb(82,155,88),5);
            drawHouse(c,w*.69f,h*.64f,w*.93f,h*.84f,"LIBRARY",Color.rgb(126,94,161),6);
            drawGarden(c,w*.02f,h*.55f,w*.20f,h*.72f);
            drawPet(c,w,h); drawHud(c,w,h); if(running)postInvalidateDelayed(120);
        }
        private void drawSky(Canvas c,float w,float h){LinearGradient g=new LinearGradient(0,0,0,h*.52f,Color.rgb(124,194,241),Color.rgb(224,243,255),Shader.TileMode.CLAMP);p.setShader(g);c.drawRect(0,0,w,h*.55f,p);p.setShader(null);p.setColor(Color.rgb(255,218,103));c.drawCircle(w*.86f,h*.11f,w*.055f,p);cloud(c,w*.16f,h*.12f,.045f);cloud(c,w*.55f,h*.09f,.035f);}
        private void drawGround(Canvas c,float w,float h){p.setColor(Color.rgb(102,174,96));Path q=new Path();q.moveTo(0,h*.43f);q.quadTo(w*.28f,h*.35f,w*.5f,h*.44f);q.quadTo(w*.76f,h*.34f,w,h*.44f);q.lineTo(w,h);q.lineTo(0,h);q.close();c.drawPath(q,p);p.setColor(Color.rgb(121,187,104));c.drawRect(0,h*.58f,w,h,p);}
        private void drawRoads(Canvas c,float w,float h){p.setColor(Color.rgb(218,198,161));Path r=new Path();r.moveTo(w*.46f,h*.47f);r.cubicTo(w*.42f,h*.60f,w*.55f,h*.68f,w*.50f,h);r.lineTo(w*.59f,h);r.cubicTo(w*.65f,h*.68f,w*.53f,h*.59f,w*.55f,h*.47f);r.close();c.drawPath(r,p);p.setColor(Color.rgb(204,186,150));c.drawRoundRect(w*.18f,h*.55f,w*.82f,h*.60f,25,25,p);}
        private void drawHouse(Canvas c,float l,float top,float r,float b,String label,int color,int index){
            zones[index]=new RectF(l,top,r,b); doors[index]=new RectF(l+(r-l)*.38f,b-(b-top)*.25f,l+(r-l)*.62f,b-(b-top)*.04f);
            p.setShadowLayer(9,0,5,Color.argb(70,0,0,0));p.setColor(Color.rgb(245,239,225));c.drawRoundRect(l,top,r,b,10,10,p);p.clearShadowLayer();
            Path roof=new Path();roof.moveTo(l-(r-l)*.08f,top);roof.lineTo((l+r)/2,top-(b-top)*.32f);roof.lineTo(r+(r-l)*.08f,top);roof.close();p.setColor(color);c.drawPath(roof,p);
            p.setColor(Color.rgb(250,246,235));c.drawRect(l+5,top+5,r-5,b-5,p);
            p.setColor(Color.rgb(105,159,190));c.drawRect(l+(r-l)*.12f,top+(b-top)*.25f,l+(r-l)*.32f,top+(b-top)*.48f,p);c.drawRect(l+(r-l)*.68f,top+(b-top)*.25f,l+(r-l)*.88f,top+(b-top)*.48f,p);
            p.setColor(Color.rgb(106,74,52));c.drawRoundRect(doors[index],6,6,p);p.setColor(Color.rgb(225,185,80));c.drawCircle(doors[index].right-5,(doors[index].top+doors[index].bottom)/2,2,p);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(Math.max(10,getWidth()*.024f));t.setColor(Color.rgb(48,56,54));c.drawText(label,(l+r)/2,top-(b-top)*.36f,t);
        }
        private void drawSchool(Canvas c,float l,float top,float r,float b,String label,int color,int index){
            zones[index]=new RectF(l,top,r,b);doors[index]=new RectF((l+r)/2-(r-l)*.07f,b-(b-top)*.27f,(l+r)/2+(r-l)*.07f,b-(b-top)*.03f);
            p.setShadowLayer(10,0,6,Color.argb(75,0,0,0));p.setColor(Color.rgb(246,241,230));c.drawRoundRect(l,top,r,b,12,12,p);p.clearShadowLayer();
            Path roof=new Path();roof.moveTo(l,top);roof.lineTo((l+r)/2,top-(b-top)*.38f);roof.lineTo(r,top);roof.close();p.setColor(color);c.drawPath(roof,p);p.setColor(Color.rgb(232,236,235));c.drawRect(l+7,top+7,r-7,b-7,p);
            for(int yy=0;yy<2;yy++)for(int xx=0;xx<4;xx++){float x=l+(r-l)*(.10f+xx*.25f),y=top+(b-top)*(.24f+yy*.25f);p.setColor(Color.rgb(102,168,198));c.drawRoundRect(x,y,x+(r-l)*.14f,y+(b-top)*.14f,4,4,p);}
            p.setColor(Color.rgb(91,65,48));c.drawRoundRect(doors[index],6,6,p);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(Math.max(11,getWidth()*.026f));t.setColor(Color.WHITE);c.drawText("PYTHON LAB",(l+r)/2,top+(b-top)*.65f,t);t.setTextSize(Math.max(10,getWidth()*.023f));t.setColor(Color.rgb(48,56,54));c.drawText(label,(l+r)/2,top-(b-top)*.43f,t);
        }
        private void drawGarden(Canvas c,float l,float top,float r,float b){p.setColor(Color.rgb(76,143,76));c.drawRoundRect(l,top,r,b,18,18,p);for(int i=0;i<7;i++){float x=l+12+(i%3)*(r-l-24)/3f,y=top+20+(i/3)*28;p.setColor(Color.rgb(61,122,57));c.drawCircle(x,y,8,p);p.setColor(Color.rgb(239,150,173));c.drawCircle(x+6,y-4,3,p);}}
        private void drawPark(Canvas c,float l,float top,float r,float b,String label,int color,int index){zones[index]=new RectF(l,top,r,b);doors[index]=new RectF(l,top,r,b);p.setColor(color);c.drawRoundRect(l,top,r,b,24,24,p);p.setColor(Color.rgb(126,83,51));c.drawRoundRect(l+(r-l)*.18f,top+(b-top)*.22f,l+(r-l)*.22f,b-(b-top)*.18f,5,5,p);p.setColor(Color.rgb(66,142,74));c.drawCircle(l+(r-l)*.20f,top+(b-top)*.18f,(r-l)*.12f,p);p.setColor(Color.rgb(232,170,70));c.drawCircle((l+r)/2,top+(b-top)*.54f,(r-l)*.12f,p);t.setTextAlign(Paint.Align.CENTER);t.setColor(Color.WHITE);t.setTextSize(Math.max(11,getWidth()*.025f));c.drawText(label,(l+r)/2,b-9,t);}
        private void drawTrees(Canvas c,float w,float h){tree(c,w*.03f,h*.39f,.04f);tree(c,w*.28f,h*.46f,.045f);tree(c,w*.72f,h*.47f,.045f);tree(c,w*.97f,h*.42f,.045f);tree(c,w*.33f,h*.91f,.05f);tree(c,w*.66f,h*.93f,.05f);}
        private void tree(Canvas c,float x,float y,float s){p.setColor(Color.rgb(116,77,50));c.drawRect(x-s*.12f,y,x+s*.12f,y+s*1.4f,p);p.setColor(Color.rgb(54,135,68));c.drawCircle(x,y-s*.25f,s,p);c.drawCircle(x-s*.5f,y,s*.65f,p);c.drawCircle(x+s*.5f,y,s*.65f,p);}
        private void cloud(Canvas c,float x,float y,float s){p.setColor(Color.argb(210,255,255,255));c.drawCircle(x,y,s,p);c.drawCircle(x+s*.65f,y+s*.1f,s*.7f,p);c.drawCircle(x-s*.65f,y+s*.12f,s*.6f,p);}
        private void drawPet(Canvas c,float w,float h){float bob=(float)Math.sin((System.currentTimeMillis()-started)/900.0)*2;float x=w*playerX,y=h*playerY+bob;t.setTextAlign(Paint.Align.CENTER);t.setTextSize(Math.max(38,w*.085f));t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.current(a).emoji,x,y,t);t.setTextSize(Math.max(10,w*.022f));c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+30,t);}
        private void drawHud(Canvas c,float w,float h){p.setColor(Color.argb(225,255,255,255));c.drawRoundRect(w*.025f,h*.025f,w*.72f,h*.105f,18,18,p);t.setTextAlign(Paint.Align.LEFT);t.setColor(Color.rgb(48,57,52));t.setTextSize(Math.max(10,w*.022f));c.drawText("🏠 Home   🏫 Academy   🌳 Garden   🛠 Workshop   🌳 Park",w*.045f,h*.060f,t);c.drawText("Tap the building itself to enter. Tap the ground to walk.",w*.045f,h*.092f,t);}
        @Override public boolean onTouchEvent(MotionEvent e){
            if(e.getAction()!=MotionEvent.ACTION_UP)return true;
            float x=e.getX(),y=e.getY();
            for(int i=0;i<zones.length;i++) if(zones[i]!=null && zones[i].contains(x,y)){ playerX=Math.max(.08f,Math.min(.92f,x/getWidth())); playerY=Math.max(.18f,Math.min(.94f,y/getHeight())); enter(i); return true; }
            playerX=Math.max(.08f,Math.min(.92f,x/getWidth())); playerY=Math.max(.18f,Math.min(.94f,y/getHeight())); invalidate(); return true;
        }
        private void enter(int i){switch(i){case 0:home();break;case 1:codingLab();break;case 2:garden();break;case 3:market();break;case 4:workshop();break;case 5:play();break;case 6:library();break;}}
        private void home(){new AlertDialog.Builder(a).setTitle("🏠 HOME • ROOMS").setMessage("Your pet's home is a living space.\n\nBedroom • living room • kitchen • bathroom • study/workshop.\n\nCare, feed, rest and study with your pet here. Home development follows balanced learning and care.").setPositiveButton("Study with my pet",(d,w)->codingLab()).setNegativeButton("Back outside",null).show();}
        private void codingLab(){
            final LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,18,22,18);
            TextView title=new TextView(a);title.setText("💻 PYTHON LAB — HANDS-ON");title.setTextSize(23);root.addView(title);
            TextView mission=new TextView(a);mission.setTextSize(16);root.addView(mission);
            EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setMinLines(10);code.setText("answer = 2 + 3\nprint(answer)");code.setHint("Write real Python. Your pet learns from demonstrated skills.");root.addView(code,new LinearLayout.LayoutParams(-1,0,1));
            TextView output=new TextView(a);output.setTextSize(15);root.addView(output);Button run=new Button(a);run.setText("▶ RUN • TEST • LEARN");root.addView(run);
            try{PyObject py=Python.getInstance().getModule("pypet_engine");int idx=PetEvolutionManager.lessons(a);mission.setText("🎯 Mission "+(idx+1)+"\n"+py.callAttr("current_lesson",idx).toString()+"\n\nDo the task yourself. Run it, inspect the result, change the code, and try again.");}catch(Exception ex){mission.setText("🎯 Solve a Python problem for your pet. Run it and iterate until it works.");}
            run.setOnClickListener(v->{try{String src=code.getText().toString();PyObject py=Python.getInstance().getModule("pypet_engine");String out=py.callAttr("run_lesson",src).toString();output.setText("🧪 Result\n"+out);if(out.contains("'ok': True")&&out.contains("'passed': True")){PetEvolutionManager.completeLesson(a);PypetAchievementManager.recordDailyActivity(a);output.append("\n\n🎓 Skill demonstrated! Your pet learned it with you.\n🌎 World development: "+PetEvolutionManager.balancedDevelopmentScore(a)+"%");}else output.append("\n\n💡 Keep experimenting. Read the result, edit your code, and run again.");}catch(Exception ex){output.setText("Python error: "+ex.getMessage()+"\n\nFix it and try again.");}});
            new AlertDialog.Builder(a).setView(root).setNegativeButton("Leave lab",null).show();
        }
        private void garden(){PetEvolutionManager.performExplore(a);PypetAchievementManager.recordDailyActivity(a);new AlertDialog.Builder(a).setTitle("🌳 GARDEN YARD").setMessage("Plant, water and explore. Your pet discovers small Python-flavored puzzles as the garden grows.\n\nExploration completed! 🌱").setPositiveButton("Keep exploring",null).show();}
        private void market(){TreasureStore.show(a);}
        private void workshop(){new AlertDialog.Builder(a).setTitle("🛠 WORKSHOP").setMessage("Build World objects here: logic, automation, graphics and animation projects.\n\nNext projects unlock through demonstrated Python skills.").setPositiveButton("Open Python Lab",(d,w)->codingLab()).setNegativeButton("Back",null).show();}
        private void play(){PetEvolutionManager.playWith(a);PypetAchievementManager.recordDailyActivity(a);new AlertDialog.Builder(a).setTitle("🌳 PLAY PARK").setMessage("Your pet plays with you. Try movement, timing and animation challenges as you progress.\n\nPlay completed!").setPositiveButton("Again",null).show();}
        private void library(){new AlertDialog.Builder(a).setTitle("📚 LIBRARY").setMessage("The library is your reference center: Python documentation, algorithms, data structures, graphics, animation, 3D, testing, Git, APIs, databases, security and job-readiness challenges.\n\nReading is optional support; mastery comes from building.").setPositiveButton("Hands-on lab",(d,w)->codingLab()).setNegativeButton("Back",null).show();}
    }
}
