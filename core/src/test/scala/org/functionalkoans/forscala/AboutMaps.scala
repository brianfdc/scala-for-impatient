package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutMaps extends KoanSpec("Learn about Scala maps") {
  "Maps" when {
    "created" can {
      "be created easily" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        myMap.size should be(4)
      }
    }
    "created" must {
      "contain distinct pairings" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")
        myMap.size should be(3)
      }
    }
    "used" can {
      "be added to easily" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")
        val aNewMap = myMap + ("IL" -> "Illinois")
        aNewMap.contains("IL") should be(true)
      }
      "Be iterated over its values" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")
        val mapValues = myMap.values
        mapValues.size should be(3)
        mapValues.head should be("Michigan")
        val lastElement = mapValues.last
        lastElement should be("Wisconsin")
        // mapValues.foreach ( logger.info(mval) )
        // NOTE that the following will not compile, as iterators do not implement "contains"
        //mapValues.contains("Illinois") should be (true)
      }
      "Have keys of mixed types"  in {
        val myMap = Map("Ann Arbor" -> "MI", 49931 -> "MI")
        myMap("Ann Arbor") should be("MI")
        myMap(49931) should be("MI")
      }
      "Have mixed types for its values" in {
        val myMap = scala.collection.mutable.Map.empty[String, Any]
        myMap("Ann Arbor") = (48103, 48104, 48108)
        myMap("Houghton") = 49931
        myMap("Houghton") should be(49931)
        myMap("Ann Arbor") should be((48103, 48104, 48108))
        // what happens if you change the Any to Int?
        // compile error
      }
      "Can be accessed" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        myMap("MI") should be("Michigan")
        myMap("IA") should be("Iowa")
      }
      "Have its elements be removed easily" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        val aNewMap = myMap - "MI"
        aNewMap.contains("MI") should be(false)
      }
      "Have its elements removed in multiple" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        val aNewMap = myMap -- List("MI", "OH")
        aNewMap.contains("MI") should be(false)
        aNewMap.contains("WI") should be(true)
        aNewMap.size should be(2)
      }
      "Have its elements removed with a tuple" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        val aNewMap = myMap - ("MI", "WI") // Notice: single '-' operator for tuples
        aNewMap.contains("MI") should be(false)
        aNewMap.contains("OH") should be(true)
        aNewMap.size should be(2)
      }
      
      "be accrued in a fold combinator" in {
        val list = ("MI","Michigan")::("OH","Ohio")::("WI","Wisconsin") ::("IA","Iowa"):: Nil
        val myMap = list.foldLeft(Map[String,String]()) { case (map,(abbr,state)) =>
            map + (abbr -> state)
        }
        myMap.size should be (4)
        myMap("MI") should be ("Michigan")
      }
      "be transformed with in a map combinator" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        val xs = myMap.map { case (k,v) => v }
        xs.size should be (4)
       
      }
    }
    "accessing a map by key" must {
      "result in an exception if key is not found" in {
        val myMap = Map("OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        intercept[NoSuchElementException] {
          myMap("MI") should be("Michigan")
        }
      }
    }
    "an attempt to remove non existent elements" must {
      "Be handled gracefully" in {
        val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
        val aNewMap = myMap - "MN"
        aNewMap.equals(myMap) should be(true)
      }
    }
  }
  "Map insertion with duplicate key updates" should {
    "updates previous entry with subsequent value" in {
      val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Meechigan")
      val mapValues = myMap.values
      mapValues.size should be(3)
      myMap("MI") should be("Meechigan")
    }
  }
  "Map equivalency" should {
    "be independent of order" in {
      val myMap1 = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
      val myMap2 = Map("WI" -> "Wisconsin", "MI" -> "Michigan", "IA" -> "Iowa", "OH" -> "Ohio")
      myMap1.equals(myMap2) should be(true)
    }
  }

}
