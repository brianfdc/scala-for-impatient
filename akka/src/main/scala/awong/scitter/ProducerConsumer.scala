package scala.scitter


import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ActorSystem
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
  
  class Consumer extends Actor {
    var done = false;
    def receive = {
      case msg: StringMessage =>
        println("Received message! -> " + msg.msg)
        if (msg.msg == "DONE") {
          done = true
          sender ! Shutdown
        }
    }
  }
  
  def createConsumer(system: ActorSystem): ActorRef = {
    system.actorOf(Props[Consumer], name = "consumer")
  }
  
  
  class Producer(consumer: ActorRef) extends Actor {
    def receive = {
      case msg : StringMessage => {
        consumer ! msg
      }
      case msg : Shutdown => {
        println("shutting down master and all its superverised children")
        context.stop(self)
        println("shutting down system")
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