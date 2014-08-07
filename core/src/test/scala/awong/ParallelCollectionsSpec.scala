package awong

import collection.parallel.ParSeq

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ParallelCollectionsSuite extends AbstractWordSpec {
  var n: Int = _
  var range: Seq[Int] = _
  var parSeq: ParSeq[Int] = _
  
  before {
    n = 10000
    range = (0 until n)
    parSeq = range.par
  }
  
  "Scala collections" when {
    "processing in parallel" should {
      "have same length as the original for a simple map" in {
        val result = parSeq.map{ each => print(each + " "); each }
        result.length shouldBe parSeq.length
      }
      
      "aggregate in a map/reduce pattern just like a sequential summation" in {
        val parallelSum = parSeq.aggregate(0)( (a,b) => {
          logger.debug("seqop applied to partitions of collection: {} + {}", a,b)
          a + b
        },{ (a1,a2) =>
          logger.debug("parop combines the results applied to the partions: {} + {}:", a1, a2)
          a1 + a2
        })
        parallelSum should === (parSeq.sum)
      }
    }
  }

}