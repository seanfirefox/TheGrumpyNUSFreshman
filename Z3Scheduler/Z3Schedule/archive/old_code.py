''' 
This is where I store old code
'''
for day in self.lessons_by_day :
            for NUSCLASS in day :
                candidates =  list(filter(lambda x : x.willClash(NUSCLASS) and x is not NUSCLASS, day))
                #print("\n")
                #print(NUSCLASS)
                #print(candidates)
                #print("\n")
                antecedant = self.string_to_bool_literal[str(NUSCLASS)]
                clashLiteral = list(map(lambda x : Not(self.string_to_bool_literal[str(x)]), candidates))
                self.solver.add(Implies(antecedant, And(clashLiteral)))

def chooseExactlyOneLiteral(self, literalList) :
        #self.s.add(AtMost(*literalList, 1))
        #self.s.add(AtLeast(*literalList, 1))
        if (len(literalList) == 0) :
            return
        elif (len(literalList) == 1) :
            self.solver.add(literalList[0] == True)
        else :
            self.solver.add(Or(literalList))
            for pair in list(combinations(literalList,2)):
                self.solver.add(Not(And(pair)))

def chooseExactlyOneWithPairs(self, lst) :
        if (len(lst) == 0) : return
        elif (len(lst) == 1) :
            self.solver.add(self.string_to_bool_literal[str(lst[0])] == True)
            return
        else :  
            setsOfPairs = set()
            literalList = []
            for item in lst :
                candidates = list(filter(lambda x : x.slot == item.slot, lst))
                converts = frozenset(map(lambda x : self.string_to_bool_literal[str(x)] , candidates))
                setsOfPairs.add(converts)
            for pair in setsOfPairs :
                if len(pair) == 1 :
                    literalList.append(list(pair)[0])
                else :
                    literalList.append(And(list(pair)))
            self.chooseExactlyOneLiteral(literalList)

def chooseExactlyOne(self, lst) :
        if (len(lst) == 0) :
            return
        elif (len(lst) == 1) :
            self.solver.add(self.string_to_bool_literal[str(lst[0])])
            return
        AndList = []
        for c in lst :
            candidates = list(filter(lambda t : t is not c, lst))
            shortlist = list(map(lambda x : Not(self.string_to_bool_literal[str(x)]), candidates))
            AndList.append(And([self.string_to_bool_literal[str(c)]] + shortlist))
        self.solver.add(Or(AndList))
