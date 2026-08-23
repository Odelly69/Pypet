package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

import com.android.billingclient.api.ProductDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Lightweight Treasure Trove UI. Real prices are displayed from Google Play ProductDetails. */
public final class TreasureStore {
    private TreasureStore() {}

    public static void show(Activity activity) {
        LinearLayout root = new LinearLayout(activity);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 24, 32, 24);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(activity);
        title.setText("✨ Treasure Trove\nFind something wonderful.");
        title.setTextSize(24);
        root.addView(title);

        TextView subtitle = new TextView(activity);
        subtitle.setText("Special cosmetic treasures for your Pypet world. Your Python lessons, normal pets and core gameplay never require a purchase.");
        root.addView(subtitle);

        AlertDialog dialog = new AlertDialog.Builder(activity).setView(root).setNegativeButton("Close", null).create();
        TreasurePurchaseManager manager = new TreasurePurchaseManager(activity);
        manager.connect(new TreasurePurchaseManager.Listener() {
            @Override public void onProductsReady(List<ProductDetails> products) {
                for (TreasureCatalog.Item item : TreasureCatalog.all()) {
                    ProductDetails d = find(products, item.productId);
                    Button buy = new Button(activity);
                    String price = d != null && d.getOneTimePurchaseOfferDetails() != null
                            ? d.getOneTimePurchaseOfferDetails().getFormattedPrice() : item.priceHint;
                    buy.setText(item.name + "  •  " + price);
                    buy.setOnClickListener(v -> manager.buy(activity, item.productId));
                    root.addView(buy);
                }
            }
            @Override public void onPurchaseGranted(String productId) {
                new AlertDialog.Builder(activity).setTitle("Treasure unlocked!")
                        .setMessage("Your Treasure has been added to your collection.").setPositiveButton("Wonderful!", null).show();
            }
            @Override public void onMessage(String message) {
                if (dialog.isShowing()) subtitle.setText(message);
            }
        });
        dialog.show();
    }

    private static ProductDetails find(List<ProductDetails> products, String id) {
        for (ProductDetails d : products) if (id.equals(d.getProductId())) return d;
        return null;
    }
}
