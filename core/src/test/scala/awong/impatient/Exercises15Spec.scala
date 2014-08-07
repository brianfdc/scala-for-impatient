package awong.impatient

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.impatient.Exercises15._
import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises15Spec
  extends awong.AbstractWordSpec
{
  "(15.2)" must {
    "Be correct" in {
      val deprecated = new Deprecated("hi")
      // above will generate deprecation warnings in REPL
    }
  }

}