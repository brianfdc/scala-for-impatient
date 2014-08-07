package awong

import org.scalatest._

trait AbstractSpecLike
	extends Suite
	with BeforeAndAfterAll
	with BeforeAndAfter
	with Matchers
	with ResourceLoader
	with LoggingLike

trait AbstractFunSuite
	extends FunSuite
	with AbstractSpecLike

trait AbstractFlatSpec
	extends FlatSpecLike
	with AbstractSpecLike

trait AbstractWordSpec
	extends WordSpecLike
	with AbstractSpecLike

