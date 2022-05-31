class NUSModule :

    def __init__(self, name) :
        self.modCode = name
        self.lectures = []
        self.recitations = []
        self.tutorials = []
        self.labs = []
        self.seminars = []
        self.sectionals = []
    
    def __str__(self) :
        return self.modCode

    def __repr__(self) :
        return self.modCode
    
    def addLectures(self, lec) :
        self.lectures.append(lec)

    def addSectionalTeaching(self, sec) :
        self.sectionals.append(sec)

    def addSeminars(self, sem) :
        self.seminars.append(sem)
    
    def addLabs(self, lab) :
        self.labs.append(lab)

    def addRecitations(self, rec) :
        self.recitations.append(rec)

    def addTutorials(self, tut) :
        self.tutorials.append(tut)

    def __eq__(self, other) :
        return self.modCode == other.modCode

    def __neq__(self, other) :
        return (not (isinstance(other, NUSModule))) or (self != other)


