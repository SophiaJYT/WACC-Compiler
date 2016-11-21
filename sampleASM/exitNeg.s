valid/basic/exitNeg.wacc
calling the reference compiler on valid/basic/exitNeg.wacc
-- Test: exitNeg.wacc

-- Uploaded file: 
---------------------------------------------------------------
# common error exit statement

# Output:
# #empty#

# Exit:
# 255

# Program:

begin
  exit -1
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
exitNeg.s contents are:
===========================================================
0	.text
1	
2	.global main
3	main:
4		PUSH {lr}
5		LDR r4, =-1
6		MOV r0, r4
7		BL exit
8		LDR r0, =0
9		POP {pc}
10		.ltorg
11	
===========================================================
-- Finished

