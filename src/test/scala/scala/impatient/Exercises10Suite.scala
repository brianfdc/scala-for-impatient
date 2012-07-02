package scala.impatient

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import scala.impatient.Exercises10.RectangleLike

import scala.collection.mutable.Stack
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class Exercises10Suite extends FunSuite with BeforeAndAfter {
  
  before {
    println("executing before")
  }

  test("(10.1: mixin translate and grow into a concrete Java class") {
    val egg = new java.awt.geom.Ellipse2D.Double(5,10,20,30) with RectangleLike
    egg.translate(10,-10)
    assert(egg.getX === 15)
    assert(egg.getY === 0)
    egg.grow(10,20)
    assert(egg.getWidth  === 30)
    assert(egg.getHeight === 50)
  }

  test("ceasar Cipher") {
    import scala.impatient.Exercises11._
    val money = new Exercises11.Money(100, 100) with Exercises10.CryptoLogger
    expect( BigInt(101*100) ) { money.totalCents } 
    expect( "khoor" ) { money.log("hello") }
  }
}