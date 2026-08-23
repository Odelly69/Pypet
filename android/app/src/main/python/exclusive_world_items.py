"""Definitions for optional, non-transferable sponsored world decorations.

These are cosmetic/gameplay objects only. They never gate Python lessons, pet care,
ordinary pets, or the unicorn.
"""

EXCLUSIVE_WORLD_ITEMS = {
    "aurora_tree": {"name": "Aurora Tree", "kind": "garden", "hook": "tree.glow_gently()"},
    "rainbow_arch": {"name": "Rainbow Garden Arch", "kind": "garden", "hook": "arch.open()"},
    "tiny_castle": {"name": "Tiny Castle", "kind": "building", "hook": "castle.explore()"},
    "pet_carousel": {"name": "Pet Carousel", "kind": "play", "hook": "carousel.start()"},
    "ufo_pad": {"name": "UFO Landing Pad", "kind": "landmark", "hook": "pad.signal()"},
    "wizard_workshop": {"name": "Wizard's Workshop", "kind": "building", "hook": "workshop.experiment()"},
    "mini_train": {"name": "Miniature Train", "kind": "transport", "hook": "train.visit(location)"},
    "floating_island": {"name": "Floating Island", "kind": "habitat", "hook": "island.visit()"},
    "tiny_volcano": {"name": "Tiny Fantasy Volcano", "kind": "landmark", "hook": "volcano.observe()"},
    "unicorn_fountain": {"name": "Unicorn Fountain", "kind": "decor", "hook": "fountain.splash()"},
    "dragon_roost": {"name": "Dragon-Roost Tower", "kind": "habitat", "hook": "roost.rest()"},
    "robot_station": {"name": "Robot Charging Station", "kind": "utility", "hook": "station.charge(robot)"},
    "magical_flower_garden": {"name": "Magical Flower Garden", "kind": "garden", "hook": "garden.water()"},
    "floating_library": {"name": "Giant Floating Library", "kind": "building", "hook": "library.read()"},
    "planetarium": {"name": "Planetarium", "kind": "building", "hook": "planetarium.observe()"},
    "artists_studio": {"name": "Artist's Studio", "kind": "building", "hook": "studio.create()"},
    "observatory": {"name": "Observatory", "kind": "building", "hook": "observatory.observe()"},
    "campground": {"name": "Campground", "kind": "outdoor", "hook": "campground.rest()"},
    "rainbow_bridge": {"name": "Rainbow Bridge", "kind": "unicorn_sanctuary", "hook": "bridge.cross()"},
    "moon_garden": {"name": "Moon Garden", "kind": "unicorn_sanctuary", "hook": "garden.explore()"},
    "star_gazebo": {"name": "Star Gazebo", "kind": "unicorn_sanctuary", "hook": "gazebo.stargaze()"},
    "crystal_fountain": {"name": "Crystal Fountain", "kind": "unicorn_sanctuary", "hook": "fountain.splash()"},
}


def get_item(item_id: str) -> dict:
    try:
        return EXCLUSIVE_WORLD_ITEMS[item_id].copy()
    except KeyError as exc:
        raise ValueError(f"Unknown exclusive world item: {item_id}") from exc


def all_items() -> list[dict]:
    return [{"id": item_id, **data} for item_id, data in EXCLUSIVE_WORLD_ITEMS.items()]
