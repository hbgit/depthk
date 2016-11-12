#!/bin/bash

CURDIR=`pwd`
DIRECTORIES=`ls -d */`

echo $DIRECTORIES

for dir in $DIRECTORIES
do
  echo "========================="
  echo $dir
  sed -i 's/k-induction/k-induction-parallel/g' $dir/test.desc
  echo "========================="
done

