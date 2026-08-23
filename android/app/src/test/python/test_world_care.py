from world import World
from care import care_pet
from save_world import save_world, load_world


def test_world_has_multiple_pets():
    world = World()
    world.create_pet(0, "Buddy")
    world.create_pet(1, "Mochi")
    assert len(world.pets) == 2


def test_care_changes_pet_state():
    world = World()
    pet = world.create_pet(0, "Buddy")
    pet.hunger = 10
    care_pet(pet, "feed")
    assert pet.hunger > 10


def test_world_save_round_trip():
    world = World()
    world.create_pet(4, "Ember")
    restored = load_world(save_world(world))
    assert restored.name == world.name
    assert len(restored.pets) == 1
    assert restored.pets[0].name == "Ember"
