valid/function/nested_functions/mutualRecursion.wacc
calling the reference compiler on valid/function/nested_functions/mutualRecursion.wacc
-- Test: mutualRecursion.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a pair of mutually recursive functions

# Output:
# r1: sending 8
# r2: received 8
# r1: sending 7
# r2: received 7
# r1: sending 6
# r2: received 6
# r1: sending 5
# r2: received 5
# r1: sending 4
# r2: received 4
# r1: sending 3
# r2: received 3
# r1: sending 2
# r2: received 2
# r1: sending 1
# r2: received 1

# Program:

begin
  int r1(int x) is
    if x == 0 
    then
      skip
    else
      print "r1: sending " ;
      println x ;
      int y = call r2(x)
    fi ;
    return 42  
  end

  int r2(int y) is
    print "r2: received " ;
    println y ;
    int z = call r1(y - 1) ; 
    return 44
  end

  int x = 0 ;
  x = call r1(8)
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
mutualRecursion.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 12
4		.ascii	"r1: sending "
5	msg_1:
6		.word 13
7		.ascii	"r2: received "
8	msg_2:
9		.word 5
10		.ascii	"%.*s\0"
11	msg_3:
12		.word 3
13		.ascii	"%d\0"
14	msg_4:
15		.word 1
16		.ascii	"\0"
17	msg_5:
18		.word 82
19		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
20	
21	.text
22	
23	.global main
24	f_r1:
25		PUSH {lr}
26		LDR r4, [sp, #4]
27		LDR r5, =0
28		CMP r4, r5
29		MOVEQ r4, #1
30		MOVNE r4, #0
31		CMP r4, #0
32		BEQ L0
33		B L1
34	L0:
35		SUB sp, sp, #4
36		LDR r4, =msg_0
37		MOV r0, r4
38		BL p_print_string
39		LDR r4, [sp, #8]
40		MOV r0, r4
41		BL p_print_int
42		BL p_print_ln
43		LDR r4, [sp, #8]
44		STR r4, [sp, #-4]!
45		BL f_r2
46		ADD sp, sp, #4
47		MOV r4, r0
48		STR r4, [sp]
49		ADD sp, sp, #4
50	L1:
51		LDR r4, =42
52		MOV r0, r4
53		POP {pc}
54		.ltorg
55	f_r2:
56		PUSH {lr}
57		SUB sp, sp, #4
58		LDR r4, =msg_1
59		MOV r0, r4
60		BL p_print_string
61		LDR r4, [sp, #8]
62		MOV r0, r4
63		BL p_print_int
64		BL p_print_ln
65		LDR r4, [sp, #8]
66		LDR r5, =1
67		SUBS r4, r4, r5
68		BLVS p_throw_overflow_error
69		STR r4, [sp, #-4]!
70		BL f_r1
71		ADD sp, sp, #4
72		MOV r4, r0
73		STR r4, [sp]
74		LDR r4, =44
75		MOV r0, r4
76		ADD sp, sp, #4
77		POP {pc}
78		.ltorg
79	main:
80		PUSH {lr}
81		SUB sp, sp, #4
82		LDR r4, =0
83		STR r4, [sp]
84		LDR r4, =8
85		STR r4, [sp, #-4]!
86		BL f_r1
87		ADD sp, sp, #4
88		MOV r4, r0
89		STR r4, [sp]
90		ADD sp, sp, #4
91		LDR r0, =0
92		POP {pc}
93		.ltorg
94	p_print_string:
95		PUSH {lr}
96		LDR r1, [r0]
97		ADD r2, r0, #4
98		LDR r0, =msg_2
99		ADD r0, r0, #4
100		BL printf
101		MOV r0, #0
102		BL fflush
103		POP {pc}
104	p_print_int:
105		PUSH {lr}
106		MOV r1, r0
107		LDR r0, =msg_3
108		ADD r0, r0, #4
109		BL printf
110		MOV r0, #0
111		BL fflush
112		POP {pc}
113	p_print_ln:
114		PUSH {lr}
115		LDR r0, =msg_4
116		ADD r0, r0, #4
117		BL puts
118		MOV r0, #0
119		BL fflush
120		POP {pc}
121	p_throw_overflow_error:
122		LDR r0, =msg_5
123		BL p_throw_runtime_error
124	p_throw_runtime_error:
125		BL p_print_string
126		MOV r0, #-1
127		BL exit
128	
===========================================================
-- Finished

