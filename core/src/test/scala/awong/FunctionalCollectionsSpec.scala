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
    list.head should be (1)
    list.tail should have size 99
    list.drop(40) should have size 60
    list.take(40) should have size 40
  }
  
  "A range" should "shuffle randomly" in {
    val range = 1 to 20
    val rn = FunctionalCollections.shuffle(range.toList)
    logger.trace("shuffled range:" + rn.mkString(",") + "\n")
    
    (range.size) shouldBe (rn.size)
  }
  
  "A set" should "correctly union, intersect and difference" in {
    val aSet = (1 to 3).toSet
    val bSet = (3 to 7).toSet

    (aSet & bSet) should have size 1
    (aSet intersect bSet) should have size 1
    (aSet | bSet) should have size 7
    (aSet union bSet) should have size 7
    val setDifference = (aSet &~ bSet)
    setDifference should have size 2
    
    (bSet diff aSet) should have size 4
  }
  
  "A linked list" should "filter and partition" in {
    val (evens, odds) = list.partition( _ % 2 == 0)
    
    evens should have size 50
    val xf = odds.filter(_ % 2 == 0)
    xf should have size 0
  }
  
  "A linked list" should "map to another list" in {
    val doubled = list.map( _ * 2 )
    val xs = doubled.filter(_ % 2 == 0)
    xs should have size 100
    val ys = doubled.flatMap( each => List(each * 2, each % 2 ) )
    ys should have size 200
  }

  "A linked list" should "map to yet another list" in {
    val list = List(1,2,3)
    val mappedInt = list.map( _ + 1)
    
    mappedInt should equal (List(2,3,4))
    
    val words = List("the","quick", "brown", "fox")
    
    val mappedWords = words.map(_.toList)
    // List(List("t","h","e"), List("q","u","i","c","k"),List("b","r","o","w","n"),List("f","o","x"))
    mappedWords should have size 4
    
    val flatMap = words.flatMap(_.toList)
    // List("t","h","e", "q","u","i","c","k","b","r","o","w","n","f","o","x")
    flatMap should have size 16
    var sum = 0
    list.foreach( sum += _)
    sum should be (6)
    
  }
  
  "A linked list" should "reduce and fold" in {
    def max(a:Int, b:Int): Int = {
       if (a > b) a else b
    }
    val reduceLeft = list.reduceLeft{ max(_,_) }
    reduceLeft shouldBe (100)
    
    val foldLeft = list.foldLeft(0){ max(_,_) }
    foldLeft shouldBe (100)

    // operator form of fold left
    val opFoldLeft = ( 0 /: list ) { max(_,_) }
    opFoldLeft shouldBe (100)
    
    val foldRight = list.foldRight(0){ max(_,_) }
    foldRight shouldBe (100)
    // operator form of fold right
    val opFoldRight = ( list :\ 0 ) { max(_,_) }
    opFoldRight shouldBe (100)

  }
  
  "A linked list" should "accumulate" in {
    
    val doubles0 = seq.foldLeft(List[Double]()) { (doubles,each) =>
      // prepend to doubles
       each.toDouble :: doubles
    }
    // same as java.util.List.addAll
    val doubles1 = doubles0 ::: list.map(_.toDouble)
    
    doubles1 should have size (seq.size * 2)
    
    val doubles2 = list.foldLeft(Seq[Double]()) { (doubles,each) =>
      // prepend to doubles
      each.toDouble +: doubles
    }
    doubles2 should have size (list.size)
    
    val doubles3 = list.foldLeft(Seq[Double]()) { (doubles,each) =>
      // append to doubles
      doubles :+ each.toDouble
    }
    doubles3 should have size (list.size)
  }
  
  "A mutable list" should "accumulate" in {
    
    
    val buffer0 = seq.foldLeft(scala.collection.mutable.Buffer[Int]()){ (doubles,each) =>
        // prepend to doubles
        doubles += (each * 2)  
    }
    val buffer1 = ( buffer0 ++= list.toBuffer )
    buffer1 should have size (seq.size * 2)
    
    
    val buffer2 = list.foldLeft(list.toBuffer) { (doubles,each) =>
        if (each % 2 == 0) {
          // remove evens from doubles
          doubles -= each
        } else {
          doubles += each
        } 
    }
    buffer2 should have size (list.size)
  }
}