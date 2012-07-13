package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutEnumerations
  extends KoanSpec("""To create an enumeration, create an object that extends the
      |abstract class Enumeration, and set a val variable to the method Value.
      | This is a trick to give values to each val.""")
{
  "Enumerations" can {
    "be assigned a numerical value with Value" in {
      object Planets extends Enumeration {
        val Mercury = Value
        val Venus = Value
        val Earth = Value
        val Mars = Value
        val Jupiter = Value
        val Saturn = Value
        val Uranus = Value
        val Neptune = Value
        val Pluto = Value
      }
      Planets.Mercury.id should be(0)
      Planets.Venus.id should be(1)
      Planets.Mercury.toString should be("Mercury") //How does it get the name? by Reflection.
      Planets.Venus.toString should be("Venus")
      (Planets.Earth == Planets.Earth) should be(true)
      (Planets.Neptune == Planets.Jupiter) should be(false)
    }
    // You can create an enumeration with your own index and your own Strings, in this koan,
    // we will start with an index of one and use Greek names instead of Roman
    "set their own index and name" in {
      object GreekPlanets extends Enumeration {
        val Mercury = Value(1, "Hermes")
        val Venus = Value(2, "Aphrodite")
        //Fun Fact: Tellus is Roman for (Mother) Earth
        val Earth = Value(3, "Gaia")
        val Mars = Value(4, "Ares")
        val Jupiter = Value(5, "Zeus")
        val Saturn = Value(6, "Cronus")
        val Uranus = Value(7, "Ouranus")
        val Neptune = Value(8, "Poseidon")
        val Pluto = Value(9, "Hades")
      }
      GreekPlanets.Mercury.id should be(1)
      GreekPlanets.Venus.id should be(2)
      GreekPlanets.Mercury.toString should be("Hermes")
      GreekPlanets.Venus.toString should be("Aphrodite")
      (GreekPlanets.Earth == GreekPlanets.Earth) should be(true)
      (GreekPlanets.Neptune == GreekPlanets.Jupiter) should be(false)
    }
    // Enumerations can be declared in one line if you are merely setting variables to Value
    "can be declared in one line" in {
      object Planets extends Enumeration {
        val Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, Neptune, Pluto = Value
      }
      Planets.Mercury.id should be(0)
      Planets.Venus.id should be(1)
      Planets.Mercury.toString should be("Mercury") 
      Planets.Venus.toString should be("Venus")
      (Planets.Earth == Planets.Earth) should be(true)
      (Planets.Neptune == Planets.Jupiter) should be(false)
    }
    "can be declared with a string value only" in {
      object GreekPlanets extends Enumeration {
        val Mercury = Value("Hermes")
        val Venus = Value("Aphrodite")
        val Earth = Value("Gaia")
        val Mars = Value("Ares")
        val Jupiter = Value("Zeus")
        val Saturn = Value("Cronus")
        val Uranus = Value("Ouranus")
        val Neptune = Value("Poseidon")
        val Pluto = Value("Hades")
      }
      GreekPlanets.Mercury.id should be(0)
      GreekPlanets.Venus.id should be(1)
      GreekPlanets.Mercury.toString should be("Hermes")
      GreekPlanets.Venus.toString should be("Aphrodite")
      (GreekPlanets.Earth == GreekPlanets.Earth) should be(true)
      (GreekPlanets.Neptune == GreekPlanets.Jupiter) should be(false)
      
    }
    "can be extended by extending the Val class" in {
      object Planets extends Enumeration {
        val G = 6.67300E-11;
        class PlanetValue(val i: Int, val name: String, val mass: Double, val radius: Double)
                extends Val(i: Int, name: String) {
          def surfaceGravity = G * mass / (radius * radius)
          def surfaceWeight(otherMass: Double) = otherMass * surfaceGravity
        }
        val Mercury = new PlanetValue(0, "Mercury", 3.303e+23, 2.4397e6)
        val Venus = new PlanetValue(1, "Venus", 4.869e+24, 6.0518e6)
        val Earth = new PlanetValue(2, "Earth", 5.976e+24, 6.37814e6)
        val Mars = new PlanetValue(3, "Mars", 6.421e+23, 3.3972e6)
        val Jupiter = new PlanetValue(4, "Jupiter", 1.9e+27, 7.1492e7)
        val Saturn = new PlanetValue(5, "Saturn", 5.688e+26, 6.0268e7)
        val Uranus = new PlanetValue(6, "Uranus", 8.686e+25, 2.5559e7)
        val Neptune = new PlanetValue(7, "Neptune", 1.024e+26, 2.4746e7)
        val Pluto = new PlanetValue(8, "Pluto", 1.27e+22, 1.137e6)
      }
      val earth = Planets.Earth
      earth.mass should be(5.976e+24)
      earth.radius should be(6.37814e6)
      earth.surfaceGravity should be (9.802652743337129)
      earth.surfaceWeight(100) should be (980.2652743337129)
    }
  }
}
