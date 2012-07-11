package scala.impatient

import scala.xml._
/**
 * XML Parsing
 */
object Exercises16 {
  /**
   * (16.1) are both scala.xml.Node, since Scala interprets "<fred/>" as an 
   * XML literal, and "(0)" as an accessor to the first element. All XML objects
   * in Scala are a Seq[Node]
   */
  def sixteenOne: (Any,Any) = {
    val fred0 = <fred/>(0)
    val fred1 = <fred/>(0)(0)
    (fred0,fred1)
  }
  
  /**
   * (16.4)
   */
  def filterForAllImgsWithAlts(xhtml: Node):List[Node] = {
    val imgs = xhtml  \\ "img"
    imgs.filter{ 
      _.attribute("alt").isDefined
    }.toList
  }
  /**
   * 
   */
  def getAllImgNames(xhtml: Node):List[String] = {
    val imgs = xhtml  \\ "img"
    imgs.flatMap{  _.attribute("src")  }.map{  _.toString  }.toList
  }
}