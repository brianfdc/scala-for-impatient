package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutValAndVar
  extends KoanSpec("Meditate on Scala assignments")
{
  "The Scala language" must {
    "reassign vars" in {
      var a = 5
      a should be(5)
      a = 7
      a should be(7)
    }
    "not reassign vals" in {
      val a = 5
      a should be(5)
      
      // What happens if you uncomment these lines?
      //a = 7
      //a should be (7)
    }
  }

}
