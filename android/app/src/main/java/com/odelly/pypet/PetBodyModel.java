package com.odelly.pypet;

/**
 * Data-only body specification for the living pet. Rendering can consume these
 * values without exposing hidden lineage/genetic recipes to the player.
 * Body traits are presentation/development traits, not a lineage disclosure API.
 */
public final class PetBodyModel {
    public final float height;
    public final float bodyWidth;
    public final float headScale;
    public final float legLength;
    public final float earScale;
    public final float tailScale;
    public final float eyeScale;
    public final int bodyTone;
    public final int accentTone;
    public final boolean hasTail;
    public final boolean hasEars;

    private PetBodyModel(float height, float bodyWidth, float headScale, float legLength,
                         float earScale, float tailScale, float eyeScale,
                         int bodyTone, int accentTone, boolean hasTail, boolean hasEars) {
        this.height = height;
        this.bodyWidth = bodyWidth;
        this.headScale = headScale;
        this.legLength = legLength;
        this.earScale = earScale;
        this.tailScale = tailScale;
        this.eyeScale = eyeScale;
        this.bodyTone = bodyTone;
        this.accentTone = accentTone;
        this.hasTail = hasTail;
        this.hasEars = hasEars;
    }

    /** Stable visual body profile derived only from the visible pet variant id/stage. */
    public static PetBodyModel forVariant(PetEvolutionManager.PetVariant pet) {
        int h = Math.abs(pet.id.hashCode());
        float stage = Math.max(0, Math.min(3, pet.level - 1));
        float height = 0.82f + stage * 0.10f + (h % 7) * 0.018f;
        float width = 0.78f + ((h / 7) % 6) * 0.025f;
        float head = 0.88f + ((h / 43) % 5) * 0.025f;
        float legs = 0.82f + ((h / 211) % 6) * 0.035f;
        float ears = 0.70f + ((h / 997) % 7) * 0.045f;
        float tail = 0.70f + ((h / 3137) % 7) * 0.045f;
        float eyes = 0.88f + ((h / 11) % 5) * 0.035f;
        int body = tone(h, 0);
        int accent = tone(h, 1);
        return new PetBodyModel(height, width, head, legs, ears, tail, eyes,
                body, accent, !pet.species.equals("Bird"), true);
    }

    private static int tone(int seed, int offset) {
        int[][] tones = {
                {238, 170, 126}, {154, 190, 232}, {174, 145, 205},
                {137, 185, 137}, {232, 205, 111}, {207, 150, 177},
                {176, 176, 176}, {229, 144, 112}
        };
        int i = Math.floorMod(seed + offset * 3, tones.length);
        return (tones[i][0] << 16) | (tones[i][1] << 8) | tones[i][2];
    }
}
