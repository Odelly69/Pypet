"""Canonical Pypet job-readiness curriculum and hands-on lesson API."""
from dataclasses import dataclass, asdict

@dataclass(frozen=True)
class CareerLesson:
    id: str; stage: str; title: str; skill: str; project: str

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
    'Python Developer':['full Python','standard library','testing','Git','databases','APIs','deployment'],
    'Automation Developer':['Python','files','CLI','APIs','concurrency','testing','security'],
    'QA/Test Automation':['Python','testing','debugging','CI','APIs','automation'],
    'Game/Interactive Developer':['Python','Pygame','2D graphics','animation','3D graphics','optimization'],
    'GUI Developer':['Python','Tkinter','event-driven programming','testing','packaging'],
    'Junior Software Engineer':['full Python','standard library','algorithms','data structures','Git','testing','architecture'],
}
REQUIRED_MODULE_DOMAINS = ['builtins','collections','itertools','functools','operator','math','statistics','decimal','fractions','random','datetime','zoneinfo','calendar','time','pathlib','os','shutil','glob','tempfile','json','csv','configparser','sqlite3','re','string','textwrap','difflib','enum','dataclasses','typing','abc','contextlib','logging','traceback','warnings','unittest','doctest','argparse','subprocess','threading','multiprocessing','concurrent.futures','asyncio','queue','socket','http','urllib','email','xml','html','hashlib','hmac','secrets','base64','struct','pickle','shelve','cProfile','tkinter','pygame']

# The World uses this API for learn-by-doing missions. The lesson is an
# actionable task, not a passive reading card; the learner must write/run/
# debug code in the Academy Lab before progression is awarded.
LESSON_TASKS = {
    'career-01': ('Fix the project workflow', 'Create a branch, make a clean commit, and explain the change.'),
    'career-02': ('Refactor a World feature', 'Improve the supplied code without changing its behavior; run the tests.'),
    'career-03': ('Repair the failing tests', 'Write or repair tests until the complete suite passes.'),
    'career-04': ('Hunt the bug', 'Reproduce the defect, isolate it, patch it, and prove the fix.'),
    'career-13': ('Program your pet', 'Write Python that changes your pet\'s behavior, run it, observe the result, and iterate.'),
    'career-14': ('Choose the right module', 'Solve a World task using an appropriate standard-library module and justify the choice.'),
    'career-15': ('Map the World', 'Convert a logical target point to screen coordinates at multiple resolutions.'),
    'career-16': ('Animate the pet', 'Implement timed movement with interpolation; observe and tune the result.'),
    'career-17': ('Build a 3D object', 'Transform, position, and animate a 3D object while explaining camera coordinates.'),
    'career-18': ('Build a GUI', 'Create a functional Tkinter interface with events and validation.'),
    'career-19': ('Build a game', 'Create a playable Pygame loop with input, collision, scoring, and timing.'),
    'career-26': ('Ship your capstone', 'Implement requirements, tests, documentation, release build, and a technical defense.'),
}

def curriculum(): return [asdict(x) for x in LESSONS]
def total_lessons(): return len(LESSONS)
def role_skills(): return ROLE_SKILLS
def required_modules(): return REQUIRED_MODULE_DOMAINS

def current_lesson(index=0):
    lesson = LESSONS[max(0, min(int(index), len(LESSONS)-1))]
    title, task = LESSON_TASKS.get(lesson.id, (lesson.title, lesson.project))
    return {'id':lesson.id,'stage':lesson.stage,'title':title,'skill':lesson.skill,'project':lesson.project,'task':task,'hands_on':True}

def lesson_task(index=0): return current_lesson(index)['task']

def mastery_requirements():
    return {'all_previous_curricula':True,'full_python_language':True,'standard_library_module_breadth':True,'2d_graphics':True,'coordinate_resolution_mapping':True,'animation':True,'3d_graphics':True,'tkinter':True,'pygame':True,'algorithms_data_structures':True,'testing_debugging':True,'git_code_review':True,'apis_databases':True,'security':True,'deployment_ci':True,'portfolio_projects':4,'technical_interview':True,'take_home_project':True,'capstone':True,'confidence_from_demonstrated_competency':True,'employment_guarantee':False}
