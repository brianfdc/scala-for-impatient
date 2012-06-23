package scala.impatient

class Exercises02 {
  /*
   * (2.1) 
   */
  def signum(x : Int) : Int = {
    if (x > 0) {
      1
    } else if (x == 0) {
      0
    } else {
      -1
    }
  }
  /*
   * (2.2) value of {} is "x: Unit = ()"
   * (2.3) "x = y = 1" -> "val x: Unit = { val y = 1 }"
   * (2.4)
   */
  def loop = {
    for (i <- 0 to 10) {
      println(10-i)
    }
  }
  
  /*
   * (2.5)
   */
  def countdown(n: Int) = {
    for (i <- 0 to n) {
      println(n-i)
    }
  }
  
  /*
   * Prints a tab separated list of characters from a given string
   * and their Unicode code points in a format suitable for using in Scala/Java strings: 
   */
  
  def printTabSeparated(s: String) = {
    s.map(c => "%s\t\\u%04X".format(c, c.toInt)).foreach(println)
  }
  
  /*
   * (2.6) convert unicode of each char in the given s and multiply out the product
   */
  def unicodeProduct(s:String) : Long = {
    var product : Long = 1
    for (i <- 0 until s.length) {
      product = product * s(i).toLong
    }
    return product
  }
  
  /*
   * (2.7/8)
   */
  def product(s:String) : Long = {
    var x = s.map(c => c.toLong)
    x.product;
  }
  /*
   * (2.9) Recursive version of (1.6)
   *  
   */
  def recursiveProduct(s:String) : Long = {
    var product = 1
    def helper(str:String, n: Long) : Long = {
      val product = n * (str.head.toLong)
      if (str.length == 1) {
        return product
      } else {
        return helper(str.tail, product)
      }
    }
    return helper(s, product)
  }
  
  /**
   * Computes x to the nth power. Uses the following recursive definition
   * <ul>
   * <li>x<sup>n</sup> = y<sup>2</sup> if n is even and positive where y = x <sup>n/2</sup></li>
   * <li>x<sup>n</sup> = x * x<sup>n-1</sup> if n is odd and positive</li>
   * <li>x<sup>0</sup> = 1</li>
   * <li>x<sup>n</sup> = 1 / x<sup>-n</sup></li>
   * </ul>
   */
  def power(x : Int, n: Int) : Int = {
    val s = signum(n);
    if (s == 0) {
      1
    } else if (s < 0) {
      2
    } else {
      3
    }
  }
  
}