valid/function/nested_functions/fibonacciFullRec.wacc
calling the reference compiler on valid/function/nested_functions/fibonacciFullRec.wacc
-- Test: fibonacciFullRec.wacc

-- Uploaded file: 
---------------------------------------------------------------
# recursively calculate the nth fibonacci number

# Output:
# This program calculates the nth fibonacci number recursively.
# Please enter n (should not be too large): #input#
# The input n is #output#
# The nth fibonacci number is #output#

# Program:

begin
  int fibonacci(int n) is
    if n <= 1
    then
      return n
    else
      skip
    fi ;
    int f1 = call fibonacci(n - 1) ;
    int f2 = call fibonacci(n - 2) ;
    return f1 + f2
  end

  println "This program calculates the nth fibonacci number recursively." ;
  print "Please enter n (should not be too large): " ;
  int n = 0;
  read n ;
  print "The input n is " ;
  println n ;
  print "The nth fibonacci number is " ;
  int result = call fibonacci(n) ;
  println  result
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
fibonacciFullRec.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 61
4		.ascii	"This program calculates the nth fibonacci number recursively."
5	msg_1:
6		.word 42
7		.ascii	"Please enter n (should not be too large): "
8	msg_2:
9		.word 15
10		.ascii	"The input n is "
11	msg_3:
12		.word 28
13		.ascii	"The nth fibonacci number is "
14	msg_4:
15		.word 82
16		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
17	msg_5:
18		.word 5
19		.ascii	"%.*s\0"
20	msg_6:
21		.word 1
22		.ascii	"\0"
23	msg_7:
24		.word 3
25		.ascii	"%d\0"
26	msg_8:
27		.word 3
28		.ascii	"%d\0"
29	
30	.text
31	
32	.global main
33	f_fibonacci:
34		PUSH {lr}
35		SUB sp, sp, #8
36		LDR r4, [sp, #12]
37		LDR r5, =1
38		CMP r4, r5
39		MOVLE r4, #1
40		MOVGT r4, #0
41		CMP r4, #0
42		BEQ L0
43		LDR r4, [sp, #12]
44		MOV r0, r4
45		ADD sp, sp, #8
46		B L1
47	L0:
48	L1:
49		LDR r4, [sp, #12]
50		LDR r5, =1
51		SUBS r4, r4, r5
52		BLVS p_throw_overflow_error
53		STR r4, [sp, #-4]!
54		BL f_fibonacci
55		ADD sp, sp, #4
56		MOV r4, r0
57		STR r4, [sp, #4]
58		LDR r4, [sp, #12]
59		LDR r5, =2
60		SUBS r4, r4, r5
61		BLVS p_throw_overflow_error
62		STR r4, [sp, #-4]!
63		BL f_fibonacci
64		ADD sp, sp, #4
65		MOV r4, r0
66		STR r4, [sp]
67		LDR r4, [sp, #4]
68		LDR r5, [sp]
69		ADDS r4, r4, r5
70		BLVS p_throw_overflow_error
71		MOV r0, r4
72		ADD sp, sp, #8
73		POP {pc}
74		.ltorg
75	main:
76		PUSH {lr}
77		SUB sp, sp, #8
78		LDR r4, =msg_0
79		MOV r0, r4
80		BL p_print_string
81		BL p_print_ln
82		LDR r4, =msg_1
83		MOV r0, r4
84		BL p_print_string
85		LDR r4, =0
86		STR r4, [sp, #4]
87		ADD r4, sp, #4
88		MOV r0, r4
89		BL p_read_int
90		LDR r4, =msg_2
91		MOV r0, r4
92		BL p_print_string
93		LDR r4, [sp, #4]
94		MOV r0, r4
95		BL p_print_int
96		BL p_print_ln
97		LDR r4, =msg_3
98		MOV r0, r4
99		BL p_print_string
100		LDR r4, [sp, #4]
101		STR r4, [sp, #-4]!
102		BL f_fibonacci
103		ADD sp, sp, #4
104		MOV r4, r0
105		STR r4, [sp]
106		LDR r4, [sp]
107		MOV r0, r4
108		BL p_print_int
109		BL p_print_ln
110		ADD sp, sp, #8
111		LDR r0, =0
112		POP {pc}
113		.ltorg
114	p_throw_overflow_error:
115		LDR r0, =msg_4
116		BL p_throw_runtime_error
117	p_print_string:
118		PUSH {lr}
119		LDR r1, [r0]
120		ADD r2, r0, #4
121		LDR r0, =msg_5
122		ADD r0, r0, #4
123		BL printf
124		MOV r0, #0
125		BL fflush
126		POP {pc}
127	p_print_ln:
128		PUSH {lr}
129		LDR r0, =msg_6
130		ADD r0, r0, #4
131		BL puts
132		MOV r0, #0
133		BL fflush
134		POP {pc}
135	p_read_int:
136		PUSH {lr}
137		MOV r1, r0
138		LDR r0, =msg_7
139		ADD r0, r0, #4
140		BL scanf
141		POP {pc}
142	p_print_int:
143		PUSH {lr}
144		MOV r1, r0
145		LDR r0, =msg_8
146		ADD r0, r0, #4
147		BL printf
148		MOV r0, #0
149		BL fflush
150		POP {pc}
151	p_throw_runtime_error:
152		BL p_print_string
153		MOV r0, #-1
154		BL exit
155	
===========================================================
-- Finished

