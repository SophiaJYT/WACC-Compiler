#!/bin/bash

result="tests.txt"
testfile="test"
temp="temp"
stdout="stdout"

> $result
make
for file in $(find $1 -name "*.wacc")
do
  echo "Testing $file" > $testfile
  ./compile $file > $stdout 2> $temp
  cat $temp >> $testfile
  echo "=====DONE=====" >> $testfile
  [ -s $temp ] && cat $testfile >> $result
done
rm -f $temp $stdout $testfile
