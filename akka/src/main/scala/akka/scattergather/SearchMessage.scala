package akka.scattergather

import scala.actors.OutputChannel
import scala.collection.immutable._

sealed trait SearchMessage
case class SearchQuery(query: String, maxDocs: Int, gatherer: OutputChannel[QueryResponse]) extends SearchMessage
case class QueryResponse(results:Seq[ScoredDocument]) extends SearchMessage
/**
 * A message representing a document to add to the search tree.
 */
case class SearchableDocument(content: String) extends SearchMessage


case class ScoredDocument(score: Double, document: String)
