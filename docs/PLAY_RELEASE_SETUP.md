# Pypet Google Play release setup

This document covers the remaining account-side steps that cannot be safely completed from source control alone.

## 1. Google Play service account

1. In Google Play Console, create/select the Pypet app with package name `com.odelly.pypet`.
2. In Google Cloud, create a dedicated service account for Play purchase verification.
3. Grant the service account the minimum Google Play Console permissions required to read one-time product purchases.
4. Create a JSON key only if your deployment environment requires it. Never commit the key to GitHub.
5. Store the complete JSON as the Vercel environment variable `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` for the production environment.

## 2. Product catalog

The Android app's premium Treasure catalog defines these product IDs:

- `treasure_little`
- `treasure_shiny`
- `treasure_rare`
- `treasure_royal`
- `treasure_legendary`
- `treasure_mythic`

Create matching **one-time in-app products** in Play Console. Product IDs must match exactly.

## 3. Deploy the verifier

The serverless verifier is `api/purchase-verify.js`. It accepts POST requests only and fails closed unless the package, product ID and purchase token are valid. It calls Google Play's Android Publisher API and only returns `verified: true` for purchase state `0`.

Deploy the repository root to a Vercel project. Configure:

- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` = the service-account JSON

After deployment, the HTTPS endpoint should be:

`https://<your-vercel-domain>/api/purchase-verify`

Do not put the service-account JSON, private key, or other secrets in source control.

## 4. Connect Android release builds

Build the release APK/AAB with the verifier URL supplied as a Gradle project property:

`./gradlew bundleRelease -PpypetPurchaseVerifyUrl=https://<your-vercel-domain>/api/purchase-verify`

The Android code reads this through `BuildConfig.PURCHASE_VERIFICATION_URL`. If it is empty, purchases fail closed rather than granting unverified items.

## 5. AdMob

Debug builds use Google's official rewarded-ad test unit. Release builds use the configured Pypet rewarded-ad unit. Keep testing on the test unit until the Play release is ready.

Rewarded ads grant 25 Pypet Coins per completed ad. Coins are only used for optional exclusive world decorations; lessons, normal pet care, required world content and saved progression never depend on ads.

## 6. Play test purchase

Before production release:

1. Add tester accounts in Play Console.
2. Upload a release build to an internal testing track.
3. Make the six products available to that testing track.
4. Test a purchase for each product.
5. Confirm the verifier returns `verified: true` only after Google Play reports the purchase as completed.
6. Confirm the same product is not granted twice locally.
7. Confirm Restore Purchases restores already-owned items.
8. Confirm pending/cancelled/unverified purchases do not grant an item.

## 7. Release gate

The GitHub Actions Android workflow runs Python tests, compiles the Android app, verifies the APK exists, and uploads the debug APK artifact. A release should additionally be built with the production verifier URL and tested through the Play internal-testing track before promotion.

## 8. What cannot be automated from this repository

The following require access to the user's Google/Vercel accounts and should not be represented as completed until the external systems confirm them:

- Google Play app registration and product activation
- Google Play service-account permissions
- Vercel project creation and production domain selection
- Vercel secret entry
- AdMob app/ad-unit approval and payment profile
- Play internal-testing purchase execution
- Final Play production submission
