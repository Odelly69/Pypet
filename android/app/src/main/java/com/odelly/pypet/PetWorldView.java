package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;

/**
 * The game world is the primary gameplay surface. Landmarks unlock progressively
 * from balanced development and each landmark launches its actual gameplay system.
 * Graphics are deliberately calm: no flashing, strobing or rapid effects.
 */
public final class PetWorldView {
    private PetWorldView() {}

    public static void show(Activity a) {
        WorldCanvas world = new WorldCanvas(a);
        LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(248, 246, 252));
        TextView title = new TextView(a);
        title.setText("🐾 " + PetEvolutionManager.name(a) + "'s Living World");
        title.setTextSize(25); title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.rgb(55, 45, 82)); title.setPadding(8, 12, 8, 4);
        root.addView(title);
        TextView hint = new TextView(a);
        hint.setText("Tap a landmark to play • New areas develop as your pet grows");
        hint.setGravity(Gravity.CENTER); hint.setTextSize(14); hint.setTextColor(Color.DKGRAY);
        root.addView(hint);
        root.addView(world, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout buttons = new LinearLayout(a);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        Button trophies = new Button(a); trophies.setText("🏆 Trophy Hall"); trophies.setAllCaps(false);
        Button close = new Button(a); close.setText("Back"); close.setAllCaps(false);
        buttons.addView(trophies, new LinearLayout.LayoutParams(0, -2, 1));
        buttons.addView(close, new LinearLayout.LayoutParams(0, -2, 1));
        root.addView(buttons);
        AlertDialog d = new AlertDialog.Builder(a).setView(root).create();
        trophies.setOnClickListener(v -> TrophyCabinetView.show(a));
        close.setOnClickListener(v -> d.dismiss());
        d.show();
        if (d.getWindow() != null) {
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            d.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels * .98f),
                    (int)(a.getResources().getDisplayMetrics().heightPixels * .93f));
        }
    }

    private static final class WorldCanvas extends View {
        private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Paint text = new Paint(Paint.ANTI_ALIAS_FLAG);
        private final Activity a;
        private final RectF[] zones = new RectF[9];
        private final String[] names = {"Home", "Pypet Academy", "Hatchery", "Garden", "Play Park", "Library", "Workshop", "Market", "Trophy Hall"};
        private float petX = .50f, petY = .66f;

        WorldCanvas(Activity a) {
            super(a); this.a = a;
            text.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override protected void onDraw(Canvas c) {
            float w = getWidth(), h = getHeight();
            drawSky(c, w, h);
            drawLandscape(c, w, h);
            drawPathAndWater(c, w, h);
            drawDecor(c, w, h);

            int score = PetEvolutionManager.balancedDevelopmentScore(a);
            int stage = PetEvolutionManager.current(a).level - 1;
            drawBuilding(c, 0, w*.04f, h*.47f, w*.25f, h*.66f, "HOME", "♥", Color.rgb(231,128,117), true, score >= 0);
            drawBuilding(c, 1, w*.30f, h*.39f, w*.53f, h*.58f, "ACADEMY", "λ", Color.rgb(102,128,205), true, score >= 10);
            drawBuilding(c, 2, w*.70f, h*.39f, w*.94f, h*.58f, "HATCHERY", "E", Color.rgb(199,111,157), true, score >= 20);
            drawBuilding(c, 3, w*.05f, h*.77f, w*.26f, h*.94f, "GARDEN", "✿", Color.rgb(83,158,100), true, score >= 30);
            drawBuilding(c, 4, w*.29f, h*.73f, w*.49f, h*.92f, "PLAY PARK", "★", Color.rgb(235,169,58), true, score >= 40);
            drawBuilding(c, 5, w*.55f, h*.73f, w*.72f, h*.92f, "LIBRARY", "≡", Color.rgb(137,102,177), true, score >= 50);
            drawBuilding(c, 6, w*.76f, h*.70f, w*.95f, h*.90f, "WORKSHOP", "W", Color.rgb(71,139,151), true, score >= 60);
            drawBuilding(c, 7, w*.39f, h*.58f, w*.61f, h*.70f, "MARKET", "$", Color.rgb(221,119,72), true, score >= 70);
            drawBuilding(c, 8, w*.72f, h*.15f, w*.95f, h*.31f, "TROPHY HALL", "🏆", Color.rgb(186,145,55), true, score >= 80);

            drawProgressHud(c, w, h, score, stage);
            drawPet(c, w, h);
            postInvalidateDelayed(350);
        }

        private void drawSky(Canvas c, float w, float h) {
            LinearGradient g = new LinearGradient(0, 0, 0, h*.55f,
                    Color.rgb(130, 198, 246), Color.rgb(224, 242, 255), Shader.TileMode.CLAMP);
            p.setShader(g); c.drawRect(0, 0, w, h*.55f, p); p.setShader(null);
            p.setColor(Color.rgb(255,220,104)); c.drawCircle(w*.83f, h*.12f, w*.055f, p);
            cloud(c,w*.14f,h*.13f,.055f); cloud(c,w*.52f,h*.08f,.043f);
        }

        private void drawLandscape(Canvas c, float w, float h) {
            p.setColor(Color.rgb(113,181,103));
            Path hill = new Path(); hill.moveTo(0,h*.48f); hill.quadTo(w*.23f,h*.34f,w*.50f,h*.48f);
            hill.quadTo(w*.77f,h*.32f,w,h*.46f); hill.lineTo(w,h); hill.lineTo(0,h); hill.close(); c.drawPath(hill,p);
            p.setColor(Color.rgb(93,164,91));
            Path meadow = new Path(); meadow.moveTo(0,h*.62f); meadow.quadTo(w*.35f,h*.52f,w*.65f,h*.64f);
            meadow.quadTo(w*.82f,h*.55f,w,h*.64f); meadow.lineTo(w,h); meadow.lineTo(0,h); meadow.close(); c.drawPath(meadow,p);
        }

        private void drawPathAndWater(Canvas c,float w,float h){
            p.setColor(Color.rgb(235,205,151));
            Path path=new Path(); path.moveTo(w*.49f,h*.54f); path.cubicTo(w*.42f,h*.64f,w*.58f,h*.76f,w*.51f,h); path.lineTo(w*.60f,h); path.cubicTo(w*.66f,h*.75f,w*.50f,h*.64f,w*.55f,h*.54f); path.close(); c.drawPath(path,p);
            p.setColor(Color.rgb(90,180,207));
            Path creek=new Path(); creek.moveTo(0,h*.69f); creek.cubicTo(w*.22f,h*.63f,w*.62f,h*.82f,w,h*.70f); creek.lineTo(w,h*.78f); creek.cubicTo(w*.62f,h*.91f,w*.24f,h*.74f,0,h*.79f); creek.close(); c.drawPath(creek,p);
            p.setColor(Color.argb(110,255,255,255));
            for(int i=0;i<6;i++) c.drawRoundRect(w*(.08f+i*.17f),h*(.72f+(i%2)*.04f),w*(.13f+i*.17f),h*(.725f+(i%2)*.04f),4,4,p);
        }

        private void drawDecor(Canvas c,float w,float h){
            tree(c,w*.07f,h*.53f,.045f); tree(c,w*.94f,h*.51f,.05f); tree(c,w*.28f,h*.93f,.045f); tree(c,w*.77f,h*.94f,.045f);
            flower(c,w*.34f,h*.68f); flower(c,w*.66f,h*.67f); flower(c,w*.18f,h*.72f);
            p.setColor(Color.rgb(151,105,67)); c.drawRect(w*.42f,h*.665f,w*.58f,h*.69f,p);
            p.setColor(Color.rgb(222,184,102)); c.drawRect(w*.43f,h*.65f,w*.57f,h*.675f,p);
        }

        private void drawBuilding(Canvas c,int i,float l,float top,float r,float b,String label,String icon,int base,boolean exists,boolean unlocked){
            zones[i]=new RectF(l,top,r,b);
            p.setShadowLayer(8,0,5,Color.argb(80,0,0,0));
            p.setColor(Color.argb(255,245,242,236)); c.drawRoundRect(l,top,r,b,18,18,p); p.clearShadowLayer();
            if(unlocked){
                p.setColor(base); c.drawRoundRect(l+3,top+3,r-3,b-3,15,15,p);
                p.setColor(Color.argb(55,255,255,255)); c.drawRoundRect(l+8,top+7,r-8,top+(b-top)*.30f,10,10,p);
                text.setTextAlign(Paint.Align.CENTER); text.setColor(Color.WHITE); text.setTextSize(Math.max(20,getWidth()*.048f)); c.drawText(icon,(l+r)/2,top+(b-top)*.52f,text);
                text.setTextSize(Math.max(9,getWidth()*.021f)); c.drawText(label,(l+r)/2,b-9,text);
            } else {
                p.setColor(Color.rgb(133,133,139)); c.drawRoundRect(l+3,top+3,r-3,b-3,15,15,p);
                text.setTextAlign(Paint.Align.CENTER); text.setColor(Color.WHITE); text.setTextSize(Math.max(20,getWidth()*.045f)); c.drawText("🔒",(l+r)/2,top+(b-top)*.57f,text);
                text.setTextSize(Math.max(8,getWidth()*.018f)); c.drawText(label,(l+r)/2,b-8,text);
            }
        }

        private void drawProgressHud(Canvas c,float w,float h,int score,int stage){
            p.setColor(Color.argb(235,255,255,255)); p.setShadowLayer(7,0,3,Color.argb(70,0,0,0));
            c.drawRoundRect(w*.03f,h*.025f,w*.64f,h*.125f,20,20,p); p.clearShadowLayer();
            text.setTextAlign(Paint.Align.LEFT); text.setColor(Color.rgb(55,45,82)); text.setTextSize(Math.max(11,w*.025f));
            c.drawText("WORLD DEVELOPMENT  "+score+"%",w*.055f,h*.065f,text);
            p.setColor(Color.rgb(226,226,232)); c.drawRoundRect(w*.055f,h*.078f,w*.59f,h*.102f,10,10,p);
            p.setColor(Color.rgb(100,168,111)); c.drawRoundRect(w*.055f,h*.078f,w*(.055f+.535f*score/100f),h*.102f,10,10,p);
            text.setTextSize(Math.max(9,w*.019f)); c.drawText("Growth stage "+(stage+1)+" • Balanced care + learning unlock the world",w*.055f,h*.119f,text);
        }

        private void drawPet(Canvas c,float w,float h){
            float bob=(float)Math.sin(System.currentTimeMillis()/1600.0)*2.5f;
            PetEvolutionManager.PetVariant pet=PetEvolutionManager.current(a);
            float px=w*petX, py=h*petY+bob;
            p.setColor(Color.argb(55,0,0,0)); c.drawOval(px-w*.038f,py+h*.015f,px+w*.038f,py+h*.031f,p);
            text.setTextAlign(Paint.Align.CENTER); text.setTextSize(Math.max(34,w*.082f)); text.setColor(Color.DKGRAY); c.drawText(pet.emoji,px,py,text);
            text.setTextSize(Math.max(11,w*.025f)); c.drawText(PetEvolutionManager.name(a)+" • Lv "+pet.level,px,py+h*.045f,text);
        }

        private void tree(Canvas c,float x,float y,float s){
            p.setColor(Color.rgb(119,78,49)); c.drawRoundRect(x-s*.15f,y,x+s*.15f,y+s*1.5f,5,5,p);
            p.setColor(Color.rgb(57,139,75)); c.drawCircle(x,y-s*.25f,s,p); c.drawCircle(x-s*.55f,y,s*.65f,p); c.drawCircle(x+s*.55f,y,s*.65f,p);
            p.setColor(Color.argb(60,255,255,255)); c.drawCircle(x-s*.25f,y-s*.35f,s*.18f,p);
        }
        private void flower(Canvas c,float x,float y){p.setColor(Color.rgb(72,128,61));c.drawRect(x-1,y,x+1,y+9,p);p.setColor(Color.rgb(244,145,175));for(int i=0;i<5;i++){double q=i*Math.PI*2/5;c.drawCircle(x+(float)Math.cos(q)*5,y+(float)Math.sin(q)*5,3,p);}p.setColor(Color.rgb(247,207,76));c.drawCircle(x,y,3,p);}
        private void cloud(Canvas c,float x,float y,float s){p.setColor(Color.argb(205,255,255,255));c.drawCircle(x,y,s,p);c.drawCircle(x+s*.7f,y+s*.1f,s*.72f,p);c.drawCircle(x-s*.7f,y+s*.12f,s*.62f,p);}

        @Override public boolean onTouchEvent(MotionEvent e){
            if(e.getAction()!=MotionEvent.ACTION_UP)return true;
            float x=e.getX(),y=e.getY();
            for(int i=0;i<zones.length;i++) if(zones[i]!=null&&zones[i].contains(x,y)){visit(i);return true;}
            petX=Math.max(.12f,Math.min(.88f,x/getWidth())); petY=Math.max(.18f,Math.min(.70f,y/getHeight())); invalidate(); return true;
        }

        private boolean unlocked(int i){
            int score=PetEvolutionManager.balancedDevelopmentScore(a);
            int[] need={0,10,20,30,40,50,60,70,80}; return score>=need[i];
        }

        private void visit(int i){
            if(!unlocked(i)){int[] need={0,10,20,30,40,50,60,70,80}; new AlertDialog.Builder(a).setTitle("🔒 "+names[i]).setMessage("This area develops at "+need[i]+"% balanced world development.\n\nCare, play, Python learning, school, exploration and routines all contribute equally.").setPositiveButton("Keep growing",null).show();return;}
            switch(i){
                case 0: home(); break;
                case 1: PypetSchoolView.show(a); break;
                case 2: PypetHatcheryView.show(a); break;
                case 3: PetEvolutionManager.performExplore(a); PypetAchievementManager.recordDailyActivity(a); TreasureStore.show(a); break;
                case 4: playPark(); break;
                case 5: PypetSchoolView.show(a); break;
                case 6: TreasureStore.show(a); break;
                case 7: TreasureStore.show(a); break;
                case 8: TrophyCabinetView.show(a); break;
            }
        }

        private void home(){
            String msg="🏠 Home is your pet's base.\n\n❤️ Health "+PetEvolutionManager.health(a)+"%   😊 Happiness "+PetEvolutionManager.happiness(a)+"%\n🍖 Hunger "+PetEvolutionManager.hunger(a)+"%\n\nChoose an activity:";
            new AlertDialog.Builder(a).setTitle("🏠 Home").setMessage(msg)
                .setPositiveButton("🧼 Care",(d,w)->{PetEvolutionManager.performCare(a);PypetAchievementManager.recordDailyActivity(a);})
                .setNeutralButton("🎾 Play",(d,w)->{PetEvolutionManager.playWith(a);PypetAchievementManager.recordDailyActivity(a);})
                .setNegativeButton("🍎 Feed",(d,w)->PetEvolutionManager.feed(a,PetEvolutionManager.foods().get(0))).show();
        }
        private void playPark(){
            new AlertDialog.Builder(a).setTitle("🎡 Play Park").setMessage("Play a calm activity with your pet. Every session raises happiness and play development.")
                .setPositiveButton("🎾 Play",(d,w)->{PetEvolutionManager.playWith(a);PypetAchievementManager.recordDailyActivity(a);})
                .setNegativeButton("Back",null).show();
        }
    }
}
