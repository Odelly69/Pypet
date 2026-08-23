package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/** In-world building router. Every building opens a real interactive activity, never a description-only popup. */
public final class BuildingEventManager {
    private BuildingEventManager() {}
    public static void handle(Context context,String building){if(context instanceof Activity)open((Activity)context,building);}
    public static void open(Activity a,String building){
        try{
            switch(building){
                case "HOME": BuildingActivityView.home(a); break;
                case "PYTHON ACADEMY": PypetAcademyActivityView.show(a); break;
                case "MARKET": BuildingActivityView.market(a); break;
                case "WORKSHOP": BuildingActivityView.workshop(a); break;
                case "PARK": BuildingActivityView.park(a); break;
                case "LIBRARY": BuildingActivityView.library(a); break;
                default: Toast.makeText(a,"No activity registered for "+building,Toast.LENGTH_SHORT).show();
            }
        }catch(Throwable t){android.util.Log.e("PYPET","Building activity failed: "+building,t);Toast.makeText(a,building+" could not open. Please try again.",Toast.LENGTH_SHORT).show();}
    }
}
