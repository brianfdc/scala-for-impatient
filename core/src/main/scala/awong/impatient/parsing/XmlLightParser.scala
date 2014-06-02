package awong.impatient.parsing

import java.text.ParseException

import scala.util.parsing.combinator._
import scala.util.parsing.input.CharArrayReader

import scala.xml.Text
import scala.xml.Node
import scala.xml.Elem
import scala.xml.MetaData
import scala.xml.TopScope
import scala.xml.UnprefixedAttribute
import scala.xml.Null



/**
 * (19.5) Parses a subset of XML
 * 
 * EBNF for XML
 * 
 * @see http://www.jelks.nu/XML/xmlebnf.html
 * @see http://www.berniepope.id.au/docs/scala_parser_combinators.pdf
 */
class XmlLightParser extends RegexParsers with PackratParsers {
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
	private type Declaration = String~Version~Option[Encoding]~Option[Standalone]~Option[String]~String
	def declaration: Parser[Declaration] = {
		"<?xml" ~ version ~ (encoding?) ~ (standalone?) ~ optSpace ~ "?>"
	}
	/**
	 * version := space "version" equals string
	 */
	private type Version = String~String~String~String
	def version: Parser[Version] = {
		space ~ "version" ~ equals ~ string
	}
	/**
	 * encoding := space "encoding" equals string
	 */
	private type Encoding = String~String~String~String
	def encoding: Parser[Encoding] = {
		space ~ "encoding" ~ equals ~ string
	}
	/**
	 * standalone := space "standalone" equals (yes | no)
	 */
	private type Standalone = String~String~String~String
	def standalone: Parser[Standalone] = {
		space ~ "standalone" ~ equals ~ (yes | no)
	}
	/**
	 * element := nonEmpty | empty
	 */
	lazy val element: PackratParser[Node] = {
		log(nonEmpty)("nonEmpty element") | log(empty)("empty element")
	}
	/**
	 * nonEmpty := startTag content endTag
	 */
	lazy val nonEmpty: PackratParser[Node] = {
		startTag ~ log(content)("content in nonEmpty") ~ endTag >> mkNonEmpty
	}
	/**
	 * empty := "<" name attributes space? "/>"
	 */
	lazy val empty: PackratParser[Node] = {
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
	lazy val attributes: PackratParser[MetaData] = {
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
	private type Attribute = String~String
	lazy val attribute: PackratParser[Attribute] = {
		(name <~ equals) ~ string 
	}
	/**
	 * equals := space? "=" space?
	 */
	def equals: Parser[String] = {
		optSpace ~> "=" <~ optSpace
	}
	
	lazy val string: PackratParser[String] = {
		doubleString | singleString
	}
	
	lazy val optSpace: PackratParser[Option[String]] = {
		space?
	}
	
	def charData: Parser[Text] = {
		// "[^<]+".r ^^ Text
		throw new IllegalArgumentException("backward compatability for scala.xml.Text parsing broken")
	}
	
	def space: Parser[String] = {
		"""\s+""".r
	}
	
	lazy val name: PackratParser[String] = {
		"""(:|\w)((\-|\.|\d|:|\w))*""".r ^^ { (a) => a}
	}
	lazy val doubleString: PackratParser[String] = {
		"\"" ~> """[^"]*""".r <~ "\""
	}
	lazy val singleString: PackratParser[String] = {
		"'" ~> "[^']*".r <~ "'"
	}
	/**
	 * yes := "yes" | 'yes'
	 */
	lazy val yes: PackratParser[String] = {
		"\"yes\"" | "'yes'"
	}
	/**
	 * no := "no" | 'no'
	 */
	lazy val no: PackratParser[String] = {
		"\"no\"" | "'no'"
	}
	
	def parse(text: String): Node = {
		parseAll(document, text) match {
			case Success(doc, _) => doc
			case NoSuccess(msg, _) => throw new ParseException(msg.toString, 0)
		}
	}

	private def mkAttributes = {
		(list : List[Attribute]) => {
			((Null:MetaData) /: list.reverse) {
				 case (atts,key~value) => new UnprefixedAttribute(key,value,atts) 
			}
		}
	}
	
	private type NonEmpty = String~MetaData~List[Node]~String
	private def mkNonEmpty : NonEmpty => Parser[Node] = {
		case startName~atts~children~endName => {
			if (startName == endName) 
				success(Elem(null, startName, atts, TopScope, children:_* ))
			else
				err("tag mismatch:" + startName + "!=" +endName)
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