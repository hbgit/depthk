#!/bin/bash

# usage ./script.sh dir_cprogram/

echo "Reading: "$1
cd $1
lsdir=($(ls *.c))

for i in "${lsdir[@]}"
do
	#echo $i">>>>>>> \n"
	ouputname=$i".i"
	clang -E $i > $ouputname

done

cd ..
