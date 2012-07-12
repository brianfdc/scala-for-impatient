package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutForExpressions extends KoanSpec("About for loops") {
  "For loops" can {
    "Be simple" in {
      val someNumbers = 0 until 10
      var sum = 0
      for (i <- someNumbers) {
        sum += i
      }
      // 1 + 2 + 3 + 4 + 5 + 6 + 7 + 8 + 9 + 10
      sum should equal(45)
    }
    "Contain additional logic" in {
      val someNumbers = 0 until 10
      var sum = 0
      // sum only the even numbers
      for (i <- someNumbers) {
        if (i % 2 == 0) {
          sum += i
        }
      }
      sum should equal(20)
    }
  }
  "For expressions" can {
    "nest, with later generators varying more rapidly than earlier ones" in {
      val xValues = 1 until 5
      val yValues = 1 until 3
      val coordinates = for {
        x <- xValues
        y <- yValues
      }
      yield (x, y)
      coordinates(4) should be(3, 1)
    }
  }
  
  


}
