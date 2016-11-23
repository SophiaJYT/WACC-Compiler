valid/function/simple_functions/asciiTable.wacc
calling the reference compiler on valid/function/simple_functions/asciiTable.wacc
-- Test: asciiTable.wacc

-- Uploaded file: 
---------------------------------------------------------------
# print out a lookup table for ascii character representations

# Output:
# Asci character lookup table:
# -------------
# |   32 =    |
# |   33 = !  |
# |   34 = "  |
# |   35 = #  |
# |   36 = $  |
# |   37 = %  |
# |   38 = &  |
# |   39 = '  |
# |   40 = (  |
# |   41 = )  |
# |   42 = *  |
# |   43 = +  |
# |   44 = ,  |
# |   45 = -  |
# |   46 = .  |
# |   47 = /  |
# |   48 = 0  |
# |   49 = 1  |
# |   50 = 2  |
# |   51 = 3  |
# |   52 = 4  |
# |   53 = 5  |
# |   54 = 6  |
# |   55 = 7  |
# |   56 = 8  |
# |   57 = 9  |
# |   58 = :  |
# |   59 = ;  |
# |   60 = <  |
# |   61 = =  |
# |   62 = >  |
# |   63 = ?  |
# |   64 = @  |
# |   65 = A  |
# |   66 = B  |
# |   67 = C  |
# |   68 = D  |
# |   69 = E  |
# |   70 = F  |
# |   71 = G  |
# |   72 = H  |
# |   73 = I  |
# |   74 = J  |
# |   75 = K  |
# |   76 = L  |
# |   77 = M  |
# |   78 = N  |
# |   79 = O  |
# |   80 = P  |
# |   81 = Q  |
# |   82 = R  |
# |   83 = S  |
# |   84 = T  |
# |   85 = U  |
# |   86 = V  |
# |   87 = W  |
# |   88 = X  |
# |   89 = Y  |
# |   90 = Z  |
# |   91 = [  |
# |   92 = \  |
# |   93 = ]  |
# |   94 = ^  |
# |   95 = _  |
# |   96 = `  |
# |   97 = a  |
# |   98 = b  |
# |   99 = c  |
# |  100 = d  |
# |  101 = e  |
# |  102 = f  |
# |  103 = g  |
# |  104 = h  |
# |  105 = i  |
# |  106 = j  |
# |  107 = k  |
# |  108 = l  |
# |  109 = m  |
# |  110 = n  |
# |  111 = o  |
# |  112 = p  |
# |  113 = q  |
# |  114 = r  |
# |  115 = s  |
# |  116 = t  |
# |  117 = u  |
# |  118 = v  |
# |  119 = w  |
# |  120 = x  |
# |  121 = y  |
# |  122 = z  |
# |  123 = {  |
# |  124 = |  |
# |  125 = }  |
# |  126 = ~  |
# -------------


# Program:

begin
  bool printLine(int n) is
    int i = 0 ;
    while i < n do
      print "-" ;
      i = i + 1
    done ;
    println "" ;
    return true
  end

  bool printMap(int n) is
    print "|  " ;
    if n <100 then
      print " "
    else
      skip
    fi ;
    print n ;
    print " = " ;
    print chr n ;
    println "  |" ; 
    return true
  end

  println "Asci character lookup table:" ;
  bool r = call printLine(13) ;
  int num = ord ' ' ;
  while num < 127 do
    r = call printMap(num) ;
    num = num + 1
  done ;
  r = call printLine(13)
end

---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
asciiTable.s contents are:
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
9		.word 3
10		.ascii	"|  "
11	msg_3:
12		.word 1
13		.ascii	" "
14	msg_4:
15		.word 3
16		.ascii	" = "
17	msg_5:
18		.word 3
19		.ascii	"  |"
20	msg_6:
21		.word 28
22		.ascii	"Asci character lookup table:"
23	msg_7:
24		.word 5
25		.ascii	"%.*s\0"
26	msg_8:
27		.word 82
28		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
29	msg_9:
30		.word 1
31		.ascii	"\0"
32	msg_10:
33		.word 3
34		.ascii	"%d\0"
35	
36	.text
37	
38	.global main
39	f_printLine:
40		PUSH {lr}
41		SUB sp, sp, #4
42		LDR r4, =0
43		STR r4, [sp]
44		B L0
45	L1:
46		LDR r4, =msg_0
47		MOV r0, r4
48		BL p_print_string
49		LDR r4, [sp]
50		LDR r5, =1
51		ADDS r4, r4, r5
52		BLVS p_throw_overflow_error
53		STR r4, [sp]
54	L0:
55		LDR r4, [sp]
56		LDR r5, [sp, #8]
57		CMP r4, r5
58		MOVLT r4, #1
59		MOVGE r4, #0
60		CMP r4, #1
61		BEQ L1
62		LDR r4, =msg_1
63		MOV r0, r4
64		BL p_print_string
65		BL p_print_ln
66		MOV r4, #1
67		MOV r0, r4
68		ADD sp, sp, #4
69		POP {pc}
70		.ltorg
71	f_printMap:
72		PUSH {lr}
73		LDR r4, =msg_2
74		MOV r0, r4
75		BL p_print_string
76		LDR r4, [sp, #4]
77		LDR r5, =100
78		CMP r4, r5
79		MOVLT r4, #1
80		MOVGE r4, #0
81		CMP r4, #0
82		BEQ L2
83		LDR r4, =msg_3
84		MOV r0, r4
85		BL p_print_string
86		B L3
87	L2:
88	L3:
89		LDR r4, [sp, #4]
90		MOV r0, r4
91		BL p_print_int
92		LDR r4, =msg_4
93		MOV r0, r4
94		BL p_print_string
95		LDR r4, [sp, #4]
96		MOV r0, r4
97		BL putchar
98		LDR r4, =msg_5
99		MOV r0, r4
100		BL p_print_string
101		BL p_print_ln
102		MOV r4, #1
103		MOV r0, r4
104		POP {pc}
105		.ltorg
106	main:
107		PUSH {lr}
108		SUB sp, sp, #5
109		LDR r4, =msg_6
110		MOV r0, r4
111		BL p_print_string
112		BL p_print_ln
113		LDR r4, =13
114		STR r4, [sp, #-4]!
115		BL f_printLine
116		ADD sp, sp, #4
117		MOV r4, r0
118		STRB r4, [sp, #4]
119		MOV r4, #' '
120		STR r4, [sp]
121		B L4
122	L5:
123		LDR r4, [sp]
124		STR r4, [sp, #-4]!
125		BL f_printMap
126		ADD sp, sp, #4
127		MOV r4, r0
128		STRB r4, [sp, #4]
129		LDR r4, [sp]
130		LDR r5, =1
131		ADDS r4, r4, r5
132		BLVS p_throw_overflow_error
133		STR r4, [sp]
134	L4:
135		LDR r4, [sp]
136		LDR r5, =127
137		CMP r4, r5
138		MOVLT r4, #1
139		MOVGE r4, #0
140		CMP r4, #1
141		BEQ L5
142		LDR r4, =13
143		STR r4, [sp, #-4]!
144		BL f_printLine
145		ADD sp, sp, #4
146		MOV r4, r0
147		STRB r4, [sp, #4]
148		ADD sp, sp, #5
149		LDR r0, =0
150		POP {pc}
151		.ltorg
152	p_print_string:
153		PUSH {lr}
154		LDR r1, [r0]
155		ADD r2, r0, #4
156		LDR r0, =msg_7
157		ADD r0, r0, #4
158		BL printf
159		MOV r0, #0
160		BL fflush
161		POP {pc}
162	p_throw_overflow_error:
163		LDR r0, =msg_8
164		BL p_throw_runtime_error
165	p_print_ln:
166		PUSH {lr}
167		LDR r0, =msg_9
168		ADD r0, r0, #4
169		BL puts
170		MOV r0, #0
171		BL fflush
172		POP {pc}
173	p_print_int:
174		PUSH {lr}
175		MOV r1, r0
176		LDR r0, =msg_10
177		ADD r0, r0, #4
178		BL printf
179		MOV r0, #0
180		BL fflush
181		POP {pc}
182	p_throw_runtime_error:
183		BL p_print_string
184		MOV r0, #-1
185		BL exit
186	
===========================================================
-- Finished

