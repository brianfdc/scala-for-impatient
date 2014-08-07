package awong.calc.dsl

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.AbstractFlatSpec

import awong.calc.dsl._

@RunWith(classOf[JUnitRunner])
class CalcSpec extends AbstractFlatSpec {
  it should "render AST simply" in {
    val n1 = Number(5)
    
    n1.value shouldBe 5
  }
    
  it should "equality test" in {
    val binop = BinaryOp("+", Number(5), Number(10))
    binop.left should === (Number(5))
    binop.right should === (Number(10))
    binop.operator should === ("+")
  }

  it should "parse number" in {
    Calc.parse("5") should === (Number(5)) 
    Calc.parse("5.0") should === (Number(5)) 
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
      logger.debug(x + " = " + parser.parse(x))
    }
  }
  
  it should " run many ExprParser" in {
    Calc.parse("5") should === (Number(5))
    Calc.parse("(5)") should === (Number(5))
    Calc.parse("5 + 5") should === (BinaryOp("+", Number(5), Number(5)))
    Calc.parse("(5 + 5)") should === (BinaryOp("+", Number(5), Number(5)))
    Calc.parse("(5 + 5) + 5") should === ( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),Number(5) ) )
    Calc.parse("(5 + 5) + (5 + 5)") should === ( BinaryOp("+",BinaryOp("+", Number(5), Number(5)),BinaryOp("+", Number(5), Number(5)) ) )
  }
  
  it should "do simple addition" in {
    Calc.evaluate("1 + 1") shouldBe (2.0)
  }
  
  it should "do simple exponentiation" in {
    Calc.evaluate("4 ^ 2") shouldBe (16.0)
  }
  it should "do complex exponentiation" in {
    Calc.evaluate("(2 + 2) ^ (4 / 2)") shouldBe (16.0)
  }
}