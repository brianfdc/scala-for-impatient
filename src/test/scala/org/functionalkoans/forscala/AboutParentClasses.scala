package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutParentClasses
  extends KoanSpec("Meditate on AboutParentClasses to understand inheritance")
{
  "Class hierarchy" must {
    "Be linear, a class can only extend from one parent class" in {
      val worker = new Worker("Alan", "Wong")
      val employee = new Employee("Alan", "Wong", 123)
      (worker.firstName) should be (employee.firstName)
    }
  }
  
  "A class that extends from another" must {
    "be polymorphic" in {
      val me = new ImmutableEmployee("Name", "Yourself", 1233)
      val worker: ImmutableWorker = me
      worker.firstName should be("Name")
      worker.lastName should be("Yourself")
    }
    
  }
  
  "An abstract class (as in Java)" must {
    abstract class AbstractWorker(val firstName: String, val lastName: String) {
      class Assignment(val hours: Long) {
        // nothing to do here.  Just observe that it compiles
      }
    }
    class CityEmployee(override val firstName: String,
                        override val lastName: String,
                        val employeeID: Long) extends AbstractWorker(firstName, lastName)
    "Never be instantiated and only inherited" in {
      // if you uncomment this line, if will fail compilation
      //val worker = new AbstractWorker
      
    }
    "Be able to have inner classes just like in Java" in {
      val employee = new CityEmployee("Mike","Bloomberg", 123)
      val hisAssignment = new employee.Assignment(10)
    }
  }
}
