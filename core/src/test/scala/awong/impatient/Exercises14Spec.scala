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
    Exercises14.swap(1,2) should === (2,1)
  }
  
  "(14.3)" should "swap 1st 2 elements of a list" in {
    var arry = List("foo","bar","barry","baz")
    
    Exercises14.swap(arry) should === ( List("bar","foo","barry","baz") )
    arry = List("foo","bar");
    
    Exercises14.swap(arry) should === ( List("bar","foo") )
    arry = List("foo");
    Exercises14.swap(arry) should === ( List("foo") )
    logger.trace("don't swap if size < 2")
    arry = List[String]();
    logger.trace("don't swap if empty list")
    Exercises14.swap(arry) shouldBe 'isEmpty
  }
  
  "(14.4)" should "add totals for items" in {
    val product0 = Product(3.14, "Blackwell Toaster")
    val product1 = Product(13.14, "Ginzu Knife")
    val product2 = Product(272.52, "Kitchen Aid Stand Mixer")
    product0.price should === (3.14)

    
    val multiple = Mutiple(10, product0, "10 Toasters")
    multiple.price should === ( product0.price * 10 )
    
    val wishList = Bundle("Wedding List")
    wishList.add(product0)
    wishList.add(product1)
    wishList.add(product2)
    val prices = List( product0.price + product1.price + product2.price )
    wishList.price should === (prices.sum)
    wishList.add(multiple)
    val modifiedPrices = multiple.price  :: prices
    wishList.price should === (modifiedPrices.sum)
  }
  
  "(14.5)" should "descend a tree recursively" in {
    Exercises14.leafSum( List() ) shouldBe 0
    Exercises14.leafSum( List(1,1) ) shouldBe 2
    Exercises14.leafSum( List(2, List(5) ) ) shouldBe 7
    Exercises14.leafSum( List(List(3,8), 2, List(5) ) ) shouldBe 18
  }
  
  "(14.6)" should "descend a tree recursively" in {
    import awong.impatient.Exercises14.{BTrie, Node, Leaf, BinaryNode}

    Exercises14.treeSum(Node( Leaf(1)) ) shouldBe 1
    Exercises14.treeSum( Node( Leaf(1),Leaf(1)) ) shouldBe 2
    
    var node: BTrie = Node(Leaf(2),
                         Node(Leaf(5),Leaf(0))
                      )
    Exercises14.treeSum(node) shouldBe 7
    
    node = BinaryNode(BinaryNode(Leaf(3),
                     Leaf(8)),
                     BinaryNode(Leaf(2),Leaf(5))
                    )
    Exercises14.treeSum(node) shouldBe 18
    
    node = Node(Node(Leaf(3),Leaf(8)),
               Leaf(2),
               Node(Leaf(5))
           )
    Exercises14.treeSum(node) shouldBe 18
  }
  
  "(14.9)" should "sum up options" in {
    val options = List(Some(1),
                       Some(2),
                       None,
                       Some(3),
                       None,
                       Some(4) )
    options should have size 6
    Exercises14.sumOptions(options) shouldBe 10
  }
  
  "14.10" should "compose functions with optional outputs" in {
    def f(x:Double): Option[Double] ={
      if (x >= 0)
        Some(Math.sqrt(x))
      else
        None
    }
    def g(x:Double): Option[Double] = {
      if (x != 1)
        Some( 1 / (x-1) )
      else
        None
    }
    val h = Exercises14.compose(f,g)
    
    h(2) should be (Some(1))
    g(1) should be (None)
    h(1) should be (None)
    g(0) should be (Some(-1))
    h(0) should be (None)
  }
}