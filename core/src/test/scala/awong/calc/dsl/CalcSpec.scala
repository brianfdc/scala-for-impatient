package awong.calc.dsl

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.AbstractFlatSpec

import awong.calc.dsl._

@RunWith(classOf[JUnitRunner])
class CalcSpec extends AbstractFlatSpec {
  it should "render AST simply" in {
    val n1 = Number(5)
    expectResult(5) {
      n1.value
    }
  }
    
  it should "equality test" in {
    val binop = BinaryOp("+", Number(5), Number(10))
    expectResult(Number(5)) {
      binop.left
    }
    expectResult(Number(10)) {
      binop.right
    }
    expectResult("+") {
      binop.operator
    }
  }

  it should "parse number" in {
    expectResult(Number(5)) {
      Calc.parse("5")
    }
    expectResult(Number(5)) {
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
    val parser = new ArithParser
    expressions.foreach{ (x) =>
      println(x + " = " + parser.parse(x))
    }
  }
  
  it should " run many ExprParser" in {
    expectResult( Number(5) ) {
      Calc.parse("5")
    }
    expectResult( Number(5) ) {
      Calc.parse("(5)")
    }
    expectResult( BinaryOp("+", Number(5), Number(5)) ) {
      Calc.parse("5 + 5")
    }
    expectResult( BinaryOp("+", Number(5), Number(5)) ) {
      Calc.parse("(5 + 5)")
    }
    expectResult( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),Number(5) ) ) {
      Calc.parse("(5 + 5) + 5")
    }
    expectResult( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),BinaryOp("+", Number(5), Number(5)) ) ) {
      Calc.parse("(5 + 5) + (5 + 5)")
    }
  }
  
  it should "do simple addition" in {
    expectResult( 2.0 ) {
      Calc.evaluate("1 + 1")
    }
  }
  
  it should "do simple exponentiation" in {
    expectResult( 16.0 ) {
      Calc.evaluate("4 ^ 2")
    }
  }
  it should "do complex exponentiation" in {
    expectResult( 16.0 ) {
      Calc.evaluate("(2 + 2) ^ (4 / 2)")
    }
  }
}