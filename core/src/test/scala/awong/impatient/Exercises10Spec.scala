package awong.impatient

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.impatient.Exercises10.RectangleLike
import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises10Spec extends AbstractFlatSpec {
  
  before {
    logger.trace("executing before")
  }

  "(10.1)" should "mixin translate and grow into a concrete Java class" in {
    val egg = new java.awt.geom.Ellipse2D.Double(5,10,20,30) with RectangleLike
    egg.translate(10,-10)
    egg.getX should be (15)
    egg.getY should be (0)
    egg.grow(10,20)
    egg.getWidth should be (30)
    egg.getHeight should be (50)
  }

  it should "ceasar Cipher" in {
    import awong.impatient.Exercises11.Money
    import awong.impatient.Exercises10.CryptoLogger
    val money = new Money(100, 100) with CryptoLogger
    
    money.totalCents should be ( BigInt(101*100) )
    money.log("hello") should be ("khoor")
  }
}