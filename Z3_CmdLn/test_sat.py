import unittest
import sys
import os
from timetableZ3 import *
from scrapper import Scrapper

class Test_SAT(unittest.TestCase) :

    '''
    Test cases were obtained with permission from friends of Tan Kai Min, Russell and Tan Li Thai Sean.
    We would like to extend our gratitude for those who had contributed their timetables.
    '''

    def test_sat_1(self) :
        mods = ["CS1101S", "CS1231S", "MA1521", "MA2001", "GEC1030"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_2(self) :
        mods = ["CS2030S", "CS2040S", "ST2334", "IS1103", "GEA1000", "GESS1027"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_3(self) :
        mods = ["IS1103", "CS2030S", "CS2040S", "ST2131", "MA2104", "SP1541", "HSS1000", "GEA1000"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_4(self) :
        mods = ["IS1103", "CS2030S", "CS2040S", "ST2334", "MA2104", "CM1102"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_5(self) :
        mods = ["CS2030S", "CS2040S", "MA2001", "ST2334", "GEA1000"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_6(self) :
        mods = ["CS3230", "MA4211", "CS2100", "MA5203", "CS2103", "MA4262", "MA4235"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_7(self) :
        mods = ["CS3230", "MA4211", "CS2100", "MA5203", "CS2103", "MA4262", "MA4235"]
        scrapper = Scrapper(mods, "2021-2022", 1)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_8(self) :
        mods = ["UTC1114", "GEA1000", "CS2030S", "ENV2102", "UTC1404", "MA2101S", "ST2131", "MA2108S", "CS2040S", "ST2132"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

    def test_sat_9(self) :
        mods = ["HSA1000", "HSS1000", "GEA1000", "ST2131", "CS2030S", "MA1100", "CS2103T", "MA2104", "IS1103"]
        scrapper = Scrapper(mods, "2021-2022", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")


if __name__ == '__main__' :
    unittest.main()


