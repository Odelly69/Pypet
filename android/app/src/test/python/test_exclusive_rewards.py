from exclusive_world_items import all_items, get_item
from world import World


def test_all_exclusive_rewards_have_definitions():
    items = all_items()
    assert len(items) == 22
    assert len({item['id'] for item in items}) == len(items)
    assert all(item['name'] and item['hook'] for item in items)


def test_world_can_place_exclusive_reward():
    world = World()
    placed = world.add_world_item("aurora_tree", "garden")
    assert placed["id"] == "aurora_tree"
    assert placed["location"] == "garden"
    assert world.status()["world_items"] == 1


def test_unknown_reward_is_rejected():
    try:
        get_item("not-a-real-item")
    except ValueError:
        return
    raise AssertionError("Unknown reward should raise ValueError")
