package awong.scitter


import akka.actor._

import akka.routing.RoundRobinRouter
import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit

/**
 * @see Ted Neward
 * @see http://www.ibm.com/developerworks/java/library/j-scala02049/index.html
 * @see http://www.ibm.com/developerworks/java/library/j-scala04109/index.html
 */
object ProducerConsumer extends App {
  run
  
  def run : Unit = {
    val system = ActorSystem("ProducerConsumerSystem")
    val consumer = createConsumer(system)
    val producer = createProducer(system,consumer)
    
    producer ! StringMessage("Mares eat oats")
    producer ! StringMessage("Does eat oats")
    producer ! StringMessage("Little lambs eat ivy")
    producer ! StringMessage("Kids eat ivy too")
    producer ! StringMessage("DONE")
  }
  
  
  sealed trait Message
  case class StringMessage(msg : String) extends Message
  case class Shutdown() extends Message
  
  class Consumer extends Actor with ActorLogging {
    var done = false;
    def receive = {
      case msg: StringMessage =>
        log.debug("Received message! -> " + msg.msg)
        if (msg.msg == "DONE") {
          done = true
          sender ! Shutdown
        }
    }
  }
  
  def createConsumer(system: ActorSystem): ActorRef = {
    system.actorOf(Props[Consumer], name = "consumer")
  }
  
  
  class Producer(consumer: ActorRef) extends Actor with ActorLogging {
    def receive = {
      case msg : StringMessage => {
        consumer ! msg
      }
      case msg : Shutdown => {
        log.debug("shutting down master and all its superverised children")
        context.stop(self)
        log.debug("shutting down system")
        context.system.shutdown
      }
    }
  }
  
  def createProducer(system: ActorSystem, listener: ActorRef): ActorRef = {
    system.actorOf(
        Props(new Producer(listener)),
        name = "producer")
  }
}