package awong.impatient

import scala.collection.mutable._
import scala.collection.immutable._
import scala.util.Random
import java.util.TimeZone
import java.awt.datatransfer._

class Exercises03 {

  /**
   * (1.1)
   */
  def randomArray(n:Int): Array[Int] = {
    val r = Random
    var a = ArrayBuffer[Int]()
    for (i <- 0 until n) {
      a += r.nextInt(n)
    }
    a.toArray
  }
  /**
   * (1.2) Loop that swaps adjacent elements of an array
   */
  def swapAdjacent(a:Array[Int]): Array[Int] = {
    var b = ArrayBuffer[Int]()
    for (i <- 0 until (a.length,2) ) {
      if (i + 1 < a.length) {
        b += a(i+1)
        b += a(i)
      } else {
        b += a(i)
      }
    }
    b.toArray
  }
  /**
   * (1.3)
   */
  def swapYield(a:Array[Int]): Array[Int] = {
    val b = for (i <- 0 until a.length ) yield {
      if (i < a.length - 1) {
        if (i % 2 == 0) a(i+1) else a(i-1)
      } else {
        a(i)
      }
    }
    b.toArray
  }
  /**
   * (1.4) Given an array of integers, produce a new array that contains
   * all the positive values of the original array in their original order,
   * followed by all values that are zero or negative, in their original order
   */
  def partition(a : Array[Int]) : Array[Int] = {
    var b = a.filter{ _ > 0 }
    b ++= a.filter{ _ <= 0 }
    b.toArray
  }
  /**
   * (1.5)
   */
  def average(a: Array[Double]) : Double = {
    var s = a.sum
    s / a.length
  }
  /**
   * (1.6)
   */
  def reverseSortArray(a : Array[Int]) : Array[Int] = {
    a.sorted.reverse
  }
  def reverseSortArrayBuf(a : ArrayBuffer[Int]) : ArrayBuffer[Int] = {
    a.sorted.reverse
  }
  
  /**
   * (1.7)
   */
  def filterDuplicates(a: Array[Int]) : Array[Int] = {
    a.distinct
  }
  /**
   * (1.8) Rewrite example on p 32: collect indices of negative elements, reverse 
   * the sequence, drop the last index and call a.remove(i) for each index
   * 
   * e.g. (1,2,3,-1,4,5) -> (1,2,3,-1)
   */
  def original(a:Array[Int]) : Array[Int] = {
    var first = true
    val indices = for (i <- 0 until a.length if first) yield {
      if (a(i) >= 0){
        i
      } else {
        first = false;
        i
      }
    }
    var b = ArrayBuffer[Int]()
    for (j <- 0 until indices.length) {
      b += a(indices(j))
    }
    b.toArray
  }
  /*
   * Collect indices of negative elements &
   * then drop everything except for the index of
   * the first negative element.
   */
  def rewrite(a: Array[Int]) : Array[Int] = {
    val indices = for (i <- 0 until a.length if a(i) < 0) yield i
    a.dropRight(indices.head)
  }
  /**
   * (1.9)
   */
  def findAmericanTimeZones : Array[String] = {
    val b = TimeZone.getAvailableIDs
    val prefix = "America/"
    val c = b.filter( _.startsWith(prefix) ).map( _.stripPrefix(prefix) )
    c
  }
  
  /**
   * (1.10)
   */
  def flavors : Array[String] = {
    import scala.collection.JavaConversions._
    val command = ArrayBuffer("ls", "-al" , "~/bin");
    // converts Scala {@code command} to java.util.List
    val pb = new java.lang.ProcessBuilder(command)
    
    def flavors = SystemFlavorMap.getDefaultFlavorMap().asInstanceOf[SystemFlavorMap]
    // converts a java.util.List<String> to Scala buffer
    val f : Buffer[String] = flavors.getNativesForFlavor(DataFlavor.imageFlavor)
    f.toArray
  }
}