package scala.impatient

import org.joda.time._
import org.joda.time.format._

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import scala.impatient.parsing._
import scala.calc.dsl._

@RunWith(classOf[JUnitRunner])
class Exercises19Spec extends scala.awong.AbstractFlatSpec {

  /**
   * (19.1/2)
   */
  "Arithmetic Parser" should "calculate" in {
    var result = ArithParser.parse("3*4")
    result.get should be (12)
    result = ArithParser.parse("3+4")
    result.get should be (7)
    result = ArithParser.parse("3-4")
    result.get should be (-1)
    result = ArithParser.parse("3-(4-6)")
    result.get should be (5)
  }
  /**
   * (19.3)
   */
  "CSVParser" should "find integers in CSV" in {
    import scala.impatient.parsing.CSVParser._
    var result = CSVParser.parse("1,2,3")
    result.get should be (List(1,2,3))
    result = CSVParser.parse("1,2,3.14")
    (result match { case Failure(msg,_) => true }) should be (true)
  }
  /**
   * 
   */
  "19.4" should "parse date times" in {
    var dm = new DateMidnight(2012,7,14, DateTimeZone.UTC)
    var str = DateTimeFormat.forPattern("yyyy-MM-dd").print(dm)
    JodaDateTimeParser.parseDate(str) should be (dm)
    
    val fmt = ISODateTimeFormat.dateTime();
    val dt0 = new DateTime(2012,7,14,22,47,31,893, DateTimeZone.UTC)
    str = fmt.print(dt0)
    JodaDateTimeParser.parseDateTime(str) should be (dt0)
    
    val dt1 = new DateTime(2012,7,14,22,47,31,893, DateTimeZone.forOffsetHours(4))
    str = fmt.print(dt1)
    JodaDateTimeParser.parseDateTime(str) should be (dt1)
    
  }
}