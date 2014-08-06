package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSpec
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class AboutTypeVariance extends KoanSpec("About type variance") {

  class Fruit

  abstract class Citrus extends Fruit
  class Orange extends Citrus
  class Tangelo extends Citrus
  class Apple extends Fruit
  class Banana extends Fruit
  
  class InvariantContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
    private[this] var item = a
    def set(a: A): Unit = { item = a }
    def get = item
    def contents = manifest.erasure.getSimpleName
  }
  
  "With type inference, the type that you instantiate" must {
    "be the val or var reference type" in {
      val fruitBasket = new InvariantContainer(new Orange())
      fruitBasket.contents should be(__)
    }
  }
  
  "The type variable of the object during instantiation" can {
    "be explicitly declared" in {
      val fruitBasket = new InvariantContainer[Fruit](new Orange())
      fruitBasket.contents should be(__)
    }
  }
  

  "An object" can {
    "be coerced to a type" in {
      val fruitBasket: InvariantContainer[Fruit] = new InvariantContainer(new Orange())
      fruitBasket.contents should be(__)
    }
    
    val beforeMsg = """That one probably blew your mind. Now if you assign a type to the instantiation,
        | that's different to the variable type, you'll have problems.  You may want to take time after this
        | to compare this koan with the previous koan to compare and contrast."""
    println(beforeMsg)
    
    "not be coerced when we're dealing with a variable type, which must match the assigned type" in {
      // Uncomment the following line
      // val fruitBasket:MyContainer[Fruit] = new MyContainer[Orange](new Orange())
    }
    
    val afterMsg = """So, if you want to set a Fruit basket to an orange basket so how do we fix that? You make it covariant using +.
      | This will allow you to set the your container to a either a variable with the same type or parent type.
      | In other words, you can assign MyContainer[Fruit] or MyContainer[Citrus]."""
    println(afterMsg)
  }
  
  "Covariance" can {
    class CovariantContainer[+A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
      private[this] val item = a
      def get = item
      def contents = manifest.erasure.getSimpleName
    }
    
    "let you specify the container of that type or parent type" in {
      val fruitBasket: CovariantContainer[Fruit] = new CovariantContainer[Orange](new Orange())
      fruitBasket.contents should be(__)
    }
    
    val notaBene = """The problem with covariance is that you can't mutate, set, or change the object since
      | it has to guarantee that what you put in has to be that type.  In other words the reference is a fruit basket,
      | but we still have to make sure that no other fruit can be placed in our orange basket"""
    println(notaBene)
    
    "forbid mutating an object" in {
      val fruitBasket: CovariantContainer[Fruit] = new CovariantContainer[Orange](new Orange())
      fruitBasket.contents should be(__)
      
      class NavelOrange extends Orange //Creating a subtype to prove a point
      // val navelOrangeBasket: CovariantContainer[NavelOrange] = new CovariantContainer[Orange](new Orange()) //Bad!
      // val tangeloBasket: CovariantContainer[Tangelo] = new CovariantContainer[Orange](new Orange()) //Bad!
      
    }
  }
  
  "Contravariance, the opposite of covariance" can {
    class ContravariantContainer[-A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
        private[this] var item = a
        def set(a: A): Unit = { item = a }
        def contents = manifest.erasure.getSimpleName
    }
    
    "be declared with '-'" in {
      val msg = """Declaring '-' indicates contravariance variance.
        | Using '-' you can apply any container with a certain type to a container with a superclass of that type.
        | This is reverse to covariant.  In our example, we can set a citrus basket to
        | an orange or tangelo basket. Since an orange or tangelo basket is a citrus basket"""
      println(msg)
      
      val citrusBasket: ContravariantContainer[Citrus] = new ContravariantContainer[Citrus](new Orange)
      citrusBasket.contents should be(__)
      val orangeBasket: ContravariantContainer[Orange] = new ContravariantContainer[Citrus](new Tangelo)
      orangeBasket.contents should be(__)
      val tangeloBasket: ContravariantContainer[Tangelo] = new ContravariantContainer[Citrus](new Orange)
      tangeloBasket.contents should be(__)

      val orangeBasketReally: ContravariantContainer[Orange] = tangeloBasket.asInstanceOf[ContravariantContainer[Orange]]
      orangeBasketReally.contents should be(__)
      orangeBasketReally.set(new Orange())
    }
    
    "also imply: a reference to a parent type means you cannot anticipate getting a more specific type" in {
      val msg = """Declaring contravariance variance with - also means that the container cannot be accessed with a getter or
        | or some other accessor, since that would cause type inconsistency.  In our example, you can put an orange
        | or a tangelo into a citrus basket. Problem is, if you have a reference to an orange basket,
        | and if you believe that you have an orange basket then you shouldn't expect to get a tangelo out of it."""
      println(msg)
      val citrusBasket: ContravariantContainer[Citrus] = new ContravariantContainer[Citrus](new Orange)
      citrusBasket.contents should be(__)
      val orangeBasket: ContravariantContainer[Orange] = new ContravariantContainer[Citrus](new Tangelo)
      orangeBasket.contents should be(__)
      val tangeloBasket: ContravariantContainer[Tangelo] = new ContravariantContainer[Citrus](new Orange)
      tangeloBasket.contents should be(__)
    }
  }

  "Invariance variance" can {
    val msg = """Declaring neither -/+, indicates invariance variance.  You cannot use a superclass
        | variable reference (\"contravariant\" position) or a subclass variable reference (\"covariant\" position)
        | of that type.  In our example, this means that if you create a citrus basket you can only reference that
        | that citrus basket with a citrus variable only."""
    println(msg)
    
    "be declared used to specify the type exactly" in {
      val citrusBasket: InvariantContainer[Citrus] = new InvariantContainer[Citrus](new Orange)
      citrusBasket.contents should be(__)
    }
    
    "be used to both mutate and access elemnts from an object of a generic type" in {
      val citrusBasket: InvariantContainer[Citrus] = new InvariantContainer[Citrus](new Orange)
      citrusBasket.set(new Orange)
      citrusBasket.contents should be(__)
      citrusBasket.set(new Tangelo)
      citrusBasket.contents should be(__)
    }
  }
}