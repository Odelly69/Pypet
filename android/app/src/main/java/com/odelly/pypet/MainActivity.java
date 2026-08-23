package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.*;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    TextView status;
    TextView rewardStatus;
    TextView petView;
    EditText editor;
    Python py;
    RewardAdManager rewardAdManager;
    RewardCatalog.Item selectedItem;
    Spinner rewardSpinner;
    Button earnButton;
    Button buyButton;
    PypetAudio audio;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));
        py = Python.getInstance();
        audio = new PypetAudio();

        MobileAds.initialize(this, initializationStatus -> {});
        rewardAdManager = new RewardAdManager(this);
        rewardAdManager.preload();

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setGravity(Gravity.CENTER_HORIZONTAL);
        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("🐾 Pypet Python Academy");
        title.setTextSize(26);
        title.setTextColor(Color.DKGRAY);
        root.addView(title);

        status = new TextView(this);
        status.setText("Meet Pip. Learn Python by teaching Pip new tricks.");
        status.setTextSize(18);
        root.addView(status);

        petView = new TextView(this);
        petView.setText("🐶");
        petView.setTextSize(72);
        petView.setGravity(Gravity.CENTER);
        root.addView(petView, new LinearLayout.LayoutParams(-1, 150));
        animatePet();

        LinearLayout petButtons = new LinearLayout(this);
        Button feed = button("Feed Pip");
        Button play = button("Play");
        Button learn = button("Learn");
        petButtons.addView(feed); petButtons.addView(play); petButtons.addView(learn);
        root.addView(petButtons);

        LinearLayout soundRow = new LinearLayout(this);
        Button music = button("♫ Music: ON");
        soundRow.addView(music);
        root.addView(soundRow);
        music.setOnClickListener(v -> {
            audio.setEnabled(!audio.isEnabled());
            if (audio.isEnabled()) {
                music.setText("♫ Music: ON");
                audio.start();
            } else {
                music.setText("♫ Music: OFF");
            }
        });
        audio.start();

        editor = new EditText(this);
        editor.setGravity(Gravity.TOP | Gravity.START);
        editor.setText("answer = 2 + 3\nprint(answer)");
        editor.setHint("Write Python here...");
        editor.setMinLines(8);
        root.addView(editor, new LinearLayout.LayoutParams(-1, 0, 1));

        Button run = button("Run Python Lesson");
        root.addView(run);
        TextView note = new TextView(this);
        note.setText("Calm mode: no flashing, strobing, screen shake, or reward haptics. Animations are gentle and non-flashing.");
        root.addView(note);

        root.addView(sectionTitle("🎁 Reward Center"));
        TextView rewardExplanation = new TextView(this);
        rewardExplanation.setText("Optional sponsored world items use Pypet Coins. Watch an optional rewarded ad to earn coins, then choose which exclusive item to buy. Python lessons, pet care, normal pets, and the unicorn are never locked behind ads.");
        root.addView(rewardExplanation);

        rewardStatus = new TextView(this);
        root.addView(rewardStatus);

        earnButton = button("Watch optional ad: +" + RewardAdManager.COINS_PER_REWARDED_AD + " Pypet Coins");
        root.addView(earnButton);

        List<String> rewardNames = new ArrayList<>();
        for (RewardCatalog.Item item : RewardCatalog.all()) {
            rewardNames.add(item.name + " — " + item.priceCoins + " Pypet Coins");
        }
        rewardSpinner = new Spinner(this);
        rewardSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rewardNames));
        root.addView(rewardSpinner);

        buyButton = button("Buy selected item");
        root.addView(buyButton);

        TextView policyNote = new TextView(this);
        policyNote.setText("You choose whether to watch each rewarded ad. Skipping or declining never blocks normal gameplay. Coins and items are non-transferable game content.");
        root.addView(policyNote);

        refreshRewardStatus();
        feed.setOnClickListener(v -> { status.setText("Pip is happily eating. +15 hunger"); audio.petSound(330); animatePet(); });
        play.setOnClickListener(v -> { status.setText("Pip wants to play. +10 happiness"); audio.petSound(392); animatePet(); });
        learn.setOnClickListener(v -> { status.setText("Lesson progress recorded. Next: variables and expressions."); audio.petSound(523); animatePet(); });
        run.setOnClickListener(v -> runCode());
        earnButton.setOnClickListener(v -> earnCoins());
        buyButton.setOnClickListener(v -> purchaseSelectedItem());
        setContentView(scroll);
    }

    private void animatePet() {
        if (petView == null) return;
        ScaleAnimation bounce = new ScaleAnimation(0.96f, 1.04f, 0.96f, 1.04f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        bounce.setDuration(650);
        bounce.setRepeatCount(1);
        bounce.setRepeatMode(Animation.REVERSE);
        petView.startAnimation(bounce);
    }

    private TextView sectionTitle(String text) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(22);
        view.setTextColor(Color.DKGRAY);
        view.setPadding(0, 24, 0, 8);
        return view;
    }

    private Button button(String text) {
        Button b = new Button(this); b.setText(text); return b;
    }

    private void earnCoins() {
        earnButton.setEnabled(false);
        rewardAdManager.show(this, new RewardAdManager.RewardListener() {
            @Override public void onCoinsGranted(int coins) {
                refreshRewardStatus();
                status.setText("You earned " + coins + " Pypet Coins. Choose an exclusive world item to buy.");
                animatePet();
                audio.petSound(523);
                earnButton.setEnabled(true);
            }
            @Override public void onAdUnavailable(String message) {
                status.setText(message);
                earnButton.setEnabled(true);
            }
        });
    }

    private void purchaseSelectedItem() {
        RewardCatalog.Item item = RewardCatalog.all().get(rewardSpinner.getSelectedItemPosition());
        if (RewardInventory.owns(this, item.id)) {
            status.setText("You already own " + item.name + ".");
            return;
        }
        int balance = RewardInventory.coins(this);
        if (balance < item.priceCoins) {
            int needed = item.priceCoins - balance;
            new AlertDialog.Builder(this)
                    .setTitle("Not enough Pypet Coins")
                    .setMessage(item.name + " costs " + item.priceCoins + " coins. You have " + balance + " and need " + needed + " more.")
                    .setPositiveButton("Earn Coins", (dialog, which) -> earnCoins())
                    .setNegativeButton("Cancel", null)
                    .show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Buy " + item.name + "?")
                .setMessage(item.description + "\n\nPrice: " + item.priceCoins + " Pypet Coins. Your balance after purchase: " + (balance - item.priceCoins) + ".")
                .setPositiveButton("Buy", (dialog, which) -> {
                    if (RewardInventory.purchase(this, item.id)) {
                        try {
                            py.getModule("world_api").callAttr("place_exclusive_item", item.id, "home");
                        } catch (Exception e) {
                            status.setText("Bought " + item.name + ", but world placement will retry: " + e.getMessage());
                        }
                        refreshRewardStatus();
                        status.setText(item.name + " has been added to your world-item collection and placed at home.");
                        animatePet();
                        audio.petSound(440);
                    } else {
                        refreshRewardStatus();
                        status.setText("Purchase could not be completed.");
                    }
                })
                .setNegativeButton("No thanks", null)
                .show();
    }

    private void refreshRewardStatus() {
        rewardStatus.setText("Pypet Coins: " + RewardInventory.coins(this)
                + "    |    Exclusive items owned: " + RewardInventory.count(this)
                + " / " + RewardCatalog.all().size());
    }

    private void runCode() {
        try {
            String result = py.getModule("pypet_engine").callAttr("run_lesson", editor.getText().toString()).toString();
            status.setText(result);
            audio.petSound(523);
            animatePet();
        } catch (Exception e) {
            status.setText("Lesson error: " + e.getMessage());
            audio.petSound(196);
        }
    }

    @Override protected void onPause() {
        super.onPause();
        audio.stop();
    }

    @Override protected void onResume() {
        super.onResume();
        if (audio != null && audio.isEnabled()) audio.start();
    }

    @Override protected void onDestroy() {
        audio.stop();
        super.onDestroy();
    }
}
