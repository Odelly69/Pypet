package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Exclusive optional world items. Prices are paid with ad-earned Pypet Coins. */
public final class RewardCatalog {
    public static final class Item {
        public final String id;
        public final String name;
        public final String description;
        public final int priceCoins;

        public Item(String id, String name, String description, int priceCoins) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.priceCoins = priceCoins;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("aurora_tree", "Aurora Tree", "A calm fantasy tree for any outdoor area.", 25),
        new Item("rainbow_arch", "Rainbow Garden Arch", "A colorful garden entrance with gentle motion.", 25),
        new Item("tiny_castle", "Tiny Castle", "A miniature castle where pets can explore.", 100),
        new Item("pet_carousel", "Pet Carousel", "A playful non-rideable decorative carousel.", 75),
        new Item("ufo_pad", "UFO Landing Pad", "A whimsical landing pad for a tiny visitor.", 150),
        new Item("wizard_workshop", "Wizard's Workshop", "A fantasy workshop for harmless pet experiments.", 200),
        new Item("mini_train", "Miniature Train", "A decorative train that travels around a short track.", 125),
        new Item("floating_island", "Floating Island", "A small floating garden island.", 300),
        new Item("tiny_volcano", "Tiny Fantasy Volcano", "A non-hazardous fantasy volcano landmark.", 150),
        new Item("unicorn_fountain", "Unicorn Fountain", "A peaceful unicorn-themed fountain.", 100),
        new Item("dragon_roost", "Dragon-Roost Tower", "A cozy tower for dragon friends.", 250),
        new Item("robot_station", "Robot Charging Station", "A friendly charging station for robot pets.", 125),
        new Item("magical_flower_garden", "Magical Flower Garden", "A garden of fictional, non-flashing flowers.", 75),
        new Item("floating_library", "Giant Floating Library", "A whimsical library for Python discoveries.", 350),
        new Item("planetarium", "Planetarium", "A calm star-learning building.", 400),
        new Item("artists_studio", "Artist's Studio", "A creative studio for decorating the world.", 150),
        new Item("observatory", "Observatory", "A quiet place to explore the fictional sky.", 300),
        new Item("campground", "Campground", "A cozy outdoor gathering area for pets.", 100),
        new Item("rainbow_bridge", "Rainbow Bridge", "A decorative bridge for the Unicorn Sanctuary.", 200),
        new Item("moon_garden", "Moon Garden", "A peaceful nighttime garden.", 175),
        new Item("star_gazebo", "Star Gazebo", "A quiet gazebo for pet stargazing.", 125),
        new Item("crystal_fountain", "Crystal Fountain", "A fantasy fountain for the Unicorn Sanctuary.", 250)
    ));

    private RewardCatalog() {}
    public static List<Item> all() { return ITEMS; }
    public static Item byId(String id) {
        for (Item item : ITEMS) if (item.id.equals(id)) return item;
        return null;
    }
}
