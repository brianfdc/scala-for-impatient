package scala.impatient

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises17._

@RunWith(classOf[JUnitRunner])
class Exercises17Spec extends KoanSpec("Specs for Chapter 17"){
  "(17.1)" should {
    "be immutable and swap" in {
      val first = new ImmutablePair("Any", 12)
      val second = first.swap
      (first eq second) should not be (true)
      first.first should be (second.second)
      first.second should be (second.first)
    }
  }
  "(17.2)" should {
    "be mutable and swap" in {
      val pair = new MutablePair("Black", "White")
      pair.first should be ("Black")
      pair.second should be ("White")
      pair.swap()
      pair.first should be ("White")
      pair.second should be ("Black")
      
    }
  }
  "(17.3)" should {
    "be a visitor and swap" in {
      val first = new Pair("Any", 12)
      val second = first.swap(first)
      (first eq second) should not be (true)
      first.first should be (second.second)
      first.second should be (second.first)
      
    }
  }

}