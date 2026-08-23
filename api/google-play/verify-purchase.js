import crypto from 'node:crypto';
import { google } from 'googleapis';

const PRODUCTS = new Set([
  'treasure_little', 'treasure_shiny', 'treasure_rare',
  'treasure_royal', 'treasure_legendary', 'treasure_mythic'
]);

function json(res, status, body) {
  res.status(status).setHeader('Cache-Control', 'no-store').json(body);
}

function safeEqual(a, b) {
  const aa = Buffer.from(a || ''); const bb = Buffer.from(b || '');
  return aa.length === bb.length && crypto.timingSafeEqual(aa, bb);
}

function serviceAccount() {
  const raw = process.env.GOOGLE_PLAY_SERVICE_ACCOUNT_JSON;
  if (!raw) throw new Error('GOOGLE_PLAY_SERVICE_ACCOUNT_JSON is not configured');
  return JSON.parse(raw);
}

export default async function handler(req, res) {
  if (req.method !== 'POST') return json(res, 405, { error: 'method_not_allowed' });

  // Require an app-issued secret until a full user-auth system is enabled.
  // Store this only as a Vercel environment variable; never ship it in the APK.
  const expected = process.env.PYPET_VERIFY_SECRET;
  if (!expected || !safeEqual(req.headers['x-pypet-verify-secret'], expected))
    return json(res, 401, { error: 'unauthorized' });

  const { packageName, productId, purchaseToken } = req.body || {};
  if (packageName !== 'com.odelly.pypet' || !PRODUCTS.has(productId) || typeof purchaseToken !== 'string' || purchaseToken.length < 20)
    return json(res, 400, { error: 'invalid_purchase_request' });

  try {
    const auth = new google.auth.GoogleAuth({ credentials: serviceAccount(), scopes: ['https://www.googleapis.com/auth/androidpublisher'] });
    const client = await auth.getClient();
    const androidpublisher = google.androidpublisher({ version: 'v3', auth: client });
    const result = await androidpublisher.purchases.products.get({
      packageName,
      productId,
      token: purchaseToken,
    });
    const purchase = result.data;

    if (purchase.purchaseState !== 0 || purchase.consumptionState === 1) {
      return json(res, 409, { verified: false, error: 'purchase_not_eligible' });
    }

    // Acknowledge only after Google has confirmed the purchase. This prevents
    // the client from granting content based solely on locally supplied data.
    if (purchase.acknowledgementState !== 1) {
      await androidpublisher.purchases.products.acknowledge({
        packageName, productId, token: purchaseToken,
        requestBody: { developerPayload: 'pypet-treasure' },
      });
    }

    return json(res, 200, {
      verified: true,
      productId,
      orderId: purchase.orderId || null,
    });
  } catch (error) {
    console.error('Google Play verification failed', error);
    return json(res, 502, { error: 'verification_unavailable' });
  }
}
