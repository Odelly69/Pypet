package com.odelly.pypet;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

/**
 * Explicit opt-in rewarded-ad bridge. Only a completed reward callback grants an item.
 * Debug builds use Google's dedicated test rewarded-ad unit.
 */
public final class RewardAdManager {
    private static final String TAG = "PypetRewardAd";
    private static final String LIVE_REWARDED_ID = "ca-app-pub-3352973554477677/6771650805";
    private static final String TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";

    private final Context context;
    private RewardedAd rewardedAd;
    private boolean loading;

    public interface RewardListener {
        void onRewardGranted(RewardCatalog.Item item);
        void onAdUnavailable(String message);
    }

    public RewardAdManager(Context context) {
        this.context = context.getApplicationContext();
    }

    private String adUnitId() {
        return BuildConfig.DEBUG ? TEST_REWARDED_ID : LIVE_REWARDED_ID;
    }

    public void preload() {
        if (loading || rewardedAd != null) return;
        loading = true;
        RewardedAd.load(context, adUnitId(), new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override public void onAdLoaded(RewardedAd ad) {
                        loading = false;
                        rewardedAd = ad;
                    }
                    @Override public void onAdFailedToLoad(LoadAdError error) {
                        loading = false;
                        rewardedAd = null;
                        Log.w(TAG, "Rewarded ad unavailable: " + error.getMessage());
                    }
                });
    }

    public void show(Activity activity, RewardCatalog.Item item, RewardListener listener) {
        if (item == null) {
            listener.onAdUnavailable("Choose an exclusive world item first.");
            return;
        }
        if (RewardInventory.owns(activity, item.id)) {
            listener.onAdUnavailable("You already own this world item.");
            return;
        }
        if (rewardedAd == null) {
            preload();
            listener.onAdUnavailable("The rewarded ad is not ready yet. Please try again shortly.");
            return;
        }

        RewardedAd ad = rewardedAd;
        rewardedAd = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override public void onAdDismissedFullScreenContent() {
                preload();
            }
            @Override public void onAdFailedToShowFullScreenContent(AdError error) {
                preload();
            }
        });

        ad.show(activity, rewardItem -> {
            // Grant only from Google's earned-reward callback; do not grant on dismissal.
            if (RewardInventory.grant(activity, item.id)) {
                listener.onRewardGranted(item);
            }
        });
    }
}
