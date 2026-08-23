package com.odelly.pypet;

import android.content.Context;
import android.content.SharedPreferences;

/** Persists the active hands-on stage for each cumulative lesson. */
public final class AcademyLessonSession {
    public enum Stage { LEARN, SEE, WRITE, RUN, INSPECT, DEBUG, PRACTICE, CHALLENGE, APPLY, REFLECT, MASTER }
    private static final String PREFS="pypet_academy_stages";
    private final SharedPreferences prefs;
    private final String lessonId;
    private boolean ran;
    private boolean inspected;

    public AcademyLessonSession(Context context,String lessonId){
        this.prefs=context.getSharedPreferences(PREFS,Context.MODE_PRIVATE);
        this.lessonId=lessonId;
        this.ran=prefs.getBoolean(lessonId+".ran",false);
        this.inspected=prefs.getBoolean(lessonId+".inspected",false);
    }
    public Stage stage(){int i=prefs.getInt(lessonId+".stage",0);return Stage.values()[Math.max(0,Math.min(i,Stage.values().length-1))];}
    public String stageLabel(){switch(stage()){
        case LEARN:return "Learn"; case SEE:return "See"; case WRITE:return "Write"; case RUN:return "Run"; case INSPECT:return "Inspect"; case DEBUG:return "Debug"; case PRACTICE:return "Practice"; case CHALLENGE:return "Challenge"; case APPLY:return "Apply"; case REFLECT:return "Reflect"; default:return "Master";}}
    public String instruction(){switch(stage()){
        case LEARN:return "Understand the concept and why it matters.";
        case SEE:return "Watch a working example and predict what it will do.";
        case WRITE:return "Write the Python yourself. Keep the previous project code.";
        case RUN:return "Run your code and observe the actual result.";
        case INSPECT:return "Inspect the output and explain what happened.";
        case DEBUG:return "Find a mistake, change the code, and run it again.";
        case PRACTICE:return "Solve a second guided problem with less help.";
        case CHALLENGE:return "Solve a new problem independently.";
        case APPLY:return "Apply the skill to your pet, World, or growing project.";
        case REFLECT:return "Explain what you learned and why the solution works.";
        default:return "Pass the mastery check to unlock the next lesson.";}}
    public void recordRun(boolean passed){ran=true;prefs.edit().putBoolean(lessonId+".ran",true).apply();if(passed)inspected=true;prefs.edit().putBoolean(lessonId+".inspected",inspected).apply();}
    public boolean canAdvance(){Stage s=stage();if(s==Stage.WRITE)return ran; if(s==Stage.RUN)return ran; if(s==Stage.INSPECT||s==Stage.DEBUG||s==Stage.PRACTICE||s==Stage.CHALLENGE||s==Stage.MASTER)return ran; return true;}
    public boolean advance(){if(stage()==Stage.MASTER||!canAdvance())return false;int next=stage().ordinal()+1;prefs.edit().putInt(lessonId+".stage",next).apply();return true;}
    public boolean readyToMaster(){return stage()==Stage.MASTER&&ran&&inspected;}
}
