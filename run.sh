#!/usr/bin/env bash
if [ $# -eq 1 ] ; then
    runTime=$1
else
    runTime=10
fi

./runFileIO.sh
./runSolver.sh $runTime