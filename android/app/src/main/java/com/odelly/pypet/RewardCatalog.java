package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Original Pypet world cosmetics. Prices use Pypet Coins earned through play and optional ads. */
public final class RewardCatalog {
    public static final class Item {
        public final String id;
        public final String name;
        public final String description;
        public final int priceCoins;

        public Item(String id, String name, String description, int priceCoins) {
            this.id = id; this.name = name; this.description = description; this.priceCoins = priceCoins;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("aurora_tree", "Aurora Tree", "A calm fantasy tree with softly shifting leaves.", 25),
        new Item("sunny_bench", "Sunny Nook Bench", "A cozy place for a pet to rest.", 15),
        new Item("berry_patch", "Berry Patch", "A cheerful garden patch for collecting and decorating.", 20),
        new Item("rainbow_arch", "Rainbow Garden Arch", "A colorful garden entrance with gentle motion.", 40),
        new Item("cloud_pillow", "Cloud Pillow", "A soft-looking decorative cloud seat.", 35),
        new Item("tiny_castle", "Tiny Castle", "A miniature castle where pets can explore.", 100),
        new Item("pet_carousel", "Pet Carousel", "A playful decorative carousel with no ride requirement.", 90),
        new Item("ufo_pad", "Starlight Visitor Pad", "A whimsical landing pad for a tiny friendly visitor.", 150),
        new Item("wizard_workshop", "Wizard's Workshop", "A cozy fantasy workshop for harmless experiments.", 200),
        new Item("mini_train", "Miniature Garden Train", "A decorative train traveling a short garden loop.", 125),
        new Item("floating_island", "Floating Garden Island", "A small floating garden landmark.", 300),
        new Item("tiny_volcano", "Tiny Fantasy Volcano", "A non-hazardous fantasy landmark.", 150),
        new Item("unicorn_fountain", "Moonmane Fountain", "A peaceful original fantasy-horse fountain.", 100),
        new Item("dragon_roost", "Dragon-Roost Tower", "A cozy tower for friendly dragon companions.", 250),
        new Item("robot_station", "Buddy Bot Station", "A friendly charging nook for robot pets.", 125),
        new Item("magical_flower_garden", "Glowgarden", "Fictional flowers with gentle, non-flashing illumination.", 75),
        new Item("floating_library", "Giant Floating Library", "A whimsical library for Python discoveries.", 350),
        new Item("planetarium", "Pocket Planetarium", "A calm star-learning building.", 400),
        new Item("artists_studio", "Maker's Studio", "A creative studio for decorating the world.", 150),
        new Item("observatory", "Cloudwatch Observatory", "A quiet place to explore the fictional sky.", 300),
        new Item("campground", "Cozy Campground", "A friendly outdoor gathering area for pets.", 100),
        new Item("rainbow_bridge", "Prism Creek Bridge", "A colorful decorative bridge.", 200),
        new Item("moon_garden", "Moonlit Garden", "A peaceful nighttime garden.", 175),
        new Item("star_gazebo", "Stargazer Gazebo", "A quiet gazebo for pet stargazing.", 125),
        new Item("crystal_fountain", "Crystal Spring", "A fantasy fountain with calm flowing water.", 250),
        new Item("pypet_treehouse", "Pypet Treehouse", "An original treehouse for lessons and play.", 225),
        new Item("code_cafe", "Code Café", "A tiny café where pets can celebrate completed lessons.", 175),
        new Item("pixel_garden", "Pixel Garden", "A playful garden inspired by coding shapes.", 80),
        new Item("maker_workbench", "Maker Workbench", "A creative work area for building imaginary gadgets.", 120),
        new Item("story_stage", "Storybook Stage", "A little stage for pet adventures and stories.", 160)
    ));

    private RewardCatalog() {}
    public static List<Item> all() { return ITEMS; }
    public static Item byId(String id) {
        for (Item item : ITEMS) if (item.id.equals(id)) return item;
        return null;
    }
}
