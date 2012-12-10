package scala.scitter

import scala.actors._
/**
 * @see Ted Neward
 * @see http://www.ibm.com/developerworks/java/library/j-scala02049/index.html
 * @see http://www.ibm.com/developerworks/java/library/j-scala04109/index.html
 */
object ProducerConsumer extends App {
  produce
  
  case class Message(msg : String)
  
  class Consumer extends Actor {
    var done = false;
    def act() {
      while(!done) {
        receive {
          case msg: Message =>
            println("Received message! -> " + msg.msg)
            done = (msg.msg == "DONE")
        }
      }
    }
  }
  def produce : Unit = {
    val consumer = new Consumer
    consumer.start();
    
    consumer ! Message("Mares eat oats")
    consumer ! Message("Does eat oats")
    consumer ! Message("Little lambs eat ivy")
    consumer ! Message("Kids eat ivy too")
    consumer ! Message("DONE")
  }
}