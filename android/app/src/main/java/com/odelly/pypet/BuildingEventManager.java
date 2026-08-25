package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/** In-world router. Buildings teach a Python skill first; the Academy opens the full curriculum. */
public final class BuildingEventManager {
    private BuildingEventManager() {}
    public static void handle(Context context,String building){if(context instanceof Activity)open((Activity)context,building);}
    public static void open(Activity a,String building){
        try{
            switch(building){
                case "PYTHON ACADEMY": PypetAcademyActivityView.show(a); break;
                case "HOME": case "MARKET": case "WORKSHOP": case "PARK": case "LIBRARY": BuildingLearningGateView.show(a,building); break;
                default: Toast.makeText(a,"No activity registered for "+building,Toast.LENGTH_SHORT).show();
            }
        }catch(Throwable t){android.util.Log.e("PYPET","Building activity failed: "+building,t);Toast.makeText(a,building+" could not open. Please try again.",Toast.LENGTH_SHORT).show();}
    }
}
