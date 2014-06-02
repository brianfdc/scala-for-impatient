package scala.impatient

import scala.awong._

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Exercises01Spec extends AbstractFlatSpec {
  var exercises: Exercises01 = _
  
  before {
    exercises = new Exercises01
  }
  
  it should "generate a random filename" in {
    val filename01 = exercises.randomFilename(10);
    assert(filename01.size === 10)
    val filename02 = exercises.randomFilename(10);
    assert(filename02.size === 10)
    assert( (filename01 != filename02) )
  }
}