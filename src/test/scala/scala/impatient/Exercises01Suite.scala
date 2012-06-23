package scala.impatient

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner

import scala.collection.mutable.Stack
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Exercises01Suite extends FunSuite with BeforeAndAfter {
  var exercises: Exercises01 = _
  
  before {
    exercises = new Exercises01
  }
  
  test("random filename is generated") {
    val filename01 = exercises.randomFilename(10);
    assert(filename01.size === 10)
    val filename02 = exercises.randomFilename(10);
    assert(filename02.size === 10)
    assert( (filename01 != filename02) )
  }
}