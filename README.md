# Pypet

Pypet is a multi-pet, learn-by-doing Python academy for Android. It is a standalone project and is **not** part of the separate `Chat-gpt-epilepsy-filters` repository.

## Core idea

**Learn Python by programming your pet.** Every lesson introduces a real Python concept, has the learner write real Python, executes it on-device, shows the effect on the pet, explains the result, then gives an experiment and challenge.

## Curriculum scope

The curriculum progresses from beginner to advanced Python and includes syntax, control flow, data structures, functions, exceptions, modules/packages, object-oriented programming, the Python data model, files/data, iterators/generators, context managers, typing, standard-library programming, networking, concurrency/asyncio, testing/debugging, packaging/professional Python, third-party ecosystem orientation, and a capstone programmable pet project.

OOP explicitly covers classes, objects, inheritance, composition, polymorphism, properties, dataclasses, abstract base classes and protocols. The third-party-library track distinguishes the Python language from the standard library and external ecosystem and introduces Pillow, Pygame, NumPy, pandas, Matplotlib, Requests/httpx, Flask, FastAPI, SQLAlchemy, scikit-learn, PyTorch, Ruff, Black and mypy/Pyright.

## Pets

Pypet supports a catalog model rather than a single character. The initial catalog includes dogs, cats, foxes, rabbits, dragons, owls, robots, axolotls, penguins and dinosaurs. Pets have species, names, personalities, needs, inventory, knowledge and XP, and their behavior can be driven by learner-written Python.

## Sensory safety

Pet presentation is deliberately calm: no intentional strobing, screen shaking or reward haptics. Animation requests are passed through a safety guardrail. This is a risk-reduction design and **not** a medical device or guarantee against seizures.

## Android

The Android application lives under `android/`. It embeds Python 3.13 with Chaquopy and has a GitHub Actions workflow which builds and uploads a debug APK artifact.
