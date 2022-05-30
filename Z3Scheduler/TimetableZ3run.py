from scrapper import *
from timetableZ3 import *
import sys

OPTIONS = [

]
numMods = len(sys.argv) - 1

print("You have entered " + str(numMods) + "modules\n") 
AY = input("Enter Academic Year in the form 2021-2022\n")
SEM = int(input("Input semester number\n"))
modules = [sys.argv[i] for i in range(1, numMods + 1)]

scrapper = Scrapper(modules, AY, SEM)
scrapper.scrape()

timetable = TimeTableSchedulerZ3(scrapper.semesterProcessed)
timetable.optimiseTimetable()

if (timetable.last_solution_status() == unsat) :
    print("Terminating . . . \n")
else :
    happy = False
    print("Are You Happy with this timetable? \n")
    happy = input("Yes or No only \n") == "Yes"
    if (happy != True and happy != False) :
        print("Wrong input! \n")
    while(happy != True) :
        print("Regenerating timetable... \n")
        timetable.another_solution()
        print("Are You Happy with this timetable? \n")
        happy = input("Yes or No only \n") == "Yes"
        if (happy != True and happy != False) :
            print("Wrong input! ")
    if (happy) :
        print("Saving Timetable...\n")

