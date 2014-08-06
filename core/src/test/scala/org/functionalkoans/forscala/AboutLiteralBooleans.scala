package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutLiteralBooleans extends KoanSpec("about literal booleans") {
  "Boolean literals" must {
    "be either true or false, using the true or false keyword" in {
      println("If you want a non-classical logic, write it yourself!")
      val a = true
      val b = false
      val c = (1 > 2)
      val d = (1 < 2)
      val e = (a == c)
      val f = (b == d)
      a should be(true)
      b should be(false)
      c should be(false)
      d should be(true)
      e should be(false)
      f should be(false)
    }
  }
}
