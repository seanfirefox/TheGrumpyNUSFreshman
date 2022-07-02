#usr/bin/bash

echo "BIG TEST 1 : TO DETECT POSSIBLE TIMETABLES"
python3 test_sat.py

echo "BIG TEST 2 : TO DETECT IMPOSSIBLE TIMETABLES"
python3 test_unsat.py

echo "STRESS TEST 1 : SEE IF IT IS GOOD UNDER HEAVY WORKLOAD"
python3 stress_test.py
