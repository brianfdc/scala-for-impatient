package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutTuples extends KoanSuite {

  koan("Tuples can be created easily") {
    val tuple = ("apple", "dog")
    tuple should be(__)
  }

  koan("Tuple items may be accessed individually") {
    val tuple = ("apple", "dog")
    val fruit = tuple._1
    val animal = tuple._2

    fruit should be(__)
    animal should be(__)
  }

  koan("Tuples may be of mixed type") {
    import java.util.Date
    val tuple5 = ("a", 1, 2.2, new Date(), BigDecimal(5))

    tuple5._2 should be(__)
    tuple5._5 should be(__)
  }
}
