package awong.akka.tutorial

import scala.concurrent.duration._

import akka.util.Timeout

import akka.actor._
import akka.testkit._

import awong.akka.TestkitSpec

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith


@RunWith(classOf[JUnitRunner])
class ReaperSpec(_system: ActorSystem) extends TestkitSpec(_system)
{
	import Reaper._
 
	def this() = this(ActorSystem("ReaperSpec"))
	
	override def afterAll() {
		TestKit.shutdownActorSystem(system)
	}

	// Our test reaper.	Sends the snooper a message when all
	// the souls have been reaped
	class TestReaper(snooper: ActorRef) extends Reaper {
		def allSoulsReaped(): Unit = snooper ! "Dead"
	}

	"Reaper" should {
		"work" in {
			// Set up some dummy Actors
			val a = TestProbe()
			val b = TestProbe()
			val c = TestProbe()
			val d = TestProbe()

			// Build our reaper
			val reaper = TestActorRef[TestReaper](Props(new TestReaper(testActor)), "testReaper")
			
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



