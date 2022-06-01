#usr/bin/bash

echo "BIG TEST 1 : TO DETECT POSSIBLE TIMETABLES"
python3 tests/automated/test_sat.py

echo "BIG TEST 2 : TO DETECT IMPOSSIBLE TIMETABLES"
python3 tests/automated/test_unsat.py
