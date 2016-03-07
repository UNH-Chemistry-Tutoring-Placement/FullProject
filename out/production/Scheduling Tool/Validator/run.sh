#!/bin/bash
string="ILDS"

input=$1
output=$2

cat ../Files/$input ../Files/$output | java Validate
