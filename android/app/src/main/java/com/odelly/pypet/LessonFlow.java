package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Player-facing lesson progression. Every core lesson moves from understanding
 * to independent application instead of being a passive reading screen.
 */
public final class LessonFlow {
    public enum Stage {
        LEARN("Learn", "Understand the concept with a short explanation and example."),
        SEE("See", "Watch the concept work in the pet/world with a concrete example."),
        WRITE("Write", "Write or edit the Python code yourself."),
        RUN("Run", "Execute your code and observe the result."),
        INSPECT("Inspect", "Read the output and compare it with the expected behavior."),
        DEBUG("Debug", "Find and fix a deliberate or naturally occurring mistake."),
        PRACTICE("Practice", "Solve a second guided problem with less help."),
        CHALLENGE("Challenge", "Solve a new problem independently."),
        APPLY("Apply", "Use the concept in the Pypet World, pet, or a project."),
        REFLECT("Reflect", "Explain what you learned and why the solution works."),
        MASTER("Master", "Pass the mastery check to unlock progression and pet learning.");

        public final String title;
        public final String instruction;
        Stage(String title, String instruction) { this.title = title; this.instruction = instruction; }
    }

    private static final List<Stage> CORE_STAGES = Collections.unmodifiableList(Arrays.asList(
            Stage.LEARN, Stage.SEE, Stage.WRITE, Stage.RUN, Stage.INSPECT,
            Stage.DEBUG, Stage.PRACTICE, Stage.CHALLENGE, Stage.APPLY,
            Stage.REFLECT, Stage.MASTER));

    private final String lessonId;
    private int stageIndex;
    private boolean completed;
    private int attempts;

    public LessonFlow(String lessonId) {
        if (lessonId == null || lessonId.trim().isEmpty()) throw new IllegalArgumentException("lessonId required");
        this.lessonId = lessonId;
    }

    public String lessonId() { return lessonId; }
    public Stage stage() { return CORE_STAGES.get(stageIndex); }
    public int stageNumber() { return stageIndex + 1; }
    public int stageCount() { return CORE_STAGES.size(); }
    public int attempts() { return attempts; }
    public boolean isComplete() { return completed; }
    public List<Stage> stages() { return CORE_STAGES; }

    /** Record an attempt. Failed attempts keep the learner in the stage so they can debug and retry. */
    public boolean submit(boolean correct) {
        attempts++;
        if (!correct) return false;
        if (stageIndex == CORE_STAGES.size() - 1) {
            completed = true;
            return true;
        }
        stageIndex++;
        return true;
    }

    public void retryCurrentStage() { /* Intentional no-op: learner keeps the same stage and tries again. */ }
}
