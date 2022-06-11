from scrapper import *
import sys
sys.path.append("app/.heroku/python/lib/python3.10/site-packages/z3/")
from z3 import *
from timetableZ3 import *
from flask import Flask
#import sys

# Flask Constructor
app = Flask(__name__)

@app.route("/")
def show_heroku_site() :
    return "Heroku site"

@app.route("/z3", methods=['GET'])
def show_z3_stuff() :
    modules = ["CS2030S", "CS2040S", "ST2334", "MA2104", "CM1102"]
    scrapper = Scrapper(modules, "2021-2022", 2)
    scrapper.scrape()
    timetable = TimeTableSchedulerZ3(scrapper.semesterProcessed, print=True)
    return timetable.optimiseTimetable(to_string=True)

@app.route("/test")
def test_one() :
    x = Bool('x')
    return "Russell Test"

if __name__ == "__main__" :
    app.run()

'''
OPTIONS = [

]
numMods = len(sys.argv) - 1

print("You have entered " + str(numMods) + "modules\n") 
AY = input("Enter Academic Year in the form 2021-2022\n")
SEM = int(input("Input semester number\n"))
modules = [sys.argv[i] for i in range(1, numMods + 1)]

scrapper = Scrapper(modules, AY, SEM)
scrapper.scrape()

timetable = TimeTableSchedulerZ3(scrapper.semesterProcessed, print=True)
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
'''
