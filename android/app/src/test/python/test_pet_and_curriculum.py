from pet_catalog import catalog, create_pet
from lesson_engine import all_lessons, lesson


def test_pet_catalog_has_multiple_species():
    pets = catalog()
    assert len(pets) >= 10
    assert len({p['species'] for p in pets}) >= 10


def test_pet_behavior_changes_state():
    pet = create_pet(0)
    old_happiness = pet.happiness
    pet.play('fetch')
    assert pet.happiness > old_happiness


def test_curriculum_is_substantial():
    assert len(all_lessons()) >= 100
    assert lesson(6, 0)['track'] == 'Object-oriented Python'
