package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutLazySequences extends KoanSpec("About lazy sequences") {

  "Lazy collection" can {
    "be created from a strict collection" in {
      val strictList = List(10, 20, 30)
      val lazyList = strictList.view
      lazyList.head should be(__)
    }
    
    "process its elements on demand whereas a strict collection does not" in {
      var x = 0
      def inc = {x += 1; x}
      
      val strictList = List(inc _, inc _, inc _)
      strictList.map(f => f).head should be(__)
      x should be(__)

      strictList.map(f => f).head
      x should be(__)

      x = 0
      val lazyList = strictList.view
      lazyList.map(f => f).head should be(__)
      x should be(__)
      lazyList.map(f => f).head should be(__)
      x should be(__)
    }
    
    "sometimes avoid processing errors" in {
      val lazyList = List(2, -2, 0, 4).view map {
        2 / _
      }
      lazyList.head should be(__)
      lazyList(1) should be(__)
      intercept[ArithmeticException] {
        lazyList(2)
      }
    }
    
    "be also infinite" in {
      val infinite = Stream.from(1)
      infinite.take(4).sum should be(__)
      Stream.continually(1).take(4).sum should be(__)
    }
    
  }
  "The tail of a lazy collection" must {
    "never be computed unless required" in {
      def makeLazy(value: Int): Stream[Int] = {
        Stream.cons(value, makeLazy(value + 1))
      }
      val stream = makeLazy(1)
      stream.head should be(__)
      stream.tail.head should be(__)
    }
  }


}
