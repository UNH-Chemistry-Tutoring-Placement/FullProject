#!/bin/bash
string="ILDS"

input=$1
output=$2
cd ..
cat $input $output | java Validator.Validate
