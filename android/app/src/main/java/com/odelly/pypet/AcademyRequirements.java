package com.odelly.pypet;

import java.util.*;

/**
 * Required in-world school progression. Learning/practice is also pet learning.
 * Milestones provide frequent motivation without replacing mastery requirements.
 */
public final class AcademyRequirements {
    public static final class Milestone {
        public final int number;
        public final String title;
        public final String reward;
        public final int trophyValue;
        public Milestone(int number, String title, String reward, int trophyValue) {
            this.number = number; this.title = title; this.reward = reward; this.trophyValue = trophyValue;
        }
    }

    // More, smaller milestones make a very broad mastery course feel achievable.
    public static final List<Milestone> MILESTONES = Collections.unmodifiableList(Arrays.asList(
        new Milestone(1, "First Steps", "Starter trophy + 100 coins", 1),
        new Milestone(2, "Python Explorer", "Explorer badge + 150 coins", 1),
        new Milestone(3, "Logic Builder", "Logic trophy + 200 coins", 1),
        new Milestone(4, "Function Crafter", "Function trophy + 250 coins", 1),
        new Milestone(5, "Data Keeper", "Data badge + 300 coins", 1),
        new Milestone(6, "Standard Library Scout", "Library badge + 350 coins", 1),
        new Milestone(7, "Python Engineer", "Engineer trophy + 400 coins", 1),
        new Milestone(8, "Application Builder", "Builder trophy + 500 coins", 1),
        new Milestone(9, "GUI Creator", "GUI trophy + 550 coins", 1),
        new Milestone(10, "Game Developer", "Game-dev trophy + 650 coins", 1),
        new Milestone(11, "Graphics Apprentice", "Graphics badge + 700 coins", 1),
        new Milestone(12, "3D Apprentice", "3D trophy + 800 coins", 1),
        new Milestone(13, "Systems Builder", "Systems badge + 900 coins", 1),
        new Milestone(14, "Professional Developer", "Professional trophy + 1000 coins", 1),
        new Milestone(15, "Job Ready", "Career trophy + 1500 coins", 2),
        new Milestone(16, "Python Master", "Master trophy + rare pet-lineage chance", 3)
    ));

    /** School is a required progression pillar after the player enters the World. */
    public static boolean schoolRequired() { return true; }

    /** Learning and practice both count as pet learning; mastery is still required to unlock the next lesson. */
    public static int petLearningXp(int lessonXp, boolean practice) {
        if (lessonXp <= 0) return 0;
        return practice ? Math.max(1, lessonXp / 2) : lessonXp;
    }

    /** Returns the milestone for cumulative mastered lessons. */
    public static Milestone milestoneFor(int masteredLessons) {
        if (masteredLessons <= 0) return null;
        int index = Math.min(MILESTONES.size() - 1, (masteredLessons - 1) * MILESTONES.size() / 16);
        return MILESTONES.get(index);
    }

    /** Progression gate: a learner may review freely but cannot bypass required mastery. */
    public static boolean canStartLesson(int lessonIndex, int masteredLessons) {
        return lessonIndex <= masteredLessons;
    }

    private AcademyRequirements() {}
}
