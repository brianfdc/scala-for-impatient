package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutHigherOrderFunctions extends KoanSpec("Higher ordered functions") {
  "Lambda" should {
    "be an anonymous function" in {
      def lambda = {
        x: Int => x + 1
      }
      def result = List(1, 2, 3) map lambda
      result should be( (2 to 4).toList )
    }
  }
  "Closure" should {
    "be any function that closes over the free variables in its lexical environment" in {
      var incrementor = 1
      println("free variable is: " + incrementor)
      def closure = {
        x: Int => x + incrementor
      }
      val result = List(1, 2, 3) map closure
      result should be( (2 to 4).toList )
      incrementor = 2
      val result1 = List(1, 2, 3) map closure
      result1 should be( (3 to 5).toList )
    }
  }
  "Functions" can {
    "return another function" in {
      def addWithoutSyntaxSugar(x: Int) = {
        new Function1[Int, Int]() {
          def apply(y: Int): Int = x + y
        }
      }
      addWithoutSyntaxSugar(1).isInstanceOf[Function1[Int, Int]] should be(true)

      def add(x: Int) = {
        (y: Int) => x + y
      }
      add(1).isInstanceOf[Function1[Int, Int]] should be(true)
      add(2)(3) should be(5)

      def fiveAdder = add(5)
      fiveAdder(5) should be(10)
    }
    "take another function as a parameter. This helps in composing functions" in {
      def makeUpper(xs: List[String]) = xs map {
        _.toUpperCase()
      }
      def makeWhatEverYouLike(xs: List[String], sideEffect: String => String) = {
        xs map sideEffect
      }
      val lst = List("abc", "xyz", "123")
      (makeUpper(lst)) should be(List("ABC","XYZ","123"))

      makeWhatEverYouLike(List("ABC", "XYZ", "123"), {
        x => x.toLowerCase
      }) should be(lst)
      //using it inline
      List("Scala", "Erlang", "Clojure") map {
        _.length
      } should be( List(5,6,7) )
    }
    "be curried which is a technique to transform a function multiple parameters to one with one parameter" in {
      def multiply(x: Int, y: Int) = x * y
      (multiply _).isInstanceOf[Function2[Int, Int, Int]] should be(true)
      val multiplyCurried = (multiply _).curried
      multiply(4, 5) should be(20)
      multiplyCurried(3)(2) should be(6)
    }
    "be curried so that you can create specialized versions of a generalized function" in {
      def customFilter(f: Int => Boolean)(xs: List[Int]) = {
        xs filter f
      }
      def onlyEven(x: Int) = x % 2 == 0
      val xs = List(12, 11, 5, 20, 3, 13, 2)
      customFilter(onlyEven)(xs) should be( List(12,20,2) )

      val onlyEvenFilter = customFilter(onlyEven) _
      onlyEvenFilter(xs) should be( List(12,20,2) )
    }
  }



}
