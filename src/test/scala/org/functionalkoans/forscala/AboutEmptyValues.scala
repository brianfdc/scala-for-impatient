package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutEmptyValues extends KoanSpec("Meditate on nothing") {

  "None" should {
    "equal None" in {
      assert(None === None)
    }
    "be identical to None" in {
      val a = None
      assert(a eq None)
    }
  }
  "None" can {
    "be converted to a string" in {
      None.toString should be("None")
    }
    "be converted to an empty list" in {
      val a = None
      a.toList should be(Nil)
    }
    "be considered empty" in {
      val a = None
      a.isEmpty should be(true)
    }
    "be cast as: Any, AnyRef or AnyVal" in {
      (None.asInstanceOf[Any]) should be (None)
      (None.asInstanceOf[AnyRef]) should be (None)
      (None.asInstanceOf[AnyVal]) should be (None)
    }
    "never be cast to any type of object" in {
      intercept[ClassCastException] {
        // put the exception you expect to see in place of the blank
        assert(None.asInstanceOf[String] === __)
      }
    }
  }
  "None can be the opposite of Some for Option types" in {
      val optional: Option[String] = None
      optional.isEmpty should be (true)
      optional should be(None)
  }
  "Some is the opposite of None for Option types" in {
    val someValue: Option[String] = Some("Some Value")
    someValue != None should be(true)
    someValue.isEmpty should be(false)
  }
  "Option.getOrElse can be used to provide a default in the case of None" in {
    val someValue: Option[String] = Some("Some Value")
    val none: Option[String] = None
    someValue.getOrElse("Specified default value") should be ("Some Value")
    none.getOrElse("Specified default value") should be("Specified default value")
  }
  
  "An empty list" can {
    "be represented by another nothing value: Nil" in {
      List() should be(Nil)
    }
  }

}
