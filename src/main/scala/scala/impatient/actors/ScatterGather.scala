package scala.impatient.actors

import scala.actors._
import scala.actors.Actor._
import scala.actors.scheduler._
import scala.collection.immutable._
/**
 * 
 * Scatter-gather example from Josh Suereth, _Scala in Depth_, chp 9 (Manning: 2012)
 */
object SearchTreeMain {
  def create(aName: String) = {
    val supervisor = new SearchNodeSupervisor{
      val name = aName
    }
  }

  def makeResponder: Actor = {
    val tmp = actor {
      while(true) {
        react {
          case x => println(x)
        }
      }
    }
    tmp.start()
    tmp
  }
}


/** Defines a SearchNode that contains a fragment of the search index */
trait SearchNode extends Actor {
  val id: Int
  
  lazy val index = initIndex()
  
  private def initIndex() = {
    def makeIndex(docs : String*): HashMap[String,Seq[ScoredDocument]] = {
      var tmp = HashMap[String, Seq[ScoredDocument]]()
      docs.foreach{ (doc) =>
        val tokens = doc.split("\\s+").groupBy(identity)
        for ( (key,value) <- tokens ) {
          val list = tmp.get(key) getOrElse Seq()
          tmp += ((key, ScoredDocument(value.length.toDouble, doc) +: list))
        }
      }
      tmp
    }
    id match {
      case 1 => makeIndex("Some example data for you")
      case 2 => makeIndex("Some more example data for you to use")
      case 3 => makeIndex("To be or not to be, that is the question")
      case 4 => makeIndex("OMG it's a cat")
      case 5 => makeIndex("This is an example.  It's a great one")
      case 6 => makeIndex("HAI there", "HAI IZ HUNGRY")
      case 7 => makeIndex("Hello, World")
      case 8 => makeIndex("Hello, and welcome to the search node 8")
      case 9 => makeIndex("The lazy brown fox jumped over the")
      case 10 => makeIndex("Winning is the best because it's winning.")
    }
  }
  
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

/**
 * An actor which receives distributed results and aggregates/responds to the original query.
 */
trait GathererNode extends Actor {
  // max # of docs to return from a query
  val maxDocs: Int
  // max # of nodes that can response b4 sending results for a query
  val maxResponses: Int
  // target to send results
  val client: OutputChannel[QueryResponse]
  
  override def act() = {
    bundleResult(0, Seq())
  }
  /**
   * curCount is # of responses seen thus far
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
    (current ++ next).view.sortBy(_.score).take(maxDocs).force
  }
}
/**
 * Responsible for creating the HeadNode & SearchNodes,
 * linking them appropriately.
 * 
 * Also handles failures in the search zone.
 */
trait SearchNodeSupervisor extends Actor {
  val name: String
  
  val numThreadForSearchTree = 5
  
  private def createSearchTree(size: Int) = {
    val numProcessors = java.lang.Runtime.getRuntime.availableProcessors
    val s = new ForkJoinScheduler(
              initCoreSize = numProcessors,
              maxSize = numThreadForSearchTree,
              daemon = false,
              fair = true)
    val searchNodes = (1 to size).map { i =>
      new SearchNode {
        override val id = i
        override val scheduler = s
      }
    }
    searchNodes.foreach{ SearchNodeSupervisor.this link _ }
    searchNodes.foreach{ _.start() }
    val headNode = new HeadNode {
      override val nodes = searchNodes
      override val scheduler = s
    }
    this link headNode
    headNode.start()
    headNode
  }
  def act() = {
    trapExit = true
    def run(head: Actor): Nothing = react {
      case Exit(deadActor, reason) =>
        // reboot search tree in case an actor dies
        run(createSearchTree(10))
      case x =>
        head ! x
        run(head)
    }
    run(createSearchTree(10))
  }
}

/**
 * The head node for the search tree.  Note:  The tree can be multiple levels deep, with head nodes
 * pointing to other head nodes.
 */
trait HeadNode extends Actor {
  // all nodes that head can scatter to.
  val nodes = Seq[OutputChannel[SearchMessage]]()
  override def act() = {
    react {
      case SearchQuery(q, max, responder) => {
        // send message to each search node to search & await a future
        // Gatherer is created on demand
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


