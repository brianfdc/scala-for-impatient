package akka.tutorial


import akka.actor.ActorSystem
import akka.actor.ActorRef
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.akka.tutorial._
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit.MILLISECONDS

/**
 * @see http://doc.akka.io/docs/akka/2.0.1/scala/testing.html
 */
@RunWith(classOf[JUnitRunner])
class PiSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender		with WordSpec
	with MustMatchers		with BeforeAndAfterAll
{
	def this() = this(ActorSystem("PiSystem"))

	override def afterAll {
		system.shutdown()
	}
	
	"An Pi actor system" must {
		"calculate Pi" in {
			val listener = Pi.createListener(system)
			val nrOfWorkers = 4
			val nrOfElements = 10000
			val nrOfMessages = 10000
			val master = Pi.createMaster(nrOfWorkers, nrOfElements, nrOfMessages, system, listener)
			master ! Calculate
			val duration = Duration(100, MILLISECONDS)
			within (duration) {
				expectMsg(PiApproximation(scala.math.Pi, duration))
			}
		}
	}
}
