# Pypet World — Master Game Design

## Core identity
Pypet is a world-heavy virtual-pet adventure in which Python learning is an in-world school activity. The player raises, explores with, and learns alongside pets rather than navigating a utility app.

## World-first home
The first screen is the pet's world. The active pet is visible in the environment. Major destinations are represented as world locations, not merely menu buttons:
- Home / habitat — sleep, food, care, customization
- Academy / school — attend class and open Python lessons
- Park / playground — play and social interaction
- Market — food and ordinary items
- Hatchery — discover and hatch eggs
- Wilds — exploration and discovery
- Pet clinic — health/care activities
- Treasure Trove — optional cosmetic/world purchases
- Trophy Hall — achievements and streaks
- Festival/event areas — rotating safe activities

## Active pet and collection
The player can own many pets. One is active and accompanies the player. Other owned pets can live in habitats/world locations and can be swapped into the active slot. NPC/wild pets are distinct from owned pets.

## Egg generation
Every new pet begins with a unique randomized egg. Egg generation independently seeds appearance, markings, rarity, name, lineage and development variation. The lineage is hidden until hatching. There is no single universal starting pet.

Example lineage families include Fox, Unicorn, Dragon, Cat, Wolf, Bunny, Turtle, Deer, Bear, Frog, Bird, Fae and Nature/Spirit. Each family has multiple branches and forms.

## Evolution
Evolution creates a new collectible pet form; previous forms remain in the encyclopedia/collection. Evolution is influenced by multiple development dimensions rather than a single XP bar:
- nutrition/hunger
- health/vitality
- care/grooming
- happiness
- playfulness
- school attendance
- Python lesson participation/mastery
- exploration/curiosity
- routine/streak behavior

Controlled randomness is combined with these traits so players influence outcomes without every evolution being perfectly predictable. There is no single strongest or universal final pet.

## School and Python
The pet physically goes to school in the world. Entering school opens the current lesson. Completing a lesson improves learning/development and can affect evolution, while also awarding ordinary game rewards such as coins, school progress, trophies or streak progress.

Python curriculum should progress from beginner concepts to projects, but always be presented as the pet's education.

## Food and resources
Food can be earned through care, school, play, exploration, streaks and events; discovered in the world; purchased with earned in-game currency; or offered through optional rewarded ads. Food affects hunger and can also affect happiness/health. Paid purchases should not be required for essential care or learning.

## Monetization boundary
Google Play purchases are optional. Treasure Trove should emphasize cosmetic pets/world items, decorations, outfits and other non-essential content. High-value purchases require authoritative server-side Google Play purchase-token verification before entitlement is granted. Do not sell guaranteed evolution outcomes.

## Audio/animation
Use a cheerful, cozy background tune plus gentle pet/location sounds. Animations should be subtle and readable. No intentional flashing, strobing, rapid flicker, screen shake or reward haptics. Reduced-motion mode must disable/reduce animation and be respected by all world/evolution effects.

## HD art direction
Target polished HD game art with a cohesive, colorful virtual-pet aesthetic. The world should use layered backgrounds, expressive pets, readable landmarks, collectible objects and location-specific visual identity. Keep interactive elements large and uncluttered on Android phones.

## Main player loop
Discover egg → hatch → name/meet pet → care/feed → play → visit school → complete Python lesson → explore → earn/discover food and rewards → influence development → evolve → add new pet form to collection → switch active pet → discover another egg.

## Product principle
The player should always feel: "I am taking care of my pet and exploring its world." Python is a meaningful activity inside that world, not the reason the app looks like an IDE.