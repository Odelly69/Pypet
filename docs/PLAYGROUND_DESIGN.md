# Pypet Playground Design

The playground is a persistent living-world location that the **player builds over time**. It starts as an available play area rather than a finished park. Equipment is placed individually by the player as pieces are acquired through Pypet's existing progression, achievement/earning, purchase, and optional rewarded-ad systems.

The four acquisition routes intentionally provide different item families rather than four ways to obtain the same catalog:

- **Progression:** practical equipment that arrives through balanced development and world progression.
- **Earned:** distinctive achievement, Python/project, exploration, trophy, and skill-themed equipment earned through play.
- **Purchase:** optional premium/customizable equipment purchased with normal in-game currency.
- **Rewarded ad:** optional exclusive novelty equipment/items obtained through the separate rewarded-ad currency. These are never required for Python education or core pet care.

No route is mandatory for the core educational experience.

## Core equipment

Progression family:

1. Picnic bench — social/rest landmark
2. Spring rider — rocking/balance play
3. Standard swing set — classic hanging swings
4. Slide — climbing/descending play
5. Seesaw — cooperative balance
6. Merry-go-round — multi-pet social spinner
7. Climbing frame — routes and exploration
8. Overhead bars — coordination/persistence
9. Play tower — connected climbing/bridge/slide system
10. Nature play area — logs, stepping stones and discovery

## Distinctive acquired equipment

Earned family includes items such as:

- Storytelling bench
- Logic climber
- Maker spring rider
- Explorer balance trail

Purchase family includes items such as:

- Garden canopy swing
- Castle slide
- Garden carousel

Rewarded-ad exclusive family includes items such as:

- **Vintage rigid-arm pump swing** — intentionally modeled as an old-school mechanical/arm swing category, distinct from a normal chain swing and not a water pump
- Star spinner
- Discovery tunnel

The vintage pump swing is specifically retained as its own item. It uses a rigid-arm pumping motion with grip, forward/back pumping, coasting and controlled slowdown.

## Building the playground

A player does not receive a finished playground. They acquire individual pieces, then place/build them in the available playground area. Future construction can include paths, fencing, shade, landscaping, benches, accessibility routes, and constrained player-designed layouts.

Items can arrive as complete equipment or, where appropriate, as construction components. A future construction UI can allow frame/seat/handle/etc. components to be assembled before placement.

## Animation design

Each equipment item has an explicit calm animation choreography. Animations include approach, mount/enter, activity cycle, pause, slowdown, and exit/dismount where appropriate.

Examples:

- Standard swing: mount → forward arc → backward arc → slow → dismount
- Vintage pump swing: approach → grip → pump forward → pump back → coast → slow → dismount
- Slide: walk → climb → sit → slide → land → optional celebration
- Seesaw: mount → tilt down → tilt up → balance → dismount
- Merry-go-round: mount → slow rotation → pause → slow rotation → dismount
- Climber: grip → climb → pause/look → route choice → descend
- Overhead bars: grip → reach → pull → pause → traverse → dismount
- Nature trail: step → balance → observe → discover → leave

Animations are original Pypet designs inspired by real playground categories, not copied manufacturer products. Rendering must remain calm: no intentional flashing/strobing, no rapid camera effects, and reduced-motion settings must be respected.

## Living-world pet rules

The player raises and works with **one active pet at a time**. Care, education, activities, needs, and personal development are scoped to that active pet.

When the active pet grows/evolves and reaches an eligible milestone, the player can earn a new egg opportunity. The older pet remains alive in the world instead of being replaced.

After a pet reaches its **final evolution**, it no longer needs care. It becomes a permanent world resident/companion that can wander, socialize, play, use appropriate playground equipment, and interact with other resident pets.

All resident pets can coexist and interact at the world level. Their interactions do not transfer personal care stats between pets. For example, caring for Pet A must not refill Pet B's hunger. Social play, shared discoveries, and world events can involve multiple residents.

The long-term result is a large, living population accumulated through successive generations while only one pet is actively raised at any moment.

## Evolution and education

Lineage and genetic recipes remain hidden from the player. The player discovers appearances, behaviors, evolutions, and descendants as surprises.

Playground activities can reinforce Python concepts such as variables, loops, conditionals, timing, motion, data collection, debugging, and simulation, but no playground activity replaces the structured Python curriculum.

## Safety research basis

Real-world safety research informs the simulated layout. The 2025 CPSC Public Playground Safety Handbook identifies age-appropriate equipment categories including swings, slides, climbers, merry-go-rounds/spinners, ramps, spring rockers, and overhead equipment. It also emphasizes protective surfacing, spacing, entrapment prevention, sharp-edge/pinch-point avoidance, anchoring, and maintenance.

CPSC references:

- https://www.cpsc.gov/Playground-Handbook
- https://www.cpsc.gov/safety-education/safety-guides/playgrounds/public-playground-safety-checklist

Pypet is a virtual game; these references are design/safety inspiration and are not a claim that the virtual playground is a certified real-world playground.
