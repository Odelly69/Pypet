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
    private PypetSchoolView() {}
    private static final String PREFS="pypet_academy";
    private static final String CODE="project_code";

    public static void show(Activity a) {
        PetEvolutionManager.attendSchool(a);
        final int mastered=PetEvolutionManager.lessons(a);
        final PypetCurriculum.Lesson current=PypetCurriculum.currentLesson(mastered);
        LinearLayout root=new LinearLayout(a); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(22,16,22,16); root.setGravity(Gravity.CENTER_HORIZONTAL);
        TextView title=new TextView(a); title.setText("🏫 PYPET ACADEMY — LEARN BY DOING"); title.setTextSize(23); title.setTextColor(Color.DKGRAY); root.addView(title);
        TextView intro=new TextView(a); intro.setText("Learn → See → Write → Run → Inspect → Debug → Practice → Challenge → Apply → Reflect → Master\n\nEach lesson builds on the code and skills from the lesson before it. Your pet learns with you."); intro.setTextSize(15); intro.setGravity(Gravity.CENTER); root.addView(intro);
        TextView count=new TextView(a); count.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" cumulative lessons • Mastered "+mastered); count.setGravity(Gravity.CENTER); root.addView(count);

        Spinner moduleSpinner=new Spinner(a);
        String[] moduleNames=new String[PypetCurriculum.MODULES.size()];
        for(int i=0;i<moduleNames.length;i++) moduleNames[i]=PypetCurriculum.MODULES.get(i).title;
        moduleSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,moduleNames)); root.addView(moduleSpinner);
        Spinner lessonSpinner=new Spinner(a); root.addView(lessonSpinner);
        TextView lock=new TextView(a); lock.setTextSize(13); lock.setTextColor(Color.DKGRAY); root.addView(lock);
        TextView mission=new TextView(a); mission.setTextSize(16); mission.setPadding(6,8,6,8); root.addView(mission);
        EditText code=new EditText(a); code.setGravity(android.view.Gravity.TOP|android.view.Gravity.START); code.setHint("Continue your Python project here..."); code.setMinLines(7); root.addView(code);
        TextView result=new TextView(a); result.setTextSize(15); root.addView(result);
        LinearLayout buttons=new LinearLayout(a); Button run=new Button(a); run.setText("▶ RUN"); Button check=new Button(a); check.setText("✓ CHECK"); Button hint=new Button(a); hint.setText("💡 HINT"); buttons.addView(run); buttons.addView(check); buttons.addView(hint); root.addView(buttons);

        final int currentModuleIndex=findModuleForLesson(current==null?null:current.id);
        if(currentModuleIndex>=0) moduleSpinner.setSelection(currentModuleIndex);
        final boolean[] refreshing={false};

        Runnable refreshLessons=()->{
            int moduleIndex=moduleSpinner.getSelectedItemPosition();
            PypetCurriculum.Module m=PypetCurriculum.MODULES.get(moduleIndex);
            java.util.ArrayList<String> names=new java.util.ArrayList<>();
            java.util.ArrayList<PypetCurriculum.Lesson> allowed=new java.util.ArrayList<>();
            for(PypetCurriculum.Lesson l:m.lessons){
                int index=PypetCurriculum.indexOf(l.id);
                if(index<=mastered){ names.add((index<mastered?"✓ ":"▶ ")+l.title); allowed.add(l); }
            }
            if(allowed.isEmpty()){ names.add("🔒 Future lessons unlock sequentially"); }
            lessonSpinner.setAdapter(new ArrayAdapter<String>(a,android.R.layout.simple_spinner_dropdown_item,names));
            if(moduleIndex==currentModuleIndex && current!=null){int pos=0;for(int i=0;i<allowed.size();i++)if(allowed.get(i).id.equals(current.id))pos=i;lessonSpinner.setSelection(pos);}
            if(current!=null) lock.setText("🔐 Current lesson: "+current.title+" • Previous lessons are reviewable; future lessons unlock only after mastery.");
        };
        moduleSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){if(!refreshing[0])refreshLessons.run();}public void onNothingSelected(android.widget.AdapterView<?> p){}});
        refreshLessons.run();

        Runnable showLesson=()->{
            int moduleIndex=moduleSpinner.getSelectedItemPosition();
            PypetCurriculum.Module m=PypetCurriculum.MODULES.get(moduleIndex);
            int selected=lessonSpinner.getSelectedItemPosition();
            int masteredNow=PetEvolutionManager.lessons(a);
            java.util.ArrayList<PypetCurriculum.Lesson> allowed=new java.util.ArrayList<>();
            for(PypetCurriculum.Lesson l:m.lessons) if(PypetCurriculum.indexOf(l.id)<=masteredNow) allowed.add(l);
            if(allowed.isEmpty()||selected>=allowed.size()) return;
            PypetCurriculum.Lesson l=allowed.get(selected);
            int index=PypetCurriculum.indexOf(l.id);
            mission.setText("🎯 Skill: "+l.skill+"\n\nHands-on mission: "+l.task+"\n\nThis builds on the previous lesson's project. Mastery comes from doing, not reading.");
            code.setText(a.getSharedPreferences(PREFS,0).getString(CODE,""));
            if(index<masteredNow) lock.setText("📖 Review mode • This lesson is mastered. Your current lesson remains locked until you finish the next step.");
            else lock.setText("▶ CURRENT • Finish all lesson stages to unlock the next lesson.");
        };
        lessonSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener(){public void onItemSelected(android.widget.AdapterView<?> p,android.view.View v,int pos,long id){showLesson.run();}public void onNothingSelected(android.widget.AdapterView<?> p){}});

        run.setOnClickListener(v->{
            String source=code.getText().toString().trim();
            if(source.isEmpty()){result.setText("✏️ Write your solution first. Your project code carries forward between lessons.");return;}
            a.getSharedPreferences(PREFS,0).edit().putString(CODE,source).apply();
            try{
                PyObject py=Python.getInstance().getModule("pypet_engine"); String output=py.callAttr("run_lesson",source).toString(); result.setText("🧪 Result\n"+output);
                if(output.contains("'ok': True")&&output.contains("'passed': True")){
                    int now=PetEvolutionManager.lessons(a);
                    PypetCurriculum.Lesson active=PypetCurriculum.currentLesson(now);
                    int selectedIndex=currentSelectedIndex(moduleSpinner,lessonSpinner,now);
                    if(active!=null&&selectedIndex==PypetCurriculum.indexOf(active.id)){
                        PetEvolutionManager.completeLesson(a);
                        int after=PetEvolutionManager.lessons(a);
                        PypetCurriculum.Lesson next=PypetCurriculum.currentLesson(after);
                        String nextText=next==null?"You completed the full pathway!":"Next: "+next.title;
                        new AlertDialog.Builder(a).setTitle("🎓 Mastery achieved!").setMessage("You demonstrated the skill.\n\n🐾 "+PetEvolutionManager.name(a)+" learned it with you.\n🧠 Mastered: "+after+" / "+PypetCurriculum.lessonCount()+"\n\n"+nextText).setPositiveButton("Continue",null).show();
                        count.setText("🐍 "+PypetCurriculum.MODULES.size()+" modules • "+PypetCurriculum.lessonCount()+" cumulative lessons • Mastered "+after);
                        refreshLessons.run(); showLesson.run();
                    } else result.append("\n\n📖 Review passed. Return to the current lesson to advance the course.");
                } else result.append("\n\n💡 Not mastered yet. Inspect the result, debug your code and run it again.");
            }catch(Exception e){result.setText("Keep experimenting.\n"+e.getMessage());}
        });
        check.setOnClickListener(v->new AlertDialog.Builder(a).setTitle("✓ Check your thinking").setMessage("Before running: predict the output. After running: explain why you got it. Then improve the same project.\n\nThe goal is understanding, not guessing.").setPositiveButton("Continue coding",null).show());
        hint.setOnClickListener(v->{int mi=moduleSpinner.getSelectedItemPosition();PypetCurriculum.Module m=PypetCurriculum.MODULES.get(mi);int si=lessonSpinner.getSelectedItemPosition();java.util.ArrayList<PypetCurriculum.Lesson> allowed=new java.util.ArrayList<>();for(PypetCurriculum.Lesson l:m.lessons)if(PypetCurriculum.indexOf(l.id)<=PetEvolutionManager.lessons(a))allowed.add(l);if(si<allowed.size()){PypetCurriculum.Lesson l=allowed.get(si);new AlertDialog.Builder(a).setTitle("💡 Hint").setMessage("Current skill: "+l.skill+"\n\nBreak the mission into the smallest working step. Inspect intermediate values, change one thing at a time, and keep your previous working code.").setPositiveButton("Got it",null).show();}});
        TextView career=new TextView(a); career.setText("\n📚 The pathway covers Python fundamentals and full practical library work, OOP, testing, APIs, SQL, Tkinter, Pygame, coordinates/resolution, graphics, 3D, animation, Git/GitHub, portfolio work and job readiness.\n\n🐾 Your demonstrated learning is the pet's learning and unlocks World development."); career.setTextSize(15); root.addView(career);
        new AlertDialog.Builder(a).setView(root).setNegativeButton("Return to World",null).show();
    }

    private static int findModuleForLesson(String id){if(id==null)return 0;for(int i=0;i<PypetCurriculum.MODULES.size();i++)for(PypetCurriculum.Lesson l:PypetCurriculum.MODULES.get(i).lessons)if(id.equals(l.id))return i;return 0;}
    private static int currentSelectedIndex(Spinner moduleSpinner,Spinner lessonSpinner,int mastered){int mi=moduleSpinner.getSelectedItemPosition();if(mi<0)return -1;PypetCurriculum.Module m=PypetCurriculum.MODULES.get(mi);int si=lessonSpinner.getSelectedItemPosition();int n=0;for(PypetCurriculum.Lesson l:m.lessons){if(PypetCurriculum.indexOf(l.id)>mastered)break;if(n++==si)return PypetCurriculum.indexOf(l.id);}return -1;}
}
