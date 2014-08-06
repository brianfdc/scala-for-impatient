package org.functionalkoans.forscala

import org.scalatest.matchers.ShouldMatchers
import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * Created by Daniel Hinojosa
 * User: Daniel Hinojosa
 * Date: 3/10/11
 * Time: 5:38 PM
 * url: <a href="http://www.evolutionnext.com">http://www.evolutionnext.com</a>
 * email: <a href="mailto:dhinojosa@evolutionnext.com">dhinojosa@evolutionnext.com</a>
 * tel: 505.363.5832
 */
@RunWith(classOf[JUnitRunner])
class AboutImplicits extends KoanSpec("""Implicits wrap around existing classes to provide extra functionality
           |   This is similar to \'monkey patching\' in Ruby, and Meta-Programming in Groovy.
           |   Creating a method isOdd for Int, which doesn't exist""")
  with ShouldMatchers
{
  "An implicit" can {
    "Be specified on a standalone method" in {
      class KoanIntWrapper(val original: Int) {
        def isOdd() = original % 2 != 0
      }
      implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)
      19.isOdd() should be(__)
      20.isOdd() should be(__)
    }
    
    "be imported into your scope" in {
      object MyPredef {
        class KoanIntWrapper(val original: Int) {
          def isOdd() = original % 2 != 0
          def isEven() = !isOdd()
        }
        implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)
      }
      import MyPredef._
      //imported implicits come into effect within this scope
      19.isOdd() should be(__)
      20.isOdd() should be(__)
    }
    
    "be imported used to automatically convert from one type to another" in {
      import java.math.BigInteger
      implicit def Int2BigIntegerConvert(value: Int): BigInteger = new BigInteger(value.toString)
      def add(a: BigInteger, b: BigInteger) = a.add(b)
      (add(3, 6)) should be(__)
    }
    
    
  }
  
  "An implicit function parameter" can {
    "be declared as a value to be provided as a default as long as it is set within scope" in {
      def howMuchCanIMake_?(hours: Int)(implicit dollarsPerHour: BigDecimal) = dollarsPerHour * hours
      implicit var hourlyRate = BigDecimal(34.00)
      howMuchCanIMake_?(30) should be(__)

      hourlyRate = BigDecimal(95.00)
      howMuchCanIMake_?(95) should be(__)
    }
    
    "be used as a list of implicits" in {
      def howMuchCanIMake_?(hours: Int)(implicit amount: BigDecimal, currencyName: String) =
        (amount * hours).toString() + " " + currencyName
      implicit var hourlyRate = BigDecimal(34.00)
      implicit val currencyName = "Dollars"
      howMuchCanIMake_?(30) should be(__)

      hourlyRate = BigDecimal(95.00)
      howMuchCanIMake_?(95) should be(__)
    }
    
  }
  
  "Default arguments" should {
    "still be preferred to implicit function parameters " in {
      def howMuchCanIMake_?(hours: Int, amount: BigDecimal = 34, currencyName: String = "Dollars") =
        (amount * hours).toString() + " " + currencyName

      howMuchCanIMake_?(30) should be(__)
      howMuchCanIMake_?(95, 95) should be(__)
    }
  }
}