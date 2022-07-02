from z3 import *
from itertools import combinations
from abc import ABC, abstractmethod

class Constraint(ABC) :

    def __init__(self, classes, string_to_bool_literal_dict, name="Abstract Constraint") :
        self.classes = classes
        self.string_to_bool_literal_dict = string_to_bool_literal_dict
        self.name = name

    @abstractmethod
    def enforce(self, solver) :
        pass

# Below are the Constraints that will be used on the Solver.

class No8AMLessonsConstraint(Constraint) :

    def __init__(self, classes, string_to_bool_literal_dict, name="No 8am Lessons Constraint") :
        super().__init__(classes, string_to_bool_literal_dict, name)

    def __str__(self) :
        return self.name

    def __repr__(self) :
        return self.name

    def enforce(self, solver) :
        morning_lessons = list(filter(lambda x : x.start <= 800, self.classes))
        negated_variables = list(map(lambda x : Not(self.string_to_bool_literal_dict[str(x)]), morning_lessons))
        solver.add(And(negated_variables))

class NoConsecutiveLessonsConstraint(Constraint) :

    def __init__(self, classes, string_to_bool_literal_dict, name="No Consecutive Lessons") :
        super().__init__(classes, string_to_bool_literal_dict, name)
        self.lessons_by_day = [[], [], [], [], []]

    def __str__(self) :
        return self.name

    def __repr__(self) :
        return self.name

    def sort_by_days(self) :
        for lesson in self.classes :
            self.lessons_by_day[lesson.day - 1].append(lesson)

    def enforce(self, solver) :
        self.sort_by_days()
        for day in self.lessons_by_day :
            for nus_class in day :
                candidates = list(filter(lambda x : x.start == nus_class.end, day))
                antecedant = self.string_to_bool_literal_dict[str(nus_class)]
                negated_lessons = list(map(lambda x : Not(self.string_to_bool_literal_dict[str(x)]), candidates))
                solver.add(Implies(antecedant, And(negated_lessons)))

class NoClashConstraint(Constraint) :

    def __init__(self, classes, string_to_bool_literal_dict, name="No Time Clash Constraint") :
        super().__init__(classes, string_to_bool_literal_dict, name)
        self.lessons_by_day = [[], [], [], [], []]

    def sort_by_days(self) :
        for lesson in self.classes :
            self.lessons_by_day[lesson.day - 1].append(lesson)

    def __str__(self) :
        return self.name

    def __repr__(self) :
        return self.name

    def enforce(self, solver) :
        self.sort_by_days()
        for day in self.lessons_by_day :
            for nus_class in day :
                candidates = list(filter(lambda x : x.willClash(nus_class) and x is not nus_class, day))
                antecedant = self.string_to_bool_literal_dict[str(nus_class)]
                clash_literals = list(map(lambda x : Not(self.string_to_bool_literal_dict[str(x)]), candidates))
                solver.add(Implies(antecedant, And(clash_literals)))

class OneDayFreeConstraint(Constraint) :

    def __init__(self, classes, string_to_bool_literal_dict, name="One Free Day Constraint") :
        super().__init__(classes, string_to_bool_literal_dict, name)

    def __str__(self) :
        return self.name

    def __repr__(self) :
        return self.name

    def enforce(self, solver) :
        byDays = [[], [], [], [], []]
        for lesson in self.classes :
            byDays[lesson.day - 1].append(Not(self.string_to_bool_literal_dict[str(lesson)]))
        OrList = []
        for day in byDays :
            OrList.append(And(day))
        solver.add(Or(OrList))

class SelectOnlyOneSlot(Constraint) :

    def __init__(self, classes, string_to_bool_literal_dict, name="Select Only 1 Slot Constraint") :
        super().__init__(classes, string_to_bool_literal_dict, name)

    def __str__(self) :
        return self.name

    def __repr__(self) :
        return self.name

    def enforce(self, solver) :
        if (len(self.classes) == 0) :
            return
        elif (len(self.classes) == 1) :
            solver.add(self.string_to_bool_literal_dict[str(self.classes[0])] == True)
            return
        else :
            set_of_common_slots = set()
            literal_list = []
            for nus_class in self.classes :
                candidates = list(filter(lambda x : x.slot == nus_class.slot, self.classes))
                converts = frozenset(map(lambda x : self.string_to_bool_literal_dict[str(x)], candidates))
                set_of_common_slots.add(converts)
                '''
                All with different slots will be negated
                '''
                different_slots = list(filter(lambda x : x.slot != nus_class.slot, self.classes))
                change = frozenset(map(lambda x : Not(self.string_to_bool_literal_dict[str(x)]), different_slots))
                solver.add(Implies(self.string_to_bool_literal_dict[str(nus_class)], And(list(change))))
            for group in set_of_common_slots :
                if (len(group) == 1) :
                    literal_list.append(list(group)[0])
                else :
                    literal_list.append(And(list(group)))
            self.pick_one(literal_list, solver)

    def pick_one(self, literal_list, solver) :
        if (len(literal_list) == 0) :
            return
        elif (len(literal_list) == 1) :
            solver.add(literal_list[0] == True)
            return
        else :
            solver.add(Or(literal_list))
            for group in list(combinations(literal_list, 2)) :
                solver.add(Not(And(group)))

