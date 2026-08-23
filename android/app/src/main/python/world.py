from dataclasses import dataclass, field
from pet_catalog import Pet, create_pet
from exclusive_world_items import get_item

@dataclass
class World:
    name: str = "Pypet Valley"
    day: int = 1
    hour: int = 8
    weather: str = "clear"
    pets: list[Pet] = field(default_factory=list)
    world_items: list[dict] = field(default_factory=list)
    locations: list[str] = field(default_factory=lambda: [
        "home", "park", "garden", "market", "grooming", "clinic", "academy", "library", "workshop"
    ])

    def add_pet(self, pet: Pet) -> Pet:
        self.pets.append(pet)
        return pet

    def create_pet(self, species_index: int = 0, name: str | None = None) -> Pet:
        return self.add_pet(create_pet(species_index, name))

    def add_world_item(self, item_id: str, location: str = "home") -> dict:
        item = get_item(item_id)
        if location not in self.locations:
            raise ValueError(f"Unknown world location: {location}")
        placed = {"id": item_id, "name": item["name"], "kind": item["kind"], "location": location}
        if not any(i["id"] == item_id for i in self.world_items):
            self.world_items.append(placed)
        return placed

    def remove_world_item(self, item_id: str) -> bool:
        before = len(self.world_items)
        self.world_items = [i for i in self.world_items if i["id"] != item_id]
        return len(self.world_items) != before

    def tick(self, hours: int = 1) -> None:
        for _ in range(max(0, hours)):
            self.hour += 1
            if self.hour >= 24:
                self.hour = 0
                self.day += 1
            for pet in self.pets:
                pet.hunger = max(0, pet.hunger - 2)
                pet.energy = max(0, pet.energy - 1)
                if pet.hunger < 20:
                    pet.happiness = max(0, pet.happiness - 1)

    def pets_needing_care(self) -> list[Pet]:
        return [p for p in self.pets if p.hunger < 30 or p.energy < 20 or p.happiness < 30]

    def status(self) -> dict:
        return {"name": self.name, "day": self.day, "hour": self.hour,
                "weather": self.weather, "pets": len(self.pets),
                "world_items": len(self.world_items),
                "needs_care": len(self.pets_needing_care())}
