package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutLiteralNumbers extends KoanSpec("Integer literals") {
  "Integer literals" can {
    "be created from decimal, octal or hexadecimal, and are 32 bit" in {
      val a = 2
      val b = 31
      val c = 0x30F
      val d = 077
      val e = 0
      val f = -2
      val g = -31
      val h = -0x30F
      val i = -077
      a should be(2)
      b should be(31)
      c should be(783) //Hint: 30F = 783
      d should be(63) //Hint: 077 = 63
      e should be(0)
      f should be(-2)
      g should be(-31)
      h should be(-783) //Hint: 30F = 783
      i should be(-63) //Hint: 077 = 63
    }
  }
  "Long literals" should {
    "be 64 bit, are specified by appending an L or l at the end." in {
      val a = 2L
      val b = 31L
      val c = 0x30FL
      val d = 077L
      val e = 0L
      println("though 'l' is rarely used since it resembles '1'")
      val f = -2l
      val g = -31L
      val h = -0x30FL
      val i = -077L
  
      a should be(2L)
      b should be(31L)
      c should be(783L) //Hint: 30F = 783
      d should be(63L) //Hint: 077 = 73
      e should be(0L)
      f should be(-2L)
      g should be(-31L)
      h should be(-783L) //Hint: 30F = 783
      i should be(-63L) //Hint: 077 = 63
    }
  }
  "Float & double literals" should {
    "follow IEEE 754 specificiation" in {
      val msg = """Float and Double Literals are IEEE 754 for specific,
         |   Float are 32-bit length, Doubles are 64-bit.
         |   Floats can be coerced using a f or F suffix, and
         |   Doubles can be coerced using a d or D suffix.
         |   Exponent are specified using e or E."""
      println(msg)
      val a = 3.0
      val b = 3.00
      val c = 2.73
      val d = 3f
      val e = 3.22d
      val f = 93e-9
      val g = 93E-9
      val h = 0.0
      val i = 9.23E-9D
  
      a should be(3.0d)
      b should be(3.00d)
      c should be(2.73d)
      d should be(3F)
      e should be(3.22d)
      f should be(93e-9)
      g should be(f)
      h should be(0d)
      i should be(9.23e-9D)
    }
  }
  "Dismambiguating dot" can {
    "be accomplished to distinguish between method invocation & double/float literal" in {
      val msg = """Trick: To distinguish the dot for a method invocation from the
          |   decimal point in a float or double literal,
          |   add a space after the literal"""
      println(msg)
      3.0.toString should be("3.0")
      3.toString should be("3")
      (3. toString) should be("3.0")
      (3.0 toString) should be("3.0")
      3d.toString should be("3.0")
      
    }
  }



}
