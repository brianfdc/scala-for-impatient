package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutManifests extends KoanSpec("About manifests") {
  
  "Manifests" can {
    "be used to determine a type used before the VM erases it by using an implicit manifest argument " in {
      def inspect[T](l: List[T])(implicit manifest: scala.reflect.Manifest[T]) = manifest.toString
      val list = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
      inspect(list) should be(__)
    }
    "be attached to classes as they have other meta-information about the erased type" in {
      class Monkey
      class Barrel[T](implicit m: scala.reflect.Manifest[T]) {
        def +(t: T) = "1 %s has been added".format(m.erasure.getSimpleName) //Simple-name of the class erased
      }
      val monkeyBarrel = new Barrel[Monkey]
      (monkeyBarrel + new Monkey) should be(__)
    }
  }
}


