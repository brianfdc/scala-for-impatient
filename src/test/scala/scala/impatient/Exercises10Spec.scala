package scala.impatient

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises10.RectangleLike


@RunWith(classOf[JUnitRunner])
class Exercises10Spec extends FlatSpec with BeforeAndAfter {
  
  before {
    println("executing before")
  }

  "(10.1)" should "mixin translate and grow into a concrete Java class" in {
    val egg = new java.awt.geom.Ellipse2D.Double(5,10,20,30) with RectangleLike
    egg.translate(10,-10)
    assert(egg.getX === 15)
    assert(egg.getY === 0)
    egg.grow(10,20)
    assert(egg.getWidth  === 30)
    assert(egg.getHeight === 50)
  }

  it should "ceasar Cipher" in {
    import scala.impatient.Exercises11._
    val money = new Exercises11.Money(100, 100) with Exercises10.CryptoLogger
    expect( BigInt(101*100) ) { money.totalCents } 
    expect( "khoor" ) { money.log("hello") }
  }
}