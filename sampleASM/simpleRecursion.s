valid/function/nested_functions/simpleRecursion.wacc
calling the reference compiler on valid/function/nested_functions/simpleRecursion.wacc
-- Test: simpleRecursion.wacc

-- Uploaded file: 
---------------------------------------------------------------
# a simple recursive function

# Output:
# #empty#

# Program:

begin
  int rec(int x) is
    if x == 0 
    then
      skip
    else
      int y = call rec(x - 1)
    fi ;
    return 42  
  end

  int x = 0 ;
  x = call rec(8)
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
simpleRecursion.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 82
4		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
5	msg_1:
6		.word 5
7		.ascii	"%.*s\0"
8	
9	.text
10	
11	.global main
12	f_rec:
13		PUSH {lr}
14		LDR r4, [sp, #4]
15		LDR r5, =0
16		CMP r4, r5
17		MOVEQ r4, #1
18		MOVNE r4, #0
19		CMP r4, #0
20		BEQ L0
21		B L1
22	L0:
23		SUB sp, sp, #4
24		LDR r4, [sp, #8]
25		LDR r5, =1
26		SUBS r4, r4, r5
27		BLVS p_throw_overflow_error
28		STR r4, [sp, #-4]!
29		BL f_rec
30		ADD sp, sp, #4
31		MOV r4, r0
32		STR r4, [sp]
33		ADD sp, sp, #4
34	L1:
35		LDR r4, =42
36		MOV r0, r4
37		POP {pc}
38		.ltorg
39	main:
40		PUSH {lr}
41		SUB sp, sp, #4
42		LDR r4, =0
43		STR r4, [sp]
44		LDR r4, =8
45		STR r4, [sp, #-4]!
46		BL f_rec
47		ADD sp, sp, #4
48		MOV r4, r0
49		STR r4, [sp]
50		ADD sp, sp, #4
51		LDR r0, =0
52		POP {pc}
53		.ltorg
54	p_throw_overflow_error:
55		LDR r0, =msg_0
56		BL p_throw_runtime_error
57	p_throw_runtime_error:
58		BL p_print_string
59		MOV r0, #-1
60		BL exit
61	p_print_string:
62		PUSH {lr}
63		LDR r1, [r0]
64		ADD r2, r0, #4
65		LDR r0, =msg_1
66		ADD r0, r0, #4
67		BL printf
68		MOV r0, #0
69		BL fflush
70		POP {pc}
71	
===========================================================
-- Finished

