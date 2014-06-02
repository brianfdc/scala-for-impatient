package awong.impatient.parsing

import java.text.ParseException

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

import org.joda.time._

/**
 * (19.4)
 * See DateParser in http://code.google.com/p/gdata-scala-client/ 
 * 
 * Parse a date that follows RFC 3339 (used by the Atom spec)
 */
class JodaDateTimeParser extends RegexParsers with ImplicitConversions {
  def digit: Parser[Int] = {
    """[0-9]""".r ^^ { _.toInt }
  }
  def sign: Parser[Char] = {
    """[+-]""".r ^^ { _.toCharArray.head }
    //elem('+') | elem('-')
  }
  /** Parse a positive integer literal. */
  def fullYear: Parser[Int] = {
    digit ~ digit ~ digit ~ digit ^^ { case a ~ b ~ c ~ d => a * 1000 + b * 100 + c * 10 + d }
  }
  def twoDigits: Parser[Int] = {
    digit ~ digit ^^ {case a ~ b => a * 10 + b }
  }
  def oneOrTwoDigits: Parser[Int] = {
    digit ~ opt(digit) ^^ {
      case a ~ Some(b) => a * 10 + b
      case a ~ None => a
    }
  }
  
  def month =  {
    twoDigits >> { n => 
      if ((1 to 12).contains(n)) success(n)
      else failure("Invalid month: " + n)
    }
  }
  def day = {
    twoDigits >> { n =>
      if ((1 to 31).contains(n)) success(n)
      else failure("Invalid day of the month: " + n)
    }
  }
  def hour = {
    twoDigits >> { n =>
      if ((0 until 24).contains(n)) success(n)
      else failure("Invalid hour: " + n)
    }
  }
  def minute = {
    twoDigits >> { n =>
      if ((0 to 60).contains(n)) success(n)
      else failure("Invalid minute: " + n)
    }
  }
  def second = {
    twoDigits >> { n =>
      if ((0 to 60).contains(n)) success(n)
      else failure("Invalid second: " + n)
    }
  }
  def secFraction = {
    (elem('.') ~ rep1(digit)) ^^ { 
      case p ~ rest => (p :: rest).mkString("", "", "").toDouble
    }
  }

  case class Offset(sign: Char, hour: Int, minute: Int) {
    def toMillis = {
      val res = (hour * 60 + minute) * 60000 
      if (sign == '-') -res else res
    }
    def toDateTimeZone = {
      val dtz = org.joda.time.DateTimeZone.forOffsetMillis(toMillis)
      dtz
    } 
  }
  object ZuluOffset extends Offset('+', 0, 0)

  def numOffset = {
    sign ~ hour ~ (elem(':') ~> minute) ^^ Offset
  }
  def offset = {
    elem('Z') ^^^ ZuluOffset | numOffset
  }
  def date = {
    (fullYear <~ '-') ~ (month <~ '-') ~ day
  }
  def time = {
    (hour <~ ':') ~ (minute <~ ':') ~ second
  }
  
  def dateTime: Parser[DateTime] = {
    (date <~ 'T') ~ time ~ opt(secFraction) ~ opt(offset) ^^ {
      case (y ~ m ~ d) ~ (h ~ min ~ sec) ~ frac ~ Some(offset) =>
        toDateTime(y, m, d, h, min, sec, frac, offset)
      case (y ~ m ~ d) ~ (h ~ min ~ sec) ~ frac ~ None =>
        toDateTime(y, m, d, h, min, sec, frac, ZuluOffset)
    }
  }
  def dateOnly: Parser[DateMidnight] = {
    date ~ opt(offset) ^^ {
      case y ~ m ~ d ~ Some(offset) =>
        toDateMidnight(y, m, d, offset)
      case y ~ m ~ d ~ None =>
        toDateMidnight(y, m, d, ZuluOffset)
    }
  }
  
  private def toDateMidnight(year: Int, month: Int, day: Int, offset:Offset): DateMidnight = {
    assert((1 to 12).contains(month))
    assert((1 to 31).contains(day))
    val dateMidnight = new DateMidnight(year, month, day, offset.toDateTimeZone)
    dateMidnight
  }
  
  private def toDateTime(year: Int, month: Int, day: Int, 
      hour: Int, min: Int, sec: Int, frac: Option[Double], 
      offset: Offset): DateTime = {
    
    assert((1 to 12).contains(month))
    assert((1 to 31).contains(day))
    assert((0 until 24).contains(hour))
    assert((0 until 61).contains(min))
    assert((0 until 61).contains(sec))
    
    val millisOfSecond = frac match {
      case Some(fraction) => (fraction * 1000).toInt
      case None => 0
    }
    val dateTime = new DateTime(year, month, day, hour, min, sec, millisOfSecond, offset.toDateTimeZone)
    dateTime
  }
  
  /** 
   * Parse a date/time in RFC 3339 format.
   *
   * @throws ParseException when the string is not a proper date/time. 
   */
  def parseDateTime(text: String): DateTime = {
    parseAll(dateTime, text) match {
      case Success(dt, _) => dt
      case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
    }
  } 
  /**
   * Parse a date only, in RFC 3339 format. It returns a DateTime instance with the
   * 'dateOnly' flag set.
   * 
   * @throws ParseException when the string is not a proper date/time.
   */
  def parseDate(text: String): DateMidnight = {
    parseAll(dateOnly, text) match {
      case Success(dt, _) => dt
      case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
    }
  } 
}