valid/advanced/ticTacToe.wacc
calling the reference compiler on valid/advanced/ticTacToe.wacc
-- Test: ticTacToe.wacc

-- Uploaded file: 
---------------------------------------------------------------
# This is a program that allows a human to play Tic Tac Toe with a smart AI. 
# The AI is implemented using minimax approach. The AI is a perfect one, meaning 
# that it will never lose and if there is an immediate win, it will play that. 
#
# It takes quite a long time to initialise and free up the memory for the AI. 
# All observed bugs have been fixed.
#
# In this program, we are very often need the memory from the heap. 
# But because a pair is the only data type allocated on the heap, 
# we have to use it. We very often need a data structure to store 
# a set of 3 objects. We will use the format below. 
# 
#  root --\
#         |
#        \|/
#          
#  +----------+----------+
#  |          |          |
#  |  front   | object 3 |
#  |          |          |
#  +----------+----------+ 
#       |
#       |
#      \|/
#          
#  +----------+----------+
#  |          |          |
#  | object 1 | object 2 |
#  |          |          |
#  +----------+----------+ 
# 
# We call this structure Pair4Three.

# Program:

begin
	
	############################## Interface Functions ######################
	
	# Print greeting message and ask the player to choose their symbol.
	# Return either 'x' or 'o'. 'x' plays first.
	char chooseSymbol() is
		println "========= Tic Tac Toe ================" ;
		println "=  Because we know you want to win   =" ;
		println "======================================" ;
		println "=                                    =" ;
		println "= Who would you like to be?          =" ;
		println "=   x  (play first)                  =" ;
		println "=   o  (play second)                 =" ;
		println "=   q  (quit)                        =" ;
		println "=                                    =" ;
		println "======================================" ;
		
		char chosen = '\0' ;
		while chosen == '\0' do 
			print "Which symbol you would like to choose: " ;
			char c = '\0' ;
			read c ;
			if c == 'x' || c == 'X' then
				chosen = 'x'
			else
				if c == 'o' || c == 'O' then
					chosen = 'o'
				else
					if c == 'q' || c == 'Q' then
						println "Goodbye safety." ;
						exit 0
					else
						print "Invalid symbol: " ;
						println c ;
						println "Please try again."
					fi 
				fi
			fi
		done ;
		print "You have chosen: " ;
		println chosen ;
		return chosen
	end
	
	# Print the board out to the screen.
	bool printBoard(pair(pair, pair) board) is
		pair(pair, pair) front = fst board ;
		pair(pair, char) row1 = fst front ;
		pair(pair, char) row2 = snd front ;
		pair(pair, char) row3 = snd board ;
	
    println " 1 2 3";
    print "1";	
		bool _ = call printRow(row1) ;
		println " -+-+-" ;
    print "2";	
		_ = call printRow(row2) ;
		println " -+-+-" ;
    print "3";	
		_ = call printRow(row3) ;
		println "";
		return true 
	end
	
	# Print a row with a newline to the screen.
	bool printRow(pair(pair, char) row) is
		pair(char, char) front = fst row ;
		
		char cell1 = fst front ;
		char cell2 = snd front ;
		char cell3 = snd row ;
		
		bool _ = call printCell(cell1) ;
		print '|' ;
		_ = call printCell(cell2) ;
		print '|' ;
		_ = call printCell(cell3) ;
		println "" ;
		return true
	end 
	
	# Print a given cell. Print an empty space if it is empty. Return true.
	bool printCell(char cell) is
		if cell == '\0' then
			print ' ' 
		else
			print cell 
		fi ;
		return true
	end
	
	# Ask for a move from the human player. The valid move is then stored in the given move array. 
	# The row number is stored at move[0], the column number is stored at move[1]. Return true.
	bool askForAMoveHuman(pair(pair, pair) board, int[] move) is
		bool success = false ;
		int row = 0 ;
		int column = 0 ;
			
		while !success do
			println "What is your next move?" ;
			print " row (1-3): " ;
			read row ;
			print " column (1-3): " ;
			read column ; 
			success = call validateMove(board, row, column) ;
			
			if success then
				println "" ; # Just print out an empty line
				move[0] = row ;
				move[1] = column ;
				return true
			else
				println "Your move is invalid. Please try again."
			fi			
		done ; 
		# Should not reach here
		return true
	end
	
	# Validate that the give move is valid. Returns true iff it is valid.
	bool validateMove(pair(pair, pair) board, int moveRow, int moveColumn) is
		if 1 <= moveRow && moveRow <= 3 && 1 <= moveColumn && moveColumn <= 3 then
			char sym = call symbolAt(board, moveRow, moveColumn) ;
			# Make sure that the cell is empty
			return sym == '\0'
		else
			return false
		fi
	end
	
	# Print out to the screen about a recent move maid by the AI. Return true.
	bool notifyMoveHuman(pair(pair, pair) board, char currentTurn, char playerSymbol, int moveRow, int moveColumn) is
		print "The AI played at row " ;
		print moveRow ;
		print " column " ;
		println moveColumn ;
		return true
	end
	
	############################### AI Functions #########################################
	
	# Initialise an AI data.
	pair(pair, pair) initAI(char aiSymbol) is
		
		pair(char, pair) info = newpair(aiSymbol, null) ; # Don't know yet how to use the second element.
 		pair(pair, int) stateTree = call generateAllPossibleStates(aiSymbol) ;
		int value = call setValuesForAllStates(stateTree, aiSymbol, 'x') ;
		
		pair(pair, pair) aiData = newpair(info, stateTree) ;
		return aiData
	end
	
	# Generate the whole tree of all states. Then return the tree.
	pair(pair, int) generateAllPossibleStates(char aiSymbol) is
		pair(pair, pair) board = call allocateNewBoard() ;
		pair(pair, int) rootState = call convertFromBoardToState(board) ;
		rootState = call generateNextStates(rootState, 'x') ;
		return rootState
	end
	
	# Convert from a board to a state.
	# A state consists of 3 objects: the board, the pointers to the next states, and the value for this state (int).
	# Therefore, we use the Pair4Three structure.
	pair(pair, int) convertFromBoardToState(pair(pair, pair) board) is
		
		pair(pair, pair) pointers = call generateEmptyPointerBoard() ;
		pair(pair, pair) front = newpair(board, pointers) ;
		pair(pair, int) state = newpair(front, 0) ; # The initial value of 0 will be replaced.
		
		return state
	end
	
	# Allocate memory for the pointers to the next state.
	# It looks like a board, but contains pointers (pairs) instead of chars.
	pair(pair, pair) generateEmptyPointerBoard() is
		
		pair(pair, pair) row1 = call generateEmptyPointerRow() ;
		pair(pair, pair) row2 = call generateEmptyPointerRow() ;
		pair(pair, pair) row3 = call generateEmptyPointerRow() ;
		
		pair(pair, pair) front = newpair(row1, row2) ;
		pair(pair, pair) root = newpair(front, row3) ;
		return root
		
	end
	
	# Allocate memory for the 3 pointers to the next state of a row.
	pair(pair, pair) generateEmptyPointerRow() is
		pair(pair, pair) front = newpair(null, null) ;
		pair(pair, pair) root = newpair(front, null) ;
		return root 
	end
	
	# Generate next states recursively. Returns the state.
	pair(pair, int) generateNextStates(pair(pair, int) state, char currentPlayer) is
		pair(pair, pair) front = fst state ;
		
		pair(pair, pair) board = fst front ;
		pair(pair, pair) pointers = snd front ;
		
		char previousPlayer = call oppositeSymbol(currentPlayer) ;
		
		bool won = call hasWon(board, previousPlayer) ;
		
		if won then
			# The game ends. The winner is known.
			return state 
		else
			bool _ = call generateNextStatesBoard(board, pointers, currentPlayer) ;
			return state
		fi
		
	end
	
	# Generate Just the next states for every possible point on the board. Update the pointers accordingly. Return true.
	bool generateNextStatesBoard(pair(pair, pair) board, pair(pair, pair) pointers, char currentPlayer) is
		pair(pair, pair) front = fst board ;
		
		pair(pair, char) row1 = fst front ;
		pair(pair, char) row2 = snd front ;
		pair(pair, char) row3 = snd board ;
		
		pair(pair, pair) frontP = fst pointers ;
		
		pair(pair, pair) row1P = fst frontP ;
		pair(pair, pair) row2P = snd frontP ;
		pair(pair, pair) row3P = snd pointers ;
		
		bool _ = call generateNextStatesRow(board, row1, row1P, currentPlayer, 1) ;
		_ = call generateNextStatesRow(board, row2, row2P, currentPlayer, 2) ;
		_ = call generateNextStatesRow(board, row3, row3P, currentPlayer, 3) ;
		
		return true
	end
	
	# Generate Just the next states for every possible point on the row. Update the pointers accordingly. Return true.
	bool generateNextStatesRow(pair(pair, pair) board, pair(pair, char) row, pair(pair, pair) pointerRow, char currentPlayer, int rowNumber) is
		pair(char, char) front = fst row ;
		
		char cell1 = fst front ;
		char cell2 = snd front ;
		char cell3 = snd row ;
		
		pair(pair, pair) frontP = fst pointerRow ;
		
		fst frontP = call generateNextStatesCell(board, cell1, currentPlayer, rowNumber, 1) ;
		snd frontP = call generateNextStatesCell(board, cell2, currentPlayer, rowNumber, 2) ;
		snd pointerRow = call generateNextStatesCell(board, cell3, currentPlayer, rowNumber, 3) ;
		
		return true
	end
	
	# Generate Just the next states for the cell on the board. Returns the pointer to the next state.
	pair(pair, int) generateNextStatesCell(pair(pair, pair) board, char cell, char currentPlayer, int rowNumber, int columnNumber) is
		if cell == '\0' then
			# If the cell is empty, generate the next state.
			pair(pair, pair) board2 = call cloneBoard(board) ;
			bool _ = call placeMove(board2, currentPlayer, rowNumber, columnNumber) ;
			pair(pair, int) state = call convertFromBoardToState(board2) ;
			char nextPlayer = call oppositeSymbol(currentPlayer) ;
			
			# Generate next states recursively and return it out.
			state = call generateNextStates(state, nextPlayer) ;
			return state
		else
			# If the cell is not empty, return null.
			return null
		fi
	end
	
	# Clone board.
	pair(pair, pair) cloneBoard(pair(pair, pair) board) is
		pair(pair, pair) board2 = call allocateNewBoard() ; 
		bool _ = call copyBoard(board, board2) ;
		return board2 
	end
	
	# Copy the content of one board to another. Return true.
	bool copyBoard(pair(pair, pair) from, pair(pair, pair) to) is
		pair(pair, pair) frontFrom = fst from ;
		pair(pair, char) row1From = fst frontFrom ;
		pair(pair, char) row2From = snd frontFrom ;
		pair(pair, char) row3From = snd from ;
		
		pair(pair, pair) frontTo = fst to ;
		pair(pair, char) row1To = fst frontTo ;
		pair(pair, char) row2To = snd frontTo ;
		pair(pair, char) row3To = snd to ;
				
		bool _ = call copyRow(row1From, row1To) ;		
		_ = call copyRow(row2From, row2To) ;
		_ = call copyRow(row3From, row3To) ;
				
		return true
	end
	
	# Copy from one board row to another. Return true.
	bool copyRow(pair(pair, char) from, pair(pair, char) to) is
		pair(char, char) frontFrom = fst from ;
		pair(char, char) frontTo = fst to ;
		
		fst frontTo = fst frontFrom ;
		snd frontTo = snd frontFrom ;
		snd to = snd from ;
		return true
	end
	
	# Calculate the value of how good each state is using Minimax approach. 
	# If AI wins, value = 100.
	# If AI lose, value = -100.
	# If Stalemate, value = 0.
	# Otherwise, combine the values from the next states.
	# If this state is null, then return -101 if it is a max state, 101 if it is a min state (thus those values will not be picked).
	# Return the value.
	int setValuesForAllStates(pair(pair, int) state, char aiSymbol, char currentPlayer) is
		int outValue = 0 ;
		if state == null then
			# The current state is impossible to reach.
			# Assign a value that will not be picked in the future.
			if currentPlayer == aiSymbol then
				# Later on, we will pick the lowest value (min). So we set this value high so that it will not be picked.
				outValue = 101
			else
				# Later on, we will pick the highest value (max). So we set this value low so that it will not be picked.
				outValue = -101
			fi
		else 
		
			pair(pair, pair) front = fst state ;
			
			pair(pair, pair) board = fst front ;
			pair(pair, pair) pointers = snd front ;
			
			char anotherPlayer = call oppositeSymbol(currentPlayer) ;
			
			# The current player is about to play. So if another player has won it already, the current player cannot play it.
			bool won = call hasWon(board, anotherPlayer) ;
		
			if won then
				if anotherPlayer == aiSymbol then
					outValue = 100 # We won
				else
					outValue = -100 # We lost
				fi 
			else
				bool hasEmptyCell = call containEmptyCell(board) ;
				if hasEmptyCell then
					# If can do next move, calculate the value from the next states.
					outValue = call calculateValuesFromNextStates(pointers, aiSymbol, anotherPlayer) ;
					
					# In order for the AI to choose the winning move immediately, we have to reduce the value for those not winning yet.
					# So if the next move has value 100, we set the value of this move 90.
					if outValue == 100 then
						outValue = 90 
					else
						skip
					fi
				else
					# Otherwise, it is a stalemate.
					outValue = 0 
				fi 
			fi ;
			snd state = outValue
		fi ;
		return outValue
	end
	
	# Calculate the values for each next state, then combine them to get the value of this state. Return the value.
	int calculateValuesFromNextStates(pair(pair, pair) pointers, char aiSymbol, char playerOfNextState) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ;
		pair(pair, pair) row3 = snd pointers ;
		
		int value1 = call calculateValuesFromNextStatesRow(row1, aiSymbol, playerOfNextState) ;
		int value2 = call calculateValuesFromNextStatesRow(row2, aiSymbol, playerOfNextState) ;
		int value3 = call calculateValuesFromNextStatesRow(row3, aiSymbol, playerOfNextState) ;
		
		int out = call combineValue(aiSymbol, playerOfNextState, value1, value2, value3) ;
		return out
	end
	
	# Calculate the values for each next state in a row, then combine them to get the value of this row. Return the value.
	int calculateValuesFromNextStatesRow(pair(pair, pair) rowPointers, char aiSymbol, char playerOfNextState) is
		pair(pair, pair) front = fst rowPointers ;
		
		pair(pair, int) state1 = fst front ;
		pair(pair, int) state2 = snd front ; 
		pair(pair, int) state3 = snd rowPointers ;
		
		int value1 = call setValuesForAllStates(state1, aiSymbol, playerOfNextState) ;
		int value2 = call setValuesForAllStates(state2, aiSymbol, playerOfNextState) ;
		int value3 = call setValuesForAllStates(state3, aiSymbol, playerOfNextState) ;
		
		int out = call combineValue(aiSymbol, playerOfNextState, value1, value2, value3) ;
		return out
	end
	
	int combineValue(char aiSymbol, char playerOfNextState, int value1, int value2, int value3) is
		int out = 0 ;
		if aiSymbol == playerOfNextState then
			# We move next so the human moves now. Pick the lowest value.
			out = call min3(value1, value2, value3)
		else
			# Human moves next so we move now. Pick the highest value.
			out = call max3(value1, value2, value3)
		fi ;
		return out
	end
	
	# Find the minimum of the three.
	int min3(int a, int b, int c) is
		if a < b then
			if a < c then
				return a 
			else 
				return c
			fi
		else
			if b < c then
				return b
			else 
				return c
			fi
		fi
	end
	
	# Find the maximum of the three.
	int max3(int a, int b, int c) is
		if a > b then
			if a > c then
				return a 
			else 
				return c
			fi
		else
			if b > c then
				return b
			else 
				return c
			fi
		fi
	end
	
	# Destroy all memory used by the AI. Return true.
	bool destroyAI(pair(pair, pair) aiData) is
		
		pair(char, pair) info = fst aiData ;
 		pair(pair, int) stateTree = snd aiData ;

		bool _ = call deleteStateTreeRecursively(stateTree) ;
		free info ;
		free aiData ;
		return true
	end
	
	# Ask the AI for a new move. Return true.
	bool askForAMoveAI(pair(pair, pair) board, char currentTurn, char playerSymbol, pair(pair, pair) aiData, int[] move) is
		
		pair(char, pair) info = fst aiData ;
 		pair(pair, int) stateTree = snd aiData ;
		
		pair(pair, pair) front = fst stateTree ;
		
		pair(pair, pair) pointers = snd front ;
		int stateValue = snd stateTree ;
		
		bool _ = call findTheBestMove(pointers, stateValue, move) ;		
		
		println "AI is cleaning up its memory..." ;
		# Update the state tree by using the new move.
		snd aiData = call deleteAllOtherChildren(pointers, move[0], move[1]) ;
	
		_ = call deleteThisStateOnly(stateTree) ;
		return true
	end
	
	# Given the pointers to all next states, pick the first one with the given stateValue and store the move in the the given array.
	# Return true. 
	bool findTheBestMove(pair(pair, pair) pointers, int stateValue, int[] move) is

		# We have a hack by changing the state value to 90 if the next state is 100. 
		# So if we have a state value of 90, look for the one with 100 first.
		# If found, use it. Otherwise, look for the one with 90.
		if stateValue == 90 then
			bool found = call findMoveWithGivenValue(pointers, 100, move) ;
			if found then
				return true
			else
				skip
			fi
		else
			skip
		fi ;
		
		# Normal case. Or when cannot find the child with 100.
		bool found = call findMoveWithGivenValue(pointers, stateValue, move) ;
		if found then
			return true
		else
			# Should not happen. Cannot find such move.
			println "Internal Error: cannot find the next move for the AI" ;
			exit -1
		fi
		
	end

	# Given the pointers to all next states, pick the first one with the given stateValue and store the move in the the given array.
	# Return true in this case. Otherwise, the move array is untouched and return false. 
	bool findMoveWithGivenValue(pair(pair, pair) pointers, int stateValue, int[] move) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ; 
		pair(pair, pair) row3 = snd pointers ;
		
		bool find = call findMoveWithGivenValueRow(row1, stateValue, move) ;
		if find then
			move[0] = 1
		else
			find = call findMoveWithGivenValueRow(row2, stateValue, move) ;
			if find then
				move[0] = 2
			else
				find = call findMoveWithGivenValueRow(row3, stateValue, move) ;
				if find then
					move[0] = 3
				else
					# Not found, return false.
					return false
				fi
			fi
		fi ;
		return true
	end
	
	# Given a row of pointers, pick the first one with the given stateValue and store in move[1], return true if such child state is found. Otherwise, return false and move[1] is untouched.
	bool findMoveWithGivenValueRow(pair(pair, pair) rowPointers, int stateValue, int[] move) is
		
		pair(pair, pair) front = fst rowPointers ;
		
		pair(pair, int) cell1 = fst front ;
		pair(pair, int) cell2 = snd front ;
		pair(pair, int) cell3 = snd rowPointers ;
		
		bool find = call hasGivenStateValue(cell1, stateValue) ;
		if find then
			move[1] = 1
		else
			find = call hasGivenStateValue(cell2, stateValue) ;
			if find then
				move[1] = 2 
			else 
				find = call hasGivenStateValue(cell3, stateValue) ;
				if find then
					move[1] = 3
				else 
					return false
				fi
			fi
		fi ;
		return true
	end
	
	# Given a state, an a state value. Returns true iff the state has the given state value.
	bool hasGivenStateValue(pair(pair, int) state, int stateValue) is
		if state == null then
			return false
		else
			int actual = snd state ;
			return actual == stateValue
		fi
	end
	
	# Notify a move made by a human player to the AI. Return true.
	bool notifyMoveAI(pair(pair, pair) board, char currentTurn, char playerSymbol, pair(pair, pair) aiData, int moveRow, int moveColumn) is
		
		#pair(char, pair) info = fst aiData ; #unused
		pair(pair, int) stateTree = snd aiData ;
		
		pair(pair, pair) front = fst stateTree ;
		
		#pair(pair, pair) board = fst front ; #unused
		pair(pair, pair) pointers = snd front ;
		
		println "AI is cleaning up its memory..." ;
		
		# Set new state tree, remove all other children created by making other moves.
		snd aiData = call deleteAllOtherChildren(pointers, moveRow, moveColumn) ;
		bool _ = call deleteThisStateOnly(stateTree) ;
		return true
	end
	
	# Delete all decendent states apart from those made by moving a given move. Return the child state of that given move.
	pair(pair, int) deleteAllOtherChildren(pair(pair, pair) pointers, int moveRow, int moveColumn) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ;
		pair(pair, pair) row3 = snd pointers ;

		# Find which row to keep or which rows to delete.
		pair(pair, pair) toKeepRow = null;
		pair(pair, pair) toDeleteRow1 = null;
		pair(pair, pair) toDeleteRow2 = null;
		
		if moveRow == 1 then
			toKeepRow = row1 ; 
			toDeleteRow1 = row2 ; 
			toDeleteRow2 = row3
		else 
			toDeleteRow1 = row1 ;
			if moveRow == 2 then
				toKeepRow = row2 ; 
				toDeleteRow2 = row3
			else
				# moveRow == 3
				toKeepRow = row3 ; 
				toDeleteRow2 = row2
			fi
		fi ;
		
		pair(pair, int) out = call deleteAllOtherChildrenRow(toKeepRow, moveColumn) ;
		bool _ = call deleteChildrenStateRecursivelyRow(toDeleteRow1) ;
		_ = call deleteChildrenStateRecursivelyRow(toDeleteRow2) ;
		
		return out
	end
	
	pair(pair, int) deleteAllOtherChildrenRow(pair(pair, pair) rowPointers, int moveColumn) is
		pair(pair, pair) front = fst rowPointers ;
		
		pair(pair, int) cell1 = fst front ;
		pair(pair, int) cell2 = snd front ;
		pair(pair, int) cell3 = snd rowPointers ;

		# Find which cell to keep or which cells to delete.
		pair(pair, int) toKeepCell = null;
		pair(pair, int) toDeleteCell1 = null;
		pair(pair, int) toDeleteCell2 = null;
		
		if moveColumn == 1 then
			toKeepCell = cell1 ; 
			toDeleteCell1 = cell2 ; 
			toDeleteCell2 = cell3
		else 
			toDeleteCell1 = cell1 ;
			if moveColumn == 2 then
				toKeepCell = cell2 ; 
				toDeleteCell2 = cell3
			else
				# moveColumn == 3
				toKeepCell = cell3 ; 
				toDeleteCell2 = cell2
			fi
		fi ;
		
		bool _ = call deleteStateTreeRecursively(toDeleteCell1) ;
		_ = call deleteStateTreeRecursively(toDeleteCell2) ;
		
		return toKeepCell
	end
	
	# Deallocate a given state and all its decendents.
	bool deleteStateTreeRecursively(pair(pair, int) stateTree) is
		if stateTree == null then
			return true 
		else
			pair(pair, pair) front = fst stateTree ;
			
			pair(pair, pair) board = fst front ;
			pair(pair, pair) pointers = snd front ;
			
			bool _ = call deleteChildrenStateRecursively(pointers) ;
			_ = call deleteThisStateOnly(stateTree) ;
			return true
		fi		
	end
	
	# Given a state tree, deallocate the board, the pointers and the other pairs of this state only. The childrens are preserved. Return true.
	bool deleteThisStateOnly(pair(pair, int) stateTree) is	
		pair(pair, pair) front = fst stateTree ;
		
		pair(pair, pair) board = fst front ;
		pair(pair, pair) pointers = snd front ;

		bool _ = call freeBoard(board) ;
		_ = call freePointers(pointers) ;
		free front ;
		free stateTree ;
		return true
	end
	
	bool freePointers(pair(pair, pair) pointers) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ;
		pair(pair, pair) row3 = snd pointers ;
		
		bool _ = call freePointersRow(row1) ;
		_ = call freePointersRow(row2) ;
		_ = call freePointersRow(row3) ;
		
		free front ;
		free pointers ;
		return true
	end
	
	bool freePointersRow(pair(pair, pair) rowPointers) is
		pair(pair, pair) front = fst rowPointers ;
		
		free front ;
		free rowPointers ;
		return true
	end
	
	# Deallocate all decendent states.
	bool deleteChildrenStateRecursively(pair(pair, pair) pointers) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ;
		pair(pair, pair) row3 = snd pointers ;
		
		bool _ = call deleteChildrenStateRecursivelyRow(row1) ;
		_ = call deleteChildrenStateRecursivelyRow(row2) ;
		_ = call deleteChildrenStateRecursivelyRow(row3) ;
		
		return true
	end
	
	# Deallocate all decendent states given a row of pointers.
	bool deleteChildrenStateRecursivelyRow(pair(pair, pair) rowPointers) is
		pair(pair, pair) front = fst rowPointers ;
		pair(pair, int) cell1 = fst front ;
		pair(pair, int) cell2 = snd front ;
		pair(pair, int) cell3 = snd rowPointers ;
		
		bool _ = call deleteStateTreeRecursively(cell1) ;
		_ = call deleteStateTreeRecursively(cell2) ;
		_ = call deleteStateTreeRecursively(cell3) ;
		
		return true
	end
	
	############################### Game Engine Functions ##################################
	
	# Ask for a move from the current player. The valid move is stored in the move array. Return true.
	bool askForAMove(pair(pair, pair) board, char currentTurn, char playerSymbol, pair(pair, pair) aiData, int[] move) is
		if currentTurn == playerSymbol then
			bool _ = call askForAMoveHuman(board, move)
		else 
			bool _ = call askForAMoveAI(board, currentTurn, playerSymbol, aiData, move)
		fi ;
		return true
	end
	
	# Place the given move of the currentTurn in the board. Return true.
	bool placeMove(pair(pair, pair) board, char currentTurn, int moveRow, int moveColumn) is
		
		# Find the target row.
		pair(pair, char) targetRow = null ;
		if moveRow <= 2 then
			pair(pair, pair) front = fst board ;
			if moveRow == 1 then
				targetRow = fst front
			else
				# moveRow == 2
				targetRow = snd front
			fi
		else
			# moveRow == 3
			targetRow = snd board
		fi ;
		
		# Set the target cell
		if moveColumn <= 2 then
			pair(char, char) front = fst targetRow ;
			if moveColumn == 1 then
				fst front = currentTurn
			else
				# moveColumn == 2
				snd front = currentTurn
			fi
		else
			# moveColumn == 3
			snd targetRow = currentTurn
		fi ;
		return true
		
	end
	
	# Notify the opponent about a move of another party. Return true.
	bool notifyMove(pair(pair, pair) board, char currentTurn, char playerSymbol, pair(pair, pair) aiData, int moveRow, int moveColumn) is
		if currentTurn == playerSymbol then
			bool _ = call notifyMoveAI(board, currentTurn, playerSymbol, aiData, moveRow, moveColumn)
		else 
			bool _ = call notifyMoveHuman(board, currentTurn, playerSymbol, moveRow, moveColumn)
		fi ;
		return true
	end
	
	# Given either 'x' or 'o', returns another one.
	char oppositeSymbol(char symbol) is
		if symbol == 'x' then
			return 'o' 
		else
			if symbol == 'o' then
				return 'x'
			else
				# Should not happen!
				println "Internal Error: symbol given is neither \'x\' or \'o\'" ;
				exit -1 
			fi 
		fi
	end
	
	# row = 1, 2 or 3
	# column = 1, 2 or 3
	char symbolAt(pair(pair, pair) board, int row, int column) is
	
		# Find the target row.
		pair(pair, char) targetRow = null ;
		if row <= 2 then
			pair(pair, pair) front = fst board ;
			if row == 1 then
				targetRow = fst front
			else
				# row == 2
				targetRow = snd front
			fi
		else
			# row == 3
			targetRow = snd board
		fi ;
		
		# Now find the target cell.
		char targetCell = '\0' ;
		if column <= 2 then
			pair(char, char) front = fst targetRow ;
			if column == 1 then 
				targetCell = fst front 
			else
				# column == 2
				targetCell = snd front
			fi
		else
			# column == 3
			targetCell = snd targetRow
		fi ;
			
		return targetCell	
	end
	
	# Return true if there is at least one empty cell where the next player can place a move. Otherwise, return false (game ends).
	bool containEmptyCell(pair(pair, pair) board) is
		pair(pair, pair) front = fst board ;
		
		pair(pair, char) row1 = fst front ;
		pair(pair, char) row2 = snd front ;
		pair(pair, char) row3 = snd board ;
		
		bool row1ContainEmpty = call containEmptyCellRow(row1) ;
		bool row2ContainEmpty = call containEmptyCellRow(row2) ;
		bool row3ContainEmpty = call containEmptyCellRow(row3) ;
		
		return row1ContainEmpty || row2ContainEmpty || row3ContainEmpty
	end
	
	bool containEmptyCellRow(pair(pair, char) row) is
		pair(char, char) front = fst row ;
		
		char cell1 = fst front ;
		char cell2 = snd front ;
		char cell3 = snd row ;
		
		return cell1 == '\0' || cell2 == '\0' || cell3 == '\0'
	end
	
	# Find if the candidate symbol ('x' or 'o') has won the game.
	# Returns true if and only if it has won. 
	bool hasWon(pair(pair, pair) board, char candidate) is
		char c11 = call symbolAt(board, 1, 1) ;
		char c12 = call symbolAt(board, 1, 2) ;
		char c13 = call symbolAt(board, 1, 3) ;
		char c21 = call symbolAt(board, 2, 1) ;
		char c22 = call symbolAt(board, 2, 2) ;
		char c23 = call symbolAt(board, 2, 3) ;
		char c31 = call symbolAt(board, 3, 1) ;
		char c32 = call symbolAt(board, 3, 2) ;
		char c33 = call symbolAt(board, 3, 3) ;
		
		return 
			# Row win
			c11 == candidate && c12 == candidate && c13 == candidate ||
			c21 == candidate && c22 == candidate && c23 == candidate ||
			c31 == candidate && c32 == candidate && c33 == candidate ||
			 
			# Column win
			c11 == candidate && c21 == candidate && c31 == candidate ||
			c12 == candidate && c22 == candidate && c32 == candidate ||
			c13 == candidate && c23 == candidate && c33 == candidate ||
			
			# Diagonal win
			c11 == candidate && c22 == candidate && c33 == candidate ||
			c13 == candidate && c22 == candidate && c31 == candidate
	end
	
	# Allocate a new board. 
	# We use a Pair4Three structure to store pointers to the 3 rows.
	pair(pair, pair) allocateNewBoard() is
		pair(pair, char) row1 = call allocateNewRow() ;
		pair(pair, char) row2 = call allocateNewRow() ;
		pair(pair, char) row3 = call allocateNewRow() ;
		
		pair(pair, pair) front = newpair(row1, row2) ;
		pair(pair, pair) root = newpair(front, row3) ;
		return root
	end
	
	# Allocate a row of the board. 
	# A row is represented by a Pair4Three structure.
	# The default value in each cell is '\0'.
	pair(pair, char) allocateNewRow() is
		pair(char, char) front = newpair('\0', '\0') ;
		pair(pair, char) root = newpair(front, '\0') ;
		return root
	end

	# Free a memory used to store the whole board.
	# Return true.
	bool freeBoard(pair(pair, pair) board) is
		pair(pair, pair) front = fst board ;
		
		pair(pair, char) row1 = fst front ;
		pair(pair, char) row2 = snd front ;
		pair(pair, char) row3 = snd board ;
		
		bool _ = call freeRow(row1) ;
		_ = call freeRow(row2) ;
		_ = call freeRow(row3) ;
		
		free front ;
		free board ;
		return true
	end
	
	# Free the memory used for a row. Return true.
	bool freeRow(pair(pair, char) row) is
		pair(char, char) front = fst row ;
		free front ;
		free row ;
		return true
	end
	
	# For debugging purpose.
	bool printAiData(pair(pair, pair) aiData) is
		
		pair(char, pair) info = fst aiData ;
		pair(pair, int) stateTree = snd aiData ;
		
		bool _ = call printStateTreeRecursively(stateTree) ;
		exit 0
	end
	
	bool printStateTreeRecursively(pair(pair, int) stateTree) is
		if stateTree == null then
			return true 
		else 
			pair(pair, pair) front = fst stateTree ;
			
			pair(pair, pair) board = fst front ;
			pair(pair, pair) pointers = snd front ;
			int value = snd stateTree ;
			
			# Print the value
			print 'v' ;
			print '=' ;
			println value ;
			
			bool _ = call printBoard(board) ;
			_ = call printChildrenStateTree(pointers) ;
			
			println 'p' ;
			return true
		fi
	end
	
	bool printChildrenStateTree(pair(pair, pair) pointers) is
		pair(pair, pair) front = fst pointers ;
		
		pair(pair, pair) row1 = fst front ;
		pair(pair, pair) row2 = snd front ;
		pair(pair, pair) row3 = snd pointers ;
		
		bool _ = call printChildrenStateTreeRow(row1) ;
		_ = call printChildrenStateTreeRow(row2) ;
		_ = call printChildrenStateTreeRow(row3) ;
		return true
	end
	
	bool printChildrenStateTreeRow(pair(pair, pair) rowPointers) is
		pair(pair, pair) front = fst rowPointers ;
		
		pair(pair, int) cell1 = fst front ;
		pair(pair, int) cell2 = snd front ;
		pair(pair, int) cell3 = snd rowPointers ;
		
		bool _ = call printStateTreeRecursively(cell1) ;
		_ = call printStateTreeRecursively(cell2) ;
		_ = call printStateTreeRecursively(cell3) ;
		
		return true
	end
	
	############################## Main Function ############################
	
	char playerSymbol = call chooseSymbol() ;
	char aiSymbol = call oppositeSymbol(playerSymbol) ;
	char currentTurn = 'x' ;
	
	pair(pair, pair) board = call allocateNewBoard() ;
	
	println "Initialising AI. Please wait, this may take a few minutes." ;
	pair(pair, pair) aiData = call initAI(aiSymbol) ;
	
	int turnCount = 0 ;
	char winner = '\0' ;
	
	bool _ = call printBoard(board) ;
	
	while winner == '\0' && turnCount < 9 do
		int[] move = [0, 0] ;
		_ = call askForAMove(board, currentTurn, playerSymbol, aiData, move) ;
		_ = call placeMove(board, currentTurn, move[0], move[1]) ;
		_ = call notifyMove(board, currentTurn, playerSymbol, aiData, move[0], move[1]) ;
		_ = call printBoard(board) ;
		bool won = call hasWon(board, currentTurn) ;
		if won then
			winner = currentTurn
		else 
			skip
		fi ;
		
		# Progress to the next turn
		currentTurn = call oppositeSymbol(currentTurn) ;
		turnCount = turnCount + 1
	done ;
	
	_ = call freeBoard(board) ;
	_ = call destroyAI(aiData) ;
	
	if winner != '\0' then
		print winner ;
		println " has won!" 
	else 
		println "Stalemate!" 
	fi
end
---------------------------------------------------------------

-- Compiler Output:
-- Compiling...
-- Printing Assembly...
ticTacToe.s contents are:
===========================================================
0	.data
1	
2	msg_0:
3		.word 38
4		.ascii	"========= Tic Tac Toe ================"
5	msg_1:
6		.word 38
7		.ascii	"=  Because we know you want to win   ="
8	msg_2:
9		.word 38
10		.ascii	"======================================"
11	msg_3:
12		.word 38
13		.ascii	"=                                    ="
14	msg_4:
15		.word 38
16		.ascii	"= Who would you like to be?          ="
17	msg_5:
18		.word 38
19		.ascii	"=   x  (play first)                  ="
20	msg_6:
21		.word 38
22		.ascii	"=   o  (play second)                 ="
23	msg_7:
24		.word 38
25		.ascii	"=   q  (quit)                        ="
26	msg_8:
27		.word 38
28		.ascii	"=                                    ="
29	msg_9:
30		.word 38
31		.ascii	"======================================"
32	msg_10:
33		.word 39
34		.ascii	"Which symbol you would like to choose: "
35	msg_11:
36		.word 15
37		.ascii	"Goodbye safety."
38	msg_12:
39		.word 16
40		.ascii	"Invalid symbol: "
41	msg_13:
42		.word 17
43		.ascii	"Please try again."
44	msg_14:
45		.word 17
46		.ascii	"You have chosen: "
47	msg_15:
48		.word 6
49		.ascii	" 1 2 3"
50	msg_16:
51		.word 1
52		.ascii	"1"
53	msg_17:
54		.word 6
55		.ascii	" -+-+-"
56	msg_18:
57		.word 1
58		.ascii	"2"
59	msg_19:
60		.word 6
61		.ascii	" -+-+-"
62	msg_20:
63		.word 1
64		.ascii	"3"
65	msg_21:
66		.word 0
67		.ascii	""
68	msg_22:
69		.word 0
70		.ascii	""
71	msg_23:
72		.word 23
73		.ascii	"What is your next move?"
74	msg_24:
75		.word 12
76		.ascii	" row (1-3): "
77	msg_25:
78		.word 15
79		.ascii	" column (1-3): "
80	msg_26:
81		.word 0
82		.ascii	""
83	msg_27:
84		.word 39
85		.ascii	"Your move is invalid. Please try again."
86	msg_28:
87		.word 21
88		.ascii	"The AI played at row "
89	msg_29:
90		.word 8
91		.ascii	" column "
92	msg_30:
93		.word 31
94		.ascii	"AI is cleaning up its memory..."
95	msg_31:
96		.word 52
97		.ascii	"Internal Error: cannot find the next move for the AI"
98	msg_32:
99		.word 31
100		.ascii	"AI is cleaning up its memory..."
101	msg_33:
102		.word 50
103		.ascii	"Internal Error: symbol given is neither \'x\' or \'o\'"
104	msg_34:
105		.word 58
106		.ascii	"Initialising AI. Please wait, this may take a few minutes."
107	msg_35:
108		.word 9
109		.ascii	" has won!"
110	msg_36:
111		.word 10
112		.ascii	"Stalemate!"
113	msg_37:
114		.word 5
115		.ascii	"%.*s\0"
116	msg_38:
117		.word 1
118		.ascii	"\0"
119	msg_39:
120		.word 4
121		.ascii	" %c\0"
122	msg_40:
123		.word 50
124		.ascii	"NullReferenceError: dereference a null reference\n\0"
125	msg_41:
126		.word 3
127		.ascii	"%d\0"
128	msg_42:
129		.word 44
130		.ascii	"ArrayIndexOutOfBoundsError: negative index\n\0"
131	msg_43:
132		.word 45
133		.ascii	"ArrayIndexOutOfBoundsError: index too large\n\0"
134	msg_44:
135		.word 3
136		.ascii	"%d\0"
137	msg_45:
138		.word 50
139		.ascii	"NullReferenceError: dereference a null reference\n\0"
140	msg_46:
141		.word 82
142		.ascii	"OverflowError: the result is too small/large to store in a 4-byte signed-integer.\n"
143	
144	.text
145	
146	.global main
147	f_chooseSymbol:
148		PUSH {lr}
149		SUB sp, sp, #1
150		LDR r4, =msg_0
151		MOV r0, r4
152		BL p_print_string
153		BL p_print_ln
154		LDR r4, =msg_1
155		MOV r0, r4
156		BL p_print_string
157		BL p_print_ln
158		LDR r4, =msg_2
159		MOV r0, r4
160		BL p_print_string
161		BL p_print_ln
162		LDR r4, =msg_3
163		MOV r0, r4
164		BL p_print_string
165		BL p_print_ln
166		LDR r4, =msg_4
167		MOV r0, r4
168		BL p_print_string
169		BL p_print_ln
170		LDR r4, =msg_5
171		MOV r0, r4
172		BL p_print_string
173		BL p_print_ln
174		LDR r4, =msg_6
175		MOV r0, r4
176		BL p_print_string
177		BL p_print_ln
178		LDR r4, =msg_7
179		MOV r0, r4
180		BL p_print_string
181		BL p_print_ln
182		LDR r4, =msg_8
183		MOV r0, r4
184		BL p_print_string
185		BL p_print_ln
186		LDR r4, =msg_9
187		MOV r0, r4
188		BL p_print_string
189		BL p_print_ln
190		MOV r4, #0
191		STRB r4, [sp]
192		B L0
193	L1:
194		SUB sp, sp, #1
195		LDR r4, =msg_10
196		MOV r0, r4
197		BL p_print_string
198		MOV r4, #0
199		STRB r4, [sp]
200		ADD r4, sp, #0
201		MOV r0, r4
202		BL p_read_char
203		LDRSB r4, [sp]
204		MOV r5, #'x'
205		CMP r4, r5
206		MOVEQ r4, #1
207		MOVNE r4, #0
208		LDRSB r5, [sp]
209		MOV r6, #'X'
210		CMP r5, r6
211		MOVEQ r5, #1
212		MOVNE r5, #0
213		ORR r4, r4, r5
214		CMP r4, #0
215		BEQ L2
216		MOV r4, #'x'
217		STRB r4, [sp, #1]
218		B L3
219	L2:
220		LDRSB r4, [sp]
221		MOV r5, #'o'
222		CMP r4, r5
223		MOVEQ r4, #1
224		MOVNE r4, #0
225		LDRSB r5, [sp]
226		MOV r6, #'O'
227		CMP r5, r6
228		MOVEQ r5, #1
229		MOVNE r5, #0
230		ORR r4, r4, r5
231		CMP r4, #0
232		BEQ L4
233		MOV r4, #'o'
234		STRB r4, [sp, #1]
235		B L5
236	L4:
237		LDRSB r4, [sp]
238		MOV r5, #'q'
239		CMP r4, r5
240		MOVEQ r4, #1
241		MOVNE r4, #0
242		LDRSB r5, [sp]
243		MOV r6, #'Q'
244		CMP r5, r6
245		MOVEQ r5, #1
246		MOVNE r5, #0
247		ORR r4, r4, r5
248		CMP r4, #0
249		BEQ L6
250		LDR r4, =msg_11
251		MOV r0, r4
252		BL p_print_string
253		BL p_print_ln
254		LDR r4, =0
255		MOV r0, r4
256		BL exit
257		B L7
258	L6:
259		LDR r4, =msg_12
260		MOV r0, r4
261		BL p_print_string
262		LDRSB r4, [sp]
263		MOV r0, r4
264		BL putchar
265		BL p_print_ln
266		LDR r4, =msg_13
267		MOV r0, r4
268		BL p_print_string
269		BL p_print_ln
270	L7:
271	L5:
272	L3:
273		ADD sp, sp, #1
274	L0:
275		LDRSB r4, [sp]
276		MOV r5, #0
277		CMP r4, r5
278		MOVEQ r4, #1
279		MOVNE r4, #0
280		CMP r4, #1
281		BEQ L1
282		LDR r4, =msg_14
283		MOV r0, r4
284		BL p_print_string
285		LDRSB r4, [sp]
286		MOV r0, r4
287		BL putchar
288		BL p_print_ln
289		LDRSB r4, [sp]
290		MOV r0, r4
291		ADD sp, sp, #1
292		POP {pc}
293		.ltorg
294	f_printBoard:
295		PUSH {lr}
296		SUB sp, sp, #17
297		LDR r4, [sp, #21]
298		MOV r0, r4
299		BL p_check_null_pointer
300		LDR r4, [r4]
301		LDR r4, [r4]
302		STR r4, [sp, #13]
303		LDR r4, [sp, #13]
304		MOV r0, r4
305		BL p_check_null_pointer
306		LDR r4, [r4]
307		LDR r4, [r4]
308		STR r4, [sp, #9]
309		LDR r4, [sp, #13]
310		MOV r0, r4
311		BL p_check_null_pointer
312		LDR r4, [r4, #4]
313		LDR r4, [r4]
314		STR r4, [sp, #5]
315		LDR r4, [sp, #21]
316		MOV r0, r4
317		BL p_check_null_pointer
318		LDR r4, [r4, #4]
319		LDR r4, [r4]
320		STR r4, [sp, #1]
321		LDR r4, =msg_15
322		MOV r0, r4
323		BL p_print_string
324		BL p_print_ln
325		LDR r4, =msg_16
326		MOV r0, r4
327		BL p_print_string
328		LDR r4, [sp, #9]
329		STR r4, [sp, #-4]!
330		BL f_printRow
331		ADD sp, sp, #4
332		MOV r4, r0
333		STRB r4, [sp]
334		LDR r4, =msg_17
335		MOV r0, r4
336		BL p_print_string
337		BL p_print_ln
338		LDR r4, =msg_18
339		MOV r0, r4
340		BL p_print_string
341		LDR r4, [sp, #5]
342		STR r4, [sp, #-4]!
343		BL f_printRow
344		ADD sp, sp, #4
345		MOV r4, r0
346		STRB r4, [sp]
347		LDR r4, =msg_19
348		MOV r0, r4
349		BL p_print_string
350		BL p_print_ln
351		LDR r4, =msg_20
352		MOV r0, r4
353		BL p_print_string
354		LDR r4, [sp, #1]
355		STR r4, [sp, #-4]!
356		BL f_printRow
357		ADD sp, sp, #4
358		MOV r4, r0
359		STRB r4, [sp]
360		LDR r4, =msg_21
361		MOV r0, r4
362		BL p_print_string
363		BL p_print_ln
364		MOV r4, #1
365		MOV r0, r4
366		ADD sp, sp, #17
367		POP {pc}
368		.ltorg
369	f_printRow:
370		PUSH {lr}
371		SUB sp, sp, #8
372		LDR r4, [sp, #12]
373		MOV r0, r4
374		BL p_check_null_pointer
375		LDR r4, [r4]
376		LDR r4, [r4]
377		STR r4, [sp, #4]
378		LDR r4, [sp, #4]
379		MOV r0, r4
380		BL p_check_null_pointer
381		LDR r4, [r4]
382		LDRSB r4, [r4]
383		STRB r4, [sp, #3]
384		LDR r4, [sp, #4]
385		MOV r0, r4
386		BL p_check_null_pointer
387		LDR r4, [r4, #4]
388		LDRSB r4, [r4]
389		STRB r4, [sp, #2]
390		LDR r4, [sp, #12]
391		MOV r0, r4
392		BL p_check_null_pointer
393		LDR r4, [r4, #4]
394		LDRSB r4, [r4]
395		STRB r4, [sp, #1]
396		LDRSB r4, [sp, #3]
397		STRB r4, [sp, #-1]!
398		BL f_printCell
399		ADD sp, sp, #1
400		MOV r4, r0
401		STRB r4, [sp]
402		MOV r4, #'|'
403		MOV r0, r4
404		BL putchar
405		LDRSB r4, [sp, #2]
406		STRB r4, [sp, #-1]!
407		BL f_printCell
408		ADD sp, sp, #1
409		MOV r4, r0
410		STRB r4, [sp]
411		MOV r4, #'|'
412		MOV r0, r4
413		BL putchar
414		LDRSB r4, [sp, #1]
415		STRB r4, [sp, #-1]!
416		BL f_printCell
417		ADD sp, sp, #1
418		MOV r4, r0
419		STRB r4, [sp]
420		LDR r4, =msg_22
421		MOV r0, r4
422		BL p_print_string
423		BL p_print_ln
424		MOV r4, #1
425		MOV r0, r4
426		ADD sp, sp, #8
427		POP {pc}
428		.ltorg
429	f_printCell:
430		PUSH {lr}
431		LDRSB r4, [sp, #4]
432		MOV r5, #0
433		CMP r4, r5
434		MOVEQ r4, #1
435		MOVNE r4, #0
436		CMP r4, #0
437		BEQ L8
438		MOV r4, #' '
439		MOV r0, r4
440		BL putchar
441		B L9
442	L8:
443		LDRSB r4, [sp, #4]
444		MOV r0, r4
445		BL putchar
446	L9:
447		MOV r4, #1
448		MOV r0, r4
449		POP {pc}
450		.ltorg
451	f_askForAMoveHuman:
452		PUSH {lr}
453		SUB sp, sp, #9
454		MOV r4, #0
455		STRB r4, [sp, #8]
456		LDR r4, =0
457		STR r4, [sp, #4]
458		LDR r4, =0
459		STR r4, [sp]
460		B L10
461	L11:
462		LDR r4, =msg_23
463		MOV r0, r4
464		BL p_print_string
465		BL p_print_ln
466		LDR r4, =msg_24
467		MOV r0, r4
468		BL p_print_string
469		ADD r4, sp, #4
470		MOV r0, r4
471		BL p_read_int
472		LDR r4, =msg_25
473		MOV r0, r4
474		BL p_print_string
475		ADD r4, sp, #0
476		MOV r0, r4
477		BL p_read_int
478		LDR r4, [sp]
479		STR r4, [sp, #-4]!
480		LDR r4, [sp, #8]
481		STR r4, [sp, #-4]!
482		LDR r4, [sp, #21]
483		STR r4, [sp, #-4]!
484		BL f_validateMove
485		ADD sp, sp, #12
486		MOV r4, r0
487		STRB r4, [sp, #8]
488		LDRSB r4, [sp, #8]
489		CMP r4, #0
490		BEQ L12
491		LDR r4, =msg_26
492		MOV r0, r4
493		BL p_print_string
494		BL p_print_ln
495		LDR r4, [sp, #4]
496		ADD r5, sp, #17
497		LDR r6, =0
498		LDR r5, [r5]
499		MOV r0, r6
500		MOV r1, r5
501		BL p_check_array_bounds
502		ADD r5, r5, #4
503		ADD r5, r5, r6, LSL #2
504		STR r4, [r5]
505		LDR r4, [sp]
506		ADD r6, sp, #17
507		LDR r7, =1
508		LDR r6, [r6]
509		MOV r0, r7
510		MOV r1, r6
511		BL p_check_array_bounds
512		ADD r6, r6, #4
513		ADD r6, r6, r7, LSL #2
514		STR r4, [r6]
515		MOV r4, #1
516		MOV r0, r4
517		ADD sp, sp, #9
518		B L13
519	L12:
520		LDR r4, =msg_27
521		MOV r0, r4
522		BL p_print_string
523		BL p_print_ln
524	L13:
525	L10:
526		LDRSB r4, [sp, #8]
527		EOR r4, r4, #1
528		CMP r4, #1
529		BEQ L11
530		MOV r4, #1
531		MOV r0, r4
532		ADD sp, sp, #9
533		POP {pc}
534		.ltorg
535	f_validateMove:
536		PUSH {lr}
537		LDR r4, =1
538		LDR r5, [sp, #8]
539		CMP r4, r5
540		MOVLE r4, #1
541		MOVGT r4, #0
542		LDR r5, [sp, #8]
543		LDR r6, =3
544		CMP r5, r6
545		MOVLE r5, #1
546		MOVGT r5, #0
547		AND r4, r4, r5
548		LDR r5, =1
549		LDR r6, [sp, #12]
550		CMP r5, r6
551		MOVLE r5, #1
552		MOVGT r5, #0
553		AND r4, r4, r5
554		LDR r5, [sp, #12]
555		LDR r6, =3
556		CMP r5, r6
557		MOVLE r5, #1
558		MOVGT r5, #0
559		AND r4, r4, r5
560		CMP r4, #0
561		BEQ L14
562		SUB sp, sp, #1
563		LDR r4, [sp, #13]
564		STR r4, [sp, #-4]!
565		LDR r4, [sp, #13]
566		STR r4, [sp, #-4]!
567		LDR r4, [sp, #13]
568		STR r4, [sp, #-4]!
569		BL f_symbolAt
570		ADD sp, sp, #12
571		MOV r4, r0
572		STRB r4, [sp]
573		LDRSB r4, [sp]
574		MOV r5, #0
575		CMP r4, r5
576		MOVEQ r4, #1
577		MOVNE r4, #0
578		MOV r0, r4
579		ADD sp, sp, #1
580		ADD sp, sp, #1
581		B L15
582	L14:
583		MOV r4, #0
584		MOV r0, r4
585	L15:
586		POP {pc}
587		.ltorg
588	f_notifyMoveHuman:
589		PUSH {lr}
590		LDR r4, =msg_28
591		MOV r0, r4
592		BL p_print_string
593		LDR r4, [sp, #10]
594		MOV r0, r4
595		BL p_print_int
596		LDR r4, =msg_29
597		MOV r0, r4
598		BL p_print_string
599		LDR r4, [sp, #14]
600		MOV r0, r4
601		BL p_print_int
602		BL p_print_ln
603		MOV r4, #1
604		MOV r0, r4
605		POP {pc}
606		.ltorg
607	f_initAI:
608		PUSH {lr}
609		SUB sp, sp, #16
610		LDR r0, =8
611		BL malloc
612		MOV r4, r0
613		LDRSB r5, [sp, #20]
614		LDR r0, =1
615		BL malloc
616		STRB r5, [r0]
617		STR r0, [r4]
618		LDR r5, =0
619		LDR r0, =4
620		BL malloc
621		STR r5, [r0]
622		STR r0, [r4, #4]
623		STR r4, [sp, #12]
624		LDRSB r4, [sp, #20]
625		STRB r4, [sp, #-1]!
626		BL f_generateAllPossibleStates
627		ADD sp, sp, #1
628		MOV r4, r0
629		STR r4, [sp, #8]
630		MOV r4, #'x'
631		STRB r4, [sp, #-1]!
632		LDRSB r4, [sp, #21]
633		STRB r4, [sp, #-1]!
634		LDR r4, [sp, #10]
635		STR r4, [sp, #-4]!
636		BL f_setValuesForAllStates
637		ADD sp, sp, #6
638		MOV r4, r0
639		STR r4, [sp, #4]
640		LDR r0, =8
641		BL malloc
642		MOV r4, r0
643		LDR r5, [sp, #12]
644		LDR r0, =4
645		BL malloc
646		STR r5, [r0]
647		STR r0, [r4]
648		LDR r5, [sp, #8]
649		LDR r0, =4
650		BL malloc
651		STR r5, [r0]
652		STR r0, [r4, #4]
653		STR r4, [sp]
654		LDR r4, [sp]
655		MOV r0, r4
656		ADD sp, sp, #16
657		POP {pc}
658		.ltorg
659	f_generateAllPossibleStates:
660		PUSH {lr}
661		SUB sp, sp, #8
662		BL f_allocateNewBoard
663		MOV r4, r0
664		STR r4, [sp, #4]
665		LDR r4, [sp, #4]
666		STR r4, [sp, #-4]!
667		BL f_convertFromBoardToState
668		ADD sp, sp, #4
669		MOV r4, r0
670		STR r4, [sp]
671		MOV r4, #'x'
672		STRB r4, [sp, #-1]!
673		LDR r4, [sp, #1]
674		STR r4, [sp, #-4]!
675		BL f_generateNextStates
676		ADD sp, sp, #5
677		MOV r4, r0
678		STR r4, [sp]
679		LDR r4, [sp]
680		MOV r0, r4
681		ADD sp, sp, #8
682		POP {pc}
683		.ltorg
684	f_convertFromBoardToState:
685		PUSH {lr}
686		SUB sp, sp, #12
687		BL f_generateEmptyPointerBoard
688		MOV r4, r0
689		STR r4, [sp, #8]
690		LDR r0, =8
691		BL malloc
692		MOV r4, r0
693		LDR r5, [sp, #16]
694		LDR r0, =4
695		BL malloc
696		STR r5, [r0]
697		STR r0, [r4]
698		LDR r5, [sp, #8]
699		LDR r0, =4
700		BL malloc
701		STR r5, [r0]
702		STR r0, [r4, #4]
703		STR r4, [sp, #4]
704		LDR r0, =8
705		BL malloc
706		MOV r4, r0
707		LDR r5, [sp, #4]
708		LDR r0, =4
709		BL malloc
710		STR r5, [r0]
711		STR r0, [r4]
712		LDR r5, =0
713		LDR r0, =4
714		BL malloc
715		STR r5, [r0]
716		STR r0, [r4, #4]
717		STR r4, [sp]
718		LDR r4, [sp]
719		MOV r0, r4
720		ADD sp, sp, #12
721		POP {pc}
722		.ltorg
723	f_generateEmptyPointerBoard:
724		PUSH {lr}
725		SUB sp, sp, #20
726		BL f_generateEmptyPointerRow
727		MOV r4, r0
728		STR r4, [sp, #16]
729		BL f_generateEmptyPointerRow
730		MOV r4, r0
731		STR r4, [sp, #12]
732		BL f_generateEmptyPointerRow
733		MOV r4, r0
734		STR r4, [sp, #8]
735		LDR r0, =8
736		BL malloc
737		MOV r4, r0
738		LDR r5, [sp, #16]
739		LDR r0, =4
740		BL malloc
741		STR r5, [r0]
742		STR r0, [r4]
743		LDR r5, [sp, #12]
744		LDR r0, =4
745		BL malloc
746		STR r5, [r0]
747		STR r0, [r4, #4]
748		STR r4, [sp, #4]
749		LDR r0, =8
750		BL malloc
751		MOV r4, r0
752		LDR r5, [sp, #4]
753		LDR r0, =4
754		BL malloc
755		STR r5, [r0]
756		STR r0, [r4]
757		LDR r5, [sp, #8]
758		LDR r0, =4
759		BL malloc
760		STR r5, [r0]
761		STR r0, [r4, #4]
762		STR r4, [sp]
763		LDR r4, [sp]
764		MOV r0, r4
765		ADD sp, sp, #20
766		POP {pc}
767		.ltorg
768	f_generateEmptyPointerRow:
769		PUSH {lr}
770		SUB sp, sp, #8
771		LDR r0, =8
772		BL malloc
773		MOV r4, r0
774		LDR r5, =0
775		LDR r0, =4
776		BL malloc
777		STR r5, [r0]
778		STR r0, [r4]
779		LDR r5, =0
780		LDR r0, =4
781		BL malloc
782		STR r5, [r0]
783		STR r0, [r4, #4]
784		STR r4, [sp, #4]
785		LDR r0, =8
786		BL malloc
787		MOV r4, r0
788		LDR r5, [sp, #4]
789		LDR r0, =4
790		BL malloc
791		STR r5, [r0]
792		STR r0, [r4]
793		LDR r5, =0
794		LDR r0, =4
795		BL malloc
796		STR r5, [r0]
797		STR r0, [r4, #4]
798		STR r4, [sp]
799		LDR r4, [sp]
800		MOV r0, r4
801		ADD sp, sp, #8
802		POP {pc}
803		.ltorg
804	f_generateNextStates:
805		PUSH {lr}
806		SUB sp, sp, #14
807		LDR r4, [sp, #18]
808		MOV r0, r4
809		BL p_check_null_pointer
810		LDR r4, [r4]
811		LDR r4, [r4]
812		STR r4, [sp, #10]
813		LDR r4, [sp, #10]
814		MOV r0, r4
815		BL p_check_null_pointer
816		LDR r4, [r4]
817		LDR r4, [r4]
818		STR r4, [sp, #6]
819		LDR r4, [sp, #10]
820		MOV r0, r4
821		BL p_check_null_pointer
822		LDR r4, [r4, #4]
823		LDR r4, [r4]
824		STR r4, [sp, #2]
825		LDRSB r4, [sp, #22]
826		STRB r4, [sp, #-1]!
827		BL f_oppositeSymbol
828		ADD sp, sp, #1
829		MOV r4, r0
830		STRB r4, [sp, #1]
831		LDRSB r4, [sp, #1]
832		STRB r4, [sp, #-1]!
833		LDR r4, [sp, #7]
834		STR r4, [sp, #-4]!
835		BL f_hasWon
836		ADD sp, sp, #5
837		MOV r4, r0
838		STRB r4, [sp]
839		LDRSB r4, [sp]
840		CMP r4, #0
841		BEQ L16
842		LDR r4, [sp, #18]
843		MOV r0, r4
844		ADD sp, sp, #14
845		B L17
846	L16:
847		SUB sp, sp, #1
848		LDRSB r4, [sp, #23]
849		STRB r4, [sp, #-1]!
850		LDR r4, [sp, #4]
851		STR r4, [sp, #-4]!
852		LDR r4, [sp, #12]
853		STR r4, [sp, #-4]!
854		BL f_generateNextStatesBoard
855		ADD sp, sp, #9
856		MOV r4, r0
857		STRB r4, [sp]
858		LDR r4, [sp, #19]
859		MOV r0, r4
860		ADD sp, sp, #15
861		ADD sp, sp, #1
862	L17:
863		POP {pc}
864		.ltorg
865	f_generateNextStatesBoard:
866		PUSH {lr}
867		SUB sp, sp, #33
868		LDR r4, [sp, #37]
869		MOV r0, r4
870		BL p_check_null_pointer
871		LDR r4, [r4]
872		LDR r4, [r4]
873		STR r4, [sp, #29]
874		LDR r4, [sp, #29]
875		MOV r0, r4
876		BL p_check_null_pointer
877		LDR r4, [r4]
878		LDR r4, [r4]
879		STR r4, [sp, #25]
880		LDR r4, [sp, #29]
881		MOV r0, r4
882		BL p_check_null_pointer
883		LDR r4, [r4, #4]
884		LDR r4, [r4]
885		STR r4, [sp, #21]
886		LDR r4, [sp, #37]
887		MOV r0, r4
888		BL p_check_null_pointer
889		LDR r4, [r4, #4]
890		LDR r4, [r4]
891		STR r4, [sp, #17]
892		LDR r4, [sp, #41]
893		MOV r0, r4
894		BL p_check_null_pointer
895		LDR r4, [r4]
896		LDR r4, [r4]
897		STR r4, [sp, #13]
898		LDR r4, [sp, #13]
899		MOV r0, r4
900		BL p_check_null_pointer
901		LDR r4, [r4]
902		LDR r4, [r4]
903		STR r4, [sp, #9]
904		LDR r4, [sp, #13]
905		MOV r0, r4
906		BL p_check_null_pointer
907		LDR r4, [r4, #4]
908		LDR r4, [r4]
909		STR r4, [sp, #5]
910		LDR r4, [sp, #41]
911		MOV r0, r4
912		BL p_check_null_pointer
913		LDR r4, [r4, #4]
914		LDR r4, [r4]
915		STR r4, [sp, #1]
916		LDR r4, =1
917		STR r4, [sp, #-4]!
918		LDRSB r4, [sp, #49]
919		STRB r4, [sp, #-1]!
920		LDR r4, [sp, #14]
921		STR r4, [sp, #-4]!
922		LDR r4, [sp, #34]
923		STR r4, [sp, #-4]!
924		LDR r4, [sp, #50]
925		STR r4, [sp, #-4]!
926		BL f_generateNextStatesRow
927		ADD sp, sp, #17
928		MOV r4, r0
929		STRB r4, [sp]
930		LDR r4, =2
931		STR r4, [sp, #-4]!
932		LDRSB r4, [sp, #49]
933		STRB r4, [sp, #-1]!
934		LDR r4, [sp, #10]
935		STR r4, [sp, #-4]!
936		LDR r4, [sp, #30]
937		STR r4, [sp, #-4]!
938		LDR r4, [sp, #50]
939		STR r4, [sp, #-4]!
940		BL f_generateNextStatesRow
941		ADD sp, sp, #17
942		MOV r4, r0
943		STRB r4, [sp]
944		LDR r4, =3
945		STR r4, [sp, #-4]!
946		LDRSB r4, [sp, #49]
947		STRB r4, [sp, #-1]!
948		LDR r4, [sp, #6]
949		STR r4, [sp, #-4]!
950		LDR r4, [sp, #26]
951		STR r4, [sp, #-4]!
952		LDR r4, [sp, #50]
953		STR r4, [sp, #-4]!
954		BL f_generateNextStatesRow
955		ADD sp, sp, #17
956		MOV r4, r0
957		STRB r4, [sp]
958		MOV r4, #1
959		MOV r0, r4
960		ADD sp, sp, #33
961		POP {pc}
962		.ltorg
963	f_generateNextStatesRow:
964		PUSH {lr}
965		SUB sp, sp, #11
966		LDR r4, [sp, #19]
967		MOV r0, r4
968		BL p_check_null_pointer
969		LDR r4, [r4]
970		LDR r4, [r4]
971		STR r4, [sp, #7]
972		LDR r4, [sp, #7]
973		MOV r0, r4
974		BL p_check_null_pointer
975		LDR r4, [r4]
976		LDRSB r4, [r4]
977		STRB r4, [sp, #6]
978		LDR r4, [sp, #7]
979		MOV r0, r4
980		BL p_check_null_pointer
981		LDR r4, [r4, #4]
982		LDRSB r4, [r4]
983		STRB r4, [sp, #5]
984		LDR r4, [sp, #19]
985		MOV r0, r4
986		BL p_check_null_pointer
987		LDR r4, [r4, #4]
988		LDRSB r4, [r4]
989		STRB r4, [sp, #4]
990		LDR r4, [sp, #23]
991		MOV r0, r4
992		BL p_check_null_pointer
993		LDR r4, [r4]
994		LDR r4, [r4]
995		STR r4, [sp]
996		LDR r4, =1
997		STR r4, [sp, #-4]!
998		LDR r4, [sp, #32]
999		STR r4, [sp, #-4]!
1000		LDRSB r4, [sp, #35]
1001		STRB r4, [sp, #-1]!
1002		LDRSB r4, [sp, #15]
1003		STRB r4, [sp, #-1]!
1004		LDR r4, [sp, #25]
1005		STR r4, [sp, #-4]!
1006		BL f_generateNextStatesCell
1007		ADD sp, sp, #14
1008		MOV r4, r0
1009		LDR r5, [sp]
1010		MOV r0, r5
1011		BL p_check_null_pointer
1012		LDR r5, [r5]
1013		STR r4, [r5]
1014		LDR r4, =2
1015		STR r4, [sp, #-4]!
1016		LDR r4, [sp, #32]
1017		STR r4, [sp, #-4]!
1018		LDRSB r4, [sp, #35]
1019		STRB r4, [sp, #-1]!
1020		LDRSB r4, [sp, #14]
1021		STRB r4, [sp, #-1]!
1022		LDR r4, [sp, #25]
1023		STR r4, [sp, #-4]!
1024		BL f_generateNextStatesCell
1025		ADD sp, sp, #14
1026		MOV r4, r0
1027		LDR r5, [sp]
1028		MOV r0, r5
1029		BL p_check_null_pointer
1030		LDR r5, [r5, #4]
1031		STR r4, [r5]
1032		LDR r4, =3
1033		STR r4, [sp, #-4]!
1034		LDR r4, [sp, #32]
1035		STR r4, [sp, #-4]!
1036		LDRSB r4, [sp, #35]
1037		STRB r4, [sp, #-1]!
1038		LDRSB r4, [sp, #13]
1039		STRB r4, [sp, #-1]!
1040		LDR r4, [sp, #25]
1041		STR r4, [sp, #-4]!
1042		BL f_generateNextStatesCell
1043		ADD sp, sp, #14
1044		MOV r4, r0
1045		LDR r5, [sp, #23]
1046		MOV r0, r5
1047		BL p_check_null_pointer
1048		LDR r5, [r5, #4]
1049		STR r4, [r5]
1050		MOV r4, #1
1051		MOV r0, r4
1052		ADD sp, sp, #11
1053		POP {pc}
1054		.ltorg
1055	f_generateNextStatesCell:
1056		PUSH {lr}
1057		LDRSB r4, [sp, #8]
1058		MOV r5, #0
1059		CMP r4, r5
1060		MOVEQ r4, #1
1061		MOVNE r4, #0
1062		CMP r4, #0
1063		BEQ L18
1064		SUB sp, sp, #10
1065		LDR r4, [sp, #14]
1066		STR r4, [sp, #-4]!
1067		BL f_cloneBoard
1068		ADD sp, sp, #4
1069		MOV r4, r0
1070		STR r4, [sp, #6]
1071		LDR r4, [sp, #24]
1072		STR r4, [sp, #-4]!
1073		LDR r4, [sp, #24]
1074		STR r4, [sp, #-4]!
1075		LDRSB r4, [sp, #27]
1076		STRB r4, [sp, #-1]!
1077		LDR r4, [sp, #15]
1078		STR r4, [sp, #-4]!
1079		BL f_placeMove
1080		ADD sp, sp, #13
1081		MOV r4, r0
1082		STRB r4, [sp, #5]
1083		LDR r4, [sp, #6]
1084		STR r4, [sp, #-4]!
1085		BL f_convertFromBoardToState
1086		ADD sp, sp, #4
1087		MOV r4, r0
1088		STR r4, [sp, #1]
1089		LDRSB r4, [sp, #19]
1090		STRB r4, [sp, #-1]!
1091		BL f_oppositeSymbol
1092		ADD sp, sp, #1
1093		MOV r4, r0
1094		STRB r4, [sp]
1095		LDRSB r4, [sp]
1096		STRB r4, [sp, #-1]!
1097		LDR r4, [sp, #2]
1098		STR r4, [sp, #-4]!
1099		BL f_generateNextStates
1100		ADD sp, sp, #5
1101		MOV r4, r0
1102		STR r4, [sp, #1]
1103		LDR r4, [sp, #1]
1104		MOV r0, r4
1105		ADD sp, sp, #10
1106		ADD sp, sp, #10
1107		B L19
1108	L18:
1109		LDR r4, =0
1110		MOV r0, r4
1111	L19:
1112		POP {pc}
1113		.ltorg
1114	f_cloneBoard:
1115		PUSH {lr}
1116		SUB sp, sp, #5
1117		BL f_allocateNewBoard
1118		MOV r4, r0
1119		STR r4, [sp, #1]
1120		LDR r4, [sp, #1]
1121		STR r4, [sp, #-4]!
1122		LDR r4, [sp, #13]
1123		STR r4, [sp, #-4]!
1124		BL f_copyBoard
1125		ADD sp, sp, #8
1126		MOV r4, r0
1127		STRB r4, [sp]
1128		LDR r4, [sp, #1]
1129		MOV r0, r4
1130		ADD sp, sp, #5
1131		POP {pc}
1132		.ltorg
1133	f_copyBoard:
1134		PUSH {lr}
1135		SUB sp, sp, #33
1136		LDR r4, [sp, #37]
1137		MOV r0, r4
1138		BL p_check_null_pointer
1139		LDR r4, [r4]
1140		LDR r4, [r4]
1141		STR r4, [sp, #29]
1142		LDR r4, [sp, #29]
1143		MOV r0, r4
1144		BL p_check_null_pointer
1145		LDR r4, [r4]
1146		LDR r4, [r4]
1147		STR r4, [sp, #25]
1148		LDR r4, [sp, #29]
1149		MOV r0, r4
1150		BL p_check_null_pointer
1151		LDR r4, [r4, #4]
1152		LDR r4, [r4]
1153		STR r4, [sp, #21]
1154		LDR r4, [sp, #37]
1155		MOV r0, r4
1156		BL p_check_null_pointer
1157		LDR r4, [r4, #4]
1158		LDR r4, [r4]
1159		STR r4, [sp, #17]
1160		LDR r4, [sp, #41]
1161		MOV r0, r4
1162		BL p_check_null_pointer
1163		LDR r4, [r4]
1164		LDR r4, [r4]
1165		STR r4, [sp, #13]
1166		LDR r4, [sp, #13]
1167		MOV r0, r4
1168		BL p_check_null_pointer
1169		LDR r4, [r4]
1170		LDR r4, [r4]
1171		STR r4, [sp, #9]
1172		LDR r4, [sp, #13]
1173		MOV r0, r4
1174		BL p_check_null_pointer
1175		LDR r4, [r4, #4]
1176		LDR r4, [r4]
1177		STR r4, [sp, #5]
1178		LDR r4, [sp, #41]
1179		MOV r0, r4
1180		BL p_check_null_pointer
1181		LDR r4, [r4, #4]
1182		LDR r4, [r4]
1183		STR r4, [sp, #1]
1184		LDR r4, [sp, #9]
1185		STR r4, [sp, #-4]!
1186		LDR r4, [sp, #29]
1187		STR r4, [sp, #-4]!
1188		BL f_copyRow
1189		ADD sp, sp, #8
1190		MOV r4, r0
1191		STRB r4, [sp]
1192		LDR r4, [sp, #5]
1193		STR r4, [sp, #-4]!
1194		LDR r4, [sp, #25]
1195		STR r4, [sp, #-4]!
1196		BL f_copyRow
1197		ADD sp, sp, #8
1198		MOV r4, r0
1199		STRB r4, [sp]
1200		LDR r4, [sp, #1]
1201		STR r4, [sp, #-4]!
1202		LDR r4, [sp, #21]
1203		STR r4, [sp, #-4]!
1204		BL f_copyRow
1205		ADD sp, sp, #8
1206		MOV r4, r0
1207		STRB r4, [sp]
1208		MOV r4, #1
1209		MOV r0, r4
1210		ADD sp, sp, #33
1211		POP {pc}
1212		.ltorg
1213	f_copyRow:
1214		PUSH {lr}
1215		SUB sp, sp, #8
1216		LDR r4, [sp, #12]
1217		MOV r0, r4
1218		BL p_check_null_pointer
1219		LDR r4, [r4]
1220		LDR r4, [r4]
1221		STR r4, [sp, #4]
1222		LDR r4, [sp, #16]
1223		MOV r0, r4
1224		BL p_check_null_pointer
1225		LDR r4, [r4]
1226		LDR r4, [r4]
1227		STR r4, [sp]
1228		LDR r4, [sp, #4]
1229		MOV r0, r4
1230		BL p_check_null_pointer
1231		LDR r4, [r4]
1232		LDRSB r4, [r4]
1233		LDR r5, [sp]
1234		MOV r0, r5
1235		BL p_check_null_pointer
1236		LDR r5, [r5]
1237		STRB r4, [r5]
1238		LDR r4, [sp, #4]
1239		MOV r0, r4
1240		BL p_check_null_pointer
1241		LDR r4, [r4, #4]
1242		LDRSB r4, [r4]
1243		LDR r5, [sp]
1244		MOV r0, r5
1245		BL p_check_null_pointer
1246		LDR r5, [r5, #4]
1247		STRB r4, [r5]
1248		LDR r4, [sp, #12]
1249		MOV r0, r4
1250		BL p_check_null_pointer
1251		LDR r4, [r4, #4]
1252		LDRSB r4, [r4]
1253		LDR r5, [sp, #16]
1254		MOV r0, r5
1255		BL p_check_null_pointer
1256		LDR r5, [r5, #4]
1257		STRB r4, [r5]
1258		MOV r4, #1
1259		MOV r0, r4
1260		ADD sp, sp, #8
1261		POP {pc}
1262		.ltorg
1263	f_setValuesForAllStates:
1264		PUSH {lr}
1265		SUB sp, sp, #4
1266		LDR r4, =0
1267		STR r4, [sp]
1268		LDR r4, [sp, #8]
1269		LDR r5, =0
1270		CMP r4, r5
1271		MOVEQ r4, #1
1272		MOVNE r4, #0
1273		CMP r4, #0
1274		BEQ L20
1275		LDRSB r4, [sp, #13]
1276		LDRSB r5, [sp, #12]
1277		CMP r4, r5
1278		MOVEQ r4, #1
1279		MOVNE r4, #0
1280		CMP r4, #0
1281		BEQ L22
1282		LDR r4, =101
1283		STR r4, [sp]
1284		B L23
1285	L22:
1286		LDR r4, =-101
1287		STR r4, [sp]
1288	L23:
1289		B L21
1290	L20:
1291		SUB sp, sp, #14
1292		LDR r4, [sp, #22]
1293		MOV r0, r4
1294		BL p_check_null_pointer
1295		LDR r4, [r4]
1296		LDR r4, [r4]
1297		STR r4, [sp, #10]
1298		LDR r4, [sp, #10]
1299		MOV r0, r4
1300		BL p_check_null_pointer
1301		LDR r4, [r4]
1302		LDR r4, [r4]
1303		STR r4, [sp, #6]
1304		LDR r4, [sp, #10]
1305		MOV r0, r4
1306		BL p_check_null_pointer
1307		LDR r4, [r4, #4]
1308		LDR r4, [r4]
1309		STR r4, [sp, #2]
1310		LDRSB r4, [sp, #27]
1311		STRB r4, [sp, #-1]!
1312		BL f_oppositeSymbol
1313		ADD sp, sp, #1
1314		MOV r4, r0
1315		STRB r4, [sp, #1]
1316		LDRSB r4, [sp, #1]
1317		STRB r4, [sp, #-1]!
1318		LDR r4, [sp, #7]
1319		STR r4, [sp, #-4]!
1320		BL f_hasWon
1321		ADD sp, sp, #5
1322		MOV r4, r0
1323		STRB r4, [sp]
1324		LDRSB r4, [sp]
1325		CMP r4, #0
1326		BEQ L24
1327		LDRSB r4, [sp, #1]
1328		LDRSB r5, [sp, #26]
1329		CMP r4, r5
1330		MOVEQ r4, #1
1331		MOVNE r4, #0
1332		CMP r4, #0
1333		BEQ L26
1334		LDR r4, =100
1335		STR r4, [sp, #14]
1336		B L27
1337	L26:
1338		LDR r4, =-100
1339		STR r4, [sp, #14]
1340	L27:
1341		B L25
1342	L24:
1343		SUB sp, sp, #1
1344		LDR r4, [sp, #7]
1345		STR r4, [sp, #-4]!
1346		BL f_containEmptyCell
1347		ADD sp, sp, #4
1348		MOV r4, r0
1349		STRB r4, [sp]
1350		LDRSB r4, [sp]
1351		CMP r4, #0
1352		BEQ L28
1353		LDRSB r4, [sp, #2]
1354		STRB r4, [sp, #-1]!
1355		LDRSB r4, [sp, #28]
1356		STRB r4, [sp, #-1]!
1357		LDR r4, [sp, #5]
1358		STR r4, [sp, #-4]!
1359		BL f_calculateValuesFromNextStates
1360		ADD sp, sp, #6
1361		MOV r4, r0
1362		STR r4, [sp, #15]
1363		LDR r4, [sp, #15]
1364		LDR r5, =100
1365		CMP r4, r5
1366		MOVEQ r4, #1
1367		MOVNE r4, #0
1368		CMP r4, #0
1369		BEQ L30
1370		LDR r4, =90
1371		STR r4, [sp, #15]
1372		B L31
1373	L30:
1374	L31:
1375		B L29
1376	L28:
1377		LDR r4, =0
1378		STR r4, [sp, #15]
1379	L29:
1380		ADD sp, sp, #1
1381	L25:
1382		LDR r4, [sp, #14]
1383		LDR r5, [sp, #22]
1384		MOV r0, r5
1385		BL p_check_null_pointer
1386		LDR r5, [r5, #4]
1387		STR r4, [r5]
1388		ADD sp, sp, #14
1389	L21:
1390		LDR r4, [sp]
1391		MOV r0, r4
1392		ADD sp, sp, #4
1393		POP {pc}
1394		.ltorg
1395	f_calculateValuesFromNextStates:
1396		PUSH {lr}
1397		SUB sp, sp, #32
1398		LDR r4, [sp, #36]
1399		MOV r0, r4
1400		BL p_check_null_pointer
1401		LDR r4, [r4]
1402		LDR r4, [r4]
1403		STR r4, [sp, #28]
1404		LDR r4, [sp, #28]
1405		MOV r0, r4
1406		BL p_check_null_pointer
1407		LDR r4, [r4]
1408		LDR r4, [r4]
1409		STR r4, [sp, #24]
1410		LDR r4, [sp, #28]
1411		MOV r0, r4
1412		BL p_check_null_pointer
1413		LDR r4, [r4, #4]
1414		LDR r4, [r4]
1415		STR r4, [sp, #20]
1416		LDR r4, [sp, #36]
1417		MOV r0, r4
1418		BL p_check_null_pointer
1419		LDR r4, [r4, #4]
1420		LDR r4, [r4]
1421		STR r4, [sp, #16]
1422		LDRSB r4, [sp, #41]
1423		STRB r4, [sp, #-1]!
1424		LDRSB r4, [sp, #41]
1425		STRB r4, [sp, #-1]!
1426		LDR r4, [sp, #26]
1427		STR r4, [sp, #-4]!
1428		BL f_calculateValuesFromNextStatesRow
1429		ADD sp, sp, #6
1430		MOV r4, r0
1431		STR r4, [sp, #12]
1432		LDRSB r4, [sp, #41]
1433		STRB r4, [sp, #-1]!
1434		LDRSB r4, [sp, #41]
1435		STRB r4, [sp, #-1]!
1436		LDR r4, [sp, #22]
1437		STR r4, [sp, #-4]!
1438		BL f_calculateValuesFromNextStatesRow
1439		ADD sp, sp, #6
1440		MOV r4, r0
1441		STR r4, [sp, #8]
1442		LDRSB r4, [sp, #41]
1443		STRB r4, [sp, #-1]!
1444		LDRSB r4, [sp, #41]
1445		STRB r4, [sp, #-1]!
1446		LDR r4, [sp, #18]
1447		STR r4, [sp, #-4]!
1448		BL f_calculateValuesFromNextStatesRow
1449		ADD sp, sp, #6
1450		MOV r4, r0
1451		STR r4, [sp, #4]
1452		LDR r4, [sp, #4]
1453		STR r4, [sp, #-4]!
1454		LDR r4, [sp, #12]
1455		STR r4, [sp, #-4]!
1456		LDR r4, [sp, #20]
1457		STR r4, [sp, #-4]!
1458		LDRSB r4, [sp, #53]
1459		STRB r4, [sp, #-1]!
1460		LDRSB r4, [sp, #53]
1461		STRB r4, [sp, #-1]!
1462		BL f_combineValue
1463		ADD sp, sp, #14
1464		MOV r4, r0
1465		STR r4, [sp]
1466		LDR r4, [sp]
1467		MOV r0, r4
1468		ADD sp, sp, #32
1469		POP {pc}
1470		.ltorg
1471	f_calculateValuesFromNextStatesRow:
1472		PUSH {lr}
1473		SUB sp, sp, #32
1474		LDR r4, [sp, #36]
1475		MOV r0, r4
1476		BL p_check_null_pointer
1477		LDR r4, [r4]
1478		LDR r4, [r4]
1479		STR r4, [sp, #28]
1480		LDR r4, [sp, #28]
1481		MOV r0, r4
1482		BL p_check_null_pointer
1483		LDR r4, [r4]
1484		LDR r4, [r4]
1485		STR r4, [sp, #24]
1486		LDR r4, [sp, #28]
1487		MOV r0, r4
1488		BL p_check_null_pointer
1489		LDR r4, [r4, #4]
1490		LDR r4, [r4]
1491		STR r4, [sp, #20]
1492		LDR r4, [sp, #36]
1493		MOV r0, r4
1494		BL p_check_null_pointer
1495		LDR r4, [r4, #4]
1496		LDR r4, [r4]
1497		STR r4, [sp, #16]
1498		LDRSB r4, [sp, #41]
1499		STRB r4, [sp, #-1]!
1500		LDRSB r4, [sp, #41]
1501		STRB r4, [sp, #-1]!
1502		LDR r4, [sp, #26]
1503		STR r4, [sp, #-4]!
1504		BL f_setValuesForAllStates
1505		ADD sp, sp, #6
1506		MOV r4, r0
1507		STR r4, [sp, #12]
1508		LDRSB r4, [sp, #41]
1509		STRB r4, [sp, #-1]!
1510		LDRSB r4, [sp, #41]
1511		STRB r4, [sp, #-1]!
1512		LDR r4, [sp, #22]
1513		STR r4, [sp, #-4]!
1514		BL f_setValuesForAllStates
1515		ADD sp, sp, #6
1516		MOV r4, r0
1517		STR r4, [sp, #8]
1518		LDRSB r4, [sp, #41]
1519		STRB r4, [sp, #-1]!
1520		LDRSB r4, [sp, #41]
1521		STRB r4, [sp, #-1]!
1522		LDR r4, [sp, #18]
1523		STR r4, [sp, #-4]!
1524		BL f_setValuesForAllStates
1525		ADD sp, sp, #6
1526		MOV r4, r0
1527		STR r4, [sp, #4]
1528		LDR r4, [sp, #4]
1529		STR r4, [sp, #-4]!
1530		LDR r4, [sp, #12]
1531		STR r4, [sp, #-4]!
1532		LDR r4, [sp, #20]
1533		STR r4, [sp, #-4]!
1534		LDRSB r4, [sp, #53]
1535		STRB r4, [sp, #-1]!
1536		LDRSB r4, [sp, #53]
1537		STRB r4, [sp, #-1]!
1538		BL f_combineValue
1539		ADD sp, sp, #14
1540		MOV r4, r0
1541		STR r4, [sp]
1542		LDR r4, [sp]
1543		MOV r0, r4
1544		ADD sp, sp, #32
1545		POP {pc}
1546		.ltorg
1547	f_combineValue:
1548		PUSH {lr}
1549		SUB sp, sp, #4
1550		LDR r4, =0
1551		STR r4, [sp]
1552		LDRSB r4, [sp, #8]
1553		LDRSB r5, [sp, #9]
1554		CMP r4, r5
1555		MOVEQ r4, #1
1556		MOVNE r4, #0
1557		CMP r4, #0
1558		BEQ L32
1559		LDR r4, [sp, #18]
1560		STR r4, [sp, #-4]!
1561		LDR r4, [sp, #18]
1562		STR r4, [sp, #-4]!
1563		LDR r4, [sp, #18]
1564		STR r4, [sp, #-4]!
1565		BL f_min3
1566		ADD sp, sp, #12
1567		MOV r4, r0
1568		STR r4, [sp]
1569		B L33
1570	L32:
1571		LDR r4, [sp, #18]
1572		STR r4, [sp, #-4]!
1573		LDR r4, [sp, #18]
1574		STR r4, [sp, #-4]!
1575		LDR r4, [sp, #18]
1576		STR r4, [sp, #-4]!
1577		BL f_max3
1578		ADD sp, sp, #12
1579		MOV r4, r0
1580		STR r4, [sp]
1581	L33:
1582		LDR r4, [sp]
1583		MOV r0, r4
1584		ADD sp, sp, #4
1585		POP {pc}
1586		.ltorg
1587	f_min3:
1588		PUSH {lr}
1589		LDR r4, [sp, #4]
1590		LDR r5, [sp, #8]
1591		CMP r4, r5
1592		MOVLT r4, #1
1593		MOVGE r4, #0
1594		CMP r4, #0
1595		BEQ L34
1596		LDR r4, [sp, #4]
1597		LDR r5, [sp, #12]
1598		CMP r4, r5
1599		MOVLT r4, #1
1600		MOVGE r4, #0
1601		CMP r4, #0
1602		BEQ L36
1603		LDR r4, [sp, #4]
1604		MOV r0, r4
1605		B L37
1606	L36:
1607		LDR r4, [sp, #12]
1608		MOV r0, r4
1609	L37:
1610		B L35
1611	L34:
1612		LDR r4, [sp, #8]
1613		LDR r5, [sp, #12]
1614		CMP r4, r5
1615		MOVLT r4, #1
1616		MOVGE r4, #0
1617		CMP r4, #0
1618		BEQ L38
1619		LDR r4, [sp, #8]
1620		MOV r0, r4
1621		B L39
1622	L38:
1623		LDR r4, [sp, #12]
1624		MOV r0, r4
1625	L39:
1626	L35:
1627		POP {pc}
1628		.ltorg
1629	f_max3:
1630		PUSH {lr}
1631		LDR r4, [sp, #4]
1632		LDR r5, [sp, #8]
1633		CMP r4, r5
1634		MOVGT r4, #1
1635		MOVLE r4, #0
1636		CMP r4, #0
1637		BEQ L40
1638		LDR r4, [sp, #4]
1639		LDR r5, [sp, #12]
1640		CMP r4, r5
1641		MOVGT r4, #1
1642		MOVLE r4, #0
1643		CMP r4, #0
1644		BEQ L42
1645		LDR r4, [sp, #4]
1646		MOV r0, r4
1647		B L43
1648	L42:
1649		LDR r4, [sp, #12]
1650		MOV r0, r4
1651	L43:
1652		B L41
1653	L40:
1654		LDR r4, [sp, #8]
1655		LDR r5, [sp, #12]
1656		CMP r4, r5
1657		MOVGT r4, #1
1658		MOVLE r4, #0
1659		CMP r4, #0
1660		BEQ L44
1661		LDR r4, [sp, #8]
1662		MOV r0, r4
1663		B L45
1664	L44:
1665		LDR r4, [sp, #12]
1666		MOV r0, r4
1667	L45:
1668	L41:
1669		POP {pc}
1670		.ltorg
1671	f_destroyAI:
1672		PUSH {lr}
1673		SUB sp, sp, #9
1674		LDR r4, [sp, #13]
1675		MOV r0, r4
1676		BL p_check_null_pointer
1677		LDR r4, [r4]
1678		LDR r4, [r4]
1679		STR r4, [sp, #5]
1680		LDR r4, [sp, #13]
1681		MOV r0, r4
1682		BL p_check_null_pointer
1683		LDR r4, [r4, #4]
1684		LDR r4, [r4]
1685		STR r4, [sp, #1]
1686		LDR r4, [sp, #1]
1687		STR r4, [sp, #-4]!
1688		BL f_deleteStateTreeRecursively
1689		ADD sp, sp, #4
1690		MOV r4, r0
1691		STRB r4, [sp]
1692		LDR r4, [sp, #5]
1693		MOV r0, r4
1694		BL p_free_pair
1695		LDR r4, [sp, #13]
1696		MOV r0, r4
1697		BL p_free_pair
1698		MOV r4, #1
1699		MOV r0, r4
1700		ADD sp, sp, #9
1701		POP {pc}
1702		.ltorg
1703	f_askForAMoveAI:
1704		PUSH {lr}
1705		SUB sp, sp, #21
1706		LDR r4, [sp, #31]
1707		MOV r0, r4
1708		BL p_check_null_pointer
1709		LDR r4, [r4]
1710		LDR r4, [r4]
1711		STR r4, [sp, #17]
1712		LDR r4, [sp, #31]
1713		MOV r0, r4
1714		BL p_check_null_pointer
1715		LDR r4, [r4, #4]
1716		LDR r4, [r4]
1717		STR r4, [sp, #13]
1718		LDR r4, [sp, #13]
1719		MOV r0, r4
1720		BL p_check_null_pointer
1721		LDR r4, [r4]
1722		LDR r4, [r4]
1723		STR r4, [sp, #9]
1724		LDR r4, [sp, #9]
1725		MOV r0, r4
1726		BL p_check_null_pointer
1727		LDR r4, [r4, #4]
1728		LDR r4, [r4]
1729		STR r4, [sp, #5]
1730		LDR r4, [sp, #13]
1731		MOV r0, r4
1732		BL p_check_null_pointer
1733		LDR r4, [r4, #4]
1734		LDR r4, [r4]
1735		STR r4, [sp, #1]
1736		LDR r4, [sp, #35]
1737		STR r4, [sp, #-4]!
1738		LDR r4, [sp, #5]
1739		STR r4, [sp, #-4]!
1740		LDR r4, [sp, #13]
1741		STR r4, [sp, #-4]!
1742		BL f_findTheBestMove
1743		ADD sp, sp, #12
1744		MOV r4, r0
1745		STRB r4, [sp]
1746		LDR r4, =msg_30
1747		MOV r0, r4
1748		BL p_print_string
1749		BL p_print_ln
1750		ADD r4, sp, #35
1751		LDR r5, =1
1752		LDR r4, [r4]
1753		MOV r0, r5
1754		MOV r1, r4
1755		BL p_check_array_bounds
1756		ADD r4, r4, #4
1757		ADD r4, r4, r5, LSL #2
1758		LDR r4, [r4]
1759		STR r4, [sp, #-4]!
1760		ADD r4, sp, #39
1761		LDR r5, =0
1762		LDR r4, [r4]
1763		MOV r0, r5
1764		MOV r1, r4
1765		BL p_check_array_bounds
1766		ADD r4, r4, #4
1767		ADD r4, r4, r5, LSL #2
1768		LDR r4, [r4]
1769		STR r4, [sp, #-4]!
1770		LDR r4, [sp, #13]
1771		STR r4, [sp, #-4]!
1772		BL f_deleteAllOtherChildren
1773		ADD sp, sp, #12
1774		MOV r4, r0
1775		LDR r5, [sp, #31]
1776		MOV r0, r5
1777		BL p_check_null_pointer
1778		LDR r5, [r5, #4]
1779		STR r4, [r5]
1780		LDR r4, [sp, #13]
1781		STR r4, [sp, #-4]!
1782		BL f_deleteThisStateOnly
1783		ADD sp, sp, #4
1784		MOV r4, r0
1785		STRB r4, [sp]
1786		MOV r4, #1
1787		MOV r0, r4
1788		ADD sp, sp, #21
1789		POP {pc}
1790		.ltorg
1791	f_findTheBestMove:
1792		PUSH {lr}
1793		SUB sp, sp, #1
1794		LDR r4, [sp, #9]
1795		LDR r5, =90
1796		CMP r4, r5
1797		MOVEQ r4, #1
1798		MOVNE r4, #0
1799		CMP r4, #0
1800		BEQ L46
1801		SUB sp, sp, #1
1802		LDR r4, [sp, #14]
1803		STR r4, [sp, #-4]!
1804		LDR r4, =100
1805		STR r4, [sp, #-4]!
1806		LDR r4, [sp, #14]
1807		STR r4, [sp, #-4]!
1808		BL f_findMoveWithGivenValue
1809		ADD sp, sp, #12
1810		MOV r4, r0
1811		STRB r4, [sp]
1812		LDRSB r4, [sp]
1813		CMP r4, #0
1814		BEQ L48
1815		MOV r4, #1
1816		MOV r0, r4
1817		ADD sp, sp, #2
1818		B L49
1819	L48:
1820	L49:
1821		ADD sp, sp, #1
1822		B L47
1823	L46:
1824	L47:
1825		LDR r4, [sp, #13]
1826		STR r4, [sp, #-4]!
1827		LDR r4, [sp, #13]
1828		STR r4, [sp, #-4]!
1829		LDR r4, [sp, #13]
1830		STR r4, [sp, #-4]!
1831		BL f_findMoveWithGivenValue
1832		ADD sp, sp, #12
1833		MOV r4, r0
1834		STRB r4, [sp]
1835		LDRSB r4, [sp]
1836		CMP r4, #0
1837		BEQ L50
1838		MOV r4, #1
1839		MOV r0, r4
1840		ADD sp, sp, #1
1841		B L51
1842	L50:
1843		LDR r4, =msg_31
1844		MOV r0, r4
1845		BL p_print_string
1846		BL p_print_ln
1847		LDR r4, =-1
1848		MOV r0, r4
1849		BL exit
1850	L51:
1851		POP {pc}
1852		.ltorg
1853	f_findMoveWithGivenValue:
1854		PUSH {lr}
1855		SUB sp, sp, #17
1856		LDR r4, [sp, #21]
1857		MOV r0, r4
1858		BL p_check_null_pointer
1859		LDR r4, [r4]
1860		LDR r4, [r4]
1861		STR r4, [sp, #13]
1862		LDR r4, [sp, #13]
1863		MOV r0, r4
1864		BL p_check_null_pointer
1865		LDR r4, [r4]
1866		LDR r4, [r4]
1867		STR r4, [sp, #9]
1868		LDR r4, [sp, #13]
1869		MOV r0, r4
1870		BL p_check_null_pointer
1871		LDR r4, [r4, #4]
1872		LDR r4, [r4]
1873		STR r4, [sp, #5]
1874		LDR r4, [sp, #21]
1875		MOV r0, r4
1876		BL p_check_null_pointer
1877		LDR r4, [r4, #4]
1878		LDR r4, [r4]
1879		STR r4, [sp, #1]
1880		LDR r4, [sp, #29]
1881		STR r4, [sp, #-4]!
1882		LDR r4, [sp, #29]
1883		STR r4, [sp, #-4]!
1884		LDR r4, [sp, #17]
1885		STR r4, [sp, #-4]!
1886		BL f_findMoveWithGivenValueRow
1887		ADD sp, sp, #12
1888		MOV r4, r0
1889		STRB r4, [sp]
1890		LDRSB r4, [sp]
1891		CMP r4, #0
1892		BEQ L52
1893		LDR r4, =1
1894		ADD r5, sp, #29
1895		LDR r6, =0
1896		LDR r5, [r5]
1897		MOV r0, r6
1898		MOV r1, r5
1899		BL p_check_array_bounds
1900		ADD r5, r5, #4
1901		ADD r5, r5, r6, LSL #2
1902		STR r4, [r5]
1903		B L53
1904	L52:
1905		LDR r4, [sp, #29]
1906		STR r4, [sp, #-4]!
1907		LDR r4, [sp, #29]
1908		STR r4, [sp, #-4]!
1909		LDR r4, [sp, #13]
1910		STR r4, [sp, #-4]!
1911		BL f_findMoveWithGivenValueRow
1912		ADD sp, sp, #12
1913		MOV r4, r0
1914		STRB r4, [sp]
1915		LDRSB r4, [sp]
1916		CMP r4, #0
1917		BEQ L54
1918		LDR r4, =2
1919		ADD r6, sp, #29
1920		LDR r7, =0
1921		LDR r6, [r6]
1922		MOV r0, r7
1923		MOV r1, r6
1924		BL p_check_array_bounds
1925		ADD r6, r6, #4
1926		ADD r6, r6, r7, LSL #2
1927		STR r4, [r6]
1928		B L55
1929	L54:
1930		LDR r4, [sp, #29]
1931		STR r4, [sp, #-4]!
1932		LDR r4, [sp, #29]
1933		STR r4, [sp, #-4]!
1934		LDR r4, [sp, #9]
1935		STR r4, [sp, #-4]!
1936		BL f_findMoveWithGivenValueRow
1937		ADD sp, sp, #12
1938		MOV r4, r0
1939		STRB r4, [sp]
1940		LDRSB r4, [sp]
1941		CMP r4, #0
1942		BEQ L56
1943		LDR r4, =3
1944		ADD r7, sp, #29
1945		LDR r8, =0
1946		LDR r7, [r7]
1947		MOV r0, r8
1948		MOV r1, r7
1949		BL p_check_array_bounds
1950		ADD r7, r7, #4
1951		ADD r7, r7, r8, LSL #2
1952		STR r4, [r7]
1953		B L57
1954	L56:
1955		MOV r4, #0
1956		MOV r0, r4
1957		ADD sp, sp, #17
1958	L57:
1959	L55:
1960	L53:
1961		MOV r4, #1
1962		MOV r0, r4
1963		ADD sp, sp, #17
1964		POP {pc}
1965		.ltorg
1966	f_findMoveWithGivenValueRow:
1967		PUSH {lr}
1968		SUB sp, sp, #17
1969		LDR r4, [sp, #21]
1970		MOV r0, r4
1971		BL p_check_null_pointer
1972		LDR r4, [r4]
1973		LDR r4, [r4]
1974		STR r4, [sp, #13]
1975		LDR r4, [sp, #13]
1976		MOV r0, r4
1977		BL p_check_null_pointer
1978		LDR r4, [r4]
1979		LDR r4, [r4]
1980		STR r4, [sp, #9]
1981		LDR r4, [sp, #13]
1982		MOV r0, r4
1983		BL p_check_null_pointer
1984		LDR r4, [r4, #4]
1985		LDR r4, [r4]
1986		STR r4, [sp, #5]
1987		LDR r4, [sp, #21]
1988		MOV r0, r4
1989		BL p_check_null_pointer
1990		LDR r4, [r4, #4]
1991		LDR r4, [r4]
1992		STR r4, [sp, #1]
1993		LDR r4, [sp, #25]
1994		STR r4, [sp, #-4]!
1995		LDR r4, [sp, #13]
1996		STR r4, [sp, #-4]!
1997		BL f_hasGivenStateValue
1998		ADD sp, sp, #8
1999		MOV r4, r0
2000		STRB r4, [sp]
2001		LDRSB r4, [sp]
2002		CMP r4, #0
2003		BEQ L58
2004		LDR r4, =1
2005		ADD r5, sp, #29
2006		LDR r6, =1
2007		LDR r5, [r5]
2008		MOV r0, r6
2009		MOV r1, r5
2010		BL p_check_array_bounds
2011		ADD r5, r5, #4
2012		ADD r5, r5, r6, LSL #2
2013		STR r4, [r5]
2014		B L59
2015	L58:
2016		LDR r4, [sp, #25]
2017		STR r4, [sp, #-4]!
2018		LDR r4, [sp, #9]
2019		STR r4, [sp, #-4]!
2020		BL f_hasGivenStateValue
2021		ADD sp, sp, #8
2022		MOV r4, r0
2023		STRB r4, [sp]
2024		LDRSB r4, [sp]
2025		CMP r4, #0
2026		BEQ L60
2027		LDR r4, =2
2028		ADD r6, sp, #29
2029		LDR r7, =1
2030		LDR r6, [r6]
2031		MOV r0, r7
2032		MOV r1, r6
2033		BL p_check_array_bounds
2034		ADD r6, r6, #4
2035		ADD r6, r6, r7, LSL #2
2036		STR r4, [r6]
2037		B L61
2038	L60:
2039		LDR r4, [sp, #25]
2040		STR r4, [sp, #-4]!
2041		LDR r4, [sp, #5]
2042		STR r4, [sp, #-4]!
2043		BL f_hasGivenStateValue
2044		ADD sp, sp, #8
2045		MOV r4, r0
2046		STRB r4, [sp]
2047		LDRSB r4, [sp]
2048		CMP r4, #0
2049		BEQ L62
2050		LDR r4, =3
2051		ADD r7, sp, #29
2052		LDR r8, =1
2053		LDR r7, [r7]
2054		MOV r0, r8
2055		MOV r1, r7
2056		BL p_check_array_bounds
2057		ADD r7, r7, #4
2058		ADD r7, r7, r8, LSL #2
2059		STR r4, [r7]
2060		B L63
2061	L62:
2062		MOV r4, #0
2063		MOV r0, r4
2064		ADD sp, sp, #17
2065	L63:
2066	L61:
2067	L59:
2068		MOV r4, #1
2069		MOV r0, r4
2070		ADD sp, sp, #17
2071		POP {pc}
2072		.ltorg
2073	f_hasGivenStateValue:
2074		PUSH {lr}
2075		LDR r4, [sp, #4]
2076		LDR r5, =0
2077		CMP r4, r5
2078		MOVEQ r4, #1
2079		MOVNE r4, #0
2080		CMP r4, #0
2081		BEQ L64
2082		MOV r4, #0
2083		MOV r0, r4
2084		B L65
2085	L64:
2086		SUB sp, sp, #4
2087		LDR r4, [sp, #8]
2088		MOV r0, r4
2089		BL p_check_null_pointer
2090		LDR r4, [r4, #4]
2091		LDR r4, [r4]
2092		STR r4, [sp]
2093		LDR r4, [sp]
2094		LDR r5, [sp, #12]
2095		CMP r4, r5
2096		MOVEQ r4, #1
2097		MOVNE r4, #0
2098		MOV r0, r4
2099		ADD sp, sp, #4
2100		ADD sp, sp, #4
2101	L65:
2102		POP {pc}
2103		.ltorg
2104	f_notifyMoveAI:
2105		PUSH {lr}
2106		SUB sp, sp, #13
2107		LDR r4, [sp, #23]
2108		MOV r0, r4
2109		BL p_check_null_pointer
2110		LDR r4, [r4, #4]
2111		LDR r4, [r4]
2112		STR r4, [sp, #9]
2113		LDR r4, [sp, #9]
2114		MOV r0, r4
2115		BL p_check_null_pointer
2116		LDR r4, [r4]
2117		LDR r4, [r4]
2118		STR r4, [sp, #5]
2119		LDR r4, [sp, #5]
2120		MOV r0, r4
2121		BL p_check_null_pointer
2122		LDR r4, [r4, #4]
2123		LDR r4, [r4]
2124		STR r4, [sp, #1]
2125		LDR r4, =msg_32
2126		MOV r0, r4
2127		BL p_print_string
2128		BL p_print_ln
2129		LDR r4, [sp, #31]
2130		STR r4, [sp, #-4]!
2131		LDR r4, [sp, #31]
2132		STR r4, [sp, #-4]!
2133		LDR r4, [sp, #9]
2134		STR r4, [sp, #-4]!
2135		BL f_deleteAllOtherChildren
2136		ADD sp, sp, #12
2137		MOV r4, r0
2138		LDR r5, [sp, #23]
2139		MOV r0, r5
2140		BL p_check_null_pointer
2141		LDR r5, [r5, #4]
2142		STR r4, [r5]
2143		LDR r4, [sp, #9]
2144		STR r4, [sp, #-4]!
2145		BL f_deleteThisStateOnly
2146		ADD sp, sp, #4
2147		MOV r4, r0
2148		STRB r4, [sp]
2149		MOV r4, #1
2150		MOV r0, r4
2151		ADD sp, sp, #13
2152		POP {pc}
2153		.ltorg
2154	f_deleteAllOtherChildren:
2155		PUSH {lr}
2156		SUB sp, sp, #33
2157		LDR r4, [sp, #37]
2158		MOV r0, r4
2159		BL p_check_null_pointer
2160		LDR r4, [r4]
2161		LDR r4, [r4]
2162		STR r4, [sp, #29]
2163		LDR r4, [sp, #29]
2164		MOV r0, r4
2165		BL p_check_null_pointer
2166		LDR r4, [r4]
2167		LDR r4, [r4]
2168		STR r4, [sp, #25]
2169		LDR r4, [sp, #29]
2170		MOV r0, r4
2171		BL p_check_null_pointer
2172		LDR r4, [r4, #4]
2173		LDR r4, [r4]
2174		STR r4, [sp, #21]
2175		LDR r4, [sp, #37]
2176		MOV r0, r4
2177		BL p_check_null_pointer
2178		LDR r4, [r4, #4]
2179		LDR r4, [r4]
2180		STR r4, [sp, #17]
2181		LDR r4, =0
2182		STR r4, [sp, #13]
2183		LDR r4, =0
2184		STR r4, [sp, #9]
2185		LDR r4, =0
2186		STR r4, [sp, #5]
2187		LDR r4, [sp, #41]
2188		LDR r5, =1
2189		CMP r4, r5
2190		MOVEQ r4, #1
2191		MOVNE r4, #0
2192		CMP r4, #0
2193		BEQ L66
2194		LDR r4, [sp, #25]
2195		STR r4, [sp, #13]
2196		LDR r4, [sp, #21]
2197		STR r4, [sp, #9]
2198		LDR r4, [sp, #17]
2199		STR r4, [sp, #5]
2200		B L67
2201	L66:
2202		LDR r4, [sp, #25]
2203		STR r4, [sp, #9]
2204		LDR r4, [sp, #41]
2205		LDR r5, =2
2206		CMP r4, r5
2207		MOVEQ r4, #1
2208		MOVNE r4, #0
2209		CMP r4, #0
2210		BEQ L68
2211		LDR r4, [sp, #21]
2212		STR r4, [sp, #13]
2213		LDR r4, [sp, #17]
2214		STR r4, [sp, #5]
2215		B L69
2216	L68:
2217		LDR r4, [sp, #17]
2218		STR r4, [sp, #13]
2219		LDR r4, [sp, #21]
2220		STR r4, [sp, #5]
2221	L69:
2222	L67:
2223		LDR r4, [sp, #45]
2224		STR r4, [sp, #-4]!
2225		LDR r4, [sp, #17]
2226		STR r4, [sp, #-4]!
2227		BL f_deleteAllOtherChildrenRow
2228		ADD sp, sp, #8
2229		MOV r4, r0
2230		STR r4, [sp, #1]
2231		LDR r4, [sp, #9]
2232		STR r4, [sp, #-4]!
2233		BL f_deleteChildrenStateRecursivelyRow
2234		ADD sp, sp, #4
2235		MOV r4, r0
2236		STRB r4, [sp]
2237		LDR r4, [sp, #5]
2238		STR r4, [sp, #-4]!
2239		BL f_deleteChildrenStateRecursivelyRow
2240		ADD sp, sp, #4
2241		MOV r4, r0
2242		STRB r4, [sp]
2243		LDR r4, [sp, #1]
2244		MOV r0, r4
2245		ADD sp, sp, #33
2246		POP {pc}
2247		.ltorg
2248	f_deleteAllOtherChildrenRow:
2249		PUSH {lr}
2250		SUB sp, sp, #29
2251		LDR r4, [sp, #33]
2252		MOV r0, r4
2253		BL p_check_null_pointer
2254		LDR r4, [r4]
2255		LDR r4, [r4]
2256		STR r4, [sp, #25]
2257		LDR r4, [sp, #25]
2258		MOV r0, r4
2259		BL p_check_null_pointer
2260		LDR r4, [r4]
2261		LDR r4, [r4]
2262		STR r4, [sp, #21]
2263		LDR r4, [sp, #25]
2264		MOV r0, r4
2265		BL p_check_null_pointer
2266		LDR r4, [r4, #4]
2267		LDR r4, [r4]
2268		STR r4, [sp, #17]
2269		LDR r4, [sp, #33]
2270		MOV r0, r4
2271		BL p_check_null_pointer
2272		LDR r4, [r4, #4]
2273		LDR r4, [r4]
2274		STR r4, [sp, #13]
2275		LDR r4, =0
2276		STR r4, [sp, #9]
2277		LDR r4, =0
2278		STR r4, [sp, #5]
2279		LDR r4, =0
2280		STR r4, [sp, #1]
2281		LDR r4, [sp, #37]
2282		LDR r5, =1
2283		CMP r4, r5
2284		MOVEQ r4, #1
2285		MOVNE r4, #0
2286		CMP r4, #0
2287		BEQ L70
2288		LDR r4, [sp, #21]
2289		STR r4, [sp, #9]
2290		LDR r4, [sp, #17]
2291		STR r4, [sp, #5]
2292		LDR r4, [sp, #13]
2293		STR r4, [sp, #1]
2294		B L71
2295	L70:
2296		LDR r4, [sp, #21]
2297		STR r4, [sp, #5]
2298		LDR r4, [sp, #37]
2299		LDR r5, =2
2300		CMP r4, r5
2301		MOVEQ r4, #1
2302		MOVNE r4, #0
2303		CMP r4, #0
2304		BEQ L72
2305		LDR r4, [sp, #17]
2306		STR r4, [sp, #9]
2307		LDR r4, [sp, #13]
2308		STR r4, [sp, #1]
2309		B L73
2310	L72:
2311		LDR r4, [sp, #13]
2312		STR r4, [sp, #9]
2313		LDR r4, [sp, #17]
2314		STR r4, [sp, #1]
2315	L73:
2316	L71:
2317		LDR r4, [sp, #5]
2318		STR r4, [sp, #-4]!
2319		BL f_deleteStateTreeRecursively
2320		ADD sp, sp, #4
2321		MOV r4, r0
2322		STRB r4, [sp]
2323		LDR r4, [sp, #1]
2324		STR r4, [sp, #-4]!
2325		BL f_deleteStateTreeRecursively
2326		ADD sp, sp, #4
2327		MOV r4, r0
2328		STRB r4, [sp]
2329		LDR r4, [sp, #9]
2330		MOV r0, r4
2331		ADD sp, sp, #29
2332		POP {pc}
2333		.ltorg
2334	f_deleteStateTreeRecursively:
2335		PUSH {lr}
2336		LDR r4, [sp, #4]
2337		LDR r5, =0
2338		CMP r4, r5
2339		MOVEQ r4, #1
2340		MOVNE r4, #0
2341		CMP r4, #0
2342		BEQ L74
2343		MOV r4, #1
2344		MOV r0, r4
2345		B L75
2346	L74:
2347		SUB sp, sp, #13
2348		LDR r4, [sp, #17]
2349		MOV r0, r4
2350		BL p_check_null_pointer
2351		LDR r4, [r4]
2352		LDR r4, [r4]
2353		STR r4, [sp, #9]
2354		LDR r4, [sp, #9]
2355		MOV r0, r4
2356		BL p_check_null_pointer
2357		LDR r4, [r4]
2358		LDR r4, [r4]
2359		STR r4, [sp, #5]
2360		LDR r4, [sp, #9]
2361		MOV r0, r4
2362		BL p_check_null_pointer
2363		LDR r4, [r4, #4]
2364		LDR r4, [r4]
2365		STR r4, [sp, #1]
2366		LDR r4, [sp, #1]
2367		STR r4, [sp, #-4]!
2368		BL f_deleteChildrenStateRecursively
2369		ADD sp, sp, #4
2370		MOV r4, r0
2371		STRB r4, [sp]
2372		LDR r4, [sp, #17]
2373		STR r4, [sp, #-4]!
2374		BL f_deleteThisStateOnly
2375		ADD sp, sp, #4
2376		MOV r4, r0
2377		STRB r4, [sp]
2378		MOV r4, #1
2379		MOV r0, r4
2380		ADD sp, sp, #13
2381		ADD sp, sp, #13
2382	L75:
2383		POP {pc}
2384		.ltorg
2385	f_deleteThisStateOnly:
2386		PUSH {lr}
2387		SUB sp, sp, #13
2388		LDR r4, [sp, #17]
2389		MOV r0, r4
2390		BL p_check_null_pointer
2391		LDR r4, [r4]
2392		LDR r4, [r4]
2393		STR r4, [sp, #9]
2394		LDR r4, [sp, #9]
2395		MOV r0, r4
2396		BL p_check_null_pointer
2397		LDR r4, [r4]
2398		LDR r4, [r4]
2399		STR r4, [sp, #5]
2400		LDR r4, [sp, #9]
2401		MOV r0, r4
2402		BL p_check_null_pointer
2403		LDR r4, [r4, #4]
2404		LDR r4, [r4]
2405		STR r4, [sp, #1]
2406		LDR r4, [sp, #5]
2407		STR r4, [sp, #-4]!
2408		BL f_freeBoard
2409		ADD sp, sp, #4
2410		MOV r4, r0
2411		STRB r4, [sp]
2412		LDR r4, [sp, #1]
2413		STR r4, [sp, #-4]!
2414		BL f_freePointers
2415		ADD sp, sp, #4
2416		MOV r4, r0
2417		STRB r4, [sp]
2418		LDR r4, [sp, #9]
2419		MOV r0, r4
2420		BL p_free_pair
2421		LDR r4, [sp, #17]
2422		MOV r0, r4
2423		BL p_free_pair
2424		MOV r4, #1
2425		MOV r0, r4
2426		ADD sp, sp, #13
2427		POP {pc}
2428		.ltorg
2429	f_freePointers:
2430		PUSH {lr}
2431		SUB sp, sp, #17
2432		LDR r4, [sp, #21]
2433		MOV r0, r4
2434		BL p_check_null_pointer
2435		LDR r4, [r4]
2436		LDR r4, [r4]
2437		STR r4, [sp, #13]
2438		LDR r4, [sp, #13]
2439		MOV r0, r4
2440		BL p_check_null_pointer
2441		LDR r4, [r4]
2442		LDR r4, [r4]
2443		STR r4, [sp, #9]
2444		LDR r4, [sp, #13]
2445		MOV r0, r4
2446		BL p_check_null_pointer
2447		LDR r4, [r4, #4]
2448		LDR r4, [r4]
2449		STR r4, [sp, #5]
2450		LDR r4, [sp, #21]
2451		MOV r0, r4
2452		BL p_check_null_pointer
2453		LDR r4, [r4, #4]
2454		LDR r4, [r4]
2455		STR r4, [sp, #1]
2456		LDR r4, [sp, #9]
2457		STR r4, [sp, #-4]!
2458		BL f_freePointersRow
2459		ADD sp, sp, #4
2460		MOV r4, r0
2461		STRB r4, [sp]
2462		LDR r4, [sp, #5]
2463		STR r4, [sp, #-4]!
2464		BL f_freePointersRow
2465		ADD sp, sp, #4
2466		MOV r4, r0
2467		STRB r4, [sp]
2468		LDR r4, [sp, #1]
2469		STR r4, [sp, #-4]!
2470		BL f_freePointersRow
2471		ADD sp, sp, #4
2472		MOV r4, r0
2473		STRB r4, [sp]
2474		LDR r4, [sp, #13]
2475		MOV r0, r4
2476		BL p_free_pair
2477		LDR r4, [sp, #21]
2478		MOV r0, r4
2479		BL p_free_pair
2480		MOV r4, #1
2481		MOV r0, r4
2482		ADD sp, sp, #17
2483		POP {pc}
2484		.ltorg
2485	f_freePointersRow:
2486		PUSH {lr}
2487		SUB sp, sp, #4
2488		LDR r4, [sp, #8]
2489		MOV r0, r4
2490		BL p_check_null_pointer
2491		LDR r4, [r4]
2492		LDR r4, [r4]
2493		STR r4, [sp]
2494		LDR r4, [sp]
2495		MOV r0, r4
2496		BL p_free_pair
2497		LDR r4, [sp, #8]
2498		MOV r0, r4
2499		BL p_free_pair
2500		MOV r4, #1
2501		MOV r0, r4
2502		ADD sp, sp, #4
2503		POP {pc}
2504		.ltorg
2505	f_deleteChildrenStateRecursively:
2506		PUSH {lr}
2507		SUB sp, sp, #17
2508		LDR r4, [sp, #21]
2509		MOV r0, r4
2510		BL p_check_null_pointer
2511		LDR r4, [r4]
2512		LDR r4, [r4]
2513		STR r4, [sp, #13]
2514		LDR r4, [sp, #13]
2515		MOV r0, r4
2516		BL p_check_null_pointer
2517		LDR r4, [r4]
2518		LDR r4, [r4]
2519		STR r4, [sp, #9]
2520		LDR r4, [sp, #13]
2521		MOV r0, r4
2522		BL p_check_null_pointer
2523		LDR r4, [r4, #4]
2524		LDR r4, [r4]
2525		STR r4, [sp, #5]
2526		LDR r4, [sp, #21]
2527		MOV r0, r4
2528		BL p_check_null_pointer
2529		LDR r4, [r4, #4]
2530		LDR r4, [r4]
2531		STR r4, [sp, #1]
2532		LDR r4, [sp, #9]
2533		STR r4, [sp, #-4]!
2534		BL f_deleteChildrenStateRecursivelyRow
2535		ADD sp, sp, #4
2536		MOV r4, r0
2537		STRB r4, [sp]
2538		LDR r4, [sp, #5]
2539		STR r4, [sp, #-4]!
2540		BL f_deleteChildrenStateRecursivelyRow
2541		ADD sp, sp, #4
2542		MOV r4, r0
2543		STRB r4, [sp]
2544		LDR r4, [sp, #1]
2545		STR r4, [sp, #-4]!
2546		BL f_deleteChildrenStateRecursivelyRow
2547		ADD sp, sp, #4
2548		MOV r4, r0
2549		STRB r4, [sp]
2550		MOV r4, #1
2551		MOV r0, r4
2552		ADD sp, sp, #17
2553		POP {pc}
2554		.ltorg
2555	f_deleteChildrenStateRecursivelyRow:
2556		PUSH {lr}
2557		SUB sp, sp, #17
2558		LDR r4, [sp, #21]
2559		MOV r0, r4
2560		BL p_check_null_pointer
2561		LDR r4, [r4]
2562		LDR r4, [r4]
2563		STR r4, [sp, #13]
2564		LDR r4, [sp, #13]
2565		MOV r0, r4
2566		BL p_check_null_pointer
2567		LDR r4, [r4]
2568		LDR r4, [r4]
2569		STR r4, [sp, #9]
2570		LDR r4, [sp, #13]
2571		MOV r0, r4
2572		BL p_check_null_pointer
2573		LDR r4, [r4, #4]
2574		LDR r4, [r4]
2575		STR r4, [sp, #5]
2576		LDR r4, [sp, #21]
2577		MOV r0, r4
2578		BL p_check_null_pointer
2579		LDR r4, [r4, #4]
2580		LDR r4, [r4]
2581		STR r4, [sp, #1]
2582		LDR r4, [sp, #9]
2583		STR r4, [sp, #-4]!
2584		BL f_deleteStateTreeRecursively
2585		ADD sp, sp, #4
2586		MOV r4, r0
2587		STRB r4, [sp]
2588		LDR r4, [sp, #5]
2589		STR r4, [sp, #-4]!
2590		BL f_deleteStateTreeRecursively
2591		ADD sp, sp, #4
2592		MOV r4, r0
2593		STRB r4, [sp]
2594		LDR r4, [sp, #1]
2595		STR r4, [sp, #-4]!
2596		BL f_deleteStateTreeRecursively
2597		ADD sp, sp, #4
2598		MOV r4, r0
2599		STRB r4, [sp]
2600		MOV r4, #1
2601		MOV r0, r4
2602		ADD sp, sp, #17
2603		POP {pc}
2604		.ltorg
2605	f_askForAMove:
2606		PUSH {lr}
2607		LDRSB r4, [sp, #8]
2608		LDRSB r5, [sp, #9]
2609		CMP r4, r5
2610		MOVEQ r4, #1
2611		MOVNE r4, #0
2612		CMP r4, #0
2613		BEQ L76
2614		SUB sp, sp, #1
2615		LDR r4, [sp, #15]
2616		STR r4, [sp, #-4]!
2617		LDR r4, [sp, #9]
2618		STR r4, [sp, #-4]!
2619		BL f_askForAMoveHuman
2620		ADD sp, sp, #8
2621		MOV r4, r0
2622		STRB r4, [sp]
2623		ADD sp, sp, #1
2624		B L77
2625	L76:
2626		SUB sp, sp, #1
2627		LDR r4, [sp, #15]
2628		STR r4, [sp, #-4]!
2629		LDR r4, [sp, #15]
2630		STR r4, [sp, #-4]!
2631		LDRSB r4, [sp, #18]
2632		STRB r4, [sp, #-1]!
2633		LDRSB r4, [sp, #18]
2634		STRB r4, [sp, #-1]!
2635		LDR r4, [sp, #15]
2636		STR r4, [sp, #-4]!
2637		BL f_askForAMoveAI
2638		ADD sp, sp, #14
2639		MOV r4, r0
2640		STRB r4, [sp]
2641		ADD sp, sp, #1
2642	L77:
2643		MOV r4, #1
2644		MOV r0, r4
2645		POP {pc}
2646		.ltorg
2647	f_placeMove:
2648		PUSH {lr}
2649		SUB sp, sp, #4
2650		LDR r4, =0
2651		STR r4, [sp]
2652		LDR r4, [sp, #13]
2653		LDR r5, =2
2654		CMP r4, r5
2655		MOVLE r4, #1
2656		MOVGT r4, #0
2657		CMP r4, #0
2658		BEQ L78
2659		SUB sp, sp, #4
2660		LDR r4, [sp, #12]
2661		MOV r0, r4
2662		BL p_check_null_pointer
2663		LDR r4, [r4]
2664		LDR r4, [r4]
2665		STR r4, [sp]
2666		LDR r4, [sp, #17]
2667		LDR r5, =1
2668		CMP r4, r5
2669		MOVEQ r4, #1
2670		MOVNE r4, #0
2671		CMP r4, #0
2672		BEQ L80
2673		LDR r4, [sp]
2674		MOV r0, r4
2675		BL p_check_null_pointer
2676		LDR r4, [r4]
2677		LDR r4, [r4]
2678		STR r4, [sp, #4]
2679		B L81
2680	L80:
2681		LDR r4, [sp]
2682		MOV r0, r4
2683		BL p_check_null_pointer
2684		LDR r4, [r4, #4]
2685		LDR r4, [r4]
2686		STR r4, [sp, #4]
2687	L81:
2688		ADD sp, sp, #4
2689		B L79
2690	L78:
2691		LDR r4, [sp, #8]
2692		MOV r0, r4
2693		BL p_check_null_pointer
2694		LDR r4, [r4, #4]
2695		LDR r4, [r4]
2696		STR r4, [sp]
2697	L79:
2698		LDR r4, [sp, #17]
2699		LDR r5, =2
2700		CMP r4, r5
2701		MOVLE r4, #1
2702		MOVGT r4, #0
2703		CMP r4, #0
2704		BEQ L82
2705		SUB sp, sp, #4
2706		LDR r4, [sp, #4]
2707		MOV r0, r4
2708		BL p_check_null_pointer
2709		LDR r4, [r4]
2710		LDR r4, [r4]
2711		STR r4, [sp]
2712		LDR r4, [sp, #21]
2713		LDR r5, =1
2714		CMP r4, r5
2715		MOVEQ r4, #1
2716		MOVNE r4, #0
2717		CMP r4, #0
2718		BEQ L84
2719		LDRSB r4, [sp, #16]
2720		LDR r5, [sp]
2721		MOV r0, r5
2722		BL p_check_null_pointer
2723		LDR r5, [r5]
2724		STRB r4, [r5]
2725		B L85
2726	L84:
2727		LDRSB r4, [sp, #16]
2728		LDR r5, [sp]
2729		MOV r0, r5
2730		BL p_check_null_pointer
2731		LDR r5, [r5, #4]
2732		STRB r4, [r5]
2733	L85:
2734		ADD sp, sp, #4
2735		B L83
2736	L82:
2737		LDRSB r4, [sp, #12]
2738		LDR r5, [sp]
2739		MOV r0, r5
2740		BL p_check_null_pointer
2741		LDR r5, [r5, #4]
2742		STRB r4, [r5]
2743	L83:
2744		MOV r4, #1
2745		MOV r0, r4
2746		ADD sp, sp, #4
2747		POP {pc}
2748		.ltorg
2749	f_notifyMove:
2750		PUSH {lr}
2751		LDRSB r4, [sp, #8]
2752		LDRSB r5, [sp, #9]
2753		CMP r4, r5
2754		MOVEQ r4, #1
2755		MOVNE r4, #0
2756		CMP r4, #0
2757		BEQ L86
2758		SUB sp, sp, #1
2759		LDR r4, [sp, #19]
2760		STR r4, [sp, #-4]!
2761		LDR r4, [sp, #19]
2762		STR r4, [sp, #-4]!
2763		LDR r4, [sp, #19]
2764		STR r4, [sp, #-4]!
2765		LDRSB r4, [sp, #22]
2766		STRB r4, [sp, #-1]!
2767		LDRSB r4, [sp, #22]
2768		STRB r4, [sp, #-1]!
2769		LDR r4, [sp, #19]
2770		STR r4, [sp, #-4]!
2771		BL f_notifyMoveAI
2772		ADD sp, sp, #18
2773		MOV r4, r0
2774		STRB r4, [sp]
2775		ADD sp, sp, #1
2776		B L87
2777	L86:
2778		SUB sp, sp, #1
2779		LDR r4, [sp, #19]
2780		STR r4, [sp, #-4]!
2781		LDR r4, [sp, #19]
2782		STR r4, [sp, #-4]!
2783		LDRSB r4, [sp, #18]
2784		STRB r4, [sp, #-1]!
2785		LDRSB r4, [sp, #18]
2786		STRB r4, [sp, #-1]!
2787		LDR r4, [sp, #15]
2788		STR r4, [sp, #-4]!
2789		BL f_notifyMoveHuman
2790		ADD sp, sp, #14
2791		MOV r4, r0
2792		STRB r4, [sp]
2793		ADD sp, sp, #1
2794	L87:
2795		MOV r4, #1
2796		MOV r0, r4
2797		POP {pc}
2798		.ltorg
2799	f_oppositeSymbol:
2800		PUSH {lr}
2801		LDRSB r4, [sp, #4]
2802		MOV r5, #'x'
2803		CMP r4, r5
2804		MOVEQ r4, #1
2805		MOVNE r4, #0
2806		CMP r4, #0
2807		BEQ L88
2808		MOV r4, #'o'
2809		MOV r0, r4
2810		B L89
2811	L88:
2812		LDRSB r4, [sp, #4]
2813		MOV r5, #'o'
2814		CMP r4, r5
2815		MOVEQ r4, #1
2816		MOVNE r4, #0
2817		CMP r4, #0
2818		BEQ L90
2819		MOV r4, #'x'
2820		MOV r0, r4
2821		B L91
2822	L90:
2823		LDR r4, =msg_33
2824		MOV r0, r4
2825		BL p_print_string
2826		BL p_print_ln
2827		LDR r4, =-1
2828		MOV r0, r4
2829		BL exit
2830	L91:
2831	L89:
2832		POP {pc}
2833		.ltorg
2834	f_symbolAt:
2835		PUSH {lr}
2836		SUB sp, sp, #5
2837		LDR r4, =0
2838		STR r4, [sp, #1]
2839		LDR r4, [sp, #13]
2840		LDR r5, =2
2841		CMP r4, r5
2842		MOVLE r4, #1
2843		MOVGT r4, #0
2844		CMP r4, #0
2845		BEQ L92
2846		SUB sp, sp, #4
2847		LDR r4, [sp, #13]
2848		MOV r0, r4
2849		BL p_check_null_pointer
2850		LDR r4, [r4]
2851		LDR r4, [r4]
2852		STR r4, [sp]
2853		LDR r4, [sp, #17]
2854		LDR r5, =1
2855		CMP r4, r5
2856		MOVEQ r4, #1
2857		MOVNE r4, #0
2858		CMP r4, #0
2859		BEQ L94
2860		LDR r4, [sp]
2861		MOV r0, r4
2862		BL p_check_null_pointer
2863		LDR r4, [r4]
2864		LDR r4, [r4]
2865		STR r4, [sp, #5]
2866		B L95
2867	L94:
2868		LDR r4, [sp]
2869		MOV r0, r4
2870		BL p_check_null_pointer
2871		LDR r4, [r4, #4]
2872		LDR r4, [r4]
2873		STR r4, [sp, #5]
2874	L95:
2875		ADD sp, sp, #4
2876		B L93
2877	L92:
2878		LDR r4, [sp, #9]
2879		MOV r0, r4
2880		BL p_check_null_pointer
2881		LDR r4, [r4, #4]
2882		LDR r4, [r4]
2883		STR r4, [sp, #1]
2884	L93:
2885		MOV r4, #0
2886		STRB r4, [sp]
2887		LDR r4, [sp, #17]
2888		LDR r5, =2
2889		CMP r4, r5
2890		MOVLE r4, #1
2891		MOVGT r4, #0
2892		CMP r4, #0
2893		BEQ L96
2894		SUB sp, sp, #4
2895		LDR r4, [sp, #5]
2896		MOV r0, r4
2897		BL p_check_null_pointer
2898		LDR r4, [r4]
2899		LDR r4, [r4]
2900		STR r4, [sp]
2901		LDR r4, [sp, #21]
2902		LDR r5, =1
2903		CMP r4, r5
2904		MOVEQ r4, #1
2905		MOVNE r4, #0
2906		CMP r4, #0
2907		BEQ L98
2908		LDR r4, [sp]
2909		MOV r0, r4
2910		BL p_check_null_pointer
2911		LDR r4, [r4]
2912		LDRSB r4, [r4]
2913		STRB r4, [sp, #4]
2914		B L99
2915	L98:
2916		LDR r4, [sp]
2917		MOV r0, r4
2918		BL p_check_null_pointer
2919		LDR r4, [r4, #4]
2920		LDRSB r4, [r4]
2921		STRB r4, [sp, #4]
2922	L99:
2923		ADD sp, sp, #4
2924		B L97
2925	L96:
2926		LDR r4, [sp, #1]
2927		MOV r0, r4
2928		BL p_check_null_pointer
2929		LDR r4, [r4, #4]
2930		LDRSB r4, [r4]
2931		STRB r4, [sp]
2932	L97:
2933		LDRSB r4, [sp]
2934		MOV r0, r4
2935		ADD sp, sp, #5
2936		POP {pc}
2937		.ltorg
2938	f_containEmptyCell:
2939		PUSH {lr}
2940		SUB sp, sp, #19
2941		LDR r4, [sp, #23]
2942		MOV r0, r4
2943		BL p_check_null_pointer
2944		LDR r4, [r4]
2945		LDR r4, [r4]
2946		STR r4, [sp, #15]
2947		LDR r4, [sp, #15]
2948		MOV r0, r4
2949		BL p_check_null_pointer
2950		LDR r4, [r4]
2951		LDR r4, [r4]
2952		STR r4, [sp, #11]
2953		LDR r4, [sp, #15]
2954		MOV r0, r4
2955		BL p_check_null_pointer
2956		LDR r4, [r4, #4]
2957		LDR r4, [r4]
2958		STR r4, [sp, #7]
2959		LDR r4, [sp, #23]
2960		MOV r0, r4
2961		BL p_check_null_pointer
2962		LDR r4, [r4, #4]
2963		LDR r4, [r4]
2964		STR r4, [sp, #3]
2965		LDR r4, [sp, #11]
2966		STR r4, [sp, #-4]!
2967		BL f_containEmptyCellRow
2968		ADD sp, sp, #4
2969		MOV r4, r0
2970		STRB r4, [sp, #2]
2971		LDR r4, [sp, #7]
2972		STR r4, [sp, #-4]!
2973		BL f_containEmptyCellRow
2974		ADD sp, sp, #4
2975		MOV r4, r0
2976		STRB r4, [sp, #1]
2977		LDR r4, [sp, #3]
2978		STR r4, [sp, #-4]!
2979		BL f_containEmptyCellRow
2980		ADD sp, sp, #4
2981		MOV r4, r0
2982		STRB r4, [sp]
2983		LDRSB r4, [sp, #2]
2984		LDRSB r5, [sp, #1]
2985		ORR r4, r4, r5
2986		LDRSB r5, [sp]
2987		ORR r4, r4, r5
2988		MOV r0, r4
2989		ADD sp, sp, #19
2990		POP {pc}
2991		.ltorg
2992	f_containEmptyCellRow:
2993		PUSH {lr}
2994		SUB sp, sp, #7
2995		LDR r4, [sp, #11]
2996		MOV r0, r4
2997		BL p_check_null_pointer
2998		LDR r4, [r4]
2999		LDR r4, [r4]
3000		STR r4, [sp, #3]
3001		LDR r4, [sp, #3]
3002		MOV r0, r4
3003		BL p_check_null_pointer
3004		LDR r4, [r4]
3005		LDRSB r4, [r4]
3006		STRB r4, [sp, #2]
3007		LDR r4, [sp, #3]
3008		MOV r0, r4
3009		BL p_check_null_pointer
3010		LDR r4, [r4, #4]
3011		LDRSB r4, [r4]
3012		STRB r4, [sp, #1]
3013		LDR r4, [sp, #11]
3014		MOV r0, r4
3015		BL p_check_null_pointer
3016		LDR r4, [r4, #4]
3017		LDRSB r4, [r4]
3018		STRB r4, [sp]
3019		LDRSB r4, [sp, #2]
3020		MOV r5, #0
3021		CMP r4, r5
3022		MOVEQ r4, #1
3023		MOVNE r4, #0
3024		LDRSB r5, [sp, #1]
3025		MOV r6, #0
3026		CMP r5, r6
3027		MOVEQ r5, #1
3028		MOVNE r5, #0
3029		ORR r4, r4, r5
3030		LDRSB r5, [sp]
3031		MOV r6, #0
3032		CMP r5, r6
3033		MOVEQ r5, #1
3034		MOVNE r5, #0
3035		ORR r4, r4, r5
3036		MOV r0, r4
3037		ADD sp, sp, #7
3038		POP {pc}
3039		.ltorg
3040	f_hasWon:
3041		PUSH {lr}
3042		SUB sp, sp, #9
3043		LDR r4, =1
3044		STR r4, [sp, #-4]!
3045		LDR r4, =1
3046		STR r4, [sp, #-4]!
3047		LDR r4, [sp, #21]
3048		STR r4, [sp, #-4]!
3049		BL f_symbolAt
3050		ADD sp, sp, #12
3051		MOV r4, r0
3052		STRB r4, [sp, #8]
3053		LDR r4, =2
3054		STR r4, [sp, #-4]!
3055		LDR r4, =1
3056		STR r4, [sp, #-4]!
3057		LDR r4, [sp, #21]
3058		STR r4, [sp, #-4]!
3059		BL f_symbolAt
3060		ADD sp, sp, #12
3061		MOV r4, r0
3062		STRB r4, [sp, #7]
3063		LDR r4, =3
3064		STR r4, [sp, #-4]!
3065		LDR r4, =1
3066		STR r4, [sp, #-4]!
3067		LDR r4, [sp, #21]
3068		STR r4, [sp, #-4]!
3069		BL f_symbolAt
3070		ADD sp, sp, #12
3071		MOV r4, r0
3072		STRB r4, [sp, #6]
3073		LDR r4, =1
3074		STR r4, [sp, #-4]!
3075		LDR r4, =2
3076		STR r4, [sp, #-4]!
3077		LDR r4, [sp, #21]
3078		STR r4, [sp, #-4]!
3079		BL f_symbolAt
3080		ADD sp, sp, #12
3081		MOV r4, r0
3082		STRB r4, [sp, #5]
3083		LDR r4, =2
3084		STR r4, [sp, #-4]!
3085		LDR r4, =2
3086		STR r4, [sp, #-4]!
3087		LDR r4, [sp, #21]
3088		STR r4, [sp, #-4]!
3089		BL f_symbolAt
3090		ADD sp, sp, #12
3091		MOV r4, r0
3092		STRB r4, [sp, #4]
3093		LDR r4, =3
3094		STR r4, [sp, #-4]!
3095		LDR r4, =2
3096		STR r4, [sp, #-4]!
3097		LDR r4, [sp, #21]
3098		STR r4, [sp, #-4]!
3099		BL f_symbolAt
3100		ADD sp, sp, #12
3101		MOV r4, r0
3102		STRB r4, [sp, #3]
3103		LDR r4, =1
3104		STR r4, [sp, #-4]!
3105		LDR r4, =3
3106		STR r4, [sp, #-4]!
3107		LDR r4, [sp, #21]
3108		STR r4, [sp, #-4]!
3109		BL f_symbolAt
3110		ADD sp, sp, #12
3111		MOV r4, r0
3112		STRB r4, [sp, #2]
3113		LDR r4, =2
3114		STR r4, [sp, #-4]!
3115		LDR r4, =3
3116		STR r4, [sp, #-4]!
3117		LDR r4, [sp, #21]
3118		STR r4, [sp, #-4]!
3119		BL f_symbolAt
3120		ADD sp, sp, #12
3121		MOV r4, r0
3122		STRB r4, [sp, #1]
3123		LDR r4, =3
3124		STR r4, [sp, #-4]!
3125		LDR r4, =3
3126		STR r4, [sp, #-4]!
3127		LDR r4, [sp, #21]
3128		STR r4, [sp, #-4]!
3129		BL f_symbolAt
3130		ADD sp, sp, #12
3131		MOV r4, r0
3132		STRB r4, [sp]
3133		LDRSB r4, [sp, #8]
3134		LDRSB r5, [sp, #17]
3135		CMP r4, r5
3136		MOVEQ r4, #1
3137		MOVNE r4, #0
3138		LDRSB r5, [sp, #7]
3139		LDRSB r6, [sp, #17]
3140		CMP r5, r6
3141		MOVEQ r5, #1
3142		MOVNE r5, #0
3143		AND r4, r4, r5
3144		LDRSB r5, [sp, #6]
3145		LDRSB r6, [sp, #17]
3146		CMP r5, r6
3147		MOVEQ r5, #1
3148		MOVNE r5, #0
3149		AND r4, r4, r5
3150		LDRSB r5, [sp, #5]
3151		LDRSB r6, [sp, #17]
3152		CMP r5, r6
3153		MOVEQ r5, #1
3154		MOVNE r5, #0
3155		LDRSB r6, [sp, #4]
3156		LDRSB r7, [sp, #17]
3157		CMP r6, r7
3158		MOVEQ r6, #1
3159		MOVNE r6, #0
3160		AND r5, r5, r6
3161		LDRSB r6, [sp, #3]
3162		LDRSB r7, [sp, #17]
3163		CMP r6, r7
3164		MOVEQ r6, #1
3165		MOVNE r6, #0
3166		AND r5, r5, r6
3167		ORR r4, r4, r5
3168		LDRSB r5, [sp, #2]
3169		LDRSB r6, [sp, #17]
3170		CMP r5, r6
3171		MOVEQ r5, #1
3172		MOVNE r5, #0
3173		LDRSB r6, [sp, #1]
3174		LDRSB r7, [sp, #17]
3175		CMP r6, r7
3176		MOVEQ r6, #1
3177		MOVNE r6, #0
3178		AND r5, r5, r6
3179		LDRSB r6, [sp]
3180		LDRSB r7, [sp, #17]
3181		CMP r6, r7
3182		MOVEQ r6, #1
3183		MOVNE r6, #0
3184		AND r5, r5, r6
3185		ORR r4, r4, r5
3186		LDRSB r5, [sp, #8]
3187		LDRSB r6, [sp, #17]
3188		CMP r5, r6
3189		MOVEQ r5, #1
3190		MOVNE r5, #0
3191		LDRSB r6, [sp, #5]
3192		LDRSB r7, [sp, #17]
3193		CMP r6, r7
3194		MOVEQ r6, #1
3195		MOVNE r6, #0
3196		AND r5, r5, r6
3197		LDRSB r6, [sp, #2]
3198		LDRSB r7, [sp, #17]
3199		CMP r6, r7
3200		MOVEQ r6, #1
3201		MOVNE r6, #0
3202		AND r5, r5, r6
3203		ORR r4, r4, r5
3204		LDRSB r5, [sp, #7]
3205		LDRSB r6, [sp, #17]
3206		CMP r5, r6
3207		MOVEQ r5, #1
3208		MOVNE r5, #0
3209		LDRSB r6, [sp, #4]
3210		LDRSB r7, [sp, #17]
3211		CMP r6, r7
3212		MOVEQ r6, #1
3213		MOVNE r6, #0
3214		AND r5, r5, r6
3215		LDRSB r6, [sp, #1]
3216		LDRSB r7, [sp, #17]
3217		CMP r6, r7
3218		MOVEQ r6, #1
3219		MOVNE r6, #0
3220		AND r5, r5, r6
3221		ORR r4, r4, r5
3222		LDRSB r5, [sp, #6]
3223		LDRSB r6, [sp, #17]
3224		CMP r5, r6
3225		MOVEQ r5, #1
3226		MOVNE r5, #0
3227		LDRSB r6, [sp, #3]
3228		LDRSB r7, [sp, #17]
3229		CMP r6, r7
3230		MOVEQ r6, #1
3231		MOVNE r6, #0
3232		AND r5, r5, r6
3233		LDRSB r6, [sp]
3234		LDRSB r7, [sp, #17]
3235		CMP r6, r7
3236		MOVEQ r6, #1
3237		MOVNE r6, #0
3238		AND r5, r5, r6
3239		ORR r4, r4, r5
3240		LDRSB r5, [sp, #8]
3241		LDRSB r6, [sp, #17]
3242		CMP r5, r6
3243		MOVEQ r5, #1
3244		MOVNE r5, #0
3245		LDRSB r6, [sp, #4]
3246		LDRSB r7, [sp, #17]
3247		CMP r6, r7
3248		MOVEQ r6, #1
3249		MOVNE r6, #0
3250		AND r5, r5, r6
3251		LDRSB r6, [sp]
3252		LDRSB r7, [sp, #17]
3253		CMP r6, r7
3254		MOVEQ r6, #1
3255		MOVNE r6, #0
3256		AND r5, r5, r6
3257		ORR r4, r4, r5
3258		LDRSB r5, [sp, #6]
3259		LDRSB r6, [sp, #17]
3260		CMP r5, r6
3261		MOVEQ r5, #1
3262		MOVNE r5, #0
3263		LDRSB r6, [sp, #4]
3264		LDRSB r7, [sp, #17]
3265		CMP r6, r7
3266		MOVEQ r6, #1
3267		MOVNE r6, #0
3268		AND r5, r5, r6
3269		LDRSB r6, [sp, #2]
3270		LDRSB r7, [sp, #17]
3271		CMP r6, r7
3272		MOVEQ r6, #1
3273		MOVNE r6, #0
3274		AND r5, r5, r6
3275		ORR r4, r4, r5
3276		MOV r0, r4
3277		ADD sp, sp, #9
3278		POP {pc}
3279		.ltorg
3280	f_allocateNewBoard:
3281		PUSH {lr}
3282		SUB sp, sp, #20
3283		BL f_allocateNewRow
3284		MOV r4, r0
3285		STR r4, [sp, #16]
3286		BL f_allocateNewRow
3287		MOV r4, r0
3288		STR r4, [sp, #12]
3289		BL f_allocateNewRow
3290		MOV r4, r0
3291		STR r4, [sp, #8]
3292		LDR r0, =8
3293		BL malloc
3294		MOV r4, r0
3295		LDR r5, [sp, #16]
3296		LDR r0, =4
3297		BL malloc
3298		STR r5, [r0]
3299		STR r0, [r4]
3300		LDR r5, [sp, #12]
3301		LDR r0, =4
3302		BL malloc
3303		STR r5, [r0]
3304		STR r0, [r4, #4]
3305		STR r4, [sp, #4]
3306		LDR r0, =8
3307		BL malloc
3308		MOV r4, r0
3309		LDR r5, [sp, #4]
3310		LDR r0, =4
3311		BL malloc
3312		STR r5, [r0]
3313		STR r0, [r4]
3314		LDR r5, [sp, #8]
3315		LDR r0, =4
3316		BL malloc
3317		STR r5, [r0]
3318		STR r0, [r4, #4]
3319		STR r4, [sp]
3320		LDR r4, [sp]
3321		MOV r0, r4
3322		ADD sp, sp, #20
3323		POP {pc}
3324		.ltorg
3325	f_allocateNewRow:
3326		PUSH {lr}
3327		SUB sp, sp, #8
3328		LDR r0, =8
3329		BL malloc
3330		MOV r4, r0
3331		MOV r5, #0
3332		LDR r0, =1
3333		BL malloc
3334		STRB r5, [r0]
3335		STR r0, [r4]
3336		MOV r5, #0
3337		LDR r0, =1
3338		BL malloc
3339		STRB r5, [r0]
3340		STR r0, [r4, #4]
3341		STR r4, [sp, #4]
3342		LDR r0, =8
3343		BL malloc
3344		MOV r4, r0
3345		LDR r5, [sp, #4]
3346		LDR r0, =4
3347		BL malloc
3348		STR r5, [r0]
3349		STR r0, [r4]
3350		MOV r5, #0
3351		LDR r0, =1
3352		BL malloc
3353		STRB r5, [r0]
3354		STR r0, [r4, #4]
3355		STR r4, [sp]
3356		LDR r4, [sp]
3357		MOV r0, r4
3358		ADD sp, sp, #8
3359		POP {pc}
3360		.ltorg
3361	f_freeBoard:
3362		PUSH {lr}
3363		SUB sp, sp, #17
3364		LDR r4, [sp, #21]
3365		MOV r0, r4
3366		BL p_check_null_pointer
3367		LDR r4, [r4]
3368		LDR r4, [r4]
3369		STR r4, [sp, #13]
3370		LDR r4, [sp, #13]
3371		MOV r0, r4
3372		BL p_check_null_pointer
3373		LDR r4, [r4]
3374		LDR r4, [r4]
3375		STR r4, [sp, #9]
3376		LDR r4, [sp, #13]
3377		MOV r0, r4
3378		BL p_check_null_pointer
3379		LDR r4, [r4, #4]
3380		LDR r4, [r4]
3381		STR r4, [sp, #5]
3382		LDR r4, [sp, #21]
3383		MOV r0, r4
3384		BL p_check_null_pointer
3385		LDR r4, [r4, #4]
3386		LDR r4, [r4]
3387		STR r4, [sp, #1]
3388		LDR r4, [sp, #9]
3389		STR r4, [sp, #-4]!
3390		BL f_freeRow
3391		ADD sp, sp, #4
3392		MOV r4, r0
3393		STRB r4, [sp]
3394		LDR r4, [sp, #5]
3395		STR r4, [sp, #-4]!
3396		BL f_freeRow
3397		ADD sp, sp, #4
3398		MOV r4, r0
3399		STRB r4, [sp]
3400		LDR r4, [sp, #1]
3401		STR r4, [sp, #-4]!
3402		BL f_freeRow
3403		ADD sp, sp, #4
3404		MOV r4, r0
3405		STRB r4, [sp]
3406		LDR r4, [sp, #13]
3407		MOV r0, r4
3408		BL p_free_pair
3409		LDR r4, [sp, #21]
3410		MOV r0, r4
3411		BL p_free_pair
3412		MOV r4, #1
3413		MOV r0, r4
3414		ADD sp, sp, #17
3415		POP {pc}
3416		.ltorg
3417	f_freeRow:
3418		PUSH {lr}
3419		SUB sp, sp, #4
3420		LDR r4, [sp, #8]
3421		MOV r0, r4
3422		BL p_check_null_pointer
3423		LDR r4, [r4]
3424		LDR r4, [r4]
3425		STR r4, [sp]
3426		LDR r4, [sp]
3427		MOV r0, r4
3428		BL p_free_pair
3429		LDR r4, [sp, #8]
3430		MOV r0, r4
3431		BL p_free_pair
3432		MOV r4, #1
3433		MOV r0, r4
3434		ADD sp, sp, #4
3435		POP {pc}
3436		.ltorg
3437	f_printAiData:
3438		PUSH {lr}
3439		SUB sp, sp, #9
3440		LDR r4, [sp, #13]
3441		MOV r0, r4
3442		BL p_check_null_pointer
3443		LDR r4, [r4]
3444		LDR r4, [r4]
3445		STR r4, [sp, #5]
3446		LDR r4, [sp, #13]
3447		MOV r0, r4
3448		BL p_check_null_pointer
3449		LDR r4, [r4, #4]
3450		LDR r4, [r4]
3451		STR r4, [sp, #1]
3452		LDR r4, [sp, #1]
3453		STR r4, [sp, #-4]!
3454		BL f_printStateTreeRecursively
3455		ADD sp, sp, #4
3456		MOV r4, r0
3457		STRB r4, [sp]
3458		LDR r4, =0
3459		MOV r0, r4
3460		BL exit
3461		POP {pc}
3462		.ltorg
3463	f_printStateTreeRecursively:
3464		PUSH {lr}
3465		LDR r4, [sp, #4]
3466		LDR r5, =0
3467		CMP r4, r5
3468		MOVEQ r4, #1
3469		MOVNE r4, #0
3470		CMP r4, #0
3471		BEQ L100
3472		MOV r4, #1
3473		MOV r0, r4
3474		B L101
3475	L100:
3476		SUB sp, sp, #17
3477		LDR r4, [sp, #21]
3478		MOV r0, r4
3479		BL p_check_null_pointer
3480		LDR r4, [r4]
3481		LDR r4, [r4]
3482		STR r4, [sp, #13]
3483		LDR r4, [sp, #13]
3484		MOV r0, r4
3485		BL p_check_null_pointer
3486		LDR r4, [r4]
3487		LDR r4, [r4]
3488		STR r4, [sp, #9]
3489		LDR r4, [sp, #13]
3490		MOV r0, r4
3491		BL p_check_null_pointer
3492		LDR r4, [r4, #4]
3493		LDR r4, [r4]
3494		STR r4, [sp, #5]
3495		LDR r4, [sp, #21]
3496		MOV r0, r4
3497		BL p_check_null_pointer
3498		LDR r4, [r4, #4]
3499		LDR r4, [r4]
3500		STR r4, [sp, #1]
3501		MOV r4, #'v'
3502		MOV r0, r4
3503		BL putchar
3504		MOV r4, #'='
3505		MOV r0, r4
3506		BL putchar
3507		LDR r4, [sp, #1]
3508		MOV r0, r4
3509		BL p_print_int
3510		BL p_print_ln
3511		LDR r4, [sp, #9]
3512		STR r4, [sp, #-4]!
3513		BL f_printBoard
3514		ADD sp, sp, #4
3515		MOV r4, r0
3516		STRB r4, [sp]
3517		LDR r4, [sp, #5]
3518		STR r4, [sp, #-4]!
3519		BL f_printChildrenStateTree
3520		ADD sp, sp, #4
3521		MOV r4, r0
3522		STRB r4, [sp]
3523		MOV r4, #'p'
3524		MOV r0, r4
3525		BL putchar
3526		BL p_print_ln
3527		MOV r4, #1
3528		MOV r0, r4
3529		ADD sp, sp, #17
3530		ADD sp, sp, #17
3531	L101:
3532		POP {pc}
3533		.ltorg
3534	f_printChildrenStateTree:
3535		PUSH {lr}
3536		SUB sp, sp, #17
3537		LDR r4, [sp, #21]
3538		MOV r0, r4
3539		BL p_check_null_pointer
3540		LDR r4, [r4]
3541		LDR r4, [r4]
3542		STR r4, [sp, #13]
3543		LDR r4, [sp, #13]
3544		MOV r0, r4
3545		BL p_check_null_pointer
3546		LDR r4, [r4]
3547		LDR r4, [r4]
3548		STR r4, [sp, #9]
3549		LDR r4, [sp, #13]
3550		MOV r0, r4
3551		BL p_check_null_pointer
3552		LDR r4, [r4, #4]
3553		LDR r4, [r4]
3554		STR r4, [sp, #5]
3555		LDR r4, [sp, #21]
3556		MOV r0, r4
3557		BL p_check_null_pointer
3558		LDR r4, [r4, #4]
3559		LDR r4, [r4]
3560		STR r4, [sp, #1]
3561		LDR r4, [sp, #9]
3562		STR r4, [sp, #-4]!
3563		BL f_printChildrenStateTreeRow
3564		ADD sp, sp, #4
3565		MOV r4, r0
3566		STRB r4, [sp]
3567		LDR r4, [sp, #5]
3568		STR r4, [sp, #-4]!
3569		BL f_printChildrenStateTreeRow
3570		ADD sp, sp, #4
3571		MOV r4, r0
3572		STRB r4, [sp]
3573		LDR r4, [sp, #1]
3574		STR r4, [sp, #-4]!
3575		BL f_printChildrenStateTreeRow
3576		ADD sp, sp, #4
3577		MOV r4, r0
3578		STRB r4, [sp]
3579		MOV r4, #1
3580		MOV r0, r4
3581		ADD sp, sp, #17
3582		POP {pc}
3583		.ltorg
3584	f_printChildrenStateTreeRow:
3585		PUSH {lr}
3586		SUB sp, sp, #17
3587		LDR r4, [sp, #21]
3588		MOV r0, r4
3589		BL p_check_null_pointer
3590		LDR r4, [r4]
3591		LDR r4, [r4]
3592		STR r4, [sp, #13]
3593		LDR r4, [sp, #13]
3594		MOV r0, r4
3595		BL p_check_null_pointer
3596		LDR r4, [r4]
3597		LDR r4, [r4]
3598		STR r4, [sp, #9]
3599		LDR r4, [sp, #13]
3600		MOV r0, r4
3601		BL p_check_null_pointer
3602		LDR r4, [r4, #4]
3603		LDR r4, [r4]
3604		STR r4, [sp, #5]
3605		LDR r4, [sp, #21]
3606		MOV r0, r4
3607		BL p_check_null_pointer
3608		LDR r4, [r4, #4]
3609		LDR r4, [r4]
3610		STR r4, [sp, #1]
3611		LDR r4, [sp, #9]
3612		STR r4, [sp, #-4]!
3613		BL f_printStateTreeRecursively
3614		ADD sp, sp, #4
3615		MOV r4, r0
3616		STRB r4, [sp]
3617		LDR r4, [sp, #5]
3618		STR r4, [sp, #-4]!
3619		BL f_printStateTreeRecursively
3620		ADD sp, sp, #4
3621		MOV r4, r0
3622		STRB r4, [sp]
3623		LDR r4, [sp, #1]
3624		STR r4, [sp, #-4]!
3625		BL f_printStateTreeRecursively
3626		ADD sp, sp, #4
3627		MOV r4, r0
3628		STRB r4, [sp]
3629		MOV r4, #1
3630		MOV r0, r4
3631		ADD sp, sp, #17
3632		POP {pc}
3633		.ltorg
3634	main:
3635		PUSH {lr}
3636		SUB sp, sp, #17
3637		BL f_chooseSymbol
3638		MOV r4, r0
3639		STRB r4, [sp, #16]
3640		LDRSB r4, [sp, #16]
3641		STRB r4, [sp, #-1]!
3642		BL f_oppositeSymbol
3643		ADD sp, sp, #1
3644		MOV r4, r0
3645		STRB r4, [sp, #15]
3646		MOV r4, #'x'
3647		STRB r4, [sp, #14]
3648		BL f_allocateNewBoard
3649		MOV r4, r0
3650		STR r4, [sp, #10]
3651		LDR r4, =msg_34
3652		MOV r0, r4
3653		BL p_print_string
3654		BL p_print_ln
3655		LDRSB r4, [sp, #15]
3656		STRB r4, [sp, #-1]!
3657		BL f_initAI
3658		ADD sp, sp, #1
3659		MOV r4, r0
3660		STR r4, [sp, #6]
3661		LDR r4, =0
3662		STR r4, [sp, #2]
3663		MOV r4, #0
3664		STRB r4, [sp, #1]
3665		LDR r4, [sp, #10]
3666		STR r4, [sp, #-4]!
3667		BL f_printBoard
3668		ADD sp, sp, #4
3669		MOV r4, r0
3670		STRB r4, [sp]
3671		B L102
3672	L103:
3673		SUB sp, sp, #5
3674		LDR r0, =12
3675		BL malloc
3676		MOV r4, r0
3677		LDR r5, =0
3678		STR r5, [r4, #4]
3679		LDR r5, =0
3680		STR r5, [r4, #8]
3681		LDR r5, =2
3682		STR r5, [r4]
3683		STR r4, [sp, #1]
3684		LDR r4, [sp, #1]
3685		STR r4, [sp, #-4]!
3686		LDR r4, [sp, #15]
3687		STR r4, [sp, #-4]!
3688		LDRSB r4, [sp, #29]
3689		STRB r4, [sp, #-1]!
3690		LDRSB r4, [sp, #28]
3691		STRB r4, [sp, #-1]!
3692		LDR r4, [sp, #25]
3693		STR r4, [sp, #-4]!
3694		BL f_askForAMove
3695		ADD sp, sp, #14
3696		MOV r4, r0
3697		STRB r4, [sp, #5]
3698		ADD r4, sp, #1
3699		LDR r5, =1
3700		LDR r4, [r4]
3701		MOV r0, r5
3702		MOV r1, r4
3703		BL p_check_array_bounds
3704		ADD r4, r4, #4
3705		ADD r4, r4, r5, LSL #2
3706		LDR r4, [r4]
3707		STR r4, [sp, #-4]!
3708		ADD r4, sp, #5
3709		LDR r5, =0
3710		LDR r4, [r4]
3711		MOV r0, r5
3712		MOV r1, r4
3713		BL p_check_array_bounds
3714		ADD r4, r4, #4
3715		ADD r4, r4, r5, LSL #2
3716		LDR r4, [r4]
3717		STR r4, [sp, #-4]!
3718		LDRSB r4, [sp, #27]
3719		STRB r4, [sp, #-1]!
3720		LDR r4, [sp, #24]
3721		STR r4, [sp, #-4]!
3722		BL f_placeMove
3723		ADD sp, sp, #13
3724		MOV r4, r0
3725		STRB r4, [sp, #5]
3726		ADD r4, sp, #1
3727		LDR r5, =1
3728		LDR r4, [r4]
3729		MOV r0, r5
3730		MOV r1, r4
3731		BL p_check_array_bounds
3732		ADD r4, r4, #4
3733		ADD r4, r4, r5, LSL #2
3734		LDR r4, [r4]
3735		STR r4, [sp, #-4]!
3736		ADD r4, sp, #5
3737		LDR r5, =0
3738		LDR r4, [r4]
3739		MOV r0, r5
3740		MOV r1, r4
3741		BL p_check_array_bounds
3742		ADD r4, r4, #4
3743		ADD r4, r4, r5, LSL #2
3744		LDR r4, [r4]
3745		STR r4, [sp, #-4]!
3746		LDR r4, [sp, #19]
3747		STR r4, [sp, #-4]!
3748		LDRSB r4, [sp, #33]
3749		STRB r4, [sp, #-1]!
3750		LDRSB r4, [sp, #32]
3751		STRB r4, [sp, #-1]!
3752		LDR r4, [sp, #29]
3753		STR r4, [sp, #-4]!
3754		BL f_notifyMove
3755		ADD sp, sp, #18
3756		MOV r4, r0
3757		STRB r4, [sp, #5]
3758		LDR r4, [sp, #15]
3759		STR r4, [sp, #-4]!
3760		BL f_printBoard
3761		ADD sp, sp, #4
3762		MOV r4, r0
3763		STRB r4, [sp, #5]
3764		LDRSB r4, [sp, #19]
3765		STRB r4, [sp, #-1]!
3766		LDR r4, [sp, #16]
3767		STR r4, [sp, #-4]!
3768		BL f_hasWon
3769		ADD sp, sp, #5
3770		MOV r4, r0
3771		STRB r4, [sp]
3772		LDRSB r4, [sp]
3773		CMP r4, #0
3774		BEQ L104
3775		LDRSB r4, [sp, #19]
3776		STRB r4, [sp, #6]
3777		B L105
3778	L104:
3779	L105:
3780		LDRSB r4, [sp, #19]
3781		STRB r4, [sp, #-1]!
3782		BL f_oppositeSymbol
3783		ADD sp, sp, #1
3784		MOV r4, r0
3785		STRB r4, [sp, #19]
3786		LDR r4, [sp, #7]
3787		LDR r5, =1
3788		ADDS r4, r4, r5
3789		BLVS p_throw_overflow_error
3790		STR r4, [sp, #7]
3791		ADD sp, sp, #5
3792	L102:
3793		LDRSB r4, [sp, #1]
3794		MOV r5, #0
3795		CMP r4, r5
3796		MOVEQ r4, #1
3797		MOVNE r4, #0
3798		LDR r5, [sp, #2]
3799		LDR r6, =9
3800		CMP r5, r6
3801		MOVLT r5, #1
3802		MOVGE r5, #0
3803		AND r4, r4, r5
3804		CMP r4, #1
3805		BEQ L103
3806		LDR r4, [sp, #10]
3807		STR r4, [sp, #-4]!
3808		BL f_freeBoard
3809		ADD sp, sp, #4
3810		MOV r4, r0
3811		STRB r4, [sp]
3812		LDR r4, [sp, #6]
3813		STR r4, [sp, #-4]!
3814		BL f_destroyAI
3815		ADD sp, sp, #4
3816		MOV r4, r0
3817		STRB r4, [sp]
3818		LDRSB r4, [sp, #1]
3819		MOV r5, #0
3820		CMP r4, r5
3821		MOVNE r4, #1
3822		MOVEQ r4, #0
3823		CMP r4, #0
3824		BEQ L106
3825		LDRSB r4, [sp, #1]
3826		MOV r0, r4
3827		BL putchar
3828		LDR r4, =msg_35
3829		MOV r0, r4
3830		BL p_print_string
3831		BL p_print_ln
3832		B L107
3833	L106:
3834		LDR r4, =msg_36
3835		MOV r0, r4
3836		BL p_print_string
3837		BL p_print_ln
3838	L107:
3839		ADD sp, sp, #17
3840		LDR r0, =0
3841		POP {pc}
3842		.ltorg
3843	p_print_string:
3844		PUSH {lr}
3845		LDR r1, [r0]
3846		ADD r2, r0, #4
3847		LDR r0, =msg_37
3848		ADD r0, r0, #4
3849		BL printf
3850		MOV r0, #0
3851		BL fflush
3852		POP {pc}
3853	p_print_ln:
3854		PUSH {lr}
3855		LDR r0, =msg_38
3856		ADD r0, r0, #4
3857		BL puts
3858		MOV r0, #0
3859		BL fflush
3860		POP {pc}
3861	p_read_char:
3862		PUSH {lr}
3863		MOV r1, r0
3864		LDR r0, =msg_39
3865		ADD r0, r0, #4
3866		BL scanf
3867		POP {pc}
3868	p_check_null_pointer:
3869		PUSH {lr}
3870		CMP r0, #0
3871		LDREQ r0, =msg_40
3872		BLEQ p_throw_runtime_error
3873		POP {pc}
3874	p_read_int:
3875		PUSH {lr}
3876		MOV r1, r0
3877		LDR r0, =msg_41
3878		ADD r0, r0, #4
3879		BL scanf
3880		POP {pc}
3881	p_check_array_bounds:
3882		PUSH {lr}
3883		CMP r0, #0
3884		LDRLT r0, =msg_42
3885		BLLT p_throw_runtime_error
3886		LDR r1, [r1]
3887		CMP r0, r1
3888		LDRCS r0, =msg_43
3889		BLCS p_throw_runtime_error
3890		POP {pc}
3891	p_print_int:
3892		PUSH {lr}
3893		MOV r1, r0
3894		LDR r0, =msg_44
3895		ADD r0, r0, #4
3896		BL printf
3897		MOV r0, #0
3898		BL fflush
3899		POP {pc}
3900	p_free_pair:
3901		PUSH {lr}
3902		CMP r0, #0
3903		LDREQ r0, =msg_45
3904		BEQ p_throw_runtime_error
3905		PUSH {r0}
3906		LDR r0, [r0]
3907		BL free
3908		LDR r0, [sp]
3909		LDR r0, [r0, #4]
3910		BL free
3911		POP {r0}
3912		BL free
3913		POP {pc}
3914	p_throw_overflow_error:
3915		LDR r0, =msg_46
3916		BL p_throw_runtime_error
3917	p_throw_runtime_error:
3918		BL p_print_string
3919		MOV r0, #-1
3920		BL exit
3921	
===========================================================
-- Finished

