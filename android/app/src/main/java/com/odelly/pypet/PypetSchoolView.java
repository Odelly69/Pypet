package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** Academy is a World location. The user learns by doing; demonstrated progress is the pet's learning. */
public final class PypetSchoolView {
    private PypetSchoolView() {}
    public static void show(Activity a) {
        PetEvolutionManager.attendSchool(a);
        LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,16,22,16);root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a);title.setText("🏫 PYPET ACADEMY — LEARN BY DOING");title.setTextSize(23);title.setTextColor(Color.DKGRAY);root.addView(title);
        TextView intro=new TextView(a);intro.setText("You learn it. Your pet learns it. Your World grows from it.\n\nSolve the mission, write code, run it, observe the result, debug it and try again.");intro.setTextSize(16);intro.setGravity(Gravity.CENTER);root.addView(intro);
        TextView count=new TextView(a);count.setText("🐍 Full pathway: "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" hands-on lessons");count.setGravity(Gravity.CENTER);root.addView(count);
        Spinner moduleSpinner=new Spinner(a);String[] moduleNames=new String[PypetCurriculum.MODULES.size()];for(int i=0;i<moduleNames.length;i++)moduleNames[i]=PypetCurriculum.MODULES.get(i).title;moduleSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,moduleNames));root.addView(moduleSpinner);
        Spinner lessonSpinner=new Spinner(a);root.addView(lessonSpinner);
        TextView mission=new TextView(a);mission.setTextSize(16);mission.setPadding(6,8,6,8);root.addView(mission);
        EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setHint("Write Python here...\nExample:\nanswer = 2 + 3\nprint(answer)");code.setMinLines(7);root.addView(code);
        TextView result=new TextView(a);result.setTextSize(15);root.addView(result);
        LinearLayout buttons=new LinearLayout(a);Button run=new Button(a);run.setText("▶ RUN");Button check=new Button(a);check.setText("✓ CHECK");Button hint=new Button(a);hint.setText("💡 HINT");buttons.addView(run);buttons.addView(check);buttons.addView(hint);root.addView(buttons);
        Runnable refresh=()->{PypetCurriculum.Module m=PypetCurriculum.MODULES.get(moduleSpinner.getSelectedItemPosition());String[] names=new String[m.lessons.size()];for(int i=0;i<names.length;i++)names[i]=m.lessons.get(i).title;lessonSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,names));};
        moduleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){refresh.run();}public void onNothingSelected(android.widget.AdapterView<?> p){}});
        lessonSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){PypetCurriculum.Lesson l=PypetCurriculum.MODULES.get(moduleSpinner.getSelectedItemPosition()).lessons.get(pos);mission.setText("🎯 Skill: "+l.skill+"\n\nHands-on mission: "+l.task+"\n\nMastery comes from doing, not reading.");}public void onNothingSelected(android.widget.AdapterView<?> p){}});
        run.setOnClickListener(v->{String source=code.getText().toString().trim();if(source.isEmpty()){result.setText("✏️ Write your own solution first.");return;}try{PyObject py=Python.getInstance().getModule("pypet_engine");String output=py.callAttr("run_lesson",source).toString();result.setText("🧪 Result\n"+output);if(output.contains("'ok': True")&&output.contains("'passed': True")){PetEvolutionManager.completeLesson(a);new AlertDialog.Builder(a).setTitle("🎓 Skill learned!").setMessage("You demonstrated the skill.\n\n🐾 "+PetEvolutionManager.name(a)+" learned it with you.\n🧠 Mastery: "+PetEvolutionManager.lessons(a)+" lessons\n🌎 World development: "+PetEvolutionManager.balancedDevelopmentScore(a)+"%").setPositiveButton("Continue",null).show();}else result.append("\n\n💡 Not mastered yet. Read the result, change your code, and run it again.");}catch(Exception e){result.setText("Keep experimenting.\n"+e.getMessage());}});
        check.setOnClickListener(v->new AlertDialog.Builder(a).setTitle("✓ Check your thinking").setMessage("Before running: predict the output. After running: explain why you got it. Then improve the code. This is how the pet learns with you.").setPositiveButton("Continue coding",null).show());
        hint.setOnClickListener(v->{PypetCurriculum.Lesson l=PypetCurriculum.MODULES.get(moduleSpinner.getSelectedItemPosition()).lessons.get(lessonSpinner.getSelectedItemPosition());new AlertDialog.Builder(a).setTitle("💡 Hint").setMessage("Break the mission into the smallest working step. Current skill: "+l.skill+". Inspect intermediate values and change one thing at a time.").setPositiveButton("Got it",null).show();});
        TextView career=new TextView(a);career.setText("\n📚 Includes Python foundations, standard library, OOP, testing, APIs, SQL, Tkinter, Pygame, graphics, 3D, animation, systems, Git/GitHub, portfolio work and job readiness.\n\n🐾 Your demonstrated learning is the pet's learning and unlocks World development.");career.setTextSize(15);root.addView(career);
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Return to World",null).show();
    }
}