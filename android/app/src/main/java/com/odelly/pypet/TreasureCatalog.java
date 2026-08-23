package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Google Play one-time Treasure Trove products. Product IDs must match Play Console. */
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
        new Item("treasure_little", "🌟 Little Treasure", "A small special world decoration.", "$0.99"),
        new Item("treasure_shiny", "✨ Shiny Treasure", "A charming collectible decoration.", "$1.99"),
        new Item("treasure_rare", "💎 Rare Treasure", "A larger centerpiece for your pet world.", "$4.99"),
        new Item("treasure_royal", "👑 Royal Treasure", "A grand world centerpiece with special animation.", "$9.99"),
        new Item("treasure_legendary", "🌌 Legendary Treasure", "A complete themed world set with exclusive ambience.", "$19.99"),
        new Item("treasure_mythic", "🌠 Mythic Treasure", "Our biggest Treasure Trove collection for collectors.", "$29.99")
    ));

    private TreasureCatalog() {}
    public static List<Item> all() { return ITEMS; }
}
