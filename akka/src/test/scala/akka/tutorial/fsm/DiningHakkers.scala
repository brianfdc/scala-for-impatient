package akka.tutorial.fsm

import akka.actor._

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit 

import akka.event.Logging

/**
 * Copyright (C) 2009-2010 Typesafe Inc. <http://www.typesafe.com>.
 * 
 * Akka adaptation of
 * http://www.dalnefre.com/wp/2010/08/dining-philosophers-in-humus/
 * 
 * @see https://github.com/akka/akka/blob/v2.0.3/akka-samples/akka-sample-fsm/src/main/scala/DiningHakkersOnBecome.scala
 * @see http://doc.akka.io/docs/akka/2.0.3/scala/fsm.html
 */

/*
* Alright, here's our test-harness
*/
object DiningHakkers {
  val system = ActorSystem()

  def main(args: Array[String]): Unit = run

  def run {
    //Create 5 chopsticks
    val chopsticks = for (i <- 1 to 5) yield system.actorOf(Props(new Chopstick("Chopstick#" + i)))

    //Create 5 awesome hakkers and assign them their left and right chopstick
    val hakkers = for {
      (name, i) <- List("Ghosh", "Boner", "Klang", "Krasser", "Manie").zipWithIndex
    } yield system.actorOf(Props(new Hakker(name, chopsticks(i), chopsticks((i + 1) % 5))))

    //Signal all hakkers that they should start thinking, and watch the show
    hakkers.foreach(_ ! Think)
  }
}
/*
* First we define our messages, they basically speak for themselves
*/
sealed trait DiningHakkerMessage
case class Busy(chopstick: ActorRef) extends DiningHakkerMessage
case class Put(hakker: ActorRef) extends DiningHakkerMessage
case class Take(hakker: ActorRef) extends DiningHakkerMessage
case class Taken(chopstick: ActorRef) extends DiningHakkerMessage
object Eat extends DiningHakkerMessage
object Think extends DiningHakkerMessage

/*
* A Chopstick is an actor, it can be taken, and put back
*/
class Chopstick(name: String) extends Actor with ActorLogging {
  import context._
  def logDebug(msg: String) = {
    //log.debug(msg)
    println(msg)
  }
  //When a Chopstick is taken by a hakker
  //It will refuse to be taken by other hakkers
  //But the owning hakker can put it back
  
  def takenBy(hakker: ActorRef): Receive = {
    case Take(otherHakker) =>
      logDebug("%s: Refusing to give myself to %s".format(name,otherHakker.path.name))
      otherHakker ! Busy(self)
    case Put(`hakker`) =>
      logDebug("%s: Now available".format(name))
      become(available)
  }

  //When a Chopstick is available, it can be taken by a hakker
  def available: Receive = {
    case Take(hakker) =>
      become(takenBy(hakker))
      logDebug("%s: Taken by %s".format(name, hakker.path.name))
      hakker ! Taken(self)
  }

  //A Chopstick begins its existence as available
  def receive = available
}

/*
* A hakker is an awesome dude or dudette who either thinks about hacking or has to eat ;-)
*/
class Hakker(name: String, left: ActorRef, right: ActorRef) extends Actor with ActorLogging {

  import context._

  def logDebug(msg: String) = {
    //log.debug(msg)
    println(msg)
  }
  //When a hakker is thinking it can become hungry
  //and try to pick up its chopsticks and eat
  def thinking: Receive = {
    case Eat =>
      become(hungry)
      logDebug("%s (%s) has become hungry and is trying to pick up chopsticks from %s and %s".format(name, self.path.name, left.path.name, right.path.name))
      left  ! Take(self)
      right ! Take(self)
  }

  //When a hakker is hungry it tries to pick up its chopsticks and eat
  //When it picks one up, it goes into wait for the other
  //If the hakkers first attempt at grabbing a chopstick fails,
  //it starts to wait for the response of the other grab
  def hungry: Receive = {
    case Taken(`left`) =>
      become(waiting_for(right, left))
    case Taken(`right`) =>
      become(waiting_for(left, right))
    case Busy(chopstick) =>
      become(denied_a_chopstick)
  }

  //When a hakker is waiting for the last chopstick it can either obtain it
  //and start eating, or the other chopstick was busy, and the hakker goes
  //back to think about how he should obtain his chopsticks :-)
  def waiting_for(chopstickToWaitFor: ActorRef, otherChopstick: ActorRef): Receive = {
    case Taken(`chopstickToWaitFor`) =>
      become(eating)
      logDebug("%s (%s) has picked up %s and %s and starts to eat".format(name, self.path.name, left.path.name, right.path.name))
      val duration = Duration(5, TimeUnit.SECONDS)
      system.scheduler.scheduleOnce(duration, self, Think)

    case Busy(chopstick) =>
      become(thinking)
      otherChopstick ! Put(self)
      logDebug("%s (%s) is now thinking and put down chopstick by %s".format(name, self.path.name, otherChopstick.path.name))
      self ! Eat
  }

  //When the results of the other grab comes back,
  //he needs to put it back if he got the other one.
  //Then go back and think and try to grab the chopsticks again
  def denied_a_chopstick: Receive = {
    case Taken(chopstick) =>
      become(thinking)
      chopstick ! Put(self)
      logDebug("%s (%s) starts to think and putting down %s which is taken".format(name, self.path.name, chopstick.path.name))
      self ! Eat
    case Busy(chopstick) =>
      become(thinking)
      logDebug("%s (%s) starts to think and putting down %s which is busy".format(name, self.path.name, chopstick.path.name))
      self ! Eat
  }

  //When a hakker is eating, he can decide to start to think,
  //then he puts down his chopsticks and starts to think
  def eating: Receive = {
    case Think =>
      become(thinking)
      left ! Put(self)
      right ! Put(self)
      logDebug("%s (%s) puts down his chopsticks and starts to think".format(name, self.path.name))
      val duration = Duration(5, TimeUnit.SECONDS)
      system.scheduler.scheduleOnce(duration, self, Eat)
  }

  //All hakkers start in a non-eating state
  def receive = {
    case Think =>
      become(thinking)
      logDebug("%s (%s) starts to think".format(name, self.path.name))
      val duration = Duration(5, TimeUnit.SECONDS)
      system.scheduler.scheduleOnce(duration, self, Eat)
  }
}

