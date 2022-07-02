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


if __name__ == '__main__' :
    unittest.main()


