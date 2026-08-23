import { google } from "googleapis";

const PACKAGE_NAME = "com.odelly.pypet";
const ALLOWED_PRODUCTS = new Set([
  "treasure_little",
  "treasure_shiny",
  "treasure_rare",
  "treasure_royal",
  "treasure_legendary",
  "treasure_mythic",
]);

function json(res, status, body) {
  res.status(status).setHeader("Content-Type", "application/json").send(JSON.stringify(body));
}

function getCredentials() {
  const raw = process.env.GOOGLE_PLAY_SERVICE_ACCOUNT_JSON;
  if (!raw) throw new Error("GOOGLE_PLAY_SERVICE_ACCOUNT_JSON is not configured");
  return JSON.parse(raw);
}

export default async function handler(req, res) {
  if (req.method !== "POST") {
    res.setHeader("Allow", "POST");
    return json(res, 405, { verified: false, error: "method_not_allowed" });
  }

  try {
    const { packageName, productId, purchaseToken } = req.body || {};

    if (packageName !== PACKAGE_NAME || !ALLOWED_PRODUCTS.has(productId) || typeof purchaseToken !== "string" || purchaseToken.length < 10) {
      return json(res, 400, { verified: false, error: "invalid_purchase_request" });
    }

    const credentials = getCredentials();
    const auth = new google.auth.GoogleAuth({
      credentials,
      scopes: ["https://www.googleapis.com/auth/androidpublisher"],
    });
    const androidpublisher = google.androidpublisher({ version: "v3", auth });

    const result = await androidpublisher.purchases.products.get({
      packageName: PACKAGE_NAME,
      productId,
      token: purchaseToken,
    });

    const purchase = result.data;
    // Google Play purchaseState 0 means purchased. Other states fail closed.
    const verified = purchase.purchaseState === 0;

    return json(res, 200, {
      verified,
      productId,
      purchaseState: purchase.purchaseState ?? null,
      orderId: purchase.orderId ?? null,
    });
  } catch (error) {
    console.error("purchase verification failed", error?.message || error);
    return json(res, 200, { verified: false, error: "verification_failed" });
  }
}
