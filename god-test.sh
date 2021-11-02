#!/usr/bin/bash

javac -d . *.java 

for level in 1_1 1_2 1_3 2_1 2_2 2_3 2_4 2_4 3_1 3_2
do
    echo Testing level $level...
    java Main${level:0:1} < level$level.in | diff - level$level.out -w
    echo
done

