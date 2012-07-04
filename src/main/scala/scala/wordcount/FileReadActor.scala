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
    val allTokens = lines.par.aggregate(Seq[String]()) ( (seq,eachLine) => {
        val tokensPerLine = eachLine.split("\\s+")
        seq ++ tokensPerLine
      },{
        _ ++ _
      })
    allTokens
  }
  
  def countWordsInTokensInParallel(tokens: Seq[String]) : Map[String,Int] = {
    val parallelizedTokens = tokens.par
    // Map each token into a map from tokens to a list of counts
    val wordCountMap = parallelizedTokens.aggregate( Map[String,List[Int]]() )(
        (map, eachToken) => {
          val counts = map.getOrElse(eachToken,List[Int]())
          // add a tick for each extant token
          val newCount = if (counts.isEmpty) {
            1 :: counts
          } else {
            List[Int](counts.head + 1)
          }
          map + ( eachToken -> newCount )
        },
        (m1,m2) => {
          val mappedTokens = (m1.keySet union m2.keySet )
          val reducedResult = mappedTokens.foldLeft( Map[String,List[Int]]() )( (mappedMap,eachToken) => {
              val sums = m1.getOrElse(eachToken, List[Int](0)) ::: m2.getOrElse(eachToken, List[Int](0))
              mappedMap + (eachToken -> sums )
          })
          ( reducedResult )
        })
    // finally reduce the wordCount map
    wordCountMap.map( (each) => (each._1, each._2.sum) ).toMap
  }
  
  def countWordsSerially(tokens: Seq[String]) : Map[String,Int] = {
    tokens.foldLeft( Map[String,Int]() ){
      (map,eachToken) => map + ( eachToken -> (map.getOrElse(eachToken,0) + 1 ) )
    }
  }
  
}