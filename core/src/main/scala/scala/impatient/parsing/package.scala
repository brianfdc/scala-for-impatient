package scala.impatient

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

import java.text.ParseException

import org.joda.time._
import org.joda.time.format._

/**
 * Parsing exercises from chapter 19
 */
package object parsing {
  def padInt(sb: StringBuilder, n: Long, digits: Int): StringBuilder = {
    val str = n.toString
    var delta = digits - str.length
    
    assert(delta >= 0, "Not enough digits to pad number: " + n + " on " + digits + " digits")
    
    while (delta > 0) {
      sb.append('0')
      delta = delta - 1
    }
    sb.append(str)
  }
}

package parsing {
  /**
   * (19.1)
   * BNF of our parser:
   * 
   * expr   ::= term {'+' term | '-' term}
   * term   ::= factor {'*' factor | '/' factor}
   * factor ::= floatingPointNumber | '(' expr ')'
   */
  class HorstmannArithmeticParser extends JavaTokenParsers {
    def expr: Parser[Int] = {
      (term ~ opt(( "+" | "-") ~ expr) ) ^^ {
        case t ~ None => t
        case t ~ Some("+" ~ e) => t + e
        case t ~ Some("-" ~ e) => t - e
      }
    }
    def term: Parser[Int] = {
      (factor ~ rep("*" ~> term)) ^^ {
        case f ~ r => f * r.product
      }
    }
    def factor: Parser[Int] = {
      wholeNumber ^^ (_.toInt ) | "(" ~> expr <~ ")"
    }
    def parse(text: String): Int = {
      parseAll(expr, text) match {
        case Success(result, _) => result
        case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
      }
    }
  }
  
  /**
   * (19.6)
   * see scala.calc.dsl
   */
  trait Expr
  case class Number(value: Int) extends Expr
  case class Operator(op: String, left: Expr, right: Expr) extends Expr
  
  class IntermediateExprParser extends RegexParsers {
    val number = "[0-9]+".r
    def expr: Parser[Expr] = {
      (term ~ opt(( "+" | "-") ~ expr) ) ^^ {
        case a ~ None => a
        case a ~ Some(op ~ b) => Operator(op, a, b)
      }
    }
    def term: Parser[Expr] = {
      (factor ~ opt("*" ~> term)) ^^ {
        case a ~ None => a
        case a ~ Some(b) => Operator("*", a, b)
      }
    }
    def factor: Parser[Expr] = {
      number ^^ (n => Number(n.toInt)) | "(" ~> expr <~ ")"
    }
    def parse(text: String): Expr = {
      parseAll(expr, text) match {
        case Success(result, _) => result
        case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
      }
    }
  }
  
  
  /**
   * (19.3)
   */
  class CSVParser extends JavaTokenParsers {
    def value: Parser[Int] = {
      wholeNumber ^^ { _.toInt } | failure("failed to parse whole number") | err("erroneously parsed a whole number")
    }
    def integers: Parser[List[Int]] = {
     log(integers)("integers")
     repsep(value, ",") ^^ {
       case Nil => List[Int]()
       case head::tail => head.toInt :: tail.map( _.toInt )
     }
    }
    def parse(text: String): List[Int] = {
      parseAll(integers, text) match {
        case Success(result, _) => result
        case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
      }
    }
  }

}