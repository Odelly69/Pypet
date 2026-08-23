package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Original Google Play one-time Treasure Trove products. Product IDs must match Play Console. */
public final class TreasureCatalog {
    public static final class Item {
        public final String productId;
        public final String name;
        public final String description;
        public final String priceHint;
        public Item(String productId, String name, String description, String priceHint) {
            this.productId = productId; this.name = name; this.description = description; this.priceHint = priceHint;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("treasure_little", "🌟 Little Treasure", "A small original world decoration bundle.", "$0.99"),
        new Item("treasure_shiny", "✨ Shiny Treasure", "A polished original collectible set.", "$1.99"),
        new Item("treasure_rare", "💎 Rare Treasure", "A larger centerpiece collection for your Pypet world.", "$4.99"),
        new Item("treasure_royal", "👑 Royal Garden Estate", "A grand original garden estate with calm ambient scenery.", "$9.99"),
        new Item("treasure_legendary", "🌌 Celestial Academy", "A complete original learning-world set with buildings, paths, and decorations.", "$19.99"),
        new Item("treasure_mythic", "🌠 Dreamworld Collection", "The largest original Pypet world set for collectors.", "$29.99"),
        new Item("treasure_skyrealm", "☁️ Skybound Realm", "A complete floating-island environment with cloud gardens and bridges.", "$14.99"),
        new Item("treasure_deepsea", "🌊 Deepsea Discovery Dome", "An underwater exploration environment with calm aquarium scenery.", "$14.99"),
        new Item("treasure_woodland", "🌲 Enchanted Canopy", "A woodland environment filled with original friendly creatures and cozy shelters.", "$12.99"),
        new Item("treasure_stargazer", "🔭 Stargazer Observatory", "A premium astronomy environment with original observatory buildings.", "$7.99"),
        new Item("treasure_dragon", "🐉 Sky Dragon Sanctuary", "A fantasy sanctuary for original friendly dragon companions.", "$19.99"),
        new Item("treasure_space", "🚀 Pypet Space Harbor", "An original space-exploration hub with a launch deck and starfield scenery.", "$24.99"),
        new Item("treasure_maker", "🔧 Master Maker Campus", "A complete maker-themed world with workshops and invention spaces.", "$19.99"),
        new Item("treasure_storybook", "📖 Storybook Valley", "A whimsical story world with original buildings and adventure landmarks.", "$14.99"),
        new Item("treasure_festival", "🎪 Wonderlight Festival", "A festive original world filled with games, stages, and decorations.", "$9.99"),
        new Item("treasure_crystal", "💠 Crystal Cavern Realm", "A fantasy cavern world with crystal gardens and underground paths.", "$17.99")
    ));

    private TreasureCatalog() {}
    public static List<Item> all() { return ITEMS; }
}
