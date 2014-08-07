package awong.impatient

import scala.collection.JavaConversions.propertiesAsScalaMap
import scala.collection.Iterable
import scala.collection.Map
import scala.io.Source
import awong.ScalaForImpatientExercise

/**
 * Maps and Tuples
 */
object Exercises04 extends awong.ScalaForImpatientExercise {
  /**
   * (4.1)
   */
  def gizmos :Map[String,Double] = {
    var gizmos = Map("standMixer" -> 100.0, "screws" -> 0.01, "chefsKnife" -> 120.2)
    gizmos
  }
  
  def discount(gizmos: Map[String,Double]) : Map[String,Double] = {
    val d = for ( (k,v) <- gizmos ) yield (k, 0.90 * v)
    d
  }
  import scala.io.Source
  
  def source(sourceName:String) : Source = {
    val source = Source.fromFile(sourceName:String, awong.Defaults.defaultEncoding)
    //val sourceURL = Source.fromURL(sourceName,awong.Defaults.defaultEncoding)
    //val sourceStr = Source.fromString("hola there hola")
    
    source
  } 

  def readTokens(filename:String) : Array[String] = {
    val src = source(filename);
    val lineIterator = src.getLines
    val lines = lineIterator.toArray    // put lines into an Array
    val content = src.mkString       // read whole file into a String
    val tokens : Array[String]  = content.split("\\s+")
    tokens
  }
  /**
   * (4.2)
   */
  def wordCount(filename:String): Map[String,Int] = {
    val tokens = readTokens(filename)
    var m = scala.collection.mutable.Map[String,Int]()
    for (each <- tokens) {
      if (m.contains(each)) {
        var i = m(each) + 1
        m = m + (each -> i )
      } else {
        m = m + (each -> 1)
      }
    }
    m
  }
  
  /**
   * (4.3)
   */
  def wordCountImmutable(filename:String) : Map[String,Int] = {
    var tokens : Array[String] = readTokens(filename)
    var m = scala.collection.immutable.Map[String,Int]()
    for (i <- 0 until tokens.length) {
      var token = tokens(i)
      if (m.contains(token)) {
        var i = m(token) + 1
        m = m + (token -> i)
      } else {
        m = m + (token -> 1)
      }
    }
    m
  }
  /**
   * (4.4)
   */
  def wordCountSorted(filename: String) : Map[String,Int] = {
    val tokens = readTokens(filename)
    var m  = scala.collection.immutable.SortedMap[String,Int]()
    for (i <- 0 until tokens.length) {
      var token = tokens(i)
      if (m.contains(token)) {
        var i = m(token) + 1
        m = m + (token -> i)
      } else {
        m = m + (token -> 1)
      }
    }
    m
  }
  def wordCountFold(filename: String) : Map[String,Int] = {
    val tokens = readTokens(filename)
    var map  = scala.collection.immutable.SortedMap[String,Int]()
    tokens.foldLeft(map){
      (m,c) => m + ( c -> (m.getOrElse(c,0) + 1 ) )
    }
  }
  /**
   * (4.7)
   */
  def javaProperties() = {
    import scala.collection.JavaConversions.propertiesAsScalaMap
    import scala.collection.Iterable
    val props:collection.mutable.Map[String,String] = System.getProperties
    
    var s = collection.immutable.SortedMap[Int,Iterable[String]]()
    s = s ++ props.keys.groupBy{ k => k.size } 
    val largestSize = s.lastKey
    val fmt = "%" + largestSize + "s"
    // BUG to be fixed in Scala 2.9.2 ... should be (k,v) instead of entry
    props.foreach{ e =>
      logger.debug(fmt.format(e._1) + " : " + e._2)
    }
  }

  /**
   * (4.8)
   */
  def minmax(a : Array[Int]) : (Int,Int) = {
    (a.min,a.max)
  }
  /**
   * (4.9)
   */
  def lteqgt(a : Array[Int], v:Int) : (Int,Int,Int) = {
    val lt = a.count( _ < v )
    val eq = a.count( _ == v )
    val gt = a.count( _ > v )
    (lt,eq,gt)
  }
}