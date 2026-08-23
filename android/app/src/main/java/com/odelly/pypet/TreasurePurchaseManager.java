package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import com.android.billingclient.api.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.json.*;

/** Google Play Billing client with fail-closed server purchase verification. */
public final class TreasurePurchaseManager implements PurchasesUpdatedListener {
    public interface Listener { void onProductsReady(List<ProductDetails> products); void onPurchaseGranted(String productId); void onMessage(String message); }
    private static final String PREFS = "pypet_treasure_purchases";
    private final BillingClient billing;
    private final SharedPreferences prefs;
    private final Map<String, ProductDetails> products = new HashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Listener listener;
    private final String verifyUrl;

    public TreasurePurchaseManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        verifyUrl = BuildConfig.PURCHASE_VERIFICATION_URL;
        PendingPurchasesParams pending = PendingPurchasesParams.newBuilder().enableOneTimeProducts().build();
        billing = BillingClient.newBuilder(context).enablePendingPurchases(pending).setListener(this).build();
    }

    public void connect(Listener l) {
        listener = l;
        if (billing.isReady()) { queryProducts(); return; }
        billing.startConnection(new BillingClientStateListener() {
            @Override public void onBillingSetupFinished(BillingResult r) {
                if (r.getResponseCode() == BillingClient.BillingResponseCode.OK) queryProducts();
                else message("Google Play Billing unavailable: " + r.getDebugMessage());
            }
            @Override public void onBillingServiceDisconnected() { message("Google Play Billing disconnected. Try the store again."); }
        });
    }

    private void queryProducts() {
        List<QueryProductDetailsParams.Product> ids = new ArrayList<>();
        for (TreasureCatalog.Item i : TreasureCatalog.all()) ids.add(QueryProductDetailsParams.Product.newBuilder().setProductId(i.productId).setProductType(BillingClient.ProductType.INAPP).build());
        billing.queryProductDetailsAsync(QueryProductDetailsParams.newBuilder().setProductList(ids).build(), (r, d) -> {
            if (r.getResponseCode() != BillingClient.BillingResponseCode.OK) { message("Could not load Treasure Trove: " + r.getDebugMessage()); return; }
            products.clear(); for (ProductDetails p : d.getProductDetailsList()) products.put(p.getProductId(), p);
            if (listener != null) listener.onProductsReady(new ArrayList<>(products.values()));
            restorePurchases();
        });
    }

    public void buy(Activity a, String id) {
        ProductDetails d = products.get(id); if (d == null) { message("That Treasure is not currently available."); return; }
        BillingFlowParams.ProductDetailsParams p = BillingFlowParams.ProductDetailsParams.newBuilder().setProductDetails(d).build();
        BillingResult r = billing.launchBillingFlow(a, BillingFlowParams.newBuilder().setProductDetailsParamsList(Collections.singletonList(p)).build());
        if (r.getResponseCode() != BillingClient.BillingResponseCode.OK) message("Google Play could not start the purchase: " + r.getDebugMessage());
    }

    public void restorePurchases() {
        if (!billing.isReady()) return;
        billing.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP).build(), (r, ps) -> {
            if (r.getResponseCode() == BillingClient.BillingResponseCode.OK && ps != null) for (Purchase p : ps) handlePurchase(p);
        });
    }

    @Override public void onPurchasesUpdated(BillingResult r, List<Purchase> ps) {
        if (r.getResponseCode() == BillingClient.BillingResponseCode.OK && ps != null) for (Purchase p : ps) handlePurchase(p);
        else if (r.getResponseCode() != BillingClient.BillingResponseCode.USER_CANCELED) message("Purchase was not completed: " + r.getDebugMessage());
    }

    private void handlePurchase(Purchase p) {
        if (p.getPurchaseState() == Purchase.PurchaseState.PENDING) { message("Purchase pending. Google Play will notify us when it completes."); return; }
        if (p.getPurchaseState() != Purchase.PurchaseState.PURCHASED) return;
        for (String id : p.getProducts()) if (isKnownProduct(id)) verifyThenGrant(p, id);
    }

    private boolean isKnownProduct(String id) { return products.containsKey(id) || findCatalogItem(id) != null; }
    private TreasureCatalog.Item findCatalogItem(String id) { for (TreasureCatalog.Item item : TreasureCatalog.all()) if (item.productId.equals(id)) return item; return null; }

    private void verifyThenGrant(Purchase p, String id) {
        if (verifyUrl == null || verifyUrl.trim().isEmpty()) { message("Treasure purchases are temporarily unavailable until purchase verification is configured."); return; }
        new Thread(() -> {
            boolean ok; try { ok = verify(p, id); } catch (Exception e) { ok = false; }
            final boolean verified = ok;
            mainHandler.post(() -> {
                if (!verified) { message("We couldn't verify that purchase yet. Your Treasure has not been granted; please try Restore Purchases later."); return; }
                grant(id);
                if (!p.isAcknowledged()) billing.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder().setPurchaseToken(p.getPurchaseToken()).build(), r -> { if (r.getResponseCode() != BillingClient.BillingResponseCode.OK) message("Purchase acknowledgement will be retried."); });
            });
        }, "pypet-purchase-verification").start();
    }

    private boolean verify(Purchase p, String id) throws Exception {
        URL url = new URL(verifyUrl); if (!"https".equalsIgnoreCase(url.getProtocol())) return false;
        HttpURLConnection c = (HttpURLConnection) url.openConnection(); c.setRequestMethod("POST"); c.setConnectTimeout(8000); c.setReadTimeout(10000); c.setDoOutput(true); c.setRequestProperty("Content-Type", "application/json; charset=UTF-8"); c.setRequestProperty("Accept", "application/json");
        JSONObject body = new JSONObject(); body.put("packageName", "com.odelly.pypet"); body.put("productId", id); body.put("purchaseToken", p.getPurchaseToken());
        try (OutputStream out = c.getOutputStream()) { out.write(body.toString().getBytes(StandardCharsets.UTF_8)); }
        int code = c.getResponseCode(); if (code != HttpURLConnection.HTTP_OK) { c.disconnect(); return false; }
        try (InputStream in = c.getInputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder(); String line; while ((line = reader.readLine()) != null) response.append(line);
            JSONObject result = new JSONObject(response.toString()); return result.optBoolean("verified", false) && id.equals(result.optString("productId"));
        } finally { c.disconnect(); }
    }
    private void grant(String id) { if (!prefs.getBoolean(id, false)) { prefs.edit().putBoolean(id, true).apply(); if (listener != null) listener.onPurchaseGranted(id); } }
    public boolean owns(String id) { return prefs.getBoolean(id, false); }
    private void message(String s) { mainHandler.post(() -> { if (listener != null) listener.onMessage(s); }); }
}
