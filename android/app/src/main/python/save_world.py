import json
from dataclasses import asdict
from world import World
from pet_catalog import Pet


def save_world(world: World) -> str:
    return json.dumps({
        "name": world.name,
        "day": world.day,
        "hour": world.hour,
        "weather": world.weather,
        "pets": [asdict(p) for p in world.pets],
    })


def load_world(data: str) -> World:
    raw = json.loads(data)
    world = World(raw.get("name", "Pypet Valley"), raw.get("day", 1), raw.get("hour", 8), raw.get("weather", "clear"))
    for p in raw.get("pets", []):
        world.pets.append(Pet(**p))
    return world
