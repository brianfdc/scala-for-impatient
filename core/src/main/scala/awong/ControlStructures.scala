package awong

/**
 * Created by IntelliJ IDEA.
 * User: awong
 * Date: 10/10/11
 * Time: 8:32 PM
 */

class ControlStructures {

  def conditional(x: Int): Int = {
    if (x > 0) {
      1
    } else {
      -1
    }
  }

  def signum(x: Int): Int = {
    // replace with a pattern match
    if (x > 0) {
      1
    } else if (x == 0) {
      0
    } else {
      -1
    }
  }

  /**
   * If you're a FP, this is weird to you
   */
  def weirdConditional(x: Int): Any = {
    if (x > 0) {
      1
    }
  }

  def whileLoop(counter: Int, initial: Int) : Int = {
    var r = initial
    var n = counter
    while (n > 0) {
      r = r * n
      n -= 1
    }
    r
  }
  /*
  def forLoop(counter: Int, s: Iterable) : Int = {
    for (i <- 0 until s.size) {

    }
  }
  */
  def foreach[T](seq : Seq[T], fn : (T) => Any) {
    for (each <- seq) {
      fn(each)
    } 
  }

  def decorate(str: String, left: String = "[", right: String = "]") : String = {
    left + str + right
  }

  /*
  def sum(varargs : Int*) = {
    var result = 0
    for (arg -> varargs.elements) {
      result += arg
    }
    result
  }
  */

  def box(s: String) : Unit = {
    val border = "-" * s.size + "--\n"
    println(border + "|" + s + "|\n" + border)
  }
}