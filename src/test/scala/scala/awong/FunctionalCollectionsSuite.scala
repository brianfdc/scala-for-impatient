package scala.awong


import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class FunctionalCollectionsSuite extends FunSuite with BeforeAndAfter {
  var list: List[Int] = _
  var seq: Seq[Int] = _
  
  before {
    list = (1 to 100).toList
    seq = (1 to 100).toSeq
  }
  
  test("head/tail of linked list") {
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
  
  test("shuffling") {
    val range = 1 to 20
    val rn = FunctionalCollections.shuffle(range.toList)
    println("shuffled range:" + rn.mkString(",") + "\n")
    expect(range.size) { rn.size }
    expect(false) { rn.head == range.head }
  }
  
  test("set methods") {
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
  
  test("filtering/partition of linked list ") {
    val (evens, odds) = list.partition( _ % 2 == 0)
    expect(50) { evens.size }
    expect(0) { odds.filter(_ % 2 == 0).size }
  }
  
  test("map of linked list") {
    val doubled = list.map( _ * 2 )
    expect(100) { doubled.filter(_ % 2 == 0).size }
    expect(200) { doubled.flatMap( each => List(each * 2, each % 2 ) ).size }
  }

  test("testing mapping") {
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
  
  test("reduce and fold linked lists") {
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
  
  test("linked list accumulate") {
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
  
  test("mutable list accumulate") {
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
  
  test("parallelizing collections") {
    val range = 0 until 1000
    val parallelRange = range.par
    val result = parallelRange.map{ each => print(each + " "); each }
    expect( parallelRange.length ) { result.length }
    
    expect(parallelRange.sum) {
      /*
       * aggregate requires two operations
       * (1) a seqop applied to partitions of the collection
       * (2) a parop to combine the results applied to the partitions
       */
      val sum = parallelRange.aggregate(0)( (a,b) => {
        a + b
      },{ (a1,a2) =>
        println("adding: (" + a1 + "," + a2 + ")")
        a1 + a2
      })
      
      ( sum )
    }
  }
}