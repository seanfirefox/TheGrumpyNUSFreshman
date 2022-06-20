from Z3Schedule import db

class Z3Schedule(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.Text(), nullable=False)

    def __repr__(self):
        return '<Task: {}>'.format(self.name)