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
  def foo(doubles: Array[Double], columns: Int): Array[Array[Double]] = {
    // "grouped" partitions the receiver Seq into fixed-size sequences
    // "groupBy" partitions this sequence[A] into a map of sequences according to some
    //   discriminator function which maps from A to K 
    val ds = doubles.grouped(columns).toArray
    ( ds )
  }
  /*
   * (13.9)
   * Unfortunately scala.actors.Actor is now deprecated and akka Actors are now the standard
   */
//  def readFiles(files:Seq[String]) = {
//    import scala.actors.Actor._
//      val readFileActor = actor {
//        while(true) {
//          receive {
//            case s:String =>{
//              val wordCount = Exercises04.wordCountFold(s)  // now update one of the frequencies maps
//            }
//            case c:Char => {
//              import scala.collection.JavaConversions.asScalaConcurrentMap
//              val freq1 = new scala.collection.mutable.HashMap[Char,Int] with scala.collection.mutable.SynchronizedMap[Char,Int]
//              val freq2: scala.collection.mutable.ConcurrentMap[Char,Int] = new java.util.concurrent.ConcurrentHashMap[Char,Int]
//              freq1.getOrElse(c, 0) + 1
//              freq2.getOrElse(c, 0) + 1
//            }
//            case _ => println("Do nothing")
//          }
//        }
//      }
//    for (file <- files) {
//    }
//  }
  /*
   * (13.10) 
   */

}