valid/function/simple_functions/functionManyArguments.wacc
calling the reference compiler on valid/function/simple_functions/functionManyArguments.wacc
-- Test: functionManyArguments.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a function with varied inputs

# Output:
# a is 42
# b is true
# c is u
# d is hello
# e is #addrs#
# f is #addrs#
# answer is g

# Program:

begin
  char doSomething(int a, bool b, char c, string d, bool[] e, int[] f) is
    print "a is " ;
    println a ;
    print "b is " ;
    println b ;
    print "c is " ;
    println c ;
    print "d is " ;
    println d ;
    print "e is " ;
    println e ;
    print "f is " ;
    println f ;
    return 'g'
  end
  bool[] bools = [ false, true ] ;
  int[] ints = [ 1, 2 ] ;
  char answer = call doSomething(42, true, 'u', "hello", bools, ints) ;
  print "answer is " ;
  println answer
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionManyArguments.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 5
4		.ascii	"a is "
5	msg_1:
6		.word 5
7		.ascii	"b is "
8	msg_2:
9		.word 5
10		.ascii	"c is "
11	msg_3:
12		.word 5
13		.ascii	"d is "
14	msg_4:
15		.word 5
16		.ascii	"e is "
17	msg_5:
18		.word 5
19		.ascii	"f is "
20	msg_6:
21		.word 5
22		.ascii	"hello"
23	msg_7:
24		.word 10
25		.ascii	"answer is "
26	msg_8:
27		.word 5
28		.ascii	"%.*s\0"
29	msg_9:
30		.word 3
31		.ascii	"%d\0"
32	msg_10:
33		.word 1
34		.ascii	"\0"
35	msg_11:
36		.word 5
37		.ascii	"true\0"
38	msg_12:
39		.word 6
40		.ascii	"false\0"
41	msg_13:
42		.word 3
43		.ascii	"%p\0"
44	
45	.text
46	
47	.global main
48	f_doSomething:
49		PUSH {lr}
50		LDR r4, =msg_0
51		MOV r0, r4
52		BL p_print_string
53		LDR r4, [sp, #4]
54		MOV r0, r4
55		BL p_print_int
56		BL p_print_ln
57		LDR r4, =msg_1
58		MOV r0, r4
59		BL p_print_string
60		LDRSB r4, [sp, #8]
61		MOV r0, r4
62		BL p_print_bool
63		BL p_print_ln
64		LDR r4, =msg_2
65		MOV r0, r4
66		BL p_print_string
67		LDRSB r4, [sp, #9]
68		MOV r0, r4
69		BL putchar
70		BL p_print_ln
71		LDR r4, =msg_3
72		MOV r0, r4
73		BL p_print_string
74		LDR r4, [sp, #10]
75		MOV r0, r4
76		BL p_print_string
77		BL p_print_ln
78		LDR r4, =msg_4
79		MOV r0, r4
80		BL p_print_string
81		LDR r4, [sp, #14]
82		MOV r0, r4
83		BL p_print_reference
84		BL p_print_ln
85		LDR r4, =msg_5
86		MOV r0, r4
87		BL p_print_string
88		LDR r4, [sp, #18]
89		MOV r0, r4
90		BL p_print_reference
91		BL p_print_ln
92		MOV r4, #'g'
93		MOV r0, r4
94		POP {pc}
95		.ltorg
96	main:
97		PUSH {lr}
98		SUB sp, sp, #9
99		LDR r0, =6
100		BL malloc
101		MOV r4, r0
102		MOV r5, #0
103		STRB r5, [r4, #4]
104		MOV r5, #1
105		STRB r5, [r4, #5]
106		LDR r5, =2
107		STR r5, [r4]
108		STR r4, [sp, #5]
109		LDR r0, =12
110		BL malloc
111		MOV r4, r0
112		LDR r5, =1
113		STR r5, [r4, #4]
114		LDR r5, =2
115		STR r5, [r4, #8]
116		LDR r5, =2
117		STR r5, [r4]
118		STR r4, [sp, #1]
119		LDR r4, [sp, #1]
120		STR r4, [sp, #-4]!
121		LDR r4, [sp, #9]
122		STR r4, [sp, #-4]!
123		LDR r4, =msg_6
124		STR r4, [sp, #-4]!
125		MOV r4, #'u'
126		STRB r4, [sp, #-1]!
127		MOV r4, #1
128		STRB r4, [sp, #-1]!
129		LDR r4, =42
130		STR r4, [sp, #-4]!
131		BL f_doSomething
132		ADD sp, sp, #18
133		MOV r4, r0
134		STRB r4, [sp]
135		LDR r4, =msg_7
136		MOV r0, r4
137		BL p_print_string
138		LDRSB r4, [sp]
139		MOV r0, r4
140		BL putchar
141		BL p_print_ln
142		ADD sp, sp, #9
143		LDR r0, =0
144		POP {pc}
145		.ltorg
146	p_print_string:
147		PUSH {lr}
148		LDR r1, [r0]
149		ADD r2, r0, #4
150		LDR r0, =msg_8
151		ADD r0, r0, #4
152		BL printf
153		MOV r0, #0
154		BL fflush
155		POP {pc}
156	p_print_int:
157		PUSH {lr}
158		MOV r1, r0
159		LDR r0, =msg_9
160		ADD r0, r0, #4
161		BL printf
162		MOV r0, #0
163		BL fflush
164		POP {pc}
165	p_print_ln:
166		PUSH {lr}
167		LDR r0, =msg_10
168		ADD r0, r0, #4
169		BL puts
170		MOV r0, #0
171		BL fflush
172		POP {pc}
173	p_print_bool:
174		PUSH {lr}
175		CMP r0, #0
176		LDRNE r0, =msg_11
177		LDREQ r0, =msg_12
178		ADD r0, r0, #4
179		BL printf
180		MOV r0, #0
181		BL fflush
182		POP {pc}
183	p_print_reference:
184		PUSH {lr}
185		MOV r1, r0
186		LDR r0, =msg_13
187		ADD r0, r0, #4
188		BL printf
189		MOV r0, #0
190		BL fflush
191		POP {pc}
192	
===========================================================
-- Finished

