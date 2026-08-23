package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.view.View;

/** Presentation safety guardrails; these reduce avoidable sensory risk but are not medical protection. */
public final class PypetSafetyGuard {
    private static final String PREFS = "pypet_safety";
    private static final String REDUCED_MOTION = "reduced_motion";
    private static final long MIN_ANIMATION_INTERVAL_MS = 850;
    private final SharedPreferences prefs;
    private long lastAnimation;

    public PypetSafetyGuard(Context context) { prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE); }
    public boolean reducedMotion() { return prefs.getBoolean(REDUCED_MOTION, false); }
    public void setReducedMotion(boolean value) { prefs.edit().putBoolean(REDUCED_MOTION, value).apply(); }

    /** Blocks rapid repeated animations and all animations when reduced-motion is enabled. */
    public boolean allowAnimation() {
        if (reducedMotion()) return false;
        long now = SystemClock.uptimeMillis();
        if (now - lastAnimation < MIN_ANIMATION_INTERVAL_MS) return false;
        lastAnimation = now;
        return true;
    }

    /** Keep animations gentle and bounded; never permit a caller to request an extreme duration. */
    public long safeAnimationDuration(long requestedMs) {
        if (reducedMotion()) return 0;
        return Math.max(650, Math.min(1200, requestedMs));
    }

    /** Pypet does not use reward haptics. */
    public void suppressHaptics(View view) { if (view != null) view.setHapticFeedbackEnabled(false); }
}
