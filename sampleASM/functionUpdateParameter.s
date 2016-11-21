valid/function/simple_functions/functionUpdateParameter.wacc
calling the reference compiler on valid/function/simple_functions/functionUpdateParameter.wacc
-- Test: functionUpdateParameter.wacc

-- Uploaded file: 
---------------------------------------------------------------
# test that the passed parameter can be updated and used
# and that y remains the same

# Output:
# y is 1
# x is 1
# x is now 5
# y is still 1

# Program:

begin

  int f(int x) is
    print "x is ";
    println x;
    x = 5;
    print "x is now ";
    println x;
    return x
  end

  int y = 1;
  print "y is ";
  println y;
  int x = call f(y);
  print "y is still ";
  println y
end

---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionUpdateParameter.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 5
4		.ascii	"x is "
5	msg_1:
6		.word 9
7		.ascii	"x is now "
8	msg_2:
9		.word 5
10		.ascii	"y is "
11	msg_3:
12		.word 11
13		.ascii	"y is still "
14	msg_4:
15		.word 5
16		.ascii	"%.*s\0"
17	msg_5:
18		.word 3
19		.ascii	"%d\0"
20	msg_6:
21		.word 1
22		.ascii	"\0"
23	
24	.text
25	
26	.global main
27	f_f:
28		PUSH {lr}
29		LDR r4, =msg_0
30		MOV r0, r4
31		BL p_print_string
32		LDR r4, [sp, #4]
33		MOV r0, r4
34		BL p_print_int
35		BL p_print_ln
36		LDR r4, =5
37		STR r4, [sp, #4]
38		LDR r4, =msg_1
39		MOV r0, r4
40		BL p_print_string
41		LDR r4, [sp, #4]
42		MOV r0, r4
43		BL p_print_int
44		BL p_print_ln
45		LDR r4, [sp, #4]
46		MOV r0, r4
47		POP {pc}
48		.ltorg
49	main:
50		PUSH {lr}
51		SUB sp, sp, #8
52		LDR r4, =1
53		STR r4, [sp, #4]
54		LDR r4, =msg_2
55		MOV r0, r4
56		BL p_print_string
57		LDR r4, [sp, #4]
58		MOV r0, r4
59		BL p_print_int
60		BL p_print_ln
61		LDR r4, [sp, #4]
62		STR r4, [sp, #-4]!
63		BL f_f
64		ADD sp, sp, #4
65		MOV r4, r0
66		STR r4, [sp]
67		LDR r4, =msg_3
68		MOV r0, r4
69		BL p_print_string
70		LDR r4, [sp, #4]
71		MOV r0, r4
72		BL p_print_int
73		BL p_print_ln
74		ADD sp, sp, #8
75		LDR r0, =0
76		POP {pc}
77		.ltorg
78	p_print_string:
79		PUSH {lr}
80		LDR r1, [r0]
81		ADD r2, r0, #4
82		LDR r0, =msg_4
83		ADD r0, r0, #4
84		BL printf
85		MOV r0, #0
86		BL fflush
87		POP {pc}
88	p_print_int:
89		PUSH {lr}
90		MOV r1, r0
91		LDR r0, =msg_5
92		ADD r0, r0, #4
93		BL printf
94		MOV r0, #0
95		BL fflush
96		POP {pc}
97	p_print_ln:
98		PUSH {lr}
99		LDR r0, =msg_6
100		ADD r0, r0, #4
101		BL puts
102		MOV r0, #0
103		BL fflush
104		POP {pc}
105	
===========================================================
-- Finished

