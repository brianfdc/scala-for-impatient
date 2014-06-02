package awong.sudoku

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.AbstractWordSpec

@RunWith(classOf[JUnitRunner])
class SudokuSolverSpec extends AbstractWordSpec {
	trait TestBoard {
		val testString = """200000060000075030048090100000300000300010009000008000001020570080730000090000004""";
	}

	"coordinates(i)" should {
		"Return (0,0) in cell 0 for index = 0" in {
			val i = 0
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(0)
			cell should be(0)
		}
		"Return (0,1) in cell 0 for index = 1" in {
			val i = 1
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(1)
			cell should be(0)
		}
		"Return (0,2) in cell 0 for index = 2" in {
			val i = 2
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(2)
			cell should be(0)
		}
		
		"Return (0,3) in cell 1 for index = 3" in {
			val i = 3
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(3)
			cell should be(1)
		}
		"Return (0,4) in cell 1 for index = 4" in {
			val i = 4
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(4)
			cell should be(1)
		}
		"Return (0,5) in cell 1 for index = 5" in {
			val i = 5
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(5)
			cell should be(1)
		}

		"Return (0,6) in cell 1 for index = 6" in {
			val i = 6
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(6)
			cell should be(2)
		}
		"Return (0,7) in cell 1 for index = 7" in {
			val i = 7
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(7)
			cell should be(2)
		}
		"Return (0,8) in cell 1 for index = 8" in {
			val i = 8
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(0)
			col should be(8)
			cell should be(2)
		}		
		
		"Return (0,6) in cell 1 for index = 9" in {
			val i = 9
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(1)
			col should be(0)
			cell should be(0)
		}
		"Return (0,7) in cell 1 for index = 10" in {
			val i = 10
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(1)
			col should be(1)
			cell should be(0)
		}
		"Return (0,8) in cell 1 for index = 11" in {
			val i = 11
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(1)
			col should be(2)
			cell should be(0)
		}
		
		"Return (8,6) in cell 8 for index = 78" in {
			val i = 78
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(8)
			col should be(6)
			cell should be(8)
		}
		"Return (8,7) in cell 8 for index = 79" in {
			val i = 79
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(8)
			col should be(7)
			cell should be(8)
		}
		"Return (8,8) in cell 8 for index = 80" in {
			val i = 80
			val (row, col, cell) = SudokuSolver.coordinates(i)
			row should be(8)
			col should be(8)
			cell should be(8)
		}
	}
	
	"getRelatedIndexes(i)" should {
		"Return (1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 18, 19, 20, 27, 36, 45, 54, 63, 72) for i = 0" in {
			val i = 0
			val related = SudokuSolver.getRelatedIndexes(i)
			related should be(List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 18, 19, 20, 27, 36, 45, 54, 63, 72))
		}
	}
	
	"isLegal(ch: Char, i: Int, board: SudokuVec)" should {
		"Be true for '1' at 1 on testString" in {
			new TestBoard {
				val ch = '1'
				val i = 1
				val board = testString.toVector
				SudokuSolver.isLegal(ch, i, board) should be (true)
			}
			
		}
	}

	"getLegalNumbers(i: Int, board: SudokuVec)" should {
		"Be (2) at 0 on testString" in {
			new TestBoard {
				val i = 0
				val board = testString.toVector
				SudokuSolver.getLegalNumbers(i, board) should be (List('2'))
			}
		}
		"Be (1,3,5,7) at 1 on testString" in {
			new TestBoard {
				val i = 1
				val board = testString.toVector
				SudokuSolver.getLegalNumbers(i, board) should be (List('1', '3', '5', '7'))
			}
		}
		"Be (1,3,5,7) at 2 on testString" in {
			new TestBoard {
				val i = 2
				val board = testString.toVector
				SudokuSolver.getLegalNumbers(i, board) should be (List('3', '5', '7', '9'))
			}
		}
	}

}