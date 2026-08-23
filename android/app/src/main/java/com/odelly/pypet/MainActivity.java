package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    TextView status;
    TextView rewardStatus;
    EditText editor;
    Python py;
    RewardAdManager rewardAdManager;
    Spinner rewardSpinner;
    Button rewardButton;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));
        py = Python.getInstance();

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

        LinearLayout petButtons = new LinearLayout(this);
        Button feed = button("Feed Pip");
        Button play = button("Play");
        Button learn = button("Learn");
        petButtons.addView(feed); petButtons.addView(play); petButtons.addView(learn);
        root.addView(petButtons);

        editor = new EditText(this);
        editor.setGravity(Gravity.TOP | Gravity.START);
        editor.setText("answer = 2 + 3\nprint(answer)");
        editor.setHint("Write Python here...");
        editor.setMinLines(8);
        root.addView(editor, new LinearLayout.LayoutParams(-1, 0, 1));

        Button run = button("Run Python Lesson");
        root.addView(run);
        TextView note = new TextView(this);
        note.setText("Calm mode: no flashing, strobing, screen shake, or reward haptics.");
        root.addView(note);

        root.addView(sectionTitle("🎁 Reward Center"));
        TextView rewardExplanation = new TextView(this);
        rewardExplanation.setText("Optional sponsored world items. Your Python lessons, pet care, normal pets, and unicorn are never locked behind ads.");
        root.addView(rewardExplanation);

        List<String> rewardNames = new ArrayList<>();
        for (RewardCatalog.Item item : RewardCatalog.all()) rewardNames.add(item.name);
        rewardSpinner = new Spinner(this);
        rewardSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, rewardNames));
        root.addView(rewardSpinner);

        rewardStatus = new TextView(this);
        rewardStatus.setText("Owned: " + RewardInventory.count(this) + " / " + RewardCatalog.all().size());
        root.addView(rewardStatus);

        rewardButton = button("Watch optional ad to unlock item");
        root.addView(rewardButton);

        TextView policyNote = new TextView(this);
        policyNote.setText("You choose whether to watch each rewarded ad. Skipping or declining never blocks normal gameplay.");
        root.addView(policyNote);

        feed.setOnClickListener(v -> status.setText("Pip is happily eating. +15 hunger"));
        play.setOnClickListener(v -> status.setText("Pip wants to play. +10 happiness"));
        learn.setOnClickListener(v -> status.setText("Lesson progress recorded. Next: variables and expressions."));
        run.setOnClickListener(v -> runCode());

        rewardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                RewardCatalog.Item item = RewardCatalog.all().get(position);
                rewardButton.setText(RewardInventory.owns(MainActivity.this, item.id)
                        ? "Already owned: " + item.name
                        : "Watch optional ad to unlock " + item.name);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        rewardButton.setOnClickListener(v -> requestReward());
        setContentView(scroll);
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

    private void requestReward() {
        RewardCatalog.Item item = RewardCatalog.all().get(rewardSpinner.getSelectedItemPosition());
        if (RewardInventory.owns(this, item.id)) {
            rewardStatus.setText("You already own " + item.name + ".");
            return;
        }

        // Clear disclosure and affirmative opt-in before every rewarded ad.
        new AlertDialog.Builder(this)
                .setTitle("Unlock " + item.name + "?")
                .setMessage("Watch an optional rewarded ad to receive 1 exclusive, non-transferable " + item.name + " for your Pypet world. You can choose No and continue playing normally.")
                .setPositiveButton("Watch ad", (dialog, which) -> showRewardedAd(item))
                .setNegativeButton("No thanks", null)
                .show();
    }

    private void showRewardedAd(RewardCatalog.Item item) {
        rewardButton.setEnabled(false);
        rewardAdManager.show(this, item, new RewardAdManager.RewardListener() {
            @Override public void onRewardGranted(RewardCatalog.Item rewardedItem) {
                rewardStatus.setText("Unlocked: " + rewardedItem.name + "! Owned: "
                        + RewardInventory.count(MainActivity.this) + " / " + RewardCatalog.all().size());
                rewardButton.setEnabled(true);
                rewardButton.setText("Already owned: " + rewardedItem.name);
                status.setText(rewardedItem.name + " has been added to your world-item collection.");
            }

            @Override public void onAdUnavailable(String message) {
                rewardStatus.setText(message);
                rewardButton.setEnabled(true);
            }
        });
    }

    private void runCode() {
        try {
            String result = py.getModule("pypet_engine").callAttr("run_lesson", editor.getText().toString()).toString();
            status.setText(result);
        } catch (Exception e) {
            status.setText("Lesson error: " + e.getMessage());
        }
    }
}
