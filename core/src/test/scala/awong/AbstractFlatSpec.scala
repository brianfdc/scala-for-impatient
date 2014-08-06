package awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.Matchers

trait AbstractFlatSpec
  extends FlatSpec
  with BeforeAndAfter
  with Matchers
  with ResourceLoader
