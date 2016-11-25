#!/bin/bash

file="$1"
filebase=$(basename "$file")
exe="${filebase%.*}"
asmfile="${filebase%.*}.s"

make
./compile "$1"
arm-linux-gnueabi-gcc -o "$exe" -mcpu=arm1176jzf-s -mtune=arm1176jzf-s "$asmfile"
qemu-arm -L /usr/arm-linux-gnueabi/ "$exe"
