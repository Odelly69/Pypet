from dataclasses import dataclass, field

@dataclass
class Reward:
    id: str
    name: str
    description: str
    xp: int = 0

@dataclass
class RewardCenter:
    xp: int = 0
    coins: int = 0
    unlocked: set[str] = field(default_factory=set)
    history: list[str] = field(default_factory=list)

    rewards = (
        Reward("first_pet", "First Friend", "Care for your first pet", 25),
        Reward("python_start", "Python Starter", "Complete your first Python challenge", 50),
        Reward("good_care", "Caring Heart", "Complete five care actions", 75),
        Reward("loop_master", "Loop Trainer", "Complete a loop challenge", 100),
        Reward("oop_builder", "Pet Architect", "Create a programmable pet class", 250),
        Reward("world_builder", "World Builder", "Complete the world capstone", 500),
    )

    def grant(self, reward_id: str) -> Reward | None:
        reward = next((r for r in self.rewards if r.id == reward_id), None)
        if reward is None or reward_id in self.unlocked:
            return None
        self.unlocked.add(reward_id)
        self.xp += reward.xp
        self.coins += max(1, reward.xp // 25)
        self.history.append(reward_id)
        return reward

    def trigger(self, event: str) -> list[Reward]:
        mapping = {
            "first_pet": "first_pet",
            "lesson_complete": "python_start",
            "care_5": "good_care",
            "loop_complete": "loop_master",
            "oop_complete": "oop_builder",
            "capstone_complete": "world_builder",
        }
        reward_id = mapping.get(event)
        reward = self.grant(reward_id) if reward_id else None
        return [reward] if reward else []

    def status(self) -> dict:
        return {"xp": self.xp, "coins": self.coins, "unlocked": sorted(self.unlocked)}
