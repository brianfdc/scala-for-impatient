package scala.awong


import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class CollectionsSuite extends FunSuite with BeforeAndAfter {
  var list: List[Int] = _
  var seq: Seq[Int] = _
  
  before {
    list = (1 to 100).toList
    seq = (1 to 100).toSeq
  }
  
  test("head/tail of linked list") {
    expect(1) { list.head }
    expect(99) { list.tail.size }
    
    expect(60) { list.drop(40).size }
    expect(40) { list.take(40).size }
    
    
    
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

  test("reduce and fold linked lists") {
    def max(a:Int, b:Int): Int = {
       if (a > b) a else b
    }
    expect(100) {  list.reduceLeft{ max(_,_) }  }
    
    expect(100) {  list.foldLeft(0){ max(_,_) } }
    // operator form of fold left
    expect(100) {  ( 0 /: list ) { max(_,_) }  }
    
    expect(100) {  list.foldRight(0){ max(_,_) } }
    // operator form of fold right
    expect(100) {  ( list :\ 0 ) { max(_,_) }  }
  }
}