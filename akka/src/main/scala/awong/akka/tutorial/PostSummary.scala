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
 * Example of result aggregator
 */
class PostSummary(publisher: ActorRef, postStore: ActorRef, authService: ActorRef) extends Actor {
	implicit val timeout = Timeout(5 seconds)
	implicit val ec = ExecutionContext.Implicits.global
	
	def askSerially: Receive = {
		case Get(postId, user, password) => {
			val response = for {
				status <- (publisher ? GetStatus(postId)).mapTo[PostStatus]
				text <- (postStore ? GetPost(postId)).mapTo[Post]
				auth <- (authService ? Login(user, password)).mapTo[AuthStatus]
			} yield {
				if (auth.successful) PostResult(status, text)
				else PostFailure("unauthorized")
			}
			response pipeTo sender
		}
	}
	
	def askInParallel: Receive = {
		case Get(postId, user, password) => {
			val status = (publisher ? GetStatus(postId)).mapTo[PostStatus]
			val text   = (postStore ? GetPost(postId)).mapTo[Post]
			val auth   = (authService ? Login(user, password)).mapTo[AuthStatus]
			// then compose the future when the last one is complete
			val response = for (s <- status; t <- text; a <- auth) yield {
				if (a.successful) PostResult(s, t)
				else PostFailure("unauthorized")
			}
			response pipeTo sender
		}
	}
	
	def receive: Receive = askInParallel
}


trait PostSummaryMessage
case class Get(postId: String, user: String, password: String) extends PostSummaryMessage
case class GetStatus(postId: String) extends PostSummaryMessage
case class PostStatus(status: String) extends PostSummaryMessage
case class GetPost(postId: String) extends PostSummaryMessage
case class Post(text: String) extends PostSummaryMessage
case class Login(user: String, password: String) extends PostSummaryMessage
case class AuthStatus(successful: Boolean) extends PostSummaryMessage
case class PostResult(status: PostStatus, text: Post) extends PostSummaryMessage
case class PostFailure(msg: String) extends PostSummaryMessage

