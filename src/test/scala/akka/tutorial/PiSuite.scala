package scala.akka


import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class PiSuite extends FunSuite with BeforeAndAfter {
  val pi = akka.tutorial.Pi

  test("Run the Pi actor system") {
    pi.run
    
  }
  

}