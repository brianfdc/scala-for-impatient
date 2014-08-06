package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutInteroperability extends KoanSpec("About interoperability with Java") {
  
  "Scala" can {
    
    val msg = """interoperate with a java class and its use of collections by importing
          |   scala.collection.JavaConversions and letting scala implicitly convert
          |   from a Scala collection type into a Java collection type. """
    
    "interop with a Java class and its use of collections " in {
      val moreDetails = """See AboutImplicits Koan Suite for more details
          |   and see src/test/java for the SomeJavaClass file. This koan
          |   converts a scala List of String to java List of raw type."""

      println(moreDetails)
      import scala.collection.JavaConversions._
      val d = new SomeJavaClass
      val e = List("one", "two", "three")
      d.findSizeOfRawType(e) should be(3)
    }
    
    "convert a Scala List[Boat] to a Java List<?>" in {
      class Boat(size: Int, manufacturer: String)
      import scala.collection.JavaConversions._
      val d = new SomeJavaClass
      val e = List(new Boat(33, "Skyway"), new Boat(35, "New Boat"))
      d.findSizeOfUnknownType(e) should be(2)
    }
  }


}
