package scala.impatient.actors

import java.io.File

sealed trait WordCountMessage
case class Start(regex: String, directory: String) extends WordCountMessage
case class RecurseFiles(regex: String, directory: String) extends WordCountMessage
case class DirectoryResult(regex: String, directories: List[File], files: List[File]) extends WordCountMessage
case class RegexMatch(regex: String) extends WordCountMessage
case class RegexResults(regex: String, file: File, matches: Seq[String]) extends WordCountMessage
case class RegexSummary(regex: String, wordCountMatch: Map[String, Int]) extends WordCountMessage
