package org.functionalkoans.forscala

import org.functionalkoans.forscala.caseclasses._
import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith


@RunWith(classOf[JUnitRunner])
class AboutNamedAndDefaultArguments extends KoanSpec("About named and default arguments in Scala") {

  "Scala" can {
    "specify arguments in any order if you use their names" in {
      val me = new WithoutClassParameters()
      // what happens if you change the order of these parameters (nothing)
      val myColor = me.addColors(green = 0, red = 255, blue = 0)
      // for koan, remove the values in the should equal
      myColor should equal(255, 0, 0)
    }
    "have default arguments if you leave them off" in {
      val me = new WithoutClassParameters()
      val myColor = me.addColorsWithDefaults(green = 255)
      myColor should equal(0, 255, 0)
    }
    "access class parameters and specify arguments in any order if you use their names" in {
      val me = new WithClassParameters(40, 50, 60)
      val myColor = me.addColors(green = 50, red = 60, blue = 40)
      myColor should equal(100, 100, 100)
    }
    "access class parameters and default arguments if you leave them off" in {
      val me = new WithClassParameters(10, 20, 30)
      val myColor = me.addColorsWithDefaults(green = 70)
      myColor should equal(10, 90, 30)
    }
    "default class parameters and and have default arguments too" in {
      val me = new WithClassParametersInClassDefinition()
      val myColor = me.addColorsWithDefaults(green = 70)
      myColor should equal(0, 325, 100)
    }
    "have default parameters which are functional too" in {
      def reduce(a: Int, f: (Int, Int) => Int = (_ + _)): Int = f(a, a)
      reduce(5) should equal(10)
      reduce(5, _ * _) should equal(25)
    }
  }



}
