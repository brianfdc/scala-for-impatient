package scala.impatient

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner

import org.junit.runner.RunWith

import scala.impatient.Exercises14._

@RunWith(classOf[JUnitRunner])
class Exercises14Suite extends FunSuite with BeforeAndAfter {
  before {
    println("executing before")
  }

  test("14.2: swap integers") {
    expect( (2,1) ) { Exercises14.swap(1,2) }
  }

  test("14.3: swap first two elements of list") {
    var arry = List("foo","bar","barry","baz")
    expect( List("bar","foo","barry","baz") ) {
      Exercises14.swap(arry)
    }
    arry = List("foo","bar");
    expect( List("bar","foo") ) {
      Exercises14.swap(arry)
    }
    arry = List("foo");
    expect( List("foo") ) {
      Exercises14.swap(arry)
    }
    arry = List[String]();
    expect( true ) {
      Exercises14.swap(arry).isEmpty
    }
  }
  
  test("14.4: case class for Items") {
    val product0 = Product(3.14, "Blackwell Toaster")
    val product1 = Product(13.14, "Ginzu Knife")
    val product2 = Product(272.52, "Kitchen Aid Stand Mixer")
    expect( 3.14 ) {
      product0.price
    }
    
    val multiple = Mutiple(10, product0, "10 Toasters")
    expect( product0.price * 10 ) {
      multiple.price
    }
    
    val wishList = Bundle("Wedding List")
    wishList.add(product0)
    wishList.add(product1)
    wishList.add(product2)
    expect( product0.price + product1.price + product2.price ) {
      wishList.price
    }
    
    wishList.add(multiple)
    expect( product0.price + product1.price + product2.price + multiple.price ) {
      wishList.price
    }
  }
  
  test("14.5: recursive descent of tree") {
    expect( 0 ) {
      Exercises14.leafSum( List() )
    }
    expect( 2 ) {
      Exercises14.leafSum( List(1,1) )
    }
    expect( 7 ) {
      Exercises14.leafSum( List(2, List(5) ) )
    }
    expect( 18 ) {
      Exercises14.leafSum( List(List(3,8), 2, List(5) ) )
    }
  }
  
  test("14.6: recursive descent of tree") {
    import scala.impatient.Exercises14.{BTrie, Node, Leaf, BinaryNode}
    expect( 1 ) {
      Exercises14.treeSum(
          Node( Leaf(1)) )
    }
    expect( 2 ) {
      Exercises14.treeSum(
          Node( Leaf(1),Leaf(1)) )
    }
    expect( 7 ) {
      Exercises14.treeSum(
          Node(Leaf(2), 
               Node(Leaf(5),Leaf(0))
              )
      )
    }
    expect( 18 ) {
      Exercises14.treeSum(
          BinaryNode(BinaryNode(Leaf(3),
                     Leaf(8)),
                     BinaryNode(Leaf(2),Leaf(5))
                    )
      )
    }
    expect( 18 ) {
      Exercises14.treeSum(
          Node(Node(Leaf(3),Leaf(8)),
               Leaf(2),
               Node(Leaf(5))
          )
        )
    }
  }
}