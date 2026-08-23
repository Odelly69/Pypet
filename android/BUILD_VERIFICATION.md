# Android build verification

The Pypet Android workflow is the production gate for the installable APK.

Current integration baseline:
- Random unique eggs and randomized lineages
- Lineage-specific evolution branches and rarity rolls
- Balanced development across hunger/nutrition, health, care, happiness, play, school, Python learning, exploration and routines
- Evolution forms preserved in the collection
- School-based Python learning/world development
- World, hatchery, food, trophies, streaks, audio and reduced-motion systems

CI must pass both the Python curriculum/world tests and `./gradlew assembleDebug --stacktrace` before an APK is considered verified.

This file intentionally triggers the Android workflow after the Java integration fixes already present on the current main branch, so the build gate is re-evaluated on the complete source tree.
