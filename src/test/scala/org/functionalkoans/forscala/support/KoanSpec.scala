package org.functionalkoans.forscala.support

import scala.io.Source
import scala.io.BufferedSource

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
  
  /**
   * Load resource as a stream which is located in the
   * same package as the concrete koan spec
   * 
   * TODO : not quite the same as the full strength Spring
   * version that searches the entire class path
   */
  def getSource(resourceName: String):Option[BufferedSource] = {
    try {
      var stream = getClass().getResourceAsStream(resourceName)
      val src = Source.fromInputStream(stream)
      if (!src.isEmpty) {
        Some(src)
      } else {
        None
      }
    } catch  {
      case ex:Exception => None
    }
  }
}