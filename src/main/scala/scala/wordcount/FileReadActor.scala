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
  def readTokens(src:Source) : Seq[String] = {
    val lineIterator = src.getLines
    val lines = lineIterator.toArray
    // process each line for tokens and accrue them in parallel
    val allTokens = lines.par.aggregate(Seq[String]()) (
      (seq,eachLine) => {
        val tokensPerLine = eachLine.split("\\s+")
        seq ++ tokensPerLine
      },
      _ ++ _
    )
    allTokens
  }
  
  def countWordsInTokens(tokens: Seq[String]) : Map[String,Int] = {
    val wordCountMap = tokens.par.aggregate( Map[String,Int]() )(
        (map, eachToken) => {
          val count = map.getOrElse(eachToken,0) + 1
          map + ( eachToken -> count )
        },
        _ ++ _
    )
    ( wordCountMap )
  }
}