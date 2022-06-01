from z3 import *
from Constraint import *

class Options :

    def __init__(self, array_options) :
        self.options = options
        self.settings_dict = {
            "no8am" : False ,\
            "breaks_between_lessons" : False,\
            "onedayfree" : False\
                }

    def set_options_dict(self) :
        for option in self.options :
            self.settings_dict[option] = True
        return self.settings_dict
