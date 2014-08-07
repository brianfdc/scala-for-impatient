package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutPartialFunctions extends KoanSpec("About partial functions") {

  "A partial function" should {
    "be a trait that when implemented can be used as building blocks for determining a solution"  in {
      logger.info("The PartialFunction trait requires that the method 'isDefinedAt' and 'apply' be implemented")
      val doubleEvens: PartialFunction[Int, Int] = new PartialFunction[Int, Int] {
        //States that this partial function will take on the task
        def isDefinedAt(x: Int) = x % 2 == 0
        //What we do if this does partial function matches
        def apply(v1: Int) = v1 * 2
      }

      val tripleOdds: PartialFunction[Int, Int] = new PartialFunction[Int, Int] {
        def isDefinedAt(x: Int) = x % 2 != 0
        def apply(v1: Int) = v1 * 3
      }
      val whatToDo = doubleEvens orElse tripleOdds //Here we chain the partial functions together
      whatToDo(3) should be(9)
      whatToDo(4) should be(8)
    }
  }
  
  "Case statements" can {
    "be a quick way to create a partial function as they implement apply and isDefinedAt for you" in {
      //The case statements are called case statements with guards
      val doubleEvens: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) == 0) => x * 2
      }
      val tripleOdds: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) != 0) => x * 3
      }

      val whatToDo = doubleEvens orElse tripleOdds //Here we chain the partial functions together
      whatToDo(3) should be(9)
      whatToDo(4) should be(8)
    }
  }
  
  "The result of partial functions" can {
    "have an \'andThen\' function added to the end of the chain" in {
      //These are called case statements with guards
      val doubleEvens: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) == 0) => x * 2
      }
      val tripleOdds: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) != 0) => x * 3
      }

      val addFive = (x: Int) => x + 5
      //Here we chain the partial functions together
      val whatToDo = doubleEvens orElse tripleOdds andThen addFive
      whatToDo(3) should be(14)
      whatToDo(4) should be(13)
    }
    
    "have an \'andThen\' function added to the end of the chain used to continue onto another chain of logic" in {
      val doubleEvens: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) == 0) => x * 2
      }
      val tripleOdds: PartialFunction[Int, Int] = {
        case x: Int if ((x % 2) != 0) => x * 3
      }

      val printEven: PartialFunction[Int, String] = {
        case x: Int if ((x % 2) == 0) => "Even"
      }
      val printOdd: PartialFunction[Int, String] = {
        case x: Int if ((x % 2) != 0) => "Odd"
      }

      val whatToDo = doubleEvens orElse tripleOdds andThen (printEven orElse printOdd)

      whatToDo(3) should be("Odd")
      whatToDo(4) should be("Even")
    }
  }


}