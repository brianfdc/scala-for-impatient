package org.functionalkoans.forscala


import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.Specs
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.junit.runners.Suite.SuiteClasses

@RunWith(classOf[JUnitRunner])
class AboutImportsAndPackages extends KoanSpec("About imports and packages") {
  "An import" can {
    "be placed in a method (hint: this koan is a method)" in {
      import scala.collection.mutable.ArrayBuffer
      val arrayBuffer = ArrayBuffer.range(2, 10)
      arrayBuffer(0) should be(2)
      arrayBuffer(1) should be(3)
    }
  }
}

case class Artist(val firstName: String, val lastName: String)

package subpackage {
  @RunWith(classOf[JUnitRunner])
  class AboutImportsAndPackagesInSubpackages extends KoanSpec("subpackages") {
    "A package" can {
      """be included in a file with an established package,
      and can encapsulate its contents with a {} block""" in {
        val luther = Artist("Luther", "Vandross")
        luther.lastName should be("Vandross")
      }
    }
  }
}

package album {
  case class Album(val name: String, val year: Short, val artist: Artist)
}

package media {
  @RunWith(classOf[JUnitRunner])
  class AboutReferencingAbsolutePackages extends KoanSpec("Referencing absolute packages") {
    import org.functionalkoans.forscala.album.Album
    // <<< Note the import style
    "A import" can {
      "be done based from absolute package hierarchy" in {
        val stLouisBlues = Album("St. Louis Blues", 1940, Artist("Louie", "Armstrong"))
        stLouisBlues.getClass.getCanonicalName should be("org.functionalkoans.forscala.album.Album")
      }
    }
  }
  @RunWith(classOf[JUnitRunner])
  class AboutReferencingAbsoluteRootPackages extends KoanSpec("Referencing absoulte root packages") {
    import _root_.org.functionalkoans.forscala.album.Album
    // <<< Note the import style
    "A import" can {
      "be done based from absolute root package heirarchy using _root_" in {
        val stLouisBlues = Album("St. Louis Blues", 1940, Artist("Louie", "Armstrong"))
        stLouisBlues.getClass.getCanonicalName should be("org.functionalkoans.forscala.album.Album")
      }
    }
  }
  @RunWith(classOf[JUnitRunner])
  class AboutReferencingRelativePackages extends KoanSpec("Referncing relative package") {
    import album.Album
    // <<< Note the import style
    "A import" can {
      "be done based from relative packaging" in {
        val stLouisBlues = Album("St. Louis Blues", 1940, Artist("Louie", "Armstrong"))
        stLouisBlues.getClass.getCanonicalName should be("org.functionalkoans.forscala.album.Album")
      }
    }
  }
}

package music_additions {
  class Genre(val name: String)
  class Producer(val firstName: String, lastName: String)
  class Distributor(val name: String)
}

@RunWith(classOf[JUnitRunner])
class AboutImportingTechniques extends KoanSpec("importing techniques") {
  "Imports" can {
    "use _ as a wildcard to import all classes of a package" in {
      import music_additions._
      val genre = new Genre("Jazz")
      val producer = new Producer("Joe", "Oliver")
      val distributor = new Distributor("RYKO Classic Music")

      genre.name should be("Jazz")
      producer.firstName should be("Joe")
      distributor.name should be("RYKO Classic Music")
    }
    "also use {_} as a wildcard to import all classes of a package" in {
      import music_additions.{_}
      val genre = new Genre("Jazz")
      val producer = new Producer("Joe", "Oliver")
      val distributor = new Distributor("RYKO Classic Music")

      genre.name should be("Jazz")
      producer.firstName should be("Joe")
      distributor.name should be("RYKO Classic Music")
    }
    "selectively pick a group of classes in a package using 'use {className1, className2}'" in {
      import music_additions.{Genre, Distributor}
      val genre = new Genre("Jazz")
      val distributor = new Distributor("RYKO Classic Music")

      genre.name should be("Jazz")
      distributor.name should be("RYKO Classic Music")
    }
    "alias a class by using '{original => alias}'" in {
      import music_additions.{Genre => MusicType, Distributor}
      val musicType = new MusicType("Jazz")
      val distributor = new Distributor("RYKO Classic Music")
      musicType.name should be("Jazz")
      distributor.name should be("RYKO Classic Music")
    }
    "alias a class by using '{original => alias}' and also import all other classes in a package keeping their name" in {
      import music_additions.{Genre => MusicType, _}
      val musicType = new MusicType("Jazz")
      val producer = new Producer("Joe", "Oliver")
      val distributor = new Distributor("RYKO Classic Music")

      musicType.name should be("Jazz")
      producer.firstName should be("Joe")
      distributor.name should be("RYKO Classic Music")
    }
    "refuse classes from being imported using '=> _'" in {
      import music_additions.{Producer => _, _}
      val musicType = new Genre("Jazz")
      val distributor = new Distributor("RYKO Classic Music")

      musicType.name should be("Jazz")
      distributor.name should be("RYKO Classic Music")
    }
    "import just the package itself so you can give its members a verbose identity" in {
      import scala.collection.mutable
      val arrayBuffer = mutable.ArrayBuffer.range(2, 10) //sounds better: A Mutable ArrayBuffer
      arrayBuffer(0) should be(2)
      arrayBuffer(1) should be(3)
    }
    "alias the imported package" in {
      import scala.collection.{mutable => changeable}
      val arrayBuffer = changeable.ArrayBuffer.range(2, 10)
      arrayBuffer(0) should be(2)
      arrayBuffer(1) should be(3)
    }
  }

}

