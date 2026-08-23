package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;

/** Spacious, zoomable town map. Buildings are rendered as recognizable structures, not UI boxes. */
public final class WorldMapView {
    private WorldMapView() {}
    public static void show(Activity a) {
        WorldCanvas world=new WorldCanvas(a);
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.rgb(214,232,214));
        TextView title=new TextView(a); title.setText("🌎 "+PypetProfileManager.townName(a)); title.setTextSize(25); title.setGravity(Gravity.CENTER); title.setPadding(8,8,8,3); root.addView(title);
        TextView help=new TextView(a); help.setText("Pinch to zoom • drag to look around • tap buildings to enter"); help.setGravity(Gravity.CENTER); help.setTextSize(14); root.addView(help);
        root.addView(world,new LinearLayout.LayoutParams(-1,0,1));
        Button settings=new Button(a); settings.setText("⚙️ Settings"); settings.setAllCaps(false); root.addView(settings);
        AlertDialog d=new AlertDialog.Builder(a).setView(root).create(); settings.setOnClickListener(v->PypetSettingsView.show(a,new PypetAudio(),new PypetSafetyGuard(a))); d.setOnDismissListener(x->world.stop()); d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));d.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels*.99f),(int)(a.getResources().getDisplayMetrics().heightPixels*.94f));}
    }
    private static final class WorldCanvas extends View {
        private final Activity a; private final Paint p=new Paint(3),t=new Paint(3); private float zoom=1f,ox=0,oy=0,lastX,lastY,lastDist; private boolean dragging,run=true; private long time=System.currentTimeMillis();
        private final RectF[] buildings=new RectF[6];
        WorldCanvas(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void stop(){run=false;}
        protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(137,198,224));c.save();c.translate(w/2+ox,h/2+oy);c.scale(zoom,zoom);float cw=w/zoom,ch=h/zoom;drawTown(c,cw,ch);c.restore();if(run)postInvalidateDelayed(80);}
        private void drawTown(Canvas c,float w,float h){p.setColor(Color.rgb(103,168,91));c.drawRect(-w,-h,w,h,p); // grass, deliberately no isolated green UI square
            p.setColor(Color.rgb(225,202,157));Path road=new Path();road.moveTo(-w*.08f,-h);road.lineTo(w*.08f,-h);road.lineTo(w*.22f,h);road.lineTo(-w*.22f,h);road.close();c.drawPath(road,p);p.setColor(Color.rgb(213,191,148));c.drawRoundRect(-w*.8f,-h*.05f,w*.8f,h*.07f,30,30,p);
            house(c,-w*.72f,-h*.45f,w*.05f,-h*.03f,"HOME",Color.rgb(190,105,80),0); academy(c,w*.18f,-h*.55f,w*.78f,-h*.05f,1); market(c,w*.42f,h*.10f,w*.82f,h*.48f,2); house(c,-w*.82f,h*.10f,-w*.30f,h*.48f,"WORKSHOP",Color.rgb(64,128,146),3); park(c,-w*.20f,h*.20f,w*.25f,h*.63f,4); house(c,w*.48f,h*.56f,w*.88f,h*.88f,"LIBRARY",Color.rgb(112,83,145),5); trees(c,w,h); pet(c,w,h);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(30);t.setColor(Color.rgb(48,55,49));c.drawText(PypetProfileManager.townName(a),0,-h*.84f,t);
        }
        private void house(Canvas c,float l,float top,float r,float b,String name,int roof,int i){buildings[i]=new RectF(l,top,r,b);p.setShadowLayer(10,0,5,Color.argb(70,0,0,0));p.setColor(Color.rgb(246,238,220));c.drawRoundRect(l,top,r,b,16,16,p);p.clearShadowLayer();Path q=new Path();q.moveTo(l-18,top+3);q.lineTo((l+r)/2,top-(b-top)*.35f);q.lineTo(r+18,top+3);q.close();p.setColor(roof);c.drawPath(q,p);p.setColor(Color.rgb(109,158,190));c.drawRect(l+(r-l)*.12f,top+(b-top)*.25f,l+(r-l)*.29f,top+(b-top)*.48f,p);c.drawRect(r-(r-l)*.29f,top+(b-top)*.25f,r-(r-l)*.12f,top+(b-top)*.48f,p);p.setColor(Color.rgb(102,72,51));c.drawRoundRect((l+r)/2-22,b-55,(l+r)/2+22,b,7,7,p);t.setTextSize(20);t.setColor(Color.DKGRAY);c.drawText(name,(l+r)/2,top-(b-top)*.40f,t);}
        private void academy(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);p.setShadowLayer(12,0,6,Color.argb(75,0,0,0));p.setColor(Color.rgb(238,236,222));c.drawRoundRect(l,top,r,b,18,18,p);p.clearShadowLayer();Path q=new Path();q.moveTo(l-10,top+4);q.lineTo((l+r)/2,top-(b-top)*.40f);q.lineTo(r+10,top+4);q.close();p.setColor(Color.rgb(61,104,157));c.drawPath(q,p);p.setColor(Color.rgb(101,166,197));for(int x=0;x<5;x++)c.drawRect(l+25+x*(r-l-50)/5,top+35,l+45+x*(r-l-50)/5,top+70,p);p.setColor(Color.rgb(95,67,48));c.drawRoundRect((l+r)/2-25,b-65,(l+r)/2+25,b,8,8,p);t.setTextSize(22);t.setColor(Color.DKGRAY);c.drawText("PYTHON ACADEMY",(l+r)/2,top-(b-top)*.45f,t);}
        private void market(Canvas c,float l,float top,float r,float b,int i){house(c,l,top,r,b,"MARKET",Color.rgb(205,136,55),i);}
        private void park(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);p.setColor(Color.rgb(72,145,78));c.drawRoundRect(l,top,r,b,35,35,p);p.setColor(Color.rgb(123,82,49));c.drawRect((l+r)/2-6,top+35,(l+r)/2+6,b-30,p);p.setColor(Color.rgb(47,125,57));c.drawCircle((l+r)/2,top+30,45,p);t.setTextSize(20);t.setColor(Color.WHITE);c.drawText("PARK",(l+r)/2,b-20,t);}
        private void trees(Canvas c,float w,float h){for(int i=-2;i<=2;i++){float x=i*w*.32f,y=-h*.10f+i*20;p.setColor(Color.rgb(113,76,49));c.drawRect(x-7,y,x+7,y+42,p);p.setColor(Color.rgb(52,131,63));c.drawCircle(x,y,34,p);}}
        private void pet(Canvas c,float w,float h){float x=w*.02f,y=h*.08f+(float)Math.sin((System.currentTimeMillis()-time)/650.0)*4;p.setColor(Color.rgb(245,221,175));c.drawOval(x-25,y-22,x+25,y+25,p);p.setColor(Color.rgb(100,72,52));c.drawCircle(x-10,y-2,4,p);c.drawCircle(x+10,y-2,4,p);p.setColor(Color.rgb(150,95,95));c.drawCircle(x,y+10,5,p);t.setTextSize(15);t.setColor(Color.DKGRAY);c.drawText(PetEvolutionManager.name(a),x,y+48,t);}
        public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0){zoom=Math.max(.65f,Math.min(2.4f,zoom*d/lastDist));invalidate();}lastDist=d;return true;}lastDist=0; if(e.getAction()==MotionEvent.ACTION_DOWN){lastX=e.getX();lastY=e.getY();dragging=true;return true;}if(e.getAction()==MotionEvent.ACTION_MOVE&&dragging){ox+=e.getX()-lastX;oy+=e.getY()-lastY;lastX=e.getX();lastY=e.getY();invalidate();return true;}if(e.getAction()==MotionEvent.ACTION_UP){dragging=false;float wx=(e.getX()-getWidth()/2-ox)/zoom,wy=(e.getY()-getHeight()/2-oy)/zoom;for(int i=0;i<buildings.length;i++)if(buildings[i]!=null&&buildings[i].contains(wx,wy)){enter(i);return true;}return true;}return true;}
        private float dist(MotionEvent e){if(e.getPointerCount()<2)return 0;float dx=e.getX(0)-e.getX(1),dy=e.getY(0)-e.getY(1);return (float)Math.hypot(dx,dy);}
        private void enter(int i){switch(i){case 0:new AlertDialog.Builder(a).setTitle("🏠 HOME").setMessage("Rooms, care, rest and study with your pet.").setPositiveButton("Study",(d,w)->{ }).show();break;case 1:new AlertDialog.Builder(a).setTitle("🏫 PYTHON ACADEMY").setMessage("Hands-on Python curriculum: learn by doing with your pet.").setPositiveButton("Open Academy",(d,w)->PypetSchoolView.show(a)).show();break;case 2:Toast.makeText(a,"Market: World items and supplies",Toast.LENGTH_SHORT).show();break;case 3:Toast.makeText(a,"Workshop: build and customize your town",Toast.LENGTH_SHORT).show();break;case 4:Toast.makeText(a,"Park: play with your pet",Toast.LENGTH_SHORT).show();break;case 5:Toast.makeText(a,"Library: lessons and references",Toast.LENGTH_SHORT).show();}}
    }
}