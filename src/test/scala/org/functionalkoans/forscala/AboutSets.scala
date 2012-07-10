package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutSets extends KoanSpec("Learn about Scala sets") {
  "Sets" can {
    "be created easily" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet.size should be(4)
    }
    "contain distinct values" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Michigan")
      mySet.size should be(3)
    }
    "be added to easily" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val aNewSet = mySet + "Illinois"
      aNewSet.contains("Illinois") should be(true)
    }
    "be of mixed type" in {
      val mySet = Set("Michigan", "Ohio", 12)
      mySet.contains(12) should be(true)
      mySet.contains("MI") should be(false)
    }
    "be accessed" in {
      val mySet = Set("Michigan", "Ohio", 12)
      mySet(12) should be(true)
      mySet("MI") should be(false)
    }
    "have their elements removed easily" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val aNewSet = mySet - "Michigan"
      aNewSet.contains("Michigan") should be(false)
    }
    "have their elements removed in multiple" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val aNewSet = mySet -- List("Michigan", "Ohio")
      aNewSet.contains("Michigan") should be(false)
      aNewSet.contains("Wisconsin") should be(true)
      aNewSet.size should be(2)
    }
    "have their elements removed with a tuple" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val aNewSet = mySet - ("Michigan", "Ohio") // Notice: single '-' operator for tuples
      aNewSet.contains("Michigan") should be(false)
      aNewSet.contains("Wisconsin") should be(true)
      aNewSet.size should be(2)
    }
    "have the attempted removal of nonexistent elements be handled gracefully" in {
      val mySet = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val aNewSet = mySet - ("Michigan", "Ohio") // Notice: single '-' operator for tuples
      aNewSet.contains("Michigan") should be(false)
      aNewSet.contains("Wisconsin") should be(true)
      aNewSet.size should be(2)
    }
    "Be iterated easily" in {
      val mySet = Set(1, 3, 4, 9)
      var sum = 0
      for (i <- mySet) {
        sum = sum + i
      }
      sum should be(17)
    }
    "Be intersected easily" in {
      val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val mySet2 = Set("Wisconsin", "Michigan", "Minnesota")
      val aNewSet = mySet1 intersect mySet2
      // NOTE: Scala 2.7 used **, deprecated for & or intersect in Scala 2.8
      aNewSet.equals(Set("Michigan", "Wisconsin")) should be(true)
    }
    "Be joined easily with another set through their union" in {
      val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val mySet2 = Set("Wisconsin", "Michigan", "Minnesota")
      val aNewSet = mySet1 union mySet2 // NOTE: You can also use the "|" operator
      aNewSet.equals(Set("Michigan", "Wisconsin", "Ohio", "Iowa", "Minnesota")) should be(true)
    }
    "Either be a subset of another set or not" in {
      val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val mySet2 = Set("Wisconsin", "Michigan", "Minnesota")
      val mySet3 = Set("Wisconsin", "Michigan")
      mySet2 subsetOf mySet1 should be(false)
      mySet3 subsetOf mySet1 should be(true)
    }
    "Be differenced with another set easily" in {
      val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val mySet2 = Set("Wisconsin", "Michigan")
      val aNewSet = mySet1 diff mySet2 // Note: you can use the "&~" operator if you *really* want to.
      aNewSet.equals(Set("Ohio", "Iowa")) should be(true)
    }
    "Be equivalent independent of order" in {
      val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      val mySet2 = Set("Wisconsin", "Michigan", "Ohio", "Iowa")
      mySet1.equals(mySet2) should be(true)
    }
  }


}
