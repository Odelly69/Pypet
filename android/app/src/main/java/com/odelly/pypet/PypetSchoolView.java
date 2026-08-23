package com.odelly.pypet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** In-world Academy: cumulative, hands-on and mastery-gated. */
public final class PypetSchoolView {
    private static final String PREFS="pypet_academy";
    private static final String CODE="project_code";
    private PypetSchoolView() {}

    public static void show(Activity a) {
        PetEvolutionManager.attendSchool(a);
        final int mastered=PetEvolutionManager.lessons(a);
        final PypetCurriculum.Lesson current=PypetCurriculum.currentLesson(mastered);
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(20,14,20,14); root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a); title.setText("🏫 PYPET ACADEMY — LEARN BY DOING"); title.setTextSize(22); title.setTextColor(Color.DKGRAY); root.addView(title);
        TextView intro=new TextView(a); intro.setText("Each lesson builds on the previous lesson's project. Learn → See → Write → Run → Inspect → Debug → Practice → Challenge → Apply → Reflect → Master."); intro.setTextSize(14); intro.setGravity(Gravity.CENTER); root.addView(intro);
        TextView count=new TextView(a); count.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" cumulative lessons • Mastered "+mastered); count.setGravity(Gravity.CENTER); root.addView(count);
        TextView stage=new TextView(a); stage.setTextSize(16); stage.setTextColor(Color.rgb(55,45,82)); stage.setGravity(Gravity.CENTER); root.addView(stage);

        Spinner moduleSpinner=new Spinner(a); String[] moduleNames=new String[PypetCurriculum.MODULES.size()];
        for(int i=0;i<moduleNames.length;i++)moduleNames[i]=PypetCurriculum.MODULES.get(i).title;
        moduleSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,moduleNames)); root.addView(moduleSpinner);
        Spinner lessonSpinner=new Spinner(a); root.addView(lessonSpinner);
        TextView lock=new TextView(a); lock.setTextSize(12); root.addView(lock);
        TextView mission=new TextView(a); mission.setTextSize(15); mission.setPadding(5,6,5,6); root.addView(mission);
        EditText code=new EditText(a); code.setGravity(Gravity.TOP|Gravity.START); code.setHint("Write and continue your Python project here..."); code.setMinLines(6); root.addView(code);
        TextView result=new TextView(a); result.setTextSize(14); root.addView(result);
        LinearLayout buttons=new LinearLayout(a); Button run=new Button(a); run.setText("▶ RUN"); Button nextStage=new Button(a); nextStage.setText("NEXT STAGE ▶"); Button hint=new Button(a); hint.setText("💡 HINT"); buttons.addView(run,new LinearLayout.LayoutParams(0,-2,1)); buttons.addView(nextStage,new LinearLayout.LayoutParams(0,-2,1)); buttons.addView(hint,new LinearLayout.LayoutParams(0,-2,1)); root.addView(buttons);

        int currentModule=findModule(current==null?null:current.id); if(currentModule>=0)moduleSpinner.setSelection(currentModule);
        final PypetCurriculum.Lesson[] selectedLesson={current};
        final AcademyLessonSession[] session={current==null?null:new AcademyLessonSession(a,current.id)};

        Runnable updateStage=()->{
            if(session[0]==null){stage.setText("🏆 FULL CURRICULUM MASTERED");nextStage.setEnabled(false);return;}
            stage.setText("Stage: "+session[0].stageLabel()+"\n"+session[0].instruction());
            nextStage.setEnabled(session[0].canAdvance());
            nextStage.setText(session[0].stage()==AcademyLessonSession.Stage.MASTER?"✓ MASTER LESSON":"NEXT STAGE ▶");
        };

        Runnable refreshLessons=()->{
            int mi=moduleSpinner.getSelectedItemPosition(); if(mi<0)return;
            PypetCurriculum.Module m=PypetCurriculum.MODULES.get(mi); java.util.ArrayList<PypetCurriculum.Lesson> allowed=new java.util.ArrayList<>(); java.util.ArrayList<String> labels=new java.util.ArrayList<>();
            for(PypetCurriculum.Lesson l:m.lessons){int idx=PypetCurriculum.indexOf(l.id);if(idx<=PetEvolutionManager.lessons(a)){allowed.add(l);labels.add((idx<PetEvolutionManager.lessons(a)?"✓ ":"▶ ")+l.title);}}
            if(labels.isEmpty())labels.add("🔒 Complete earlier lessons first");
            lessonSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,labels));
            if(current!=null&&mi==currentModule){int pos=0;for(int i=0;i<allowed.size();i++)if(allowed.get(i).id.equals(current.id))pos=i;lessonSpinner.setSelection(pos);}
        };
        Runnable showSelected=()->{
            int mi=moduleSpinner.getSelectedItemPosition();if(mi<0)return;PypetCurriculum.Module m=PypetCurriculum.MODULES.get(mi);int si=lessonSpinner.getSelectedItemPosition();int masteredNow=PetEvolutionManager.lessons(a);
            java.util.ArrayList<PypetCurriculum.Lesson> allowed=new java.util.ArrayList<>();for(PypetCurriculum.Lesson l:m.lessons)if(PypetCurriculum.indexOf(l.id)<=masteredNow)allowed.add(l);
            if(si<0||si>=allowed.size())return;selectedLesson[0]=allowed.get(si);session[0]=new AcademyLessonSession(a,selectedLesson[0].id);
            mission.setText("🎯 "+selectedLesson[0].title+"\nSkill: "+selectedLesson[0].skill+"\n\nHands-on mission: "+selectedLesson[0].task);
            code.setText(a.getSharedPreferences(PREFS,0).getString(CODE,""));
            int idx=PypetCurriculum.indexOf(selectedLesson[0].id);lock.setText(idx<masteredNow?"📖 Review mode — this lesson is already mastered.":"▶ CURRENT LESSON — future lessons unlock only after mastery.");updateStage();
        };
        moduleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){refreshLessons.run();}public void onNothingSelected(android.widget.AdapterView<?> p){}});
        lessonSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){showSelected.run();}public void onNothingSelected(android.widget.AdapterView<?> p){}});
        refreshLessons.run();

        run.setOnClickListener(v->{
            if(selectedLesson[0]==null||session[0]==null)return;
            String source=code.getText().toString().trim(); if(source.isEmpty()){result.setText("✏️ Write the code yourself first.");return;}
            a.getSharedPreferences(PREFS,0).edit().putString(CODE,source).apply();
            try{PyObject py=Python.getInstance().getModule("pypet_engine");String output=py.callAttr("run_lesson",source).toString();result.setText("🧪 Result\n"+output);boolean passed=output.contains("'ok': True")&&output.contains("'passed': True");session[0].recordRun(passed);if(passed){result.append("\n\n✓ Run succeeded. Inspect the result, debug it, practice, apply it, then advance through the remaining stages.");updateStage();}else result.append("\n\n💡 Not mastered yet. Inspect the result, debug your code and run it again.");}catch(Exception e){result.setText("Keep experimenting.\n"+e.getMessage());}
        });

        nextStage.setOnClickListener(v->{
            if(session[0]==null)return;
            if(session[0].stage()==AcademyLessonSession.Stage.MASTER){
                int now=PetEvolutionManager.lessons(a);int idx=PypetCurriculum.indexOf(selectedLesson[0].id);
                if(idx==now&&session[0].readyToMaster()){PetEvolutionManager.completeLesson(a);int after=PetEvolutionManager.lessons(a);PypetCurriculum.Lesson next=PypetCurriculum.currentLesson(after);new AlertDialog.Builder(a).setTitle("🎓 Lesson mastered!").setMessage("🐾 "+PetEvolutionManager.name(a)+" learned it with you.\n\nMastered "+after+" / "+PypetCurriculum.lessonCount()+"\n\n"+(next==null?"🏆 Full course complete!":"Next: "+next.title)).setPositiveButton("Continue",null).show();}
                else new AlertDialog.Builder(a).setTitle("🔐 Mastery required").setMessage("Finish the current lesson's hands-on stages before advancing.").setPositiveButton("Continue",null).show();
                return;
            }
            if(!session[0].advance()){new AlertDialog.Builder(a).setTitle("Keep working").setMessage("Complete the current hands-on activity first. Write and run your code when the stage asks for it.").setPositiveButton("Continue",null).show();return;}
            updateStage();
        });
        hint.setOnClickListener(v->{if(selectedLesson[0]!=null)new AlertDialog.Builder(a).setTitle("💡 Hint").setMessage("Current skill: "+selectedLesson[0].skill+"\n\nBreak the mission into the smallest working step. Keep your previous code, inspect intermediate values, change one thing at a time, and explain the result.").setPositiveButton("Got it",null).show();});
        TextView career=new TextView(a);career.setText("\n📚 Full pathway: Python foundations, practical standard library, OOP, testing, APIs, SQL, Tkinter, Pygame, coordinates/resolution, graphics, 3D, animation, Git/GitHub, portfolio work and job readiness.\n\n🐾 Your demonstrated learning is the pet's learning and World development.");career.setTextSize(14);root.addView(career);
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Return to World",null).show();
    }
    private static int findModule(String id){if(id==null)return 0;for(int i=0;i<PypetCurriculum.MODULES.size();i++)for(PypetCurriculum.Lesson l:PypetCurriculum.MODULES.get(i).lessons)if(id.equals(l.id))return i;return 0;}
}
