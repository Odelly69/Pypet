package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Toast;

/** In-world building event router. Every permanent building has a purposeful interaction. */
public final class BuildingEventManager {
    private BuildingEventManager() {}

    public static void open(Activity a, String building) {
        switch (building) {
            case "HOME": home(a); break;
            case "PYTHON ACADEMY": academy(a); break;
            case "MARKET": market(a); break;
            case "WORKSHOP": workshop(a); break;
            case "PARK": park(a); break;
            case "LIBRARY": library(a); break;
            default: Toast.makeText(a, building + " is open.", Toast.LENGTH_SHORT).show();
        }
    }

    private static void home(Activity a) {
        String[] actions={"🍓 Feed pet","🎾 Play","🧼 Bathe / care","💩 Clean up","😴 Rest"};
        new AlertDialog.Builder(a).setTitle("🏠 Home • Pet Care")
            .setMessage("Your pet lives here. Choose a healthy care activity; needs use real elapsed time.")
            .setItems(actions,(d,w)->{try{switch(w){case 0:PetEvolutionManager.feed(a,PetEvolutionManager.foods().get(0));break;case 1:PetEvolutionManager.playWith(a);break;case 2:PetEvolutionManager.performCare(a);PetCareSystem.bathe(a);break;case 3:PetCareSystem.cleanWaste(a);break;case 4:PetEvolutionManager.performRoutine(a);break;}Toast.makeText(a,"Pet activity completed.",Toast.LENGTH_SHORT).show();}catch(Throwable t){Toast.makeText(a,"Pet activity unavailable right now.",Toast.LENGTH_SHORT).show();}}).show();
    }

    private static void academy(Activity a) {
        new AlertDialog.Builder(a).setTitle("🐍 Python Academy")
            .setMessage("Learning is an active World activity. Sessions are paced with healthy breaks so your pet and player can recover.")
            .setPositiveButton("Start lesson",(d,w)->{try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Academy temporarily unavailable.",Toast.LENGTH_SHORT).show();}})
            .setNegativeButton("Not now",null).show();
    }

    private static void market(Activity a) {
        new AlertDialog.Builder(a).setTitle("🛍 Market")
            .setMessage("Browse rewards, exclusive World items and decorations. Owned buildings and decorations can be placed through Build mode.")
            .setPositiveButton("Build / decorate",(d,w)->WorldMapView.openBuild(a))
            .setNegativeButton("Close",null).show();
    }

    private static void workshop(Activity a) {
        new AlertDialog.Builder(a).setTitle("🔧 Workshop")
            .setMessage("Build, maintain and improve your town. This is where construction projects and World placement are managed.")
            .setPositiveButton("Build / place",(d,w)->WorldMapView.openBuild(a))
            .setNegativeButton("Close",null).show();
    }

    private static void park(Activity a) {
        new AlertDialog.Builder(a).setTitle("🌳 Park")
            .setMessage("A calm place for play, exploration and confidence-building. Keep sessions short and enjoyable.")
            .setPositiveButton("Play & explore",(d,w)->{PetEvolutionManager.playWith(a);PetEvolutionManager.performExplore(a);Toast.makeText(a,"A happy park visit was recorded.",Toast.LENGTH_SHORT).show();})
            .setNegativeButton("Close",null).show();
    }

    private static void library(Activity a) {
        new AlertDialog.Builder(a).setTitle("📚 Library")
            .setMessage("Read, review Python concepts and explore job-readiness knowledge at your own pace. The library is intentionally low-pressure.")
            .setPositiveButton("Study",(d,w)->{try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Library study is temporarily unavailable.",Toast.LENGTH_SHORT).show();}})
            .setNegativeButton("Close",null).show();
    }
}
