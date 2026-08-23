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

/** Explicit opt-in rewarded-ad bridge. A completed rewarded-ad callback creates Pypet Coins. */
public final class RewardAdManager {
    private static final String TAG = "PypetRewardAd";
    private static final String LIVE_REWARDED_ID = "ca-app-pub-3352973554477677/6771650805";
    private static final String TEST_REWARDED_ID = "ca-app-pub-3940256099942544/5224354917";
    public static final int COINS_PER_REWARDED_AD = 25;
    private final Context context;
    private RewardedAd rewardedAd;
    private boolean loading;
    public interface RewardListener { void onCoinsGranted(int coins); void onAdUnavailable(String message); }
    public RewardAdManager(Context context) { this.context = context.getApplicationContext(); }
    private String adUnitId() {
        // Keep production builds on the configured live unit without depending on a generated BuildConfig class.
        return TEST_REWARDED_ID.equals(LIVE_REWARDED_ID) ? TEST_REWARDED_ID : LIVE_REWARDED_ID;
    }
    public void preload() {
        if (loading || rewardedAd != null) return; loading = true;
        RewardedAd.load(context, adUnitId(), new AdRequest.Builder().build(), new RewardedAdLoadCallback() {
            @Override public void onAdLoaded(RewardedAd ad) { loading = false; rewardedAd = ad; }
            @Override public void onAdFailedToLoad(LoadAdError error) { loading = false; rewardedAd = null; Log.w(TAG, "Rewarded ad unavailable: " + error.getMessage()); }
        });
    }
    public void show(Activity activity, RewardListener listener) {
        if (rewardedAd == null) { preload(); listener.onAdUnavailable("The rewarded ad is not ready yet. Please try again shortly."); return; }
        RewardedAd ad = rewardedAd; rewardedAd = null;
        ad.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override public void onAdDismissedFullScreenContent() { preload(); }
            @Override public void onAdFailedToShowFullScreenContent(AdError error) { preload(); }
        });
        ad.show(activity, rewardItem -> { RewardInventory.addCoins(activity, COINS_PER_REWARDED_AD); listener.onCoinsGranted(COINS_PER_REWARDED_AD); });
    }
}
