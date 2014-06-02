package awong.impatient.actors

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ActorSystem

/**
 * (20.1)
 */
object AverageCast extends App {
	run(1000500)
	
	def run(n: Int) = {
		val system = ActorSystem("AverageCast")
		val supervisor = createSupervisor(system, 10000, 1000)
		supervisor ! n
	}
	
	sealed trait AverageMessage
	case class GetRandomNumber(size: Int) extends AverageMessage
	case class SumMessage(sum: Int, size: Int) extends AverageMessage
	case class CalculateAverage() extends AverageMessage
	case class AverageResult(sum: Int, size: Int, average:Double) extends AverageMessage
	case class Done() extends AverageMessage
	case class Shutdown() extends AverageMessage
	
	class Supervisor(val system: ActorSystem, val partitionSize: Int, val upperBound: Int) extends Actor {
		var done = false
		var randomNumberActors = Seq[ActorRef]()
		var reduceActor = createReducer(system, new SumMessage(0,0))
		
		def createReducer(system: ActorSystem, sumMessage: SumMessage): ActorRef = {
			system.actorOf(
					Props(new ReduceActor(sumMessage)),
					name = "reducer")
		}
		
		def createRandomNumberActors(id: Int, supervisor: Supervisor) :ActorRef = {
			system.actorOf(
					Props(new RandomNumberActor(id, supervisor)),
					name = "randomNumberActor-" + id)
		}
		
		def receive = {
			case n: Int => {
				val size = partitionSize
				val partitions = n / size
				val remainder = n % size
				val numberOfActors = partitions + (if (remainder > 0) 1 else 0)
				
				randomNumberActors = (0 to numberOfActors).map { (id) =>
					createRandomNumberActors(id, this)
				}
				val msgs = (0 to numberOfActors).map { (a) =>
					if (a != numberOfActors) GetRandomNumber(size)
					else GetRandomNumber(remainder)
				}
				val li	= (randomNumberActors zip msgs).foreach { (x) => x._1 ! x._2 }
			}
			case x: Done => {
				sender match {
					case x: ActorRef =>
						if (randomNumberActors.contains(x)) {
							var bf = randomNumberActors.toBuffer
							bf -= x
							x ! Shutdown()
							randomNumberActors = bf.toSeq
						}
						if (randomNumberActors.isEmpty) {
							reduceActor ! CalculateAverage()
						}
				}
			}
			case msg : SumMessage => {
				reduceActor ! msg
			}
			case msg: AverageResult => {
				println("Size:	 " + msg.size)
				println("Sum:		" + msg.sum)
				println("Average:" + msg.average)
				done = true
			}
		}
	}
	
	def createSupervisor(system: ActorSystem, partitionSize: Int, upperBound: Int): ActorRef = {
		system.actorOf(
				Props(new Supervisor(system, partitionSize, upperBound)),
				name = "supervisor")
	}
	
	/**
	 * Have many of these for the scatter phase
	 */
	class RandomNumberActor(val id: Int, val supervisor: Supervisor) extends Actor {
		import scala.util.Random
		var done = false
		
		def receive = {
			case m: GetRandomNumber => {
				val randoms = (0 until m.size).map{ (a) => Random.nextInt(supervisor.upperBound) }
				val sum = randoms.sum
				val size = randoms.size
				println(id + ": sum = " + sum + " size: " + size)
				sender ! SumMessage(sum, size)
				sender ! Done()
			}
			case m: Shutdown => {
				done = true
				sys.exit()
			}
		}
	}
	
	/**
	 * Only have one of these for the gather phase
	 */
	class ReduceActor(var sumMessage: SumMessage) extends Actor {
		var done = false;
		
		def receive = {
			case m: SumMessage => {
				val sum	= sumMessage.sum + m.sum
				val size = sumMessage.size + m.size
				sumMessage = SumMessage(sum,size)
			}
			case d: CalculateAverage => {
				val avg : Double = sumMessage.sum / sumMessage.size
				sender ! AverageResult(sumMessage.sum, sumMessage.size, avg)
				done = true;
			}
		}
	}
	
	
}


