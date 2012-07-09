package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutAsserts
  extends KoanSpec("Medidate on AboutAsserts to see how Scala Koans work") 
{
  "Scala Test" can {
    "have asserts that take a boolean argument" in {
      assert(false) // should be true
    }
    "have asserts that include a message" in {
      assert(false, "This should be true")
    }
    "have true and false values that can be compared with should matchers" in {
      (1 == 1) should be(true) // should be true
    }
    "have booleans in asserts that can test equality" in {
      val v1 = 4
      val v2 = 4
      assert(v1 === v2)
    }
    "have test where sometimes we expect you to fill in the values" in {
      val v1 = 2
      assert(v1 == 1 + 1)
    }
  }
}
