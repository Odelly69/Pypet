package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** Full lesson experience: teach first, then practice, run, inspect, debug and challenge. */
public final class PypetAcademyActivityView {
    private PypetAcademyActivityView(){}

    public static void show(Activity a){
        PetEvolutionManager.attendSchool(a);
        LinearLayout r=new LinearLayout(a);r.setOrientation(LinearLayout.VERTICAL);r.setPadding(24,18,24,18);
        ScrollView scroll=new ScrollView(a);scroll.addView(r);
        TextView title=new TextView(a);title.setText("🏫 PyPet Academy — Learn Python by Doing");title.setTextSize(25);title.setTextColor(Color.DKGRAY);title.setGravity(Gravity.CENTER);r.addView(title);
        int mastered=PetEvolutionManager.lessons(a);PypetCurriculum.Lesson lesson=PypetCurriculum.currentLesson(mastered);
        TextView progress=new TextView(a);progress.setGravity(Gravity.CENTER);progress.setTextSize(16);progress.setPadding(0,8,0,14);progress.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" lessons\nMastered "+mastered);r.addView(progress);
        if(lesson==null){TextView done=new TextView(a);done.setText("🏆 Full Python curriculum mastered!\n\nBuild a portfolio project, review any skill, and keep developing your World.");done.setTextSize(19);done.setGravity(Gravity.CENTER);r.addView(done);returnButton(a,r);a.setContentView(scroll);return;}

        AcademyLessonSession session=new AcademyLessonSession(a,lesson.id);
        PypetCurriculum.Lesson previous=PypetCurriculum.prerequisite(lesson.id);
        TextView stage=new TextView(a);stage.setTextSize(17);stage.setGravity(Gravity.CENTER);stage.setPadding(0,8,0,10);r.addView(stage);
        TextView lessonInfo=new TextView(a);lessonInfo.setTextSize(17);lessonInfo.setPadding(0,8,0,12);r.addView(lessonInfo);
        TextView teaching=new TextView(a);teaching.setTextSize(16);teaching.setPadding(0,8,0,14);r.addView(teaching);
        TextView prerequisite=new TextView(a);prerequisite.setTextSize(14);prerequisite.setTextColor(Color.DKGRAY);prerequisite.setPadding(0,4,0,12);r.addView(prerequisite);
        EditText code=new EditText(a);code.setGravity(Gravity.TOP|Gravity.START);code.setHint("Write Python code for the mission...");code.setMinLines(8);code.setInputType(1|0x80000);r.addView(code,new LinearLayout.LayoutParams(-1,0,1));
        TextView result=new TextView(a);result.setTextSize(15);result.setPadding(0,10,0,10);r.addView(result);
        Button run=new Button(a);run.setText("▶ RUN REAL PYTHON");r.addView(run);
        Button advance=new Button(a);r.addView(advance);
        Button hint=new Button(a);hint.setText("💡 HINT");r.addView(hint);

        Runnable refresh=()->{
            stage.setText("Stage: "+session.stageLabel()+"\n"+session.instruction());
            lessonInfo.setText("🎯 LESSON\n"+lesson.title+"\nSkill: "+lesson.skill+"\n\nMission: "+lesson.task);
            teaching.setText("📖 WHAT YOU NEED TO KNOW\n"+teach(lesson.skill,lesson.title,session.stage()));
            prerequisite.setText(previous==null?"🟢 Starting point — no previous Python knowledge is required.":"🔗 Prerequisite: "+previous.title+"\nThis lesson builds directly on the skill you just mastered.");
            advance.setText(session.stage()==AcademyLessonSession.Stage.MASTER?"✓ COMPLETE LESSON":"NEXT "+nextStage(session.stage())+" ▶");
            advance.setEnabled(session.canAdvance()||session.stage()==AcademyLessonSession.Stage.MASTER);
            progress.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" lessons\nMastered "+PetEvolutionManager.lessons(a));
        };
        refresh.run();

        run.setOnClickListener(v->{
            String source=code.getText().toString().trim();
            if(source.isEmpty()){result.setText("✏️ Enter your answer/code first. The Academy will not assume prior knowledge or supply the solution.");return;}
            try{
                PyObject py=Python.getInstance().getModule("pypet_engine");
                String output=py.callAttr("run_lesson",source).toString();
                boolean passed=output.contains("'ok': True")&&output.contains("'passed': True");
                session.recordRun(passed);
                if(passed){
                    result.setText("🧪 Correct — real Python executed successfully.\n\n➡️ Moving to the next challenge.");
                    if(session.advance()){
                        code.setText("");
                        refresh.run();
                    }else refresh.run();
                }else{
                    result.setText("🧪 Your answer ran, but the challenge is not passed yet.\n\nInspect the result, fix one thing, and run again.\n\n"+output);
                    refresh.run();
                }
            }catch(Exception e){result.setText("🧪 Python execution error\n"+String.valueOf(e.getMessage())+"\n\nFix the code and run it again.");}
        });
        advance.setOnClickListener(v->{
            if(session.stage()==AcademyLessonSession.Stage.MASTER){
                if(session.readyToMaster()){
                    PetEvolutionManager.completeLesson(a);RewardInventory.completeTask(a,"academy_"+lesson.id,10);PypetAchievementManager.awardTrophy(a,"python_starter",25);
                    Toast.makeText(a,"🎓 Lesson mastered! Next lesson unlocked.",Toast.LENGTH_SHORT).show();show(a);
                }else result.setText("🔐 Mastery requires a successful answer and inspection first.");
                return;
            }
            if(session.advance()){code.setText("");result.setText("");refresh.run();}else result.setText("🔐 Complete the current hands-on requirement before advancing.");
        });
        hint.setOnClickListener(v->result.setText("💡 Hint\n"+hintFor(lesson.skill)+"\n\nDon't skip the lesson explanation. Build the answer yourself, run it, inspect the result, then continue."));
        returnButton(a,r);a.setContentView(scroll);
    }
    private static String nextStage(AcademyLessonSession.Stage s){AcademyLessonSession.Stage[] v=AcademyLessonSession.Stage.values();int i=s.ordinal()+1;return i<v.length?v[i].name():"MASTER";}
    private static String teach(String skill,String title,AcademyLessonSession.Stage stage){
        String base;
        if("syntax".equals(skill))base="Python programs are made from readable statements. Indentation matters, and you can run tiny expressions immediately to see what they do.";
        else if("variables".equals(skill))base="A variable is a name bound to a value, for example pet_name = 'Pip'. Python determines the value's type at runtime.";
        else if("arithmetic".equals(skill))base="Use +, -, *, /, //, %, and ** for arithmetic. Parentheses make the intended order explicit.";
        else if("strings".equals(skill))base="Strings hold text. Use quotes and f-strings such as f'Hello {name}' to combine values with readable text.";
        else if("logic".equals(skill)||"conditionals".equals(skill))base="Boolean expressions are True or False. Use if/elif/else to choose behavior based on those expressions.";
        else if("io".equals(skill))base="print() displays information. input() reads text from a user; convert it explicitly when a number is required.";
        else if("loops".equals(skill))base="for repeats over an iterable. while repeats while a condition remains true; always design a safe stopping condition.";
        else if("functions".equals(skill))base="Functions package reusable behavior. Define parameters, call the function, and return a value when the caller needs a result.";
        else if("collections".equals(skill))base="Lists preserve order, tuples are immutable sequences, dictionaries map keys to values, and sets store unique values.";
        else if("errors".equals(skill))base="Exceptions represent runtime problems. Use try/except for expected failures and keep the normal path clear.";
        else if("files".equals(skill))base="Use open() with a context manager so files are closed reliably. Read, write, and validate text deliberately.";
        else if("modules".equals(skill)||"packaging".equals(skill))base="Modules let you separate responsibilities. Import only what you need and keep a clear entry point.";
        else if("oop".equals(skill))base="Objects combine state and behavior. Classes define the model; composition is often preferable to deep inheritance.";
        else if("typing".equals(skill))base="Type hints document intended values and help tools catch mistakes before runtime. They do not replace tests.";
        else if("testing".equals(skill))base="Tests turn expected behavior into repeatable checks. Start with small deterministic cases, then cover edge cases.";
        else if("debugging".equals(skill))base="Debug systematically: reproduce, isolate, inspect values, change one thing, rerun, and verify the fix.";
        else base="This lesson introduces "+title+" through a small practical problem. Start with the concept shown here, then apply it to the mission; the previous lesson is the prerequisite, not assumed outside knowledge.";
        if(stage==AcademyLessonSession.Stage.LEARN)return base+"\n\nStart by understanding the words and purpose before writing code.";
        if(stage==AcademyLessonSession.Stage.SEE)return base+"\n\nPredict the result of a small example before you run your own version.";
        if(stage==AcademyLessonSession.Stage.WRITE)return base+"\n\nNow write the solution yourself. The goal is understanding, not copying.";
        if(stage==AcademyLessonSession.Stage.INSPECT)return base+"\n\nRead the actual output. Explain which values changed and why.";
        return base;
    }
    private static String hintFor(String skill){if("variables".equals(skill))return "Create a variable with name = value, then use print() to inspect it.";if("loops".equals(skill))return "Start with for item in items: and indent the repeated action.";if("conditionals".equals(skill)||"logic".equals(skill))return "Write the Boolean condition first, then put the action under if with indentation.";if("functions".equals(skill))return "Use def function_name(parameter): and return the value the caller needs.";if("collections".equals(skill))return "Choose the collection based on whether you need order, immutability, key/value lookup, or uniqueness.";return "Break the mission into the smallest working step, use print() to inspect values, then change one thing at a time.";}
    private static void returnButton(Activity a,LinearLayout r){Button b=new Button(a);b.setText("🌎 Return to PyPet World");b.setAllCaps(false);b.setOnClickListener(v->LivingWorldView.show(a));r.addView(b);}
}
