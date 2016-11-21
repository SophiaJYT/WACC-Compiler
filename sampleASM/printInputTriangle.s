valid/function/nested_functions/printInputTriangle.wacc
calling the reference compiler on valid/function/nested_functions/printInputTriangle.wacc
-- Test: printInputTriangle.wacc

-- Uploaded file: 
---------------------------------------------------------------
# print a user-specified sized triangle

# Output:
# Please enter the size of the triangle to print:
# #input#
# #output#

# Program:

begin
  int f(int x) is
    if x == 0 then
      skip
    else
      int i = x ;
      while i > 0 do 
        print "-" ;
        i = i - 1
      done ;
      println "" ;
      int s = call f(x - 1)
    fi ;
    return 0
  end

  println "Please enter the size of the triangle to print: " ;
  int x = 0;

  read x;
  int s = call f(x) 
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
printInputTriangle.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 1
4		.ascii	"-"
5	msg_1:
6		.word 0
7		.ascii	""
8	msg_2:
9		.word 48
10		.ascii	"Please enter the size of the triangle to print: "
11	msg_3:
12		.word 5
13		.ascii	"%.*s\0"
14	msg_4:
15		.word 82
16		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
17	msg_5:
18		.word 1
19		.ascii	"\0"
20	msg_6:
21		.word 3
22		.ascii	"%d\0"
23	
24	.text
25	
26	.global main
27	f_f:
28		PUSH {lr}
29		LDR r4, [sp, #4]
30		LDR r5, =0
31		CMP r4, r5
32		MOVEQ r4, #1
33		MOVNE r4, #0
34		CMP r4, #0
35		BEQ L0
36		B L1
37	L0:
38		SUB sp, sp, #8
39		LDR r4, [sp, #12]
40		STR r4, [sp, #4]
41		B L2
42	L3:
43		LDR r4, =msg_0
44		MOV r0, r4
45		BL p_print_string
46		LDR r4, [sp, #4]
47		LDR r5, =1
48		SUBS r4, r4, r5
49		BLVS p_throw_overflow_error
50		STR r4, [sp, #4]
51	L2:
52		LDR r4, [sp, #4]
53		LDR r5, =0
54		CMP r4, r5
55		MOVGT r4, #1
56		MOVLE r4, #0
57		CMP r4, #1
58		BEQ L3
59		LDR r4, =msg_1
60		MOV r0, r4
61		BL p_print_string
62		BL p_print_ln
63		LDR r4, [sp, #12]
64		LDR r5, =1
65		SUBS r4, r4, r5
66		BLVS p_throw_overflow_error
67		STR r4, [sp, #-4]!
68		BL f_f
69		ADD sp, sp, #4
70		MOV r4, r0
71		STR r4, [sp]
72		ADD sp, sp, #8
73	L1:
74		LDR r4, =0
75		MOV r0, r4
76		POP {pc}
77		.ltorg
78	main:
79		PUSH {lr}
80		SUB sp, sp, #8
81		LDR r4, =msg_2
82		MOV r0, r4
83		BL p_print_string
84		BL p_print_ln
85		LDR r4, =0
86		STR r4, [sp, #4]
87		ADD r4, sp, #4
88		MOV r0, r4
89		BL p_read_int
90		LDR r4, [sp, #4]
91		STR r4, [sp, #-4]!
92		BL f_f
93		ADD sp, sp, #4
94		MOV r4, r0
95		STR r4, [sp]
96		ADD sp, sp, #8
97		LDR r0, =0
98		POP {pc}
99		.ltorg
100	p_print_string:
101		PUSH {lr}
102		LDR r1, [r0]
103		ADD r2, r0, #4
104		LDR r0, =msg_3
105		ADD r0, r0, #4
106		BL printf
107		MOV r0, #0
108		BL fflush
109		POP {pc}
110	p_throw_overflow_error:
111		LDR r0, =msg_4
112		BL p_throw_runtime_error
113	p_print_ln:
114		PUSH {lr}
115		LDR r0, =msg_5
116		ADD r0, r0, #4
117		BL puts
118		MOV r0, #0
119		BL fflush
120		POP {pc}
121	p_read_int:
122		PUSH {lr}
123		MOV r1, r0
124		LDR r0, =msg_6
125		ADD r0, r0, #4
126		BL scanf
127		POP {pc}
128	p_throw_runtime_error:
129		BL p_print_string
130		MOV r0, #-1
131		BL exit
132	
===========================================================
-- Finished

