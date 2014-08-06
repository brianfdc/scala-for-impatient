package org.functionalkoans.forscala.support


import org.scalatest.WordSpec
import org.scalatest.Suite
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.BeforeAndAfter
import awong.AbstractWordSpec

abstract class KoanSpec(val description:String)
  extends AbstractWordSpec
  with BlankTests
  with Suite
{
  override def beforeAll = {
    println(description)
  }
}