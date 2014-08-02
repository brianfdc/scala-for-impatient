package awong.akka.tutorial

import scala.util._
import scala.concurrent._
import scala.concurrent.util._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration._
import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util._
import akka.pattern.{ask, pipe}

/**
 * Example of risk delegation
 */
class FileWriter extends Actor {
	var workerToCustomer = Map.empty[ActorRef, ActorRef]
	
	override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
	
	def receive: Receive = {
		case Write(contents, file) =>
			val worker = context.actorOf(Props(new FileWorker(contents, file, self)))
			context.watch(worker)
			workerToCustomer += worker -> sender
		case Done =>
			workerToCustomer.get(sender).foreach { _ ! Done }
			workerToCustomer -= sender
		case Terminated(worker) =>
			workerToCustomer.get(sender).foreach { _ ! Failed }
			workerToCustomer -= sender
	}
}



class FileWorker(contents: String, file: java.io.File, parent: ActorRef) extends Actor {
	def receive: Receive = {
		case _ =>
			println("oops")
	}
}

trait FileWriterMessage
case class Write(contents: String, file: java.io.File) extends FileWriterMessage
case class Done() extends FileWriterMessage
case class Failed() extends FileWriterMessage
case class Terminated(worker: ActorRef) extends FileWriterMessage
