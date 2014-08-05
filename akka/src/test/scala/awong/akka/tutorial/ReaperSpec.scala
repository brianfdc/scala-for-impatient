package awong.akka.tutorial

import scala.concurrent.Await
import scala.concurrent.duration._

import akka.util.Timeout

import akka.actor._
import akka.testkit.{TestKit, ImplicitSender, TestProbe}

import org.scalatest.{WordSpec, BeforeAndAfterAll}
import org.scalatest.matchers.MustMatchers
import org.scalatest.junit.JUnitRunner

import org.junit.runner.RunWith


@RunWith(classOf[JUnitRunner])
class ReaperSpec extends TestKit(ActorSystem("ReaperSpec"))
	with ImplicitSender with WordSpec
	with BeforeAndAfterAll with MustMatchers
{
	import Reaper._
 
	override def afterAll() {
		system.shutdown()
	}

	"Reaper" should {
		"work" in {
			// Set up some dummy Actors
			val a = TestProbe()
			val b = TestProbe()
			val c = TestProbe()
			val d = TestProbe()

			// Build our reaper
			val reaper = system.actorOf(Props(new TestReaper(testActor)))
			
			// Watch a couple 
			reaper ! WatchMe(a.ref)
			reaper ! WatchMe(d.ref)

			// Stop them
			system.stop(a.ref)
			system.stop(d.ref)

			// Make sure we've been called
			within (1 seconds, 5 seconds) {
				expectMsg("Dead")
			}
		}
	}
}

// Our test reaper.	Sends the snooper a message when all
// the souls have been reaped
class TestReaper(snooper: ActorRef) extends Reaper {
	def allSoulsReaped(): Unit = snooper ! "Dead"
}



