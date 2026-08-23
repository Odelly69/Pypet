package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Original Pypet world cosmetics across gameplay, ad-earned, and premium collections. */
public final class RewardCatalog {
    public static final class Item {
        public final String id;
        public final String name;
        public final String description;
        public final int priceCoins;
        public final String tier;
        public Item(String id, String name, String description, int priceCoins, String tier) {
            this.id = id; this.name = name; this.description = description; this.priceCoins = priceCoins; this.tier = tier;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("aurora_tree", "Aurora Tree", "A calm fantasy tree with softly shifting leaves.", 25, "COIN"),
        new Item("sunny_bench", "Sunny Nook Bench", "A cozy place for a pet to rest.", 15, "COIN"),
        new Item("berry_patch", "Berry Patch", "A cheerful garden patch for collecting and decorating.", 20, "COIN"),
        new Item("rainbow_arch", "Rainbow Garden Arch", "A colorful garden entrance with gentle motion.", 40, "COIN"),
        new Item("cloud_pillow", "Cloud Pillow", "A soft-looking decorative cloud seat.", 35, "COIN"),
        new Item("tiny_castle", "Tiny Castle", "A miniature castle where pets can explore.", 100, "COIN"),
        new Item("pet_carousel", "Pet Carousel", "A playful decorative carousel with no ride requirement.", 90, "COIN"),
        new Item("ufo_pad", "Starlight Visitor Pad", "A whimsical landing pad for a tiny friendly visitor.", 150, "COIN"),
        new Item("wizard_workshop", "Wizard's Workshop", "A cozy fantasy workshop for harmless experiments.", 200, "COIN"),
        new Item("mini_train", "Miniature Garden Train", "A decorative train traveling a short garden loop.", 125, "COIN"),
        new Item("floating_island", "Floating Garden Island", "A small floating garden landmark.", 300, "COIN"),
        new Item("tiny_volcano", "Tiny Fantasy Volcano", "A non-hazardous fantasy landmark.", 150, "COIN"),
        new Item("unicorn_fountain", "Moonmane Fountain", "A peaceful original fantasy-horse fountain.", 100, "COIN"),
        new Item("dragon_roost", "Dragon-Roost Tower", "A cozy tower for friendly dragon companions.", 250, "COIN"),
        new Item("robot_station", "Buddy Bot Station", "A friendly charging nook for robot pets.", 125, "COIN"),
        new Item("magical_flower_garden", "Glowgarden", "Fictional flowers with gentle, non-flashing illumination.", 75, "COIN"),
        new Item("floating_library", "Giant Floating Library", "A whimsical library for Python discoveries.", 350, "COIN"),
        new Item("planetarium", "Pocket Planetarium", "A calm star-learning building.", 400, "COIN"),
        new Item("artists_studio", "Maker's Studio", "A creative studio for decorating the world.", 150, "COIN"),
        new Item("observatory", "Cloudwatch Observatory", "A quiet place to explore the fictional sky.", 300, "COIN"),
        new Item("campground", "Cozy Campground", "A friendly outdoor gathering area for pets.", 100, "COIN"),
        new Item("rainbow_bridge", "Prism Creek Bridge", "A colorful decorative bridge.", 200, "COIN"),
        new Item("moon_garden", "Moonlit Garden", "A peaceful nighttime garden.", 175, "COIN"),
        new Item("star_gazebo", "Stargazer Gazebo", "A quiet gazebo for pet stargazing.", 125, "COIN"),
        new Item("crystal_fountain", "Crystal Spring", "A fantasy fountain with calm flowing water.", 250, "COIN"),
        new Item("pypet_treehouse", "Pypet Treehouse", "An original treehouse for lessons and play.", 225, "COIN"),
        new Item("code_cafe", "Code Café", "A tiny café where pets can celebrate completed lessons.", 175, "COIN"),
        new Item("pixel_garden", "Pixel Garden", "A playful garden inspired by coding shapes.", 80, "COIN"),
        new Item("maker_workbench", "Maker Workbench", "A creative work area for building imaginary gadgets.", 120, "COIN"),
        new Item("story_stage", "Storybook Stage", "A little stage for pet adventures and stories.", 160, "COIN"),

        // Dedicated ad-earned collection. These are still bought with Pypet Coins;
        // ads only provide an optional accelerated earning path.
        new Item("ad_cloud_lounge", "Cloud Lounge", "A floating lounge earned through the Pypet Coin economy.", 75, "AD_COIN"),
        new Item("ad_sparkle_garden", "Sparkle Garden", "A gentle garden of original geometric flowers.", 125, "AD_COIN"),
        new Item("ad_comet_cart", "Comet Cart", "A tiny decorative cart for collecting imaginary stardust.", 175, "AD_COIN"),
        new Item("ad_mini_airship", "Pocket Airship", "A friendly floating airship for the home world.", 250, "AD_COIN"),
        new Item("ad_crystal_cabin", "Crystal Cabin", "A cozy fantasy cabin with calm ambient scenery.", 300, "AD_COIN"),
        new Item("ad_sky_garden", "Sky Garden", "A raised garden platform with soft cloud scenery.", 400, "AD_COIN"),
        new Item("ad_moon_obelisk", "Moonstone Marker", "A quiet fictional landmark for the night garden.", 500, "AD_COIN"),
        new Item("ad_code_lab", "Pocket Code Lab", "A miniature lab for playful Python-themed decoration.", 600, "AD_COIN"),
        new Item("ad_starship_dock", "Starship Dock", "An original miniature exploration dock.", 750, "AD_COIN"),
        new Item("ad_cloud_palace", "Cloud Palace", "A large original floating residence for advanced collectors.", 1000, "AD_COIN")
    ));

    private RewardCatalog() {}
    public static List<Item> all() { return ITEMS; }
    public static Item byId(String id) { for (Item item : ITEMS) if (item.id.equals(id)) return item; return null; }
}
