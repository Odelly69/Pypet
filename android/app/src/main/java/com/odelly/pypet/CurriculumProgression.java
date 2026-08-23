package com.odelly.pypet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The Academy is a cumulative course: every lesson builds the learner's project
 * and requires mastery of the previous lesson before advancing.
 */
public final class CurriculumProgression {
    public static final class Lesson {
        public final String id, module, title;
        public final int number;
        Lesson(int number, String module, String title) {
            this.number = number; this.module = module; this.title = title;
            this.id = String.format("L%02d", number);
        }
    }

    private static final List<Lesson> LESSONS = buildLessons();
    private final Map<String, Boolean> mastered = new LinkedHashMap<>();
    private final Map<String, String> projectState = new LinkedHashMap<>();

    public CurriculumProgression() {
        for (Lesson lesson : LESSONS) mastered.put(lesson.id, false);
    }

    private static List<Lesson> buildLessons() {
        String[][] modules = {
            {"Foundations", "Setup and Python mindset", "Variables and values", "Types and conversion", "Strings", "Input and output", "Operators", "Booleans and comparisons", "Conditionals", "While loops", "For loops"},
            {"Core Python", "Functions", "Parameters and return values", "Scope", "Lists", "Tuples", "Dictionaries", "Sets", "Comprehensions", "Iteration", "Exceptions", "Files"},
            {"Standard Library", "Imports and modules", "pathlib", "json", "csv", "datetime", "math", "random", "re", "collections", "itertools", "functools", "logging"},
            {"Professional Python", "Object-oriented programming", "Classes and objects", "Inheritance", "Composition", "Dataclasses", "Type hints", "Decorators", "Generators", "Context managers", "Testing", "Debugging", "Profiling", "Packaging and virtual environments", "Dependencies and project structure"},
            {"Applications", "Command-line programs", "HTTP and APIs", "JSON APIs", "SQLite and SQL", "Data processing", "Automation", "Concurrency", "asyncio", "Security fundamentals"},
            {"Tkinter", "GUI fundamentals", "Widgets", "Layout and responsive UI", "Events and callbacks", "Forms and validation", "Build a desktop pet tool"},
            {"Pygame", "Game loop", "Input", "Sprites", "Collision", "Scenes and state", "Sound", "2D animation", "Build a playable pet game"},
            {"Graphics and 3D", "Screen coordinates", "Resolution independence", "Scaling and aspect ratios", "Vectors and transforms", "Animation systems", "Cameras", "3D coordinates", "Meshes and materials", "Lighting concepts", "3D animation", "Graphics optimization"},
            {"Job Readiness", "Git and GitHub workflow", "Code review", "Documentation", "Architecture", "Testing strategy", "Portfolio project", "Technical communication", "Interview coding", "Debugging interview", "Capstone planning", "Capstone implementation", "Capstone testing", "Capstone presentation"}
        };
        List<Lesson> out = new ArrayList<>();
        int n = 1;
        for (String[] module : modules) {
            for (int i = 1; i < module.length; i++) out.add(new Lesson(n++, module[0], module[i]));
        }
        return Collections.unmodifiableList(out);
    }

    public List<Lesson> lessons() { return LESSONS; }
    public int lessonCount() { return LESSONS.size(); }
    public Lesson currentLesson() {
        for (Lesson l : LESSONS) if (!isMastered(l.id)) return l;
        return LESSONS.get(LESSONS.size() - 1);
    }
    public boolean isMastered(String id) { return Boolean.TRUE.equals(mastered.get(id)); }
    public boolean canStart(String id) {
        int index = indexOf(id);
        return index == 0 || isMastered(LESSONS.get(index - 1).id);
    }
    public Lesson prerequisite(String id) {
        int index = indexOf(id);
        return index > 0 ? LESSONS.get(index - 1) : null;
    }
    public boolean master(String id) {
        if (!canStart(id)) return false;
        mastered.put(id, true);
        return true;
    }
    public int masteredCount() {
        int count = 0; for (Boolean value : mastered.values()) if (value) count++;
        return count;
    }
    public double completion() { return (double) masteredCount() / LESSONS.size(); }
    public void saveProjectValue(String key, String value) { if (key != null) projectState.put(key, value == null ? "" : value); }
    public String projectValue(String key) { return projectState.get(key); }
    public Map<String, String> projectState() { return Collections.unmodifiableMap(projectState); }

    private int indexOf(String id) {
        if (id == null) return -1;
        for (int i = 0; i < LESSONS.size(); i++) if (LESSONS.get(i).id.equals(id)) return i;
        return -1;
    }
}
