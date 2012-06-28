package scala.impatient

import scala.io.Source

/**
 * Files and Regular Expressions
 */
class Exercises09 {
  /**
   * (9.1)
   */
  def reverseLines(filename: String) : Array[String] = {
    val src = Source.fromFile(filename, "UTF-8")
    val reversed = src.getLines.toArray.reverse
    (reversed)
  }
  /**
   * (9.2)
   */
  def replaceTabs(filename: String) = {
    val src = Source.fromFile(filename, "UTF-8")
    val lines = src.getLines.toArray
    val replaced = lines.map{ line =>
      line.replaceAll("\t","  ")
    }
    writefile(filename,replaced)
  }
  
  def writefile(filename: String, lines: Array[String]) = {
    val out = new java.io.PrintWriter(filename)
    lines.foreach{ line =>
      out.println(line)
    }
    out.close
  }
  /**
   * (9.3)
   */
  def printWordsWithMoreThan12Chars(filename: String) = {
    Source.fromFile(filename, "UTF-8").mkString.split("\\s+").filter( _.size > 12).foreach( System.out.print(_) )
  }
  /**
   * (9.4)
   */
  def average(filename: String) = {
    val floats = Source.fromFile(filename, "UTF-8").mkString.split("\\s+").map( _.toFloat ).toList
    val avg = ( floats.sum / floats.size )
    System.out.println("min: " + floats.min)
    System.out.println("max: " + floats.max)
    System.out.println("avg: " + avg)
  }
  
  def printSeq(strings : Seq[String]) = {
    strings.foreach{ System.out.println(_) }
  }
  /**
   * (9.7) TBD
   */
  def findAllNonFloats(filename:String) : Seq[String] = {
    val src = Source.fromFile(filename, "UTF-8")
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
    val src = Source.fromURL(httpAddress, "UTF-8")
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