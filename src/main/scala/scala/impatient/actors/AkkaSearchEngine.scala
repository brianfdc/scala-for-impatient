package scala.impatient.actors

import akka.actor._
import akka.actor.Actor

import scala.collection.immutable.HashMap
/**
 * Scatter-gather example from end of Josh Suereth, _Scala in Depth_, chp 9 (Manning: 2012)
 * 
 * It relies on Akka 1 ... refactor so that it uses Akka 2
 */
object AkkaSearchEngine {

}
/*
trait LeafNode { self: AdaptiveSearchNode =>
  var documents = Vector[String]()
  val MAX_DOCUMENTS = 500
  var index = HashMap[String,Seq[ScoredDocument]]()
  def leafNode: PartialFunction[Any,Unit] = {
    case SearchQuery(query, maxDocs, handler) =>
      executeLocalQuery(query,maxDocs, handler)
    case SearchableDocument(content) =>
      addDocumentToLocalIndex(content)
  }
  private def executeLocalQuery(query: String, maxDocs:Int, handler: ActorRef) = {
    val results =  index.get(query).getOrElse(Seq()).take(maxDocs)
    handler ! QueryResponse(results)
  }
  private def addDocumentToLocalIndex(content:String) = {
    documents = documents :+ content
    if (documents.size > MAX_DOCUMENTS) split() 
    else {
      for ( (k,v) <- content.split("\\s+").groupBy(identity)) {
        val list = index.get(k).getOrElse(Seq())
        val doc = ScoredDocument(v.length.toDouble, content)
        index += ( (k, doc +: list) )
      } 
    }
  }
  protected def split(): Unit
}

trait ParentNode { self: AdaptiveSearchNode =>
  var children = IndexedSeq[ActorRef]()
  val actorSystem = ActorSystem("sys")
  def parentNode: PartialFunction[Any,Unit] = {
    case SearchQuery(q,max,responder) =>
      val gatherer: ActorRef = actorSystem.actorOf(Props(new GathererNode {
        val maxDocs = max
        val maxResponses = children.size
        val query = q
        val client = responder
      }))
  }
}

class AdaptiveSearchNode extends Actor with ParentNode with LeafNode {
  def receive = leafNode
  protected def split(): Unit = {
    
  }
}
*/