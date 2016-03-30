#!/bin/bash
string="ILDS"

alg=$1
input=$2
output=$3
timeout=$4


    touch ../Files/temp
    chmod +x ../Files/temp

   # cd ../Solver/
   # printf "tree search\n"
   # ./run.sh $timeout $input > ../Files/temp

    printf "local search\n"
    #cat $input ../Files/temp | node ../sLocal-Search-master/slocalsearch.js $timeout $output "improve"

    #node ../sLocal-Search-master/slocalsearch.js $timeout $output "regular" < $input &

    cd ..
    java LocalSearch.LocalSearch $timeout $output < $input

    #while [ $timeout -gt 0 ]; do
     #  echo -ne "Time remaining: $timeout\033[0K\r"
      # sleep 1
       #: $((timeout--))
    #done

    #wait
    printf "done\n"

    cd ./Validator
    ./run.sh $input $output


