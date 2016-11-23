valid/function/nested_functions/fibonacciRecursive.wacc
calling the reference compiler on valid/function/nested_functions/fibonacciRecursive.wacc
-- Test: fibonacciRecursive.wacc

-- Uploaded file: 
---------------------------------------------------------------
# recursive calculation of the first 20 fibonacci numbers

# Output:
# The first 20 fibonacci numbers are:
# 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181...

# Program:

begin
  int fibonacci(int n, bool toPrint) is
    if n <= 1
    then
      return n
    else
      skip
    fi ;
    int f1 = call fibonacci(n - 1, toPrint) ;
    if toPrint
    then
      print f1 ;
      print ", "
    else
      skip
    fi ;
    int f2 = call fibonacci(n - 2, false) ;
    return f1 + f2
  end

  println "The first 20 fibonacci numbers are:" ;
  print "0, " ;
  int result = call fibonacci(19, true) ;
  print result ;
  println "..."
end

---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
fibonacciRecursive.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 2
4		.ascii	", "
5	msg_1:
6		.word 35
7		.ascii	"The first 20 fibonacci numbers are:"
8	msg_2:
9		.word 3
10		.ascii	"0, "
11	msg_3:
12		.word 3
13		.ascii	"..."
14	msg_4:
15		.word 82
16		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
17	msg_5:
18		.word 3
19		.ascii	"%d\0"
20	msg_6:
21		.word 5
22		.ascii	"%.*s\0"
23	msg_7:
24		.word 1
25		.ascii	"\0"
26	
27	.text
28	
29	.global main
30	f_fibonacci:
31		PUSH {lr}
32		SUB sp, sp, #8
33		LDR r4, [sp, #12]
34		LDR r5, =1
35		CMP r4, r5
36		MOVLE r4, #1
37		MOVGT r4, #0
38		CMP r4, #0
39		BEQ L0
40		LDR r4, [sp, #12]
41		MOV r0, r4
42		ADD sp, sp, #8
43		B L1
44	L0:
45	L1:
46		LDRSB r4, [sp, #16]
47		STRB r4, [sp, #-1]!
48		LDR r4, [sp, #13]
49		LDR r5, =1
50		SUBS r4, r4, r5
51		BLVS p_throw_overflow_error
52		STR r4, [sp, #-4]!
53		BL f_fibonacci
54		ADD sp, sp, #5
55		MOV r4, r0
56		STR r4, [sp, #4]
57		LDRSB r4, [sp, #16]
58		CMP r4, #0
59		BEQ L2
60		LDR r4, [sp, #4]
61		MOV r0, r4
62		BL p_print_int
63		LDR r4, =msg_0
64		MOV r0, r4
65		BL p_print_string
66		B L3
67	L2:
68	L3:
69		MOV r4, #0
70		STRB r4, [sp, #-1]!
71		LDR r4, [sp, #13]
72		LDR r5, =2
73		SUBS r4, r4, r5
74		BLVS p_throw_overflow_error
75		STR r4, [sp, #-4]!
76		BL f_fibonacci
77		ADD sp, sp, #5
78		MOV r4, r0
79		STR r4, [sp]
80		LDR r4, [sp, #4]
81		LDR r5, [sp]
82		ADDS r4, r4, r5
83		BLVS p_throw_overflow_error
84		MOV r0, r4
85		ADD sp, sp, #8
86		POP {pc}
87		.ltorg
88	main:
89		PUSH {lr}
90		SUB sp, sp, #4
91		LDR r4, =msg_1
92		MOV r0, r4
93		BL p_print_string
94		BL p_print_ln
95		LDR r4, =msg_2
96		MOV r0, r4
97		BL p_print_string
98		MOV r4, #1
99		STRB r4, [sp, #-1]!
100		LDR r4, =19
101		STR r4, [sp, #-4]!
102		BL f_fibonacci
103		ADD sp, sp, #5
104		MOV r4, r0
105		STR r4, [sp]
106		LDR r4, [sp]
107		MOV r0, r4
108		BL p_print_int
109		LDR r4, =msg_3
110		MOV r0, r4
111		BL p_print_string
112		BL p_print_ln
113		ADD sp, sp, #4
114		LDR r0, =0
115		POP {pc}
116		.ltorg
117	p_throw_overflow_error:
118		LDR r0, =msg_4
119		BL p_throw_runtime_error
120	p_print_int:
121		PUSH {lr}
122		MOV r1, r0
123		LDR r0, =msg_5
124		ADD r0, r0, #4
125		BL printf
126		MOV r0, #0
127		BL fflush
128		POP {pc}
129	p_print_string:
130		PUSH {lr}
131		LDR r1, [r0]
132		ADD r2, r0, #4
133		LDR r0, =msg_6
134		ADD r0, r0, #4
135		BL printf
136		MOV r0, #0
137		BL fflush
138		POP {pc}
139	p_print_ln:
140		PUSH {lr}
141		LDR r0, =msg_7
142		ADD r0, r0, #4
143		BL puts
144		MOV r0, #0
145		BL fflush
146		POP {pc}
147	p_throw_runtime_error:
148		BL p_print_string
149		MOV r0, #-1
150		BL exit
151	
===========================================================
-- Finished

