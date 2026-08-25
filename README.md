# PyPet

PyPet is a multi-pet, learn-by-doing Python academy for Android. The product name is intentionally styled **PyPet** as a programmer joke: Python + Pet, with the capitalization resembling a familiar programming identifier while remaining a friendly game name.

PyPet is a standalone project and is **not** part of the separate `Chat-gpt-epilepsy-filters` repository.

## Core idea

**Learn Python by programming your pet.** Every lesson introduces a real Python concept, has the learner write real Python, executes it on-device, shows the effect on the pet, explains the result, then gives an experiment and challenge.

## Virtual world and care

Pets live together in a persistent, full-screen virtual town with road-planned homes, a clearly identifiable park, market, grooming/clinic-style care routines, academy, library and workshop locations. Building taps launch real activities rather than description-only popups. Pet care includes food, water, sleep, play, grooming, happiness, energy and hunger. The world also contains persistent decorations and accomplishment trophies.

World placement uses the same coordinate bounds as the rendered town and automatically keeps decorations inside the map and off roads. Decorations have identifiable silhouettes instead of placeholder rectangles. Trophies can be displayed in-world for visible bragging rights.

The Android world includes a persistent multi-pet collection, hatchery, lineage-based evolution, named evolutionary forms, common/uncommon/rare/mythic outcomes, and balanced development. Evolution weighs the nine development categories equally: nutrition/hunger, happiness, health, lessons, play, care, school, exploration and routine. The learner can build different lineages through different combinations of real gameplay and Python learning while keeping earlier pets in the collection.

## Calm presentation: animation and sound

PyPet includes gentle, non-flashing pet animation and an original synthesized happy background tune. Feeding, playing, learning, lesson completion, rewards and purchases can use short, quiet interaction tones. Music is user-toggleable and now pauses with the Android activity lifecycle. Audio is deliberately low-volume and avoids abrupt/loud effects. There is no intentional strobing, screen shaking, or reward haptics.

Animation and audio are presentation features, not medical protection. PyPet is not a medical device and cannot guarantee seizure prevention.

## Curriculum scope

The curriculum progresses from beginner to advanced Python and includes syntax, control flow, data structures, functions, exceptions, modules/packages, object-oriented programming, the Python data model, files/data, iterators/generators, context managers, typing, standard-library programming, networking, concurrency/asyncio, testing/debugging, packaging/professional Python, GUI/game development, 3D concepts, third-party ecosystem orientation, job readiness and a capstone programmable pet project.

OOP explicitly covers classes, objects, inheritance, composition, polymorphism, properties, dataclasses, abstract concepts and protocols. The ecosystem track introduces Pillow, NumPy, pandas, Matplotlib, Requests/httpx, Flask, FastAPI, SQLAlchemy, scikit-learn, PyTorch, Ruff, Black and mypy/Pyright as professional ecosystem choices. These ecosystem lessons are orientation/planning lessons unless a library is explicitly bundled into the Android build.

The job-readiness path adds Git/GitHub, code review, documentation, architecture, portfolio work, technical interviews, debugging interviews, presentation and a final professional capstone. Graduation is based on demonstrated competency rather than lesson count.

## Pets

PyPet supports a catalog model rather than a single character. The initial catalog includes dogs, cats, foxes, rabbits, dragons, owls, robots, axolotls, penguins and dinosaurs. A unicorn is a guaranteed core pet and is never an ad-gated reward. Pets have species, names, personalities, needs, inventory, knowledge and XP, and their behavior can be driven by learner-written Python.

## Reward Center and optional sponsored items

The player-facing Reward Center contains **40 original decorations**, including the core coin collection and a dedicated ad-accelerated collection. Rewarded ads are optional and explicitly opted into one at a time. A completed rewarded ad grants **25 Pypet Coins**. Coins can be spent on exclusive World decorations that are persisted and displayed in the town. Ads do not gate Python lessons, pet care, ordinary pets, the unicorn, required world areas or saved progress.

Development/debug builds use Google's rewarded-ad test unit. Release builds use the configured production rewarded-ad unit.

## Google Play purchases

Premium/Treasure purchases use Google Play Billing and fail closed until a trusted HTTPS purchase-verification endpoint is configured. The verifier checks the package name, product allowlist and Google Play purchase state, rejects already-consumed purchases, and acknowledges eligible purchases server-side. The Android client never receives the service-account key.

The repository contains serverless verifiers at `api/purchase-verify.js` and `api/google-play/verify-purchase.js`. They expect `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` as a deployment secret. See `docs/PLAY_RELEASE_SETUP.md` and `docs/PURCHASE_VERIFICATION.md` for the remaining Google Play and hosting configuration steps.

## Sensory safety

Pet presentation is deliberately calm: no intentional strobing, screen shaking or reward haptics. Animation requests are passed through a safety guardrail. This is a risk-reduction design and **not** a medical device or guarantee against seizures.

## Android

The Android application lives under `android/`. It embeds Python 3.13 with Chaquopy and has a GitHub Actions workflow which runs the Python tests, verifies real building routing, verifies World placement/reward/purchase acceptance requirements, builds the debug APK, verifies it, builds the production release APK, verifies it, and uploads both APK artifacts. The current beta build is `0.1.0-beta6` / version code `7`.

The Academy Java/Python bridge uses Chaquopy `PyObject`; Android CI must build from the current `main` commit rather than an earlier stale failing build.

## Product naming convention

**PyPet** is the user-facing product name. Source packages and Java identifiers remain lowercase/camel/Pascal case as required by their programming languages; this preserves conventional code style while keeping the product's intentional **PyPet** branding visible to players.
