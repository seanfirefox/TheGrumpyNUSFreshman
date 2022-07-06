class NUSClass :
    
    WEEKDAYS = {1 : "MON", 2 : "TUES", 3 : "WED", 4 : "THURS", 5 : "FRI"}
    
    def __init__(self, slot, week, day, start, end, modCode) :
        self.slot = slot
        self.week = week
        self.day = day
        self.start = start
        self.end = end
        self.mod = modCode

    def willClash(self, b) :
        if (len(list(set(self.week) & set(b.week))) == 0) :
            # Case of no weeks that can clash
            return False

            # Case of at least 1 week that can clash
        # b clashes with this class if
        return ((self.day == b.day) 
            and ((self.start <= b.start < self.end) 
            or (self.start < b.end <= self.end)
            or (b.start <= self.start <= b.end and b.start <= self.end <= b.end)))
        #((self.start <= b.start < self.end) or (self.start < b.end <= self.end)) and (self.day == b.day)

class Sectional_Teaching(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " SEC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"

    def __repr__(self) -> str:
        return self.mod + " SEC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Sectional_Teaching) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)

class Seminar(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " SEM " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"

    def __repr__(self) -> str:
        return self.mod + " SEM " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Seminar) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)

class Lecture(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " LEC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"

    def __repr__(self) -> str:
        return self.mod + " LEC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Lecture) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)

class Lab(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " LAB " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"

    def __repr__(self) -> str:
        return self.mod + " LAB " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Lab) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)

class Tutorial(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " TUT " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"

    def __repr__(self) -> str:
        return self.mod + " TUT " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Tutorial) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)

class Recitation(NUSClass) :

    def __init__(self, slot, week, day, start, end, mod) :
        super().__init__(slot, week, day, start, end, mod)

    def __str__(self) :
        return self.mod + " REC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def __repr__(self) -> str:
        return self.mod + " REC " + str(self.slot) + " @ " + str(self.start) + " - " + str(self.end) + "[" + self.WEEKDAYS[self.day] + "]"
    
    def isSameSlot(self, other) :
        return isinstance(other, Recitation) and (other.slot == self.slot)

    def willClash(self, b):
        return super().willClash(b)
