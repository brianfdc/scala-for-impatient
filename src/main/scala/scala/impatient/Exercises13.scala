package scala.impatient

import scala.collection.immutable._

/**
 * Collections
 */
class Exercises13 {
  /**
   * (13.1/2) Fills a tree map with a map of the indices of 
   * all the characters.
   */
  def indexString(string:String): SortedMap[Char,Set[Int]] = {
    ( TreeMap[Char,Set[Int]]() /: string.zipWithIndex ){ (map, each) =>
      val char = each._1
      val index = each._2
      ( map + (char -> (map.getOrElse(char, Set[Int]()) + index) ) )
    }
  }
  /**
   * (13.3) Remove all zeros
   */
  def removeAllZeros(ints:List[Int]) : List[Int] = {
    ints.filterNot{ _ == 0 }
  }
  /**
   * (13.4)
   */
  def getIntegersThatMapped(strings:Seq[String], map: Map[String,Int]):Set[Int] = {
    val intersection = map.keySet.intersect(strings.toSet)
    val result = intersection.map{ map.get(_).get }
    ( result )
  }
  
  /**
   * (13.5)
   */
  def makeString[T](seq: Seq[T], separator:String): String = {
    var strings = seq.map( _.toString )
    strings.reduceLeft{ _ + separator + _ }
  }
  /**
   * (13.6)
   */
  def reverse(lst : List[Int])  {
    // Both copy from lst to result of the folding function
    (lst :\ List[Int]())( _ :: _)  // fold left
    (List[Int]() /: lst)( _ :+ _)  // fold right
    
    // modifies the foldRight function to reverse lst
    (List[Int]() /: lst){ (reversed, each) => each :: reversed }
  }
  
  /**
   * (13.7) Refine the expression, foo
   */
  def total(prices : List[Double], quantities: List[Int]): Double = {
    val foo = (prices zip quantities).map( p => p._1 * p._2 ).sum
    
    def subtotal = { (x:Double, y:Int) => x * y }.tupled
    val refinedFoo = (prices zip quantities).map( subtotal(_) ).sum
    refinedFoo
  }
  
  /*
   * (13.8)
   */
  
  /*
   * (13.9) 
   */
  
  /*
   * (13.10) 
   */

}