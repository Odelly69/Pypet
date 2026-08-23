"""Small, learner-facing API for hands-on Python lessons."""
from world import World
from care import care_pet

world = World()

# A friendly starting neighborhood with several pets together.
world.create_pet(0, "Buddy")
world.create_pet(1, "Mochi")
world.create_pet(4, "Ember")


def feed(pet, food="pet food"):
    return pet.feed(food)


def play(pet, activity="playground"):
    return pet.play(activity)


def care(pet, action="feed"):
    return care_pet(pet, action)


def place_exclusive_item(item_id, location="home"):
    """Place an owned exclusive decoration into the simulated world."""
    return world.add_world_item(item_id, location)


def advance(hours=1):
    world.tick(hours)
    return world.status()
