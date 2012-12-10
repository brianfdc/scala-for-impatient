package scala.impatient

import java.math.MathContext
/**
 * Pattern Matching and Case Classes
 */
object Exercises14 {
  /**
   * (14.2)
   */
  def swap(pair: Tuple2[Int,Int]): Tuple2[Int,Int] = {
    val result = pair match {
      case(x,y) => (y,x)
    }
    result
  }
  
  /*
   * (14.3)
   */
  def swap[T](lst: List[T]): List[T] = {
    lst match {
      case x::y::tail => (y::x::tail)
      case _ => lst
    }
  }
  /**
   * (14.4)
   */
  abstract class Item {
    def price: Double
    def description: String
  }
  case class Product(val price: Double, val description: String) extends Item
  case class Bundle(val description: String) extends Item {
    var items = List[Item]()
    def price : Double = {
      // this has obvious rounding errors
      val prices = items.map(_.price)
      var total = prices.reduceLeft(_ + _)
      total
    }
    def add(item:Item) = {
      items = item :: items
    }
  }
  case class Mutiple(val quantity: Int, val product: Item, val description: String) extends Item {
    def price : Double = {
      // this has obvious rounding errors
      val total = product.price * quantity;
      total
    }
  }
  
  /**
   * (14.5)
   */
  private def leafSumImpl(total:Int, nodes:List[Any]): Int = {
    nodes match {
      case List() => total
      case head::tail => 
        head match {
          case x:Int => leafSumImpl(x + total, tail)
          case y:List[Any] => leafSumImpl(total, y ::: tail)
          case _ => total
        }
    }
  }
  def leafSum(tree:List[Any]): Int = {
    val result = leafSumImpl(0, tree)
    result
  }
  /**
   * (14.6/7)
   */
  sealed abstract class BTrie
  case class Leaf(value:Int) extends BTrie
  case class BinaryNode(left:BTrie, right:BTrie) extends BTrie
  case class Node(nodes:BTrie*) extends BTrie
  
  def treeSumImpl(total:Int, tree: BTrie):Int = {
    tree match {
      case leaf : Leaf => total + leaf.value
      case binaryNode: BinaryNode => treeSumImpl(total, binaryNode.left) + treeSumImpl(total, binaryNode.right)
      case node: Node =>
        node.nodes.foldLeft(total) { (runningTotal,eachNode) =>
          runningTotal + treeSumImpl(0,eachNode)
        }
    }
  }
  def treeSum(tree:BTrie): Int = {
    treeSumImpl(0, tree)
  }
  
  /**
   *  (14.8) Previews ideas in DSLs (chapter 19)
   */
  
  /**
   * (14.10)
   */
  def sumOptions(options: List[Option[Int]]): Int = {
    options.foldLeft(0) { (subTotal,eachOption) =>
      subTotal + eachOption.getOrElse(0)
    }
  }
  
  /**
   * (14.11)
   */
  def compose(f: Double=>Option[Double], g: Double=>Option[Double]): Double=>Option[Double] = {
    g.apply(_) match {
      case Some(x) => f.apply(x)
      case None => None
    }
  }
}