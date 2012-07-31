package scala.wordcount


import java.io.File

import scala.actors.Actor
import scala.io.Source

/**
 * (20.3) Write a program that counts how many words
 * match a given regex in all files of all subdirectories
 * of a given directory. Have one actor per file,
 * one actor that traverses the subdirectories, one
 * actor to accumulate the results.
 * 
 * (20.4) Modify the program of the preceding exercise
 * to display all the maching words.
 * 
 * (20.5) Modify the program of the preceding exercies to display
 * all matching words, each with the list of files containing it.
 * 
 * (20.7) add a supervisor that monitors the file reading actors and 
 * logs any exception with an IOException.
 */
object WordCountActors extends App with scala.calc.dsl.JavaTokens {
  run
  
  def run() {
    val directory = "/Users/awong/projects/src"
    val regex = ident.pattern.toString
    val supervisor = new WordCountSupervisor
    supervisor.start()
    
    val future = supervisor !! Start(directory, regex)
    future() match {
      case s: Map[String,String] => println(s)
      case _ => throw new IllegalArgumentException("fail!")
    }
  }
}
trait WordCountActor extends Actor {
  var done = false;
  val supervisor: WordCountSupervisor
  val encoding = "UTF-8"
}

sealed trait WordCountMessage
case class Start(directory: String, regex: String) extends WordCountMessage
case class RecurseFiles(directory: String) extends WordCountMessage
case class DirectoryResult(directories: List[File], files: List[File]) extends WordCountMessage
case class RegexMatch(regexExpression: String) extends WordCountMessage
case class Shutdown() extends WordCountMessage


class WordCountSupervisor extends WordCountActor {
  val supervisor = this
  var regex = ""
  
  def act() {
    loopWhile(!done) {
      react {
        case m: Start => {
          val visitor = new DirectoryVisitor(this)
          regex = m.regex
          visitor.start()
          visitor ! RecurseFiles(m.directory)
        }
        case m: DirectoryResult => {
          val fileReaders = m.files.map { (file) =>
            new FileReadActor(this, file).start()
          }
          fileReaders.foreach{
            _ ! RegexMatch(regex)
          }
        }
        case m: Shutdown => {
          done = true
          val result = Map[String,String]()
          reply(result)
        }
      }
    }
  }
  
}

class DirectoryVisitor(val supervisor: WordCountSupervisor) extends WordCountActor {
  def act() {
    while (!done) {
      receive {
        case m: RecurseFiles => {
          val subdirectories = subdirs(new File(m.directory))
          val allFiles = listFiles(subdirectories).filter( _.isFile )
          val srcFiles = allFiles.filter{ f =>
            f.getAbsolutePath.endsWith(".java") || f.getAbsolutePath.endsWith(".scala")
          } 
          supervisor ! DirectoryResult(subdirectories, srcFiles)
          this ! Shutdown()
        }
        case m: Shutdown => {
          done = true
        }
      }
    }
  }
  private def subdirs(dir: File) : List[File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    val all = children.toIterator ++ children.toIterator.flatMap(subdirs _)
    all.toList
  }
  private def listFiles(subdirectories: List[File]): List[File] = {
    subdirectories.foldLeft(List[File]()) {
      (fileList,subdir) => subdir.listFiles.filter( _.isFile ).toList ::: fileList
    }
  }
}

trait WordCounter {
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

trait Lexer {
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
}


class FileReadActor(val supervisor: WordCountSupervisor, val file: File) extends WordCountActor with Lexer {
  def act() {
    while(!done) {
      receive {
        case m: RegexMatch => {
          val source = Source.fromFile(file, encoding)
          val tokens = readTokens(source)
          val regex = m.regexExpression.r
          (source)
        }
      }
    }
  }
}