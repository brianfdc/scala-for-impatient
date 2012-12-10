package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutTuples extends KoanSpec("Meditate on AboutTuples to get tuples") {
  "Tuples" can {
    "Be created easily" in {
      val tuple = ("apple", "dog")
      tuple._1 should be("apple")
      tuple._2 should be("dog")
    }
    "Be accessed individually" in {
      val tuple = ("apple", "dog")
      val fruit = tuple._1
      val animal = tuple._2

      fruit should be("apple")
      animal should be("dog")
      
    }
    "Be of mixed type" in {
      import java.util.Date
      val tuple5 = ("a", 1, 2.2, new Date(), BigDecimal(5))
      tuple5._1 should be("a")
      tuple5._2 should be(1)
      tuple5._5 should be(BigDecimal(5))
    }
  }


}
