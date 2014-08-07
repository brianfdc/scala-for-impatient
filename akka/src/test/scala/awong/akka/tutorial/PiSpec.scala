package awong.akka.tutorial

import java.util.concurrent.TimeUnit._

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.duration.Duration

import akka.util.Timeout

import akka.actor._
import akka.testkit.TestActorRef

import awong.akka.TestkitSpec

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @see http://doc.akka.io/docs/akka/2.0.1/scala/testing.html
 */
@RunWith(classOf[JUnitRunner])
class PiSpec(_system: ActorSystem) extends TestkitSpec(_system)
{
	import awong.akka.tutorial.Pi
	import awong.akka.tutorial._
	
	def this() = this(ActorSystem("PiSystem"))

	"An Pi actor system" must {
		val listener = TestActorRef[Listener](Listener.props, "listener")
		val nrOfWorkers = 4
		val nrOfElements = 10000
		val nrOfMessages = 10000
		val master = TestActorRef[Master](Master.props(nrOfWorkers, nrOfMessages, nrOfElements, listener), "master")
		"calculate Pi" in {
			master ! Calculate
			val duration = 100.millis
			within(duration) {
				expectMsg(PiApproximation(scala.math.Pi, duration))
			}
		}
	}
}
