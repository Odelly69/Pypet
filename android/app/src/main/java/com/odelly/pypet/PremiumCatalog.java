package com.odelly.pypet;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Product IDs must exactly match the one-time products created in Play Console. */
public final class PremiumCatalog {
    public static final class Item {
        public final String productId;
        public final String name;
        public final String description;
        public final String priceTier;

        Item(String productId, String name, String description, String priceTier) {
            this.productId = productId;
            this.name = name;
            this.description = description;
            this.priceTier = priceTier;
        }
    }

    private static final List<Item> ITEMS = Collections.unmodifiableList(Arrays.asList(
        new Item("pypet_premium_cozy_home", "Cozy Home Pack", "Premium home furniture, rugs, lamps, and a fireplace.", "$0.99"),
        new Item("pypet_premium_garden", "Enchanted Garden", "A permanent garden expansion with animated flowers and a pond.", "$1.99"),
        new Item("pypet_premium_pet_outfits", "Pet Fashion Chest", "A permanent collection of premium collars, hats, capes, and outfits.", "$2.99"),
        new Item("pypet_premium_music", "Pip's Music Box", "Unlocks an expanded collection of gentle original background tunes.", "$2.99"),
        new Item("pypet_premium_treehouse", "Sky Treehouse", "A premium elevated home with a private play deck.", "$4.99"),
        new Item("pypet_premium_dragon_lair", "Dragon Lair", "A premium fantasy habitat for dragon-themed world play.", "$4.99"),
        new Item("pypet_premium_space_station", "Pet Space Station", "A premium orbital play area with planets, windows, and a rover.", "$7.99"),
        new Item("pypet_premium_arcade", "Pypet Arcade", "A permanent mini-game room for pets and Python challenges.", "$7.99"),
        new Item("pypet_premium_world_expansion", "Grand World Expansion", "A large premium world district with new scenery and activities.", "$14.99"),
        new Item("pypet_premium_royal_estate", "Royal Pet Estate", "A large premium estate, gardens, ballroom, and pet stage.", "$19.99"),
        new Item("pypet_premium_creator_bundle", "Creator Bundle", "A premium creator toolkit with themed buildings and decoration sets.", "$24.99"),
        new Item("pypet_premium_ultimate_world", "Ultimate World Collection", "The largest premium collection: multiple districts, habitats, and decoration sets.", "$39.99")
    ));

    private PremiumCatalog() {}
    public static List<Item> all() { return ITEMS; }
}
