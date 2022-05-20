from scrapper import Scrapper
from timetableZ3 import *

mods = ["CM1102", "MA2104", "CS2040S","ST2334","CS2030S"]
test1 = ["CS2030S", "CS1231S", "MA2001", "MA1521", "IS1103"]
largeTestSAT = ["UTC1114", "MA2101S", "ST2131", "MA2108S", "ST2132", "CS2040S", "CS2030S", "GEA1000","ENV2102"]
russellSem1 = ["CS1101S", "CS1231S", "MA2001", "MA1521", "GEC1030"]
scrapper = Scrapper(mods, '2021-2022', 2)
scrapper.scrape()
print(scrapper.semesterProcessed)
timetable = TimeTableSchedulerZ3(scrapper.semesterProcessed)
timetable.optimiseTimetable()
print('-----------------\n')
timetable.anotherSolution()

print('-----------------\n')
timetable.anotherSolution()

print('-----------------\n')
timetable.anotherSolution()

print('-----------------\n')
timetable.anotherSolution()