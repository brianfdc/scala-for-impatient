package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutSequencesAndArrays extends KoanSpec("About sequences and arrays") {

  "A list" can {
    "Be converted to an array" in {
      val l = List(1, 2, 3)
      val a = l.toArray
      a should equal(Array(1,2,3))
    }
  }
  "Any sequence" can {
    "Be converted to a list" in {
      val a = Array(1, 2, 3)
      val s = a.toSeq
      val l = s.toList
      l should equal(List(1,2,3))
    }
    "Be created from a for-comprehension" in {
      val s = for (v <- 1 to 4) yield v
      s.toList should be(List(1,2,3,4))
    }
    "Be created from a for-comprehension conditionally" in {
      val s = for (v <- 1 to 10 if v % 3 == 0) yield v
      s.toList should be(List(3,6,9))
    }
    "Be filtered based on a predicate" in {
      val s = Seq("hello", "to", "you")
      val filtered = s.filter(_.length > 2)
      filtered should be(Seq("hello","you"))
    }
    "Is also an array, which can also be filtered in the same way" in {
      val a = Array("hello", "to", "you", "again")
      val filtered = a.filter(_.length > 3)
      filtered should be(Array(__, __))
    }
    "Have its values mapped through a function" in {
      val s = Seq("hello", "world")
      val r = s map {
        _.reverse
      }
      r should be(__)
    }
  }
}
