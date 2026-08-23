package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** Full-screen hands-on Academy. This is the actual activity launched by the Academy building. */
public final class PypetAcademyActivityView {
    private PypetAcademyActivityView(){}
    public static void show(Activity a){
        PetEvolutionManager.attendSchool(a);
        LinearLayout r=new LinearLayout(a);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(24,20,24,20);
        TextView title=new TextView(a);title.setText("🏫 PYPET ACADEMY — LEARN BY DOING");title.setTextSize(25);title.setTextColor(Color.DKGRAY);title.setGravity(Gravity.CENTER);r.addView(title);
        int mastered=PetEvolutionManager.lessons(a);PypetCurriculum.Lesson lesson=PypetCurriculum.currentLesson(mastered);
        TextView progress=new TextView(a);progress.setGravity(Gravity.CENTER);progress.setTextSize(16);progress.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" lessons\nMastered "+mastered);r.addView(progress);
        if(lesson==null){TextView done=new TextView(a);done.setText("🏆 Full Python curriculum mastered! Build a portfolio project next.");done.setTextSize(19);done.setGravity(Gravity.CENTER);r.addView(done);returnButton(a,r);a.setContentView(r);return;}
        TextView lessonInfo=new TextView(a);lessonInfo.setTextSize(17);lessonInfo.setPadding(0,18,0,18);lessonInfo.setText("🎯 "+lesson.title+"\nSkill: "+lesson.skill+"\n\nHands-on mission: "+lesson.task);r.addView(lessonInfo);
        EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setHint("Write Python code for the mission...");code.setMinLines(7);code.setInputType(1|0x80000);r.addView(code,new LinearLayout.LayoutParams(-1,0,1));
        TextView result=new TextView(a);result.setTextSize(15);result.setPadding(0,10,0,10);r.addView(result);
        LinearLayout buttons=new LinearLayout(a);Button run=new Button(a);run.setText("▶ RUN");Button hint=new Button(a);hint.setText("💡 HINT");Button master=new Button(a);master.setText("✓ MARK MASTERY");buttons.addView(run,new LinearLayout.LayoutParams(0,-2,1));buttons.addView(hint,new LinearLayout.LayoutParams(0,-2,1));buttons.addView(master,new LinearLayout.LayoutParams(0,-2,1));r.addView(buttons);
        run.setOnClickListener(v->{String source=code.getText().toString().trim();if(source.isEmpty()){result.setText("✏️ Write the code yourself first.");return;}try{PyObject py=Python.getInstance().getModule("pypet_engine");String output=py.callAttr("run_lesson",source).toString();boolean passed=output.contains("'ok': True")&&output.contains("'passed': True");result.setText("🧪 Result\n"+output+(passed?"\n\n✓ Mission passed. You can claim mastery.":"\n\n💡 Inspect, debug and run again."));}catch(Exception e){result.setText("🧪 Keep experimenting.\n"+e.getMessage());}});
        hint.setOnClickListener(v->result.setText("💡 Hint\nBreak the mission into the smallest working step. Use print() to inspect values, change one thing at a time, then run again."));
        master.setOnClickListener(v->{PetEvolutionManager.completeLesson(a);progress.setText("✓ Lesson mastered!\nMastered "+PetEvolutionManager.lessons(a)+" / "+PypetCurriculum.lessonCount());Toast.makeText(a,"🎓 Lesson mastered with your pet!",Toast.LENGTH_SHORT).show();});
        returnButton(a,r);a.setContentView(r);
    }
    private static void returnButton(Activity a,LinearLayout r){Button b=new Button(a);b.setText("🌎 Return to PyPet World");b.setAllCaps(false);b.setOnClickListener(v->LivingWorldView.show(a));r.addView(b);}
}
