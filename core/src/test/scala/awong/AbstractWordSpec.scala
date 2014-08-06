package awong

import org.scalatest.WordSpec
import org.scalatest.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfter

abstract class AbstractWordSpec extends WordSpec
  with Matchers
  with BeforeAndAfterAll
  with BeforeAndAfter
  with ResourceLoader
