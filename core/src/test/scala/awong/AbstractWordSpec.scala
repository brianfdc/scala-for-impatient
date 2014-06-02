package awong

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfter

abstract class AbstractWordSpec extends WordSpec
  with MustMatchers
  with ShouldMatchers
  with BeforeAndAfterAll
  with BeforeAndAfter
  with ResourceLoader
