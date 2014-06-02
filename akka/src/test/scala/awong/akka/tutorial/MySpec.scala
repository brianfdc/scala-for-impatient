package awong.akka.tutorial

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @see http://doc.akka.io/docs/akka/2.0.1/scala/testing.html
 */
object MySpec {
  class EchoActor extends Actor {
    def receive = {
      case x => sender ! x
    }
  }
}

@RunWith(classOf[JUnitRunner])
class MySpec(_system: ActorSystem) extends TestKit(_system)
  with ImplicitSender     with WordSpec
  with MustMatchers       with BeforeAndAfterAll
{
  def this() = this(ActorSystem("MySpec"))
 
  import MySpec._
 
  override def afterAll {
    system.shutdown()
  }
 
  "An Echo actor" must {
    "send back messages unchanged" in {
      val echo = system.actorOf(Props[EchoActor])
      echo ! "hello world"
      expectMsg("hello world")
    }
  }
}