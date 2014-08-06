package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutPreconditions extends KoanSpec("About preconditions") {
  // Instruction: use Intercept to catch the type of exception thrown by an invalid precondition
  "WithParameterRequirement" should {
    "Throw an exception on precondition violation on integer value" in {
      intercept[IllegalArgumentException] {
        val satisfiesParameterRequirement = new WithParameterRequirement(0)
        satisfiesParameterRequirement.myValue should be (0)
      }
    }
    "Be coerced to 1 when constructed with string" in {
        val myInstance = new WithParameterRequirement("")
        myInstance.myValue should be (1)
    }
  }
}
