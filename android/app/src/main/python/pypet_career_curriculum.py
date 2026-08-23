"""Canonical Pypet job-readiness and graduation curriculum.

Job readiness incorporates every earlier course requirement. Graduation is
competency based: learners demonstrate skills, build, test, debug, explain,
and ship projects. It prepares learners for real-world work but does not
promise employment.
"""
from dataclasses import dataclass, asdict

@dataclass(frozen=True)
class CareerLesson:
    id: str
    stage: str
    title: str
    skill: str
    project: str

LESSONS = [
    CareerLesson('career-01','Foundation','Professional Python Workflow','Git, branches, commits, issues, environments','Maintain a project from issue to tested commit.'),
    CareerLesson('career-02','Foundation','Code Quality','PEP 8, readability, documentation, type hints','Refactor an intentionally messy World feature.'),
    CareerLesson('career-03','Foundation','Testing','unit, integration, regression and test design','Build tests and repair failing tests.'),
    CareerLesson('career-04','Foundation','Debugging','reproduce, isolate, diagnose, fix, verify','Investigate a realistic production-style bug.'),
    CareerLesson('career-05','Professional','Algorithms & Data Structures','complexity, lists, maps, sets, stacks, queues, trees, graphs','Solve practical problems and explain tradeoffs.'),
    CareerLesson('career-06','Professional','APIs and Web Development','HTTP, REST, JSON, clients and services','Build an API-backed World service.'),
    CareerLesson('career-07','Professional','Databases','SQL, schema design, migrations, transactions','Design persistent application data.'),
    CareerLesson('career-08','Professional','Security','validation, secrets, authentication concepts, safe dependencies','Threat-model and harden a project feature.'),
    CareerLesson('career-09','Professional','Deployment and CI','packaging, configuration, CI, release artifacts','Prepare repeatable deployment.'),
    CareerLesson('career-10','Professional','Collaboration','code review, issues, documentation and pull requests','Review and improve another project.'),
    CareerLesson('career-11','Professional','Architecture','requirements, interfaces, modular design and tradeoffs','Turn requirements into maintainable architecture.'),
    CareerLesson('career-12','Professional','Performance','profiling, benchmarks, memory and concurrency tradeoffs','Measure and optimize a slow feature.'),
    CareerLesson('career-13','Core Mastery','Full Python Language','fundamentals through advanced language features','Complete integrated Python challenges independently.'),
    CareerLesson('career-14','Core Mastery','Standard Library & Module Mastery','major standard-library families, documentation and module selection','Solve practical problems using appropriate modules and explain why.'),
    CareerLesson('career-15','Graphics','2D Graphics & Coordinates','points, vectors, world/screen space, resolution, DPI, transforms','Build a resolution-independent interactive scene.'),
    CareerLesson('career-16','Graphics','Animation','timing, interpolation, sprites, state machines, curves, procedural animation','Build a smooth interactive animation system.'),
    CareerLesson('career-17','Graphics','3D Graphics','X/Y/Z, transforms, camera, perspective, meshes, materials, lighting, collision','Build and optimize an interactive 3D World feature.'),
    CareerLesson('career-18','Frameworks','Tkinter Mastery','widgets, layout, events, Canvas and architecture','Build a complete tested GUI.'),
    CareerLesson('career-19','Frameworks','Pygame Mastery','game loop, input, sprites, surfaces, collision and timing','Build a complete playable game.'),
    CareerLesson('career-20','Portfolio','Professional Python Application','requirements through release','Build a polished tested application with documentation.'),
    CareerLesson('career-21','Portfolio','Graphics/Game Project','Pygame, graphics and animation','Build and explain a complete playable project.'),
    CareerLesson('career-22','Portfolio','GUI Project','Tkinter and event-driven programming','Build a usable desktop application.'),
    CareerLesson('career-23','Portfolio','3D/Animation Project','3D, animation, coordinates and optimization','Build a safe animated 3D World feature.'),
    CareerLesson('career-24','Master','Technical Interview Lab','problem solving, algorithms, data structures and explanation','Solve practical problems and explain tradeoffs.'),
    CareerLesson('career-25','Master','Take-Home Simulation','requirements, ambiguity, implementation and tests','Complete a realistic engineering assignment.'),
    CareerLesson('career-26','Master','Final Capstone & Graduation','end-to-end engineering and communication','Ship, document, test and defend a production-style capstone.'),
]

ROLE_SKILLS = {
    'Python Developer': ['full Python','standard library','testing','Git','databases','APIs','deployment'],
    'Automation Developer': ['Python','files','CLI','APIs','concurrency','testing','security'],
    'QA/Test Automation': ['Python','testing','debugging','CI','APIs','automation'],
    'Game/Interactive Developer': ['Python','Pygame','2D graphics','animation','3D graphics','optimization'],
    'GUI Developer': ['Python','Tkinter','event-driven programming','testing','packaging'],
    'Junior Software Engineer': ['full Python','standard library','algorithms','data structures','Git','testing','architecture'],
}

REQUIRED_MODULE_DOMAINS = [
    'builtins','collections','itertools','functools','operator','math','statistics','decimal','fractions','random',
    'datetime','zoneinfo','calendar','time','pathlib','os','shutil','glob','tempfile','json','csv','configparser',
    'sqlite3','re','string','textwrap','difflib','enum','dataclasses','typing','abc','contextlib','logging','traceback',
    'warnings','unittest','doctest','argparse','subprocess','threading','multiprocessing','concurrent.futures',
    'asyncio','queue','socket','http','urllib','email','xml','html','hashlib','hmac','secrets','base64','struct',
    'pickle','shelve','cProfile','tkinter','pygame',
]

def curriculum(): return [asdict(x) for x in LESSONS]
def total_lessons(): return len(LESSONS)
def role_skills(): return ROLE_SKILLS
def required_modules(): return REQUIRED_MODULE_DOMAINS

def mastery_requirements():
    return {
        'all_previous_curricula': True,
        'full_python_language': True,
        'standard_library_module_breadth': True,
        '2d_graphics': True,
        'coordinate_resolution_mapping': True,
        'animation': True,
        '3d_graphics': True,
        'tkinter': True,
        'pygame': True,
        'algorithms_data_structures': True,
        'testing_debugging': True,
        'git_code_review': True,
        'apis_databases': True,
        'security': True,
        'deployment_ci': True,
        'portfolio_projects': 4,
        'technical_interview': True,
        'take_home_project': True,
        'capstone': True,
        'confidence_from_demonstrated_competency': True,
        'employment_guarantee': False,
    }
