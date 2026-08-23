"""Hands-on 3D graphics track for Pypet Academy.

3D is taught by building things in the World: coordinates first, then camera,
transforms, lighting, animation, interaction, optimization and complete scenes.
All projects use the game's calm visual-safety constraints.
"""
from dataclasses import dataclass, asdict

@dataclass(frozen=True)
class GraphicsLesson:
    id: str
    level: str
    title: str
    skill: str
    mission: str

LESSONS = [
    GraphicsLesson('3d-01','Novice','3D Coordinates','x, y, z and world space','Place a pet and three objects at exact 3D points.'),
    GraphicsLesson('3d-02','Novice','Screen Mapping','world-to-screen coordinates','Map a 3D point onto different phone resolutions.'),
    GraphicsLesson('3d-03','Novice','Vectors','direction, distance, magnitude','Make the pet walk toward a target.'),
    GraphicsLesson('3d-04','Apprentice','Transforms','translation, rotation, scale','Move, rotate and resize a world object.'),
    GraphicsLesson('3d-05','Apprentice','Camera','position, target, projection','Build a calm third-person camera.'),
    GraphicsLesson('3d-06','Apprentice','Perspective','field of view and depth','Place objects at different depths and predict their size.'),
    GraphicsLesson('3d-07','Intermediate','Meshes','vertices, edges, faces','Build a simple 3D object from primitives.'),
    GraphicsLesson('3d-08','Intermediate','Materials','surface properties and textures','Give a World object an appropriate material.'),
    GraphicsLesson('3d-09','Intermediate','Lighting','ambient, directional, soft shadows','Light a scene without flashing or pulsing.'),
    GraphicsLesson('3d-10','Intermediate','Animation','time-based transforms','Animate a pet smoothly at a controlled rate.'),
    GraphicsLesson('3d-11','Advanced','Collision','bounds, rays, intersections','Make the pet interact safely with 3D objects.'),
    GraphicsLesson('3d-12','Advanced','UI Mapping','touch coordinates to world rays','Tap a point on the phone and select the correct 3D object.'),
    GraphicsLesson('3d-13','Advanced','Optimization','LOD, batching, culling','Keep a large World responsive on a phone.'),
    GraphicsLesson('3d-14','Expert','Scene Architecture','nodes, cameras, systems, assets','Build a maintainable multi-area 3D World.'),
    GraphicsLesson('3d-15','Expert','Shaders','materials, lighting math, restrained effects','Create a gentle visual effect with no strobe or rapid flashing.'),
    GraphicsLesson('3d-16','Master','3D Capstone','complete interactive scene','Design, build, debug, optimize and explain a complete 3D World feature.'),
]

def curriculum():
    return [asdict(x) for x in LESSONS]

def lesson_for(index=0):
    return asdict(LESSONS[index % len(LESSONS)])

def total_lessons():
    return len(LESSONS)
