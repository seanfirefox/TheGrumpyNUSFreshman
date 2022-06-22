from scrapper import *
from z3 import *
from timetableZ3 import *
from flask import Flask
from flask import request
import gc
#import sys

# Flask Constructor
app = Flask(__name__)

DISCARD = {"On Monday": "MON", "On Tuesday" : "TUE", "On Wednesday" : "WED" , "On Thursday" : "THUR", "On Friday" : "FRI"}

@app.route("/")
def show_heroku_site() :
    return "Heroku site"

@app.route("/z3runner", methods=['POST'])
def test_one() :
    '''Get basic data'''
    num_mods = int(request.form['numMods'])
    mods = []
    for i in range(num_mods) :
        mods.append(request.form["mod" + str(i)])
    AY = request.form["AY"]
    SEM = int(request.form["Sem"])
    n_th = int(request.form["iter"])
    scrapper = Scrapper(mods, AY, SEM)
    scrapper.scrape()
    scheduler = TimeTableSchedulerZ3(scrapper.semesterProcessed, True)
    constraints = {'no8amLessons' : bool(request.form['no8amLessons']),\
            'oneFreeDay' : bool(request.form['oneFreeDay'])}
    print(constraints)
    scheduler.add_constraint_dict(constraints)
    string = scheduler.optimiseTimetable(to_string=True)
    for i in range(n_th) :
        string = scheduler.another_solution()
    print(string)
    process_string_to_json(string)
    return string

def process_string_to_json(string) :
    dictionary = {"MON" : [], "TUE" : [], "WED" : [], "THUR" : [], "FRI" : []}
    a = string.split("\n")
    print(a)
    key_ = None
    for string_item in a :
        if string_item in DISCARD :
            key_ = string_item
        elif string_item == "" :
            continue
        else :
            dictionary[key_].append(string_item)
    print(dictionary)
    print("STRING LEFT")
    print(string)


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

@app.route("/posttest", methods=['POST'])
def post_from_android() :
    value = request.form['value']
    return (value)

if __name__ == "__main__" :
    app.run()

