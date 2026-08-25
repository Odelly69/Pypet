package com.odelly.pypet;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.*;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;

/** Every non-Academy building teaches a small Python skill before its activity. */
public final class BuildingLearningGateView {
    private BuildingLearningGateView() {}
    private static final class Lesson { final String building,skill,title,teaching,example,mission,hint; Lesson(String building,String skill,String title,String teaching,String example,String mission,String hint){this.building=building;this.skill=skill;this.title=title;this.teaching=teaching;this.example=example;this.mission=mission;this.hint=hint;} }
    public static void show(Activity a,String building){
        Lesson l=lessonFor(building);LinearLayout root=new LinearLayout(a);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,18,22,18);ScrollView scroll=new ScrollView(a);scroll.addView(root);
        TextView title=text(a,l.building+" • LEARN BY DOING",25,true);title.setGravity(Gravity.CENTER);root.addView(title);
        TextView subtitle=text(a,"🐍 Python is taught here before the building activity. No prior Python knowledge is required.",16,false);subtitle.setPadding(0,10,0,16);root.addView(subtitle);
        TextView lesson=text(a,"📘 "+l.title+"\n\n"+l.teaching,17,false);lesson.setPadding(18,16,18,16);lesson.setBackgroundColor(Color.rgb(241,246,235));root.addView(lesson);
        TextView example=text(a,"WORKED EXAMPLE\n\n"+l.example+"\n\nNow change it yourself for the mission.",16,false);example.setTypeface(Typeface.MONOSPACE);example.setPadding(18,14,18,14);root.addView(example);
        TextView mission=text(a,"🎯 YOUR MISSION\n"+l.mission,17,true);mission.setPadding(0,16,0,12);root.addView(mission);
        EditText code=new EditText(a);code.setHint("Write Python here...");code.setMinLines(8);code.setMinHeight(360);code.setGravity(Gravity.TOP|Gravity.START);code.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE|InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);root.addView(code,new LinearLayout.LayoutParams(-1,-2));
        TextView result=text(a,"",15,false);result.setPadding(0,10,0,10);root.addView(result);
        Button run=button(a,"▶ RUN REAL PYTHON");root.addView(run);Button hint=button(a,"💡 SHOW HINT");root.addView(hint);Button enter=button(a,"🔒 PASS THE LESSON TO ENTER "+l.building);enter.setEnabled(false);root.addView(enter);
        code.setText(a.getSharedPreferences("pypet_building_lessons",0).getString(l.building,""));
        run.setOnClickListener(v->{String source=code.getText().toString().trim();if(source.isEmpty()){result.setText("✏️ Write the code yourself. Read the teaching card and worked example first.");return;}a.getSharedPreferences("pypet_building_lessons",0).edit().putString(l.building,source).apply();try{PyObject py=Python.getInstance().getModule("pypet_engine");String output=py.callAttr("run_lesson",source).toString();boolean executed=output.contains("'ok': True");boolean passed=executed&&missionComplete(l.skill,source);if(passed){result.setText("✅ Lesson passed!\n\nREAL PYTHON RESULT:\n"+output+"\n\nYou demonstrated the skill. Now enter the building activity.");enter.setEnabled(true);RewardInventory.completeTask(a,"building_lesson_"+l.building,3);}else result.setText("🧪 The code ran, but the mission is not complete yet.\n\nUse the teaching card and hint, change one thing, then run it again.\n\n"+output);}catch(Exception e){result.setText("🧪 Python execution error\n"+String.valueOf(e.getMessage())+"\n\nFix the code and run it again.");}});
        hint.setOnClickListener(v->result.setText("💡 HINT\n"+l.hint));enter.setOnClickListener(v->openActivity(a,l.building));Button back=button(a,"🌎 Return to PyPet World");back.setOnClickListener(v->LivingWorldView.show(a));root.addView(back);a.setContentView(scroll);
    }
    private static void openActivity(Activity a,String building){switch(building){case "HOME":BuildingActivityView.home(a);break;case "MARKET":BuildingActivityView.market(a);break;case "WORKSHOP":BuildingActivityView.workshop(a);break;case "PARK":BuildingActivityView.park(a);break;case "LIBRARY":BuildingActivityView.library(a);break;default:LivingWorldView.show(a);break;}}
    private static Lesson lessonFor(String b){
        if("HOME".equals(b))return new Lesson(b,"variables","Variables: give your pet data","A variable is a name that refers to a value. Use name = value. Python keeps track of the value's type for you.","pet_name = 'Pip'\nhappiness = 80\nprint(pet_name, happiness)","Create a pet_name variable and a happiness variable, then print both.","Use pet_name = 'Pip' and happiness = 100, followed by print(...).");
        if("MARKET".equals(b))return new Lesson(b,"arithmetic","Arithmetic: calculate a purchase","Python uses +, -, *, /, //, %, and ** for calculations. Put a calculation in a variable when you want to reuse it.","food = 3\ncoins_per_food = 2\ntotal = food * coins_per_food\nprint(total)","Calculate a total cost using multiplication or addition and print the result.","Create two numbers, combine them with * or +, store the answer, then print it.");
        if("WORKSHOP".equals(b))return new Lesson(b,"functions","Functions: build a reusable tool","def starts a function. Parameters receive inputs. return sends a result back to the caller.","def feed_pet(food):\n    return 'Fed ' + food\nprint(feed_pet('apple'))","Define a function with def, accept a parameter, return a value, and call the function.","Start with def feed_pet(food):, indent the return, then call feed_pet(...).");
        if("PARK".equals(b))return new Lesson(b,"loops","Loops: care for every item","A for loop repeats once for each item. The indented block is the action repeated for each item.","for food in ['apple', 'berry']:\n    print(food)","Write a for loop that processes every item in a list and prints each item.","Use for item in ['apple', 'berry']:, then indent print(item).");
        return new Lesson(b,"collections","Collections: organize a pet profile","Lists hold ordered items. Dictionaries map keys to values. Sets keep unique values. Pick the structure that matches the job.","pet = {'name': 'Pip', 'level': 1}\nprint(pet['name'])","Create a list or dictionary containing pet information and print a value from it.","Try pet = {'name': 'Pip'} and print(pet['name']), or make a list with square brackets.");
    }
    private static boolean missionComplete(String skill,String source){String s=source.toLowerCase();if("variables".equals(skill))return s.contains("=")&&s.contains("print");if("arithmetic".equals(skill))return s.contains("=")&&(s.contains("+")||s.contains("-")||s.contains("*")||s.contains("/")||s.contains("%"));if("functions".equals(skill))return s.contains("def ")&&s.contains("return")&&s.contains("(");if("loops".equals(skill))return(s.contains("for ")||s.contains("while "))&&s.contains("print");if("collections".equals(skill))return(s.contains("[")||s.contains("{"))&&s.contains("print");return false;}
    private static TextView text(Activity a,String value,float size,boolean bold){TextView v=new TextView(a);v.setText(value);v.setTextSize(size);if(bold)v.setTypeface(Typeface.DEFAULT,Typeface.BOLD);v.setTextColor(Color.rgb(39,53,42));return v;}
    private static Button button(Activity a,String value){Button b=new Button(a);b.setText(value);b.setAllCaps(false);return b;}
}
