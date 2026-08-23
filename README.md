# Pypet

Pypet is a multi-pet, learn-by-doing Python academy for Android. It is a standalone project and is **not** part of the separate `Chat-gpt-epilepsy-filters` repository.

## Core idea

**Learn Python by programming your pet.** Every lesson introduces a real Python concept, has the learner write real Python, executes it on-device, shows the effect on the pet, explains the result, then gives an experiment and challenge.

## Virtual world and care

Pets live together in a persistent virtual world with homes, parks, gardens, market, grooming, clinic, academy, library and workshop locations. Pet care includes food, water, sleep, play, grooming, happiness, energy and hunger. The world can also contain optional exclusive decorations.

## Calm presentation: animation and sound

Pypet now includes gentle, non-flashing pet animation and an original synthesized happy background tune. Feeding, playing, learning, lesson completion, rewards and purchases can use short, quiet interaction tones. Music is user-toggleable and stops when the app is paused. Audio is deliberately low-volume and avoids abrupt/loud effects. There is no intentional strobing, screen shaking, or reward haptics.

Animation and audio are presentation features, not medical protection. Pypet is not a medical device and cannot guarantee seizure prevention.

## Curriculum scope

The curriculum progresses from beginner to advanced Python and includes syntax, control flow, data structures, functions, exceptions, modules/packages, object-oriented programming, the Python data model, files/data, iterators/generators, context managers, typing, standard-library programming, networking, concurrency/asyncio, testing/debugging, packaging/professional Python, third-party ecosystem orientation, and a capstone programmable pet project.

OOP explicitly covers classes, objects, inheritance, composition, polymorphism, properties, dataclasses, abstract base classes and protocols. The third-party-library track distinguishes the Python language from the standard library and external ecosystem and introduces Pillow, Pygame, NumPy, pandas, Matplotlib, Requests/httpx, Flask, FastAPI, SQLAlchemy, scikit-learn, PyTorch, Ruff, Black and mypy/Pyright.

## Pets

Pypet supports a catalog model rather than a single character. The initial catalog includes dogs, cats, foxes, rabbits, dragons, owls, robots, axolotls, penguins and dinosaurs. A unicorn is a guaranteed core pet and is never an ad-gated reward. Pets have species, names, personalities, needs, inventory, knowledge and XP, and their behavior can be driven by learner-written Python.

## Reward Center and optional sponsored items

The Reward Center contains optional, non-transferable exclusive world decorations. The current catalog contains 22 items: Aurora Tree, Rainbow Garden Arch, Tiny Castle, Pet Carousel, UFO Landing Pad, Wizard's Workshop, Miniature Train, Floating Island, Tiny Fantasy Volcano, Unicorn Fountain, Dragon-Roost Tower, Robot Charging Station, Magical Flower Garden, Giant Floating Library, Planetarium, Artist's Studio, Observatory, Campground, Rainbow Bridge, Moon Garden, Star Gazebo and Crystal Fountain.

Rewarded ads are optional and explicitly opted into one at a time. Ads only unlock these exclusive decorations; they do not gate Python lessons, pet care, ordinary pets, the unicorn, required world areas or saved progress. Rewards are non-transferable and remain inside Pypet. Development builds use Google's rewarded-ad test unit; the configured production unit is used only outside debug builds. A completed rewarded ad grants 25 Pypet Coins, which the learner can spend on exclusive items.

## Sensory safety

Pet presentation is deliberately calm: no intentional strobing, screen shaking or reward haptics. Animation requests are passed through a safety guardrail. This is a risk-reduction design and **not** a medical device or guarantee against seizures.

## Android

The Android application lives under `android/`. It embeds Python 3.13 with Chaquopy and has a GitHub Actions workflow which builds and uploads a debug APK artifact.
