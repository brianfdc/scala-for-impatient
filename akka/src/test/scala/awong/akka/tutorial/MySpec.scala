package awong.akka.tutorial

import awong.akka.TestkitSpec
import akka.actor._

import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll

import org.scalatest.junit.JUnitRunner

import org.junit.runner.RunWith

/**
 * @see http://doc.akka.io/docs/akka/2.0.1/scala/testing.html
 */
@RunWith(classOf[JUnitRunner])
class MySpec(_system: ActorSystem) extends TestkitSpec(_system)
{
	private class EchoActor extends Actor {
		def receive = {
			case x => sender ! x
		}
	}

	"An Echo actor" must {
		"send back messages unchanged" in {
			val echo = system.actorOf(Props[EchoActor])
			echo ! "hello world"
			expectMsg("hello world")
		}
	}
}


