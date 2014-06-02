package awong.impatient

/**
 * Objects
 */
package Exercises06 {
  /**
   * (6.1)
   */
  object Conversions {
    def inchesToCentimeter(inches : Double) : Double = {
      (2.54 * inches)
    }
    def gallonsToLiters(gallons : Double) : Double = {
      (3.78541178 * gallons)
    }
    def milesToKilometers(miles : Double) : Double = {
      (1.609344 * miles)
    }
  }
  /**
   * (6.2)
   */
  abstract class UnitConversion(val factor: Double) {
    def convert(source: Double) : Double = {
      (factor * source)
    } 
  }
  
  object InchesToCentimeters extends UnitConversion(2.54)
  object GallonsToLiters extends UnitConversion(3.78541178)
  object MilesToKilometers extends UnitConversion(1.609344)
  
  /**
   * (6.3)
   */
  object Point extends java.awt.Point {
    def comment : String = {
      ("Bad idea, because java.awt.Point has instance semantics but Scala object has singleton semantics");
    }
  }
  /**
   * (6.4)
   */
  class MyPoint(x:Int, y: Int) {
    override def toString : String = {
      ("x: " + x + ", y:" + y)
    }
  }
  object MyPoint {
    def apply(x:Int, y: Int) : MyPoint = {
      new MyPoint(x, y)
    }
  }
  /**
   * (6.5)
   * If this extends the trait App it will have main method for free
   */
  object ReverseApp {
    def reverse(args:Array[String]) = {
      println("Hello World: " + args.reverse.mkString(" ") )
    }
  }
  
  /**
   * (6.6)
   */
  object CardSuits extends Enumeration {
    type CardSuits = Value
    val Clubs = Value(0, "\u2663")
    val Spades = Value(1, "\u2660")
    val Hearts = Value(2, "\u2665")
    val Diamonds = Value(3, "\u2666")
    /**
     * (6.7)
     */
    def isRed(card: CardSuits): Boolean = {
      (card.id >= 2)
    }
  }
}