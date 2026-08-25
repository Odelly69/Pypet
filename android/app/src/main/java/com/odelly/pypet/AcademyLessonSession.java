package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

/** Persists the active hands-on stage for each cumulative lesson and gates progression on demonstrated work. */
public final class AcademyLessonSession {
    public enum Stage { LEARN, SEE, WRITE, RUN, INSPECT, DEBUG, PRACTICE, CHALLENGE, APPLY, REFLECT, MASTER }
    private static final String PREFS="pypet_academy_stages";
    private final SharedPreferences prefs;
    private final String lessonId;

    public AcademyLessonSession(Context context,String lessonId){prefs=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);this.lessonId=lessonId;}
    public Stage stage(){int i=prefs.getInt(lessonId+".stage",0);return Stage.values()[Math.max(0,Math.min(i,Stage.values().length-1))];}
    public String stageLabel(){switch(stage()){
        case LEARN:return "Learn";case SEE:return "See";case WRITE:return "Write";case RUN:return "Run";case INSPECT:return "Inspect";case DEBUG:return "Debug";case PRACTICE:return "Practice";case CHALLENGE:return "Challenge";case APPLY:return "Apply";case REFLECT:return "Reflect";default:return "Master";}}
    public String instruction(){switch(stage()){
        case LEARN:return "Learn the idea first. No Python knowledge is assumed.";
        case SEE:return "See a working example and predict what it does.";
        case WRITE:return "Write the Python yourself. Your code is saved as you progress.";
        case RUN:return "Run your code and observe the real result.";
        case INSPECT:return "Inspect the output and decide whether it matches the goal.";
        case DEBUG:return "Find a mistake, change one thing, and run it again.";
        case PRACTICE:return "Solve a second guided problem with less help.";
        case CHALLENGE:return "Solve a new problem independently.";
        case APPLY:return "Apply the skill to your pet, World, or growing project.";
        case REFLECT:return "Explain what you learned and why the solution works.";
        default:return "Pass the mastery check to unlock the next lesson.";}}
    public void markLearned(){prefs.edit().putBoolean(lessonId+".learned",true).apply();}
    public void markSeen(){prefs.edit().putBoolean(lessonId+".seen",true).apply();}
    public void recordRun(boolean passed){String key=lessonId+"."+stage().name()+".passed";prefs.edit().putBoolean(lessonId+".ran",true).putBoolean(key,passed).apply();}
    public void markInspected(){prefs.edit().putBoolean(lessonId+".inspected",true).apply();}
    public void markReflected(){prefs.edit().putBoolean(lessonId+".reflected",true).apply();}
    private boolean stagePassed(){return prefs.getBoolean(lessonId+"."+stage().name()+".passed",false);}
    public boolean canAdvance(){switch(stage()){
        case LEARN:return prefs.getBoolean(lessonId+".learned",false);
        case SEE:return prefs.getBoolean(lessonId+".seen",false);
        case WRITE:return stagePassed();
        case RUN:return stagePassed();
        case INSPECT:return prefs.getBoolean(lessonId+".inspected",false);
        case DEBUG:return stagePassed();
        case PRACTICE:return stagePassed();
        case CHALLENGE:return stagePassed();
        case APPLY:return stagePassed();
        case REFLECT:return prefs.getBoolean(lessonId+".reflected",false);
        default:return prefs.getBoolean(lessonId+".WRITE.passed",false)&&prefs.getBoolean(lessonId+".MASTER.passed",false)&&prefs.getBoolean(lessonId+".inspected",false);
    }}
    public boolean advance(){if(stage()==Stage.MASTER||!canAdvance())return false;int next=stage().ordinal()+1;prefs.edit().putInt(lessonId+".stage",next).apply();return true;}
    public boolean readyToMaster(){return stage()==Stage.MASTER&&prefs.getBoolean(lessonId+".MASTER.passed",false)&&prefs.getBoolean(lessonId+".inspected",false);}
}
