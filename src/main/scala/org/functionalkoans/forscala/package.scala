package org.functionalkoans

import scala.reflect.BeanProperty

package object forscala {
  def packageMethod = {
    println("I am a package method")
  }
}

package forscala {
  class Worker(@BeanProperty var firstName: String, @BeanProperty var lastName: String)
  class Employee(firstName: String,
                 lastName: String,
                 @BeanProperty var employeeID: Long)
    extends Worker(firstName, lastName)
  
  class ImmutableWorker(val firstName: String, val lastName: String)
  class ImmutableEmployee(override val firstName: String,
                          override val lastName: String,
                          val employeeID: Long)
    extends ImmutableWorker(firstName, lastName)
  
  class WithParameterRequirement(val myValue: Int) {
    require(myValue != 0)

    def this(someValue: String) {
      this (1)
    }
  }
  
  trait Randomizer[A] {
      def draw(): A
  }
  
  class IntRandomizer extends Randomizer[Int] {
      def draw = {
        import util.Random
        Random.nextInt()
      }
  }
  
  case class Event(name: String, source: Any)
  trait EventListener {
    def listen(event: Event): Option[String]
    def print(event:Event): Unit = {
      listen(event) match {
        case Some(x) => println(x)
        case None => println ("Nothing of importance occured")
      }
    }
  }
  class MyListener extends EventListener {
    def listen(event: Event):Option[String] = {
      event match {
        case Event("Moose Stampede", _) => 
          Some("An unfortunate moose stampede occured")
        case _ =>
          None
      }
    }
  }
  
  class OurListener
  
  class ComplexListener extends OurListener with EventListener {
    def listen(event: Event):Option[String] = {
      event match {
        case Event("Woodchuck Stampede", _) =>
          Some("An unfortunate woodchuck stampede occured")
        case _ =>
          None
      }
    }
  }
  
  class CalculatesAgeUsingMethod(var currentYear: Int, birthYear: Int) {
    def age = currentYear - birthYear
    // calculated when method is called
  }

  class CalculatesAgeUsingProperty(var currentYear: Int, birthYear: Int) {
    // does age stay up to date if defined as a var instead of a val?
    val age = currentYear - birthYear
    // calculated at instantiation, returns property when called
  }
  
  package traits {
    trait Logging {
      var logCache = List[String]()
      def log(value: String) = {
        logCache = logCache :+ value
        println(value)
      }
    }
    class Welder extends Logging {
      def weld() {
        log("welding pipe")
      }
    }
    
    class Baker extends Logging {
      def bake() {
        log("baking cake")
      }
    }
  }
}