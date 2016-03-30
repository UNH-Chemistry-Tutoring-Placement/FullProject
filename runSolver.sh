#!/usr/bin/env bash
runTime=$1
touch ./Files/complete-input
touch ./Files/solution
chmod +x ./Files/solution
cat ./Files/objFile ./Files/classFile ./Files/students > ./Files/complete-input
cd Demo
printf "solving $runTime for seconds\n"
./run.sh "Local" ./Files/complete-input ./Files/solution $runTime