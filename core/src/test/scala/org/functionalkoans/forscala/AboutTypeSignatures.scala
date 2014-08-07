package org.functionalkoans.forscala


import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutTypeSignatures extends KoanSpec("About type signatures") {
  "Types in Scala" can {
    "Be declared with '[]', whereas in Java it is with '<>'" in {
      val z: List[String] = "Do" :: "Re" :: "Mi" :: "Fa" :: "So" :: "La" :: "Te" :: "Do" :: Nil
      z.getClass.getSimpleName should be ("$colon$colon")
    }
    "Are usually inferred by Scala, so that [] is optional" in {
      val z = "Do" :: "Re" :: "Mi" :: "Fa" :: "So" :: "La" :: "Te" :: "Do" :: Nil
      //Infers that the list assigned to variable is of type List[String]
      z.getClass.getSimpleName should be ("$colon$colon")
    }
  }
  "A trait" can {
    "be declared containing a type, where a concrete implmenter will satisfy the type" in {
      val intRand = new IntRandomizer
      (intRand.draw < Int.MaxValue) should be (true)
    }
  }
  "Class meta-data" can {
    "be retrieved by class name by using classOf[className]" in {
      classOf[String].getCanonicalName() should be("java.lang.String")
      classOf[String].getSimpleName() should be("String")
    }
    "be derived from an object reference using getClass()" in {
      val zoom = "zoom"
      zoom.getClass should be(classOf[String]) // Hint: classOf ...
      zoom.getClass.getCanonicalName() should be("java.lang.String")
      zoom.getClass.getSimpleName() should be("String")
    }
  }
  "isInstanceOf[className]" can {
    "be used to determine the if an object reference is an instance of given class" in {
      val intRand = new IntRandomizer
      intRand.draw.isInstanceOf[Int] should be(true)
      intRand.isInstanceOf[Randomizer[Int]] should be(true)
    }
  }
  "asInstanceOf[className]" should {
    "be used to cast one reference to another" in {
      val intRand = new IntRandomizer
      val rand = intRand
      val intRand2 = rand.asInstanceOf[IntRandomizer]
      intRand2.isInstanceOf[IntRandomizer] should be(true)
    }
    "will throw will throw a ClassCastException if a class derived from and the class target aren't from the same inheritance branch" in {
      val intRand = new IntRandomizer
      intercept[ClassCastException] {
        intRand.asInstanceOf[String] //intRand cannot be cast to String
      }
    }
  }

  "null.asInstanceOf[className]" can {
    "be used to generate basic default values" in {
      val x0 = null.asInstanceOf[String]
      x0 shouldBe null
      val x1 = null.asInstanceOf[Int]
      x1 shouldBe 0
    }
  }
}
