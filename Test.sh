#!/bin/bash


result="tests.txt"
> $result

for file in $(find $1 -name "*.wacc")
do
  echo "Testing $file" >> $result
  ./compile $file > "stdout.txt" 2>> $result
  echo "=====DONE=====" >> $result
done
