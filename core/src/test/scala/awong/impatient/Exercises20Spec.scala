package awong.impatient


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises20Spec extends AbstractFlatSpec {

  "Random number generation" should "just work" in {
     val xf = scala.util.Random.nextInt(10)
     logger.debug("Next random int: {}", xf.toString)
  }
}