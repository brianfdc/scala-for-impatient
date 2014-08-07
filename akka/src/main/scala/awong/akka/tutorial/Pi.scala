package awong.akka.tutorial

/**
 * Copyright (C) 2009-2012 Typesafe Inc. <http://www.typesafe.com>
 * 
 * @see http://doc.akka.io/docs/akka/2.0.2/intro/getting-started-first-scala.html
 */
import akka.actor._

import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

object Pi extends App
{
	run
	
	def run = {
		calculate(4, 10000, 10000)
	}
	
	def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) = {
		// TODO factor this to Cake pattern
		// Create an Akka system
		val system = ActorSystem("PiSystem")
		val listener =  system.actorOf(Listener.props, name = "listener")
		val master =  system.actorOf(Master.props(nrOfWorkers, nrOfMessages, nrOfElements, listener), name = "master")

		// start the calculation
		master ! Calculate
	}
	
}

sealed trait PiMessage
case object Calculate extends PiMessage
case class Shutdown() extends PiMessage
case class Work(start: Int, nrOfElements: Int) extends PiMessage
case class PiResult(value: Double) extends PiMessage
case class PiApproximation(pi: Double, duration: Duration)

trait BasePiActor extends Actor with ActorLogging {
	override def preStart(): Unit = ()
	
	override def postStop(): Unit = ()
	
	override def preRestart(reason: Throwable, mssage: Option[Any]): Unit = {
		context.children foreach { child =>
			context.unwatch(child)
			context.stop(child)
		}
	}
	
	override def postRestart(reason: Throwable): Unit = {
		preStart()
	}
}

class Worker extends BasePiActor {
	def receive = {
		case Work(start, nrOfElements) =>
			log.debug("worker receives {}, {}", start, nrOfElements)
			sender ! PiResult(calculatePiFor(start, nrOfElements)) // perform the work
	}
	
	def calculatePiFor(start: Int, nrOfElements: Int): Double = {
		var acc = 0.0
		( start until (start + nrOfElements) ).foreach{ (i) =>
			acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
		}
		acc
	}
}

object Master {
	def props(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef): Props = {
		Props(new Master(nrOfWorkers, nrOfMessages, nrOfElements, listener))
	}
}

class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends BasePiActor {
	var pi: Double = _
	var nrOfResults: Int = _
	val start: Long = System.currentTimeMillis

	val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

	def receive = {
		case Calculate =>
			val messages = 0 until nrOfMessages
			log.debug("master receives Calculate command and sending {} Work messages", nrOfMessages)
			messages.foreach{ i =>
				workerRouter ! Work(i * nrOfElements, nrOfElements)
			}
		case PiResult(value) =>
			pi += value
			nrOfResults += 1
			log.debug("master has received a Result: pi = {}, nrOfResults = {}", pi, nrOfResults)
			if (nrOfResults == nrOfMessages) {
				// Send the result to the listener
				log.debug("master has accumulated enough results and sending PiApprox of {}", pi)
				listener ! PiApproximation(pi, Duration(System.currentTimeMillis - start,TimeUnit.MILLISECONDS) )
			}
		case msg: Shutdown =>
			log.debug("shutting down master and all its superverised children")
			context.stop(self)
			log.debug("shutting down systems")
			context.system.shutdown
	}
}

object Listener {
	def props: Props = Props(new Listener)
}

class Listener extends BasePiActor {
	def receive = {
		case msg: PiApproximation =>
			log.debug("\nPi approximation:\t\t%s".format(msg.pi))
			log.debug("Calculation time:\t\t%s".format(msg.duration))
			sender ! Shutdown
			//context.system.shutdown()
	}
}