valid/function/simple_functions/negFunction.wacc
calling the reference compiler on valid/function/simple_functions/negFunction.wacc
-- Test: negFunction.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a simple negation function definition and usage

# Output:
# true
# false
# true

# Program:

begin
  bool neg(bool b) is
    return !b 
  end
  bool b = true ;
  println b ;
  b = call neg(b) ;
  println b ;
  b = call neg(b) ;
  b = call neg(b) ;
  b = call neg(b) ;
  println b
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
negFunction.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 5
4		.ascii	"true\0"
5	msg_1:
6		.word 6
7		.ascii	"false\0"
8	msg_2:
9		.word 1
10		.ascii	"\0"
11	
12	.text
13	
14	.global main
15	f_neg:
16		PUSH {lr}
17		LDRSB r4, [sp, #4]
18		EOR r4, r4, #1
19		MOV r0, r4
20		POP {pc}
21		.ltorg
22	main:
23		PUSH {lr}
24		SUB sp, sp, #1
25		MOV r4, #1
26		STRB r4, [sp]
27		LDRSB r4, [sp]
28		MOV r0, r4
29		BL p_print_bool
30		BL p_print_ln
31		LDRSB r4, [sp]
32		STRB r4, [sp, #-1]!
33		BL f_neg
34		ADD sp, sp, #1
35		MOV r4, r0
36		STRB r4, [sp]
37		LDRSB r4, [sp]
38		MOV r0, r4
39		BL p_print_bool
40		BL p_print_ln
41		LDRSB r4, [sp]
42		STRB r4, [sp, #-1]!
43		BL f_neg
44		ADD sp, sp, #1
45		MOV r4, r0
46		STRB r4, [sp]
47		LDRSB r4, [sp]
48		STRB r4, [sp, #-1]!
49		BL f_neg
50		ADD sp, sp, #1
51		MOV r4, r0
52		STRB r4, [sp]
53		LDRSB r4, [sp]
54		STRB r4, [sp, #-1]!
55		BL f_neg
56		ADD sp, sp, #1
57		MOV r4, r0
58		STRB r4, [sp]
59		LDRSB r4, [sp]
60		MOV r0, r4
61		BL p_print_bool
62		BL p_print_ln
63		ADD sp, sp, #1
64		LDR r0, =0
65		POP {pc}
66		.ltorg
67	p_print_bool:
68		PUSH {lr}
69		CMP r0, #0
70		LDRNE r0, =msg_0
71		LDREQ r0, =msg_1
72		ADD r0, r0, #4
73		BL printf
74		MOV r0, #0
75		BL fflush
76		POP {pc}
77	p_print_ln:
78		PUSH {lr}
79		LDR r0, =msg_2
80		ADD r0, r0, #4
81		BL puts
82		MOV r0, #0
83		BL fflush
84		POP {pc}
85	
===========================================================
-- Finished

