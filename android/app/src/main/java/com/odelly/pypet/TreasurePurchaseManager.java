package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Client-side Google Play Billing integration for one-time Treasure purchases.
 * Production should additionally validate purchase tokens on a trusted backend
 * before granting valuable content. No ad or Play purchase is used to gate lessons.
 */
public final class TreasurePurchaseManager implements BillingClient.PurchasesUpdatedListener {
    public interface Listener {
        void onProductsReady(List<ProductDetails> products);
        void onPurchaseGranted(String productId);
        void onMessage(String message);
    }

    private static final String PREFS = "pypet_treasure_purchases";
    private final BillingClient billing;
    private final SharedPreferences prefs;
    private final Map<String, ProductDetails> products = new HashMap<>();
    private Listener listener;

    public TreasurePurchaseManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        billing = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(this)
                .build();
    }

    public void connect(Listener listener) {
        this.listener = listener;
        if (billing.isReady()) { queryProducts(); return; }
        billing.startConnection(new BillingClientStateListener() {
            @Override public void onBillingSetupFinished(BillingResult result) {
                if (result.getResponseCode() == BillingClient.BillingResponseCode.OK) queryProducts();
                else message("Google Play Billing unavailable: " + result.getDebugMessage());
            }
            @Override public void onBillingServiceDisconnected() {
                message("Google Play Billing disconnected. It will retry when the store is opened again.");
            }
        });
    }

    private void queryProducts() {
        List<QueryProductDetailsParams.Product> ids = new ArrayList<>();
        for (TreasureCatalog.Item item : TreasureCatalog.all()) {
            ids.add(QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(item.productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build());
        }
        billing.queryProductDetailsAsync(QueryProductDetailsParams.newBuilder().setProductList(ids).build(), (result, details) -> {
            if (result.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                message("Could not load Treasure Trove: " + result.getDebugMessage()); return;
            }
            products.clear();
            for (ProductDetails d : details.getProductDetailsList()) products.put(d.getProductId(), d);
            if (listener != null) listener.onProductsReady(new ArrayList<>(products.values()));
            restorePurchases();
        });
    }

    public void buy(Activity activity, String productId) {
        ProductDetails details = products.get(productId);
        if (details == null) { message("That Treasure is not currently available from Google Play."); return; }
        BillingFlowParams.ProductDetailsParams params = BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(details).build();
        BillingResult result = billing.launchBillingFlow(activity,
                BillingFlowParams.newBuilder().setProductDetailsParamsList(java.util.Collections.singletonList(params)).build());
        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK)
            message("Google Play could not start the purchase: " + result.getDebugMessage());
    }

    public void restorePurchases() {
        if (!billing.isReady()) return;
        billing.queryPurchasesAsync(QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.INAPP).build(), (result, purchases) -> {
            if (result.getResponseCode() == BillingClient.BillingResponseCode.OK)
                for (Purchase p : purchases) handlePurchase(p);
        });
    }

    @Override public void onPurchasesUpdated(BillingResult result, List<Purchase> purchases) {
        if (result.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase p : purchases) handlePurchase(p);
        } else if (result.getResponseCode() != BillingClient.BillingResponseCode.USER_CANCELED) {
            message("Purchase was not completed: " + result.getDebugMessage());
        }
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() != Purchase.PurchaseState.PURCHASED) {
            if (purchase.getPurchaseState() == Purchase.PurchaseState.PENDING) message("Purchase pending. Google Play will notify us when it completes.");
            return;
        }
        for (String productId : purchase.getProducts()) {
            // Local idempotency prevents duplicate grants after restoration. A backend should
            // perform authoritative token verification before production entitlement delivery.
            if (!prefs.getBoolean(productId, false)) {
                prefs.edit().putBoolean(productId, true).apply();
                if (listener != null) listener.onPurchaseGranted(productId);
            }
        }
        if (!purchase.isAcknowledged()) {
            billing.acknowledgePurchase(AcknowledgePurchaseParams.newBuilder()
                    .setPurchaseToken(purchase.getPurchaseToken()).build(), result -> {
                        if (result.getResponseCode() != BillingClient.BillingResponseCode.OK)
                            message("Purchase acknowledgement will be retried.");
                    });
        }
    }

    public boolean owns(String productId) { return prefs.getBoolean(productId, false); }

    private void message(String text) { if (listener != null) listener.onMessage(text); }
}
