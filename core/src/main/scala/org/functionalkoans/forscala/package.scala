package org.functionalkoans

import org.slf4j.LoggerFactory
import org.slf4j.Logger

package object forscala {
  def log(msg: String): Unit = {
    val logger = LoggerFactory.getLogger("org.functionalkoans.forscala")
    logger.debug(msg)
  }
  
  def packageMethod: Unit = {
    log("I am a package method")
  }
}

package forscala {
  
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
  trait EventListener extends awong.LoggingLike {
    def listen(event: Event): Option[String]
    def print(event:Event): Unit = {
      listen(event) match {
        case Some(x) => logger.debug(x.toString)
        case None => logger.debug("Nothing of importance occured")
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
    trait Logging extends awong.LoggingLike {
      var logCache = List[String]()
      def log(value: String) = {
        logCache = logCache :+ value
        logger.debug(value)
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