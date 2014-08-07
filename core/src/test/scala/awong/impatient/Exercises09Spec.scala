package awong.impatient

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises09Spec extends AbstractFlatSpec {
  "Regex playground" should "work" in {
    val text = 
      """case class MyClass(id:String) extends TheirClass {
           val number = 4
           val decimal = 2.0
           val name = "Alan"
      }"""
    logger.debug(Exercises09.ident.toString)
    val identifiers = Exercises09.ident.findAllIn(text).toList
    identifiers.isEmpty should be(false)
    identifiers.size should be (14)
    
    val integers = Exercises09.wholeNumber.findAllIn(text).toList
    integers.isEmpty should be(false)
    integers.size should be (3)
    integers should be (List("4","2","0"))
    
    val stringLiterals = Exercises09.stringLiteral.findAllIn(text).toList
    stringLiterals.isEmpty should be(false)
    stringLiterals.size should be (1)
    
    val decimals = Exercises09.decimalNumber.findAllIn(text).toList
    decimals.isEmpty should be(false)
    decimals.size should be (2)
    decimals should be (List("4","2.0"))
  }


}