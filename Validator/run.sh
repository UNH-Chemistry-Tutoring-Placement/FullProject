#!/bin/bash
string="ILDS"

alg=$1
input=$2
output=$3

if [ "$alg" == "ILDS" ]
then
	cat ../Files/$input ../Files/$output | java Validate
else	
	cat ../Files/$input ../Files/$output | java Validate
fi
