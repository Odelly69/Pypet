package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.*;

/**
 * Compatibility guard for legacy building-learning entry points.
 * Python curriculum is Academy-only. Other buildings must never present lessons.
 */
public final class BuildingLearningGateView {
    private BuildingLearningGateView() {}

    public static void show(Activity a, String building) {
        if ("ACADEMY".equalsIgnoreCase(building)) {
            PypetAcademyActivityView.show(a);
            return;
        }
        LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(Gravity.CENTER);
        root.setPadding(32, 32, 32, 32);

        TextView title = new TextView(a);
        title.setText("🏫 Python Academy");
        title.setTextSize(25);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title);

        TextView message = new TextView(a);
        message.setText("Python lessons are taught only at the Academy.\n\n" +
                "This building has its own activity and does not replace or advance the Academy curriculum.");
        message.setTextSize(17);
        message.setTextColor(Color.rgb(39, 53, 42));
        message.setGravity(Gravity.CENTER);
        message.setPadding(0, 24, 0, 24);
        root.addView(message);

        Button academy = new Button(a);
        academy.setText("🏫 GO TO ACADEMY");
        academy.setAllCaps(false);
        academy.setOnClickListener(v -> PypetAcademyActivityView.show(a));
        root.addView(academy);

        Button back = new Button(a);
        back.setText("🌎 Return to PyPet World");
        back.setAllCaps(false);
        back.setOnClickListener(v -> LivingWorldView.show(a));
        root.addView(back);

        a.setContentView(root);
    }
}
