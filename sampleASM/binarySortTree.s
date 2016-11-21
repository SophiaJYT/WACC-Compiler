valid/advanced/binarySortTree.wacc
calling the reference compiler on valid/advanced/binarySortTree.wacc
-- Test: binarySortTree.wacc

-- Uploaded file: 
---------------------------------------------------------------
# The program reads n (number of integers), then n integers. After each input, 
# it insert the integer into a binary search tree. At the end, it prints out 
# the content in the binary search tree so that we have a sorted list of 
# integer.
# 
# We represent a node in the binary search tree using two pair elements. The 
# first element has a type <int, pair>, the int is the integer stored in the 
# node, the pair is the pointer to the second pair element. The second pair 
# element has a type <pair, pair> which is the pointer to the two children 
# nodes in the binary search tree.

begin

  # Create a new node of a binary search tree having the given integer value 
  # and points to the two given pairs.
  pair(int, pair) createNewNode(int value, pair(int, pair) left, pair(int, pair) right) is
    pair(pair, pair) p = newpair(left, right) ;
    pair(int, pair) q = newpair(value, p) ;
    return q
  end

  # Given a root of a binary search tree and an integer to insert, the function 
  # inserts the integer into the tree and returns the new root of the tree.
  pair(int, pair) insert(pair(int, pair) root, int n) is
    if root == null then
      root = call createNewNode(n, null, null)
    else
      pair(pair, pair) p = snd root ;
      int current = fst root ;
      pair(int, pair) q = null ;
      if n < current then
      	q = fst p ;
        fst p = call insert(q, n)
      else 
      	q = snd p ;
        snd p = call insert(q, n)
      fi 
    fi ;
    return root
  end

  # Print the integers in the binary search tree in the increasing order.
  int printTree(pair(int, pair) root) is
    if root == null then
      return 0 
    else
      pair(pair, pair) body = snd root ;
      pair(int, pair) p = fst body ;
      int temp = call printTree(p) ;
      temp = fst root ; 
      print temp ;
      print ' ' ;
      p = snd body ;
      temp = call printTree(p) ;
      return 0
    fi
  end

  # The main function
  int n = 0 ;
  print "Please enter the number of integers to insert: " ;
  read n ;
  print "There are " ;
  print n ;
  println " integers." ;
  int i = 0 ;
  pair(int, pair) root = null ;
  while i < n do
    int x = 0 ;
    print "Please enter the number at position " ; 
    print i + 1 ;
    print " : " ;
    read x ;
    root = call insert(root, x) ;
    i = i + 1
  done ;
  print "Here are the numbers sorted: " ;
  i = call printTree(root) ;
  println ""
end



---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
binarySortTree.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 47
4		.ascii	"Please enter the number of integers to insert: "
5	msg_1:
6		.word 10
7		.ascii	"There are "
8	msg_2:
9		.word 10
10		.ascii	" integers."
11	msg_3:
12		.word 36
13		.ascii	"Please enter the number at position "
14	msg_4:
15		.word 3
16		.ascii	" : "
17	msg_5:
18		.word 29
19		.ascii	"Here are the numbers sorted: "
20	msg_6:
21		.word 0
22		.ascii	""
23	msg_7:
24		.word 50
25		.ascii	"NullReferenceError: dereference a null reference\n\0"
26	msg_8:
27		.word 3
28		.ascii	"%d\0"
29	msg_9:
30		.word 5
31		.ascii	"%.*s\0"
32	msg_10:
33		.word 3
34		.ascii	"%d\0"
35	msg_11:
36		.word 1
37		.ascii	"\0"
38	msg_12:
39		.word 82
40		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
41	
42	.text
43	
44	.global main
45	f_createNewNode:
46		PUSH {lr}
47		SUB sp, sp, #8
48		LDR r0, =8
49		BL malloc
50		MOV r4, r0
51		LDR r5, [sp, #16]
52		LDR r0, =4
53		BL malloc
54		STR r5, [r0]
55		STR r0, [r4]
56		LDR r5, [sp, #20]
57		LDR r0, =4
58		BL malloc
59		STR r5, [r0]
60		STR r0, [r4, #4]
61		STR r4, [sp, #4]
62		LDR r0, =8
63		BL malloc
64		MOV r4, r0
65		LDR r5, [sp, #12]
66		LDR r0, =4
67		BL malloc
68		STR r5, [r0]
69		STR r0, [r4]
70		LDR r5, [sp, #4]
71		LDR r0, =4
72		BL malloc
73		STR r5, [r0]
74		STR r0, [r4, #4]
75		STR r4, [sp]
76		LDR r4, [sp]
77		MOV r0, r4
78		ADD sp, sp, #8
79		POP {pc}
80		.ltorg
81	f_insert:
82		PUSH {lr}
83		LDR r4, [sp, #4]
84		LDR r5, =0
85		CMP r4, r5
86		MOVEQ r4, #1
87		MOVNE r4, #0
88		CMP r4, #0
89		BEQ L0
90		LDR r4, =0
91		STR r4, [sp, #-4]!
92		LDR r4, =0
93		STR r4, [sp, #-4]!
94		LDR r4, [sp, #16]
95		STR r4, [sp, #-4]!
96		BL f_createNewNode
97		ADD sp, sp, #12
98		MOV r4, r0
99		STR r4, [sp, #4]
100		B L1
101	L0:
102		SUB sp, sp, #12
103		LDR r4, [sp, #16]
104		MOV r0, r4
105		BL p_check_null_pointer
106		LDR r4, [r4, #4]
107		LDR r4, [r4]
108		STR r4, [sp, #8]
109		LDR r4, [sp, #16]
110		MOV r0, r4
111		BL p_check_null_pointer
112		LDR r4, [r4]
113		LDR r4, [r4]
114		STR r4, [sp, #4]
115		LDR r4, =0
116		STR r4, [sp]
117		LDR r4, [sp, #20]
118		LDR r5, [sp, #4]
119		CMP r4, r5
120		MOVLT r4, #1
121		MOVGE r4, #0
122		CMP r4, #0
123		BEQ L2
124		LDR r4, [sp, #8]
125		MOV r0, r4
126		BL p_check_null_pointer
127		LDR r4, [r4]
128		LDR r4, [r4]
129		STR r4, [sp]
130		LDR r4, [sp, #20]
131		STR r4, [sp, #-4]!
132		LDR r4, [sp, #4]
133		STR r4, [sp, #-4]!
134		BL f_insert
135		ADD sp, sp, #8
136		MOV r4, r0
137		LDR r5, [sp, #8]
138		MOV r0, r5
139		BL p_check_null_pointer
140		LDR r5, [r5]
141		STR r4, [r5]
142		B L3
143	L2:
144		LDR r4, [sp, #8]
145		MOV r0, r4
146		BL p_check_null_pointer
147		LDR r4, [r4, #4]
148		LDR r4, [r4]
149		STR r4, [sp]
150		LDR r4, [sp, #20]
151		STR r4, [sp, #-4]!
152		LDR r4, [sp, #4]
153		STR r4, [sp, #-4]!
154		BL f_insert
155		ADD sp, sp, #8
156		MOV r4, r0
157		LDR r5, [sp, #8]
158		MOV r0, r5
159		BL p_check_null_pointer
160		LDR r5, [r5, #4]
161		STR r4, [r5]
162	L3:
163		ADD sp, sp, #12
164	L1:
165		LDR r4, [sp, #4]
166		MOV r0, r4
167		POP {pc}
168		.ltorg
169	f_printTree:
170		PUSH {lr}
171		LDR r4, [sp, #4]
172		LDR r5, =0
173		CMP r4, r5
174		MOVEQ r4, #1
175		MOVNE r4, #0
176		CMP r4, #0
177		BEQ L4
178		LDR r4, =0
179		MOV r0, r4
180		B L5
181	L4:
182		SUB sp, sp, #12
183		LDR r4, [sp, #16]
184		MOV r0, r4
185		BL p_check_null_pointer
186		LDR r4, [r4, #4]
187		LDR r4, [r4]
188		STR r4, [sp, #8]
189		LDR r4, [sp, #8]
190		MOV r0, r4
191		BL p_check_null_pointer
192		LDR r4, [r4]
193		LDR r4, [r4]
194		STR r4, [sp, #4]
195		LDR r4, [sp, #4]
196		STR r4, [sp, #-4]!
197		BL f_printTree
198		ADD sp, sp, #4
199		MOV r4, r0
200		STR r4, [sp]
201		LDR r4, [sp, #16]
202		MOV r0, r4
203		BL p_check_null_pointer
204		LDR r4, [r4]
205		LDR r4, [r4]
206		STR r4, [sp]
207		LDR r4, [sp]
208		MOV r0, r4
209		BL p_print_int
210		MOV r4, #' '
211		MOV r0, r4
212		BL putchar
213		LDR r4, [sp, #8]
214		MOV r0, r4
215		BL p_check_null_pointer
216		LDR r4, [r4, #4]
217		LDR r4, [r4]
218		STR r4, [sp, #4]
219		LDR r4, [sp, #4]
220		STR r4, [sp, #-4]!
221		BL f_printTree
222		ADD sp, sp, #4
223		MOV r4, r0
224		STR r4, [sp]
225		LDR r4, =0
226		MOV r0, r4
227		ADD sp, sp, #12
228		ADD sp, sp, #12
229	L5:
230		POP {pc}
231		.ltorg
232	main:
233		PUSH {lr}
234		SUB sp, sp, #12
235		LDR r4, =0
236		STR r4, [sp, #8]
237		LDR r4, =msg_0
238		MOV r0, r4
239		BL p_print_string
240		ADD r4, sp, #8
241		MOV r0, r4
242		BL p_read_int
243		LDR r4, =msg_1
244		MOV r0, r4
245		BL p_print_string
246		LDR r4, [sp, #8]
247		MOV r0, r4
248		BL p_print_int
249		LDR r4, =msg_2
250		MOV r0, r4
251		BL p_print_string
252		BL p_print_ln
253		LDR r4, =0
254		STR r4, [sp, #4]
255		LDR r4, =0
256		STR r4, [sp]
257		B L6
258	L7:
259		SUB sp, sp, #4
260		LDR r4, =0
261		STR r4, [sp]
262		LDR r4, =msg_3
263		MOV r0, r4
264		BL p_print_string
265		LDR r4, [sp, #8]
266		LDR r5, =1
267		ADDS r4, r4, r5
268		BLVS p_throw_overflow_error
269		MOV r0, r4
270		BL p_print_int
271		LDR r4, =msg_4
272		MOV r0, r4
273		BL p_print_string
274		ADD r4, sp, #0
275		MOV r0, r4
276		BL p_read_int
277		LDR r4, [sp]
278		STR r4, [sp, #-4]!
279		LDR r4, [sp, #8]
280		STR r4, [sp, #-4]!
281		BL f_insert
282		ADD sp, sp, #8
283		MOV r4, r0
284		STR r4, [sp, #4]
285		LDR r4, [sp, #8]
286		LDR r5, =1
287		ADDS r4, r4, r5
288		BLVS p_throw_overflow_error
289		STR r4, [sp, #8]
290		ADD sp, sp, #4
291	L6:
292		LDR r4, [sp, #4]
293		LDR r5, [sp, #8]
294		CMP r4, r5
295		MOVLT r4, #1
296		MOVGE r4, #0
297		CMP r4, #1
298		BEQ L7
299		LDR r4, =msg_5
300		MOV r0, r4
301		BL p_print_string
302		LDR r4, [sp]
303		STR r4, [sp, #-4]!
304		BL f_printTree
305		ADD sp, sp, #4
306		MOV r4, r0
307		STR r4, [sp, #4]
308		LDR r4, =msg_6
309		MOV r0, r4
310		BL p_print_string
311		BL p_print_ln
312		ADD sp, sp, #12
313		LDR r0, =0
314		POP {pc}
315		.ltorg
316	p_check_null_pointer:
317		PUSH {lr}
318		CMP r0, #0
319		LDREQ r0, =msg_7
320		BLEQ p_throw_runtime_error
321		POP {pc}
322	p_print_int:
323		PUSH {lr}
324		MOV r1, r0
325		LDR r0, =msg_8
326		ADD r0, r0, #4
327		BL printf
328		MOV r0, #0
329		BL fflush
330		POP {pc}
331	p_print_string:
332		PUSH {lr}
333		LDR r1, [r0]
334		ADD r2, r0, #4
335		LDR r0, =msg_9
336		ADD r0, r0, #4
337		BL printf
338		MOV r0, #0
339		BL fflush
340		POP {pc}
341	p_read_int:
342		PUSH {lr}
343		MOV r1, r0
344		LDR r0, =msg_10
345		ADD r0, r0, #4
346		BL scanf
347		POP {pc}
348	p_print_ln:
349		PUSH {lr}
350		LDR r0, =msg_11
351		ADD r0, r0, #4
352		BL puts
353		MOV r0, #0
354		BL fflush
355		POP {pc}
356	p_throw_overflow_error:
357		LDR r0, =msg_12
358		BL p_throw_runtime_error
359	p_throw_runtime_error:
360		BL p_print_string
361		MOV r0, #-1
362		BL exit
363	
===========================================================
-- Finished

