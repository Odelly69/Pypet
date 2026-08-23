from dataclasses import dataclass, field
from typing import Any

@dataclass
class Pet:
    species: str
    name: str
    personality: str
    hunger: int = 80
    energy: int = 80
    happiness: int = 70
    knowledge: int = 0
    xp: int = 0
    inventory: list[str] = field(default_factory=list)

    def feed(self, food: str = 'treat') -> str:
        self.hunger = min(100, self.hunger + 15)
        self.happiness = min(100, self.happiness + 3)
        self.inventory.append(food)
        return f'{self.name} the {self.species} happily ate {food}!'

    def play(self, game: str = 'play') -> str:
        self.energy = max(0, self.energy - 5)
        self.happiness = min(100, self.happiness + 10)
        return f'{self.name} played {game}!'

    def learn(self, xp: int = 10) -> str:
        self.knowledge += 1
        self.xp += xp
        return f'{self.name} learned something new! +{xp} XP'

PET_CATALOG = [('Dog','Buddy','playful'),('Cat','Mochi','curious'),('Fox','Ember','clever'),('Rabbit','Clover','gentle'),('Dragon','Nova','brave'),('Owl','Pixel','wise'),('Robot','Byte','logical'),('Axolotl','Bubbles','friendly'),('Penguin','Pipkin','cheerful'),('Dinosaur','Rex','adventurous')]

def create_pet(index: int = 0, name: str | None = None) -> Pet:
    species, default_name, personality = PET_CATALOG[index % len(PET_CATALOG)]
    return Pet(species, name or default_name, personality)

def catalog() -> list[dict[str, Any]]:
    return [{'species': s, 'name': n, 'personality': p} for s, n, p in PET_CATALOG]
