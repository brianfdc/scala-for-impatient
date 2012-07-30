package scala.impatient.actors

import scala.actors.Actor

/**
 * (20.1)
 */
object AverageCast extends App {
  run(1000500)
  
  def run(n: Int) = {
    val s = new Supervisor(10000, 1000)
    s.start()
    s ! n
  }

  sealed trait AverageMessage
  case class GetRandomNumber(size: Int) extends AverageMessage
  case class SumMessage(sum: Int, size: Int) extends AverageMessage
  case class CalculateAverage() extends AverageMessage
  case class AverageResult(sum: Int, size: Int, average:Double) extends AverageMessage
  case class Done() extends AverageMessage
  case class Shutdown() extends AverageMessage
  
  class Supervisor(val partitionSize: Int, val upperBound: Int) extends Actor {
    var done = false
    var actors = Seq[RandomNumberActor]()
    var reduceActor = new ReduceActor(SumMessage(0,0), this);
    def act() {
      while(!done) {
        receive {
          case n: Int => {
            val size = partitionSize
            val partitions = n / size
            val remainder  = n % size
            val numberOfActors = partitions + (if (remainder > 0) 1 else 0)
            
            reduceActor.start()
            
            actors = (0 to numberOfActors).map { (id) =>
              val actor = new RandomNumberActor(id, this)
              actor.start()
              actor
            }
            val msgs = (0 to numberOfActors).map { (a) =>
              if (a != numberOfActors) GetRandomNumber(size)
              else GetRandomNumber(remainder)
            }
            val li  = (actors zip msgs).foreach { (x) => x._1 ! x._2 }
          }
          case x: Done => {
            sender match {
              case x: RandomNumberActor =>
                if (actors.contains(x)) {
                  var bf = actors.toBuffer
                  bf -= x
                  x ! Shutdown()
                  actors = bf.toSeq
                }
                if (actors.isEmpty) {
                  reduceActor ! CalculateAverage()
                }
            }
          }
          case x: AverageResult => {
            println("Size:   " + x.size)
            println("Sum:    " + x.sum)
            println("Average:" + x.average)
            done = true
          }
        }
      }
    }
  }
  /**
   * Have many of these for the scatter phase
   */
  class RandomNumberActor(val id: Int, val supervisor: Supervisor) extends Actor {
    import scala.util.Random
    var done = false
    def act() {
      while(!done) {
        receive {
          case m: GetRandomNumber => {
            val randoms = (0 until m.size).map{ (a) => Random.nextInt(supervisor.upperBound) }
            val sum = randoms.sum
            val size = randoms.size
            println(id + ": sum = " + sum + " size: " + size)
            supervisor.reduceActor ! SumMessage(sum, size)
            supervisor ! Done()
          }
          case m: Shutdown =>
            done = true
            exit()
        }
      }
    }
  }
  
  /**
   * Only have one of these for the gather phase
   */
  class ReduceActor(var sumMessage: SumMessage, supervisor: Supervisor) extends Actor {
    var done = false;
    def act() {
      while(!done) {
        receive {
          case m: SumMessage =>
            val sum  = sumMessage.sum + m.sum
            val size = sumMessage.size + m.size
            sumMessage = SumMessage(sum,size)
          case d: CalculateAverage =>
            val avg : Double = sumMessage.sum / sumMessage.size
            supervisor ! AverageResult(sumMessage.sum, sumMessage.size, avg)
            done = true;
        }
      }
    }
  }
  
  
}


