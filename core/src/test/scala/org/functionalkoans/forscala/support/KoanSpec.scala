package org.functionalkoans.forscala.support


import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfter
import awong.AbstractWordSpec

abstract class KoanSpec(val description:String)
  extends AbstractWordSpec
  with BlankTests
{
  override def beforeAll = {
    println(description)
  }
}