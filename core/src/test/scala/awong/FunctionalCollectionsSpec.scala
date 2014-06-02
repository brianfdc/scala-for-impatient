package scala.awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.FunctionalCollections

@RunWith(classOf[JUnitRunner])
class FunctionalCollectionsSpec extends AbstractFlatSpec {
  var list: List[Int] = _
  var seq: Seq[Int] = _
  
  before {
    list = (1 to 100).toList
    seq = (1 to 100).toSeq
  }
  
  "A linked list" should "yield head and tail" in {
    expect(1) {
      list.head
    }
    expect(99) {
      list.tail.size
    }
    expect(60) {
      list.drop(40).size
    }
    expect(40) {
      list.take(40).size
    }
  }
  
  "A range" should "shuffle randomly" in {
    val range = 1 to 20
    val rn = FunctionalCollections.shuffle(range.toList)
    println("shuffled range:" + rn.mkString(",") + "\n")
    expect(range.size) { rn.size }
    expect(false) { rn.head == range.head }
  }
  
  "A set" should "correctly union, intersect and difference" in {
    val aSet = (1 to 3).toSet
    val bSet = (3 to 7).toSet
    
    expect(1) {
      (aSet & bSet).size
    }
    expect(1) {
      (aSet intersect bSet).size
    }
    expect(7) {
      (aSet | bSet).size
    }
    expect(7) {
      (aSet union bSet).size
    }
    println( aSet &~ bSet )
    expect(2) {
      (aSet &~ bSet).size
    }
    expect(4) {
      (bSet diff aSet).size
    }
  }
  
  "A linked list" should "filter and partition" in {
    val (evens, odds) = list.partition( _ % 2 == 0)
    expect(50) { evens.size }
    expect(0) { odds.filter(_ % 2 == 0).size }
  }
  
  "A linked list" should "map to another list" in {
    val doubled = list.map( _ * 2 )
    expect(100) { doubled.filter(_ % 2 == 0).size }
    expect(200) { doubled.flatMap( each => List(each * 2, each % 2 ) ).size }
  }

  "A linked list" should "map to yet another list" in {
    val list = List(1,2,3)
    expect(List(2,3,4)) {
      list.map( _ + 1) // (2,3,4)
    }
    
    val words = List("the","quick", "brown", "fox")
    
    val mapped = words.map(_.toList)
    // List(List("t","h","e"), List("q","u","i","c","k"),List("b","r","o","w","n"),List("f","o","x"))
    expect( 4 ) {
      mapped.size
    }
    
    val flatMap = words.flatMap(_.toList)
    // List("t","h","e", "q","u","i","c","k","b","r","o","w","n","f","o","x")
    expect( 16 ) {
      flatMap.size
    }
    var sum = 0
    
    expect( 6 ) {
      list.foreach( sum += _)
      sum
    }
    
  }
  
  "A linked list" should "reduce and fold" in {
    def max(a:Int, b:Int): Int = {
       if (a > b) a else b
    }
    expect(100) {
      list.reduceLeft{ max(_,_) }
    }
    
    expect(100) {
      list.foldLeft(0){ max(_,_) }
    }
    // operator form of fold left
    expect(100) {
      ( 0 /: list ) { max(_,_) }
    }
    expect(100) {
      list.foldRight(0){ max(_,_) }
    }
    // operator form of fold right
    expect(100) {
      ( list :\ 0 ) { max(_,_) }
    }
  }
  
  "A linked list" should "accumulate" in {
    expect(seq.size * 2) {
      val result = ( List[Double]() /: seq ) { (doubles,each) =>
        // prepend to doubles
        each.toDouble :: doubles
      }
      println (result.size)
      // same as java.util.List.addAll
      ( result ::: list.map(_.toDouble) ).size
    }
    
    expect(list.size) {
      val result = ( Seq[Double]() /: list ) { (doubles,each) =>
        // prepend to doubles
        each.toDouble +: doubles
      }
      result.size
    }
    
    expect(list.size) {
      val result = ( Seq[Double]() /: list ) { (doubles,each) =>
        // append to doubles
        doubles :+ each.toDouble
      }
      result.size
    }
  }
  
  "A mutable list" should "accumulate" in {
    expect(seq.size * 2) {
      val result = ( scala.collection.mutable.Buffer[Int]() /: seq ) { (doubles,each) =>
        // prepend to doubles
        doubles += (each * 2)  
      }
      ( result ++= list.toBuffer ).size
      
    }
    
    expect(list.size) {
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