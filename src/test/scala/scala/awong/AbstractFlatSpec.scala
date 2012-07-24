package scala.awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers

trait AbstractFlatSpec
  extends FlatSpec
  with BeforeAndAfter
  with ShouldMatchers
  with MustMatchers
{

}