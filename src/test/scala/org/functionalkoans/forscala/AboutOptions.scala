package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutOptions extends KoanSpec("About scala options") {
  def maybeItWillReturnSomething(flag: Boolean): Option[String] = {
    if (flag) Some("Found value") else None
  }
  
  "A Scala Option" can {
    "have one of two values - Some or None" in {
      val someValue: Option[String] = Some("I am wrapped in something")
      someValue.get should be("I am wrapped in something")

      val nullValue: Option[String] = None
      nullValue should be(None)
    }
    "be a better representation of null with None because null is a bad idea" in {
      val value1 = maybeItWillReturnSomething(true)
      val value2 = maybeItWillReturnSomething(false)
      value1.get should be("Found value")
      intercept[java.util.NoSuchElementException] {
        value2.get
      }
    }
    "provide a default value for None" in {
      val value1 = maybeItWillReturnSomething(true)
      val value2 = maybeItWillReturnSomething(false)

      value1.getOrElse("No value") should be("Found value")
      value2.getOrElse("No value") should be("No value")
      value2.getOrElse{
        "default function"
      } should be("default function")
    }
    "be checked for whether it has a value" in {
      val value1 = maybeItWillReturnSomething(true)
      val value2 = maybeItWillReturnSomething(false)

      value1.isEmpty should be(false)
      value2.isEmpty should be(true)
    }
    "be used with pattern matching" in {
      val someValue: Option[Double] = Some(20.0)
      val value = someValue match {
        case Some(v) => v
        case None => 0.0
      }
      value should be(20.0)
      val noValue: Option[Double] = None
      val value1 = noValue match {
        case Some(v) => v
        case None => 0.0
      }
      value1 should be(0.0)
    }
    "is more than just a replacement of null; it's also a collection" in {
    }
    "Be neglected, when you're tempted to do an ugly null check" in {
      //the ugly version
      def makeFullName(firstName: String, lastName: String) = {
        if (firstName != null) {
          if (lastName != null) {
            firstName + " " + lastName
          } else {
            null
          }
        } else {
          null
        }
      }
      makeFullName("Nilanjan", "Raychaudhuri") should be("Nilanjan Raychaudhuri")
      makeFullName("Nilanjan", null) should be(null)
    }
    "Be used to avoid null checks (pretty version)" in {
      def makeFullNamePrettyVersion(firstName: Option[String], lastName: Option[String]) = {
        firstName flatMap {
          fname =>
            lastName flatMap {
              lname =>
                Some(fname + " " + lname)
            }
        }
      }
      makeFullNamePrettyVersion(Some("Nilanjan"), Some("Raychaudhuri")) should be(Some("Nilanjan Raychaudhuri"))
      makeFullNamePrettyVersion(Some("Nilanjan"), None) should be(None)
    }
    "Used in a for-comprehension" in {
      val values = List(Some(10), Some(20), None, Some(15))
      val newValues = for {
        someValue <- values
        value <- someValue
        
      } yield value
      newValues should be(List(10, 20, 15))
    }
  }
  "A Scala Option is more than a replacement of null" when {
    "Used as a collection " can {
      "Be mapped" in {
        Some(10) map {
          _ + 10
        } should be(Some(20))
      }
      "Be filtered" in {
        Some(10) filter {
          _ == 10
        } should be(Some(10))
      }
      "Be flatMapped" in {
        Some(Some(10)) flatMap {
          _ map {
            _ + 10
          }
        } should be(Some(20))
      }
      "Be foreach-ed" in {
        var newValue1 = 0
        Some(20) foreach {
          newValue1 = _
        }
        newValue1 should be(20)
      }
      "Be foreach-ed (even None)" in {
        var newValue1 = 0
        None foreach {
          newValue1 = _
        }
        newValue1 should be(0)
      }
    }
  }
}
