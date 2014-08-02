package awong.akka.tutorial


import scala.util._
import scala.concurrent._
import scala.concurrent.util._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration._
import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util._
import akka.pattern.ask


object RequestResponse extends App {

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
		// why doesn't the below work?
		ask_4(myActor)
		
		system.shutdown
	}
	
	// (1) this is one way to "ask" another actor
	private def ask_1(myActor: ActorRef)(implicit timeout: Timeout): Unit = {
		val future = myActor ? AskNameMessage
		val result = Await.result(future, timeout.duration).asInstanceOf[String]
		println(result)
	}
	
	// (2) this is a slightly different way to ask another actor
	private def ask_2(myActor: ActorRef)(implicit timeout: Timeout): Unit = {
		val future: Future[String] = ask(myActor, AskNameMessage).mapTo[String]
		val result = Await.result(future, 1 second)
		println(result)
	}
	
	// (3) this is a slightly different way to ask another actor
	private def ask_3(myActor: ActorRef)(implicit timeout: Timeout, ec: ExecutionContext): Unit = {
		val future: Future[String] = (myActor ? AskNameMessage).mapTo[String]
		future.onComplete {
			case Success(string) => println(string)
			case Failure(th) => println("Exception was thrown")
		}
	}
	private def ask_4(myActor: ActorRef)(implicit timeout: Timeout, ec: ExecutionContext): Unit = {
		val future: Future[Int] = (myActor ? DelegateMessage(42)).mapTo[Int]
		future.onComplete {
			case Success(string) => println(string)
			case Failure(th) => println("Exception was thrown to RequestResponse")
		}
	}
}


case object AskNameMessage
case class DelegateMessage(n: Int)

object TestActor {
	def props(name: String): Props = Props(new TestActor(name))
}

class TestActor(val name: String) extends Actor {
	val delegate: ActorRef = createDelegate()
	implicit val timeout = Timeout(5 seconds)
	implicit val ec = ExecutionContext.Implicits.global

	def receive: Receive = {
		case AskNameMessage =>
			println(s"${name} respond to the 'AskNameMessage' request")
			sender ! "Fred"
		case DelegateMessage(m) =>
			println(s"${name} delegating the 'DelegateMessage' request")
			val client = sender
			
			(delegate ? DelegateMessage(m)).mapTo[Int].onComplete{
				case Success(int) =>
					client ! Success(int)
				case Failure(th) =>
					println(s"exception ${th.getClass.getSimpleName} thrown to TestActor" )
					Failure(th)
			}
		case _ => println("that was unexpected")
	}
	
	private def createDelegate(): ActorRef = {
		context.system.actorOf(Delegate.props("delegate"), name = "delegate")
	}
}

object Delegate {
	def props(name: String): Props = Props(new Delegate(name))
}

class Delegate(val name: String) extends Actor {
	def receive: Receive = {
		case DelegateMessage(n) =>
			println(s"${name} respond to the 'DelegateMessage' request")
			val client = sender
			sender ! n
		case _ => println("that was unexpected")
	}
}
