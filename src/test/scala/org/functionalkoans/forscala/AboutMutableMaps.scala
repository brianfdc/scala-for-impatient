package org.functionalkoans.forscala

import scala.collection.mutable

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutMutableMaps extends KoanSpec("About Scala Mutable Maps")
{
  "Mutable maps" can {
    "be created easily" in {
      val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      myMap.size should be(4)
      myMap += "OR" -> "Oregon"
      myMap contains "OR" should be(true)
    }
    "have elements removed" in {
      val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      myMap -= "OH"
      myMap contains "OH" should be(false)
    }
    "have tuples of elements removed" in {
      val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      myMap -= ("IA", "OH")
      myMap contains "OH" should be(false)
      myMap.size should be(2)
    }
    "have tuples of elements added" in {
      val myMap = mutable.Map("MI" -> "Michigan", "WI" -> "Wisconsin")
      myMap += ("IA" -> "Iowa", "OH" -> "Ohio")
      myMap contains "OH" should be(true)
      myMap.size should be(4)
    }
    "have lists of elements added" in {
      val myMap = mutable.Map("MI" -> "Michigan", "WI" -> "Wisconsin")
      myMap ++= List("IA" -> "Iowa", "OH" -> "Ohio")
      myMap contains "OH" should be(true)
      myMap.size should be(4)
    }
    "have lists of elements removed" in {
      val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      myMap --= List("IA", "OH")
      myMap contains "OH" should be(false)
      myMap.size should be(2)
    }
    "be cleared" in {
      val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      myMap.clear() // Convention is to use parens if possible when method called changes state
      myMap contains "OH" should be(false)
      myMap.size should be(0)
    }
  }
}
