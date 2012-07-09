package org.functionalkoans.forscala.support

import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfter

abstract class KoanSpec(val description:String) extends WordSpec
  with MustMatchers
  with ShouldMatchers
  with BeforeAndAfterAll
  with BeforeAndAfter
  with BlankTests
{
  override def beforeAll = {
    println(description)
  }
}