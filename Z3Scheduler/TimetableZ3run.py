from scrapper import *
from z3 import *
from timetableZ3 import *
from flask import Flask, request, session, redirect, url_for
from flask_session import Session
from flask_sqlalchemy import SQLAlchemy
#from flask_kvsession import KVSessionExtension
import gc
#import sys

# Flask Constructor
app = Flask(__name__)
app.config['SECRET_KEY'] = "SATSolver"
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///db.sqlite3"
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['SESSION_PERMANENT'] = True
app.config['SESSION_TYPE'] = "sqlalchemy"
app.config.from_object(__name__)

db = SQLAlchemy(app)

app.config['SESSION_SQLALCHEMY'] = db

#KVSessionExtension(db, app)
sess = Session(app)

@app.before_first_request
def create_tables():
    db.create_all()

@app.route("/")
def show_heroku_site() :
    return "Heroku site"

@app.route("/login", methods=["POST", "GET"])
def login():
    if request.method == "POST":
        session["user"] = request.form["userID"]
        print(session["user"])
        num_mods = int(request.form["numMods"])
        print(num_mods)
        for i in range(num_mods) :
            session["mod" + str(i)] = request.form["mod" + str(i)]
        session["num_mods"] = num_mods
        print(session["num_mods"])
        session["AY"] = request.form["AY"]
        session["SEM"] = int(request.form["Sem"])
        return redirect("/get_soln")
    else:
        return "Login failed"

@app.route("/user")
def user():
    print(session["user"])
    if "user" in session:
        return redirect(url_for("get_soln"))
    else:
        return redirect(url_for("login"))

@app.route("/get_soln", methods=['GET'])
def get_soln() :
    mods = []
    print(session.get("num_mods"))
    num_mods = session["num_mods"]
    for i in range(num_mods) :
        mods.append(session["mod" + str(i)])
    AY = session["AY"]
    SEM = session["SEM"]
    scrapper = Scrapper(mods, AY, SEM)
    scrapper.scrape()
    scheduler = TimeTableSchedulerZ3(scrapper.semesterProcessed, True)
    string = scheduler.optimiseTimetable(to_string=True)
    session["constraints"] = scheduler.solver.to_smt2()
    session["literal_hashmap"] = scheduler.string_to_bool_literal
    session["nus_class_hashmap"] = scheduler.literal_to_object
    return string

@app.route("/alt_soln", methods=["POST"])
def alt_soln() :
    if "constraints" and "literal_hashmap" and "nus_class_hashmap" in session:
        saved_constraints_string = session["constraints"]
        saved_constraints = parse_smt2_string(saved_constraints_string, sorts = {}, decls = {})
        literal_hashmap = session["literal_hashmap"]
        nus_class_hashmap = session["nus_class_hashmap"]
        scheduler = TimeTableSchedulerZ3(None, print=True)
        scheduler.solver.add(saved_constraints)
        scheduler.string_to_bool_literal = literal_hashmap
        scheduler.literal_to_object = nus_class_hashmap
        return scheduler.another_solution(to_string=True)

@app.route("/logout")
def logout():
    session.pop("user", None)
    return redirect(url_for("login"))

@app.route("/userID", methods=["POST"])
def test_two() :
    return

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


if __name__ == "__main__" :
    app.run()
