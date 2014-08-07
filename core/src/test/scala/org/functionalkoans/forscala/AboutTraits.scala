package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutTraits extends KoanSpec("About traits") {
  "A class" can {
    "use the 'extends' keyword to mixin a trait if it is the only relationship the class inherits" in {
      val evt = Event("Moose Stampede", this)
      // poke into class definition of MyListener
      val myListener = new MyListener()
      myListener.listen(evt).isEmpty should be (false)
    }
    "only extend from one class or trait, any subsequent extension should use the keyword 'with'" in {
      val evt = Event("Woodchuck Stampede", this)
      // poke into class definition of ComplexListener
      val myListener = new ComplexListener()
      myListener.listen(evt).isEmpty should be(false)
    }
  }
  "Traits" should {
    "Be polymorphic. Any type can be referred to by another type if related by extension" in {
      val evt = Event("Moose Stampede", this)
      val myListener = new MyListener()
      myListener.isInstanceOf[MyListener] should be(true)
      myListener.isInstanceOf[EventListener] should be(true)
      myListener.isInstanceOf[Any] should be(true)
      myListener.isInstanceOf[AnyRef] should be(true)
    }
  }
  
  "Traits" can {
    "have concrete implementations that can be mixed into concrete classes with its own state" in {
      val welder = new traits.Welder
      welder.weld
      val baker = new traits.Baker
      baker.bake

      welder.logCache.size should be(1)
      baker.logCache.size should be(1)
    }
  }
  "Traits" should {
    "be instantiated before a classes' instantition" in {
      var sb = List[String]()
      trait T1 {
        sb = sb :+ ("In T1:x=%s".format(x))
        val x = 1
        sb = sb :+ ("In T1:x=%s".format(x))
      }

      class C0 extends T1 {
        sb = sb :+ ("In C0:y=%s".format(y))
        val y = 2
        sb = sb :+ ("In C0:y=%s".format(y))
      }
      sb = sb :+ ("Creating C0")
      new C0
      sb = sb :+ ("Created C0")

      sb.mkString(";") should be("""Creating C0;In T1:x=0;In T1:x=1;In C0:y=0;In C0:y=2;Created C0""")
    }
    "be instantiated before a classes' instantition from left to right" in {
      var sb = List[String]()
      trait T1 {
        sb = sb :+ ("In T1:x=%s".format(x))
        val x = 1
        sb = sb :+ ("In T1:x=%s".format(x))
      }
      trait T2 {
        sb = sb :+ ("In T2:z=%s".format(z))
        val z = 1
        sb = sb :+ ("In T2:z=%s".format(z))
      }
      class C1 extends T1 with T2 {
        sb = sb :+ ("In C1:y=%s".format(y))
        val y = 2
        sb = sb :+ ("In C1:y=%s".format(y))
      }
      sb = sb :+ ("Creating C1")
      new C1
      sb = sb :+ ("Created C1")
      sb.mkString(";") should be("""Creating C1;In T1:x=0;In T1:x=1;In T2:z=0;In T2:z=1;In C1:y=0;In C1:y=2;Created C1""")
      
    }
  }
  "Instantiations" should {
    "be tracked and will not allow a duplicate instantiation." in {
      var sb = List[String]()
      trait T1 extends T2 {
        sb = sb :+ ("In T1:x=%s".format(x))
        val x = 1
        sb = sb :+ ("In T1:x=%s".format(x))
      }
      trait T2 {
        sb = sb :+ ("In T2:z=%s".format(z))
        val z = 1
        sb = sb :+ ("In T2:z=%s".format(z))
      }
      class C2 extends T1 with T2 {
        sb = sb :+ ("In C2:y=%s".format(y))
        val y = 2
        sb = sb :+ ("In C2:y=%s".format(y))
      }
      sb = sb :+ ("Creating C2")
      new C2
      sb = sb :+ ("Created C2")
      sb.mkString(";") should be("""Creating C2;In T2:z=0;In T2:z=1;In T1:x=0;In T1:x=1;In C2:y=0;In C2:y=2;Created C2""")
      logger.debug("Note T1 extends T2, and C1 also extends T2, but T2 is only instantiated twice.")
    }
  }
  
  /**
   * @see http://en.wikipedia.org/wiki/Diamond_problem
   */
  "The diamond of death" should {
    "be avoided since instantiations are tracked and will not allow multiple instantiation" in {
      var sb = List[String]()
  
      trait T1 {
        sb = sb :+ ("In T1:x=%s".format(x))
        val x = 1
        sb = sb :+ ("In T1:x=%s".format(x))
      }
  
      trait T2 extends T1 {
        sb = sb :+ ("In T2:z=%s".format(z))
        val z = 2
        sb = sb :+ ("In T2:z=%s".format(z))
      }
  
      trait T3 extends T1 {
        sb = sb :+ ("In T3:w=%s".format(w))
        val w = 3
        sb = sb :+ ("In T3:w=%s".format(w))
      }
  
      class C4 extends T2 with T3 {
        sb = sb :+ ("In C4:y=%s".format(y))
        val y = 4
        sb = sb :+ ("In C4:y=%s".format(y))
      }
      sb = sb :+ ("Creating C4")
      new C4
      sb = sb :+ ("Created C4")
  
      val msg = """Creating C4;In T1:x=0;In T1:x=1;In T2:z=0;In T2:z=2;In T3:w=0;In T3:w=3;In C4:y=0;In C4:y=4;Created C4"""
      sb.mkString(";") should be(msg)
    }
  }
}
