package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutPatternMatching extends KoanSpec("About Pattern Matching") {
  "Pattern matching" must {
    "return something" in {
      val stuff = "blue"
      val myStuff = stuff match {
        case "red" => logger.debug("RED"); 1
        case "blue" => logger.debug("BLUE"); 2
        case "green" => logger.debug("GREEN"); 3
        case _ => logger.debug(stuff); 0
      }
      myStuff should be(2)
    }
    "Be able to return complex somethings" in {
      val stuff = "blue"
      val myStuff = stuff match {
        case "red" => (255, 0, 0)
        case "green" => (0, 255, 0)
        case "blue" => (0, 0, 255)
        case _ => logger.debug(stuff); 0
      }
      myStuff should be(0, 0, 255)
    }
    "Be able to match complex expressions" in {
      def goldilocks(expr: Any) = expr match {
        case ("porridge", "Papa") => "Papa eating porridge"
        case ("porridge", "Mama") => "Mama eating porridge"
        case ("porridge", "Baby") => "Baby eating porridge"
        case _ => "what?"
      }
      goldilocks(("porridge", "Mama")) should be("Mama eating porridge")
    }
    "Be able to match wildcard parts of expressions" in {
      def goldilocks(expr: Any) = expr match {
        case ("porridge", _) => "eating"
        case ("chair", "Mama") => "sitting"
        case ("bed", "Baby") => "sleeping"
        case _ => "what?"
      }
      goldilocks(("porridge", "Papa")) should be("eating")
      goldilocks(("chair", "Mama")) should be("sitting")
    }
    "be able to substitute parts of expressions" in {
      def goldilocks(expr: Any) = expr match {
        case ("porridge", bear) => bear + " said someone's been eating my porridge"
        case ("chair", bear) => bear + " said someone's been sitting in my chair"
        case ("bed", bear) => bear + " says someone's been sleeping in my bed"
        case _ => "what?"
      }
      goldilocks(("porridge", "Papa")) should be("Papa said someone's been eating my porridge")
      goldilocks(("chair", "Mama")) should be("Mama said someone's been sitting in my chair")
    }
  }
}
