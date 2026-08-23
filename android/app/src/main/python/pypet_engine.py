from dataclasses import dataclass, asdict
from typing import Any

@dataclass
class PetState:
    name: str = 'Pip'
    hunger: int = 80
    energy: int = 80
    happiness: int = 70
    knowledge: int = 0
    xp: int = 0
    lesson: int = 1

    def feed(self) -> dict[str, Any]:
        self.hunger = min(100, self.hunger + 15)
        self.happiness = min(100, self.happiness + 3)
        return asdict(self)

    def play(self) -> dict[str, Any]:
        self.energy = max(0, self.energy - 5)
        self.happiness = min(100, self.happiness + 10)
        return asdict(self)

    def learn(self, xp: int = 10) -> dict[str, Any]:
        self.knowledge += 1
        self.xp += xp
        return asdict(self)

def run_lesson(code: str, expected: str | None = None) -> dict[str, Any]:
    """Run a small educational Python exercise in a restricted namespace.

    The Android editor will use this for beginner lessons. It intentionally
    provides no Android objects, filesystem helpers, subprocesses, or network
    handles to student code.
    """
    safe_builtins = {
        'abs': abs, 'all': all, 'any': any, 'bool': bool, 'dict': dict,
        'enumerate': enumerate, 'filter': filter, 'float': float,
        'int': int, 'len': len, 'list': list, 'max': max, 'min': min,
        'range': range, 'reversed': reversed, 'round': round, 'set': set,
        'sorted': sorted, 'str': str, 'sum': sum, 'tuple': tuple,
        'type': type, 'zip': zip, 'print': print,
    }
    namespace = {'__builtins__': safe_builtins}
    output: list[str] = []
    namespace['print'] = lambda *args, **kwargs: output.append(' '.join(map(str, args)))
    try:
        exec(code, namespace, namespace)
        result = namespace.get('answer', None)
        passed = expected is None or str(result).strip() == expected.strip()
        return {'ok': True, 'passed': passed, 'answer': repr(result), 'output': '\n'.join(output)}
    except Exception as exc:
        return {'ok': False, 'passed': False, 'error': f'{type(exc).__name__}: {exc}', 'output': '\n'.join(output)}
