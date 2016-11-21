#!/bin/bash

result="tests.txt"
assemdir="asmFiles"

> $result
make

if [ ! -d $assemdir ]; then
  echo "Creating directory for storing assembly files..."
  mkdir $assemdir;
fi

for file in $(find $1 -name "*.wacc")
do
  echo "Testing $file" >> $result
  ./compile $file 2>> $result
  filebase=$(basename "$file")
  filename="${filebase%.*}.s"
  if [ -f "$filename" ]; then
    echo "Moving $filename to $assemdir..."
    mv "$filename" $assemdir
  fi
  echo "=====DONE=====" >> $result
done
