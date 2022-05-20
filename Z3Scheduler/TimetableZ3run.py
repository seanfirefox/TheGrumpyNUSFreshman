from scrapper import *
from timetableZ3 import *

OPTIONS = [
    "OneFreeDay"
]

numMods = int(input("How many modules are you intending to take ? \n"))
AY = input("Enter Academic Year in the form 2021-2022\n")
SEM = int(input("Input semester number\n"))
modules = []
for i in range(numMods) :
    modules.append(input("Name of Module : \n"))

scrapper = Scrapper(modules, AY, SEM)
scrapper.scrape()

timetable = TimeTableSchedulerZ3(scrapper.semesterProcessed)
timetable.optimiseTimetable()
for item in OPTIONS :
    print("Do You want the " + item + " constraint ? \n")
    if (input() == "Yes") :
        TimeTableSchedulerZ3.OPTIONS[item] = True
        timetable.addOtherConstraints()

if (timetable.lastSolnStatus == unsat) :
    print("Terminating . . . \n")
else :
    happy = False
    print("Are You Happy with this timetable? /n")
    happy = input("Yes or No only") == "Yes"
    if (happy != True and happy != False) :
        print("Wrong input! ")
    while(happy != True) :
        timetable.anotherSolution()
        print("Are You Happy with this timetable? /n")
        happy = input("Yes or No only") == "Yes"
        if (happy != True and happy != False) :
            print("Wrong input! ")
