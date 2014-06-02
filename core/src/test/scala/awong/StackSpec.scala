package awong

import scala.collection.mutable.Stack

import org.scalatest.Tag
import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

/**
 * @see http://www.scalatest.org/user_guide/writing_your_first_test
 */
@RunWith(classOf[JUnitRunner])
class StackSpec extends AbstractFlatSpec {
  "A Stack" should "pop values in last-in-first-out order" in {
    val stack = Stack[Int]()
    stack.push(1)
    stack.push(2)
    assert(stack.pop === 2, "stack should be LIFO")
    expect(1, "stack should now pop first item") {
      stack.pop
    }
  }

  it should "throw NoSuchElementException if an empty stack is popped" in {
    val emptyStack = Stack[String]()
    intercept[NoSuchElementException] {
      emptyStack.pop()
    }
  }
  
  ignore should "do nothing" in {
    val emptyStack = Stack[String]()
    emptyStack.push("a")
    
  }
  
  "The Scala language" must "add correctly" taggedAs(SlowTest) in {
    val sum = 1 + 1
    assert(sum === 2)
  }

  it must "subtract correctly" taggedAs(SlowTest, DbTest) in {
    val diff = 4 - 1
    assert(diff === 3)
  }
}

object SlowTest extends Tag("scala.awong.tags.SlowTest")
object DbTest extends Tag("scala.awong.tags.DbTest")
