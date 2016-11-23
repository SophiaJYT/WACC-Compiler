valid/function/simple_functions/functionReturnPair.wacc
calling the reference compiler on valid/function/simple_functions/functionReturnPair.wacc
-- Test: functionReturnPair.wacc

-- Uploaded file: 
---------------------------------------------------------------
# creates a pair which is returned from a function

# Output:
# 10

# Program:

begin

  pair(int, int) getPair() is
    pair(int, int) p = newpair(10,15);
    return p
  end

  pair(int, int) p = call getPair();
  int x = fst p;
  println x
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionReturnPair.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 50
4		.ascii	"NullReferenceError: dereference a null reference\n\0"
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
18	f_getPair:
19		PUSH {lr}
20		SUB sp, sp, #4
21		LDR r0, =8
22		BL malloc
23		MOV r4, r0
24		LDR r5, =10
25		LDR r0, =4
26		BL malloc
27		STR r5, [r0]
28		STR r0, [r4]
29		LDR r5, =15
30		LDR r0, =4
31		BL malloc
32		STR r5, [r0]
33		STR r0, [r4, #4]
34		STR r4, [sp]
35		LDR r4, [sp]
36		MOV r0, r4
37		ADD sp, sp, #4
38		POP {pc}
39		.ltorg
40	main:
41		PUSH {lr}
42		SUB sp, sp, #8
43		BL f_getPair
44		MOV r4, r0
45		STR r4, [sp, #4]
46		LDR r4, [sp, #4]
47		MOV r0, r4
48		BL p_check_null_pointer
49		LDR r4, [r4]
50		LDR r4, [r4]
51		STR r4, [sp]
52		LDR r4, [sp]
53		MOV r0, r4
54		BL p_print_int
55		BL p_print_ln
56		ADD sp, sp, #8
57		LDR r0, =0
58		POP {pc}
59		.ltorg
60	p_check_null_pointer:
61		PUSH {lr}
62		CMP r0, #0
63		LDREQ r0, =msg_0
64		BLEQ p_throw_runtime_error
65		POP {pc}
66	p_print_int:
67		PUSH {lr}
68		MOV r1, r0
69		LDR r0, =msg_1
70		ADD r0, r0, #4
71		BL printf
72		MOV r0, #0
73		BL fflush
74		POP {pc}
75	p_print_ln:
76		PUSH {lr}
77		LDR r0, =msg_2
78		ADD r0, r0, #4
79		BL puts
80		MOV r0, #0
81		BL fflush
82		POP {pc}
83	p_throw_runtime_error:
84		BL p_print_string
85		MOV r0, #-1
86		BL exit
87	p_print_string:
88		PUSH {lr}
89		LDR r1, [r0]
90		ADD r2, r0, #4
91		LDR r0, =msg_3
92		ADD r0, r0, #4
93		BL printf
94		MOV r0, #0
95		BL fflush
96		POP {pc}
97	
===========================================================
-- Finished

