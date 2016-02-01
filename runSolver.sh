#!/usr/bin/env bash
touch ./Files/complete-input
touch ./Files/solution
chmod +x ./Files/solution
cat ./Files/objFile ./Files/classFile ./Files/students > ./Files/complete-input
cd Demo
printf "solving for 10 seconds"
./run.sh "tree" ../Files/complete-input ../Files/solution 10