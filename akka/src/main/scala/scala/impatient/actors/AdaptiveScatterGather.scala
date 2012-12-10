package scala.impatient.actors

import collection.immutable.HashMap
//import akka.actor.{Supervisor,ReceiveTimeout, ActorRef, Actor}
//import akka.config.Supervision._
//import akka.dispatch.Dispatchers

import scala.collection.immutable.HashMap
/**
 * Scatter-gather example from end of Josh Suereth, _Scala in Depth_, chp 9 (Manning: 2012)
 * 
 * It relies on Akka 1 ... refactor so that it uses Akka 2
 * 
 * @see https://github.com/jsuereth/scala-in-depth-source
 */
/*
object AdaptiveSearchTreeMain {
  def submitInitialDocuments(searchNode: ActorRef) = {
    Seq("Some example data for you",
        "Some more example data for you to use",
        "To be or not to be, that is the question",
        "OMG it's a cat",
        "This is an example.  It's a great one",
        "HAI there", 
        "HAI IZ HUNGRY",
        "Hello, World",
        "Hello, and welcome to the search node 8",
        "The lazy brown fox jumped over the",
        "Winning is the best because it's winning."
    ) foreach (doc =>  searchNode ! SearchableDocument(doc))
  }
  def makeTree = {
    val supervisor = Supervisor(SupervisorConfig(AllForOneStrategy(List(classOf[Exception]), 3, 1000), Nil))
    val searchnodedispatcher = Dispatchers.newExecutorBasedEventDrivenDispatcher("adaptive search tree")
        .withNewThreadPoolWithLinkedBlockingQueueWithCapacity(100)
        .setCorePoolSize(10)
        .setMaxPoolSize(128)
        .setKeepAliveTimeInMillis(60000)
        .setRejectionPolicy(new CallerRunsPolicy)
        .build
    val searchTree = Actor.actorOf(new AdaptiveSearchNode {
      self.dispatcher = searchnodedispatcher
    })
    supervisor link searchTree
    searchTree.start()
    submitInitialDocuments(searchTree)
    searchTree
  }
}


trait BaseHeadNode { self: AdaptiveSearchNode =>
  var children = IndexedSeq[ActorRef]()
  var currentIdx = 0
  def parentNode: PartialFunction[Any, Unit] = {
    case SearchQuery(q, max, responder) =>
        // TODO - use gatherer scheduler
        val gatherer = Actor.actorOf(new GathererNode {
          val maxDocs = max
          val maxResponses = children.size
          val query = q
          val client = responder
        })
        gatherer.start
        for (node <- children) {
          node ! SearchQuery(q, max, gatherer)
        }
    case s @ SearchableDocument(_) => getNextChild ! s
  }

  // Round Robin
  private def getNextChild = {
    currentIdx = (1 + currentIdx) % children.size
    children(currentIdx)
  }

}

trait BaseChildNode { self: AdaptiveSearchNode =>
  final val maxNoOfDocuments = 10
  var documents: Vector[String] = Vector()
  var index: HashMap[String, Seq[(Double, String)]] = HashMap()

  def leafNode: PartialFunction[Any, Unit] = {
    case SearchQuery(query, maxDocs, handler) => executeLocalQuery(query, maxDocs, handler)
    case SearchableDocument(content)          => addDocumentToLocalIndex(content)
  }
  private def executeLocalQuery(query: String, maxDocs: Int, handler: ActorRef) = {
    val result = for {
      results <- index.get(query).toList
      resultList <- results
    } yield resultList
    handler ! QueryResponse(result take maxDocs)
  }

  private def addDocumentToLocalIndex(content: String) = {
    for( (key,value) <- content.split("\\s+").groupBy(identity)) {
      val list = index.get(key) getOrElse Seq()
      index += ((key, ((value.length.toDouble, content)) +: list))
    }
    documents = documents :+ content
    // Split on size....
    if (documents.size > maxNoOfDocuments) split()
  }

  /** Abstract method to split this actor. */
  protected def split(): Unit

  protected def clearIndex(): Unit = {
    documents = Vector()
    index = HashMap()
  }
}


class AdaptiveSearchNode extends Actor with BaseHeadNode with BaseChildNode {
  def receive = leafNode

  /** Splits this search node into a tree of search nodes if there are too many documents. */
  protected def split(): Unit = {
    children = (for(docs <- documents grouped 5) yield {
      // TODO - use search scheduler + hook up to supervisor...
      val child = Actor.actorOf[AdaptiveSearchNode]
      child.start()
      docs foreach (child ! SearchableDocument(_))
      child
    }).toIndexedSeq
    clearIndex()
    this become parentNode
  }
}
*/