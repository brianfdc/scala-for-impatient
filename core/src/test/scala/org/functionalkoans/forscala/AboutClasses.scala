package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutClasses extends KoanSpec("about classes") {
  // you can define class with var or val parameters
  class ClassWithVarParameter(var description: String)
  class ClassWithValParameter(val name: String)
  // you can define class with private fields
  class ClassWithPrivateFields(name: String)

  "Scala classes" must {
     "define a getter with val parameters in class definition" in {
       val aClass = new ClassWithValParameter("name goes here")
       aClass.name should be (__);
     }
     "define a getter and setter with var parameters in class definition" in {
       val aClass = new ClassWithVarParameter("description goes here")
       aClass.description should be (__)
       aClass.description = "new description"
       aClass.description should be (__)
     }
     
     "define fields internally which are private to a class" in {
       val aClass = new ClassWithPrivateFields("name")
       // NOTE: aClass.name is not accessible
     }
  }



}
