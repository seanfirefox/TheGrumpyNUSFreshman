import unittest
import sys
import os
from timetableZ3 import *
from scrapper import Scrapper

class Test_Constraint(unittest.TestCase) :

    '''
    Test cases were obtained with permission from friends of Tan Kai Min, Russell and Tan Li Thai Sean.
    We would like to extend our gratitude for those who had contributed their timetables.
    '''

    def test_oneFreeDay_1(self) :
        mods = ["CS1101S", "CS1231S", "MA1521", "MA2001", "GEA1000"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("oneFreeDay")
        self.assertEqual(a.solver.check(), sat)
        self.assertTrue("No Classes On that Day!" in outPut)
        print("============================================================")

    def test_oneFreeDay_2(self) :
        mods = ["CS3230", "MA2101", "MA2108", "ES2660", "PC1421"]
        scrapper = Scrapper(mods, "2022-2023", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("oneFreeDay")
        self.assertEqual(a.solver.check(), sat)
        self.assertTrue("No Classes On that Day!" in outPut)
        print("============================================================")

    def test_no8amLessons_1(self) :
        mods = ["CS3203", "MA2108S", "CS4231"]
        scrapper = Scrapper(mods, "2018-2019", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        self.assertEqual(a.solver.check(), unsat)
        print("============================================================")
 
    
    def test_no8amLessons_2(self) :
        mods = ["CS1101S", "CS1231S", "MA1521", "MA2001", "GEC1030"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        self.assertEqual(a.solver.check(), unsat)
        print("============================================================")

    def test_no8amLessons_3(self) :
        mods = ["CS1101S", "CS1231S", "MA1521", "MA2001", "GEA1000"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        self.assertEqual(a.solver.check(), sat)
        self.assertFalse(" 800 - " in outPut) 
        print("============================================================")

    def test_noClash_1(self) :
        mods = ["CS3230", "MA2101", "MA2108", "ES2660", "PC1421"]
        scrapper = Scrapper(mods, "2022-2023", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        timetable = a.finalTimetable
        for i in range(5) :
            dayLessons = timetable[i]
            for j in range(len(dayLessons) - 1) :
                for k in range(j + 1, len(dayLessons)) :
                    self.assertFalse(dayLessons[j].willClash(dayLessons[k]))
        print("============================================================")

    def test_noClash_2(self) :
        mods = ["CS1101S", "CS1231S", "MA1521", "MA2001", "GEC1030"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        timetable = a.finalTimetable
        for i in range(5) :
            dayLessons = timetable[i]
            for j in range(len(dayLessons) - 1) :
                for k in range(j + 1, len(dayLessons)) :
                    self.assertFalse(dayLessons[j].willClash(dayLessons[k]))
        print("============================================================")
    
    def test_noClash_3(self) :
        mods = ["LAG1201", "ACC1701", "GE4217"]
        scrapper = Scrapper(mods, "2022-2023", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints("8amLessons")
        timetable = a.finalTimetable
        for i in range(5) :
            dayLessons = timetable[i]
            for j in range(len(dayLessons) - 1) :
                for k in range(j + 1, len(dayLessons)) :
                    self.assertFalse(dayLessons[j].willClash(dayLessons[k]))
        print("============================================================")

    def test_noClash_4(self) :
        mods = ["MA3252", "MA2108S", "USE2325", "CS4268", "CS4243"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        outPut = a.optimiseTimetableWithConstraints(["8amLessons", "oneFreeDay"])
        timetable = a.finalTimetable
        for i in range(5) :
            dayLessons = timetable[i]
            for j in range(len(dayLessons) - 1) :
                for k in range(j + 1, len(dayLessons)) :
                    self.assertFalse(dayLessons[j].willClash(dayLessons[k]))
        print("============================================================")

if __name__ == '__main__' :
    unittest.main()


