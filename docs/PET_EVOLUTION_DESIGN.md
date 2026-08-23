# Pypet Egg-to-Evolution Design

Pypet does not have one canonical starting pet or one canonical final pet.

## Lifecycle

Every pet begins as an egg. The egg hatches into a randomized baby pet with a unique generated name. Each evolution replaces the current form with a **new pet form**, while the previous form is recorded in the player's Pet Collection.

Example:

`Egg -> Baby A -> Young Pet B -> Adult C -> Advanced C1/C2/C3`

The exact branch is determined by the pet's development history rather than a fixed linear path.

## Development inputs

Evolution uses multiple dimensions:

- Hunger/nutrition
- Health
- Happiness
- Traditional care/grooming
- Food choices
- Play
- School attendance
- Python lessons completed
- Exploration/world activity
- Streak/routine consistency

No single activity should dominate every evolution path.

## Multiple species and branches

The system should support many egg types and many species, with multiple possible forms at every major stage. New species and variants can be added without changing the core evolution engine.

Examples of possible outcomes include Code Dragon, Pixel Pouncer, Clover Guardian, Giggle Sprite, Starlight Scholar, and future branches. These are examples, not exclusive endpoints.

## New-pet-on-evolution rule

When an evolution occurs:

1. Preserve the old pet form in the Pet Collection.
2. Create the next form as a new pet record with its own identity.
3. Carry forward appropriate inherited traits/history without making the new form identical to the old one.
4. Give the evolved pet a distinct name if required by the naming rules.
5. Show an evolution celebration that remains sensory-safe: no flashing, strobe, screen shake, or rapid repeated effects.
6. Continue care, school, play and world progression with the new pet.

This allows a player to build a collection of every pet they have raised while still having one active companion at a time.

## School integration

Python learning is represented as the pet attending school in its world. Lessons increase learning/academic development and can influence evolution, but school is only one input among care, hunger, happiness, health, play and exploration.

## Design principle

The player is raising a living-feeling game character through choices. The objective is discovery and relationship, not grinding toward one best pet.
