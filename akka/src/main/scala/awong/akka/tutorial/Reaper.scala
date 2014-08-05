package awong.akka.tutorial

import scala.collection.mutable.ArrayBuffer

import akka.actor._
 
object Reaper {
	// Used by others to register an Actor for watching
	case class WatchMe(ref: ActorRef)
}

abstract class Reaper extends Actor with ActorLogging {
	import Reaper._

	// Keep track of what we're watching
	val watched = ArrayBuffer.empty[ActorRef]

	// Derivations need to implement this method.  It's the
	// hook that's called when everything's dead
	def allSoulsReaped(): Unit
 
	// Watch and check for termination
	final def receive: Receive = {
		case WatchMe(ref) =>
			log.info("WatchMe({}) sent to Reaper", ref.path)
			context.watch(ref)
			watched += ref
		case Terminated(ref) =>
			log.info("Terminated({}) sent to Reaper", ref.path)
			watched -= ref
			if (watched.isEmpty) {
				log.info("invoking allSoulsReaped()")
				allSoulsReaped()
			} 
	}
}

class ProductionReaper extends Reaper {
	// Shutdown
	def allSoulsReaped(): Unit = context.system.shutdown()
}