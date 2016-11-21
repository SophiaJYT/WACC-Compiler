valid/advanced/hashTable.wacc
calling the reference compiler on valid/advanced/hashTable.wacc
-- Test: hashTable.wacc

-- Uploaded file: 
---------------------------------------------------------------
# This program is interactive. We implement a hash table containing integers and we play with it.
# 
# A hash table is represented by an array of node lists. Each node in a node list is represented 
# by pair(int, pair). The first element of the pair is the integer at that node, the second element 
# is the pointer to the next node (or null if no more node). 
# 
# Integers those mapped to the same index are stored on the list (in any order) in that index.

begin

	######################### Functions for Hash Table Interface ###############################
	
	# Given a hash table, initialise it. Return true.
	bool init(pair(int, pair)[] table) is
		int length = len table ;
		int i = 0 ;
		while i < length do
			table[i] = null ; 
			i = i + 1
		done ;
		return true
	end

	# Returns true if and only if the given hash table contains x.
	bool contain(pair(int, pair)[] table, int x) is
		int index = call calculateIndex(table, x) ;
		pair(int, pair) node = call findNode(table[index], x) ;
		return node != null
	end
	
	# Insert the given x into the hash table if it does not already contain x.
	# Returns true if and only if the table does not already contain x.
	bool insertIfNotContain(pair(int, pair)[] table, int x) is
		int index = call calculateIndex(table, x) ;
		pair(int, pair) node = call findNode(table[index], x) ;
		if node != null then
			# Already contain it. Do nothing.
			return false
		else
			# Insert in the front of the list.
			pair(int, pair) p = newpair(x, table[index]) ;
			table[index] = p ;
			return true 
		fi
	end
	
	# Remove the given x from the hash table. Returns true if an only if the table contains x. 
	# Otherwise, do nothing and returns false.
	bool remove(pair(int, pair)[] table, int x) is
		int index = call calculateIndex(table, x) ;
		pair(int, pair) node = call findNode(table[index], x) ;
		if node == null then
			# Not found x. Just return false.
			return false
		else
			# Found x, have to remove the node.
			table[index] = call removeNode(table[index], node) ;
			return true
		fi
	end
	
	# Remove all nodes from the table. Returns true.
	bool removeAll(pair(int, pair)[] table) is
		int length = len table ;
		int i = 0 ;
		while i < length do
			pair(int, pair) p = table[i] ;
			while p != null do
				pair(int, pair) p2 = snd p ;
				free p ;
				p = p2
			done ;
			table[i] = null ;
			i = i + 1
		done ; 
		return true
	end
	
	# Count the number of integers in the table and return it.
	int count(pair(int, pair)[] table) is
		int length = len table ;
		int sum = 0 ;
		int i = 0 ;
		while i < length do
			int subSum = call countNodes(table[i]) ;
			sum = sum + subSum ;
			i = i + 1
		done ;
		return sum
	end
	
	# Print all the integers inside the table, separated by a space and ended with a newline. Returns true.
	bool printAll(pair(int, pair)[] table) is
		int length = len table ;
		int i = 0 ;
		while i < length do
      bool result = call printAllNodes(table[i]) ;
			i = i + 1
		done ;
		println "" ;
		return true
	end
		
	# A helper function.
	# Given a hash table and an integer, calculate the index of the integer in the table.
	int calculateIndex(pair(int, pair)[] table, int x) is
		int length = len table ;
		return x % length
	end
	
	# A helper function.
	# Given a head of a chain of nodes, returns the first node containing the value x.
	# Returns null if no such node.
	pair(int, pair) findNode(pair(int, pair) head, int x) is
		while head != null do
			int y = fst head ;
			if y == x then
				return head
			else
				head = snd head
			fi 
		done ;
		return null
	end

	# A helper function.
	# Given a list of nodes and a node to remove, remove that node from the 
	# list and return the new list.
	pair(int, pair) removeNode(pair(int, pair) head, pair(int, pair) toRemove) is
		if head == null then
			# Should not happen actually.
			return null
		else
			if head == toRemove then
				# Save the new head.
				head = snd head ;
				
				# Deallocate the memory of the old node.
				free toRemove ;
				
				# Return the new head.
				return head
			else
				# Not this node, recursive.
				pair(int, pair) tail = snd head ;
				snd head = call removeNode(tail, toRemove) ;
				return head
			fi
		fi
	end

	# A helper function.
	# Given a list of nodes, count how many nodes there are.
	int countNodes(pair(int, pair) head) is 
		int sum = 0 ;
		while head != null do
			sum = sum + 1 ;
			head = snd head
		done ;
		return sum
	end

	# A helper function.
	# Given a list of nodes, print each integer in the node followed by a space. Returns true.
	bool printAllNodes(pair(int, pair) head) is
    while head != null do
			int x = fst head ;
			print x ;
			print ' ' ;
			head = snd head
		done ;
		return true
	end

	######################### Functions for Command Line Interface ###############################
	
	# Print the menu and ask to choose. Returns a valid decision.
	char printMenu() is
		println "===========================================" ;
		println "========== Hash Table Program =============" ;
		println "===========================================" ;
		println "=                                         =" ;
		println "= Please choose the following options:    =" ;
		println "=                                         =" ;
		println "= a: insert an integer                    =" ;
		println "= b: find an integer                      =" ;
		println "= c: count the integers                   =" ;
		println "= d: print all integers                   =" ;
		println "= e: remove an integer                    =" ;
		println "= f: remove all integers                  =" ;
		println "= g: exit                                 =" ;
		println "=                                         =" ;
		println "===========================================" ;
		
		int minChoice = ord 'a' ;
		int maxChoice = ord 'g' ;
		
		while true do
			print   "Your decision: " ;
			char d = '\0' ;
			read d ;
			int dInt = ord d ;
			if minChoice <= dInt && dInt <= maxChoice then
				return d
			else 
				print "You have entered: " ;
				print d ;
				println " which is invalid, please try again."
			fi
		done ;
		# The compiler is not smart enough to know that this never reaches. 
		# We have to add a return statement here.
		return '\0'
	end
	
	# Print out the question, and then read an integer. After that print the integer back and return it.
	int askForInt(char[] message) is
		print message ;
		int x = 0 ;
		read x ;
		print "You have entered: " ;
		println x ;
		return x
	end
	
	# Handle menu insert. Returns true.
	bool handleMenuInsert(pair(int, pair)[] table) is
		int x = call askForInt("Please enter an integer to insert: ") ;
		bool notContain = call insertIfNotContain(table, x) ;
		if notContain then
			println "Successfully insert it. The integer is new." 
		else 
			println "The integer is already there. No insertion is made."
		fi ;
		return true		
	end
	
	# Handle menu find. Returns true.
	bool handleMenuFind(pair(int, pair)[] table) is
		int x = call askForInt("Please enter an integer to find: ") ;
		bool find = call contain(table, x) ;
		if find then
			println "Find the integer." 
		else 
			println "The integer is not found."
		fi ;
		return true		
	end
	
	# Handle menu count. Returns true.
	bool handleMenuCount(pair(int, pair)[] table) is
		int size = call count(table) ;
		if size == 1 then
			println "There is only 1 integer."
		else
			print "There are " ;
			print size ;
			println " integers."
		fi ; 
		return true
	end
	
	# Handle menu print. Returns true.
	bool handleMenuPrint(pair(int, pair)[] table) is
		print "Here are the integers: " ;
		bool junk = call printAll(table) ;
		return true
	end
	
	# Handle menu remove. Returns true.
	bool handleMenuRemove(pair(int, pair)[] table) is
		int x = call askForInt("Please enter an integer to remove: ") ;
		bool find = call remove(table, x) ;
		if find then
			println "The integer has been removed." 
		else 
			println "The integer is not found."
		fi ;
		return true		
	end
	
	# Handle menu remove all. Returns true.
	bool handleMenuRemoveAll(pair(int, pair)[] table) is
		bool junk = call removeAll(table) ; 
		println "All integers have been removed." ; 
		return true
	end
	
	################################# The main function ########################################
	# Our hash table of size 13.
	pair(int, pair)[] table = [null, null, null, null, null, null, null, null, null, null, null, null, null] ;
	bool junk = call init(table) ;
	
	bool continue = true ;
	while continue do
		char choice = call printMenu() ;
		if choice == 'a' then
			bool result = call handleMenuInsert(table)
		else if choice == 'b' then
			bool result = call handleMenuFind(table)
		else if choice == 'c' then
			bool result = call handleMenuCount(table)
		else if choice == 'd' then
			bool result = call handleMenuPrint(table)
		else if choice == 'e' then
			bool result = call handleMenuRemove(table)
		else if choice == 'f' then
			bool result = call handleMenuRemoveAll(table)
		else if choice == 'g' then
			println "Goodbye Human" ;
			continue = false
		else
			# Should not happen.
			print "Error: unknown choice (" ;
			print choice ;
			println ")" ;
			exit -1
		fi fi fi fi fi fi fi
	done
	
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
hashTable.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 0
4		.ascii	""
5	msg_1:
6		.word 43
7		.ascii	"==========================================="
8	msg_2:
9		.word 43
10		.ascii	"========== Hash Table Program ============="
11	msg_3:
12		.word 43
13		.ascii	"==========================================="
14	msg_4:
15		.word 43
16		.ascii	"=                                         ="
17	msg_5:
18		.word 43
19		.ascii	"= Please choose the following options:    ="
20	msg_6:
21		.word 43
22		.ascii	"=                                         ="
23	msg_7:
24		.word 43
25		.ascii	"= a: insert an integer                    ="
26	msg_8:
27		.word 43
28		.ascii	"= b: find an integer                      ="
29	msg_9:
30		.word 43
31		.ascii	"= c: count the integers                   ="
32	msg_10:
33		.word 43
34		.ascii	"= d: print all integers                   ="
35	msg_11:
36		.word 43
37		.ascii	"= e: remove an integer                    ="
38	msg_12:
39		.word 43
40		.ascii	"= f: remove all integers                  ="
41	msg_13:
42		.word 43
43		.ascii	"= g: exit                                 ="
44	msg_14:
45		.word 43
46		.ascii	"=                                         ="
47	msg_15:
48		.word 43
49		.ascii	"==========================================="
50	msg_16:
51		.word 15
52		.ascii	"Your decision: "
53	msg_17:
54		.word 18
55		.ascii	"You have entered: "
56	msg_18:
57		.word 36
58		.ascii	" which is invalid, please try again."
59	msg_19:
60		.word 18
61		.ascii	"You have entered: "
62	msg_20:
63		.word 35
64		.ascii	"Please enter an integer to insert: "
65	msg_21:
66		.word 43
67		.ascii	"Successfully insert it. The integer is new."
68	msg_22:
69		.word 51
70		.ascii	"The integer is already there. No insertion is made."
71	msg_23:
72		.word 33
73		.ascii	"Please enter an integer to find: "
74	msg_24:
75		.word 17
76		.ascii	"Find the integer."
77	msg_25:
78		.word 25
79		.ascii	"The integer is not found."
80	msg_26:
81		.word 24
82		.ascii	"There is only 1 integer."
83	msg_27:
84		.word 10
85		.ascii	"There are "
86	msg_28:
87		.word 10
88		.ascii	" integers."
89	msg_29:
90		.word 23
91		.ascii	"Here are the integers: "
92	msg_30:
93		.word 35
94		.ascii	"Please enter an integer to remove: "
95	msg_31:
96		.word 29
97		.ascii	"The integer has been removed."
98	msg_32:
99		.word 25
100		.ascii	"The integer is not found."
101	msg_33:
102		.word 31
103		.ascii	"All integers have been removed."
104	msg_34:
105		.word 13
106		.ascii	"Goodbye Human"
107	msg_35:
108		.word 23
109		.ascii	"Error: unknown choice ("
110	msg_36:
111		.word 1
112		.ascii	")"
113	msg_37:
114		.word 44
115		.ascii	"ArrayIndexOutOfBoundsError: negative index\n\0"
116	msg_38:
117		.word 45
118		.ascii	"ArrayIndexOutOfBoundsError: index too large\n\0"
119	msg_39:
120		.word 82
121		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
122	msg_40:
123		.word 50
124		.ascii	"NullReferenceError: dereference a null reference\n\0"
125	msg_41:
126		.word 50
127		.ascii	"NullReferenceError: dereference a null reference\n\0"
128	msg_42:
129		.word 5
130		.ascii	"%.*s\0"
131	msg_43:
132		.word 1
133		.ascii	"\0"
134	msg_44:
135		.word 45
136		.ascii	"DivideByZeroError: divide or modulo by zero\n\0"
137	msg_45:
138		.word 3
139		.ascii	"%d\0"
140	msg_46:
141		.word 4
142		.ascii	" %c\0"
143	msg_47:
144		.word 3
145		.ascii	"%d\0"
146	
147	.text
148	
149	.global main
150	f_init:
151		PUSH {lr}
152		SUB sp, sp, #8
153		LDR r4, [sp, #12]
154		LDR r4, [r4]
155		STR r4, [sp, #4]
156		LDR r4, =0
157		STR r4, [sp]
158		B L0
159	L1:
160		LDR r4, =0
161		ADD r5, sp, #12
162		LDR r6, [sp]
163		LDR r5, [r5]
164		MOV r0, r6
165		MOV r1, r5
166		BL p_check_array_bounds
167		ADD r5, r5, #4
168		ADD r5, r5, r6, LSL #2
169		STR r4, [r5]
170		LDR r4, [sp]
171		LDR r6, =1
172		ADDS r4, r4, r6
173		BLVS p_throw_overflow_error
174		STR r4, [sp]
175	L0:
176		LDR r4, [sp]
177		LDR r6, [sp, #4]
178		CMP r4, r6
179		MOVLT r4, #1
180		MOVGE r4, #0
181		CMP r4, #1
182		BEQ L1
183		MOV r4, #1
184		MOV r0, r4
185		ADD sp, sp, #8
186		POP {pc}
187		.ltorg
188	f_contain:
189		PUSH {lr}
190		SUB sp, sp, #8
191		LDR r4, [sp, #16]
192		STR r4, [sp, #-4]!
193		LDR r4, [sp, #16]
194		STR r4, [sp, #-4]!
195		BL f_calculateIndex
196		ADD sp, sp, #8
197		MOV r4, r0
198		STR r4, [sp, #4]
199		LDR r4, [sp, #16]
200		STR r4, [sp, #-4]!
201		ADD r4, sp, #16
202		LDR r5, [sp, #8]
203		LDR r4, [r4]
204		MOV r0, r5
205		MOV r1, r4
206		BL p_check_array_bounds
207		ADD r4, r4, #4
208		ADD r4, r4, r5, LSL #2
209		LDR r4, [r4]
210		STR r4, [sp, #-4]!
211		BL f_findNode
212		ADD sp, sp, #8
213		MOV r4, r0
214		STR r4, [sp]
215		LDR r4, [sp]
216		LDR r5, =0
217		CMP r4, r5
218		MOVNE r4, #1
219		MOVEQ r4, #0
220		MOV r0, r4
221		ADD sp, sp, #8
222		POP {pc}
223		.ltorg
224	f_insertIfNotContain:
225		PUSH {lr}
226		SUB sp, sp, #8
227		LDR r4, [sp, #16]
228		STR r4, [sp, #-4]!
229		LDR r4, [sp, #16]
230		STR r4, [sp, #-4]!
231		BL f_calculateIndex
232		ADD sp, sp, #8
233		MOV r4, r0
234		STR r4, [sp, #4]
235		LDR r4, [sp, #16]
236		STR r4, [sp, #-4]!
237		ADD r4, sp, #16
238		LDR r5, [sp, #8]
239		LDR r4, [r4]
240		MOV r0, r5
241		MOV r1, r4
242		BL p_check_array_bounds
243		ADD r4, r4, #4
244		ADD r4, r4, r5, LSL #2
245		LDR r4, [r4]
246		STR r4, [sp, #-4]!
247		BL f_findNode
248		ADD sp, sp, #8
249		MOV r4, r0
250		STR r4, [sp]
251		LDR r4, [sp]
252		LDR r5, =0
253		CMP r4, r5
254		MOVNE r4, #1
255		MOVEQ r4, #0
256		CMP r4, #0
257		BEQ L2
258		MOV r4, #0
259		MOV r0, r4
260		ADD sp, sp, #8
261		B L3
262	L2:
263		SUB sp, sp, #4
264		LDR r0, =8
265		BL malloc
266		MOV r4, r0
267		LDR r5, [sp, #20]
268		LDR r0, =4
269		BL malloc
270		STR r5, [r0]
271		STR r0, [r4]
272		ADD r5, sp, #16
273		LDR r6, [sp, #8]
274		LDR r5, [r5]
275		MOV r0, r6
276		MOV r1, r5
277		BL p_check_array_bounds
278		ADD r5, r5, #4
279		ADD r5, r5, r6, LSL #2
280		LDR r5, [r5]
281		LDR r0, =4
282		BL malloc
283		STR r5, [r0]
284		STR r0, [r4, #4]
285		STR r4, [sp]
286		LDR r4, [sp]
287		ADD r5, sp, #16
288		LDR r6, [sp, #8]
289		LDR r5, [r5]
290		MOV r0, r6
291		MOV r1, r5
292		BL p_check_array_bounds
293		ADD r5, r5, #4
294		ADD r5, r5, r6, LSL #2
295		STR r4, [r5]
296		MOV r4, #1
297		MOV r0, r4
298		ADD sp, sp, #12
299		ADD sp, sp, #4
300	L3:
301		POP {pc}
302		.ltorg
303	f_remove:
304		PUSH {lr}
305		SUB sp, sp, #8
306		LDR r4, [sp, #16]
307		STR r4, [sp, #-4]!
308		LDR r4, [sp, #16]
309		STR r4, [sp, #-4]!
310		BL f_calculateIndex
311		ADD sp, sp, #8
312		MOV r4, r0
313		STR r4, [sp, #4]
314		LDR r4, [sp, #16]
315		STR r4, [sp, #-4]!
316		ADD r4, sp, #16
317		LDR r5, [sp, #8]
318		LDR r4, [r4]
319		MOV r0, r5
320		MOV r1, r4
321		BL p_check_array_bounds
322		ADD r4, r4, #4
323		ADD r4, r4, r5, LSL #2
324		LDR r4, [r4]
325		STR r4, [sp, #-4]!
326		BL f_findNode
327		ADD sp, sp, #8
328		MOV r4, r0
329		STR r4, [sp]
330		LDR r4, [sp]
331		LDR r5, =0
332		CMP r4, r5
333		MOVEQ r4, #1
334		MOVNE r4, #0
335		CMP r4, #0
336		BEQ L4
337		MOV r4, #0
338		MOV r0, r4
339		ADD sp, sp, #8
340		B L5
341	L4:
342		LDR r4, [sp]
343		STR r4, [sp, #-4]!
344		ADD r4, sp, #16
345		LDR r5, [sp, #8]
346		LDR r4, [r4]
347		MOV r0, r5
348		MOV r1, r4
349		BL p_check_array_bounds
350		ADD r4, r4, #4
351		ADD r4, r4, r5, LSL #2
352		LDR r4, [r4]
353		STR r4, [sp, #-4]!
354		BL f_removeNode
355		ADD sp, sp, #8
356		MOV r4, r0
357		ADD r5, sp, #12
358		LDR r6, [sp, #4]
359		LDR r5, [r5]
360		MOV r0, r6
361		MOV r1, r5
362		BL p_check_array_bounds
363		ADD r5, r5, #4
364		ADD r5, r5, r6, LSL #2
365		STR r4, [r5]
366		MOV r4, #1
367		MOV r0, r4
368		ADD sp, sp, #8
369	L5:
370		POP {pc}
371		.ltorg
372	f_removeAll:
373		PUSH {lr}
374		SUB sp, sp, #8
375		LDR r4, [sp, #12]
376		LDR r4, [r4]
377		STR r4, [sp, #4]
378		LDR r4, =0
379		STR r4, [sp]
380		B L6
381	L7:
382		SUB sp, sp, #4
383		ADD r4, sp, #16
384		LDR r5, [sp, #4]
385		LDR r4, [r4]
386		MOV r0, r5
387		MOV r1, r4
388		BL p_check_array_bounds
389		ADD r4, r4, #4
390		ADD r4, r4, r5, LSL #2
391		LDR r4, [r4]
392		STR r4, [sp]
393		B L8
394	L9:
395		SUB sp, sp, #4
396		LDR r4, [sp, #4]
397		MOV r0, r4
398		BL p_check_null_pointer
399		LDR r4, [r4, #4]
400		LDR r4, [r4]
401		STR r4, [sp]
402		LDR r4, [sp, #4]
403		MOV r0, r4
404		BL p_free_pair
405		LDR r4, [sp]
406		STR r4, [sp, #4]
407		ADD sp, sp, #4
408	L8:
409		LDR r4, [sp]
410		LDR r5, =0
411		CMP r4, r5
412		MOVNE r4, #1
413		MOVEQ r4, #0
414		CMP r4, #1
415		BEQ L9
416		LDR r4, =0
417		ADD r5, sp, #16
418		LDR r6, [sp, #4]
419		LDR r5, [r5]
420		MOV r0, r6
421		MOV r1, r5
422		BL p_check_array_bounds
423		ADD r5, r5, #4
424		ADD r5, r5, r6, LSL #2
425		STR r4, [r5]
426		LDR r4, [sp, #4]
427		LDR r6, =1
428		ADDS r4, r4, r6
429		BLVS p_throw_overflow_error
430		STR r4, [sp, #4]
431		ADD sp, sp, #4
432	L6:
433		LDR r4, [sp]
434		LDR r6, [sp, #4]
435		CMP r4, r6
436		MOVLT r4, #1
437		MOVGE r4, #0
438		CMP r4, #1
439		BEQ L7
440		MOV r4, #1
441		MOV r0, r4
442		ADD sp, sp, #8
443		POP {pc}
444		.ltorg
445	f_count:
446		PUSH {lr}
447		SUB sp, sp, #12
448		LDR r4, [sp, #16]
449		LDR r4, [r4]
450		STR r4, [sp, #8]
451		LDR r4, =0
452		STR r4, [sp, #4]
453		LDR r4, =0
454		STR r4, [sp]
455		B L10
456	L11:
457		SUB sp, sp, #4
458		ADD r4, sp, #20
459		LDR r5, [sp, #4]
460		LDR r4, [r4]
461		MOV r0, r5
462		MOV r1, r4
463		BL p_check_array_bounds
464		ADD r4, r4, #4
465		ADD r4, r4, r5, LSL #2
466		LDR r4, [r4]
467		STR r4, [sp, #-4]!
468		BL f_countNodes
469		ADD sp, sp, #4
470		MOV r4, r0
471		STR r4, [sp]
472		LDR r4, [sp, #8]
473		LDR r5, [sp]
474		ADDS r4, r4, r5
475		BLVS p_throw_overflow_error
476		STR r4, [sp, #8]
477		LDR r4, [sp, #4]
478		LDR r5, =1
479		ADDS r4, r4, r5
480		BLVS p_throw_overflow_error
481		STR r4, [sp, #4]
482		ADD sp, sp, #4
483	L10:
484		LDR r4, [sp]
485		LDR r5, [sp, #8]
486		CMP r4, r5
487		MOVLT r4, #1
488		MOVGE r4, #0
489		CMP r4, #1
490		BEQ L11
491		LDR r4, [sp, #4]
492		MOV r0, r4
493		ADD sp, sp, #12
494		POP {pc}
495		.ltorg
496	f_printAll:
497		PUSH {lr}
498		SUB sp, sp, #8
499		LDR r4, [sp, #12]
500		LDR r4, [r4]
501		STR r4, [sp, #4]
502		LDR r4, =0
503		STR r4, [sp]
504		B L12
505	L13:
506		SUB sp, sp, #1
507		ADD r4, sp, #13
508		LDR r5, [sp, #1]
509		LDR r4, [r4]
510		MOV r0, r5
511		MOV r1, r4
512		BL p_check_array_bounds
513		ADD r4, r4, #4
514		ADD r4, r4, r5, LSL #2
515		LDR r4, [r4]
516		STR r4, [sp, #-4]!
517		BL f_printAllNodes
518		ADD sp, sp, #4
519		MOV r4, r0
520		STRB r4, [sp]
521		LDR r4, [sp, #1]
522		LDR r5, =1
523		ADDS r4, r4, r5
524		BLVS p_throw_overflow_error
525		STR r4, [sp, #1]
526		ADD sp, sp, #1
527	L12:
528		LDR r4, [sp]
529		LDR r5, [sp, #4]
530		CMP r4, r5
531		MOVLT r4, #1
532		MOVGE r4, #0
533		CMP r4, #1
534		BEQ L13
535		LDR r4, =msg_0
536		MOV r0, r4
537		BL p_print_string
538		BL p_print_ln
539		MOV r4, #1
540		MOV r0, r4
541		ADD sp, sp, #8
542		POP {pc}
543		.ltorg
544	f_calculateIndex:
545		PUSH {lr}
546		SUB sp, sp, #4
547		LDR r4, [sp, #8]
548		LDR r4, [r4]
549		STR r4, [sp]
550		LDR r4, [sp, #12]
551		LDR r5, [sp]
552		MOV r0, r4
553		MOV r1, r5
554		BL p_check_divide_by_zero
555		BL __aeabi_idivmod
556		MOV r4, r1
557		MOV r0, r4
558		ADD sp, sp, #4
559		POP {pc}
560		.ltorg
561	f_findNode:
562		PUSH {lr}
563		B L14
564	L15:
565		SUB sp, sp, #4
566		LDR r4, [sp, #8]
567		MOV r0, r4
568		BL p_check_null_pointer
569		LDR r4, [r4]
570		LDR r4, [r4]
571		STR r4, [sp]
572		LDR r4, [sp]
573		LDR r5, [sp, #12]
574		CMP r4, r5
575		MOVEQ r4, #1
576		MOVNE r4, #0
577		CMP r4, #0
578		BEQ L16
579		LDR r4, [sp, #8]
580		MOV r0, r4
581		ADD sp, sp, #4
582		B L17
583	L16:
584		LDR r4, [sp, #8]
585		MOV r0, r4
586		BL p_check_null_pointer
587		LDR r4, [r4, #4]
588		LDR r4, [r4]
589		STR r4, [sp, #8]
590	L17:
591		ADD sp, sp, #4
592	L14:
593		LDR r4, [sp, #4]
594		LDR r5, =0
595		CMP r4, r5
596		MOVNE r4, #1
597		MOVEQ r4, #0
598		CMP r4, #1
599		BEQ L15
600		LDR r4, =0
601		MOV r0, r4
602		POP {pc}
603		.ltorg
604	f_removeNode:
605		PUSH {lr}
606		LDR r4, [sp, #4]
607		LDR r5, =0
608		CMP r4, r5
609		MOVEQ r4, #1
610		MOVNE r4, #0
611		CMP r4, #0
612		BEQ L18
613		LDR r4, =0
614		MOV r0, r4
615		B L19
616	L18:
617		LDR r4, [sp, #4]
618		LDR r5, [sp, #8]
619		CMP r4, r5
620		MOVEQ r4, #1
621		MOVNE r4, #0
622		CMP r4, #0
623		BEQ L20
624		LDR r4, [sp, #4]
625		MOV r0, r4
626		BL p_check_null_pointer
627		LDR r4, [r4, #4]
628		LDR r4, [r4]
629		STR r4, [sp, #4]
630		LDR r4, [sp, #8]
631		MOV r0, r4
632		BL p_free_pair
633		LDR r4, [sp, #4]
634		MOV r0, r4
635		B L21
636	L20:
637		SUB sp, sp, #4
638		LDR r4, [sp, #8]
639		MOV r0, r4
640		BL p_check_null_pointer
641		LDR r4, [r4, #4]
642		LDR r4, [r4]
643		STR r4, [sp]
644		LDR r4, [sp, #12]
645		STR r4, [sp, #-4]!
646		LDR r4, [sp, #4]
647		STR r4, [sp, #-4]!
648		BL f_removeNode
649		ADD sp, sp, #8
650		MOV r4, r0
651		LDR r5, [sp, #8]
652		MOV r0, r5
653		BL p_check_null_pointer
654		LDR r5, [r5, #4]
655		STR r4, [r5]
656		LDR r4, [sp, #8]
657		MOV r0, r4
658		ADD sp, sp, #4
659		ADD sp, sp, #4
660	L21:
661	L19:
662		POP {pc}
663		.ltorg
664	f_countNodes:
665		PUSH {lr}
666		SUB sp, sp, #4
667		LDR r4, =0
668		STR r4, [sp]
669		B L22
670	L23:
671		LDR r4, [sp]
672		LDR r5, =1
673		ADDS r4, r4, r5
674		BLVS p_throw_overflow_error
675		STR r4, [sp]
676		LDR r4, [sp, #8]
677		MOV r0, r4
678		BL p_check_null_pointer
679		LDR r4, [r4, #4]
680		LDR r4, [r4]
681		STR r4, [sp, #8]
682	L22:
683		LDR r4, [sp, #8]
684		LDR r5, =0
685		CMP r4, r5
686		MOVNE r4, #1
687		MOVEQ r4, #0
688		CMP r4, #1
689		BEQ L23
690		LDR r4, [sp]
691		MOV r0, r4
692		ADD sp, sp, #4
693		POP {pc}
694		.ltorg
695	f_printAllNodes:
696		PUSH {lr}
697		B L24
698	L25:
699		SUB sp, sp, #4
700		LDR r4, [sp, #8]
701		MOV r0, r4
702		BL p_check_null_pointer
703		LDR r4, [r4]
704		LDR r4, [r4]
705		STR r4, [sp]
706		LDR r4, [sp]
707		MOV r0, r4
708		BL p_print_int
709		MOV r4, #' '
710		MOV r0, r4
711		BL putchar
712		LDR r4, [sp, #8]
713		MOV r0, r4
714		BL p_check_null_pointer
715		LDR r4, [r4, #4]
716		LDR r4, [r4]
717		STR r4, [sp, #8]
718		ADD sp, sp, #4
719	L24:
720		LDR r4, [sp, #4]
721		LDR r5, =0
722		CMP r4, r5
723		MOVNE r4, #1
724		MOVEQ r4, #0
725		CMP r4, #1
726		BEQ L25
727		MOV r4, #1
728		MOV r0, r4
729		POP {pc}
730		.ltorg
731	f_printMenu:
732		PUSH {lr}
733		SUB sp, sp, #8
734		LDR r4, =msg_1
735		MOV r0, r4
736		BL p_print_string
737		BL p_print_ln
738		LDR r4, =msg_2
739		MOV r0, r4
740		BL p_print_string
741		BL p_print_ln
742		LDR r4, =msg_3
743		MOV r0, r4
744		BL p_print_string
745		BL p_print_ln
746		LDR r4, =msg_4
747		MOV r0, r4
748		BL p_print_string
749		BL p_print_ln
750		LDR r4, =msg_5
751		MOV r0, r4
752		BL p_print_string
753		BL p_print_ln
754		LDR r4, =msg_6
755		MOV r0, r4
756		BL p_print_string
757		BL p_print_ln
758		LDR r4, =msg_7
759		MOV r0, r4
760		BL p_print_string
761		BL p_print_ln
762		LDR r4, =msg_8
763		MOV r0, r4
764		BL p_print_string
765		BL p_print_ln
766		LDR r4, =msg_9
767		MOV r0, r4
768		BL p_print_string
769		BL p_print_ln
770		LDR r4, =msg_10
771		MOV r0, r4
772		BL p_print_string
773		BL p_print_ln
774		LDR r4, =msg_11
775		MOV r0, r4
776		BL p_print_string
777		BL p_print_ln
778		LDR r4, =msg_12
779		MOV r0, r4
780		BL p_print_string
781		BL p_print_ln
782		LDR r4, =msg_13
783		MOV r0, r4
784		BL p_print_string
785		BL p_print_ln
786		LDR r4, =msg_14
787		MOV r0, r4
788		BL p_print_string
789		BL p_print_ln
790		LDR r4, =msg_15
791		MOV r0, r4
792		BL p_print_string
793		BL p_print_ln
794		MOV r4, #'a'
795		STR r4, [sp, #4]
796		MOV r4, #'g'
797		STR r4, [sp]
798		B L26
799	L27:
800		SUB sp, sp, #5
801		LDR r4, =msg_16
802		MOV r0, r4
803		BL p_print_string
804		MOV r4, #0
805		STRB r4, [sp, #4]
806		ADD r4, sp, #4
807		MOV r0, r4
808		BL p_read_char
809		LDRSB r4, [sp, #4]
810		STR r4, [sp]
811		LDR r4, [sp, #9]
812		LDR r5, [sp]
813		CMP r4, r5
814		MOVLE r4, #1
815		MOVGT r4, #0
816		LDR r5, [sp]
817		LDR r6, [sp, #5]
818		CMP r5, r6
819		MOVLE r5, #1
820		MOVGT r5, #0
821		AND r4, r4, r5
822		CMP r4, #0
823		BEQ L28
824		LDRSB r4, [sp, #4]
825		MOV r0, r4
826		ADD sp, sp, #13
827		B L29
828	L28:
829		LDR r4, =msg_17
830		MOV r0, r4
831		BL p_print_string
832		LDRSB r4, [sp, #4]
833		MOV r0, r4
834		BL putchar
835		LDR r4, =msg_18
836		MOV r0, r4
837		BL p_print_string
838		BL p_print_ln
839	L29:
840		ADD sp, sp, #5
841	L26:
842		MOV r4, #1
843		CMP r4, #1
844		BEQ L27
845		MOV r4, #0
846		MOV r0, r4
847		ADD sp, sp, #8
848		POP {pc}
849		.ltorg
850	f_askForInt:
851		PUSH {lr}
852		SUB sp, sp, #4
853		LDR r4, [sp, #8]
854		MOV r0, r4
855		BL p_print_string
856		LDR r4, =0
857		STR r4, [sp]
858		ADD r4, sp, #0
859		MOV r0, r4
860		BL p_read_int
861		LDR r4, =msg_19
862		MOV r0, r4
863		BL p_print_string
864		LDR r4, [sp]
865		MOV r0, r4
866		BL p_print_int
867		BL p_print_ln
868		LDR r4, [sp]
869		MOV r0, r4
870		ADD sp, sp, #4
871		POP {pc}
872		.ltorg
873	f_handleMenuInsert:
874		PUSH {lr}
875		SUB sp, sp, #5
876		LDR r4, =msg_20
877		STR r4, [sp, #-4]!
878		BL f_askForInt
879		ADD sp, sp, #4
880		MOV r4, r0
881		STR r4, [sp, #1]
882		LDR r4, [sp, #1]
883		STR r4, [sp, #-4]!
884		LDR r4, [sp, #13]
885		STR r4, [sp, #-4]!
886		BL f_insertIfNotContain
887		ADD sp, sp, #8
888		MOV r4, r0
889		STRB r4, [sp]
890		LDRSB r4, [sp]
891		CMP r4, #0
892		BEQ L30
893		LDR r4, =msg_21
894		MOV r0, r4
895		BL p_print_string
896		BL p_print_ln
897		B L31
898	L30:
899		LDR r4, =msg_22
900		MOV r0, r4
901		BL p_print_string
902		BL p_print_ln
903	L31:
904		MOV r4, #1
905		MOV r0, r4
906		ADD sp, sp, #5
907		POP {pc}
908		.ltorg
909	f_handleMenuFind:
910		PUSH {lr}
911		SUB sp, sp, #5
912		LDR r4, =msg_23
913		STR r4, [sp, #-4]!
914		BL f_askForInt
915		ADD sp, sp, #4
916		MOV r4, r0
917		STR r4, [sp, #1]
918		LDR r4, [sp, #1]
919		STR r4, [sp, #-4]!
920		LDR r4, [sp, #13]
921		STR r4, [sp, #-4]!
922		BL f_contain
923		ADD sp, sp, #8
924		MOV r4, r0
925		STRB r4, [sp]
926		LDRSB r4, [sp]
927		CMP r4, #0
928		BEQ L32
929		LDR r4, =msg_24
930		MOV r0, r4
931		BL p_print_string
932		BL p_print_ln
933		B L33
934	L32:
935		LDR r4, =msg_25
936		MOV r0, r4
937		BL p_print_string
938		BL p_print_ln
939	L33:
940		MOV r4, #1
941		MOV r0, r4
942		ADD sp, sp, #5
943		POP {pc}
944		.ltorg
945	f_handleMenuCount:
946		PUSH {lr}
947		SUB sp, sp, #4
948		LDR r4, [sp, #8]
949		STR r4, [sp, #-4]!
950		BL f_count
951		ADD sp, sp, #4
952		MOV r4, r0
953		STR r4, [sp]
954		LDR r4, [sp]
955		LDR r5, =1
956		CMP r4, r5
957		MOVEQ r4, #1
958		MOVNE r4, #0
959		CMP r4, #0
960		BEQ L34
961		LDR r4, =msg_26
962		MOV r0, r4
963		BL p_print_string
964		BL p_print_ln
965		B L35
966	L34:
967		LDR r4, =msg_27
968		MOV r0, r4
969		BL p_print_string
970		LDR r4, [sp]
971		MOV r0, r4
972		BL p_print_int
973		LDR r4, =msg_28
974		MOV r0, r4
975		BL p_print_string
976		BL p_print_ln
977	L35:
978		MOV r4, #1
979		MOV r0, r4
980		ADD sp, sp, #4
981		POP {pc}
982		.ltorg
983	f_handleMenuPrint:
984		PUSH {lr}
985		SUB sp, sp, #1
986		LDR r4, =msg_29
987		MOV r0, r4
988		BL p_print_string
989		LDR r4, [sp, #5]
990		STR r4, [sp, #-4]!
991		BL f_printAll
992		ADD sp, sp, #4
993		MOV r4, r0
994		STRB r4, [sp]
995		MOV r4, #1
996		MOV r0, r4
997		ADD sp, sp, #1
998		POP {pc}
999		.ltorg
1000	f_handleMenuRemove:
1001		PUSH {lr}
1002		SUB sp, sp, #5
1003		LDR r4, =msg_30
1004		STR r4, [sp, #-4]!
1005		BL f_askForInt
1006		ADD sp, sp, #4
1007		MOV r4, r0
1008		STR r4, [sp, #1]
1009		LDR r4, [sp, #1]
1010		STR r4, [sp, #-4]!
1011		LDR r4, [sp, #13]
1012		STR r4, [sp, #-4]!
1013		BL f_remove
1014		ADD sp, sp, #8
1015		MOV r4, r0
1016		STRB r4, [sp]
1017		LDRSB r4, [sp]
1018		CMP r4, #0
1019		BEQ L36
1020		LDR r4, =msg_31
1021		MOV r0, r4
1022		BL p_print_string
1023		BL p_print_ln
1024		B L37
1025	L36:
1026		LDR r4, =msg_32
1027		MOV r0, r4
1028		BL p_print_string
1029		BL p_print_ln
1030	L37:
1031		MOV r4, #1
1032		MOV r0, r4
1033		ADD sp, sp, #5
1034		POP {pc}
1035		.ltorg
1036	f_handleMenuRemoveAll:
1037		PUSH {lr}
1038		SUB sp, sp, #1
1039		LDR r4, [sp, #5]
1040		STR r4, [sp, #-4]!
1041		BL f_removeAll
1042		ADD sp, sp, #4
1043		MOV r4, r0
1044		STRB r4, [sp]
1045		LDR r4, =msg_33
1046		MOV r0, r4
1047		BL p_print_string
1048		BL p_print_ln
1049		MOV r4, #1
1050		MOV r0, r4
1051		ADD sp, sp, #1
1052		POP {pc}
1053		.ltorg
1054	main:
1055		PUSH {lr}
1056		SUB sp, sp, #6
1057		LDR r0, =56
1058		BL malloc
1059		MOV r4, r0
1060		LDR r5, =0
1061		STR r5, [r4, #4]
1062		LDR r5, =0
1063		STR r5, [r4, #8]
1064		LDR r5, =0
1065		STR r5, [r4, #12]
1066		LDR r5, =0
1067		STR r5, [r4, #16]
1068		LDR r5, =0
1069		STR r5, [r4, #20]
1070		LDR r5, =0
1071		STR r5, [r4, #24]
1072		LDR r5, =0
1073		STR r5, [r4, #28]
1074		LDR r5, =0
1075		STR r5, [r4, #32]
1076		LDR r5, =0
1077		STR r5, [r4, #36]
1078		LDR r5, =0
1079		STR r5, [r4, #40]
1080		LDR r5, =0
1081		STR r5, [r4, #44]
1082		LDR r5, =0
1083		STR r5, [r4, #48]
1084		LDR r5, =0
1085		STR r5, [r4, #52]
1086		LDR r5, =13
1087		STR r5, [r4]
1088		STR r4, [sp, #2]
1089		LDR r4, [sp, #2]
1090		STR r4, [sp, #-4]!
1091		BL f_init
1092		ADD sp, sp, #4
1093		MOV r4, r0
1094		STRB r4, [sp, #1]
1095		MOV r4, #1
1096		STRB r4, [sp]
1097		B L38
1098	L39:
1099		SUB sp, sp, #1
1100		BL f_printMenu
1101		MOV r4, r0
1102		STRB r4, [sp]
1103		LDRSB r4, [sp]
1104		MOV r5, #'a'
1105		CMP r4, r5
1106		MOVEQ r4, #1
1107		MOVNE r4, #0
1108		CMP r4, #0
1109		BEQ L40
1110		SUB sp, sp, #1
1111		LDR r4, [sp, #4]
1112		STR r4, [sp, #-4]!
1113		BL f_handleMenuInsert
1114		ADD sp, sp, #4
1115		MOV r4, r0
1116		STRB r4, [sp]
1117		ADD sp, sp, #1
1118		B L41
1119	L40:
1120		LDRSB r4, [sp]
1121		MOV r5, #'b'
1122		CMP r4, r5
1123		MOVEQ r4, #1
1124		MOVNE r4, #0
1125		CMP r4, #0
1126		BEQ L42
1127		SUB sp, sp, #1
1128		LDR r4, [sp, #4]
1129		STR r4, [sp, #-4]!
1130		BL f_handleMenuFind
1131		ADD sp, sp, #4
1132		MOV r4, r0
1133		STRB r4, [sp]
1134		ADD sp, sp, #1
1135		B L43
1136	L42:
1137		LDRSB r4, [sp]
1138		MOV r5, #'c'
1139		CMP r4, r5
1140		MOVEQ r4, #1
1141		MOVNE r4, #0
1142		CMP r4, #0
1143		BEQ L44
1144		SUB sp, sp, #1
1145		LDR r4, [sp, #4]
1146		STR r4, [sp, #-4]!
1147		BL f_handleMenuCount
1148		ADD sp, sp, #4
1149		MOV r4, r0
1150		STRB r4, [sp]
1151		ADD sp, sp, #1
1152		B L45
1153	L44:
1154		LDRSB r4, [sp]
1155		MOV r5, #'d'
1156		CMP r4, r5
1157		MOVEQ r4, #1
1158		MOVNE r4, #0
1159		CMP r4, #0
1160		BEQ L46
1161		SUB sp, sp, #1
1162		LDR r4, [sp, #4]
1163		STR r4, [sp, #-4]!
1164		BL f_handleMenuPrint
1165		ADD sp, sp, #4
1166		MOV r4, r0
1167		STRB r4, [sp]
1168		ADD sp, sp, #1
1169		B L47
1170	L46:
1171		LDRSB r4, [sp]
1172		MOV r5, #'e'
1173		CMP r4, r5
1174		MOVEQ r4, #1
1175		MOVNE r4, #0
1176		CMP r4, #0
1177		BEQ L48
1178		SUB sp, sp, #1
1179		LDR r4, [sp, #4]
1180		STR r4, [sp, #-4]!
1181		BL f_handleMenuRemove
1182		ADD sp, sp, #4
1183		MOV r4, r0
1184		STRB r4, [sp]
1185		ADD sp, sp, #1
1186		B L49
1187	L48:
1188		LDRSB r4, [sp]
1189		MOV r5, #'f'
1190		CMP r4, r5
1191		MOVEQ r4, #1
1192		MOVNE r4, #0
1193		CMP r4, #0
1194		BEQ L50
1195		SUB sp, sp, #1
1196		LDR r4, [sp, #4]
1197		STR r4, [sp, #-4]!
1198		BL f_handleMenuRemoveAll
1199		ADD sp, sp, #4
1200		MOV r4, r0
1201		STRB r4, [sp]
1202		ADD sp, sp, #1
1203		B L51
1204	L50:
1205		LDRSB r4, [sp]
1206		MOV r5, #'g'
1207		CMP r4, r5
1208		MOVEQ r4, #1
1209		MOVNE r4, #0
1210		CMP r4, #0
1211		BEQ L52
1212		LDR r4, =msg_34
1213		MOV r0, r4
1214		BL p_print_string
1215		BL p_print_ln
1216		MOV r4, #0
1217		STRB r4, [sp, #1]
1218		B L53
1219	L52:
1220		LDR r4, =msg_35
1221		MOV r0, r4
1222		BL p_print_string
1223		LDRSB r4, [sp]
1224		MOV r0, r4
1225		BL putchar
1226		LDR r4, =msg_36
1227		MOV r0, r4
1228		BL p_print_string
1229		BL p_print_ln
1230		LDR r4, =-1
1231		MOV r0, r4
1232		BL exit
1233	L53:
1234	L51:
1235	L49:
1236	L47:
1237	L45:
1238	L43:
1239	L41:
1240		ADD sp, sp, #1
1241	L38:
1242		LDRSB r4, [sp]
1243		CMP r4, #1
1244		BEQ L39
1245		ADD sp, sp, #6
1246		LDR r0, =0
1247		POP {pc}
1248		.ltorg
1249	p_check_array_bounds:
1250		PUSH {lr}
1251		CMP r0, #0
1252		LDRLT r0, =msg_37
1253		BLLT p_throw_runtime_error
1254		LDR r1, [r1]
1255		CMP r0, r1
1256		LDRCS r0, =msg_38
1257		BLCS p_throw_runtime_error
1258		POP {pc}
1259	p_throw_overflow_error:
1260		LDR r0, =msg_39
1261		BL p_throw_runtime_error
1262	p_check_null_pointer:
1263		PUSH {lr}
1264		CMP r0, #0
1265		LDREQ r0, =msg_40
1266		BLEQ p_throw_runtime_error
1267		POP {pc}
1268	p_free_pair:
1269		PUSH {lr}
1270		CMP r0, #0
1271		LDREQ r0, =msg_41
1272		BEQ p_throw_runtime_error
1273		PUSH {r0}
1274		LDR r0, [r0]
1275		BL free
1276		LDR r0, [sp]
1277		LDR r0, [r0, #4]
1278		BL free
1279		POP {r0}
1280		BL free
1281		POP {pc}
1282	p_print_string:
1283		PUSH {lr}
1284		LDR r1, [r0]
1285		ADD r2, r0, #4
1286		LDR r0, =msg_42
1287		ADD r0, r0, #4
1288		BL printf
1289		MOV r0, #0
1290		BL fflush
1291		POP {pc}
1292	p_print_ln:
1293		PUSH {lr}
1294		LDR r0, =msg_43
1295		ADD r0, r0, #4
1296		BL puts
1297		MOV r0, #0
1298		BL fflush
1299		POP {pc}
1300	p_check_divide_by_zero:
1301		PUSH {lr}
1302		CMP r1, #0
1303		LDREQ r0, =msg_44
1304		BLEQ p_throw_runtime_error
1305		POP {pc}
1306	p_print_int:
1307		PUSH {lr}
1308		MOV r1, r0
1309		LDR r0, =msg_45
1310		ADD r0, r0, #4
1311		BL printf
1312		MOV r0, #0
1313		BL fflush
1314		POP {pc}
1315	p_read_char:
1316		PUSH {lr}
1317		MOV r1, r0
1318		LDR r0, =msg_46
1319		ADD r0, r0, #4
1320		BL scanf
1321		POP {pc}
1322	p_read_int:
1323		PUSH {lr}
1324		MOV r1, r0
1325		LDR r0, =msg_47
1326		ADD r0, r0, #4
1327		BL scanf
1328		POP {pc}
1329	p_throw_runtime_error:
1330		BL p_print_string
1331		MOV r0, #-1
1332		BL exit
1333	
===========================================================
-- Finished

