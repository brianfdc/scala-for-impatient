package scala.impatient.actors

import scala.actors._
import scala.collection.immutable._
/**
 * Scatter-gather example from Josh Suereth, _Scala in Depth_, chp 9 (Manning: 2012)
 */
object SearchZone extends App {
  run
  
  def run = {
    /*
     *  In v1 do nothing, since the HeadNode does not return a statically
     *  typed future. Also HeadNode blocks for an entire SearchQuery.
     *  Lastly, the search has no failure handling
     */
    
  }
}
trait SearchLike {
  type ScoredDocument = (Double, String)
}

sealed trait SearchNodeMessage {
  type ScoredDocument = (Double, String)
}
case class SearchQuery(query: String, maxDocs: Int, gatherer: OutputChannel[QueryResponse]) extends SearchNodeMessage
case class QueryResponse(results:Seq[(Double,String)]) extends SearchNodeMessage with SearchLike


trait SearchNode extends Actor with SearchLike {
  lazy val index = HashMap[String, Seq[ScoredDocument]]()
  override def act() = {
    loop {
      /**
       * {@code react} is better than {@code receive} for message reception,
       * because it will defer the actor execution until there is a message
       * available. {@code Receive} will block the current thread until
       * there is a message available.
       */
      react {
        case SearchQuery(query, maxDocs, requester) =>
          val result =  index.get(query).getOrElse(Seq()).take(maxDocs)
          requester ! QueryResponse(result)
      }
    }
  }
}

trait GathererNode extends Actor with SearchLike {
  // max # of docs to retrun from a query
  val maxDocs: Int
  // max # of nodes that can response b4 sending results for a query
  val maxResponses: Int
  // target for results to send
  val client: OutputChannel[QueryResponse]
  
  override def act() = {
    bundleResult(0, Seq())
  }
  /**
   * curCount is # of responses seen thus far
   * 
   */
  private def bundleResult(curCount: Int, current: Seq[ScoredDocument]): Unit = {
    if (curCount < maxResponses) {
      receiveWithin(1000L) {
        case QueryResponse(results) =>
          bundleResult(curCount + 1, combineResults(current,results))
        case TIMEOUT => 
          bundleResult(maxResponses,current)
      }
    } else {
      client ! QueryResponse(current)
    }
  }
  private def combineResults(current: Seq[ScoredDocument], next:Seq[ScoredDocument]) = {
    // view and force methods help efficiency by circumventing creation of intermediate collections
    (current ++ next).view.sortBy(_._1).take(maxDocs).force
  }
}

trait HeadNode extends Actor with SearchLike {
  // all nodes that head can scatter to.
  val nodes = Seq[OutputChannel[SearchNodeMessage]]()
  override def act() = {
    react {
      case SearchQuery(q, max, responder) => {
        // send message to each search node to search & await a future
        val gatherer = new GathererNode {
          val maxDocs = max
          val maxResponses = nodes.size
          val client = responder
        }
        gatherer.start()
        nodes.foreach{ _ ! SearchQuery(q,max,gatherer) }
        act
      }
    }
  }
  override def toString = {
    "HeadNode with {\n\t" + nodes.size + " search nodes\n" + nodes.mkString("\t") + "\n\t\n"
  }
}