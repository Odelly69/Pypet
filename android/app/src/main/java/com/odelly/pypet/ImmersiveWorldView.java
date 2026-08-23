package com.odelly.pypet;

import android.app.Activity;

/** Compatibility entry point for the player-facing World. */
public final class ImmersiveWorldView {
    private ImmersiveWorldView() {}
    public static void show(Activity activity) { WorldMapView.show(activity); }
}
