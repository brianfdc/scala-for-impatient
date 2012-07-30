package scala.impatient.parsing

import java.text.ParseException

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

import scala.collection.mutable.ListBuffer
import scala.xml._


/**
 * (19.5) Parses a subset of XML
 * 
 * EBNF for XML
 * 
 * @see http://www.jelks.nu/XML/xmlebnf.html
 * @see http://www.berniepope.id.au/docs/scala_parser_combinators.pdf
 */
class XmlLightParser extends RegexParsers { 
   override def skipWhitespace : Boolean = false 
   def document = declaration ~> optSpace ~> element <~ optSpace
   def declaration = "<?xml" ~ version ~ (encoding?) ~ (standalone?) ~ optSpace ~ "?>"
   def version = space ~ "version" ~ equals ~ string
   def encoding = space ~ "encoding" ~ equals ~ string
   def standalone = space ~ "standalone" ~ equals ~ (yes | no) 
   def element = nonEmpty | empty
   def nonEmpty = startTag ~ content ~ endTag >> mkNonEmpty
   def empty = "<" ~> name ~ attributes <~ optSpace <~ "/>" ^^ mkEmpty
   def startTag = "<" ~> name ~ attributes <~ optSpace <~ ">"
   def endTag = "</" ~> name <~ optSpace <~ ">"
   def attributes = (space ~> attribute *) ^^ mkAttributes 
   def content : Parser[List[Node]] = (charData?) ~ (element ~ (charData?) *) ^^ mkContent
   def attribute = (name <~ equals) ~ string 
   def equals = optSpace ~ "=" ~ optSpace
   def string = doubleString | singleString
   def optSpace = space?
   def charData = "[^<]+".r ^^ Text 
   def space = """\s+""".r
   def name = """(:|\w)((\-|\.|\d|:|\w))*""".r
   def doubleString = "\"" ~> """[^"]*""".r <~ "\"" 
   def singleString = "'" ~> "[^']*".r <~ "'"
   def yes = "\"yes\"" | "'yes'"
   def no = "\"no\"" | "'no'"

   private def mkAttributes = (list : List[String~String]) => 
      ((Null:MetaData) /: list.reverse) { 
         case (atts,key~value) => new UnprefixedAttribute(key,value,atts) 
      }

   private type NonEmpty = String~MetaData~List[Node]~String
   private def mkNonEmpty : NonEmpty => Parser[Node] = { 
      case startName~atts~children~endName => 
         if (startName == endName) 
            success (Elem(null, startName, atts, TopScope, children:_* ))
         else
            err("tag mismatch")
   }
   private def mkEmpty : String~MetaData => Node = { 
      case name~atts => Elem(null, name, atts, TopScope) 
   }

   private type Content = Option[Text]~List[Node~Option[Text]]
   private def mkContent : Content => List[Node] = { 
      case Some(text)~rest => text :: unpackContent(rest)
      case None~rest => unpackContent(rest) 
   }
   private def unpackContent (contents:List[Node~Option[Text]]) : List[Node] = {
      val acc = new ListBuffer[Node]
      for (node~chars <- contents) {
         if (chars isDefined) acc += chars.get
         acc += node
      }
      return acc.toList
   }
}