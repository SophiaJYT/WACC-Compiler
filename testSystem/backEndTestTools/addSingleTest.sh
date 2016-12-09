#!/bin/sh

waccFile=$1
sFile="$2.s"
refexeFile="exeref$2"
ourexeFile="exeour$2"
reftxtFile="ref$2.txt"
ourtxtFile="our$2.txt"
inputstream="$3"

#===========

# ./getRefAssembly
./getRefAssembly $waccFile > $sFile

# runThem and store results
arm-linux-gnueabi-gcc -o $refexeFile -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $sFile

if [ "$#" -eq 2 ]; then
  qemu-arm -L /usr/arm-linux-gnueabi/ $refexeFile > $reftxtFile
else
  cat $3 | qemu-arm -L /usr/arm-linux-gnueabi/ $refexeFile > $reftxtFile
fi

# remove generated files
rm -f $sFile
rm -f $refexeFile

#===========

./compile $waccFile

# runThem and store results
arm-linux-gnueabi-gcc -o $ourexeFile -mcpu=arm1176jzf-s -mtune=arm1176jzf-s $sFile
if [ "$#" -eq 2 ]; then
  qemu-arm -L /usr/arm-linux-gnueabi/ $ourexeFile > $ourtxtFile
else
  cat $3 | qemu-arm -L /usr/arm-linux-gnueabi/ $ourexeFile > $ourtxtFile
fi

# move results tools directory
mv $reftxtFile backEndTestTools/
mv $ourtxtFile backEndTestTools/

# remove generated files
rm -f $ourexeFile
rm -f $sFile
