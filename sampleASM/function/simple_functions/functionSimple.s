valid/function/simple_functions/functionSimple.wacc
calling the reference compiler on valid/function/simple_functions/functionSimple.wacc
-- Test: functionSimple.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a simple function definition and call

# Output:
# 0

# Program:

begin
  int f() is
    return 0 
  end
  int x = call f() ;
  println x 
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionSimple.s contents are:
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
14		LDR r4, =0
15		MOV r0, r4
16		POP {pc}
17		.ltorg
18	main:
19		PUSH {lr}
20		SUB sp, sp, #4
21		BL f_f
22		MOV r4, r0
23		STR r4, [sp]
24		LDR r4, [sp]
25		MOV r0, r4
26		BL p_print_int
27		BL p_print_ln
28		ADD sp, sp, #4
29		LDR r0, =0
30		POP {pc}
31		.ltorg
32	p_print_int:
33		PUSH {lr}
34		MOV r1, r0
35		LDR r0, =msg_0
36		ADD r0, r0, #4
37		BL printf
38		MOV r0, #0
39		BL fflush
40		POP {pc}
41	p_print_ln:
42		PUSH {lr}
43		LDR r0, =msg_1
44		ADD r0, r0, #4
45		BL puts
46		MOV r0, #0
47		BL fflush
48		POP {pc}
49	
===========================================================
-- Finished

