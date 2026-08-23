from dataclasses import dataclass, field
from pet_catalog import Pet, create_pet

@dataclass
class World:
    name: str = "Pypet Valley"
    day: int = 1
    hour: int = 8
    weather: str = "clear"
    pets: list[Pet] = field(default_factory=list)
    locations: list[str] = field(default_factory=lambda: [
        "home", "park", "garden", "market", "grooming", "clinic", "academy", "library", "workshop"
    ])

    def add_pet(self, pet: Pet) -> Pet:
        self.pets.append(pet)
        return pet

    def create_pet(self, species_index: int = 0, name: str | None = None) -> Pet:
        return self.add_pet(create_pet(species_index, name))

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
                "needs_care": len(self.pets_needing_care())}
