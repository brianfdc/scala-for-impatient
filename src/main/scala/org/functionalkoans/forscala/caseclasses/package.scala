package org.functionalkoans.forscala

package caseclasses {
  case class Person(first: String, last: String)
  case class Dog(name: String, breed: String)
  case class MutableDog(var name: String, breed: String) // you can rename a dog, but change its breed? nah!
  // case class has to be defined outside of the test for this one
  case class Citizen(first: String, last: String, age: Int = 0, ssn: String = "")
}