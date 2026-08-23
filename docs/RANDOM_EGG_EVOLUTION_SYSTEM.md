# Random Egg Evolution System

## Product rule
Every egg is a new randomized pet journey. There is no single starting pet and no single final pet.

When an egg hatches, the game randomly selects a starting species/lineage from the available egg pool. Each lineage has multiple possible evolutionary outcomes. Player behavior then influences which branch is reached.

## Core loop
1. Discover or receive a Mystery Egg.
2. Generate a unique pet identity: species lineage, name, personality seeds and appearance seed.
3. Hatch into a randomized baby pet.
4. Raise the pet through traditional care: food, hunger, health, grooming/care, happiness and play.
5. Send the pet to school in the world; Python lessons contribute to learning/intelligence development.
6. Exploration, routines and achievements provide additional developmental influences.
7. When an evolution threshold is reached, the current form evolves into a **new pet form** and the previous form is preserved in the collection.
8. The player receives/records the newly evolved pet and continues raising that new form.
9. Repeat through multiple branches. A pet line may have several advanced forms and no terminal universal form.
10. New eggs can be obtained so players build a large, varied Pet Collection over the full game.

## Randomization requirements
- Eggs must not all resolve to the same species.
- Each egg gets independent randomness for lineage, appearance seed and name selection.
- Evolution outcomes use weighted/random branch selection constrained by the pet's development traits, so care matters but outcomes are not completely deterministic.
- Never make a paid item guarantee a particular evolution.
- Avoid a single optimal evolution path.
- Preserve enough seed/state to make a pet's lineage reproducible after save/restore.

## Development influences
- Hunger/nutrition
- Health/vitality
- Grooming and care
- Happiness
- Playfulness
- School attendance
- Python lessons and lesson mastery
- Exploration/curiosity
- Routine/streak behavior

These should combine rather than use one generic XP bar.

## Collection rule
Evolution creates a new collectible form. The previous form remains in the player's encyclopedia/collection and is not deleted.

Example:

🥚 Mystery Egg #17
→ 🐣 Ember Hatchling
→ 🦊 Ember Fox
→ 🐉 Ember Code Dragon
→ ✨ Ember Star Dragon

A different egg might produce:

🥚 Mystery Egg #18
→ 🐣 Moss Hatchling
→ 🐰 Moss Garden Bunny
→ 🦌 Grove Guardian

Both journeys coexist in the collection.

## World presentation
The world should show:
- the currently active pet prominently;
- other owned pets available in the player's collection/home/world;
- NPC/wild/world pets separately;
- egg discoveries and evolution moments as celebratory but sensory-safe events.

The active pet can attend school, eat, play, receive care and explore. Other owned pets can be displayed in habitats/world locations and later swapped into the active slot.

## Safety
Evolution celebrations must use gentle animation/audio only. No intentional flashing, strobing, screen shake, rapid flicker or haptic reward effects. Reduced-motion mode must be respected.

## Monetization boundary
Paid Treasure Trove items should primarily be cosmetic/world customization. Do not sell guaranteed evolution outcomes or essential pet care/learning progress.