from scrapper import *
from z3 import *
import z3
from timetableZ3 import *
from flask import Flask
from flask import request
import gc
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
