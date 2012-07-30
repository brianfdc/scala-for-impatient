package scala.impatient.parsing

import java.text.ParseException

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader


class JsonParser extends JavaTokenParsers with ImplicitConversions {
  def literal: Parser[JValue] = {
    stringLiteral ^^ { case s => JString(s) } |
    wholeNumber ^^ { case f => JInt(f.toInt) } |
    floatingPointNumber ^^ { case f => JDouble(f.toDouble) } |
    "null" ^^ { case _ => JNull } |
    "true" ^^ { case _ => JBool(true) } |
    "false" ^^ { case _ => JBool(false) }
  }
  def value: Parser[JValue] = {
    obj | arr | literal
  }
  def obj: Parser[JObject] = {
    "{" ~> repsep(member,",") <~ "}" ^^ { case ms => JObject(ms) }
  }
  def arr: Parser[JArray] = {
    "[" ~> repsep(value,",") <~ "]" ^^ { case vs => JArray(vs) }
  }
  def member: Parser[JField] = {
    stringLiteral ~ ":" ~ value ^^ {
      case name ~ ":" ~ value => JField(name,value)
    }
  }
  def parse(text: String): JValue = {
    parseAll(value, text) match {
      case Success(result, _) => result
      case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
    }
  }
}

sealed abstract class JValue
case object JNothing extends JValue // 'zero' for JValue
case object JNull extends JValue
case class JString(s: String) extends JValue
case class JDouble(num: Double) extends JValue
case class JInt(num: BigInt) extends JValue
case class JBool(value: Boolean) extends JValue
case class JField(name: String, value: JValue) extends JValue
case class JObject(obj: List[JField]) extends JValue 
case class JArray(arr: List[JValue]) extends JValue