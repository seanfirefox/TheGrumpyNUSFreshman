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
        mods = ["CS2103T", "MA2202S", "CS2109S", "CS2107","MA2104","GEA1000","ESE5202","MA3211", "MA3227"]
        scrapper = Scrapper(mods, "2022-2023", 2)
        scrapper.scrape()
        a = TimeTableSchedulerZ3(scrapper.semesterProcessed, False)
        a.optimiseTimetable()
        self.assertEqual(a.solver.check(), sat)
        print("============================================================")

if __name__ == '__main__' :
    unittest.main()


