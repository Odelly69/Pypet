# PyPet Android build verification

The PyPet Android workflow is the production gate for the installable APK.

Current integration baseline:
- PyPet user-facing branding
- Random unique eggs and randomized lineages
- Lineage-specific evolution branches and rarity rolls
- Equal weighting of the nine development categories used for evolution
- Evolution forms preserved in the collection
- School-based Python learning/world development
- Living World, autonomous pet movement, hatchery, food, trophies, streaks, audio and reduced-motion systems
- Full-screen world rendering with connected roads, sidewalks and roadside building lots
- Valid buildable-land placement that avoids roads/intersections
- Identifiable park with entrance, sign, paths, pond, playground, benches, trees and planted areas
- Direct building-to-activity interactions without a description-only placeholder popup
- 40 original exclusive world decorations
- Rewarded-ad test/live separation with 25-coin ad rewards
- Google Play purchase verification fails closed until a trusted HTTPS verifier is configured

CI must pass both the Python curriculum/world tests and `gradle assembleDebug --stacktrace` before an APK is considered verified.

Release builds must additionally receive `-PpypetPurchaseVerifyUrl=https://<trusted-host>/api/purchase-verify` so premium purchases can be verified server-side. Debug builds intentionally have no production purchase verifier and use Google's rewarded-ad test unit.

This file intentionally triggers the Android workflow after integrated Java/World changes so the build gate is re-evaluated on the complete source tree.
