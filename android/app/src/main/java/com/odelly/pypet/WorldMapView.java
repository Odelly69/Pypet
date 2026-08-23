package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.*;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;

/** Immersive, spacious town map with recognizable buildings, terrain, pan and pinch zoom. */
public final class WorldMapView {
    private WorldMapView() {}
    public static void show(Activity a) {
        WorldCanvas world=new WorldCanvas(a);
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(Color.rgb(220,235,220));
        TextView title=new TextView(a); title.setText("🌎 "+PypetProfileManager.townName(a)+" • PYPET WORLD"); title.setTextSize(22); title.setGravity(Gravity.CENTER); title.setPadding(6,6,6,2); root.addView(title,new LinearLayout.LayoutParams(-1,52));
        TextView help=new TextView(a); help.setText("Explore freely • pinch to zoom • drag to look around • tap a building to enter"); help.setGravity(Gravity.CENTER); help.setTextSize(13); root.addView(help,new LinearLayout.LayoutParams(-1,34));
        root.addView(world,new LinearLayout.LayoutParams(-1,0,1));
        Button settings=new Button(a); settings.setText("⚙️ Settings"); settings.setAllCaps(false); root.addView(settings,new LinearLayout.LayoutParams(-1,48));
        AlertDialog d=new AlertDialog.Builder(a).setView(root).create(); settings.setOnClickListener(v->PypetSettingsView.show(a,new PypetAudio(),new PypetSafetyGuard(a))); d.setOnDismissListener(x->world.stop()); d.show();
        if(d.getWindow()!=null){d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));d.getWindow().setLayout((int)(a.getResources().getDisplayMetrics().widthPixels*.995f),(int)(a.getResources().getDisplayMetrics().heightPixels*.965f));}
    }
    private static final class WorldCanvas extends View {
        private final Activity a; private final Paint p=new Paint(3),t=new Paint(3); private float zoom=1f,ox=0,oy=0,lastX,lastY,lastDist; private boolean dragging,run=true; private long time=System.currentTimeMillis();
        private final RectF[] buildings=new RectF[6];
        WorldCanvas(Activity a){super(a);this.a=a;t.setTypeface(Typeface.DEFAULT_BOLD);setLayerType(View.LAYER_TYPE_SOFTWARE,null);}
        void stop(){run=false;}
        protected void onDraw(Canvas c){float w=getWidth(),h=getHeight();c.drawColor(Color.rgb(139,203,230));c.save();c.translate(w/2+ox,h/2+oy);c.scale(zoom,zoom);float cw=w/zoom,ch=h/zoom;drawTown(c,cw,ch);c.restore();if(run)postInvalidateDelayed(80);}
        private void drawTown(Canvas c,float w,float h){
            // Continuous terrain: no unexplained floating green UI rectangle.
            p.setColor(Color.rgb(117,177,96));c.drawRect(-w,-h,w,h,p);
            drawWater(c,w,h);drawRoads(c,w,h);drawLots(c,w,h);
            house(c,-w*.72f,-h*.43f,w*.02f,-h*.04f,"HOME",Color.rgb(177,91,70),0);
            academy(c,w*.17f,-h*.52f,w*.76f,-h*.03f,1);
            market(c,w*.45f,h*.10f,w*.83f,h*.47f,2);
            house(c,-w*.82f,h*.10f,-w*.30f,h*.48f,"WORKSHOP",Color.rgb(50,126,145),3);
            park(c,-w*.19f,h*.18f,w*.25f,h*.62f,4);
            house(c,w*.48f,h*.54f,w*.88f,h*.88f,"LIBRARY",Color.rgb(104,75,139),5);
            trees(c,w,h);pet(c,w,h);
            t.setTextAlign(Paint.Align.CENTER);t.setTextSize(28);t.setColor(Color.rgb(42,70,45));
            c.drawText(PypetProfileManager.townName(a),0,-h*.84f,t);
        }
        private void drawWater(Canvas c,float w,float h){p.setColor(Color.rgb(78,166,194));c.drawOval(-w*.98f,h*.66f,w*.05f,h*1.08f,p);p.setColor(Color.rgb(165,219,224));for(int i=0;i<5;i++)c.drawLine(-w*.85f+i*55,h*.79f,-w*.58f+i*55,h*.79f,p);}
        private void drawRoads(Canvas c,float w,float h){p.setColor(Color.rgb(218,195,151));Path v=new Path();v.moveTo(-w*.065f,-h);v.lineTo(w*.065f,-h);v.lineTo(w*.19f,h);v.lineTo(-w*.19f,h);v.close();c.drawPath(v,p);c.drawRoundRect(-w*.9f,-h*.015f,w*.9f,h*.085f,24,24,p);p.setColor(Color.rgb(201,177,135));p.setStrokeWidth(4);for(int y=-2;y<2;y++)c.drawLine(-w*.9f,y*h*.5f,w*.9f,y*h*.5f,p);}
        private void drawLots(Canvas c,float w,float h){p.setColor(Color.rgb(139,190,106));c.drawRoundRect(-w*.94f,-h*.70f,-w*.78f,-h*.47f,22,22,p);c.drawRoundRect(w*.78f,-h*.67f,w*.94f,-h*.43f,22,22,p);}
        private void house(Canvas c,float l,float top,float r,float b,String name,int roof,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2;float ww=r-l,hh=b-top;
            p.setShadowLayer(10,0,5,Color.argb(65,0,0,0));p.setColor(Color.rgb(247,240,222));c.drawRoundRect(l,top,r,b,14,14,p);p.clearShadowLayer();
            // roof, eaves and chimney
            p.setColor(roof);Path q=new Path();q.moveTo(l-16,top+5);q.lineTo(mid,top-hh*.36f);q.lineTo(r+16,top+5);q.close();c.drawPath(q,p);p.setColor(Color.rgb(100,79,67));c.drawRect(r-ww*.22f,top-hh*.22f,r-ww*.12f,top-hh*.02f,p);p.setColor(Color.rgb(230,219,192));c.drawRect(l,top+2,r,top+12,p);
            // windows with frames
            window(c,l+ww*.13f,top+hh*.24f,ww*.17f,hh*.17f);window(c,r-ww*.30f,top+hh*.24f,ww*.17f,hh*.17f);
            // door and porch
            p.setColor(Color.rgb(104,72,52));c.drawRoundRect(mid-20,b-64,mid+20,b,6,6,p);p.setColor(Color.rgb(230,201,145));c.drawCircle(mid+11,b-34,3,p);
            t.setTextSize(18);t.setColor(Color.rgb(45,52,47));t.setTextAlign(Paint.Align.CENTER);c.drawText(name,mid,top-hh*.39f,t);
        }
        private void window(Canvas c,float x,float y,float ww,float hh){p.setColor(Color.rgb(107,171,199));c.drawRoundRect(x,y,x+ww,y+hh,4,4,p);p.setColor(Color.rgb(225,235,226));p.setStrokeWidth(3);c.drawLine(x+ww/2,y,x+ww/2,y+hh,p);c.drawLine(x,y+hh/2,x+ww,y+hh/2,p);}
        private void academy(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);float mid=(l+r)/2,ww=r-l,hh=b-top;
            p.setShadowLayer(12,0,6,Color.argb(75,0,0,0));p.setColor(Color.rgb(235,234,221));c.drawRoundRect(l,top,r,b,16,16,p);p.clearShadowLayer();
            p.setColor(Color.rgb(61,100,151));Path roof=new Path();roof.moveTo(l-12,top+5);roof.lineTo(mid,top-hh*.40f);roof.lineTo(r+12,top+5);roof.close();c.drawPath(roof,p);
            for(int row=0;row<2;row++)for(int col=0;col<5;col++)window(c,l+24+col*(ww-48)/5,top+34+row*82,28,38);
            p.setColor(Color.rgb(95,67,48));c.drawRoundRect(mid-24,b-66,mid+24,b,7,7,p);t.setTextSize(21);t.setColor(Color.rgb(43,52,57));t.setTextAlign(Paint.Align.CENTER);c.drawText("PYTHON ACADEMY",mid,top-hh*.44f,t);
        }
        private void market(Canvas c,float l,float top,float r,float b,int i){house(c,l,top,r,b,"MARKET",Color.rgb(205,136,55),i);}
        private void park(Canvas c,float l,float top,float r,float b,int i){buildings[i]=new RectF(l,top,r,b);p.setColor(Color.rgb(72,145,78));c.drawRoundRect(l,top,r,b,30,30,p);p.setColor(Color.rgb(118,82,50));c.drawRect((l+r)/2-6,top+42,(l+r)/2+6,b-42,p);p.setColor(Color.rgb(54,135,62));c.drawCircle((l+r)/2,top+35,43,p);p.setColor(Color.rgb(242,174,58));c.drawCircle(l+(r-l)*.30f,top+(b-top)*.56f,18,p);t.setTextSize(19);t.setColor(Color.WHITE);t.setTextAlign(Paint.Align.CENTER);c.drawText("PARK",(l+r)/2,b-18,t);}
        private void trees(Canvas c,float w,float h){for(int i=-2;i<=2;i++){float x=i*w*.32f,y=-h*.10f+i*20;p.setColor(Color.rgb(113,76,49));c.drawRect(x-7,y,x+7,y+42,p);p.setColor(Color.rgb(52,131,63));c.drawCircle(x,y,34,p);p.setColor(Color.rgb(76,153,75));c.drawCircle(x-12,y-8,22,p);}}
        private void pet(Canvas c,float w,float h){float x=w*.02f,y=h*.08f+(float)Math.sin((System.currentTimeMillis()-time)/650.0)*4;String species=PetEvolutionManager.current(a).displayName.toLowerCase();
            if(species.contains("frog")){drawFrog(c,x,y);} else {drawGenericPet(c,x,y);}
            t.setTextSize(15);t.setColor(Color.DKGRAY);t.setTextAlign(Paint.Align.CENTER);c.drawText(PetEvolutionManager.name(a)+" • Lv "+PetEvolutionManager.current(a).level,x,y+66,t);
        }
        private void drawFrog(Canvas c,float x,float y){p.setColor(Color.rgb(76,164,70));c.drawOval(x-31,y-8,x+31,y+42,p);c.drawOval(x-29,y+28,x-10,y+58,p);c.drawOval(x+10,y+28,x+29,y+58,p);p.setColor(Color.rgb(93,190,80));c.drawCircle(x-20,y-14,14,p);c.drawCircle(x+20,y-14,14,p);p.setColor(Color.WHITE);c.drawCircle(x-20,y-15,7,p);c.drawCircle(x+20,y-15,7,p);p.setColor(Color.BLACK);c.drawCircle(x-20,y-15,3,p);c.drawCircle(x+20,y-15,3,p);p.setColor(Color.rgb(55,106,52));c.drawArc(x-15,y+8,x+15,y+27,10,160,false,p);}
        private void drawGenericPet(Canvas c,float x,float y){p.setColor(Color.rgb(181,139,94));c.drawOval(x-27,y-8,x+27,y+38,p);c.drawCircle(x,y-12,26,p);p.setColor(Color.rgb(65,45,35));c.drawCircle(x-9,y-14,3,p);c.drawCircle(x+9,y-14,3,p);p.setColor(Color.rgb(181,139,94));c.drawOval(x-25,y+31,x-12,y+55,p);c.drawOval(x+12,y+31,x+25,y+55,p);c.drawOval(x+24,y+2,x+39,y+16,p);}
        public boolean onTouchEvent(MotionEvent e){if(e.getPointerCount()==2){float d=dist(e);if(lastDist>0){zoom=Math.max(.55f,Math.min(3.0f,zoom*d/lastDist));invalidate();}lastDist=d;return true;}lastDist=0;if(e.getAction()==MotionEvent.ACTION_DOWN){lastX=e.getX();lastY=e.getY();dragging=true;return true;}if(e.getAction()==MotionEvent.ACTION_MOVE&&dragging){ox+=e.getX()-lastX;oy+=e.getY()-lastY;lastX=e.getX();lastY=e.getY();invalidate();return true;}if(e.getAction()==MotionEvent.ACTION_UP){dragging=false;float wx=(e.getX()-getWidth()/2-ox)/zoom,wy=(e.getY()-getHeight()/2-oy)/zoom;for(int i=0;i<buildings.length;i++)if(buildings[i]!=null&&buildings[i].contains(wx,wy)){enter(i);return true;}return true;}return true;}
        private float dist(MotionEvent e){if(e.getPointerCount()<2)return 0;return (float)Math.hypot(e.getX(0)-e.getX(1),e.getY(0)-e.getY(1));}
        private void enter(int i){switch(i){case 0:new AlertDialog.Builder(a).setTitle("🏠 HOME").setMessage("Rooms, care, rest and study with your pet.").setPositiveButton("Study",null).show();break;case 1:PypetSchoolView.show(a);break;case 2:Toast.makeText(a,"Market: World items and supplies",Toast.LENGTH_SHORT).show();break;case 3:Toast.makeText(a,"Workshop: build and customize your town",Toast.LENGTH_SHORT).show();break;case 4:Toast.makeText(a,"Park: play with your pet",Toast.LENGTH_SHORT).show();break;case 5:Toast.makeText(a,"Library: lessons and references",Toast.LENGTH_SHORT).show();}}
    }
}