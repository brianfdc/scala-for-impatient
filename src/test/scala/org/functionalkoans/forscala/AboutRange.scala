package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutRange extends KoanSuite {

  koan("'Until' ranges are not inclusive at end of range") {
    val someNumbers = 0 until 10

    someNumbers.size should be(10)
    someNumbers.exists( _ == 10) should be(false)
    someNumbers.exists( _ == 9) should be(true)
    someNumbers.exists( _ == 5) should be(true)
    someNumbers.exists( _ == 0) should be(true)
  }

  koan("'Until' ranges can specify increment") {
    val someNumbers = 2 until 10 by 3
    someNumbers.size should be(3)
  }

  koan("'To' ranges indicate inclusion and increments") {
    val someNumbers = 0 to 34 by 2
    someNumbers.contains(33) should be(false)
    someNumbers.contains(32) should be(true)
    someNumbers.contains(34) should be(true)
  }

  koan("Range can specify to include value") {
    val someNumbers = 0 to 34
    someNumbers.contains(34) should be(true)
  }

}
