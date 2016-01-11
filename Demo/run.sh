#!/bin/bash
string="ILDS"

alg=$1
input=$2
output=$3
timeout=$4

if [ "$alg" == "Local" ]
then
    cd ../sLocal-Search/
    node slocalsearch.js 10 "../Demo/$output" < "../Demo/$input"
    cd ../Validator
    ./run.sh $alg $input $output
else
    cd ../Solver/
    ./run.sh $timeout < "../Demo/$input" > "../Demo/$output"
    cd ../Validator
    ./run.sh $alg $input $output
fi
