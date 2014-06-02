package awong.impatient

import scala.math._
import scala.math.BigInt._
import scala.util.Random

/**
 * The Basics
 */
class Exercises01 {
  /*
   * (1.1) Methods on the integer "3"
   * 
   *  % : mod
   *  * : multiply
   *  + : add
   *  - : subtract
   *  / : divide
   *  > : greater than
   *  >= : greater than or equal
   *  >> : unsigned bitwise shift right (just like Java)
   *  >>> :signed bitwise shift right (just like Java)
   *  & : bitwise and
   *  ^ : bitwise XOR
   *  | : bitwise inclusive OR
   *  asInstanceOf[T0] : Cast the receiver object to be of ty
   *  isInstanceOf[T0] : Test whether the dynamic type of the receiver object is T0.
   *  toByte :
   *  toChar :
   *  toDouble :
   *  toFloat :
   *  toInt :
   *  toLong :
   *  toShort :
   *  toString :
   *  unary_+ : positie sign
   *  unary_- : negative sign
   *  unary_~ : negation
   */
  // (1.2)
  def exercise_1p2 : BigDecimal = {
    var x : BigDecimal = sqrt(3)
    var delta = 3 - x.pow(2)
    var y : BigInt = 2;
    return delta
  }
  
  /*
   * (1.3) In REPL, res variables are immutable
   * (1.4) In StringOps, "*" returns a string concatenated the given integer number of times.
   * (1.5) "10 max 2" involves an implicit conversion to RichInt which has the max function
   * (1.6) var x : BigInt = 2; x.pow(1024)
   * (1.7) For probablyPrime(100, Random): import scala.math.BigInt._; import scala.util.Random;
   * 
   */
  
  // (1.8)
  def  randomFilename(size:Int): String = {
    val alphanumerics : String = "0123456789abcdefghijklmnopqrstuvwxyz"
    var bldr = new scala.collection.mutable.StringBuilder()
    for (i <- 0 until size) {
      var y = scala.util.Random.nextInt(alphanumerics.length)
      bldr.append(alphanumerics.charAt(y))
    }
    bldr.toString
  }
  /*
   * (1.9) first char of String is in StringOps.head; last char of String is StringOps.last
   * (1.10)
   * take(n:Int) : selects first n elements
   * takeRight(n:Int) : select last n elements
   * drop(n:Int) : selects all elements except first n element
   * dropRight(n:Int) : selects all elements except last n element
   * 
   * Advantage of the above FP methods is that no need to check for array bounds.
   *   e.g.
   *   val alphanumerics : String = "0123456789abcdefghijklmnopqrstuvwxyz"
   *   alphanumerics.take(100) // returns all values in alphanumerics.
   *  
   */
}