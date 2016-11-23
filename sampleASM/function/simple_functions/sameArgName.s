valid/function/simple_functions/sameArgName.wacc
calling the reference compiler on valid/function/simple_functions/sameArgName.wacc
-- Test: sameArgName.wacc

-- Uploaded file: 
---------------------------------------------------------------
# program with function that has same parameter name as function

# Output:
# 99

# Program:

begin
  int f(int f) is
    return f
  end
  
  int f = call f(99);
  println f
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
sameArgName.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 3
4		.ascii	"%d\0"
5	msg_1:
6		.word 1
7		.ascii	"\0"
8	
9	.text
10	
11	.global main
12	f_f:
13		PUSH {lr}
14		LDR r4, [sp, #4]
15		MOV r0, r4
16		POP {pc}
17		.ltorg
18	main:
19		PUSH {lr}
20		SUB sp, sp, #4
21		LDR r4, =99
22		STR r4, [sp, #-4]!
23		BL f_f
24		ADD sp, sp, #4
25		MOV r4, r0
26		STR r4, [sp]
27		LDR r4, [sp]
28		MOV r0, r4
29		BL p_print_int
30		BL p_print_ln
31		ADD sp, sp, #4
32		LDR r0, =0
33		POP {pc}
34		.ltorg
35	p_print_int:
36		PUSH {lr}
37		MOV r1, r0
38		LDR r0, =msg_0
39		ADD r0, r0, #4
40		BL printf
41		MOV r0, #0
42		BL fflush
43		POP {pc}
44	p_print_ln:
45		PUSH {lr}
46		LDR r0, =msg_1
47		ADD r0, r0, #4
48		BL puts
49		MOV r0, #0
50		BL fflush
51		POP {pc}
52	
===========================================================
-- Finished

