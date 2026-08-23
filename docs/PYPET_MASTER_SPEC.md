# Pypet Master Specification

Status: **LOCKED BASELINE**

This document is the canonical product baseline for Pypet. New implementation work must preserve earlier agreed systems unless a newer decision explicitly supersedes them.

## 1. Product identity

Pypet is an immersive Android virtual-pet game and structured Python-learning academy. The player raises living pets, explores a persistent world, learns real Python, builds projects, develops job-ready skills, and grows confidence through demonstrated achievement.

The educational curriculum is not replaced by gameplay. The curriculum remains structured: **teach -> demonstrate -> practice -> challenge -> apply -> master**. The world supplies motivation and authentic application.

## 2. Pet body and life

Pets are full creatures rather than static faces. The production design must support a recognizable body, locomotion, posture, facial expression, eating, drinking, sleeping, bathing/showering, grooming, playing, exercising and environmental reactions. Species and evolutionary forms may differ in body proportions, appendages, markings and movement.

Core care includes food, water, sleep, play, grooming, cleanliness, happiness, energy, hunger, health, routine and comfort. Food includes ingredients, prepared foods, snacks and drinks; the kitchen and bathroom are interactive world spaces rather than menu-only actions.

## 3. Immersive world

The World is the primary gameplay surface. Home, kitchen, bathroom, bedroom, yard/garden, academy, library, workshop, market, park, hatchery, nature areas and future regions are represented as places or meaningful world landmarks. Objects should have purposes and interactions. Pets can move, react, explore and act autonomously.

The world should feel lived-in: day/night, calm weather variation, NPC activity, discoveries, gardening, cooking, grooming, exploration, toys, decorations, keepsakes and persistent changes are preferred over isolated menu screens.

## 4. Hidden lineage and evolution

Lineage/genetic recipes are **never shown to the player**. Internal lineage, inherited traits, mutation state, ancestry and rarity calculations remain implementation data. The player experiences the resulting appearance, behavior and surprise.

Evolution is multi-generational. Development is balanced across the nine agreed categories: nutrition/hunger, happiness, health, lessons/Python learning, play, care, school, exploration and routine. Each category has equal weight in the evolution score. Rare/uncommon/mythic outcomes are rolled separately and should not become predictable recipes.

Past pets and previous evolutions are permanent history. A pet collection/memory system retains prior pets, names, important accomplishments and memories. Descendants can carry recognizable traits without exposing their lineage formula. Keepsakes, photographs, trophies and world objects can reference past pets.

## 5. Structured Python academy

The curriculum remains comprehensive and sequential. It covers Python fundamentals, syntax, variables/types, control flow, collections, functions, errors/exceptions, modules/packages, OOP, Python data model, files/data, iterators/generators, context managers, typing, standard library, networking, concurrency/asyncio, testing/debugging, packaging/professional Python and third-party ecosystem orientation.

Hands-on tracks include graphics, 2D/3D animation, professional software engineering, algorithms/data structures, APIs, databases, security, deployment/CI, architecture, performance, code review and capstone development. The language curriculum remains real Python executed on-device where supported.

The player must be able to progress without being forced to buy ads or premium content for core lessons.

## 6. Learning through the world

After a concept is taught conventionally, the player can apply it in the world. Examples include using variables for pet needs, conditionals for pet behavior, loops for garden automation, lists for inventory, functions for routines, classes for pet/world objects, and projects for automation or simulations.

Open-ended programming is an advanced layer, not a replacement for lessons.

## 7. Job readiness

Pypet includes a career-readiness path built on demonstrated competency. It develops communication, professional writing, problem solving, teamwork, time management, digital literacy, research, project planning, coding practices, portfolios, interview practice, take-home simulations and capstone delivery. Graduation is based on demonstrated competency rather than lesson count.

The game supports practice and preparation; it must not claim that game completion guarantees employment.

## 8. Confidence, uplifting and wellbeing

The game intentionally reinforces persistence, learning from mistakes, self-efficacy, healthy routines, manageable goals, creativity, reflection and asking for help. Confidence should be earned through visible accomplishments rather than arbitrary praise or a misleading clinical score.

Pypet is not a medical device and does not diagnose or treat mental-health conditions. Missing a day should not cause destructive punishment. Returning to the game should be welcoming.

## 9. Achievements, trophies and streaks

Trophies recognize meaningful accomplishments across pet care, Python, exploration, projects, persistence and evolution. Streaks can reward consistency but must avoid harsh loss mechanics. Rewards should encourage returning and learning rather than fear of breaking a streak.

## 10. Economy, rewarded ads and purchases

Normal gameplay provides meaningful progression. Pypet Coins and cosmetic world items can be earned through gameplay. Rewarded ads are optional and only accelerate the separate cosmetic reward economy. Core pets, the unicorn, Python lessons, care and required world areas are not ad-gated.

Google Play purchases remain fail-closed until trusted HTTPS verification is configured. Purchase verification must validate package name, product allowlist and Play purchase state before granting ownership.

## 11. Accessibility and sensory safety

Presentation remains calm by design: no intentional strobing, rapid flashing, screen shaking or reward haptics. Reduced-motion and audio controls remain available. Safety features are risk-reduction design and are not a medical guarantee.

## 12. Production interface rule

Developer/debug controls are development-only. The production/release build must present only the player-facing game, learning, world, care, collection, achievements, rewards, accessibility and account/purchase surfaces. No developer debug panel, debug practice runner, developer trophy button, developer treasure button or debug reward controls may be visible in the release UI.

The current Android activity already guards its developer controls with `BuildConfig.DEBUG`; this rule must remain enforced as production UI evolves.

## 13. Release acceptance

A release candidate is not considered complete until:

- Python tests pass.
- Android compilation succeeds.
- The APK is verified and uploaded by CI.
- Production UI contains no developer/debug controls.
- Core world navigation works.
- Pet care works.
- Pet collection/past-pet history works.
- Evolution remains multi-generational and lineage-hidden.
- The structured Python academy remains intact.
- Job-readiness/capstone progression remains accessible.
- Trophies and streaks work.
- Rewarded-ad behavior remains optional and nonessential.
- Purchase verification remains fail-closed.
- Sensory-safety guardrails remain enabled.

This is the locked baseline for subsequent Pypet implementation and QA work.
