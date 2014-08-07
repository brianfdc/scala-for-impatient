package awong.akka

import akka.actor._

import akka.testkit.TestKit
import akka.testkit.ImplicitSender

import awong.AbstractWordSpec

import org.scalatest._

abstract class TestkitSpec(_system: ActorSystem)
	extends TestKit(_system)
	with ImplicitSender
	with AbstractWordSpec
{

	override def afterAll {
		TestKit.shutdownActorSystem(_system)
	}
}

abstract class AkkaFunSuite(_system: ActorSystem)
	extends TestKit(_system)
	with FunSuiteLike
	with Matchers
	with BeforeAndAfterAll
	with ImplicitSender 

