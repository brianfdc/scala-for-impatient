package org.functionalkoans.forscala

import scala.Null
import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutConstructors
  extends KoanSpec("Medidate on AboutConstructors to see how Scala constructors work")
{
  class AboutConstructorWithAuxiliaryConstructor(val name: String) {
    // invoke auxiliary constructor
    def this() {
      // what happens if you comment out the following line? compile error
      this ("defaultname")
    }
  }

  "Primary constructor specified with a parameter" must {
    "Be specified with a parameter requires that parameter to be passed in" in {
      val aboutMe = new AboutConstructorWithAuxiliaryConstructor()
      (aboutMe.name.isEmpty) should be (true)
    }
    
  }

  "Class with no class parameters" must {
    "Be called with no arguments" in {
      // add parameter to make this fail
      val aboutMe = new AboutClassWithNoClassParameter
    }
    
  }
  class AboutClassWithNoClassParameter {
  }


}
