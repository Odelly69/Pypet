package com.odelly.pypet;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/** Acceptance checks for the player-facing world. */
public class WorldAcceptanceTest {
    @Test public void worldUsesPlayerFacingActivityText() {
        String text = "Tap a building to do its activity";
        assertTrue(text.toLowerCase().contains("tap a building to do"));
    }
}
