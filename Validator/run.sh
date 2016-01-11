#!/bin/bash
string="ILDS"

alg=$1
input=$2
output=$3

if [ "$alg" == "ILDS" ]
then
	cat ../Demo/$input ../Demo/$output | java Validate
else	
	cat ../Demo/$input ../Demo/$output | java Validate
fi
