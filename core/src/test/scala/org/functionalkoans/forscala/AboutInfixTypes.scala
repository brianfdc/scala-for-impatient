package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutInfixTypes extends KoanSpec("About infix types") {

  "A type infix" can {
    "be made, which means that the type can be displayed in complement between 2 types in order to make a readable declaratoin" in {
      case class Person(name: String)
      class Loves[A, B](val a: A, val b: B)
      
      def announceCouple(couple: Person Loves Person) = {
        //Notice our type: Person loves Person!
        couple.a.name + " is in love with " + couple.b.name
      }
      val romeo = new Person("Romeo")
      val juliet = new Person("Juliet")

      announceCouple(new Loves(romeo, juliet)) should be(__)
    }
    
    "be made a bit more elegant with an infix operator" in {
      case class Person(name: String) {
        def loves(person: Person) = new Loves(this, person)
      }

      class Loves[A, B](val a: A, val b: B)

      def announceCouple(couple: Person Loves Person) = {
        //Notice our type: Person loves Person!
        couple.a.name + " is in love with " + couple.b.name
      }
      val romeo = new Person("Romeo")
      val juliet = new Person("Juliet")

      announceCouple(romeo loves juliet) should be(__)
    }
  }
}