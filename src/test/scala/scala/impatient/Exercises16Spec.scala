package scala.impatient

import scala.xml.Text
import scala.xml.Node
import scala.xml.Elem

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises16._

@RunWith(classOf[JUnitRunner])
class Exercises16Spec extends KoanSpec("Specs for Chapter 16"){
  var xhtml:Elem = _
  before {
    xhtml = <html>
              <head>
              </head>
              <body>
                <div id="2">
                  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
                  <img src="smiley.gif" alt="Smiley face" height="42" width="42" />
                  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</p>
                  <img src="unsmiley.gif" height="42" width="42" />
                </div>
                <div id="2">
                  <p>Lorem ipsum dolor sit amet, <a href="http://www.google.com">google</a> consectetur adipisicing elit,
                     sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
                     Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
                     nisi ut aliquip ex ea commodo consequat. <a href="http://www.twitter.com">twitter</a> Duis aute irure dolor in
                     reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.
                     Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia
                     deserunt mollit anim id est laborum.</p>
                  
                </div>

              </body>
            </html>
  }
  
  "(16.1)" should {
    "Reuturn nodes" in {
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
        println("Throws a matchError b/c the case expression is part of the syntax of an empedded expression")
      }
    }
  }
  
  
  "(16.4)" should {
    "Read an XHTML file" in {
      Exercises16.filterForAllImgsWithAlts(xhtml).size should be(1)
    }
  }
  "(16.5)" should {
    "Find all image names in an XHTML file" in {
      Exercises16.getAllImgNames(xhtml) should be(List("smiley.gif","unsmiley.gif"))
    }
  }
  "(16.6)" should {
    "Map all hrefs in an XHTML file" in {
      val hyperlinks = Exercises16.getAllHyperlinks(xhtml)
      hyperlinks.size should be (2)
      hyperlinks.values.toArray contains("google") should be (true)
      hyperlinks.values.toArray contains("twitter") should be (true)
      hyperlinks.keys.toArray contains("http://www.google.com") should be (true)
      hyperlinks.keys.toArray contains("http://www.twitter.com") should be (true)
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