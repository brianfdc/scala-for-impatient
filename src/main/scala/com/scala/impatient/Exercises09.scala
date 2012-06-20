package com.scala.impatient

import scala.io.Source

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
    /**
     * (9.7) TBD
     */
    def printNonFloats(filename:String) = {
      val src = Source.fromFile(filename, "UTF-8")
      val tokens = src.mkString.split("\\s+")
    }
    /**
     * (9.8) TBD
     */
    def printSrcAttributes(httpAddress:String) = {
      val src = Source.fromURL(httpAddress, "UTF-8")
      val tokens = src.mkString.split("\\s+")
      
    }
    
    /**
     * (9.9) TBD
     */
    def countClassExtensions(directory:String) = {
      
    }
  }
}