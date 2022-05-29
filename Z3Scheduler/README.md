# Z3 Timetable

This timetable scheduler was built originally by Tan Kai Min, Russell and then additionally modified by Tan Li Thai Sean.
It uses the Z3 SMT Solver built by Microsoft Research.
We used an SMT Solver because timetable scheduling could be seen as a constraint satisfaction task which a SMT solver can help.

## Requirements

```bash
Python 3
z3
z3-solver
json
requests
```

## To Run
```bash
python3 TimetableZ3run.py module_code1 module_code2 module_code3 ...
```

## Sample I/O (SAT)

```bash
python3 TimetableZ3run.py CS2030S CS2040S ST2334 MA2104 CM1102 IS1103
```

will output a timetable like

```bash
You have entered 6

Enter Academic Year in the form 2021-2022
2021-2022
Input semester number
2
CS2030S offered for chosen semester. Processing . . . .
CS2040S offered for chosen semester. Processing . . . .
MA2104 offered for chosen semester. Processing . . . .
ST2334 offered for chosen semester. Processing . . . .
IS1103 offered for chosen semester. Processing . . . .
CM1102 offered for chosen semester. Processing . . . .
Scrapping Successful
SAT
On Monday
 ST2334 TUT 1 on Day 1 @ 900 - 1000
 MA2104 TUT 2 on Day 1 @ 1000 - 1100
 CM1102 TUT 1 on Day 1 @ 1100 - 1200
 CS2030S LEC 1 on Day 1 @ 1200 - 1400
 CS2040S LEC 1 on Day 1 @ 1600 - 1800
On Tuesday
 ST2334 LEC 3 on Day 2 @ 800 - 1000
 CS2040S TUT 05 on Day 2 @ 1100 - 1300
On Wednesday
 CS2030S REC 02 on Day 3 @ 900 - 1000
 MA2104 LEC 1 on Day 3 @ 1000 - 1200
 CM1102 LEC 1 on Day 3 @ 1200 - 1400
 CS2040S LEC 1 on Day 3 @ 1400 - 1500
On Thursday
 CS2040S REC 02 on Day 4 @ 1000 - 1100
 CS2030S LAB 12G on Day 4 @ 1200 - 1400
On Friday
 ST2334 LEC 3 on Day 5 @ 800 - 1000
 MA2104 LEC 1 on Day 5 @ 1000 - 1200
 CM1102 LEC 1 on Day 5 @ 1200 - 1400
 ```

# Sample I/O (UNSAT)

```bash
python3 TimetableZ3run.py CS2030S CS2040S MA2108 ST2334 IS1103 CM1102 MA2104
```

when no possible timetable configuration can be generated (due to timing clashes, constraints impossible to fulfill etc), you get the following output

```bash
You have entered 7

Enter Academic Year in the form 2021-2022
2021-2022
Input semester number
2
CS2030S offered for chosen semester. Processing . . . .
CS2040S offered for chosen semester. Processing . . . .
MA2108 offered for chosen semester. Processing . . . .
ST2334 offered for chosen semester. Processing . . . .
IS1103 offered for chosen semester. Processing . . . .
CM1102 offered for chosen semester. Processing . . . .
MA2104 offered for chosen semester. Processing . . . .
Scrapping Successful
UNSAT
No feasible timetable
```

Note : Timetable generated can vary, but whether the generation was possible is deterministic.
