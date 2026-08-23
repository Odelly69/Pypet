"""Job-readiness track for Pypet Academy.

Completion is not a promise of employment. It is a competency-based path designed
to prepare a learner for entry-level through advanced Python development work.
It complements the core Python, graphics, 3D and animation curricula.
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
    CareerLesson('career-01','Foundation','Professional Python Workflow','Git, branches, commits, issues, environments','Maintain a small project from issue to tested commit.'),
    CareerLesson('career-02','Foundation','Code Quality','PEP 8, readable code, documentation, type hints','Refactor an intentionally messy World feature.'),
    CareerLesson('career-03','Foundation','Testing in Practice','unit, integration, regression testing','Build a test suite and repair failing tests.'),
    CareerLesson('career-04','Foundation','Debugging','reproduce, isolate, diagnose, fix, verify','Investigate a realistic production-style bug.'),
    CareerLesson('career-05','Professional','APIs and Web Development','HTTP, REST concepts, JSON, clients and services','Build a small API-backed World service.'),
    CareerLesson('career-06','Professional','Databases','SQL, schema design, migrations, transactions','Design and implement persistent application data.'),
    CareerLesson('career-07','Professional','Security','input validation, secrets, authentication concepts, safe dependencies','Threat-model and harden a project feature.'),
    CareerLesson('career-08','Professional','Deployment','packaging, configuration, CI, release artifacts','Prepare a project for repeatable deployment.'),
    CareerLesson('career-09','Professional','Collaboration','code review, issues, documentation, pull requests','Review another learner's project and improve it.'),
    CareerLesson('career-10','Professional','Architecture','requirements, interfaces, modular design, tradeoffs','Turn requirements into a maintainable project design.'),
    CareerLesson('career-11','Professional','Performance','profiling, benchmarks, memory and concurrency tradeoffs','Measure and optimize a slow feature.'),
    CareerLesson('career-12','Portfolio','Portfolio Project','requirements through release','Build a polished Python application with tests and documentation.'),
    CareerLesson('career-13','Portfolio','Graphics/Game Portfolio','Pygame or graphics project','Build and explain a complete playable project.'),
    CareerLesson('career-14','Portfolio','GUI Portfolio','Tkinter desktop application','Build a usable desktop application.'),
    CareerLesson('career-15','Portfolio','3D/Animation Portfolio','3D concepts, animation, coordinates, optimization','Build a safe animated 3D World feature.'),
    CareerLesson('career-16','Master','Technical Interview Lab','problem solving, algorithms, data structures, explanation','Solve timed practical problems and explain tradeoffs.'),
    CareerLesson('career-17','Master','Take-Home Simulation','requirements, ambiguity, implementation, tests','Complete a realistic take-home engineering assignment.'),
    CareerLesson('career-18','Master','Capstone Release','end-to-end engineering','Ship a documented, tested, review-ready capstone and defend the design.'),
]

ROLE_SKILLS = {
    'Python Developer': ['core Python','standard library','testing','Git','databases','APIs','deployment'],
    'Automation Developer': ['Python','files','CLI','APIs','concurrency','testing','security'],
    'QA/Test Automation': ['Python','unittest','testing','debugging','CI','APIs'],
    'Game/Interactive Developer': ['Python','Pygame','graphics','animation','3D concepts','optimization'],
    'GUI Developer': ['Python','Tkinter','event-driven programming','testing','packaging'],
    'Junior Software Engineer': ['all foundation skills','algorithms','data structures','Git','testing','architecture'],
}

def curriculum(): return [asdict(x) for x in LESSONS]
def total_lessons(): return len(LESSONS)
def role_skills(): return ROLE_SKILLS

def mastery_requirements():
    return {
        'all_core_curriculum': True,
        'all_graphics_curriculum': True,
        'all_3d_curriculum': True,
        'all_animation_curriculum': True,
        'portfolio_projects': 4,
        'tested_projects': True,
        'git_workflow': True,
        'code_review': True,
        'debugging': True,
        'security': True,
        'deployment': True,
        'technical_interview': True,
        'capstone': True,
    }
