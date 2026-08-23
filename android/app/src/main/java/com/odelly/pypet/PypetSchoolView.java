package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;

/** School is a world location: the user plays Python lessons and that progress develops the pet/world. */
public final class PypetSchoolView {
    private PypetSchoolView(){}
    public static void show(Activity a){
        PetEvolutionManager.attendSchool(a);
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(26,22,26,22);root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a);title.setText("🏫 Pypet Academy");title.setTextSize(29);title.setTextColor(Color.DKGRAY);root.addView(title);
        TextView intro=new TextView(a);intro.setText("🎒 "+PetEvolutionManager.name(a)+" is in class!\n\nYour Python learning game is the lesson. What you learn becomes part of your pet's education and unlocks new world development.");intro.setTextSize(18);intro.setGravity(Gravity.CENTER);root.addView(intro);
        TextView progress=new TextView(a);progress.setText("🏫 School progress: "+PetEvolutionManager.school(a)+"\n🐍 Python lessons: "+PetEvolutionManager.lessons(a)+"\n⭐ Balanced development: "+PetEvolutionManager.balancedDevelopmentScore(a)+"%");progress.setGravity(Gravity.CENTER);progress.setTextSize(18);root.addView(progress);
        EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setHint("Today's challenge\nExample: answer = 2 + 3\nprint(answer)");code.setMinLines(6);root.addView(code);
        Button lesson=new Button(a);lesson.setText("🐍 Start Python Lesson");root.addView(lesson);lesson.setOnClickListener(v->{try{String result=com.chaquo.python.Python.getInstance().getModule("pypet_engine").callAttr("run_lesson",code.getText().toString()).toString();PetEvolutionManager.completeLesson(a);new AlertDialog.Builder(a).setTitle("🎓 Class complete!").setMessage(result+"\n\n🐾 "+PetEvolutionManager.name(a)+" learned with you.\n🧠 Learning progress: "+PetEvolutionManager.lessons(a)+" lessons\n🌎 World development progress: "+PetEvolutionManager.balancedDevelopmentScore(a)+"%").setPositiveButton("Back to world",null).show();}catch(Exception e){new AlertDialog.Builder(a).setTitle("Keep practicing").setMessage("That lesson needs another try. Your pet is cheering you on!\n\n"+e.getMessage()).setPositiveButton("Try again",null).show();}});
        TextView world=new TextView(a);world.setText("\n🏗️ World development\nLearning unlocks world capabilities progressively: basic commands → building tools → interactive systems → automation → advanced projects. The school game is the gateway to developing your world.");world.setTextSize(17);root.addView(world);
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Leave school",null).show();
    }
}
