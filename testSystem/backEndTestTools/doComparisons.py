#!/usr/bin/env python

import subprocess
import os
from os import walk
import filecmp

advancedTests = "valid/advanced"
arrayTests = "valid/array"
basicTests = "valid/basic"
expressionsTests = "valid/expressions"
functionTests = "valid/function"
ifTests = "valid/if"
IOTests = "valid/IO"
pairsTests = "valid/pairs"
runtimeErrTests = "valid/runtimeErr"
scopeTests = "valid/scope"
sequenceTests = "valid/sequence"
variablesTests = "valid/variables"
whileTests = "valid/while"

testTests = [basicTests, pairsTests, whileTests]

currentlyFailingTests = [arrayTests, scopeTests, ifTests, whileTests]

allTests = [arrayTests, basicTests, expressionsTests, #functionTests,
            ifTests, IOTests, pairsTests, runtimeErrTests, scopeTests, sequenceTests,
            variablesTests, whileTests]

def allFiles(path):
  allFiles = []
  for root, subdirs, files in os.walk(path):
    for filename in files:
      if (filename[-5:] == ".wacc"):
        allFiles.append(root + "/" + filename)
  return allFiles

def testName(waccFile):
  noFileExtension = waccFile[:-5]
  return noFileExtension.rsplit('/', 1)[1]

def testInput(waccFile):
  testInputFile = waccFile[:-5] + ".in"
  if (os.path.isfile(testInputFile)):
    return testInputFile
  else:
    return ""

def addTestFile(waccFile, testname):
  callToAddSingleTest = "backEndTestTools/./addSingleTest.sh " + waccFile + " " + testname + " " + testInput(waccFile)
  os.system(callToAddSingleTest)

def areFilesEqual(expected, actual):
  try:
    return str(filecmp.cmp(expected, actual))
  except:
    return "Error"

def testFile(waccFile, testname):
  addTestFile(waccFile, testname)
  refResult = "backEndTestTools/ref" + testname + ".txt"
  ourResult = "backEndTestTools/our" + testname + ".txt"
  os.system("truncate -s -1 " + refResult)
  os.system("truncate -s -1 " + ourResult)
  testResult = areFilesEqual(refResult, ourResult)
  print(testname + " : " + testResult)
  return (testResult != "True")

def compareResults(path):
  filenames = allFiles(path)
  failures = 0
  for filename in filenames:
    failures += testFile(filename, testName(filename))
  return failures

def testFilesInDir(path):
  print("\n-- " + path + " --\n")
  failures = compareResults(path)
  print(path.upper()[6:] + " TESTS COMPLETED, FAILURES: " + str(failures))
  os.system("rm -f *.s")
  os.system("rm -f *.txt")
  os.system("rm -f backEndTestTools/*.txt")
  os.system("rm -f exe*")
  return failures

print("============ TESTS ============")
print("True = pass | False = fail | Error = get memed kid")

failures = 0
for test in allTests:
  failures += testFilesInDir(test)

print("\nALL TESTS COMPLETED, FAILURES: " + str(failures))

