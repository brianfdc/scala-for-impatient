package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutRange extends KoanSpec("About ranges") {

  "'Until' ranges" must {
    "not be inclusive at the end of the range" in {
      val someNumbers = 0 until 10
      someNumbers.size should be(10)
      someNumbers.exists( _ == 10) should be(false)
      someNumbers.exists( _ == 9) should be(true)
      someNumbers.exists( _ == 5) should be(true)
      someNumbers.exists( _ == 0) should be(true)
    }
    
    "be able to specify an increment" in {
      val someNumbers = 2 until 10 by 3
      someNumbers.size should be(3)
    }
  }
  
  "'To' ranges" must {
    "be inclusive of end of of range" in {
      val someNumbers = 0 to 34
      someNumbers.contains(34) should be(true)
    }
    "indicate inclusion and increments" in {
      val someNumbers = 0 to 34 by 2
      someNumbers.contains(33) should be(false)
      someNumbers.contains(32) should be(true)
      someNumbers.contains(34) should be(true)
    }
  }
}
