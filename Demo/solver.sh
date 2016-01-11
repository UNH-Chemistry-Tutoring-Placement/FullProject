#!/bin/bash
alg=$1
input=$2
output=$3

if [ -z "$1" ]
then
    echo "Did not enter an algorithm. Possible options are either ILDS or Local"
    exit
fi
if [ -z "$2" ]
then
    echo "Did not enter an input file."
    exit
fi
if [ -z "$3" ]
then
    echo "Did not enter an output file."
    exit
fi
if [ -z "$4" ]
then
    echo "Did not enter a timeout"
    exit
fi

./make.sh
./run.sh $1 $2 $3 $4
