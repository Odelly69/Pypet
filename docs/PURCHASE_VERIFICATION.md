# Pypet purchase verification

Pypet intentionally fails closed for Treasure purchases. The Android client must receive `verified: true` from the trusted backend before granting a Treasure.

## Backend

`api/purchase-verify.js` verifies one-time products against Google Play Developer API using a service account.

Required Vercel environment variable:

- `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` — the Google Play service-account JSON credential. Store it as a secret; never commit it.

The service account must have the minimum Google Play Console permissions needed to read in-app product purchase status for this application.

## Android

Set `purchase_verification_url` in `android/app/src/main/res/values/strings.xml` to the deployed HTTPS endpoint, for example:

`https://<your-domain>/api/purchase-verify`

Do not use HTTP. The Android client rejects non-HTTPS verification URLs.

## Release gate

Before releasing paid/high-value content:

1. Deploy the backend over HTTPS.
2. Configure the service-account secret in the hosting environment.
3. Configure the Android verification URL.
4. Create the matching one-time products in Google Play Console.
5. Test a real Play test purchase and Restore Purchases.
6. Confirm an invalid token receives `verified: false` and does not grant content.
7. Confirm the Android release build passes CI and the APK/AAB is produced.
