package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/** In-world building router. A tap performs the building's real routine; no description-only gate. */
public final class BuildingEventManager {
    private BuildingEventManager() {}

    public static void handle(Context context,String building){if(context instanceof Activity)open((Activity)context,building);}

    public static void open(Activity a,String building){
        switch(building){
            case "HOME": home(a); break;
            case "PYTHON ACADEMY": academy(a); break;
            case "MARKET": market(a); break;
            case "WORKSHOP": workshop(a); break;
            case "PARK": park(a); break;
            case "LIBRARY": library(a); break;
            default: Toast.makeText(a,building+" activity is unavailable.",Toast.LENGTH_SHORT).show();
        }
    }

    private static void home(Activity a){
        try{PetEvolutionManager.performRoutine(a);PetCareSystem.tick(a);Toast.makeText(a,"🏠 Home routine completed — your pet is cared for.",Toast.LENGTH_SHORT).show();}
        catch(Throwable t){Toast.makeText(a,"Home routine is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }

    private static void academy(Activity a){
        try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Academy is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }

    private static void market(Activity a){
        try{WorldPlacementManager.place(a,"market_tree",760,110,1f,0f);Toast.makeText(a,"🛍 Market routine: a town decoration was added.",Toast.LENGTH_SHORT).show();}
        catch(Throwable t){Toast.makeText(a,"Market is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }

    private static void workshop(Activity a){
        try{WorldPlacementManager.place(a,"workshop_bench",-760,110,1f,0f);Toast.makeText(a,"🔧 Workshop routine: your latest build was placed in town.",Toast.LENGTH_SHORT).show();}
        catch(Throwable t){Toast.makeText(a,"Workshop is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }

    private static void park(Activity a){
        try{PetEvolutionManager.playWith(a);PetEvolutionManager.performExplore(a);PetCareSystem.tick(a);Toast.makeText(a,"🌳 Park routine completed — play and exploration recorded.",Toast.LENGTH_SHORT).show();}
        catch(Throwable t){Toast.makeText(a,"Park routine is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }

    private static void library(Activity a){
        try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Library study is temporarily unavailable.",Toast.LENGTH_SHORT).show();}
    }
}
