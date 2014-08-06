package org.functionalkoans.forscala

import org.scalatest.matchers.ShouldMatchers
import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutPartiallyAppliedFunctions extends KoanSpec("About partially applied functions") {
  
  "A partially applied function" must {
    "be a function that you do not apply any or all of the arguments, creating another function" in {
      def sum(a: Int, b: Int, c: Int) = a + b + c
      val sum3 = sum _
      sum3(1, 9, 7) should be(17)
      sum(4, 5, 6) should be(15)
    }
    "be able to replace any number of arguments" in {
      def sum(a: Int, b: Int, c: Int) = a + b + c
      val sumC = sum(1, 10, _: Int)
      sumC(4) should be(15)
      sum(4, 5, 6) should be(15)
    }
  }
}