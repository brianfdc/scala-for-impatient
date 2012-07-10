package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutLists extends KoanSpec("AboutLists is about Scala Lists") {
  "Eq" must {
    "test identity of same object" in {
      val a = List(1, 2, 3)
      val b = List(1, 2, 3)
      (a eq b) should be(false)
    }
  }
  "==" must {
    "test equality of the same content" in {
      val a = List(1, 2, 3)
      val b = List(1, 2, 3)
      (a == b) should be(true)
    }
  }
  
  
  "Nil lists" must {
    "Be identical, even of different types" in {
      val a: List[String] = Nil
      val b: List[Int] = Nil

      (a == Nil) should be(true)
      (b == Nil) should be(true)
      (a == b) should be(true)
    }
  }
  "Lists" when {
    "created" can {
      "be easily created" in {
        val a = List(1, 2, 3)
        a should equal(List(1, 2, 3))
      }
      "Be created from a range" in {
        val a = (1 to 5).toList
        a should be(List(1, 2, 3, 4, 5))
      }
    }
    "created" must {
      "Be immutable" in {
        val a = List(1, 3, 5, 7, 9)
        val b = a.filterNot(v => v == 5) // remove where value is 5
        a should equal(List(1, 3, 5, 7, 9))
        b should equal(List(1, 3, 7, 9))
      }
    }
    "used" should {
      "Be accessed via head and tail" in {
        val a = List(1, 2, 3)
        a.head should equal(1)
        a.tail should equal(List(2, 3))
      }
      "Be accessed randomly" in {
        val a = List(1, 3, 5, 7, 9)
        a(0) should equal(1)
        a(2) should equal(5)
        a(4) should equal(9)
  
        intercept[IndexOutOfBoundsException] {
          println(a(5))
        }
      }
      "Have many useful methods" in {
        val a = List(1, 3, 5, 7, 9)
        // get the length of the list
        a.length should equal(5)
        // reverse the list
        a.reverse should equal(List(9, 7, 5, 3, 1))
        // convert the list to a string representation
        a.toString should equal("List(1, 3, 5, 7, 9)")
        // map a function to double the numbers over the list
        a.map {v => v * 2} should equal(List(2, 6, 10, 14, 18))
        // filter out any values not divisible by 3 in the list
        a.filter {v => v % 3 == 0} should equal(List(3,9))
      }
      "Be 'reduced' with a mathematical operation" in {
        val a = List(1, 3, 5, 7)
        // note the two _s below indicate the first and second args respectively
        a.reduceLeft(_ + _) should equal(16)
        a.reduceLeft(_ * _) should equal(15 * 7)
      }
      "Be folded: foldleft is like reduce, but with an explicit starting value" in {
        val a = List(1, 3, 5, 7)
        // NOTE: foldLeft uses a form called currying that we will explore later
        a.foldLeft(0)(_ + _) should equal(16)
        a.foldLeft(10)(_ + _) should equal(26)
        a.foldLeft(1)(_ * _) should equal(15*7)
        a.foldLeft(0)(_ * _) should equal(0)
      }
      "Reuse their tails" in {
        val d = Nil
        val c = 3 :: d
        val b = 2 :: c
        val a = 1 :: b

        a should be(List(1, 2, 3))
        a.tail should be(b)
        b.tail should be(c)
        c.tail should be(d)
      }
    }
  }

  "Functions over lists" can {
    "Use _ as shorthand" in {
      val a = List(1, 2, 3)
      a.map {_ * 2} should equal(List(2, 4, 6))
      a.filter {_ % 2 == 0} should equal(List(2))
    }
    "Use () instead of {}" in {
      val a = List(1, 2, 3)
      a.map(_ * 2) should equal(List(2, 4, 6))
      a.filter(_ % 2 != 0) should equal(List(1, 3))
    }
  }
}
