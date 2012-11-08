package scala.impatient


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Exercises20Spec extends scala.awong.AbstractFlatSpec {

  "Random number generation" should "just work" in {
     val xf = scala.util.Random.nextInt(10)
     println(xf)
  }
}