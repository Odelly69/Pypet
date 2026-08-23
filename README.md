# Pypet

Pypet is a multi-pet, learn-by-doing Python academy for Android. It is a standalone project and is **not** part of the separate `Chat-gpt-epilepsy-filters` repository.

## Core idea

**Learn Python by programming your pet.** Every lesson introduces a real Python concept, has the learner write real Python, executes it on-device, shows the effect on the pet, explains the result, then gives an experiment and challenge.

## Virtual world and care

Pets live together in a persistent virtual world with homes, parks, gardens, market, grooming, clinic, academy, library and workshop locations. Pet care includes food, water, sleep, play, grooming, happiness, energy and hunger. The world can also contain optional exclusive decorations.

The Android world includes a persistent multi-pet collection, hatchery, lineage-based evolution, named evolutionary forms, common/uncommon/rare/mythic outcomes, and balanced development. Evolution weighs the nine development categories equally: nutrition/hunger, happiness, health, lessons, play, care, school, exploration and routine. The learner can build different lineages through different combinations of real gameplay and Python learning while keeping earlier pets in the collection.

## Calm presentation: animation and sound

Pypet now includes gentle, non-flashing pet animation and an original synthesized happy background tune. Feeding, playing, learning, lesson completion, rewards and purchases can use short, quiet interaction tones. Music is user-toggleable and stops when the app is paused. Audio is deliberately low-volume and avoids abrupt/loud effects. There is no intentional strobing, screen shaking, or reward haptics.

Animation and audio are presentation features, not medical protection. Pypet is not a medical device and cannot guarantee seizure prevention.

## Curriculum scope

The curriculum progresses from beginner to advanced Python and includes syntax, control flow, data structures, functions, exceptions, modules/packages, object-oriented programming, the Python data model, files/data, iterators/generators, context managers, typing, standard-library programming, networking, concurrency/asyncio, testing/debugging, packaging/professional Python, third-party ecosystem orientation, and a capstone programmable pet project.

OOP explicitly covers classes, objects, inheritance, composition, polymorphism, properties, dataclasses, abstract base classes and protocols. The third-party-library track distinguishes the Python language from the standard library and external ecosystem and introduces Pillow, Pygame, NumPy, pandas, Matplotlib, Requests/httpx, Flask, FastAPI, SQLAlchemy, scikit-learn, PyTorch, Ruff, Black and mypy/Pyright.

The expanded job-readiness path incorporates the earlier curriculum and adds practical algorithms/data structures, APIs, databases, security, deployment/CI, code review, architecture, performance, portfolios, interview practice, a take-home simulation and a final capstone. Graduation is based on demonstrated competency rather than lesson count.

## Pets

Pypet supports a catalog model rather than a single character. The initial catalog includes dogs, cats, foxes, rabbits, dragons, owls, robots, axolotls, penguins and dinosaurs. A unicorn is a guaranteed core pet and is never an ad-gated reward. Pets have species, names, personalities, needs, inventory, knowledge and XP, and their behavior can be driven by learner-written Python.

## Reward Center and optional sponsored items

The Reward Center contains optional, non-transferable exclusive world decorations. The Android reward catalog currently contains **40 original decorations**, including the core coin collection and a dedicated ad-accelerated collection. Rewarded ads are optional and explicitly opted into one at a time. Ads only provide an accelerated way to earn Pypet Coins for these exclusive decorations; they do not gate Python lessons, pet care, ordinary pets, the unicorn, required world areas or saved progress. Rewards are non-transferable and remain inside Pypet.

Development/debug builds use Google's rewarded-ad test unit. Release builds use the configured production rewarded-ad unit. A completed rewarded ad grants 25 Pypet Coins, which the learner can spend on exclusive items.

## Google Play purchases

Premium Treasure purchases use Google Play Billing and fail closed until a trusted HTTPS purchase-verification endpoint is configured. The verifier checks the package name, product allowlist and Google Play purchase state before granting ownership, and the client acknowledges verified purchases. The Android release build receives the verifier URL through the `pypetPurchaseVerifyUrl` Gradle property so no production endpoint needs to be hard-coded into source.

The repository contains the serverless verifier at `api/purchase-verify.js`. It expects `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` as a deployment secret. See `docs/PLAY_RELEASE_SETUP.md` for the remaining Google Play and hosting configuration steps.

## Sensory safety

Pet presentation is deliberately calm: no intentional strobing, screen shaking or reward haptics. Animation requests are passed through a safety guardrail. This is a risk-reduction design and **not** a medical device or guarantee against seizures.

## Android

The Android application lives under `android/`. It embeds Python 3.13 with Chaquopy and has a GitHub Actions workflow which runs the Python tests, builds the debug APK, verifies that the APK exists, and uploads it as an Actions artifact. The current beta build is `0.1.0-beta5` / version code `6`.

The Academy Java/Python bridge uses Chaquopy `PyObject`; Android CI must build from the current `main` commit rather than the earlier stale failing build.
