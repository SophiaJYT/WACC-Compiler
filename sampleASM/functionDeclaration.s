valid/function/simple_functions/functionDeclaration.wacc
calling the reference compiler on valid/function/simple_functions/functionDeclaration.wacc
-- Test: functionDeclaration.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a simple function is declared, but not called

# Output:
# #empty#

# Program:

begin
  int f() is
    return 0 
  end
  skip
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionDeclaration.s contents are:
===========================================================
0	.text
1	
2	.global main
3	f_f:
4		PUSH {lr}
5		LDR r4, =0
6		MOV r0, r4
7		POP {pc}
8		.ltorg
9	main:
10		PUSH {lr}
11		LDR r0, =0
12		POP {pc}
13		.ltorg
14	
===========================================================
-- Finished

