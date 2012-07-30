package scala.impatient.parsing

import java.text.ParseException

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

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
  /**
   * document := declaration ~ space? element space?
   */
  def document: Parser[Node] = {
    log(declaration)("declaration") ~> optSpace ~> log(element)("building root element") <~ optSpace
  }
  /**
   * declaration := "<?xml" version encoding? standalone? space? "?>"
   */
  def declaration = {
    "<?xml" ~ version ~ (encoding?) ~ (standalone?) ~ optSpace ~ "?>"
  }
  /**
   * version := space "version" equals string
   */
  def version = {
    space ~ "version" ~ equals ~ string
  }
  /**
   * encoding := space "encoding" equals string
   */
  def encoding = {
    space ~ "encoding" ~ equals ~ string
  }
  /**
   * standalone := space "standalone" equals (yes | no)
   */
  def standalone = {
    space ~ "standalone" ~ equals ~ (yes | no)
  }
  /**
   * element := nonEmpty | empty
   */
  def element: Parser[Node] = {
    log(nonEmpty)("nonEmpty element") | log(empty)("empty element")
  }
  /**
   * nonEmpty := startTag content endTag
   */
  def nonEmpty: Parser[Node] = {
    startTag ~ log(content)("content in nonEmpty") ~ endTag >> mkNonEmpty
  }
  /**
   * empty := "<" name attributes space? "/>"
   */
  def empty: Parser[Node] = {
    "<" ~> log(name)("name of empty tag") ~ log(attributes)("attributes in empty tag") <~ optSpace <~ "/>" ^^ mkEmpty
  }
  /**
   * startTag := "<" name attributes space? ">"
   */
  def startTag: Parser[String~MetaData] = {
    "<" ~> log(name)("name of startTag") ~ log(attributes)("attributes in startTag") <~ optSpace <~ ">"
  }
  /**
   * endTag := "</" name space? ">"
   */
  def endTag: Parser[String] = {
    "</" ~> log(name)("name of endTag") <~ optSpace <~ ">"
  }
  /**
   * attributes := (space attribute)*
   */
  def attributes: Parser[MetaData] = {
    (space ~> attribute *) ^^ mkAttributes
  }
  /**
   * content := charData? (element charData?)*
   */
  def content: Parser[List[Node]] = {
    (charData?) ~ (element ~ (charData?) *) ^^ mkContent
  }
  /**
   * attribute := name equals string
   */
  def attribute: Parser[String~String] = {
    (name <~ equals) ~ string 
  }
  /**
   * equals := space? "=" space?
   */
  def equals: Parser[String] = {
    optSpace ~> "=" <~ optSpace
  }
  
  def string: Parser[String] = {
    doubleString | singleString
  }
  
  def optSpace: Parser[Option[String]] = {
    space?
  }
  
  def charData: Parser[Text] = {
    "[^<]+".r ^^ Text
  }
  
  def space: Parser[String] = {
    """\s+""".r
  }
  
  def name = {
    """(:|\w)((\-|\.|\d|:|\w))*""".r
  }
  def doubleString: Parser[String] = {
    "\"" ~> """[^"]*""".r <~ "\""
  }
  def singleString: Parser[String] = {
    "'" ~> "[^']*".r <~ "'"
  }
  /**
   * yes := "yes" | 'yes'
   */
  def yes: Parser[String] = {
    "\"yes\"" | "'yes'"
  }
  /**
   * no := "no" | 'no'
   */
  def no: Parser[String] = {
    "\"no\"" | "'no'"
  }
  
  def parse(text: String): Node = {
    parseAll(document, text) match {
      case Success(doc, _) => doc
      case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
    }
  }

  private def mkAttributes = {
    (list : List[String~String]) => {
      ((Null:MetaData) /: list.reverse) {
         case (atts,key~value) => new UnprefixedAttribute(key,value,atts) 
      }
    }
  }
  
  private type NonEmpty = String~MetaData~List[Node]~String
  private def mkNonEmpty : NonEmpty => Parser[Node] = {
    case startName~atts~children~endName => {
      if (startName == endName) 
        success (Elem(null, startName, atts, TopScope, children:_* ))
      else
        err("tag mismatch")
    }
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
    import scala.collection.mutable.ListBuffer
    val acc = new ListBuffer[Node]
    for (node~chars <- contents) {
       if (chars isDefined) acc += chars.get
       acc += node
    }
    acc.toList
  }
}