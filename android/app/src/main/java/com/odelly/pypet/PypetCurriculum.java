package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Full-spectrum Python-to-job-readiness curriculum used by the in-world Academy. */
public final class PypetCurriculum {
    public static final class Lesson {
        public final String id, title, skill, task;
        public Lesson(String id, String title, String skill, String task) { this.id=id; this.title=title; this.skill=skill; this.task=task; }
    }
    public static final class Module {
        public final String title, description;
        public final List<Lesson> lessons;
        Module(String title, String description, Lesson... lessons) { this.title=title; this.description=description; this.lessons=Collections.unmodifiableList(Arrays.asList(lessons)); }
    }
    private static Lesson l(String id,String title,String skill,String task){return new Lesson(id,title,skill,task);}
    public static final List<Module> MODULES = Collections.unmodifiableList(Arrays.asList(
        new Module("01 • Foundations","Learn Python by changing real things in the World.",
            l("f01","Python & the REPL","syntax","Print a greeting for your pet."),l("f02","Variables & types","variables","Store your pet's name, age and happiness."),l("f03","Numbers & operators","arithmetic","Calculate food and coin totals."),l("f04","Strings","strings","Format a personalized pet message."),l("f05","Booleans & logic","logic","Decide whether your pet needs care."),l("f06","Input & output","io","Ask the player for a town name."),l("f07","if / elif / else","conditionals","Choose an action from pet needs."),l("f08","for loops","loops","Process every item in a supply list."),l("f09","while loops","loops","Run a safe care loop until a need is satisfied.")),
        new Module("02 • Core Python","Build reusable programs and work with collections.",
            l("c01","Functions","functions","Write a feed_pet function with parameters and a return value."),l("c02","Lists & tuples","collections","Track a party of pets."),l("c03","Dictionaries & sets","collections","Build a pet profile and remove duplicate skills."),l("c04","Comprehensions","collections","Create a filtered list of healthy pets."),l("c05","Exceptions","errors","Handle invalid player input without crashing."),l("c06","Files","files","Save and reload a town note."),l("c07","Modules & imports","modules","Split a pet utility into an importable module."),l("c08","Packages & environments","packaging","Create a small package with a clean entry point.")),
        new Module("03 • Standard Library","Learn the practical Python library used in real work.",
            l("s01","pathlib","stdlib","Find and create files safely."),l("s02","json","stdlib","Save a pet profile as JSON."),l("s03","csv","stdlib","Export a pet-care report."),l("s04","datetime","stdlib","Calculate streak dates."),l("s05","math & statistics","stdlib","Analyze training results."),l("s06","random","stdlib","Create fair non-cheating game events."),l("s07","re","stdlib","Validate structured text."),l("s08","collections","stdlib","Use Counter, defaultdict and deque."),l("s09","itertools & functools","stdlib","Compose reusable data pipelines."),l("s10","logging","stdlib","Add useful application diagnostics.")),
        new Module("04 • Professional Python","Write maintainable software.",
            l("p01","Classes & objects","oop","Model a Pet class with state and behavior."),l("p02","Inheritance & composition","oop","Build species-specific behavior without duplication."),l("p03","Dataclasses","oop","Represent a typed pet record."),l("p04","Decorators","advanced","Add timing or validation around a function."),l("p05","Generators","advanced","Stream a large training dataset."),l("p06","Context managers","advanced","Safely manage a resource."),l("p07","Type hints","typing","Annotate a real module."),l("p08","Testing","testing","Write unit tests for pet-care rules."),l("p09","Debugging & profiling","debugging","Find and fix a deliberate bug and slow path."),l("p10","Packaging & dependency management","packaging","Prepare a professional installable project.")),
        new Module("05 • Data, Web & Systems","Build applications that communicate and persist data.",
            l("w01","HTTP & APIs","web","Call a safe sample API and parse JSON."),l("w02","SQLite & SQL","database","Create and query a pet-care database."),l("w03","Data processing","data","Transform and summarize records."),l("w04","CLI applications","cli","Build a useful command-line pet tool."),l("w05","Concurrency","systems","Compare threads, processes and async tasks."),l("w06","asyncio","systems","Run asynchronous work safely."),l("w07","Security fundamentals","security","Validate input and protect secrets.")),
        new Module("06 • Tkinter & GUI","Create desktop interfaces with events and state.",
            l("t01","Tkinter windows","tkinter","Build a working pet-care window."),l("t02","Widgets & layout","tkinter","Arrange labels, buttons, entries and frames."),l("t03","Events & callbacks","tkinter","Connect UI actions to pet behavior."),l("t04","Forms & validation","tkinter","Build a validated profile form."),l("t05","GUI project","tkinter","Ship a small multi-screen desktop app.")),
        new Module("07 • Pygame & 2D Graphics","Learn game loops, input, collision, sound and animation.",
            l("g01","Game loop","pygame","Build an update/draw loop."),l("g02","Coordinates & resolutions","graphics","Map points correctly at multiple resolutions and aspect ratios."),l("g03","Sprites","pygame","Create and move a player sprite."),l("g04","Input","pygame","Handle keyboard/controller input."),l("g05","Collision","pygame","Implement safe collision detection."),l("g06","Animation","pygame","Build an idle and walking animation."),l("g07","Sound","pygame","Trigger toggleable SFX and music."),l("g08","Camera & scaling","graphics","Build a camera that follows a pet."),l("g09","2D capstone","pygame","Ship a small playable game.")),
        new Module("08 • 3D Graphics & Animation","Understand the math and design behind immersive worlds.",
            l("d01","3D coordinates","3d","Map x/y/z positions and camera space."),l("d02","Vectors","3d","Move a character using vectors."),l("d03","Transforms","3d","Translate, rotate and scale an object."),l("d04","Meshes","3d","Understand vertices, edges and faces."),l("d05","Materials & lighting","3d","Create readable surfaces and lighting."),l("d06","Camera systems","3d","Build perspective and follow-camera behavior."),l("d07","3D animation","3d","Create a simple character animation state machine."),l("d08","Optimization","3d","Measure and improve frame performance."),l("d09","3D capstone","3d","Build and present a small interactive scene.")),
        new Module("09 • Job Readiness & Mastery","Turn skills into demonstrable professional ability.",
            l("j01","Git & GitHub","career","Create clean commits and a useful README."),l("j02","Code review","career","Review a change and give actionable feedback."),l("j03","Documentation","career","Document an API and setup process."),l("j04","Architecture","career","Design modules with clear responsibilities."),l("j05","Portfolio project","career","Build a complete Python application."),l("j06","Technical interview","career","Solve and explain coding problems."),l("j07","Debugging interview","career","Diagnose a broken program methodically."),l("j08","Presentation","career","Explain your project and design decisions."),l("j09","Master capstone","career","Plan, build, test and present a professional project."))
    ));
    private PypetCurriculum() {}
    public static int lessonCount(){int n=0;for(Module m:MODULES)n+=m.lessons.size();return n;}
}