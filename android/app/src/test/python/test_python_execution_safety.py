from pypet_curriculum import LESSONS, STANDARD_LIBRARY, ECOSYSTEM
from pypet_engine import run_lesson


def test_curriculum_covers_full_python_path():
    assert len(LESSONS) >= 60
    titles = {x.title for x in LESSONS}
    assert {"Classes", "Generators", "Async", "Tkinter", "Pygame", "Capstone"} <= titles
    assert "sqlite3" in STANDARD_LIBRARY
    assert "asyncio" in STANDARD_LIBRARY
    assert set(ECOSYSTEM) >= {"tkinter", "pygame"}


def test_real_python_execution_returns_answer_and_output():
    result = run_lesson("answer = len([1, 2, 3])\nprint(answer)", "3")
    assert result["ok"] is True
    assert result["passed"] is True
    assert result["answer"] == "3"
    assert result["output"] == "3"


def test_python_exception_is_reported_not_crashed():
    result = run_lesson("answer = 1 / 0")
    assert result["ok"] is False
    assert result["passed"] is False
    assert "ZeroDivisionError" in result["error"]


def test_python_namespace_does_not_expose_import_builtin():
    result = run_lesson("answer = __import__('os').getcwd()")
    assert result["ok"] is False


def test_python_namespace_does_not_expose_open_builtin():
    result = run_lesson("answer = open('secret.txt').read()")
    assert result["ok"] is False


def test_python_namespace_rejects_dunder_escape_path():
    result = run_lesson("answer = ().__class__.__base__.__subclasses__()")
    assert result["ok"] is False
    assert "Dunder" in result["error"]
