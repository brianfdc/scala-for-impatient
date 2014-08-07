package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import org.functionalkoans.forscala.caseclasses.Person
import org.functionalkoans.forscala.caseclasses.Dog
import org.functionalkoans.forscala.caseclasses.MutableDog
import org.functionalkoans.forscala.caseclasses.Citizen

@RunWith(classOf[JUnitRunner])
class AboutCaseClasses
  extends KoanSpec("""case classes are very convenient, they give you a lot for free. The following Koans will
    help you understand some of the conveniences. Case classes are also an integral part of
    pattern matching which will be the subject of a later""")
{
  "Case classes" must {
    "have an automatic equals method that works" in {
      val p1 = new Person("Fred", "Jones")
      val p2 = new Person("Shaggy", "Rogers")
      val p3 = new Person("Fred", "Jones")
      (p1 == p2) should be(false)
      (p1 == p3) should be(true)
      (p1 eq p2) should be(false)
      (p1 eq p3) should be(false) // not identical, merely equal
    }
    "have an automatic hashCode method that works" in {
      val p1 = new Person("Fred", "Jones")
      val p2 = new Person("Shaggy", "Rogers")
      val p3 = new Person("Fred", "Jones")
      (p1.hashCode == p2.hashCode) should be(false)
      (p1.hashCode == p3.hashCode) should be(true)
    }
    "have a convenient way they can be created" in {
      val d1 = Dog("Scooby", "Doberman")
      val d2 = Dog("Rex", "Custom")
      val d3 = new Dog("Scooby", "Doberman") // the old way of creating using new
      (d1 == d3) should be(true)
      (d1 == d2) should be(false)
      (d2 == d3) should be(false)
    }
    "have a convenient toString method defined" in {
      val d1 = Dog("Scooby", "Doberman")
      d1.toString should be("Dog(Scooby,Doberman)")
    }
    "have automatic properties" in {
      val d1 = Dog("Scooby", "Doberman")
      d1.name should be("Scooby")
      d1.breed should be("Doberman")
      // what happens if you uncomment the line below? Why?
      // compile error b/c properties are immutable
      // d1.name = "Scooby Doo"
    }
    "be able to have mutable properties" in {
      val d1 = MutableDog("Scooby", "Doberman")
      d1.name should be("Scooby")
      d1.breed should be("Doberman")
      d1.name = "Scooby Doo" // but is it a good idea? No
      d1.name should be("Scooby Doo")
      d1.breed should be("Doberman")
    }
    "be able to support safer alternatives to alter case classes " in {
      val d1 = Dog("Scooby", "Doberman")
      val d2 = d1.copy(name = "Scooby Doo") // copy the case class but change the name in the copy
      d1.name should be("Scooby") // original left alone
      d1.breed should be("Doberman")
      d2.name should be("Scooby Doo")
      d2.breed should be("Doberman")  // copied from the original
    }
    "be able to have default and named parameters" in {
      val p1 = Citizen("Fred", "Jones", 23, "111-22-3333")
      val p2 = Citizen("Samantha", "Jones") // note missing age and ssn
      val p3 = Citizen(last = "Jones", first = "Fred", ssn = "111-22-3333") // note the order can change, and missing age
      val p4 = p3.copy(age = 23)
      p1.first should be("Fred")
      p1.last should be("Jones")
      p1.age should be(23)
      p1.ssn should be("111-22-3333")

      p2.first should be("Samantha")
      p2.last should be("Jones")
      p2.age should be(0)
      p2.ssn should be("")

      p3.first should be("Fred")
      p3.last should be("Jones")
      p3.age should be(0)
      p3.ssn should be("111-22-3333")

      (p1 == p4) should be(true)
    }
    "be able to be disassembled to their constituent parts as a tuple" in {
      val p1 = Citizen("Fred", "Jones", 23, "111-22-3333")
      logger.info("this seems weird, but it's critical to other features of Scala")
      val parts = Citizen.unapply(p1).get 

      parts._1 should be("Fred")
      parts._2 should be("Jones")
      parts._3 should be(23)
      parts._4 should be("111-22-3333")
    }
  }






}
