# Pypet Final Acceptance Baseline

Status: LOCKED

This document is the implementation acceptance checklist for the unified Pypet design. New features must extend this baseline without removing previously locked requirements.

## Active-pet lifecycle
- Exactly one pet is the active care/learning subject at a time.
- Feeding, bathing, play, education, routines, health and development are scoped to the active pet.
- A growth/evolution milestone can award the next egg without deleting/replacing the current pet.
- Past and final-evolution pets remain persistent world residents.
- Final-evolution pets no longer require care.
- Resident pets can autonomously move, socialize and use compatible world mechanics.

## Hidden evolution
- Lineage, genetic recipe, percentages and internal ancestry are never shown to the player.
- Eggs and descendants remain genuine surprises.
- Evolution can produce inherited physical/behavioral traits and rare outcomes.
- All major development paths contribute with balanced weighting; no single activity is the only route to evolution.

## Living pet/body
- Pets require a real multi-part body model, not emoji-only representation.
- Body proportions, limbs, head, eyes, ears, tail where applicable, markings and movement are renderable traits.
- Animations cover idle, walk/run, eat, drink, sleep, groom, bathe, play, interact, react and social behaviors.

## Immersive world
- World is the primary gameplay surface.
- Home has usable rooms including kitchen, bathroom, bedroom and living areas.
- Food preparation, eating, drinking, shower/bath, grooming, sleep and play are physical world interactions.
- Outdoor areas include yard/garden and expandable exploration regions.
- Objects have functional interactions rather than being menu-only placeholders.

## Playground
- Player builds the playground from individually acquired objects.
- Acquisition families are distinct: progression, earned, purchased, rewarded-ad exclusive.
- Families do not merely duplicate the same item at different prices.
- Equipment includes a conventional swing set and a separate vintage rigid-arm pump swing.
- Also includes slide, seesaw, merry-go-round/spinner, spring rider, climbing equipment, overhead equipment, play tower, benches, shade and nature-play options.
- Equipment has original designs, safe collision geometry and calm interaction animations.
- Multiple pets may interact with compatible equipment; the active pet remains the only pet whose care/development state is directly managed.

## Trophies
- Accomplishments produce physical trophy objects.
- Trophy cabinet exists in the player's home/world.
- Trophy designs are individually authored for the accomplishment and inspired by broad real-world award traditions without copying a specific real trophy.
- Trophies remain persistent and inspectable.
- Trophy displays do not reveal hidden lineage.

## Python education
- Structured curriculum remains intact: teaching, examples, practice, challenges, application and mastery.
- Covers fundamentals through advanced Python, debugging, testing, data structures, OOP, algorithms and project work.
- World challenges apply learned Python concepts to meaningful game systems.
- Core education is not paywalled and does not require advertisements.

## Life/career development
- Job readiness includes communication, problem solving, teamwork, time management, professional writing, portfolio/project work and interview preparation.
- Confidence grows from demonstrated accomplishments and persistence, not arbitrary praise meters alone.
- Uplifting/wellbeing systems encourage healthy routines, breaks, reflection, resilience and returning after absence without punitive streak loss.

## Economy
- Progression, earned play, optional purchases and optional rewarded ads all have roles.
- Ads/purchases enhance cosmetics/world building and optional rewards; they do not gate essential care or Python education.

## Safety/accessibility
- No intentional flashing/strobing, rapid screen shake or unsafe reward haptics.
- Reduced-motion and audio/haptic controls remain available.
- World/equipment animations must use calm, configurable timing.

## Release acceptance
- Developer/debug controls must not be visible in release builds.
- Debug-only interfaces must remain behind BuildConfig.DEBUG or equivalent release-safe gating.
- Python tests pass.
- Android debug and release builds compile.
- APKs are verified as valid artifacts.
- Final release CI must pass before production distribution is claimed complete.
