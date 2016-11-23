valid/function/nested_functions/functionConditionalReturn.wacc
calling the reference compiler on valid/function/nested_functions/functionConditionalReturn.wacc
-- Test: functionConditionalReturn.wacc

-- Uploaded file: 
---------------------------------------------------------------
# program has function which only contains an if statement and a return in each branch

# Output:
# true

# Program:

begin
  bool f() is 
    if true then
      return true
    else
      return false
    fi
  end
  bool x = call f();
  println x
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
functionConditionalReturn.s contents are:
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
15	f_f:
16		PUSH {lr}
17		MOV r4, #1
18		CMP r4, #0
19		BEQ L0
20		MOV r4, #1
21		MOV r0, r4
22		B L1
23	L0:
24		MOV r4, #0
25		MOV r0, r4
26	L1:
27		POP {pc}
28		.ltorg
29	main:
30		PUSH {lr}
31		SUB sp, sp, #1
32		BL f_f
33		MOV r4, r0
34		STRB r4, [sp]
35		LDRSB r4, [sp]
36		MOV r0, r4
37		BL p_print_bool
38		BL p_print_ln
39		ADD sp, sp, #1
40		LDR r0, =0
41		POP {pc}
42		.ltorg
43	p_print_bool:
44		PUSH {lr}
45		CMP r0, #0
46		LDRNE r0, =msg_0
47		LDREQ r0, =msg_1
48		ADD r0, r0, #4
49		BL printf
50		MOV r0, #0
51		BL fflush
52		POP {pc}
53	p_print_ln:
54		PUSH {lr}
55		LDR r0, =msg_2
56		ADD r0, r0, #4
57		BL puts
58		MOV r0, #0
59		BL fflush
60		POP {pc}
61	
===========================================================
-- Finished

