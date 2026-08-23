package com.odelly.pypet;

/** Central visual-safety policy for the 3D World. Keep motion calm and predictable. */
public final class EpilepsySafe3DPolicy {
    private EpilepsySafe3DPolicy() {}

    public static final float MAX_CAMERA_DEGREES_PER_SECOND = 90f;
    public static final float MAX_CAMERA_ZOOM_RATE = 0.8f;
    public static final float MAX_FLASH_HZ = 0f;
    public static final boolean ALLOW_STROBE = false;
    public static final boolean ALLOW_RAPID_COLOR_CYCLING = false;
    public static final boolean ALLOW_SCREEN_SHAKE = false;
    public static final boolean ALLOW_HIGH_CONTRAST_PULSING = false;
    public static final int TRANSITION_MS = 450;

    public static float clampCameraDelta(float degrees, float seconds) {
        float limit = MAX_CAMERA_DEGREES_PER_SECOND * Math.max(0f, seconds);
        return Math.max(-limit, Math.min(limit, degrees));
    }

    public static float clampZoomDelta(float delta, float seconds) {
        float limit = MAX_CAMERA_ZOOM_RATE * Math.max(0f, seconds);
        return Math.max(-limit, Math.min(limit, delta));
    }
}
