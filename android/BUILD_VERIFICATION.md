# Android build verification

The Pypet Android workflow is the production gate for the installable APK.

Current integration baseline:
- Random unique eggs and randomized lineages
- Lineage-specific evolution branches and rarity rolls
- Equal weighting of the nine development categories used for evolution
- Evolution forms preserved in the collection
- School-based Python learning/world development
- World, hatchery, food, trophies, streaks, audio and reduced-motion systems
- 40 original exclusive world decorations
- Rewarded-ad test/live separation with 25-coin ad rewards
- Google Play purchase verification fails closed until a trusted HTTPS verifier is configured

CI must pass both the Python curriculum/world tests and `./gradlew assembleDebug --stacktrace` before an APK is considered verified.

Release builds must additionally receive `-PpypetPurchaseVerifyUrl=https://<trusted-host>/api/purchase-verify` so premium purchases can be verified server-side. Debug builds intentionally have no production purchase verifier and use Google's rewarded-ad test unit.

This file intentionally triggers the Android workflow after the Java integration fixes already present on the current main branch, so the build gate is re-evaluated on the complete source tree.
