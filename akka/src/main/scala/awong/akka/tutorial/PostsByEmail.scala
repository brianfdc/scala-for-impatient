package awong.akka.tutorial

import scala.util._
import scala.concurrent._
import scala.concurrent.util._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration._
import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util._
import akka.pattern.{ask, pipe, CircuitBreaker}

/**
 * Example of ask pattern where the sender actor asks the PostsByEmail actor,
 * who delegates the request to the user service, and then in the future, 
 * pipes the answer back to the sender.
 */
class PostsByEmail(userService: ActorRef) extends Actor {
	implicit val timeout = Timeout(5 seconds)
	implicit val ec = ExecutionContext.Implicits.global
	
	def receive: Receive = {
		case GetEmail(email) => {
			(userService ? FindByEmail(email)).mapTo[UserInfo]
			.map{ info => UserInfo(info.posts.filter(_.email == email)) }
			.recover{ case ex => GetPostFailure(ex) }
			.pipeTo(sender)
		}
	}
}

trait PostsByEmailMessage 
case class GetEmail(email: String) extends PostsByEmailMessage
case class FindByEmail(email: String) extends PostsByEmailMessage
case class UserInfo(posts: Seq[BlogPost]) extends PostsByEmailMessage
case class BlogPost(email: String) extends PostsByEmailMessage
case class GetPostFailure(th: Throwable) extends PostsByEmailMessage

/**
 * Example of asking with a CircuitBreaker
 */
class Retriver(userService: ActorRef) extends Actor {
	implicit val timeout = Timeout(2 seconds)

	val cb = CircuitBreaker(context.system.scheduler,
		maxFailures = 3,
		callTimeout = 1 second,
		resetTimeout = 30 second 
	)
	
	def receive: Receive = {
		case GetEmail(email) => {
			val result = cb.withCircuitBreaker(userService ? email).mapTo[String]
		}
	}
}

/**
 * Sketch of bulkheading where worker actor uses different thread pool from
 * akka's default threadpool
 * 
 * Props[Worker].withDispatcher("compute-jobs")
 * 
 * In akka config, define min/max number of threads for the "compute-jobs" thread pool
 * 
 * compute-jobs.fork-join-executor {
 *    parallelism-min=4
 *    parallelism-max=4
 * }
 */
