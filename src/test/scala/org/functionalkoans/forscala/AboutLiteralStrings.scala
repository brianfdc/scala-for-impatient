package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutLiteralStrings extends KoanSpec("Strings literally") {
  "Character literals" should {
    "be quoted with single quotes" in {
      val a = 'a'
      val b = 'B'
  
      a.toString should be("a")
      b.toString should be("B")
    }
  }
  "Character literals" can {
    "use hexadecimal Unicode" in {
      val c = '\u0061' //unicode for a
      c.toString should be("a")
    }
    "use octal as well" in {
      val d = '\141' //octal for a
      d.toString should be("a")
    }
    "use escape sequences" in {
      val e = '\"'
      val f = '\\'
      e.toString should be("\"")
      f.toString should be("\\")
    }
  }
  "One-line string literals" should {
    "be surrounded by double quotation marks" in {
      val a = "To be or not to be"
      a should be("To be or not to be")
    }
  }
  "One-line string literals" can {
    "contain escape sequences" in {
      val a = "An \141pple \141 d\141y keeps the doctor \141w\141y"
      a should be("An apple a day keeps the doctor away")
    }
  }
  "Multiline string literals" should {
    "be surrounded by three quotation marks" in {
      val a = """An apple a day
      keeps the doctor away"""
      a.split('\n').size should be(2) //a.split('\n').size determines the number of lines
    }
  }
  "Multiline string literals" can {
    "be prettified with stripMargin" in {
    /*
    * Multiline String literals can use | to specify the starting position
    * of subsequent lines, then use stripMargin to remove the surplus indentation.
    */
      val a = """An apple a day
                 |keeps the doctor away"""
      val split = a.stripMargin.split('\n')
      split(1).charAt(0) should be('k')
    }
  }
}
