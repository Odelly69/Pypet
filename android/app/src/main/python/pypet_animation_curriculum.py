"""Hands-on animation curriculum for Pypet Academy.
Animation is taught as a graphics/programming discipline and follows the game's calm visual-safety rules.
"""
from dataclasses import dataclass, asdict

@dataclass(frozen=True)
class AnimationLesson:
    id: str
    level: str
    title: str
    skill: str
    mission: str

LESSONS = [
    AnimationLesson('anim-01','Novice','What Is Animation?','frames, time and state','Make a pet change between calm idle poses.'),
    AnimationLesson('anim-02','Novice','Frame Timing','delta time and fixed steps','Make movement consistent on fast and slow devices.'),
    AnimationLesson('anim-03','Novice','Interpolation','lerp and smooth motion','Move Pip smoothly between two world points.'),
    AnimationLesson('anim-04','Apprentice','2D Sprite Animation','sprite sheets and frame selection','Animate walking, eating and playing.'),
    AnimationLesson('anim-05','Apprentice','Animation State Machines','states and transitions','Switch safely between pet behaviors.'),
    AnimationLesson('anim-06','Apprentice','Curves and Easing','motion curves','Create natural, non-jarring movement.'),
    AnimationLesson('anim-07','Intermediate','2D Game Animation','timelines and events','Synchronize a Pygame action with world events.'),
    AnimationLesson('anim-08','Intermediate','3D Transform Animation','position, rotation, scale over time','Animate a 3D world object smoothly.'),
    AnimationLesson('anim-09','Intermediate','Camera Animation','controlled camera movement','Create a calm cinematic World transition.'),
    AnimationLesson('anim-10','Advanced','Skeletal Concepts','bones, joints and poses','Design a simple articulated pet animation.'),
    AnimationLesson('anim-11','Advanced','Particles','bounded particles and lifetime','Create gentle environmental particles without flashing.'),
    AnimationLesson('anim-12','Advanced','Animation Performance','batching, culling and LOD','Keep animation smooth on mobile hardware.'),
    AnimationLesson('anim-13','Expert','Procedural Animation','rules and simulation','Generate repeatable environmental motion.'),
    AnimationLesson('anim-14','Expert','Interactive Animation','input, state and animation events','Make user actions trigger meaningful animations.'),
    AnimationLesson('anim-15','Master','Animation System','architecture and reusable controllers','Build a reusable animation framework for the World.'),
    AnimationLesson('anim-16','Master','Animation Capstone','complete 2D/3D animation project','Design, implement, debug, optimize and explain a complete animated World feature.'),
]

SAFETY_RULES = {
    'strobe': False, 'rapid_flashing': False, 'rapid_color_cycling': False,
    'screen_shake': False, 'high_contrast_pulsing': False,
    'calm_transitions': True, 'controlled_camera_motion': True,
}

def curriculum(): return [asdict(x) for x in LESSONS]
def lesson_for(index=0): return asdict(LESSONS[index % len(LESSONS)])
def total_lessons(): return len(LESSONS)
