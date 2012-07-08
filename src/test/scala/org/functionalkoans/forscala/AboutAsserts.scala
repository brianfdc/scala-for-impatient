package org.functionalkoans.forscala

import org.scalatest.matchers.ShouldMatchers
import org.functionalkoans.forscala.support.KoanSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutAsserts extends KoanSuite with ShouldMatchers {
// meditate on AboutAsserts to see how the Scala Koans work

  koan("asserts can take a boolean argument") {
    assert(false) // should be true
  }

  koan("asserts can include a message") {
    assert(false, "This should be true")
  }

  koan("true and false values can be compared with should matchers") {
    true should be(__) // should be true
  }

  koan("booleans in asserts can test equality") {
    val v1 = 4
    val v2 = 4
    assert(v1 === __)
  }

  koan("sometimes we expect you to fill in the values") {
    assert(__ == 1 + 1)
  }
}
