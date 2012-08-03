package scala.impatient.actors


import java.io.File

import scala.actors._
import scala.actors.scheduler._
import scala.io.Source

/**
 * (20.3) Write a program that counts how many words
 * match a given regex in all files of all subdirectories
 * of a given directory. Have one actor per file,
 * one actor that traverses the subdirectories, one
 * actor to accumulate the results.
 * 
 * (20.4) Modify the program of the preceding exercise
 * to display all the matching words.
 * 
 * (20.5) Modify the program of the preceding exercises to display
 * all matching words, each with the list of files containing it.
 * 
 * (20.7) add a supervisor that monitors the file reading actors and 
 * logs any exception with an IOException.
 */
object WordCountActors extends App with scala.calc.dsl.JavaTokens {
  run
  
  def run() {
    val directory = "/Users/awong/Documents/projects/src/scala-for-impatient/src/test/java"
    val regex = ident.pattern.toString
    val supervisor = newSupervisor()
    supervisor ! Start(regex, directory)
  }
  
  private def newSupervisor(): WordCountSupervisor = {
    println("Creating WordCountSupervisor")
    val supervisor = new WordCountSupervisor {
      val numThreadForSearchTree = 5
    }
    supervisor.start()
    supervisor
  }
}

trait WordCountActor extends Actor {
  var done = false;
  val encoding = "UTF-8"
}

trait WordCountSupervisor extends WordCountActor {
  val self = WordCountSupervisor.this
  val numThreadForSearchTree:Int
  
  def act() {
    loopWhile(!done) {
      react {
        case m: Start => {
          println("WordCountSupervisor: handling Start")
          val visitor = newDirectoryVisitor
          visitor ! RecurseFiles(m.regex, m.directory)
        }
        case m: DirectoryResult => {
          println("WordCountSupervisor: handling DirectoryResult")
          val noFileReaderNodes = m.files.size
          val s = newScheduler
          val gatherer = newRegexGatherer(m.regex, noFileReaderNodes, s)
          
          val fileReaders = m.files.map { (_file) =>
            newFileReadActor(_file, gatherer, s)
          }
          fileReaders.foreach{
            _ ! RegexMatch(m.regex)
          }
        }
        case m: RegexSummary => {
          println("WordCountSupervisor: handling RegexSummary")
          // handle results of regexSummary
          m.wordCountMatch.foreach{ (entry) =>
            println("Match: " + entry._1 + "\t\tCount:" + entry._2)
          }
        }
      }
    }
  }
  private def newScheduler(): IScheduler = {
    val numProcessors = java.lang.Runtime.getRuntime.availableProcessors
    val scheduler = new ForkJoinScheduler(
              initCoreSize = numProcessors,
              maxSize = numThreadForSearchTree,
              daemon = false,
              fair = true)
    scheduler
  }
  private def newRegexGatherer(_regex:String, _nodes: Int, s:IScheduler) : RegexGatherer = {
    println("Creating RegexGatherer")
    val node = new RegexGatherer {
      val regex = _regex
      val maxResponses = _nodes
      val client = new Channel[RegexSummary](self)
      override val scheduler = s
    }
    self link node
    node.start()
    node
  }
  
  private def newFileReadActor(_file: File, gatherer: RegexGatherer, s: IScheduler) : FileReadActor = {
    println("Creating FileReadActor")
    val node = new FileReadActor {
      val file = _file
      val client = new Channel[RegexResults](gatherer)
      override val scheduler = s
    }
    self link node
    node.start()
    node
  }
  
  private def newDirectoryVisitor: DirectoryVisitor = {
    println("Creating DirectoryVisitor")
    val node = new DirectoryVisitor{
      val client = new Channel[DirectoryResult](self)
    }
    self link node
    node.start()
    node
  }
}


trait DirectoryVisitor extends WordCountActor {
  val self = DirectoryVisitor.this
  // target to send results
  val client: OutputChannel[DirectoryResult]
  
  def act() {
    loopWhile(!done) {
      react {
        case m: RecurseFiles => {
          println("DirectoryVisitor: handling RecurseFiles")
          val result = recurseFiles(m)
          println("DirectoryVisitor: attempting to send DirectoryResult")
          client ! result
        }
      }
    }
  }
  private def recurseFiles(m: RecurseFiles): DirectoryResult = {
    val subdirectories = subdirs(new File(m.directory))
    println("DirectoryVisitor: found subdirectories")
    val allFiles = listFiles(subdirectories).filter( _.isFile )
    println("DirectoryVisitor: listing Files")
    val srcFiles = allFiles.filter{ f =>
      f.getAbsolutePath.endsWith(".java") || f.getAbsolutePath.endsWith(".scala")
    }
    println("DirectoryVisitor: filtered for java/scala source files")
    DirectoryResult(m.regex, subdirectories, srcFiles)
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
  
  def toText(src:Source): String = {
    src.mkString
  }
}

trait RegexGatherer extends WordCountActor with WordCounter {
  val self = RegexGatherer.this
  val millis = 1000L
  val regex: String
  // max # of nodes that can response b4 sending results for a query
  val maxResponses: Int
  // target to send results
  val client: OutputChannel[RegexSummary]
  
  override def act() = {
    val summary = RegexSummary(regex, Map[String, Int]())
    bundleResult(0, summary)
  }
  /**
   * curCount is # of responses seen thus far
   */
  private def bundleResult(curCount: Int, summary: RegexSummary): Unit = {
    if (curCount < maxResponses) {
      receiveWithin(millis) {
        case current: RegexResults =>
          println("RegexGatherer: handling RegexResults")
          bundleResult( curCount + 1, combineResults(current,summary) )
        case TIMEOUT => 
          println("RegexGatherer: handling TIMEOUT")
          bundleResult(maxResponses,summary)
      }
    } else {
      client ! summary
    }
  }
  private def combineResults(current: RegexResults, next:RegexSummary): RegexSummary = {
    var matches = countWordsSerially(current.matches) ++ next.wordCountMatch
    next.copy(wordCountMatch = matches)
    
    next
  }
  
}

trait FileReadActor extends WordCountActor with Lexer {
  val self = FileReadActor.this
  val client: OutputChannel[RegexResults]
  val file: File
  
  def act() {
    loopWhile(!done) {
      react {
        case m: RegexMatch => {
          println("FileReadActor: handling RegexMatch")
          client ! RegexResults(m.regex, file, findMatches(m))
        }
      }
    }
  }
  private def findMatches(regexMatch: RegexMatch): Seq[String] = {
    val source = Source.fromFile(file, encoding)
    val text = toText(source)
    val matches = regexMatch.regex.r.findAllIn(text).toSeq
    matches
  }
}