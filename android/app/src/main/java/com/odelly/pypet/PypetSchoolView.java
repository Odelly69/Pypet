package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/**
 * Academy is a World location. The user learns by doing; demonstrated user
 * progress is the pet's learning and unlocks World development.
 */
public final class PypetSchoolView {
    private PypetSchoolView() {}

    public static void show(Activity a) {
        PetEvolutionManager.attendSchool(a);
        final LinearLayout root = new LinearLayout(a);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(26, 22, 26, 22);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        TextView title = new TextView(a);
        title.setText("🏫 PYPET ACADEMY — LEARN BY DOING");
        title.setTextSize(24);
        title.setTextColor(Color.DKGRAY);
        root.addView(title);

        TextView intro = new TextView(a);
        intro.setText("You learn it. Your pet learns it. Your World grows from it.\n\nNo passive lessons: solve the mission, run your code, observe the result, fix mistakes, and try again.");
        intro.setTextSize(17);
        intro.setGravity(Gravity.CENTER);
        root.addView(intro);

        TextView mission = new TextView(a);
        mission.setTextSize(18);
        mission.setGravity(Gravity.CENTER);
        root.addView(mission);

        EditText code = new EditText(a);
        code.setGravity(Gravity.TOP | Gravity.START);
        code.setHint("Write Python here...\nExample:\nanswer = 2 + 3\nprint(answer)");
        code.setMinLines(8);
        root.addView(code);

        TextView result = new TextView(a);
        result.setTextSize(16);
        root.addView(result);

        Button run = new Button(a);
        run.setText("▶ RUN PYTHON — TEST YOUR IDEA");
        root.addView(run);

        try {
            Object py = com.chaquo.python.Python.getInstance().getModule("pypet_engine");
            int lessonIndex = PetEvolutionManager.lessons(a);
            String lesson = py.callAttr("current_lesson", lessonIndex).toString();
            mission.setText("🎯 Your next mission\n" + lesson + "\n\nComplete the task yourself. Successful code becomes your pet's knowledge.");
        } catch (Exception e) {
            mission.setText("🎯 Your next Python mission\nSolve a small problem for your pet, then run your code.");
        }

        run.setOnClickListener(v -> {
            String source = code.getText().toString().trim();
            if (source.isEmpty()) {
                result.setText("✏️ Write and run your own Python solution first.");
                return;
            }
            try {
                Object py = com.chaquo.python.Python.getInstance().getModule("pypet_engine");
                String output = py.callAttr("run_lesson", source).toString();
                result.setText("🧪 Result\n" + output);
                // A lesson is credited only after code executes successfully.
                if (output.contains("'ok': True") && output.contains("'passed': True")) {
                    PetEvolutionManager.completeLesson(a);
                    new AlertDialog.Builder(a)
                        .setTitle("🎓 Skill learned!")
                        .setMessage("You demonstrated the skill.\n\n🐾 " + PetEvolutionManager.name(a) + " learned it with you.\n🧠 Python mastery: " + PetEvolutionManager.lessons(a) + " lessons\n🌎 World development: " + PetEvolutionManager.balancedDevelopmentScore(a) + "%")
                        .setPositiveButton("Continue exploring", null).show();
                } else {
                    result.append("\n\n💡 Not mastered yet. Experiment, read the error, change your code, and run it again.");
                }
            } catch (Exception e) {
                result.setText("Keep experimenting.\n" + e.getMessage());
            }
        });

        TextView curriculum = new TextView(a);
        curriculum.setText("\n📚 PATH: Novice → Apprentice → Intermediate → Advanced → Expert → Master\n🐍 Python language + standard library + Tkinter + Pygame + real projects\n\nEvery successful skill advances the same learning state used by your pet and World.");
        curriculum.setTextSize(16);
        root.addView(curriculum);

        new AlertDialog.Builder(a).setView(root).setNegativeButton("Return to World", null).show();
    }
}
