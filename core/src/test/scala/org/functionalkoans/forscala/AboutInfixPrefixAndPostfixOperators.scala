package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutInfixPrefixAndPostfixOperators extends KoanSpec("about infix, prefix and postfix operators") {

  "An infix operator" should {
    "be available if an object has a method that takes 1 parameter" in {
      val g: Int = 3
      (g + 4) should be(__) // + is an infix operator
      (g.+(4)) should be(__) // same result but not using the infix operator
    }
    
    "not work if an object has a method that takes 2 parameter" in {
      val g: String = "Check out the big brains on Brad!"
      g indexOf 'o' should be(__) //indexOf(Char) can be used as an infix operator
      //g indexOf 'o', 4 should be (6) //indexOf(Char, Int) cannot be used an infix operator
      g.indexOf('o', 7) should be(__) //indexOf(Char, Int) must use standard java/scala calls
    }
  }

  "A postfix operator" should {
    "work if an object has a method takes 0 parameters" in {
      val g: Int = 31
      (g toHexString) should be(__) //toHexString takes no params therefore can be called
      //as a postfix operator. Hint: The answer is 1f
    }
  }

  "A prefix operator" should {
    "work if an object has a method name that starts with unary_" in {
      val g: Int = 31
      (-31) should be(__)
    }
    "be creatable on our own class using the only permissible identifiers (+, -, ! and ~)" in {
      class Stereo {
        def unary_+ = "on"
        def unary_- = "off"
      }
      val stereo = new Stereo
      (+stereo) should be(__)
      (-stereo) should be(__)
    }
  }

}