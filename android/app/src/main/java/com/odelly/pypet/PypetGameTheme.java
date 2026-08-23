package com.odelly.pypet;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Lightweight game-like presentation layer: cards, rounded controls, and a warm world palette. */
public final class PypetGameTheme {
    private PypetGameTheme() {}

    public static void apply(LinearLayout root) {
        root.setPadding(20, 18, 20, 28);
        root.setBackgroundColor(Color.rgb(247, 244, 255));
        styleChildren(root);
    }

    private static void styleChildren(LinearLayout root) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView && !(v instanceof Button)) {
                TextView t = (TextView) v;
                String s = t.getText() == null ? "" : t.getText().toString();
                if (s.contains("Academy") || s.contains("Treasure Trove") || s.contains("Trophies") || s.contains("Activities") || s.contains("Coins")) {
                    t.setTextSize(21);
                    t.setTextColor(Color.rgb(69, 52, 102));
                    t.setTypeface(null, android.graphics.Typeface.BOLD);
                } else {
                    t.setTextColor(Color.rgb(63, 61, 70));
                }
            }
            if (v instanceof Button) {
                Button b = (Button) v;
                b.setAllCaps(false);
                b.setTextSize(15);
                b.setTextColor(Color.WHITE);
                GradientDrawable bg = new GradientDrawable();
                bg.setColor(Color.rgb(116, 88, 170));
                bg.setCornerRadius(28);
                b.setBackground(bg);
                b.setPadding(22, 8, 22, 8);
            }
        }
    }
}
