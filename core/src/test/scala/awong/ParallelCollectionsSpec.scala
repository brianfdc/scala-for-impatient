package awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ParallelCollectionsSuite extends AbstractFlatSpec {
  "Scala collections" should "process in parallel" in {
    val range = 0 until 1000
    val parallelRange = range.par
    val result = parallelRange.map{ each => print(each + " "); each }
    expectResult( parallelRange.length ) {
      result.length
    }
    
    expectResult(parallelRange.sum, "parallel summation == sequential summation") {
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