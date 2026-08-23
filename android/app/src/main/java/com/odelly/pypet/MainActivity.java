package com.odelly.pypet;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class MainActivity extends Activity {
    TextView status;
    EditText editor;
    Python py;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        if (!Python.isStarted()) Python.start(new AndroidPlatform(this));
        py = Python.getInstance();

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

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

        feed.setOnClickListener(v -> status.setText("Pip is happily eating. +15 hunger"));
        play.setOnClickListener(v -> status.setText("Pip wants to play. +10 happiness"));
        learn.setOnClickListener(v -> status.setText("Lesson progress recorded. Next: variables and expressions."));
        run.setOnClickListener(v -> runCode());
        setContentView(root);
    }

    private Button button(String text) {
        Button b = new Button(this); b.setText(text); return b;
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
