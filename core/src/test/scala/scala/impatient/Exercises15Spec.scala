package scala.impatient

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises15._


@RunWith(classOf[JUnitRunner])
class Exercises15Spec
  extends scala.awong.AbstractWordSpec
{
  "(15.2)" must {
    "Be correct" in {
      val deprecated = new Deprecated("hi")
      // above will generate deprecation warnings in REPL
    }
  }

}