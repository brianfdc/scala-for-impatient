package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutUniformAccessPrinciple extends KoanSpec("Uniform access principle") {

  "One" can {
    "access age as parameterless method" in {
      val me = new CalculatesAgeUsingMethod(2010, 2003)
      me.age should be(7)
    }
    "access age as a property" in {
      val me = new CalculatesAgeUsingProperty(2010, 2003)
      me.age should be(7)
    }
    "never add a parameter to method invocation" in {
      val me = new CalculatesAgeUsingMethod(2010, 2003)
      // uncomment following line to see what happens if you try to access parameterless method with parens
      // Compile error
      //me.age() should be (7)
    }
    "update current year using property but it won't update the age property" in {
      val me = new CalculatesAgeUsingProperty(2010, 2003)
      me.currentYear = 2011
      me.age should be(7)
    }
    "update current year but since age is a method it will recalculate" in {
      val me = new CalculatesAgeUsingMethod(2010, 2003)
      me.currentYear = 2011
      me.age should be(8)
    }
  }
}
