from world import World

CARE_ACTIONS = ("feed", "play", "sleep", "water", "groom")

def care_pet(pet, action: str) -> str:
    if action == "feed":
        return pet.feed("pet food")
    if action == "play":
        return pet.play("playground")
    if action == "sleep":
        pet.energy = min(100, pet.energy + 25)
        return f"{pet.name} took a nap."
    if action == "water":
        pet.happiness = min(100, pet.happiness + 2)
        return f"{pet.name} had fresh water."
    if action == "groom":
        pet.happiness = min(100, pet.happiness + 5)
        return f"{pet.name} is clean and comfortable."
    raise ValueError(f"Unknown care action: {action}")

def care_all(world: World) -> list[str]:
    messages = []
    for pet in world.pets_needing_care():
        if pet.hunger < 30:
            messages.append(care_pet(pet, "feed"))
        if pet.energy < 20:
            messages.append(care_pet(pet, "sleep"))
    return messages
