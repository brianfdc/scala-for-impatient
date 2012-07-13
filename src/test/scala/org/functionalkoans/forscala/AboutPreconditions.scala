package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutPreconditions extends KoanSpec("About preconditions") {
  // Instruction: use Intercept to catch the type of exception thrown by an invalid precondition
  "WithParameterRequirement" should {
    "Throw an exception on precondition violation" in {
      val satisfiesParameterRequirement = new WithParameterRequirement(0)
      satisfiesParameterRequirement.myValue should be (0)
      intercept[IllegalArgumentException] {
        val myInstance = new WithParameterRequirement("")
      }
    }
  }
}
