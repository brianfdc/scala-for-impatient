package awong.akka

import akka.actor._

import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import org.scalatest.WordSpecLike
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll


abstract class TestkitSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender
	with WordSpecLike
	with Matchers
	with BeforeAndAfterAll
{

	override def afterAll {
		TestKit.shutdownActorSystem(system)
	}
}