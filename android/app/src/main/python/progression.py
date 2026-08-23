from rewards import RewardCenter

class Progression:
    def __init__(self):
        self.reward_center = RewardCenter()
        self.care_actions = 0
        self.completed_lessons = 0

    def care_completed(self, count=1):
        self.care_actions += count
        events = []
        if self.care_actions >= 5:
            events.extend(self.reward_center.trigger("care_5"))
        return events

    def lesson_completed(self, topic=None):
        self.completed_lessons += 1
        return self.reward_center.trigger("lesson_complete")

    def first_pet(self):
        return self.reward_center.trigger("first_pet")

    def challenge_completed(self, kind):
        return self.reward_center.trigger(f"{kind}_complete")
