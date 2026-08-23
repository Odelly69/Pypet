package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Optional, non-transferable world decorations unlocked through rewarded ads. */
public final class RewardCatalog {
    public static final class Item {
        public final String id;
        public final String name;
        public final String description;

        public Item(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("aurora_tree", "Aurora Tree", "A calm fantasy tree for any outdoor area."),
        new Item("rainbow_arch", "Rainbow Garden Arch", "A colorful garden entrance with gentle motion."),
        new Item("tiny_castle", "Tiny Castle", "A miniature castle where pets can explore."),
        new Item("pet_carousel", "Pet Carousel", "A playful non-rideable decorative carousel."),
        new Item("ufo_pad", "UFO Landing Pad", "A whimsical landing pad for a tiny visitor."),
        new Item("wizard_workshop", "Wizard's Workshop", "A fantasy workshop for harmless pet experiments."),
        new Item("mini_train", "Miniature Train", "A decorative train that travels around a short track."),
        new Item("floating_island", "Floating Island", "A small floating garden island."),
        new Item("tiny_volcano", "Tiny Fantasy Volcano", "A non-hazardous fantasy volcano landmark."),
        new Item("unicorn_fountain", "Unicorn Fountain", "A peaceful unicorn-themed fountain."),
        new Item("dragon_roost", "Dragon-Roost Tower", "A cozy tower for dragon friends."),
        new Item("robot_station", "Robot Charging Station", "A friendly charging station for robot pets."),
        new Item("magical_flower_garden", "Magical Flower Garden", "A garden of fictional, non-flashing flowers."),
        new Item("floating_library", "Giant Floating Library", "A whimsical library for Python discoveries."),
        new Item("planetarium", "Planetarium", "A calm star-learning building."),
        new Item("artists_studio", "Artist's Studio", "A creative studio for decorating the world."),
        new Item("observatory", "Observatory", "A quiet place to explore the fictional sky."),
        new Item("campground", "Campground", "A cozy outdoor gathering area for pets."),
        new Item("rainbow_bridge", "Rainbow Bridge", "A decorative bridge for the Unicorn Sanctuary."),
        new Item("moon_garden", "Moon Garden", "A peaceful nighttime garden."),
        new Item("star_gazebo", "Star Gazebo", "A quiet gazebo for pet stargazing."),
        new Item("crystal_fountain", "Crystal Fountain", "A fantasy fountain for the Unicorn Sanctuary.")
    ));

    private RewardCatalog() {}

    public static List<Item> all() { return ITEMS; }

    public static Item byId(String id) {
        for (Item item : ITEMS) if (item.id.equals(id)) return item;
        return null;
    }

    public static Set<String> ids() {
        Set<String> ids = new HashSet<>();
        for (Item item : ITEMS) ids.add(item.id);
        return Collections.unmodifiableSet(ids);
    }
}
