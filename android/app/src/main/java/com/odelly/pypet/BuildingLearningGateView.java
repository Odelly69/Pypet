package com.odelly.pypet;

import android.app.Activity;

/** Legacy compatibility router. Python instruction is exclusive to the Academy; every other building opens its own activity. */
public final class BuildingLearningGateView {
    private BuildingLearningGateView() {}

    public static void show(Activity a, String building) {
        if (building == null) {
            LivingWorldView.show(a);
            return;
        }
        if ("ACADEMY".equalsIgnoreCase(building) || "PYTHON ACADEMY".equalsIgnoreCase(building)) {
            PypetAcademyActivityView.show(a);
            return;
        }
        BuildingEventManager.open(a, building.toUpperCase(java.util.Locale.US));
    }
}
