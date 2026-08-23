package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

/** Player-facing town: recognizable architecture, persistent decoration placement, and visible accomplishments. */
public final class WorldMapView {
    private WorldMapView() {}

    public static void show(Activity a) {
        WorldCanvas world = new WorldCanvas(a);
        LinearLayout root = new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(226,238,226));
        TextView title = new TextView(a); title.setText("🌎 " + PypetProfileManager.townName(a) + " • PYPET WORLD"); title.setTextSize(22); title.setGravity(Gravity.CENTER); title.setPadding(6,6,6,2); root.addView(title,new LinearLayout.LayoutParams(-1,52));
        TextView help = new TextView(a); help.setText("Explore • pinch to zoom • drag to look around • tap buildings • use Build to show your accomplishments"); help.setGravity(Gravity.CENTER); help.setTextSize(13); root.addView(help,new LinearLayout.LayoutParams(-1,42));
        root.addView(world,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout actions = new LinearLayout(a); actions.setPadding(6,2,6,4);
        Button build = new Button(a); build.setText("🏗 Build & Place"); build.setAllCaps(false); actions.addView(build,new LinearLayout.LayoutParams(0,52,1));
        Button trophies = new Button(a); trophies.setText("🏆 Show Off"); trophies.setAllCaps(false); actions.addView(trophies,new LinearLayout.LayoutParams(0,52,1));
        Button settings = new Button(a); settings.setText("⚙️"); settings.setAllCaps(false); actions.addView(settings,new LinearLayout.LayoutParams(0,52,.65f));
        root.addView(actions);
        AlertDialog d = new AlertDialog.Builder(a).setView(root).create();
        build.setOnClickListener(v -> showBuildDialog(a, world));
        trophies.setOnClickListener(v -> showTrophyDialog(a, world));
        settings.setOnClickListener(v -> PypetSettingsView.show(a,new PypetAudio(),new PypetSafetyGuard(a)));
        d.setOnDismissListener(x -> world.stop()); d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);}
    }

    private static void showBuildDialog(Activity a, WorldCanvas world) {
        List<RewardCatalog.Item> owned = new ArrayList<>();
        for (RewardCatalog.Item item : RewardCatalog.all()) if (RewardInventory.owns(a,item.id)) owned.add(item);
        String[] names = new String[owned.size() + 1];
        names[0] = "Place an owned item…";
        for(int i=0;i<owned.size();i++) names[i+1] = "📍 " + owned.get(i).name;
        AlertDialog dialog = new AlertDialog.Builder(a).setTitle("🏗 Build & Place").setMessage(owned.isEmpty() ? "Unlock World items in the Market first. Your earned trophies are displayed automatically in the town plaza." : "Choose an owned item. It will be placed on an appropriate floor, garden, or display area and can then be dragged around the World.").setSingleChoiceItems(names,0,(d,which)->{
            if(which==0)return;
            RewardCatalog.Item item=owned.get(which-1); world.addPlacement(item.id); d.dismiss();
            Toast.makeText(a,item.name+" placed in your World. Drag it to personalize your town.",Toast.LENGTH_SHORT).show();
        }).setNegativeButton("Close",null).create();
        dialog.show();
    }

    private static void showTrophyDialog(Activity a, WorldCanvas world) {
        StringBuilder s=new StringBuilder();
        int shown=0;
        for(PypetAchievementManager.Trophy t:PypetAchievementManager.trophies()) if(PypetAchievementManager.hasTrophy(a,t.id)) {s.append("🏆 ").append(t.name).append("\n").append(t.description).append("\n\n");shown++;}
        if(shown==0)s.append("Your first accomplishment will appear here as a trophy when you start learning, caring, and building.");
        new AlertDialog.Builder(a).setTitle("🏆 Show Off — Hall of Accomplishments").setMessage(s.toString()).setPositiveButton("Close",null).show();
    }

    private static final class WorldCanvas extends View {
        private final Activity a; private final Paint p=new Paint(3),t=new Paint(3); private float zoom=1f,ox=0,oy=0,lastX,lastY,lastDist; private boolean dragging,run=true; private long time=System.currentTimeMillis();
        private final RectF[] buildings=new RectF[6];
        private final List<WorldPlacementManager.Placement> placements=new ArrayList<>();
        private String movingId=null; private int movingIndex=-1;
        WorldCanvas(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);placements.addAll(WorldPlacementManager.all(a));}
        void stop(){run=false;}
        void addPlacement(String id){float x=0,y=getHeight()*.18f; WorldPlacementManager.place(a,id,x,y,1f,0);placements.clear();placements.addAll(WorldPlacementManager.all(a));invalidate();}
        @Override protected void onSizeChanged(int w,int h,int oldw,int oldh){super.onSizeChanged(w,h);invalidate();}
        @Override protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(139,203,230));c.save();c.translate(w/2+ox,h/2+oy);c.scale(zoom,zoom);drawTown(c,w/zoom,h/zoom);c.restore();if(run)postInvalidateDelayed(120);}

        private void drawTown(Canvas c,float w,float h){
            p.setColor(Color.rgb(118,178,96));c.drawRect(-w,-h,w,h,p);
            drawWater(c,w,h);drawRoads(c,w,h);drawSidewalks(c,w,h);
            house(c,-w*.72f,-h*.43f,w*.02f,-h*.04f,"HOME",Color.rgb(157,78,61),0);
            academy(c,w*.17f,-h*.52f,w*.76f,-h*.03f,1);
            market(c,w*.45f,h*.10f,w*.83f,h*.47f,2);
            house(c,-w*.82f,h*.10f,-w*.30f,h*.48f,"WORKSHOP",Color.rgb(48,116,137),3);
            park(c,-w*.19f,h*.18f,w*.25f,h*.62f,4);
            house(c,w*.48f,h*.54f,w*.88f,h*.88f,"LIBRARY",Color.rgb(99,70,130),5);
            trees(c,w,h);drawTrophies(c,w,h);drawPlacements(c);pet(c,w,h);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(28);t.setColor(Color.rgb(42,70,45));c.drawText(PypetProfileManager.townName(a),0,-h*.84f,t);
            t.setTextSize(15);t.setColor(Color.rgb(55,75,58));c.drawText("Your accomplishments live here",0,-h*.77f,t);
        }

        private void drawWater(Canvas c,float w,float h){p.setColor(Color.rgb(78,166,194));c.drawOval(-w*.98f,h*.66f,w*.05f,h*1.08f,p);p.setColor(Color.rgb(190,230,233));p.setStrokeWidth(3);for(int i=0;i<5;i++)c.drawLine(-w*.84f+i*55,h*.79f,-w*.58f+i*55,h*.79f,p);}
        private void drawRoads(Canvas c,float w,float h){p.setColor(Color.rgb(72,72,70));Path v=new Path();v.moveTo(-w*.075f,-h);v.lineTo(w*.075f,-h);v.lineTo(w*.19f,h);v.lineTo(-w*.19f,h);v.close();c.drawPath(v,p);c.drawRoundRect(-w*.92f,-h*.015f,w*.92f,h*.09f,22,22,p);p.setColor(Color.rgb(239,215,135));p.setStrokeWidth(4);for(int y=-2;y<2;y++)c.drawLine(-w*.88f,y*h*.5f,w*.88f,y*h*.5f,p);}
        private void drawSidewalks(Canvas c,float w,float h){p.setColor(Color.rgb(203,196,176));c.drawRect(-w*.10f,-h,w*.10f,-h*.55f,p);c.drawRect(-w*.10f,h*.08f,w*.10f,h*.60f,p);}

        private void house(Canvas c,float l,float top,float r,float b,String name,int roof,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2,ww=r-l,hh=b-top;
            p.setShadowLayer(12,0,6,Color.argb(70,0,0,0));p.setColor(Color.rgb(244,238,221));c.drawRoundRect(l,top,r,b,12,12,p);p.clearShadowLayer();
            p.setColor(roof);Path q=new Path();q.moveTo(l-18,top+7);q.lineTo(mid,top-hh*.36f);q.lineTo(r+18,top+7);q.close();c.drawPath(q,p);
            p.setColor(Color.rgb(94,73,62));c.drawRect(l+ww*.10f,top+hh*.58f,l+ww*.19f,top+hh*.79f,p);c.drawRect(r-ww*.19f,top+hh*.58f,r-ww*.10f,top+hh*.79f,p);
            window(c,l+ww*.14f,top+hh*.23f,ww*.18f,hh*.18f);window(c,r-ww*.32f,top+hh*.23f,ww*.18f,hh*.18f);
            p.setColor(Color.rgb(104,72,52));c.drawRoundRect(mid-22,b-68,mid+22,b,6,6,p);p.setColor(Color.rgb(230,201,145));c.drawCircle(mid+12,b-35,3,p);
            p.setColor(Color.rgb(218,202,174));c.drawRect(l-10,b-8,r+10,b+3,p);
            t.setTextSize(name.equals("WORKSHOP")?16:18);t.setColor(Color.rgb(45,52,47));t.setTextAlign(Paint.Align.CENTER);c.drawText(name,mid,top-hh*.39f,t);
        }
        private void window(Canvas c,float x,float y,float ww,float hh){p.setColor(Color.rgb(91,165,201));c.drawRoundRect(x,y,x+ww,y+hh,4,4,p);p.setColor(Color.rgb(224,234,225));p.setStrokeWidth(2);c.drawLine(x+ww/2,y,x+ww/2,y+hh,p);c.drawLine(x,y+hh/2,x+ww,y+hh/2,p);}
        private void academy(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2,ww=r-l,hh=b-top;p.setShadowLayer(14,0,7,Color.argb(75,0,0,0));p.setColor(Color.rgb(235,234,221));c.drawRoundRect(l,top,r,b,16,16,p);p.clearShadowLayer();p.setColor(Color.rgb(61,91,142));Path roof=new Path();roof.moveTo(l-12,top+5);roof.lineTo(mid,top-hh*.40f);roof.lineTo(r+12,top+5);roof.close();c.drawPath(roof,p);for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+24+col*(ww-48)/5,top+34+row*82,28,38);p.setColor(Color.rgb(95,67,48));c.drawRoundRect(mid-24,b-66,mid+24,b,7,7,p);t.setTextSize(18);t.setColor(Color.rgb(43,52,57));t.setTextAlign(Paint.Align.CENTER);c.drawText("PYTHON ACADEMY",mid,top-hh*.44f,t);}
        private void market(Canvas c,float l,float top,float r,float b,int i){house(c,l,top,r,b,"MARKET",Color.rgb(205,136,55),i);}
        private void park(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);p.setColor(Color.rgb(77,151,82));c.drawRoundRect(l,top,r,b,30,30,p);p.setColor(Color.rgb(118,82,50));c.drawRect((l+r)/2-6,top+42,(l+r)/2+6,b-42,p);p.setColor(Color.rgb(54,135,62));c.drawCircle((l+r)/2,top+35,43,p);p.setColor(Color.rgb(242,174,58));c.drawCircle(l+(r-l)*.30f,top+(b-top)*.56f,18,p);t.setTextSize(19);t.setColor(Color.WHITE);t.setTextAlign(Paint.Align.CENTER);c.drawText("PARK",(l+r)/2,b-18,t);}
        private void trees(Canvas c,float w,float h){for(int i=-2;i<=2;i++){float x=i*w*.32f,y=-h*.10f+i*20;p.setColor(Color.rgb(113,76,49));c.drawRect(x-7,y,x+7,y+42,p);p.setColor(Color.rgb(52,131,63));c.drawCircle(x,y,34,p);p.setColor(Color.rgb(76,153,75));c.drawCircle(x-12,y-8,22,p);}}

        private void drawTrophies(Canvas c,float w,float h){int index=0;for(PypetAchievementManager.Trophy trophy:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,trophy.id))continue;float x=-w*.62f+(index%5)*w*.18f;float y=h*.60f+(index/5)*70;p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-18,y-28,x+18,y+10,6,6,p);p.setColor(Color.rgb(255,239,159));c.drawCircle(x,y-10,11,p);t.setTextSize(10);t.setColor(Color.rgb(69,58,35));t.setTextAlign(Paint.Align.CENTER);c.drawText("🏆",x,y-6,t);t.setTextSize(9);t.setColor(Color.WHITE);c.drawText(trophy.name,x,y+24,t);index++;}}

        private void drawPlacements(Canvas c){int index=0;for(WorldPlacementManager.Placement item:placements){float x=item.x,y=item.y; c.save();c.translate(x,y);c.rotate(item.rotation);c.scale(item.scale,item.scale);drawItem(c,item.id);c.restore();index++;}}
        private void drawItem(Canvas c,String id){RewardCatalog.Item item=RewardCatalog.byId(id);if(item==null)return;boolean building=item.name.contains("Library")||item.name.contains("Workshop")||item.name.contains("Studio")||item.name.contains("Cafe")||item.name.contains("Lab")||item.name.contains("Observatory")||item.name.contains("Planetarium")||item.name.contains("Palace")||item.name.contains("Cabin")||item.name.contains("Campground");p.setShadowLayer(7,0,4,Color.argb(65,0,0,0));p.setColor(building?Color.rgb(230,222,198):Color.rgb(196,151,82));c.drawRoundRect(-34,-25,34,25,8,8,p);p.clearShadowLayer();if(building){p.setColor(Color.rgb(99,78,64));c.drawRect(-12,-20,12,25,p);window(c,-27,-15,16,15);window(c,11,-15,16,15);}else{p.setColor(Color.rgb(63,133,72));c.drawCircle(0,-8,20,p);}t.setTextSize(9);t.setColor(Color.DKGRAY);t.setTextAlign(Paint.Align.CENTER);c.drawText(item.name,0,42,t);}
        private void pet(Canvas c,float w,float h){float x=w*.02f,y=h*.08f+(float)Math.sin((System.currentTimeMillis()-time)/650.0)*4;String species=PetEvolutionManager.current(a).displayName.toLowerCase();if(species.contains("frog"))drawFrog(c,x,y);else drawGenericPet(c,x,y);t.setTextSize(15);t.setColor(Color.DKGRAY);t.setTextAlign(Paint.Align.CENTER);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+66,t);}
        private void drawFrog(Canvas c,float x,float y){p.setColor(Color.rgb(76,164,70));c.drawOval(x-31,y-8,x+31,y+42,p);c.drawOval(x-29,y+28,x-10,y+58,p);c.drawOval(x+10,y+28,x+29,y+58,p);p.setColor(Color.rgb(93,190,80));c.drawCircle(x-20,y-14,14,p);c.drawCircle(x+20,y-14,14,p);p.setColor(Color.WHITE);c.drawCircle(x-20,y-15,7,p);c.drawCircle(x+20,y-15,7,p);p.setColor(Color.BLACK);c.drawCircle(x-20,y-15,3,p);c.drawCircle(x+20,y-15,3,p);}
        private void drawGenericPet(Canvas c,float x,float y){p.setColor(Color.rgb(181,139,94));c.drawOval(x-27,y-8,x+27,y+38,p);c.drawCircle(x,y-12,26,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(x-9,y-14,3,p);c.drawCircle(x+9,y-14,3,p);}

        @Override public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0){zoom=Math.max(.55f,Math.min(3.0f,zoom*d/lastDist));invalidate();}lastDist=d;return true;}lastDist=0;if(e.getAction()==MotionEvent.ACTION_DOWN){lastX=e.getX();lastY=e.getY();dragging=true;return true;}if(e.getAction()==MotionEvent.ACTION_MOVE&&dragging){float dx=e.getX()-lastX,dy=e.getY()-lastY;ox+=dx;oy+=dy;lastX=e.getX();lastY=e.getY();invalidate();return true;}if(e.getAction()==MotionEvent.ACTION_UP){dragging=false;float wx=(e.getX()-getWidth()/2-ox)/zoom,wy=(e.getY()-getHeight()/2-oy)/zoom;for(int i=0;i<buildings.length;i++)if(buildings[i]!=null&&buildings[i].contains(wx,wy)){enter(i);return true;}return true;}return true;}
        private float dist(MotionEvent e){return e.getPointerCount()<2?0:(float)Math.hypot(e.getX(0)-e.getX(1),e.getY(0)-e.getY(1));}
        private void enter(int i){switch(i){case 0:new AlertDialog.Builder(a).setTitle("🏠 HOME").setMessage("Rooms, care, rest and study with your pet. Your accomplishments can be displayed around town.").setPositiveButton("Close",null).show();break;case 1:PypetSchoolView.show(a);break;case 2:Toast.makeText(a,"Market: World items and supplies",Toast.LENGTH_SHORT).show();break;case 3:Toast.makeText(a,"Workshop: build and customize your town",Toast.LENGTH_SHORT).show();break;case 4:Toast.makeText(a,"Park: play with your pet",Toast.LENGTH_SHORT).show();break;case 5:Toast.makeText(a,"Library: lessons, references and Python discoveries",Toast.LENGTH_SHORT).show();}}
    }
}
