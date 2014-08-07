package awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class FunctionalCollectionsSpec extends AbstractFlatSpec {
  var list: List[Int] = _
  var seq: Seq[Int] = _
  
  before {
    list = (1 to 100).toList
    seq = (1 to 100).toSeq
  }
  
  "A linked list" should "yield head and tail" in {
    expectResult(1) {
      list.head
    }
    expectResult(99) {
      list.tail.size
    }
    expectResult(60) {
      list.drop(40).size
    }
    expectResult(40) {
      list.take(40).size
    }
  }
  
  "A range" should "shuffle randomly" in {
    val range = 1 to 20
    val rn = FunctionalCollections.shuffle(range.toList)
    logger.debug("shuffled range:" + rn.mkString(",") + "\n")
    expectResult(range.size) { rn.size }
    expectResult(false) { rn.head == range.head }
  }
  
  "A set" should "correctly union, intersect and difference" in {
    val aSet = (1 to 3).toSet
    val bSet = (3 to 7).toSet
    
    expectResult(1) {
      (aSet & bSet).size
    }
    expectResult(1) {
      (aSet intersect bSet).size
    }
    expectResult(7) {
      (aSet | bSet).size
    }
    expectResult(7) {
      (aSet union bSet).size
    }
    expectResult(2) {
      val setDifference = (aSet &~ bSet)
      val size =  setDifference.size
      logger.debug("Set difference size should be {}", size.toString)
      size
    }
    expectResult(4) {
      (bSet diff aSet).size
    }
  }
  
  "A linked list" should "filter and partition" in {
    val (evens, odds) = list.partition( _ % 2 == 0)
    expectResult(50) { evens.size }
    expectResult(0) { odds.filter(_ % 2 == 0).size }
  }
  
  "A linked list" should "map to another list" in {
    val doubled = list.map( _ * 2 )
    expectResult(100) { doubled.filter(_ % 2 == 0).size }
    expectResult(200) { doubled.flatMap( each => List(each * 2, each % 2 ) ).size }
  }

  "A linked list" should "map to yet another list" in {
    val list = List(1,2,3)
    expectResult(List(2,3,4)) {
      list.map( _ + 1) // (2,3,4)
    }
    
    val words = List("the","quick", "brown", "fox")
    
    val mapped = words.map(_.toList)
    // List(List("t","h","e"), List("q","u","i","c","k"),List("b","r","o","w","n"),List("f","o","x"))
    expectResult( 4 ) {
      mapped.size
    }
    
    val flatMap = words.flatMap(_.toList)
    // List("t","h","e", "q","u","i","c","k","b","r","o","w","n","f","o","x")
    expectResult( 16 ) {
      flatMap.size
    }
    var sum = 0
    
    expectResult( 6 ) {
      list.foreach( sum += _)
      sum
    }
    
  }
  
  "A linked list" should "reduce and fold" in {
    def max(a:Int, b:Int): Int = {
       if (a > b) a else b
    }
    expectResult(100) {
      list.reduceLeft{ max(_,_) }
    }
    
    expectResult(100) {
      list.foldLeft(0){ max(_,_) }
    }
    // operator form of fold left
    expectResult(100) {
      ( 0 /: list ) { max(_,_) }
    }
    expectResult(100) {
      list.foldRight(0){ max(_,_) }
    }
    // operator form of fold right
    expectResult(100) {
      ( list :\ 0 ) { max(_,_) }
    }
  }
  
  "A linked list" should "accumulate" in {
    expectResult(seq.size * 2) {
      val result = ( List[Double]() /: seq ) { (doubles,each) =>
        // prepend to doubles
        each.toDouble :: doubles
      }
      logger.debug(result.size.toString)
      // same as java.util.List.addAll
      ( result ::: list.map(_.toDouble) ).size
    }
    
    expectResult(list.size) {
      val result = ( Seq[Double]() /: list ) { (doubles,each) =>
        // prepend to doubles
        each.toDouble +: doubles
      }
      result.size
    }
    
    expectResult(list.size) {
      val result = ( Seq[Double]() /: list ) { (doubles,each) =>
        // append to doubles
        doubles :+ each.toDouble
      }
      result.size
    }
  }
  
  "A mutable list" should "accumulate" in {
    expectResult(seq.size * 2) {
      val result = ( scala.collection.mutable.Buffer[Int]() /: seq ) { (doubles,each) =>
        // prepend to doubles
        doubles += (each * 2)  
      }
      ( result ++= list.toBuffer ).size
    }
    
    expectResult(list.size) {
      val result = ( list.toBuffer /: list ) { (doubles,each) =>
        if (each % 2 == 0) {
          // remove evens from doubles
          doubles -= each
        } else {
          doubles += each
        } 
      }
      result.size
    }
  }
}