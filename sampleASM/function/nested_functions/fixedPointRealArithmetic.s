valid/function/nested_functions/fixedPointRealArithmetic.wacc
calling the reference compiler on valid/function/nested_functions/fixedPointRealArithmetic.wacc
-- Test: fixedPointRealArithmetic.wacc

-- Uploaded file: 
---------------------------------------------------------------
# This program implements floating-point type using integers.
# The details about how it is done can found here: 
# http://www.cse.iitd.ernet.in/~sbansal/csl373/pintos/doc/pintos_7.html#SEC135
# 
# Basically, our integer have 32 bits. We use the first bit for sign, the next 
# 17 bits for value above the decimal digit and the last 14 bits for the values 
# after the decimal digits.
# 
# We call the number 17 above p, and the number 14 above q.
# We have f = 2**q. 
# 

begin
	# Returns the number of bits behind the decimal points.
	int q() is
		return 14
	end
	
	# Because we do not have bitwise shit in the language, we have to calculate it manually.
	int power(int base, int amount) is
		int result = 1 ;
		while amount > 0 do
			result = result * base ;
			amount = amount - 1
		done ;
		return result
	end
	
	int f() is
		int qq = call q() ;
		# f = 2**q
		int f = call power(2, qq) ;
		return f
	end
	
	# The implementation of the following functions are translated from the URI above.
	# Arguments start with 'x' have type fixed-point. Those start with 'n' have type integer.
	
	int intToFixedPoint(int n) is
		int ff = call f() ;
		return n * ff
	end
	
	int fixedPointToIntRoundDown(int x) is
		int ff = call f() ;
		return x / ff
	end

	int fixedPointToIntRoundNear(int x) is
		int ff = call f() ;
		if x >= 0
		then
			return (x + ff / 2) / ff 
		else
			return (x - ff / 2) / ff
		fi
	end

	int add(int x1, int x2) is 
		return x1 + x2
	end
	
	int subtract(int x1, int x2) is
		return x1 - x2
	end
	
	int addByInt(int x, int n) is
		int ff = call f() ;
		return x + n * ff
	end

	int subtractByInt(int x, int n) is
		int ff = call f() ;
		return x - n * ff
	end

	int multiply(int x1, int x2) is
		# We don't have int_64 in our language so we just ignore the overflow
		int ff = call f() ;
		return x1 * x2 / ff 
	end

	int multiplyByInt(int x, int n) is
		return x * n
	end
	
	int divide(int x1, int x2) is
		# We don't have int_64 in our language so we just ignore the overflow
		int ff = call f() ;
		return x1 * ff / x2
	end

	int divideByInt(int x, int n) is
		return x / n
	end

	# Main function
	int n1 = 10 ;
	int n2 = 3 ;
	
	print "Using fixed-point real: " ;
	print n1 ;
	print " / " ;
	print n2 ;
	print " * " ;
	print n2 ;
	print " = " ;
	
	int x = call intToFixedPoint(n1) ;
	x = call divideByInt(x, n2) ;
	x = call multiplyByInt(x, n2) ;
	int result = call fixedPointToIntRoundNear(x) ;
	println result 
end



---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
fixedPointRealArithmetic.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 24
4		.ascii	"Using fixed-point real: "
5	msg_1:
6		.word 3
7		.ascii	" / "
8	msg_2:
9		.word 3
10		.ascii	" * "
11	msg_3:
12		.word 3
13		.ascii	" = "
14	msg_4:
15		.word 82
16		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
17	msg_5:
18		.word 45
19		.ascii	"DivideByZeroError: divide or modulo by zero\n\0"
20	msg_6:
21		.word 5
22		.ascii	"%.*s\0"
23	msg_7:
24		.word 3
25		.ascii	"%d\0"
26	msg_8:
27		.word 1
28		.ascii	"\0"
29	
30	.text
31	
32	.global main
33	f_q:
34		PUSH {lr}
35		LDR r4, =14
36		MOV r0, r4
37		POP {pc}
38		.ltorg
39	f_power:
40		PUSH {lr}
41		SUB sp, sp, #4
42		LDR r4, =1
43		STR r4, [sp]
44		B L0
45	L1:
46		LDR r4, [sp]
47		LDR r5, [sp, #8]
48		SMULL r4, r5, r4, r5
49		CMP r5, r4, ASR #31
50		BLNE p_throw_overflow_error
51		STR r4, [sp]
52		LDR r4, [sp, #12]
53		LDR r5, =1
54		SUBS r4, r4, r5
55		BLVS p_throw_overflow_error
56		STR r4, [sp, #12]
57	L0:
58		LDR r4, [sp, #12]
59		LDR r5, =0
60		CMP r4, r5
61		MOVGT r4, #1
62		MOVLE r4, #0
63		CMP r4, #1
64		BEQ L1
65		LDR r4, [sp]
66		MOV r0, r4
67		ADD sp, sp, #4
68		POP {pc}
69		.ltorg
70	f_f:
71		PUSH {lr}
72		SUB sp, sp, #8
73		BL f_q
74		MOV r4, r0
75		STR r4, [sp, #4]
76		LDR r4, [sp, #4]
77		STR r4, [sp, #-4]!
78		LDR r4, =2
79		STR r4, [sp, #-4]!
80		BL f_power
81		ADD sp, sp, #8
82		MOV r4, r0
83		STR r4, [sp]
84		LDR r4, [sp]
85		MOV r0, r4
86		ADD sp, sp, #8
87		POP {pc}
88		.ltorg
89	f_intToFixedPoint:
90		PUSH {lr}
91		SUB sp, sp, #4
92		BL f_f
93		MOV r4, r0
94		STR r4, [sp]
95		LDR r4, [sp, #8]
96		LDR r5, [sp]
97		SMULL r4, r5, r4, r5
98		CMP r5, r4, ASR #31
99		BLNE p_throw_overflow_error
100		MOV r0, r4
101		ADD sp, sp, #4
102		POP {pc}
103		.ltorg
104	f_fixedPointToIntRoundDown:
105		PUSH {lr}
106		SUB sp, sp, #4
107		BL f_f
108		MOV r4, r0
109		STR r4, [sp]
110		LDR r4, [sp, #8]
111		LDR r5, [sp]
112		MOV r0, r4
113		MOV r1, r5
114		BL p_check_divide_by_zero
115		BL __aeabi_idiv
116		MOV r4, r0
117		MOV r0, r4
118		ADD sp, sp, #4
119		POP {pc}
120		.ltorg
121	f_fixedPointToIntRoundNear:
122		PUSH {lr}
123		SUB sp, sp, #4
124		BL f_f
125		MOV r4, r0
126		STR r4, [sp]
127		LDR r4, [sp, #8]
128		LDR r5, =0
129		CMP r4, r5
130		MOVGE r4, #1
131		MOVLT r4, #0
132		CMP r4, #0
133		BEQ L2
134		LDR r4, [sp, #8]
135		LDR r5, [sp]
136		LDR r6, =2
137		MOV r0, r5
138		MOV r1, r6
139		BL p_check_divide_by_zero
140		BL __aeabi_idiv
141		MOV r5, r0
142		ADDS r4, r4, r5
143		BLVS p_throw_overflow_error
144		LDR r5, [sp]
145		MOV r0, r4
146		MOV r1, r5
147		BL p_check_divide_by_zero
148		BL __aeabi_idiv
149		MOV r4, r0
150		MOV r0, r4
151		ADD sp, sp, #4
152		B L3
153	L2:
154		LDR r4, [sp, #8]
155		LDR r5, [sp]
156		LDR r6, =2
157		MOV r0, r5
158		MOV r1, r6
159		BL p_check_divide_by_zero
160		BL __aeabi_idiv
161		MOV r5, r0
162		SUBS r4, r4, r5
163		BLVS p_throw_overflow_error
164		LDR r5, [sp]
165		MOV r0, r4
166		MOV r1, r5
167		BL p_check_divide_by_zero
168		BL __aeabi_idiv
169		MOV r4, r0
170		MOV r0, r4
171		ADD sp, sp, #4
172	L3:
173		POP {pc}
174		.ltorg
175	f_add:
176		PUSH {lr}
177		LDR r4, [sp, #4]
178		LDR r5, [sp, #8]
179		ADDS r4, r4, r5
180		BLVS p_throw_overflow_error
181		MOV r0, r4
182		POP {pc}
183		.ltorg
184	f_subtract:
185		PUSH {lr}
186		LDR r4, [sp, #4]
187		LDR r5, [sp, #8]
188		SUBS r4, r4, r5
189		BLVS p_throw_overflow_error
190		MOV r0, r4
191		POP {pc}
192		.ltorg
193	f_addByInt:
194		PUSH {lr}
195		SUB sp, sp, #4
196		BL f_f
197		MOV r4, r0
198		STR r4, [sp]
199		LDR r4, [sp, #8]
200		LDR r5, [sp, #12]
201		LDR r6, [sp]
202		SMULL r5, r6, r5, r6
203		CMP r6, r5, ASR #31
204		BLNE p_throw_overflow_error
205		ADDS r4, r4, r5
206		BLVS p_throw_overflow_error
207		MOV r0, r4
208		ADD sp, sp, #4
209		POP {pc}
210		.ltorg
211	f_subtractByInt:
212		PUSH {lr}
213		SUB sp, sp, #4
214		BL f_f
215		MOV r4, r0
216		STR r4, [sp]
217		LDR r4, [sp, #8]
218		LDR r5, [sp, #12]
219		LDR r6, [sp]
220		SMULL r5, r6, r5, r6
221		CMP r6, r5, ASR #31
222		BLNE p_throw_overflow_error
223		SUBS r4, r4, r5
224		BLVS p_throw_overflow_error
225		MOV r0, r4
226		ADD sp, sp, #4
227		POP {pc}
228		.ltorg
229	f_multiply:
230		PUSH {lr}
231		SUB sp, sp, #4
232		BL f_f
233		MOV r4, r0
234		STR r4, [sp]
235		LDR r4, [sp, #8]
236		LDR r5, [sp, #12]
237		SMULL r4, r5, r4, r5
238		CMP r5, r4, ASR #31
239		BLNE p_throw_overflow_error
240		LDR r5, [sp]
241		MOV r0, r4
242		MOV r1, r5
243		BL p_check_divide_by_zero
244		BL __aeabi_idiv
245		MOV r4, r0
246		MOV r0, r4
247		ADD sp, sp, #4
248		POP {pc}
249		.ltorg
250	f_multiplyByInt:
251		PUSH {lr}
252		LDR r4, [sp, #4]
253		LDR r5, [sp, #8]
254		SMULL r4, r5, r4, r5
255		CMP r5, r4, ASR #31
256		BLNE p_throw_overflow_error
257		MOV r0, r4
258		POP {pc}
259		.ltorg
260	f_divide:
261		PUSH {lr}
262		SUB sp, sp, #4
263		BL f_f
264		MOV r4, r0
265		STR r4, [sp]
266		LDR r4, [sp, #8]
267		LDR r5, [sp]
268		SMULL r4, r5, r4, r5
269		CMP r5, r4, ASR #31
270		BLNE p_throw_overflow_error
271		LDR r5, [sp, #12]
272		MOV r0, r4
273		MOV r1, r5
274		BL p_check_divide_by_zero
275		BL __aeabi_idiv
276		MOV r4, r0
277		MOV r0, r4
278		ADD sp, sp, #4
279		POP {pc}
280		.ltorg
281	f_divideByInt:
282		PUSH {lr}
283		LDR r4, [sp, #4]
284		LDR r5, [sp, #8]
285		MOV r0, r4
286		MOV r1, r5
287		BL p_check_divide_by_zero
288		BL __aeabi_idiv
289		MOV r4, r0
290		MOV r0, r4
291		POP {pc}
292		.ltorg
293	main:
294		PUSH {lr}
295		SUB sp, sp, #16
296		LDR r4, =10
297		STR r4, [sp, #12]
298		LDR r4, =3
299		STR r4, [sp, #8]
300		LDR r4, =msg_0
301		MOV r0, r4
302		BL p_print_string
303		LDR r4, [sp, #12]
304		MOV r0, r4
305		BL p_print_int
306		LDR r4, =msg_1
307		MOV r0, r4
308		BL p_print_string
309		LDR r4, [sp, #8]
310		MOV r0, r4
311		BL p_print_int
312		LDR r4, =msg_2
313		MOV r0, r4
314		BL p_print_string
315		LDR r4, [sp, #8]
316		MOV r0, r4
317		BL p_print_int
318		LDR r4, =msg_3
319		MOV r0, r4
320		BL p_print_string
321		LDR r4, [sp, #12]
322		STR r4, [sp, #-4]!
323		BL f_intToFixedPoint
324		ADD sp, sp, #4
325		MOV r4, r0
326		STR r4, [sp, #4]
327		LDR r4, [sp, #8]
328		STR r4, [sp, #-4]!
329		LDR r4, [sp, #8]
330		STR r4, [sp, #-4]!
331		BL f_divideByInt
332		ADD sp, sp, #8
333		MOV r4, r0
334		STR r4, [sp, #4]
335		LDR r4, [sp, #8]
336		STR r4, [sp, #-4]!
337		LDR r4, [sp, #8]
338		STR r4, [sp, #-4]!
339		BL f_multiplyByInt
340		ADD sp, sp, #8
341		MOV r4, r0
342		STR r4, [sp, #4]
343		LDR r4, [sp, #4]
344		STR r4, [sp, #-4]!
345		BL f_fixedPointToIntRoundNear
346		ADD sp, sp, #4
347		MOV r4, r0
348		STR r4, [sp]
349		LDR r4, [sp]
350		MOV r0, r4
351		BL p_print_int
352		BL p_print_ln
353		ADD sp, sp, #16
354		LDR r0, =0
355		POP {pc}
356		.ltorg
357	p_throw_overflow_error:
358		LDR r0, =msg_4
359		BL p_throw_runtime_error
360	p_check_divide_by_zero:
361		PUSH {lr}
362		CMP r1, #0
363		LDREQ r0, =msg_5
364		BLEQ p_throw_runtime_error
365		POP {pc}
366	p_print_string:
367		PUSH {lr}
368		LDR r1, [r0]
369		ADD r2, r0, #4
370		LDR r0, =msg_6
371		ADD r0, r0, #4
372		BL printf
373		MOV r0, #0
374		BL fflush
375		POP {pc}
376	p_print_int:
377		PUSH {lr}
378		MOV r1, r0
379		LDR r0, =msg_7
380		ADD r0, r0, #4
381		BL printf
382		MOV r0, #0
383		BL fflush
384		POP {pc}
385	p_print_ln:
386		PUSH {lr}
387		LDR r0, =msg_8
388		ADD r0, r0, #4
389		BL puts
390		MOV r0, #0
391		BL fflush
392		POP {pc}
393	p_throw_runtime_error:
394		BL p_print_string
395		MOV r0, #-1
396		BL exit
397	
===========================================================
-- Finished

