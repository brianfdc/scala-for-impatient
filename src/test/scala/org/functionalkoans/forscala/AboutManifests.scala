package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutManifests extends KoanSuite {
  koan("""Manifests can be used to determine a type used
         |   before it erased by the VM by using an implicit manifest argument.""") {
    def inspect[T](l: List[T])(implicit manifest: scala.reflect.Manifest[T]) = manifest.toString
    val list = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
    inspect(list) should be(__)
  }

  koan("""Manifests can be attached to classes. Manifests have other meta-information about
          |  the type erased""") {
    class Barrel[T](implicit m: scala.reflect.Manifest[T]) {
      def +(t: T) = "1 %s has been added".format(m.erasure.getSimpleName) //Simple-name of the class erased
    }
    val monkeyBarrel = new Barrel[Monkey]
    (monkeyBarrel + new Monkey) should be(__)
  }
}

class Monkey

