package awong.impatient

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import awong.impatient.Exercises14._
import awong.AbstractFlatSpec

@RunWith(classOf[JUnitRunner])
class Exercises14Spec extends AbstractFlatSpec {
  
  "(14.2)" should "swap integers" in {
    expectResult( (2,1),"swapped integers" ) {
      Exercises14.swap(1,2)
    }
  }
  
  "(14.3)" should "swap 1st 2 elements of a list" in {
    var arry = List("foo","bar","barry","baz")
    expectResult( List("bar","foo","barry","baz") ) {
      Exercises14.swap(arry)
    }
    arry = List("foo","bar");
    expectResult( List("bar","foo") ) {
      Exercises14.swap(arry)
    }
    arry = List("foo");
    expectResult( List("foo"),"don't swap if size < 2" ) {
      Exercises14.swap(arry)
    }
    arry = List[String]();
    expectResult( true, "don't swap if empty list" ) {
      Exercises14.swap(arry).isEmpty
    }
  }
  
  "(14.4)" should "add totals for items" in {
    val product0 = Product(3.14, "Blackwell Toaster")
    val product1 = Product(13.14, "Ginzu Knife")
    val product2 = Product(272.52, "Kitchen Aid Stand Mixer")
    expectResult( 3.14 ) {
      product0.price
    }
    
    val multiple = Mutiple(10, product0, "10 Toasters")
    expectResult( product0.price * 10 ) {
      multiple.price
    }
    
    val wishList = Bundle("Wedding List")
    wishList.add(product0)
    wishList.add(product1)
    wishList.add(product2)
    expectResult( product0.price + product1.price + product2.price ) {
      wishList.price
    }
    
    wishList.add(multiple)
    expectResult( product0.price + product1.price + product2.price + multiple.price ) {
      wishList.price
    }
  }
  
  "(14.5)" should "descend a tree recursively" in {
    expectResult( 0 ) {
      Exercises14.leafSum( List() )
    }
    expectResult( 2 ) {
      Exercises14.leafSum( List(1,1) )
    }
    expectResult( 7 ) {
      Exercises14.leafSum( List(2, List(5) ) )
    }
    expectResult( 18 ) {
      Exercises14.leafSum( List(List(3,8), 2, List(5) ) )
    }
  }
  
  "(14.6)" should "descend a tree recursively" in {
    import awong.impatient.Exercises14.{BTrie, Node, Leaf, BinaryNode}
    expectResult( 1 ) {
      Exercises14.treeSum(
          Node( Leaf(1)) )
    }
    expectResult( 2 ) {
      Exercises14.treeSum(
          Node( Leaf(1),Leaf(1)) )
    }
    expectResult( 7 ) {
      Exercises14.treeSum(
          Node(Leaf(2), 
               Node(Leaf(5),Leaf(0))
              )
      )
    }
    expectResult( 18 ) {
      Exercises14.treeSum(
          BinaryNode(BinaryNode(Leaf(3),
                     Leaf(8)),
                     BinaryNode(Leaf(2),Leaf(5))
                    )
      )
    }
    expectResult( 18 ) {
      Exercises14.treeSum(
          Node(Node(Leaf(3),Leaf(8)),
               Leaf(2),
               Node(Leaf(5))
          )
        )
    }
  }
  
  "(14.9)" should "sum up options" in {
    val options = List(Some(1),
                       Some(2),
                       None,
                       Some(3),
                       None,
                       Some(4) )
    expectResult(6) {
      options.size
    }
    expectResult(10) {
      Exercises14.sumOptions(options)
    }
  }
  
  "14.10" should "compose functions with optional outputs" in {
    def f(x:Double) ={
      if (x >= 0)
        Some(Math.sqrt(x))
      else
        None
    }
    def g(x:Double) = {
      if (x != 1)
        Some( 1 / (x-1) )
      else
        None
    }
    val h = Exercises14.compose(f,g)
    
    expectResult(Some(1)) {
      h(2)
    }
    expectResult(None) {
      g(1)
    }
    expectResult(None) {
      h(1)
    }
    expectResult(Some(-1)) {
      g(0)
    }
    expectResult(None) {
      h(0)
    }
  }
}