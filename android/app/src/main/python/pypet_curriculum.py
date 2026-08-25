"""PyPet Academy: hands-on novice-to-master Python curriculum."""
from dataclasses import dataclass, asdict
from typing import Any

@dataclass(frozen=True)
class Lesson:
    id: str
    tier: str
    title: str
    skill: str
    mission: str
    module: str = "python"
    world_effect: str = "knowledge"

TIERS = ("Novice", "Apprentice", "Intermediate", "Advanced", "Expert", "Master")
LESSONS: list[Lesson] = []

def _add(tier, prefix, rows):
    for i, row in enumerate(rows, 1):
        title, skill, mission, module = row
        LESSONS.append(Lesson(f"{prefix}-{i:02d}", tier, title, skill, mission, module))

_add("Novice", "N", [
    ("First Print", "print and expressions", "Make your pet greet the world.", "builtins"),
    ("Values", "numbers, strings, booleans, None", "Give your pet useful facts.", "builtins"),
    ("Variables", "assignment and names", "Track your pet's food and energy.", "builtins"),
    ("Operators", "arithmetic and comparisons", "Calculate supplies for a picnic.", "operator"),
    ("Input", "input and conversion", "Let your pet ask the player a question.", "builtins"),
    ("Decisions", "if, elif, else", "Teach when to eat or play.", "builtins"),
    ("Loops", "for and while", "Plant a row of flowers automatically.", "builtins"),
    ("Functions", "def, parameters, return", "Build a reusable care action.", "builtins"),
    ("Debugging", "tracebacks and experiments", "Find and repair a broken world action.", "traceback"),
])
_add("Apprentice", "A", [
    ("Collections", "list, tuple, set, dict", "Build a pet inventory.", "collections"),
    ("Comprehensions", "list/set/dict comprehensions", "Transform a garden plan.", "builtins"),
    ("Strings", "formatting and text processing", "Create a pet journal.", "string"),
    ("Files", "reading and writing files", "Save a world blueprint.", "pathlib"),
    ("Paths", "filesystem paths", "Organize world resources.", "pathlib"),
    ("Exceptions", "try/except/else/finally", "Make a safe market transaction.", "builtins"),
    ("Modules", "import and packages", "Turn a solution into a reusable tool.", "importlib"),
    ("Testing", "assertions and test cases", "Prove a pet-care function works.", "unittest"),
    ("Data", "JSON and CSV", "Export and restore world data.", "json"),
])
_add("Intermediate", "I", [
    ("Classes", "objects, classes, methods", "Create a custom world object.", "builtins"),
    ("Inheritance", "base classes and overriding", "Create specialized pet species from a shared class.", "builtins"),
    ("Composition", "objects containing objects", "Build a town from reusable world components.", "builtins"),
    ("Properties", "property getters and setters", "Protect and validate pet state.", "builtins"),
    ("Python Data Model", "dunder methods and protocols", "Teach a world object to behave naturally with Python operators.", "builtins"),
    ("Dataclasses", "structured state", "Model a pet and inventory.", "dataclasses"),
    ("Iterators", "iter and next", "Build a world resource stream.", "itertools"),
    ("Generators", "yield and lazy computation", "Generate an endless supply trail.", "builtins"),
    ("Decorators", "wrapping behavior", "Add rewards to world actions.", "functools"),
    ("Context Managers", "with and cleanup", "Safely manage a world resource.", "contextlib"),
    ("Typing", "type hints and protocols", "Make a reusable typed tool.", "typing"),
    ("Regex", "pattern matching", "Search and validate pet notes.", "re"),
    ("SQLite", "relational data", "Build a persistent market database.", "sqlite3"),
    ("CLI", "command-line interfaces", "Turn a world tool into a command.", "argparse"),
])
_add("Advanced", "V", [
    ("Dates", "datetime and time zones", "Schedule world events.", "datetime"),
    ("Math", "math, statistics, decimal, fractions", "Balance a world economy.", "math"),
    ("Randomness", "random and secrets", "Create fair encounters and secure tokens.", "random"),
    ("Concurrency", "threading and queues", "Run independent world jobs.", "threading"),
    ("Processes", "multiprocessing", "Parallelize a heavy simulation.", "multiprocessing"),
    ("Futures", "concurrent.futures", "Coordinate multiple tasks.", "concurrent.futures"),
    ("Async", "async/await", "Animate several world systems together.", "asyncio"),
    ("Networking", "sockets and protocols", "Make two world stations communicate.", "socket"),
    ("HTTP", "clients and servers", "Consume a safe web service.", "http"),
    ("Subprocesses", "process control", "Build a controlled automation tool.", "subprocess"),
])
_add("Expert", "E", [
    ("Tkinter", "desktop GUI", "Build a control panel for the pet.", "tkinter"),
    ("Tkinter Canvas", "2D interactive graphics", "Draw an interactive mini-world.", "tkinter"),
    ("Pygame", "game loop and input", "Build a playable pet mini-game.", "pygame"),
    ("Pygame Graphics", "sprites, surfaces, animation", "Animate your pet in a game scene.", "pygame"),
    ("Pygame Physics", "collision and timing", "Create a physics-based challenge.", "pygame"),
    ("Packaging", "build and distribute projects", "Package a world tool cleanly.", "importlib"),
    ("Logging", "diagnostics and structured logs", "Diagnose a failing world system.", "logging"),
    ("Security", "hashing, secrets, safe data", "Protect a world account token.", "hashlib"),
    ("Performance", "profiling and optimization", "Speed up a slow simulation.", "cProfile"),
    ("Architecture", "separation of concerns", "Refactor a growing world system.", "dataclasses"),
])
_add("Master", "M", [
    ("Standard Library Quest", "module selection", "Solve a world problem using the right standard-library tools.", "stdlib"),
    ("Full GUI Project", "Tkinter application design", "Build a complete world editor.", "tkinter"),
    ("Full Game Project", "Pygame architecture", "Build a complete playable game.", "pygame"),
    ("Automation Project", "integration and concurrency", "Automate a multi-stage world operation.", "stdlib"),
    ("Data Project", "database and data processing", "Build persistent world analytics.", "sqlite3"),
    ("Network Project", "networked application", "Build a robust client/server world feature.", "socket"),
    ("Testing Project", "quality engineering", "Test and repair a deliberately broken project.", "unittest"),
    ("Capstone", "independent Python engineering", "Design, build, debug, test, and explain a substantial world system.", "python"),
])

STANDARD_LIBRARY = [
    "builtins", "collections", "itertools", "functools", "operator", "math", "statistics",
    "decimal", "fractions", "random", "datetime", "zoneinfo", "calendar", "time", "pathlib",
    "os", "shutil", "glob", "tempfile", "fileinput", "json", "csv", "configparser", "sqlite3",
    "re", "string", "textwrap", "difflib", "enum", "dataclasses", "typing", "abc", "contextlib",
    "logging", "traceback", "warnings", "unittest", "doctest", "argparse", "subprocess", "threading",
    "multiprocessing", "concurrent.futures", "asyncio", "queue", "socket", "http", "urllib", "email",
    "xml", "html", "hashlib", "hmac", "secrets", "base64", "struct", "pickle", "shelve", "cProfile",
]
ECOSYSTEM = ["tkinter", "pygame"]

def curriculum() -> list[dict[str, Any]]:
    return [asdict(x) for x in LESSONS]

def lesson_for(index: int) -> dict[str, Any]:
    return asdict(LESSONS[index % len(LESSONS)])

def total_lessons() -> int:
    return len(LESSONS)
