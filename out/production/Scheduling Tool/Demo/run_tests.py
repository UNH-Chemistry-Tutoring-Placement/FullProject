#!/usr/bin/env python 

import os

times = [1, 5, 10, 20, 50, 100, 200, 400, 800, 1500, 2500, 4500]

for time in times:
    os.system("mkdir -p results")
    output = "results/roster_" + str(time)
    command = "./solver.sh ILDS ../FileFormats/remove_students output_file" + " " + str(time) + " > " + output
    print "Finished " + str(time) + " second test."
    os.system(command)
