package scala.calc.dsl

import scala.util.parsing.combinator._

/**
 * @author Ted Neward
 * @see http://www.ibm.com/developerworks/java/library/j-scala08268/index.html
 * @see http://www.ibm.com/developerworks/java/library/j-scala10248/index.html
 * @see http://www.ibm.com/developerworks/java/library/j-scala11218/index.html
 */
object Calc {
  def parse(text : String) = {
    val results = ExprParser.parse(text)
    println("parsed " + text + " as " + results + " which is a type " + results.getClass())
    results.get
  }

  /**
   * Interprets our language
   */
  def evaluate(text: String) : Double = {
    evaluate(parse(text))
  }
  def evaluate(expr: Expr) : Double = {
    import scala.math._
    simplify(expr) match {
      case Number(x) => x
      case UnaryOp("-", x) => -(evaluate(x))
      case BinaryOp("+", x1, x2) => (evaluate(x1) + evaluate(x2))
      case BinaryOp("-", x1, x2) => (evaluate(x1) - evaluate(x2))
      case BinaryOp("*", x1, x2) => (evaluate(x1) * evaluate(x2))
      case BinaryOp("/", x1, x2) => (evaluate(x1) / evaluate(x2))
      case BinaryOp("^", x1, x2) => pow(evaluate(x1), evaluate(x2))
    }
  }
  
  /**
   * simplify the "edges" of the tree (the operands inside each of the expressions, if any)
   * first, then take the resulting simplification and simplify the top-level expression
   */
  def simplify(expr: Expr): Expr = {
      // first simplify the subexpressions
      val simpSubs = expr match {
        // Ask each side to simplify
        case BinaryOp(op, left, right) => BinaryOp(op, simplify(left), simplify(right))
        // Ask the operand to simplify
        case UnaryOp(op, operand) => UnaryOp(op, simplify(operand))
        // Anything else doesn't have complexity (no operands to simplify)
        case _ => expr
      }
      simplifyTop(simpSubs)
  }

  def simplifyTop(expr : Expr) : Expr = {
      expr match {
        // Double negation returns the original value
        case UnaryOp("-", UnaryOp("-", x)) => x
        // Positive returns the original value
        case UnaryOp("+", x) => x
        // Multiplying x by 1 returns the original value
        case BinaryOp("*", x, Number(1)) => x
        // Multiplying 1 by x returns the original value
        case BinaryOp("*", Number(1), x) => x
        // Multiplying x by 0 returns zero
        case BinaryOp("*", x, Number(0)) => Number(0)
        // Multiplying 0 by x returns zero
        case BinaryOp("*", Number(0), x) => Number(0)
        // Dividing x by 1 returns the original value
        case BinaryOp("/", x, Number(1)) => x
        // Adding x to 0 returns the original value
        case BinaryOp("+", x, Number(0)) => x
        // Adding 0 to x returns the original value
        case BinaryOp("+", Number(0), x) => x
        // x to the power of 0 returns 1
        case BinaryOp("^", x, Number(0)) => Number(1)
        // x to the power of 1 returns x
        case BinaryOp("^", x, Number(1)) => x
        // 1 to any power returns 1
        case BinaryOp("^", Number(1), x) => Number(1)
        // 0 to any power returns 0
        case BinaryOp("^", Number(0), x) => Number(0)
        // Anything else cannot (yet) be simplified
        case _ => expr
      }
  }
}
/**
 * BNF of our parser:
 * 
 * expr   ::= term {'+' term | '-' term}
 * term   ::= factor {'*' factor | '/' factor}
 * factor ::= floatingPointNumber | '(' expr ')'
 */
object ArithParser extends RegexParsers  {
  import scala.math._
  // clones from JavaTokenParsers
  /** 
   *  Anything starting with an ASCII alphabetic character or underscore,
   *  followed by zero or more repetitions of regex's `\w`.
   */
  val ident = """[a-zA-Z_]\w*""".r
  /** An integer, without sign or with a negative sign. */
  val wholeNumber = """-?\d+""".r
  /**
   *   Number following one of these rules:
   *  - An integer. For example: `13`
   *  - An integer followed by a decimal point. For example: `3.`
   *  - An integer followed by a decimal point and fractional part. For example: `3.14`
   *  - A decimal point followed by a fractional part. For example: `.1`
   */
  val decimalNumber: Parser[String] = """(\d+(\.\d*)?|\d*\.\d+)""".r
  /** 
   *  Double quotes (`"`) enclosing a sequence of:
   *  - Any character except double quotes, control characters or backslash (`\`)
   *  - A backslash followed by another backslash, a single or double quote, or one
   *    of the letters `b`, `f`, `n`, `r` or `t`
   *  - `\` followed by `u` followed by four hexadecimal digits
   */
  val stringLiteral = ("\""+"""([^"\p{Cntrl}\\]|\\[\\'"bfnrt]|\\u[a-fA-F0-9]{4})*"""+"\"").r
  /** 
   *  A number following the rules of `decimalNumber`, with the following
   *  optional additions:
   *  - Preceded by a negative sign
   *  - Followed by `e` or `E` and an optionally signed integer
   *  - Followed by `f`, `f`, `d` or `D` (after the above rule, if both are used)
   */
  val floatingPointNumber = """-?(\d+(\.\d*)?|\d*\.\d+)([eE][+-]?\d+)?[fFdD]?""".r
  def expr: Parser[Double] = {
    (term~"+"~term) ^^ { case lhs~op~rhs => lhs + rhs } |
    (term~"-"~term) ^^ { case lhs~op~rhs => lhs - rhs } |
    term
  }
  def term : Parser[Double] = {
    (factor~"*"~factor) ^^ { case lhs~op~rhs => lhs * rhs    } |
    (factor~"/"~factor) ^^ { case lhs~op~rhs => lhs / rhs    } |
    (factor~"%"~factor) ^^ { case lhs~op~rhs => lhs % rhs    } |
    (factor~"^"~factor) ^^ { case lhs~op~rhs => pow(lhs,rhs) } |
    factor
  }
  def factor : Parser[Double] = {
    "(" ~> expr <~ ")" |
    floatingPointNumber ^^ (_.toDouble )
  }
  
  def parse(text: String) = {
    parseAll(expr, text)
  }
}

/**
 * Define combinators for our parser
 */
object ExprParser extends JavaTokenParsers {
  def expr: Parser[Expr] = {
    (term ~ "+" ~ term) ^^ { case lhs~plus~rhs => BinaryOp("+", lhs, rhs) }  |
    (term ~ "-" ~ term) ^^ { case lhs~minus~rhs => BinaryOp("-", lhs, rhs) } |
    term 
  }
  
  def term: Parser[Expr] = {
    (factor ~ "*" ~ factor) ^^ { case lhs~times~rhs => BinaryOp("*", lhs, rhs) } |
    (factor ~ "/" ~ factor) ^^ { case lhs~div~rhs => BinaryOp("/", lhs, rhs) }   |
    (factor ~ "^" ~ factor) ^^ { case lhs~exp~rhs => BinaryOp("^", lhs, rhs) }   |
    factor
  }

  def factor : Parser[Expr] = {
    "(" ~> expr <~ ")" |
    floatingPointNumber ^^ {x => Number(x.toFloat) }
  }
  def parse(text: String) = parseAll(expr, text)
}
/**
 * Defines our AST
 */
sealed abstract class Expr
case class Number(value: Double) extends Expr
case class UnaryOp(operator: String, arg: Expr) extends Expr
case class BinaryOp(operator: String, left: Expr, right: Expr) extends Expr


