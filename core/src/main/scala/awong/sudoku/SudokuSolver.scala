package awong.sudoku

import awong.LoggingLike
/**
 * Solve a Sudoku puzzle in a purely functional manner using lazy evaluation
 * For alternative, see https://github.com/msmith/sudoku-scala
 * 
 * @see http://www.gregreynolds.co.uk/pure-functional-sudoku-solving-with-scala/
 */
object SudokuSolver extends LoggingLike {
	type SudokuVec = Vector[Char]
	
	/* Setting up some constants.
	 * Note that this program only solves one Sudoku
	 * The data is stored in a one dimensional Vector to allow random access
	 */
	val cellSize = 3
	val rowLength = cellSize * cellSize
	val boardLength = rowLength * rowLength
	val testString = """200000060000075030048090100000300000300010009000008000001020570080730000090000004""";
	val validNumbers = List[Char]('1','2','3','4','5','6','7','8','9')
	val nullMarker = '0'

	// The board is complete if it contains no zeros
	def isDone(board: SudokuVec): Boolean = {
		!board.contains(nullMarker)
	}

	/* 
	 * Setting up the relationships that are important in working out
	 * the constraints.
	 */

	def coordinates(i: Int) : (Int, Int, Int) = {
		def rowIndex(i: Int): Int = {
			i / (cellSize * cellSize)
		}
		
		def colIndex(i: Int): Int = {
			i % (cellSize * cellSize)
		}
		
		def cellIndex(i: Int, row: Int, col: Int): Int = {
			(col / cellSize) + cellSize * (row / cellSize)
		}
		val row = rowIndex(i)
		val col = colIndex(i)
		val cell = cellIndex(i, row, col)
		(row, col, cell)
	}

	/* 
	 * Get a list of all the indexes that are in the same row, column and box as the input index
	 */
	def getRelatedIndexes(i: Int): List[Int] = {
		val (row, col, cell) = coordinates(i)
		val indexes = for {
				j <- 0 until boardLength
				(rowJ, colJ, cellJ) = coordinates(j)
				if ( (row == rowJ || col == colJ || cell == cellJ) && i != j)
			} yield j
		indexes.toList
	}

	/* 
	 * Check to see if a particular character is allowed to be inserted at
	 * a particular index
	 */
	def isLegal(ch: Char, i: Int, board: SudokuVec): Boolean = {
		val currentMarker = board(i)
		if (currentMarker == nullMarker) {
			val vals = for {
					j <- getRelatedIndexes(i)
				} yield board(j)
			!vals.contains(ch)
		} else {
			currentMarker == ch
		}
	}

	// Get a list of all the numbers that can be inserted at the given index
	def getLegalNumbers(i: Int, board: SudokuVec): List[Char] = {
		for {
			ch <- validNumbers
			if (isLegal(ch,i,board))
		} yield ch
	}

	// Format the board as a square for output
	def showBoard(board: SudokuVec): Unit = {
		if (board.length <= rowLength) {
			logger.debug(board.mkString)
		} else {
			logger.debug(board.take(rowLength).mkString)
			showBoard(board.drop(rowLength))
		}
	}

	/* 
	 * Given a particular board setup, create a stream of all the legal moves
	 * possible by updating the first encountered zero element
	 * After each move is generated, the boards are analyzed for elements where only one
	 * element is now possible. Those elements are then updated into the board.
	 * If there are no legal moves, then the stream will be empty, effectively ending this
	 * "branch" of the search
	 */
	def getNextMoves(board: SudokuVec): Stream[SudokuVec] = {
		val index = board.indexOf(nullMarker)
		def buildStream(legalNumbers: Stream[Char]): Stream[SudokuVec] = {
			if (legalNumbers.isEmpty) {
				Stream.empty
			} else {
				propagateConstraints(board.updated(index,legalNumbers.head)) #:: buildStream(legalNumbers.tail)
			}
		}
		buildStream(getLegalNumbers(index,board).toStream)
	}

	/*
	 * Check for elements where only one option is available
	 * The board is updated until no further single element substitutions are possible
	 */
	def propagateConstraints(board: SudokuVec): SudokuVec = {
		def findIndex(i: Int): Int = {
			val ind = board.indexOf(nullMarker,i)
			if (ind == -1) {
				ind
			} else {
				if (getLegalNumbers(ind,board).length == 1) {
					ind
				} else {
					findIndex(ind+1)
				}
			}
		}
		val ind = findIndex(0)
		if (ind == -1) {
			board
		} else {
			propagateConstraints(board.updated(ind,getLegalNumbers(ind,board).head))
		}
	}

	/*
	 * Given a stream of board positions, append the solution streams generated from
	 * each of those boards to the end of the stream. This is evaluated lazily, so not
	 * every board position needs to be evaluated.
	 */
	def buildSolutionList(ls: Stream[SudokuVec]): Stream[SudokuVec] = {
		if (ls.isEmpty) {
			Stream.empty
		} else {
			lazy val updatedSols = ls.tail.append(getNextMoves(ls.head))
			ls.head #:: buildSolutionList(updatedSols)
		}
	}

	// Filter the solution stream to leave only the boards that are complete
	def solve(board: SudokuVec): Stream[SudokuVec] = {
		val initial = List[SudokuVec](board).toStream
		buildSolutionList(initial).filter(p => isDone(p))
	}

	def run(testString: String) = {
		val testList = testString.toVector
		val solution = solve(testList)
		if (solution.isEmpty) {
			logger.info("No Solution")
		} else  {
			showBoard(solution.head)
		}
		logger.info("End")
		
	}
	// Extract the first solution and print it.
	def main(args: Array[String]) = {
		run(testString)
	}
}