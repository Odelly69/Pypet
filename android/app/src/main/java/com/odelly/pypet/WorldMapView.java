package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

/** Player-facing town. The whole World is zoomable/pannable; buildings are deliberately town-scale, not screen-scale. */
public final class WorldMapView {
    private WorldMapView() {}

    public static void show(Activity a) {
        final WorldCanvas world = new WorldCanvas(a);
        FrameLayout frame = new FrameLayout(a);
        frame.setBackgroundColor(Color.rgb(226,238,226));
        frame.addView(world, new FrameLayout.LayoutParams(-1,-1));

        LinearLayout top = new LinearLayout(a); top.setOrientation(LinearLayout.VERTICAL); top.setPadding(8,6,8,4); top.setBackgroundColor(Color.argb(235,244,241,226));
        TextView title = new TextView(a); title.setText("🌎 " + PypetProfileManager.townName(a) + " • PYPET WORLD"); title.setTextSize(20); title.setGravity(Gravity.CENTER); top.addView(title,new LinearLayout.LayoutParams(-1,42));
        TextView help = new TextView(a); help.setText("Pinch or use +/− to zoom the WHOLE WORLD • drag to look around • tap buildings"); help.setGravity(Gravity.CENTER); help.setTextSize(12); top.addView(help,new LinearLayout.LayoutParams(-1,34));
        frame.addView(top,new FrameLayout.LayoutParams(-1,80,Gravity.TOP));

        LinearLayout controls = new LinearLayout(a); controls.setGravity(Gravity.CENTER); controls.setPadding(8,5,8,5); controls.setBackgroundColor(Color.argb(235,244,241,226));
        Button zoomOut = new Button(a); zoomOut.setText("−"); zoomOut.setTextSize(24); zoomOut.setAllCaps(false); controls.addView(zoomOut,new LinearLayout.LayoutParams(70,58));
        Button reset = new Button(a); reset.setText("🌎 Reset View"); reset.setAllCaps(false); controls.addView(reset,new LinearLayout.LayoutParams(0,58,1));
        Button zoomIn = new Button(a); zoomIn.setText("+"); zoomIn.setTextSize(24); zoomIn.setAllCaps(false); controls.addView(zoomIn,new LinearLayout.LayoutParams(70,58));
        Button build = new Button(a); build.setText("🏗 Build"); build.setAllCaps(false); controls.addView(build,new LinearLayout.LayoutParams(0,58,1));
        Button trophies = new Button(a); trophies.setText("🏆 Show Off"); trophies.setAllCaps(false); controls.addView(trophies,new LinearLayout.LayoutParams(0,58,1));
        frame.addView(controls,new FrameLayout.LayoutParams(-1,66,Gravity.BOTTOM));

        AlertDialog d = new AlertDialog.Builder(a).setView(frame).create();
        zoomOut.setOnClickListener(v -> world.adjustZoom(-0.12f));
        zoomIn.setOnClickListener(v -> world.adjustZoom(0.12f));
        reset.setOnClickListener(v -> world.resetView());
        build.setOnClickListener(v -> showBuildDialog(a,world));
        trophies.setOnClickListener(v -> showTrophyDialog(a));
        d.setOnDismissListener(x -> world.stop()); d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);}
    }

    private static void showBuildDialog(Activity a, WorldCanvas world) {
        List<RewardCatalog.Item> owned=new ArrayList<>(); for(RewardCatalog.Item item:RewardCatalog.all()) if(RewardInventory.owns(a,item.id)) owned.add(item);
        String[] names=new String[owned.size()+1]; names[0]="Place an owned item…"; for(int i=0;i<owned.size();i++) names[i+1]="📍 "+owned.get(i).name;
        new AlertDialog.Builder(a).setTitle("🏗 Build & Place").setMessage(owned.isEmpty()?"Unlock World items in the Market first. Earned trophies appear automatically.":"Choose an owned item. Buildings use building lots; decorations use gardens, rooms or display areas. Then drag it to personalize your town.").setSingleChoiceItems(names,0,(d,w)->{if(w==0)return;RewardCatalog.Item item=owned.get(w-1);world.addPlacement(item.id);d.dismiss();Toast.makeText(a,item.name+" placed in your World.",Toast.LENGTH_SHORT).show();}).setNegativeButton("Close",null).show();
    }
    private static void showTrophyDialog(Activity a) {
        StringBuilder s=new StringBuilder();int shown=0;for(PypetAchievementManager.Trophy t:PypetAchievementManager.trophies())if(PypetAchievementManager.hasTrophy(a,t.id)){s.append("🏆 ").append(t.name).append("\n").append(t.description).append("\n\n");shown++;}if(shown==0)s.append("Your first accomplishment will become a visible trophy when you start learning, caring and building.");new AlertDialog.Builder(a).setTitle("🏆 Show Off — Hall of Accomplishments").setMessage(s.toString()).setPositiveButton("Close",null).show();
    }

    private static final class WorldCanvas extends View {
        private static final float WORLD_W=1600f, WORLD_H=2200f, MIN_ZOOM=.45f, MAX_ZOOM=2.5f;
        private final Activity a; private final Paint p=new Paint(3),t=new Paint(3); private float zoom=.68f,ox=0,oy=0,lastX,lastY,lastDist; private boolean dragging,run=true; private long time=System.currentTimeMillis();
        private final RectF[] buildings=new RectF[6]; private final List<WorldPlacementManager.Placement> placements=new ArrayList<>();
        WorldCanvas(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);placements.addAll(WorldPlacementManager.all(a));}
        void stop(){run=false;}
        void resetView(){zoom=.68f;ox=0;oy=0;invalidate();}
        void adjustZoom(float delta){zoom=Math.max(MIN_ZOOM,Math.min(MAX_ZOOM,zoom+delta));invalidate();}
        void addPlacement(String id){float x=0,y=0;WorldPlacementManager.place(a,id,x,y,1f,0);placements.clear();placements.addAll(WorldPlacementManager.all(a));invalidate();}
        @Override protected void onSizeChanged(int w,int h,int oldw,int oldh){super.onSizeChanged(w,h,oldw,oldh);invalidate();}
        @Override protected void onDraw(Canvas c){float sx=getWidth()/WORLD_W,sy=getHeight()/WORLD_H;float fit=Math.min(sx,sy)*zoom;c.drawColor(Color.rgb(139,203,230));c.save();c.translate(getWidth()/2f+ox,getHeight()/2f+oy);c.scale(fit,fit);drawTown(c);c.restore();if(run)postInvalidateDelayed(120);}
        private void drawTown(Canvas c){float w=WORLD_W,h=WORLD_H;p.setColor(Color.rgb(118,178,96));c.drawRect(-w/2,-h/2,w/2,h/2,p);drawWater(c,w,h);drawRoads(c,w,h);drawSidewalks(c,w,h);
            house(c,-620,-780,-160,-350,"HOME",Color.rgb(157,78,61),0);academy(c,180,-800,620,-330,1);market(c,450,-250,740,40,2);house(c,-700,-240,-320,80,"WORKSHOP",Color.rgb(48,116,137),3);park(c,-250,100,230,560,4);house(c,420,500,760,830,"LIBRARY",Color.rgb(99,70,130),5);trees(c,w,h);drawTrophies(c);drawPlacements(c);pet(c);t.setTextAlign(Paint.Align.CENTER);t.setTextSize(46);t.setColor(Color.rgb(42,70,45));c.drawText(PypetProfileManager.townName(a),0,-1010,t);t.setTextSize(25);c.drawText("Your accomplishments live here",0,-940,t);
        }
        private void drawWater(Canvas c,float w,float h){p.setColor(Color.rgb(78,166,194));c.drawOval(-790,610,-80,1100,p);}
        private void drawRoads(Canvas c,float w,float h){p.setColor(Color.rgb(72,72,70));c.drawRect(-110,-1100,110,1100,p);c.drawRoundRect(-760,-90,760,20,30,30,p);p.setColor(Color.rgb(239,215,135));p.setStrokeWidth(6);for(int y=-2;y<2;y++)c.drawLine(-720,y*480,720,y*480,p);}
        private void drawSidewalks(Canvas c,float w,float h){p.setColor(Color.rgb(203,196,176));c.drawRect(-150,-1100,150,-880,p);c.drawRect(-150,120,150,580,p);}
        private void house(Canvas c,float l,float top,float r,float b,String name,int roof,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2,ww=r-l,hh=b-top;p.setShadowLayer(12,0,6,Color.argb(70,0,0,0));p.setColor(Color.rgb(244,238,221));c.drawRoundRect(l,top,r,b,18,18,p);p.clearShadowLayer();p.setColor(roof);Path q=new Path();q.moveTo(l-24,top+8);q.lineTo(mid,top-hh*.30f);q.lineTo(r+24,top+8);q.close();c.drawPath(q,p);p.setColor(Color.rgb(94,73,62));c.drawRect(l+ww*.12f,top+hh*.55f,l+ww*.20f,top+hh*.77f,p);c.drawRect(r-ww*.20f,top+hh*.55f,r-ww*.12f,top+hh*.77f,p);window(c,l+ww*.14f,top+hh*.22f,ww*.18f,hh*.17f);window(c,r-ww*.32f,top+hh*.22f,ww*.18f,hh*.17f);p.setColor(Color.rgb(104,72,52));c.drawRoundRect(mid-24,b-70,mid+24,b,7,7,p);t.setTextSize(24);t.setColor(Color.rgb(45,52,47));t.setTextAlign(Paint.Align.CENTER);c.drawText(name,mid,top-hh*.32f,t);}
        private void window(Canvas c,float x,float y,float ww,float hh){p.setColor(Color.rgb(91,165,201));c.drawRoundRect(x,y,x+ww,y+hh,5,5,p);p.setColor(Color.rgb(224,234,225));p.setStrokeWidth(3);c.drawLine(x+ww/2,y,x+ww/2,y+hh,p);c.drawLine(x,y+hh/2,x+ww,y+hh/2,p);}
        private void academy(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2,ww=r-l,hh=b-top;p.setShadowLayer(14,0,7,Color.argb(75,0,0,0));p.setColor(Color.rgb(235,234,221));c.drawRoundRect(l,top,r,b,20,20,p);p.clearShadowLayer();p.setColor(Color.rgb(61,91,142));Path roof=new Path();roof.moveTo(l-16,top+5);roof.lineTo(mid,top-hh*.32f);roof.lineTo(r+16,top+5);roof.close();c.drawPath(roof,p);for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+28+col*(ww-56)/5,top+38+row*85,34,42);p.setColor(Color.rgb(95,67,48));c.drawRoundRect(mid-25,b-70,mid+25,b,7,7,p);t.setTextSize(24);t.setColor(Color.rgb(43,52,57));t.setTextAlign(Paint.Align.CENTER);c.drawText("PYTHON ACADEMY",mid,top-hh*.34f,t);}
        private void market(Canvas c,float l,float top,float r,float b,int i){house(c,l,top,r,b,"MARKET",Color.rgb(205,136,55),i);}
        private void park(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);p.setColor(Color.rgb(77,151,82));c.drawRoundRect(l,top,r,b,30,30,p);p.setColor(Color.rgb(118,82,50));c.drawRect((l+r)/2-8,top+55,(l+r)/2+8,b-55,p);p.setColor(Color.rgb(54,135,62));c.drawCircle((l+r)/2,top+45,55,p);t.setTextSize(22);t.setColor(Color.WHITE);t.setTextAlign(Paint.Align.CENTER);c.drawText("PARK",(l+r)/2,b-20,t);}
        private void trees(Canvas c,float w,float h){for(int i=-2;i<=2;i++){float x=i*260,y=-140+i*45;p.setColor(Color.rgb(113,76,49));c.drawRect(x-8,y,x+8,y+45,p);p.setColor(Color.rgb(52,131,63));c.drawCircle(x,y,36,p);p.setColor(Color.rgb(76,153,75));c.drawCircle(x-12,y-8,22,p);}}
        private void drawTrophies(Canvas c){int index=0;for(PypetAchievementManager.Trophy trophy:PypetAchievementManager.trophies()){if(!PypetAchievementManager.hasTrophy(a,trophy.id))continue;float x=-650+(index%5)*180,y=700+(index/5)*80;p.setColor(Color.rgb(224,181,64));c.drawRoundRect(x-22,y-32,x+22,y+14,7,7,p);p.setColor(Color.rgb(255,239,159));c.drawCircle(x,y-12,14,p);t.setTextSize(13);t.setColor(Color.WHITE);t.setTextAlign(Paint.Align.CENTER);c.drawText(trophy.name,x,y+32,t);index++;}}
        private void drawPlacements(Canvas c){for(WorldPlacementManager.Placement item:placements){c.save();c.translate(item.x,item.y);c.rotate(item.rotation);c.scale(item.scale,item.scale);drawItem(c,item.id);c.restore();}}
        private void drawItem(Canvas c,String id){RewardCatalog.Item item=RewardCatalog.byId(id);if(item==null)return;boolean building=item.name.contains("Library")||item.name.contains("Workshop")||item.name.contains("Studio")||item.name.contains("Cafe")||item.name.contains("Lab")||item.name.contains("Observatory")||item.name.contains("Planetarium")||item.name.contains("Palace")||item.name.contains("Cabin")||item.name.contains("Campground");p.setShadowLayer(7,0,4,Color.argb(65,0,0,0));p.setColor(building?Color.rgb(230,222,198):Color.rgb(196,151,82));c.drawRoundRect(-34,-25,34,25,8,8,p);p.clearShadowLayer();if(building){p.setColor(Color.rgb(99,78,64));c.drawRect(-12,-20,12,25,p);window(c,-27,-15,16,15);window(c,11,-15,16,15);}else{p.setColor(Color.rgb(63,133,72));c.drawCircle(0,-8,20,p);}t.setTextSize(10);t.setColor(Color.DKGRAY);t.setTextAlign(Paint.Align.CENTER);c.drawText(item.name,0,42,t);}
        private void pet(Canvas c){float x=0,y=170+(float)Math.sin((System.currentTimeMillis()-time)/650.0)*7;String species=PetEvolutionManager.current(a).displayName.toLowerCase();if(species.contains("frog"))drawFrog(c,x,y);else drawGenericPet(c,x,y);t.setTextSize(22);t.setColor(Color.DKGRAY);t.setTextAlign(Paint.Align.CENTER);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+90,t);}
        private void drawFrog(Canvas c,float x,float y){p.setColor(Color.rgb(76,164,70));c.drawOval(x-38,y-10,x+38,y+50,p);c.drawOval(x-34,y+38,x-12,y+72,p);c.drawOval(x+12,y+38,x+34,y+72,p);p.setColor(Color.rgb(93,190,80));c.drawCircle(x-24,y-18,17,p);c.drawCircle(x+24,y-18,17,p);p.setColor(Color.WHITE);c.drawCircle(x-24,y-19,8,p);c.drawCircle(x+24,y-19,8,p);p.setColor(Color.BLACK);c.drawCircle(x-24,y-19,4,p);c.drawCircle(x+24,y-19,4,p);}
        private void drawGenericPet(Canvas c,float x,float y){p.setColor(Color.rgb(181,139,94));c.drawOval(x-34,y-10,x+34,y+48,p);c.drawCircle(x,y-16,32,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(x-11,y-18,4,p);c.drawCircle(x+11,y-18,4,p);}
        @Override public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0){float factor=d/lastDist;zoom=Math.max(MIN_ZOOM,Math.min(MAX_ZOOM,zoom*factor));}lastDist=d;invalidate();return true;}lastDist=0;switch(e.getActionMasked()){case MotionEvent.ACTION_DOWN:lastX=e.getX();lastY=e.getY();dragging=true;return true;case MotionEvent.ACTION_MOVE:if(dragging){ox+=e.getX()-lastX;oy+=e.getY()-lastY;lastX=e.getX();lastY=e.getY();invalidate();}return true;case MotionEvent.ACTION_UP:dragging=false;float fit=Math.min(getWidth()/WORLD_W,getHeight()/WORLD_H)*zoom;float wx=(e.getX()-getWidth()/2f-ox)/fit,wy=(e.getY()-getHeight()/2f-oy)/fit;for(int i=0;i<buildings.length;i++)if(buildings[i]!=null&&buildings[i].contains(wx,wy)){enter(i);break;}return true;}return true;}
        private float dist(MotionEvent e){return e.getPointerCount()<2?0:(float)Math.hypot(e.getX(0)-e.getX(1),e.getY(0)-e.getY(1));}
        private void enter(int i){switch(i){case 0:new AlertDialog.Builder(a).setTitle("🏠 HOME").setMessage("Rooms, care, rest and study with your pet. Your accomplishments can be displayed around town.").setPositiveButton("Close",null).show();break;case 1:PypetSchoolView.show(a);break;case 2:Toast.makeText(a,"Market: World items and supplies",Toast.LENGTH_SHORT).show();break;case 3:Toast.makeText(a,"Workshop: build and customize your town",Toast.LENGTH_SHORT).show();break;case 4:Toast.makeText(a,"Park: play with your pet",Toast.LENGTH_SHORT).show();break;case 5:Toast.makeText(a,"Library: lessons, references and Python discoveries",Toast.LENGTH_SHORT).show();break;}}
    }
}
