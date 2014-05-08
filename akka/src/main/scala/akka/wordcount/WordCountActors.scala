package akka.wordcount


import java.io.File
import scala.io.Source
import akka.event.Logging
import akka.actor._
import akka.routing.RoundRobinRouter
import akka.dispatch.Future
import akka.util.duration._
import scala.util.parsing.combinator.JavaTokenParsers
import akka.util.Timeout.durationToTimeout


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
object WordCountActors extends App with MyJavaTokens {
	run
	
	def run() {
		val directory = "/Users/awong/Documents/projects/src/scala-for-impatient/"
		val regex = identifier.pattern.toString
		
		val system = ActorSystem("WordCountSystem")
		val supervisor = newSupervisor(system)
		
		val future: Future[RegexSummary] = supervisor.ask( Start(regex, directory) )(120 seconds) mapTo manifest[RegexSummary]
		future onComplete {
			case Right(regexSummary) => {
				println("Received RegexSummary future successfully")
				val allMatchingWords = regexSummary.wordCountMatch.keySet
				val totalNumberOfMatches = regexSummary.wordCountMatch.values.reduceLeft( _ + _ )
				println("Total number of matches: " + totalNumberOfMatches)
				println("Total number of words:	 " + allMatchingWords.size)
				val isPrint = false;
				if (isPrint) {
					regexSummary.wordCountMatch.foreach{ (entry) =>
						println("Match: " + entry._1 + "\t\tCount:" + entry._2)
					}
					regexSummary.wordFilesMap.foreach{ (entry) =>
						println("Match: " + entry._1 + "\t\t Files:" + entry._2.mkString(","))
					}
				}
				system.shutdown()
			}
			case Left(failure) => {
				println("Failed to receive RegexSummary future")
				system.shutdown()
			}
		}
	}
	
	private def newSupervisor(system: ActorSystem): ActorRef = {
		system.actorOf( Props(new WordCountSupervisor()),
				name = "wordCountSupervisor")
	}
}

trait MyJavaTokens extends JavaTokenParsers {
		val identifier = """[a-zA-Z_]\w*""".r

}

trait WordCountActor extends Actor {
	val log = Logging(context.system, this)
	var done = false;
	val encoding = "UTF-8"
}

class WordCountSupervisor extends WordCountActor {
	var initial: ActorRef = _
	
	def receive = {
		case msg: Start => {
			log.info("WordCountSupervisor: handling Start")
			log.info("WordCountSupervisor: storing reference to initial actor to return a future")
			initial = sender
			val visitor = newDirectoryVisitor
			visitor ! RecurseFiles(msg.regex, msg.directory)
			visitor ! WordCountShutdown
		}
		case msg: DirectoryResult => {
			log.info("WordCountSupervisor: handling DirectoryResult")
			val noFileReaderNodes = msg.files.size
			val gatherer = newRegexGatherer(msg.regex, noFileReaderNodes, self)
			gatherer ! msg
		}
		case msg: RegexSummary => {
			log.info("WordCountSupervisor: forwarding RegexSummary as a redeemed Future \n\n")
			initial ! msg
		}
	}

	private def newRegexGatherer(_regex:String, _nodes: Int, supervisor:ActorRef): ActorRef = {
		log.info("WordCountSupervisor: Creating RegexGatherer")
		context.actorOf( Props(new RegexGatherer(_regex, _nodes, supervisor)),
				name = "regexGatherer")
	}
	
	private def newDirectoryVisitor: ActorRef = {
		log.info("Creating DirectoryVisitor")
		context.actorOf( Props(new DirectoryVisitor()),
				name = "directoryVisitor")
	}
}


class DirectoryVisitor extends WordCountActor {
	def receive() = {
		case msg: RecurseFiles => {
			log.info("DirectoryVisitor: handling RecurseFiles")
			val result = recurseFiles(msg)
			log.info("DirectoryVisitor: attempting to send DirectoryResult back to WordCountSupervisor")
			sender ! result
		}
		case msg:WordCountShutdown => {
			log.info("shutting down DirectoryVisitor")
			context.stop(self)
		}
	}
	private def recurseFiles(m: RecurseFiles): DirectoryResult = {
		val subdirectories = subdirs(new File(m.directory))
		log.info("DirectoryVisitor: found subdirectories")
		val allFiles = listFiles(subdirectories).filter( _.isFile )
		log.info("DirectoryVisitor: listing Files")
		val srcFiles = allFiles.filter{ f =>
			f.getAbsolutePath.endsWith(".java") || f.getAbsolutePath.endsWith(".scala")
		}
		log.info("DirectoryVisitor: filtered for java/scala source files")
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

class RegexGatherer(_regex:String, _nodes:Int, supervisor: ActorRef) extends WordCountActor with WordCounter {
	var summary: RegexSummary = initRegexSummary(_regex)
	var nrOfResults: Int = 0
	
	val fileReadRouter = context.actorOf(Props[FileReadActor].withRouter(RoundRobinRouter(_nodes)), name = "fileReadRouter")

	def receive = {
		case msg:DirectoryResult => {
			// Ask each fileReadActor to handle a file
			msg.files.foreach{ (file) =>
				fileReadRouter ! RegexMatch(msg.regex,file)
			}
		}
		case msg:RegexResults => {
			nrOfResults += 1
			summary = combineResults(msg, summary)
			if (nrOfResults == _nodes) {
				log.info("Return the result to the supervisor")
				supervisor ! summary
			}
		}
		case msg:WordCountShutdown => {
			log.info("shutting down RegexGatherer & its children")
			context.stop(self)
		}
	}
	private def initRegexSummary(_regex: String) = {
		RegexSummary(_regex, Map[String,Int](), Map[String,List[File]]() )
	}
	
	private def combineWordFilesMap(current:RegexResults, next:RegexSummary): Map[String,List[File]] = {
		current.matches.foldLeft( next.wordFilesMap ){ (map,eachToken) =>
			val files = current.file :: map.getOrElse(eachToken,List[File]())
			map + ( eachToken -> files )
		}
	}
	private def combineResults(current: RegexResults, next:RegexSummary): RegexSummary = {
		var matches = countWordsSerially(current.matches) ++ next.wordCountMatch
		val wordFilesMap = combineWordFilesMap(current, next)
		next.copy(wordCountMatch = matches, wordFilesMap = wordFilesMap)
	}
	
}

class FileReadActor extends WordCountActor with Lexer {
	def receive() = {
		case msg:RegexMatch => {
			log.info("FileReadActor: handling RegexMatch for: " + msg.file.getAbsolutePath)
			sender ! RegexResults(msg.regex, msg.file, findMatches(msg))
		}
	}
	private def findMatches(regexMatch: RegexMatch): Seq[String] = {
		val source = Source.fromFile(regexMatch.file, encoding)
		val text = toText(source)
		val matches = regexMatch.regex.r.findAllIn(text).toSeq
		matches
	}
}

import java.io.File

sealed trait WordCountMessage
case class Start(regex: String, directory: String) extends WordCountMessage
case class RecurseFiles(regex: String, directory: String) extends WordCountMessage
case class DirectoryResult(regex: String, directories: List[File], files: List[File]) extends WordCountMessage
case class RegexMatch(regex: String, file:File) extends WordCountMessage
case class RegexResults(regex: String, file: File, matches: Seq[String]) extends WordCountMessage
case class RegexSummary(regex: String, wordCountMatch: Map[String, Int], wordFilesMap: Map[String,List[File]]) extends WordCountMessage
case class WordCountShutdown() extends WordCountMessage
