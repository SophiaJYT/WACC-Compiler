valid/function/simple_functions/incFunction.wacc
calling the reference compiler on valid/function/simple_functions/incFunction.wacc
-- Test: incFunction.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a simple increment function definition and usage

# Output:
# 1
# 4

# Program:

begin
  int inc(int x) is
    return x + 1
  end
  int x = 0 ;
  x = call inc(x) ;
  println x ;
  x = call inc(x) ;
  x = call inc(x) ;
  x = call inc(x) ;
  println x
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
incFunction.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 82
4		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
5	msg_1:
6		.word 3
7		.ascii	"%d\0"
8	msg_2:
9		.word 1
10		.ascii	"\0"
11	msg_3:
12		.word 5
13		.ascii	"%.*s\0"
14	
15	.text
16	
17	.global main
18	f_inc:
19		PUSH {lr}
20		LDR r4, [sp, #4]
21		LDR r5, =1
22		ADDS r4, r4, r5
23		BLVS p_throw_overflow_error
24		MOV r0, r4
25		POP {pc}
26		.ltorg
27	main:
28		PUSH {lr}
29		SUB sp, sp, #4
30		LDR r4, =0
31		STR r4, [sp]
32		LDR r4, [sp]
33		STR r4, [sp, #-4]!
34		BL f_inc
35		ADD sp, sp, #4
36		MOV r4, r0
37		STR r4, [sp]
38		LDR r4, [sp]
39		MOV r0, r4
40		BL p_print_int
41		BL p_print_ln
42		LDR r4, [sp]
43		STR r4, [sp, #-4]!
44		BL f_inc
45		ADD sp, sp, #4
46		MOV r4, r0
47		STR r4, [sp]
48		LDR r4, [sp]
49		STR r4, [sp, #-4]!
50		BL f_inc
51		ADD sp, sp, #4
52		MOV r4, r0
53		STR r4, [sp]
54		LDR r4, [sp]
55		STR r4, [sp, #-4]!
56		BL f_inc
57		ADD sp, sp, #4
58		MOV r4, r0
59		STR r4, [sp]
60		LDR r4, [sp]
61		MOV r0, r4
62		BL p_print_int
63		BL p_print_ln
64		ADD sp, sp, #4
65		LDR r0, =0
66		POP {pc}
67		.ltorg
68	p_throw_overflow_error:
69		LDR r0, =msg_0
70		BL p_throw_runtime_error
71	p_print_int:
72		PUSH {lr}
73		MOV r1, r0
74		LDR r0, =msg_1
75		ADD r0, r0, #4
76		BL printf
77		MOV r0, #0
78		BL fflush
79		POP {pc}
80	p_print_ln:
81		PUSH {lr}
82		LDR r0, =msg_2
83		ADD r0, r0, #4
84		BL puts
85		MOV r0, #0
86		BL fflush
87		POP {pc}
88	p_throw_runtime_error:
89		BL p_print_string
90		MOV r0, #-1
91		BL exit
92	p_print_string:
93		PUSH {lr}
94		LDR r1, [r0]
95		ADD r2, r0, #4
96		LDR r0, =msg_3
97		ADD r0, r0, #4
98		BL printf
99		MOV r0, #0
100		BL fflush
101		POP {pc}
102	
===========================================================
-- Finished

