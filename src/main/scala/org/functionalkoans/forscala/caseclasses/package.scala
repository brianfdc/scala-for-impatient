package org.functionalkoans.forscala

package caseclasses {
  case class Person(first: String, last: String)
  case class Dog(name: String, breed: String)
  case class MutableDog(var name: String, breed: String) // you can rename a dog, but change its breed? nah!
  // case class has to be defined outside of the test for this one
  case class Citizen(first: String, last: String, age: Int = 0, ssn: String = "")


  class WithoutClassParameters() {
    def addColors(red: Int, green: Int, blue: Int) = {
      (red, green, blue)
    }

    def addColorsWithDefaults(red: Int = 0, green: Int = 0, blue: Int = 0) = {
      (red, green, blue)
    }
  }

  class WithClassParameters(val defaultRed: Int, val defaultGreen: Int, val defaultBlue: Int) {
    def addColors(red: Int, green: Int, blue: Int) = {
      (red + defaultRed, green + defaultGreen, blue + defaultBlue)
    }

    def addColorsWithDefaults(red: Int = 0, green: Int = 0, blue: Int = 0) = {
      (red + defaultRed, green + defaultGreen, blue + defaultBlue)
    }
  }

  class WithClassParametersInClassDefinition(val defaultRed: Int = 0, val defaultGreen: Int = 255,
                                             val defaultBlue: Int = 100) {
    def addColors(red: Int, green: Int, blue: Int) = {
      (red + defaultRed, green + defaultGreen, blue + defaultBlue)
    }

    def addColorsWithDefaults(red: Int = 0, green: Int = 0, blue: Int = 0) = {
      (red + defaultRed, green + defaultGreen, blue + defaultBlue)
    }
  }
}