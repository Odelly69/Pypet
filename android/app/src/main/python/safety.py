"""Calm-output policy for the pet UI.

This is a design guardrail, not a medical device or a guarantee against seizures.
All pet animations should use slow, non-flashing transitions and optional haptics.
"""

MAX_ANIMATION_FPS = 12
ALLOW_FLASHING = False
ALLOW_SCREEN_SHAKE = False
ALLOW_REWARD_HAPTICS = False


def safe_animation(request: dict) -> dict:
    result = dict(request)
    result["flashing"] = False
    result["screen_shake"] = False
    result["haptic"] = False
    result["max_fps"] = min(int(request.get("fps", MAX_ANIMATION_FPS)), MAX_ANIMATION_FPS)
    return result
