from scrapper import *
from z3 import *
import z3
from timetableZ3 import *
from flask import Flask
from flask import request
#import sys

# Flask Constructor
app = Flask(__name__)

scheduler = None

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

@app.route("/sat")
def sat_stuff() :
    mods = ["UTC1114", "GEA1000", "CS2030S", "ENV2102", "UTC1404", "MA2101S", "ST2131", "MA2108S", "CS2040S", "ST2132"]
    scrapper = Scrapper(mods, "2021-2022", 2)
    scrapper.scrape()
    return TimeTableSchedulerZ3(scrapper.semesterProcessed, True).optimiseTimetable(to_string=True)

@app.route("/run", methods=['POST'])
def run() :
    mods = [request.form['mod1'], request.form['mod2'], request.form['mod3']]
    scrapper = Scrapper(mods, "2021-2022", 2)
    scrapper.scrape()
    return TimeTableSchedulerZ3(scrapper.semesterProcessed, True).optimiseTimetable(to_string=True)

@app.route("/test", methods=['POST'])
def test_one() :
    global scheduler
    if (scheduler is not None) :
        scheduler.endProgram()
        del globals()['scheduler']
    num_mods = int(request.form['numMods'])
    mods = []
    for i in range(num_mods) :
        mods.append(request.form["mod" + str(i)])
    AY = request.form["AY"]
    SEM = int(request.form["Sem"])
    scrapper = Scrapper(mods, AY, SEM)
    scrapper.scrape()
    scheduler = TimeTableSchedulerZ3(scrapper.semesterProcessed, True)
    string = scheduler.optimiseTimetable(to_string=True)
    return string

@app.route("/alt_soln", methods=["POST"])
def alt_soln() :
    global scheduler
    if (scheduler is None) :
        return "Nothing generated yet! No other solution offered!"
    return scheduler.another_solution(to_string=True)

def set_Scheduler(saved_scheduler) :
    global scheduler
    scheduler = saved_scheduler

@app.route("/userID", methods=["POST"])
def test_two() :
    return


@app.route("/posttest", methods=['POST'])
def post_from_android() :
    value = request.form['value']
    return (value)

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
