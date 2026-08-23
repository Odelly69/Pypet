"""Canonical full-spectrum Pypet Academy index.

All previously defined tracks remain part of the course: core Python,
standard library, Tkinter, Pygame, 2D graphics, coordinate/resolution mapping,
3D graphics, animation, World building, and professional/job-readiness skills.
"""
from pypet_curriculum import curriculum as core_curriculum, STANDARD_LIBRARY, ECOSYSTEM
from pypet_3d_curriculum import curriculum as graphics3d_curriculum
from pypet_animation_curriculum import curriculum as animation_curriculum
from pypet_career_curriculum import curriculum as career_curriculum, mastery_requirements, role_skills

TRACKS = {
    'python_core': core_curriculum,
    'graphics_3d': graphics3d_curriculum,
    'animation': animation_curriculum,
    'career_job_readiness': career_curriculum,
}

def curriculum():
    rows = []
    for name, provider in TRACKS.items():
        for lesson in provider():
            item = dict(lesson)
            item['track'] = name
            rows.append(item)
    return rows

def total_lessons():
    return len(curriculum())

def course_contract():
    return {
        'full_spectrum_python': True,
        'all_previous_curriculum': True,
        'standard_library': list(STANDARD_LIBRARY),
        'ecosystem': list(ECOSYSTEM),
        '2d_graphics': True,
        'coordinate_mapping': True,
        'resolution_independence': True,
        '3d_graphics': True,
        'animation': True,
        'game_development': True,
        'hands_on_learning': True,
        'skill_adaptive_entry': True,
        'portfolio_based_mastery': True,
        'job_readiness_track': True,
        'mastery_requirements': mastery_requirements(),
        'target_roles': role_skills(),
        'employment_guarantee': False,
    }

# Completion means demonstrated competence, not merely lesson consumption.
COMPLETION_GATES = [
    'demonstrate_core_python',
    'demonstrate_standard_library',
    'demonstrate_debugging_and_testing',
    'demonstrate_2d_graphics_and_coordinate_mapping',
    'demonstrate_tkinter',
    'demonstrate_pygame',
    'demonstrate_3d_graphics',
    'demonstrate_animation',
    'demonstrate_git_and_professional_workflow',
    'demonstrate_security_and_quality',
    'complete_portfolio_projects',
    'pass_practical_interview_lab',
    'complete_take_home_simulation',
    'ship_and_defend_capstone',
]
