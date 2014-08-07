package awong.impatient

import java.text.ParseException
import scala.xml._

import org.joda.time._
import org.joda.time.format._


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.impatient.parsing._
import awong.calc.dsl._
import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises19Spec extends AbstractFlatSpec {


  "Arithmetic Parser (19.1/2)" should "calculate" in {
    val parser = new ArithParser
    var result = parser.parse("3*4")
    result should be (12)
    result = parser.parse("3+4")
    result should be (7)
    result = parser.parse("3-4")
    result should be (-1)
    result = parser.parse("3-(4-6)")
    result should be (5)
  }

  "CSVParser (19.3)" should "find integers in CSV" in {
    val parser = new CSVParser
    var result = parser.parse("1,2,3")
    result should be (List(1,2,3))
    intercept[ParseException] {
      result = parser.parse("1,2,3.14")
    }
  }

  "JodaTime parser (19.4)" should "parse date times" in {
    var dm = new DateMidnight(2012,7,14, DateTimeZone.UTC)
    var str = DateTimeFormat.forPattern("yyyy-MM-dd").print(dm)
    val parser = new JodaDateTimeParser
    parser.parseDate(str) should be (dm)
    
    val fmt = ISODateTimeFormat.dateTime();
    val dt0 = new DateTime(2012,7,14,22,47,31,893, DateTimeZone.UTC)
    str = fmt.print(dt0)
    parser.parseDateTime(str) should be (dt0)
    
    val dt1 = new DateTime(2012,7,14,22,47,31,893, DateTimeZone.forOffsetHours(4))
    str = fmt.print(dt1)
    parser.parseDateTime(str) should be (dt1)
  }
  
  "scala xml api" should "manipulate xml" in {
    var tmp = <tmp/>.copy(label="root")
    val attrs = List(new UnprefixedAttribute("r0", "r0", Null), new UnprefixedAttribute("r1", "r1", Null))
    tmp = attrs.foldLeft(tmp) { (x,y) => x % y }
    val kids = new NodeBuffer
    kids += <fred></fred>
    kids += <wilma></wilma>
    kids.toSeq
    tmp = tmp.copy(child= kids.toSeq)
    logger.debug(tmp.toString)
  }
  
  
  "XmlLightParser (19.5)" should "parse xml" in {
    var xmlStr = """<?xml version="1.0" ?>
                    <root r0="r0" r1="1">
                      <dad d0="d0"/>
                      <mum m0="m0"><bro>Hi</bro><sis>There</sis></mum>
                    </root>"""
    var parser = new XmlLightParser
    val node = parser.parse(xmlStr);
    val sis = (node \\ "sis")
    sis.size should be(1)
    sis.head.attributes.isEmpty should be(true)
    node.attribute("r0").get.toString should be("r0")
    node.attribute("r1").get.toString should be("1")
    (node \\ "mum" \ "bro").isEmpty should be (false)
    (node \\ "dad").isEmpty should be (false)
    (node \\ "dad").head.attribute("d0").get.toString should be ("d0")
    (node \\ "mum").isEmpty should be (false)
    (node \\ "mum").head.attribute("m0").get.toString should be ("m0")
    logger.debug(node.toString)
  }

}