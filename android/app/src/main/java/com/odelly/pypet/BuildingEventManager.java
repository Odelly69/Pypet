package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

/** In-world building event router. Building taps launch real activities, not placeholder text. */
public final class BuildingEventManager {
    private BuildingEventManager() {}

    /** Compatibility entry point used by the World renderer. */
    public static void handle(Context context, String building) {
        if (context instanceof Activity) open((Activity) context, building);
    }

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
            .setItems(actions,(d,w)->{try{switch(w){case 0:PetEvolutionManager.feed(a,PetEvolutionManager.foods().get(0));break;case 1:PetEvolutionManager.playWith(a);break;case 2:PetEvolutionManager.performCare(a);PetCareSystem.bathe(a);break;case 3:PetCareSystem.cleanWaste(a);break;case 4:PetEvolutionManager.performRoutine(a);break;}PetCareSystem.tick(a);Toast.makeText(a,"Pet activity completed and needs updated.",Toast.LENGTH_SHORT).show();}catch(Throwable t){Toast.makeText(a,"Pet activity unavailable right now.",Toast.LENGTH_SHORT).show();}}).show();
    }

    private static void academy(Activity a) {
        new AlertDialog.Builder(a).setTitle("🐍 Python Academy")
            .setMessage("Learning is an active World activity. Sessions are paced with healthy breaks so your pet and player can recover.")
            .setPositiveButton("Start lesson",(d,w)->{try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Academy temporarily unavailable.",Toast.LENGTH_SHORT).show();}})
            .setNegativeButton("Not now",null).show();
    }

    private static void market(Activity a) {
        final String[] items={"🌳 Decorative tree","🪑 Garden bench","🏆 Trophy pedestal","🪨 Garden feature"};
        new AlertDialog.Builder(a).setTitle("🛍 Market • World Rewards")
            .setMessage("Choose an owned decorative item to place in your town. Your accomplishments are meant to be seen.")
            .setItems(items,(d,w)->placeItem(a,items[w],580+w*120,220+w*90))
            .setNegativeButton("Close",null).show();
    }

    private static void workshop(Activity a) {
        final String[] projects={"🌳 Place tree","🪑 Place bench","🏆 Display trophy pedestal","🧱 Place garden feature"};
        new AlertDialog.Builder(a).setTitle("🔧 Workshop • Build & Decorate")
            .setMessage("Construct and place a World item. Placements persist and become part of your town showcase.")
            .setItems(projects,(d,w)->placeItem(a,projects[w],-650+w*130,360+w*85))
            .setNegativeButton("Close",null).show();
    }

    private static void placeItem(Activity a,String label,float x,float y) {
        String id=label.replace(" ","_");
        WorldPlacementManager.place(a,id,x,y,1f,0f);
        Toast.makeText(a,label+" placed in your World.",Toast.LENGTH_SHORT).show();
    }

    private static void park(Activity a) {
        new AlertDialog.Builder(a).setTitle("🌳 Park • Play & Explore")
            .setMessage("A calm place for play, exploration and confidence-building. Keep sessions short and enjoyable.")
            .setPositiveButton("Play & explore",(d,w)->{PetEvolutionManager.playWith(a);PetEvolutionManager.performExplore(a);PetCareSystem.tick(a);Toast.makeText(a,"A happy park visit was recorded.",Toast.LENGTH_SHORT).show();})
            .setNegativeButton("Close",null).show();
    }

    private static void library(Activity a) {
        new AlertDialog.Builder(a).setTitle("📚 Library • Study & Review")
            .setMessage("Read, review Python concepts and explore job-readiness knowledge at your own pace. The library is intentionally low-pressure.")
            .setPositiveButton("Study",(d,w)->{try{PypetSchoolView.show(a);}catch(Throwable t){Toast.makeText(a,"Library study is temporarily unavailable.",Toast.LENGTH_SHORT).show();}})
            .setNegativeButton("Close",null).show();
    }
}
