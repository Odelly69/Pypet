package com.odelly.pypet;

import java.util.*;

/**
 * Animation choreography definitions for playground equipment. Rendering code
 * can consume these keys without inventing unsafe rapid/flashing effects.
 */
public final class PlaygroundAnimationLibrary {
    public static final class Sequence {
        public final String key;
        public final String[] phases;
        public final boolean flashing;
        public final boolean rapidMotion;
        Sequence(String k, boolean f, boolean r, String... p){key=k;flashing=f;rapidMotion=r;phases=p;}
    }

    private static final Map<String,Sequence> SEQUENCES = new HashMap<>();
    static {
        put("sit_idle_social", "approach","sit","idle","look","stand");
        put("spring_rock_cycle", "approach","mount","rock_forward","rock_back","pause","dismount");
        put("chain_swing_arc", "approach","mount","swing_forward","swing_back","slow","dismount");
        put("rigid_arm_pump_swing", "approach","grip","pump_forward","pump_back","coast","slow","dismount");
        put("climb_slide_land", "walk","climb","sit","slide","land","celebrate");
        put("seesaw_counterweight", "approach","mount","tilt_down","tilt_up","balance","dismount");
        put("spinner_slow_rotation", "approach","mount","slow_rotate","pause","slow_rotate","dismount");
        put("carousel_slow_rotation", "approach","mount","slow_rotate","pause","slow_rotate","dismount");
        put("climb_route_pause", "approach","grip","climb","look","climb","descend");
        put("hand_over_hand_traverse", "approach","grip","reach","pull","pause","reach","dismount");
        put("tower_climb_bridge_slide", "climb","bridge","look","slide","land");
        put("balance_step_discover", "approach","step","balance","look","discover","leave");
        put("canopy_swing_arc", "approach","mount","swing_forward","swing_back","slow","dismount");
        put("castle_climb_slide_land", "climb","look","slide","land","celebrate");
        put("carousel_slow_rotation", "approach","mount","slow_rotate","pause","dismount");
        put("star_spinner", "approach","mount","slow_rotate","pause","dismount");
        put("crawl_through_discover", "approach","enter","crawl","look","discover","exit");
        put("sit_story_gesture", "approach","sit","gesture","listen","stand");
        put("climb_pattern_pause", "approach","climb","pause","choose_route","climb","descend");
        put("spring_rock_mechanical", "approach","mount","rock_forward","rock_back","pause","dismount");
    }

    private static void put(String key,String... phases){SEQUENCES.put(key,new Sequence(key,false,false,phases));}
    public static Sequence get(String key){return SEQUENCES.get(key);}
    public static boolean isSafe(String key){Sequence s=get(key);return s==null || (!s.flashing && !s.rapidMotion);}
    public static Set<String> keys(){return Collections.unmodifiableSet(SEQUENCES.keySet());}
    private PlaygroundAnimationLibrary(){}
}
