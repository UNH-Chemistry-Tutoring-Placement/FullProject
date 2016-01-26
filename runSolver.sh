#!/usr/bin/env bash
touch ./Files/complete-input
touch ./Files/solution
cat ./Files/objFile ./Files/classFile ./Files/students > ./Files/complete-input
cd Demo
./run.sh "tree" ../Files/complete-input ../Files/solution 10