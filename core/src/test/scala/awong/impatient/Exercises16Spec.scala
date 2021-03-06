package awong.impatient

import scala.xml.Text
import scala.xml.Node
import scala.xml.Elem
import scala.xml.XML

import scala.io.Source

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.AbstractWordSpec

@RunWith(classOf[JUnitRunner])
class Exercises16Spec extends AbstractWordSpec {
  "(16.1)" should {
    "Return nodes" in {
      val x = Exercises16.sixteenOne
      x._1.isInstanceOf[scala.xml.Node] should be(true)
      x._2.isInstanceOf[scala.xml.Node] should be(true)
    }
  }
  "(16.2)" should {
    "Be an XML tree" in {
      // won't be accepted as a literal per textbook, need double braces
      val ul = <ul>
                 <li>Opening Bracket: [</li>
                 <li>Closing Bracket: ]</li>
                 <li>Opening Brace: {{</li>
                 <li>Closing Brace: }}</li>
               </ul>
      ul.isInstanceOf[scala.xml.Elem] should be(true)
      ul.child.length should be (9)
    }
  }
  "(16.3)" should {
    "match some expressions" in {
      def matcher(node:Node) = node match {  case <li>{Text(t)}</li> => t  }
      
      val x = matcher(<li>Fred</li>)
      x should be("Fred")
      intercept[MatchError] {
        val y = matcher(<li>{"Fred"}</li>)
        logger.debug("Throws a matchError b/c the case expression is part of the syntax of an empedded expression")
      }
    }
  }
  
  def getXhtml: Option[Elem] = {
    val src = resourceAsStreamFromSrc("testfile.xhtml")
    src match {
      case Some(xmlSource) => 
        Some(XML.loadString(xmlSource.mkString))
      case None => 
        None
    }
  }
  
  "(16.4/5/6)" should {
    "Read an XHTML file" in {
      getXhtml match {
        case Some(xhtml) =>
          Exercises16.filterForAllImgsWithAlts(xhtml).size should be(1)
        case None =>
          fail
      }
    }
    "Find all image names in an XHTML file" in {
      getXhtml match {
        case Some(xhtml) =>
          Exercises16.getAllImgNames(xhtml) should be(List("smiley.gif","unsmiley.gif"))
        case None =>
          fail
      }
    }
    "Map all hrefs in an XHTML file" in {
      getXhtml match {
        case None =>
          fail
        case Some(xhtml) =>
          val hyperlinks = Exercises16.getAllHyperlinks(xhtml)
          hyperlinks.size should be (2)
          hyperlinks.values.toArray contains("google") should be (true)
          hyperlinks.values.toArray contains("twitter") should be (true)
          hyperlinks.keys.toArray contains("http://www.google.com") should be (true)
          hyperlinks.keys.toArray contains("http://www.twitter.com") should be (true)
      }
    }
  }
  "(16.7/8)" should {
    "Map to dl" in {
      val map = Map("A" -> "1", "B" -> "2")
      val nodes = Exercises16.toDl(map);
      nodes.child.size should be (4)
    }
    "Reverse map to dl" in {
      val map = Map("A" -> "1", "B" -> "2")
      val nodes = Exercises16.toDl(map);
      Exercises16.mapDl(nodes) should be (map)
      
    }
  }
}