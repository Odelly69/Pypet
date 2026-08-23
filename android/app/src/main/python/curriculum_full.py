TRACKS = {
'Foundations': ['syntax','comments','variables','numbers','strings','booleans','None','operators','expressions','input/output','f-strings','conversion','truthiness'],
'Control flow': ['if','elif','else','for','while','break','continue','pass','match','comprehensions'],
'Data structures': ['list','tuple','set','dict','range','slicing','unpacking','nested data','iterables'],
'Functions': ['def','parameters','defaults','keyword arguments','*args','**kwargs','return','scope','lambda','closures','recursion','higher-order functions','decorators'],
'Exceptions': ['try','except','else','finally','raise','custom exceptions','exception chaining','tracebacks'],
'Modules and packages': ['import','from import','modules','packages','__init__','__main__','namespaces','relative imports'],
'OOP': ['classes','objects','__init__','attributes','methods','class variables','instance variables','inheritance','composition','polymorphism','properties','dataclasses','abstract base classes','protocols'],
'Python data model': ['__str__','__repr__','__eq__','__lt__','__hash__','__len__','__getitem__','__iter__','__next__','__call__','__enter__','__exit__','__getattr__','__getattribute__','__setattr__','__slots__','__init_subclass__','descriptors','metaclasses'],
'Files and data': ['open','pathlib','text files','binary files','JSON','CSV','SQLite','serialization','persistence'],
'Iterators and generators': ['iter','next','iterators','generators','yield','generator expressions','lazy evaluation'],
'Context management': ['with','context managers','resource cleanup','custom context managers'],
'Typing': ['annotations','list[str]','dict[str,int]','Union','Optional','Literal','TypeVar','Generic','Protocol','TypedDict','type aliases'],
'Standard library': ['math','statistics','random','datetime','time','calendar','decimal','fractions','collections','itertools','functools','heapq','bisect','os','shutil','glob','tempfile','sys','platform','subprocess','argparse','logging','inspect','types','dis'],
'Networking': ['urllib','http','socket','client/server concepts','JSON APIs','timeouts','safe networking'],
'Concurrency and async': ['threading','multiprocessing','queue','concurrent.futures','async','await','asyncio','tasks','event loops','async I/O'],
'Testing and debugging': ['assert','unittest','doctest','pytest concepts','fixtures','mocking','tracebacks','breakpoints','logging','regression testing'],
'Professional Python': ['virtual environments','pip','pyproject.toml','dependencies','versioning','CLI applications','documentation','packaging','publishing concepts'],
'Third-party ecosystem': ['Pillow','pygame','NumPy','pandas','Matplotlib','Requests/httpx','Flask','FastAPI','SQLAlchemy','scikit-learn','PyTorch','Ruff','Black','mypy/Pyright'],
'Capstone': ['design a pet class','pet behaviors','inventory','persistence','modules','tests','package','document','ship a playable programmable pet']}

def all_topics():
    return [{'track': k, 'topics': v} for k, v in TRACKS.items()]

def lesson_text(track='Foundations', index=0):
    topics = TRACKS[track]
    topic = topics[index % len(topics)]
    return f'{track}: {topic}. Learn it by writing real Python that changes your pet.'
