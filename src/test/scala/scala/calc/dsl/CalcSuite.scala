package scala.calc.dsl

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.calc.dsl._

@RunWith(classOf[JUnitRunner])
class CalcSuite extends FunSuite with BeforeAndAfter {
  test("test AST") {
    val n1 = Number(5)
    expect(5) {
      n1.value
    }
  }
    
  test("equality test") {
    val binop = BinaryOp("+", Number(5), Number(10))
    expect(Number(5)) {
      binop.left
    }
    expect(Number(10)) {
      binop.right
    }
    expect("+") {
      binop.operator
    }
  }

  test("parse number") {
    expect(Number(5)) {
      Calc.parse("5")
    }
    expect(Number(5)) {
      Calc.parse("5.0")
    }
  }
  
  test("many ArithParser parses") {
    val expressions = List(
        "5",
        "(5)",
        "5 + 5",
        "(5 + 5)",
        "5 + 5 + 5",
        "(5 + 5) + 5",
        "(5 + 5) + (5 + 5)",
        "(5 * 5) / (5 * 5)",
        "5 - 5",
        "5 - 5 - 5",
        "(5 - 5) - 5",
        "5 * 5 * 5",
        "5 / 5 / 5",
        "(5 / 5) / 5"
    )
    
    expressions.foreach{ (x) =>
      println(x + " = " + ArithParser.parse(x))
    }
  }
  
  test("ExprParser") {
    expect( Number(5) ) {
      Calc.parse("5")
    }
    expect( Number(5) ) {
      Calc.parse("(5)")
    }
    expect( BinaryOp("+", Number(5), Number(5)) ) {
      Calc.parse("5 + 5")
    }
    expect( BinaryOp("+", Number(5), Number(5)) ) {
      Calc.parse("(5 + 5)")
    }
    expect( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),Number(5) ) ) {
      Calc.parse("(5 + 5) + 5")
    }
    expect( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),BinaryOp("+", Number(5), Number(5)) ) ) {
      Calc.parse("(5 + 5) + (5 + 5)")
    }
  }
  
  test("Simple addition test") {
    expect( 2.0 ) {
      Calc.evaluate("1 + 1")
    }
  }
  test("Simple exponentiation test") {
    expect( 16.0 ) {
      Calc.evaluate("4 ^ 2")
    }
  }
  test("Complex exponentiation test") {
    expect( 16.0 ) {
      Calc.evaluate("(2 + 2) ^ (4 / 2)")
    }
  }
}