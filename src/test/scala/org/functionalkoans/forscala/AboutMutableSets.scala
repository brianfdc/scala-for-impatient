package org.functionalkoans.forscala

import scala.collection.mutable

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutMutableSets extends KoanSpec("About Scala mutable sets") {
  "Mutable sets" can {
    "be created easily" in {
      val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet.size should be(4)
      mySet += "Oregon"
      mySet contains "Oregon" should be(true)
    }
    "have elements removed" in {
      val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet -= "Ohio"
      mySet contains "Ohio" should be(false)
    }
    "have tuples of elements removed" in {
      val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet -= ("Iowa", "Ohio")
      mySet contains "Ohio" should be(false)
      mySet.size should be(2)
    }
    "have Lists of elements added" in {
      val mySet = mutable.Set("Michigan","Wisconsin")
      mySet ++= List("Iowa", "Ohio")
      mySet contains "Ohio" should be(true)
      mySet.size should be(4)
    }
    "have Lists of elements removed" in {
      val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet --= List("Iowa", "Ohio")
      mySet contains "Ohio" should be(false)
      mySet.size should be(2)
    }
    "can be cleared" in {
      val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
      mySet.clear() // Convention is to use parens if possible when method called changes state
      mySet contains "Ohio" should be(false)
      mySet.size should be(0)
    }
  }
}
