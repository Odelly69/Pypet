package com.odelly.pypet;

import android.app.Activity;

/** Compatibility entry point. The active player-facing World is LivingWorldView. */
public final class WorldMapView {
    private WorldMapView() {}

    public static void show(Activity activity) {
        LivingWorldView.show(activity);
    }

    public static void openBuild(Activity activity) {
        LivingWorldView.show(activity);
    }
}
