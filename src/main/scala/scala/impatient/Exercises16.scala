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
   * (16.5)
   */
  def getAllImgNames(xhtml: Node):List[String] = {
    val imgs = xhtml  \\ "img"
    imgs.flatMap{  _.attribute("src")  }.map{  _.toString  }.toList
  }
  /**
   * (16.6)
   */
  def getAllHyperlinks(xhtml:Node):Map[String, String] = {
    val as = xhtml  \\ "a"
    val hrefs = as.map{  _.attribute("href").head.toString }
    val childTexts = as.map{  _ match {  case <a>{Text(t)}</a> => t  }  }
    (hrefs zip childTexts).toMap
  }
  /**
   * (16.7)
   */
  def toDl(map: Map[String,String]): Node = {
    val dl = <dl>{for ((k,v) <- map) yield <dt>{k}</dt><dd>{v}</dd>}</dl>
    dl
  }
  /**
   * (16.8)
   */
  def mapDl(dl: Node): Map[String,String] = {
    val dts = dl \\ "dt"
    val dds = dl \\ "dd"
    val xf = ( dts.map( _.text ) zip  dds.map( _.text ) )
    xf.toMap
  }
}