package scala.wordcount

import scala.actors.Actor
import scala.io.Source

/**
 * Use actors to design a word count program
 */
class FileReadActor extends Actor {
  def act() {
    while(true) {
      receive {
        case filename:String => {
          val source = Source.fromFile(filename:String, "UTF-8")
          (source)
        }
        case _ => {
          println ("nothing to do")
        }
      }
    }
  }
  def readTokens(src:Source) : Array[String] = {
    val lineIterator = src.getLines
    val lines = lineIterator.toArray    // put lines into an Array
    val content = src.mkString       // read whole file into a String
    val tokens : Array[String]  = content.split("\\s+")
    tokens
  }
  
  def wordCountFold(tokens: Seq[String]) : Map[String,Int] = {
    val sortedMap  = scala.collection.immutable.SortedMap[String,Int]()
    tokens.foldLeft(sortedMap){
      (map,eachToken) => map + ( eachToken -> (map.getOrElse(eachToken,0) + 1 ) )
    }
  }
}