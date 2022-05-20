from z3 import *
from abc import ABC, abstractmethod

class Constraint(ABC) :

    def __init__(self, classes, hashMap) :
        self.classes = classes
        self.hashMap = hashMap

    @abstractmethod
    def enforce(self, solver) :
        pass

# Below are the Constraints that will be used on the Solver.

class OneDayFreeConstraint(Constraint) :

    def __init__(self, classes, hashMap) :
        super().__init__(classes, hashMap)

    def __str__(self) :
        return "One Free Day Constraint"

    def __repr__(self) :
        return "One Free Day Constraint"

    def enforce(self, solver) :
        byDays = [[], [], [], [], []]
        for lesson in self.classes :
            byDays[lesson.day - 1].append(Not(self.hashMap[str(lesson)]))
        OrList = []
        for day in byDays :
            OrList.append(And(day))
        solver.add(Or(OrList))


