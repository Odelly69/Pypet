package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** Full-screen hands-on Academy. No description-only dialog; progress is persisted by lesson/stage. */
public final class PypetAcademyActivityView {
    private PypetAcademyActivityView(){}

    public static void show(Activity a){
        PetEvolutionManager.attendSchool(a);
        LinearLayout r=new LinearLayout(a);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(24,20,24,20);
        ScrollView scroll=new ScrollView(a);scroll.addView(r);
        TextView title=new TextView(a);title.setText("🏫 PyPet Academy — Learn by Doing");title.setTextSize(25);title.setTextColor(Color.DKGRAY);title.setGravity(Gravity.CENTER);r.addView(title);
        int mastered=PetEvolutionManager.lessons(a);PypetCurriculum.Lesson lesson=PypetCurriculum.currentLesson(mastered);
        TextView progress=new TextView(a);progress.setGravity(Gravity.CENTER);progress.setTextSize(16);progress.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" lessons\nMastered "+mastered);r.addView(progress);
        if(lesson==null){TextView done=new TextView(a);done.setText("🏆 Full Python curriculum mastered!\n\nBuild a portfolio project and continue developing the World.");done.setTextSize(19);done.setGravity(Gravity.CENTER);r.addView(done);returnButton(a,r);a.setContentView(scroll);return;}

        AcademyLessonSession session=new AcademyLessonSession(a,lesson.id);
        TextView stage=new TextView(a);stage.setTextSize(17);stage.setGravity(Gravity.CENTER);stage.setPadding(0,12,0,12);r.addView(stage);
        TextView lessonInfo=new TextView(a);lessonInfo.setTextSize(17);lessonInfo.setPadding(0,8,0,14);lessonInfo.setText("🎯 "+lesson.title+"\nSkill: "+lesson.skill+"\n\nHands-on mission: "+lesson.task);r.addView(lessonInfo);
        EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setHint("Write Python code for the mission...");code.setMinLines(8);code.setInputType(1|0x80000);r.addView(code,new LinearLayout.LayoutParams(-1,0,1));
        TextView result=new TextView(a);result.setTextSize(15);result.setPadding(0,10,0,10);r.addView(result);
        Button run=new Button(a);run.setText("▶ RUN REAL PYTHON");r.addView(run);
        Button advance=new Button(a);advance.setText("NEXT STAGE ▶");r.addView(advance);
        Button hint=new Button(a);hint.setText("💡 HINT");r.addView(hint);
        Runnable refresh=()->{stage.setText("Stage: "+session.stageLabel()+"\n"+session.instruction());advance.setText(session.stage()==AcademyLessonSession.Stage.MASTER?"✓ COMPLETE LESSON":"NEXT STAGE ▶");advance.setEnabled(session.canAdvance()||session.stage()==AcademyLessonSession.Stage.MASTER);progress.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" lessons\nMastered "+PetEvolutionManager.lessons(a));};
        refresh.run();
        run.setOnClickListener(v->{String source=code.getText().toString().trim();if(source.isEmpty()){result.setText("✏️ Write the code yourself first.");return;}try{PyObject py=Python.getInstance().getModule("pypet_engine");String output=py.callAttr("run_lesson",source).toString();boolean passed=output.contains("'ok': True")&&output.contains("'passed': True");session.recordRun(passed);result.setText("🧪 Real execution\n"+output+(passed?"\n\n✓ Passed. Inspect the result, debug as needed, then advance.":"\n\n💡 Not passed yet. Inspect the result, change one thing, and run again."));refresh.run();}catch(Exception e){result.setText("🧪 Python execution error\n"+String.valueOf(e.getMessage()));}});
        advance.setOnClickListener(v->{if(session.stage()==AcademyLessonSession.Stage.MASTER){if(session.readyToMaster()){PetEvolutionManager.completeLesson(a);RewardInventory.completeTask(a,"academy_"+lesson.id,10);PypetAchievementManager.awardTrophy(a,"python_starter",25);Toast.makeText(a,"🎓 Lesson mastered! Your pet learned it with you.",Toast.LENGTH_SHORT).show();show(a); }else result.setText("🔐 Mastery requires a successful run and inspection first.");return;}if(session.advance())refresh.run();else result.setText("🔐 Complete the current hands-on requirement before advancing.");});
        hint.setOnClickListener(v->result.setText("💡 Hint\nBreak the mission into the smallest working step. Use print() to inspect values, change one thing at a time, then run again."));
        returnButton(a,r);a.setContentView(scroll);
    }
    private static void returnButton(Activity a,LinearLayout r){Button b=new Button(a);b.setText("🌎 Return to PyPet World");b.setAllCaps(false);b.setOnClickListener(v->LivingWorldView.show(a));r.addView(b);}
}
