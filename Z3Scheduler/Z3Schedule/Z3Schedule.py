from flask import (
    Blueprint, flash, redirect, render_template, request, url_for
)

from Z3Schedule import db, cache
from Z3Schedule.models import Z3Schedule

bp = Blueprint('Z3Schedule', __name__)

def is_post():
    return (request.method == 'POST')

@bp.route('/', methods=('GET', 'POST'))
@cache.cached(unless=is_post)
def index():
    if request.method == 'POST':
        userID = request.form['userID']
        numMods = request.form['numMods']
        AY = request.form['AY']
        SEM = request.form['Sem']
        if not userID:
            flash('UserID is required.')
        else:
            db.session.add(Z3Schedule(userID=userID))
            db.session.add(Z3Schedule(numMods=numMods))
            db.session.add(Z3Schedule(AY=AY))
            db.session.add(Z3Schedule(SEM=SEM))
            db.session.commit()
            cache.delete_memoized(get_all_timetable_settings)
            cache.delete('view//')

    #tasks = get_all_timetable_settings()
    return redirect('/alt_soln', userID=userID)

@bp.route('/alt_soln', methods=('GET', 'POST'))
def alt_soln(userID):
    query = db.session.query(userID)
    for numMods in query:
        return numMods

@bp.route('/<int:id>/delete', methods=('POST',))
def delete(id):
    task = Z3Schedule.query.get(id)
    if task != None:
        db.session.delete(task)
        db.session.commit()
        cache.delete_memoized(get_all_timetable_settings)
        cache.delete('view//')
    return redirect(url_for('task_list.index'))

@cache.memoize()
def get_all_timetable_settings():
    return Z3Schedule.query.all()


from flask import Flask, session
@bp.route('/set/')
def set():
    session['key'] = 'value'
    return 'ok'

@bp.route('/get/')
def get():
    return session.get('key', 'not set')
