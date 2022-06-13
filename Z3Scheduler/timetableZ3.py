from itertools import combinations
from socket import timeout
from z3 import *
from utils import *
from Constraint import *
from NUSModule import *
from NUSClass import *
import sys

class TimeTableSchedulerZ3 :

    WEEKDAYS = {1 : "Monday",
                2 : "Tuesday",
                3 : "Wednesday",
                4 : "Thursday",
                5 : "Friday"}

    def __init__(self, module_dict, print=True) :
        self.semesterMods = module_dict
        self.solver = Solver()
        self.lessons_by_day = [[], [], [], [], []]
        self.lecs = []
        self.tuts = []
        self.recs = []
        self.labs = []
        self.sems = []
        self.sects = []
        self.string_to_bool_literal = {}
        self.literal_to_object = {}
        self.print = print
        self.finalTimetable = [[], [], [], [], []]

    def init_variables(self) :
        '''
        Enumerates all the key, value pairs of the dictionary and classify the modules by its day and 
        their class type (etc Lecture, Recitations etc). Proceeds to build hash map

        Input : self
        Output : Void
        '''
        for [key, value] in self.semesterMods.items() :
            # Uncomment for debugging
            # enumerate_nus_classes(value)
            for lecture in value.lectures :
                self.lessons_by_day[lecture.day - 1].append(lecture)
                self.lecs.append(lecture)
            for tutorial in value.tutorials :
                self.lessons_by_day[tutorial.day - 1].append(tutorial)
                self.tuts.append(tutorial)
            for recitation in value.recitations :
                self.lessons_by_day[recitation.day - 1].append(recitation)
                self.recs.append(recitation)
            for lab in value.labs :
                self.lessons_by_day[lab.day - 1].append(lab)
                self.labs.append(lab)
            for seminar in value.seminars :
                self.lessons_by_day[seminar.day - 1].append(seminar)
                self.sems.append(seminar)
            for sectional_lesson in value.sectionals :
                self.lessons_by_day[sectional_lesson.day - 1].append(sectional_lesson)
                self.sects.append(sectional_lesson)
            
        self.build_hashmaps()

    def build_hashmaps(self) :
        '''
        Builds the following hash maps :
        
        string_to_bool_literal : str -> z3.Bool
        literal_to_object : z3.Bool -> NUSClass (and its subclasses)
        '''
        for day in self.lessons_by_day : 
            for nus_class in day : 
                literal = Bool(str(nus_class))
                self.string_to_bool_literal[str(nus_class)] = literal
                self.literal_to_object[literal] = nus_class

    def chooseAtMostOneLiteral(self, literalList) :
        if (len(literalList) == 0) :
            return
        self.solver.add(AtMost(*literalList, 1))

    def convertToLiteral(self, lstClass) :
        return list(map(lambda x : self.string_to_bool_literal[str(x)], lstClass))

    def add_basic_constraints(self) :
        # for each module
        #   for each tut/rec : pick exactly 1 that cannot clash
        #   for each lecture : pick that one set with the same slot
        # for all lessons on the same day : if got clash, negate the boolean variables
        # e.g a = lecture at 6pm - 8pm, b = Rec at 5pm - 7pm, then (a and !b) or (b and !a)

        for [modCode, module] in self.semesterMods.items() :
            SelectOnlyOneSlot(module.lectures, self.string_to_bool_literal).enforce(self.solver)
            SelectOnlyOneSlot(module.recitations, self.string_to_bool_literal).enforce(self.solver)
            SelectOnlyOneSlot(module.tutorials, self.string_to_bool_literal).enforce(self.solver)
            SelectOnlyOneSlot(module.labs, self.string_to_bool_literal).enforce(self.solver)
            SelectOnlyOneSlot(module.seminars, self.string_to_bool_literal).enforce(self.solver)
            SelectOnlyOneSlot(module.sectionals, self.string_to_bool_literal).enforce(self.solver)

        # Resolve Time clash constraints
        NoClashConstraint(self.lecs + self.tuts + self.recs + self.sems + self.labs + self.sects, \
                self.string_to_bool_literal).enforce(self.solver)

    def another_solution(self, to_string=True) :
        '''
        Simple implementation of #SAT. This function enumerates the next set of solutions
        that are possible given the current constraints.

        Input : self
        Output : void
        Prints : SAT or UNSAT with No feasible timetable flag
        '''
        literals = self.string_to_bool_literal.values()
        current_model = self.solver.model()
        self.solver.add(Or([literal != current_model.eval(literal, model_completion=True) for literal in literals]))
        if (self.solver.check() == sat) :
            print("SAT")
            if (to_string) :
                return self.string_timetable()
            self.printTimeTable()
        elif (self.solver.check() == unsat) :
            if (to_string) :
                return "No feasible Timetable."
            print("UNSAT")
            print("No feasible timetable")
    
    def last_solution_status(self) :
        return self.solver.check()

    def fix_preferred(self, lst) :
        for item in lst :
            self.solver.add(self.string_to_bool_literal[str(item)] == True)

    def optimiseTimetable(self, to_string=False) :
        self.init_variables()
        self.add_basic_constraints()
        strings = ""
        if (self.solver.check() == sat) :
            print("SAT")
            if (to_string) :
                return self.string_timetable()
            if (self.print) :
                self.printTimeTable()
        elif (self.solver.check() == unsat) :
            print("UNSAT")
            if (to_string) :
                return "No Feasible Timetable!"
            print("No feasible timetable")
        return self.solver.check()

    def string_timetable(self) :
        string = ""
        self.finalTimetable = [[], [], [], [], []]
        for item in self.solver.model() :
            if (self.solver.model()[item]) :
                pp = self.string_to_bool_literal[str(item)]
                l = self.literal_to_object[pp]
                self.finalTimetable[l.day - 1].append(l)
        for i in range(5) :
            self.finalTimetable[i] = sorted(self.finalTimetable[i], key=lambda x : x.start)
            string = string + ("On " + TimeTableSchedulerZ3.WEEKDAYS[i + 1]) + "\n"
            if (len(self.finalTimetable[i]) == 0) :
                string = string + (" Hooray! You CAN Have No Classes On that Day!\n")
                continue
            for classes in self.finalTimetable[i] :
                string = string + (" " + str(classes) + "\n")
        # print(string)
        return string

    def printTimeTable(self) :
        self.finalTimetable = [[], [], [], [], []]
        for item in self.solver.model() :
            if (self.solver.model()[item]) :
                pp = self.string_to_bool_literal[str(item)]
                l = self.literal_to_object[pp]
                self.finalTimetable[l.day - 1].append(l)
        for i in range(5) :
            self.finalTimetable[i] = sorted(self.finalTimetable[i], key=lambda x : x.start)
            print("On " + TimeTableSchedulerZ3.WEEKDAYS[i + 1])
            if (len(self.finalTimetable[i]) == 0) :
                print(" Hooray! You CAN Have No Classes On that Day!")
                continue
            for classes in self.finalTimetable[i] :
                print(" " + str(classes))