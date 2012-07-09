package scala.impatient

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises15._

@RunWith(classOf[JUnitRunner])
class Exercises15Spec
  extends KoanSpec("Spec for _Scala for the Impatient_, chapter 15")
{
  "(15.2)" must {
    "Be correct" in {
      val deprecated = new Deprecated("hi")
      // above will generate deprecation warnings in REPL
    }
  }

}