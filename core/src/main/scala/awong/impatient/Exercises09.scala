package awong.impatient

import scala.io.Source

/**
 * Files and Regular Expressions
 */
object Exercises09 extends awong.calc.dsl.JavaTokens with awong.ScalaForImpatientExercise  {
  lazy val encoding = "UTF-8"
  /**
   * (9.1)
   */
  def reverseLines(filename: String) : Array[String] = {
    val src = Source.fromFile(filename, encoding)
    val reversed = src.getLines.toArray.reverse
    (reversed)
  }
  /**
   * (9.2)
   */
  def replaceTabs(filename: String) = {
    val src = Source.fromFile(filename, encoding)
    val lines = src.getLines.toArray
    val replaced = lines.map{ line =>
      line.replaceAll("\t","  ")
    }
    writefile(filename,replaced)
  }
  
  def writefile(filename: String, lines: Array[String]) = {
    val out = new java.io.PrintWriter(filename)
    lines.foreach{ line =>
      logger.debug(line)
    }
    out.close
  }
  /**
   * (9.3)
   */
  def printWordsWithMoreThan12Chars(filename: String) = {
    Source.fromFile(filename, encoding).mkString.split("\\s+").filter( _.size > 12).foreach( System.out.print(_) )
  }
  /**
   * (9.4)
   */
  def average(filename: String) = {
    val floats = Source.fromFile(filename, encoding).mkString.split("\\s+").map( _.toFloat ).toList
    val avg = ( floats.sum / floats.size )
    logger.debug("min: " + floats.min)
    logger.debug("max: " + floats.max)
    logger.debug("avg: " + avg)
  }
  
  def printSeq(strings : Seq[String]) = {
    strings.foreach{ logger.debug(_) }
  }
  /**
   * (9.7) TBD
   */
  def findAllNonFloats(filename:String) : Seq[String] = {
    val src = Source.fromFile(filename, encoding)
    val tokens = src.mkString.split("\\s+")
    val wsNumWsPattern = """\s+[0-9]+\s+""".r
    val integerPattern = """[0-9]+""".r
    val floatPattern = """[-+]?([0-9]*\.[0-9]+|\s+[0-9]+\s+)""".r
    val nonFloatingTokens = tokens.filterNot{ _.matches(floatPattern.toString) }
    (nonFloatingTokens)
  }
  def printNonFloats(filename:String) = {
    printSeq(findAllNonFloats(filename))
  }
  /**
   * (9.8) TBD : imgPattern is not working
   */
  def findSrcAttributes(httpAddress:String) = {
    val src = Source.fromURL(httpAddress, encoding)
    val tokens = src.mkString.split("\\s+")
    val attribute = ("title","xyz")
    val anchorPattern="""<a\s+([^>]*)href="(http?:\/\/([^"]*))"\s+([^>]*)""" + attribute._1 + "=\"" + attribute._2 +""""(.*?)>(.*?)<\/a>""".r
    val imgPattern="""<img\s+([^>]*)src="(http?:\/\/([^"]*))"\s+([^>]*)(.*?)>(.*?)<\/img>""".r
    val foundTokens = tokens.filterNot{ _.matches(imgPattern.toString) }
    (foundTokens)
    
  }
  def printSrcAttributes(httpAddress:String) = {
    printSeq(findAllNonFloats(httpAddress))
  }
  /**
   * (9.9)
   */
  def countClassExtensions(directory:String) = {
    val subdirectories = subdirs(new java.io.File(directory)).toList;
    val files = Seq[java.io.File]()
    subdirectories.foldLeft(files) {
      (f,subdir) => subdir.listFiles.filter( _.isFile ) ++ f
    }
    val classFiles = files.filter( _.getAbsolutePath.endsWith(".class") ) 
    (classFiles)
  }
  
  
  def subdirs(dir: java.io.File) : Iterator[java.io.File] = {
    val children = dir.listFiles.filter(_.isDirectory)
    children.toIterator ++ children.toIterator.flatMap(subdirs _)
  }
}