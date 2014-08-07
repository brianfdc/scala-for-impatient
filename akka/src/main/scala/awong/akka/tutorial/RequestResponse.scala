package awong.akka.tutorial


import scala.util.{Try, Success, Failure}
import scala.concurrent._
import scala.concurrent.util._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration._
import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util._
import akka.pattern.{ask, pipe}


object RequestResponse extends App with awong.LoggingLike {

	run
	
	def run: Unit = {
		// create the system and actor
		val system = ActorSystem("Request-Response")
		val myActor = system.actorOf(TestActor.props("Fred"), name = "myActor")
	 
		implicit val timeout = Timeout(5 seconds)
		implicit val ec = ExecutionContext.Implicits.global
		ask_1(myActor)
		ask_2(myActor)
		ask_3(myActor)
		
		system.shutdown
	}
	
	// (1) this is one way to "ask" another actor
	private def ask_1(myActor: ActorRef)(implicit timeout: Timeout): Unit = {
		val future = myActor ? AskNameMessage
		val result = Await.result(future, timeout.duration).asInstanceOf[String]
		logger.debug(result)
	}
	
	// (2) this is a slightly different way to ask another actor
	private def ask_2(myActor: ActorRef)(implicit timeout: Timeout): Unit = {
		val future: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
		val result = Await.result(future, 1 second)
		logger.debug(result)
	}
	
	// (3) this is a slightly different way to ask another actor
	private def ask_3(myActor: ActorRef)(implicit timeout: Timeout, ec: ExecutionContext): Unit = {
		val future: Future[String] = (myActor ? AskNameMessage).mapTo[String]
		future.onComplete {
			case Success(string) => logger.debug(string)
			case Failure(th) => logger.debug("Exception was thrown")
		}
	}
}


case object AskNameMessage

object TestActor {
	def props(name: String): Props = Props(new TestActor(name))
}

class TestActor(val name: String) extends Actor with ActorLogging {
	implicit val timeout = Timeout(5 seconds)
	implicit val ec = ExecutionContext.Implicits.global

	def receive: Receive = {
		case AskNameMessage =>
			log.debug(s"${name} respond to the 'AskNameMessage' request")
			sender ! "Fred"
		case _ => log.debug("that was unexpected")
	}
}

