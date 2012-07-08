package akka.tutorial

/**
 * Copyright (C) 2009-2012 Typesafe Inc. <http://www.typesafe.com>
 * 
 * @see http://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-scala.html
 */
import akka.actor._
import akka.actor.ActorSystem
import akka.routing.RoundRobinRouter
import akka.util.Duration
import java.util.concurrent.TimeUnit

object Pi {
  def run = {
    val nrOfWorkers = 4
    val nrOfElements = 10000
    val nrOfMessages = 10000
    calculate(nrOfWorkers, nrOfElements, nrOfMessages)
  }
  
  def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) = {
    // Create an Akka system
    val system = ActorSystem("PiSystem")
    val listener = createListener(system)
    val master = createMaster(nrOfWorkers, nrOfElements, nrOfMessages, system, listener)
    // start the calculation
    master ! Calculate
  }
  
  // create the result listener, which will print the result and shutdown the system
  def createListener(system: ActorSystem): ActorRef = {
    system.actorOf(Props[Listener], name = "listener")
  }
  
  // create the master
  def createMaster(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int, system: ActorSystem, listener: ActorRef): ActorRef = {
    system.actorOf(
        Props(new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener)),
        name = "master")
  }
  
  sealed trait PiMessage
  case object Calculate extends PiMessage
  case class Work(start: Int, nrOfElements: Int) extends PiMessage
  case class Result(value: Double) extends PiMessage
  case class PiApproximation(pi: Double, duration: Duration)
  
  class Worker extends Actor {
    def receive = {
      case Work(start, nrOfElements) =>
        sender ! Result(calculatePiFor(start, nrOfElements)) // perform the work
    }
    
    def calculatePiFor(start: Int, nrOfElements: Int): Double = {
      var acc = 0.0
      ( start until (start + nrOfElements) ).foreach{ (i) =>
        acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      }
      acc
    }
  }
  
  class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {
    var pi: Double = _
    var nrOfResults: Int = _
    val start: Long = System.currentTimeMillis
  
    val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")
  
    def receive = {
      case Calculate =>
        val messages = 0 until nrOfMessages
        messages.foreach{ i =>
          workerRouter ! Work(i * nrOfElements, nrOfElements)
        }
      case Result(value) =>
        pi += value
        nrOfResults += 1
        if (nrOfResults == nrOfMessages) {
          // Send the result to the listener
          listener ! PiApproximation(pi, Duration(System.currentTimeMillis - start,TimeUnit.MILLISECONDS) )
          // Stops this actor and all its supervised children
          context.stop(self)
        }
    }
  }

  class Listener extends Actor {
    def receive = {
      case msg: PiApproximation =>
        println("\n\tPi approximation: \t\t%s\n\tCalculation time: \t%s".format(msg.pi, msg.duration))
        sender ! msg
        //context.system.shutdown()
    }
  }
  
}