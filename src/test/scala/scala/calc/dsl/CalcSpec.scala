package scala.calc.dsl

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.calc.dsl._

@RunWith(classOf[JUnitRunner])
class CalcSpec extends FlatSpec with BeforeAndAfter {
  it should "render AST simply" in {
    val n1 = Number(5)
    expect(5) {
      n1.value
    }
  }
    
  it should "equality test" in {
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

  it should "parse number" in {
    expect(Number(5)) {
      Calc.parse("5")
    }
    expect(Number(5)) {
      Calc.parse("5.0")
    }
  }
  
  it should "run many ArithParser parses" in {
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
  
  it should " run many ExprParser" in {
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
  
  it should "do simple addition" in {
    expect( 2.0 ) {
      Calc.evaluate("1 + 1")
    }
  }
  
  it should "do simple exponentiation" in {
    expect( 16.0 ) {
      Calc.evaluate("4 ^ 2")
    }
  }
  it should "do complex exponentiation" in {
    expect( 16.0 ) {
      Calc.evaluate("(2 + 2) ^ (4 / 2)")
    }
  }
}