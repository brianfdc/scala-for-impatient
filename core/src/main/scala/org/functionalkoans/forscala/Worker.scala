package org.functionalkoans.forscala

import scala.reflect.BeanProperty


class Worker(@BeanProperty var firstName: String, @BeanProperty var lastName: String)
class Employee(firstName: String,
                 lastName: String,
                 @BeanProperty var employeeID: Long)
  extends Worker(firstName, lastName)
  
class ImmutableWorker(val firstName: String, val lastName: String)
class ImmutableEmployee(override val firstName: String,
                          override val lastName: String,
                          val employeeID: Long)
  extends ImmutableWorker(firstName, lastName)

class WithParameterRequirement(val myValue: Int) {
    require(myValue != 0)

    def this(someValue: String) {
      this (1)
    }
}
