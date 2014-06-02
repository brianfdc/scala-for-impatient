package awong.impatient

/**
 * Higher Ordered Functions
 */
class Exercises12 {
  /**
   * (12.1)
   */
  def values(fun: (Int) => Int, low:Int, high:Int) = {
    val input = low to high
    val output = input.map(fun(_))
    ( input.zip(output) )
  }
  /**
   * (12.2)
   */
  def largestValue[T <: Ordered[T]](anArray: Array[T]): T = {
    anArray.reduceLeft( (a,b) => if (a > b) a else b )
  }
  /**
   * (12.3)
   */
  def factorialReduce(x: BigInt): BigInt = {
    val n: BigInt = if (x <= 0) 1 else x
    (BigInt(1) to n).reverse.reduceLeft(_ * _)
  }
  /**
   * (12.4)
   */
  def factorial(x:BigInt): BigInt = {
    (BigInt(1) /: (BigInt(1) to x) )(_ * _)
  }
  /**
   * (12.5) Returns the largest value of the function output w/i
   * the given sequence of inputs.
   */
  def largest(fun: (Int) => Int, inputs: Seq[Int]): Int = {
    ( 0 /: inputs ){ (max,each) => if (max > fun(each)) max else fun(each) }
  }
  /**
   * (12.6) Returns input at which the output of the function is the largest
   */
  def largestInput(fun: (Int) => Int, inputs: Seq[Int]): Int = {
    ( inputs.head /: inputs ){ (max,each) => if (fun(max) > fun(each)) max else each }
  }
  /**
   * (12.7)
   */
  def adjustToPair( fn: (Int,Int) => Int )( pairs: List[Tuple2[Int,Int]]) = {
    pairs.map{ (each) => fn(each._1, each._2) }
  }
  
  /**
   * (12.8) Verifies with the elements in array of strings
   * have the lengths given in an array of integers
   */
  def doTheStringsHaveLengths( strings:Array[String], lengths:Array[Integer]):Boolean = {
    strings.corresponds(lengths){ _.length == _ }
  }
  
  def until(condition: => Boolean)(block : => Unit) {
    if (!condition) {
      block
      until(condition)(block)
    }
  }
  
  def exampleUntil ={
    var x = 10
    until(x==0) {
      x -= 1
      println(x)
    }
  }
  /**
   * (12.8) define an unless
   */
}