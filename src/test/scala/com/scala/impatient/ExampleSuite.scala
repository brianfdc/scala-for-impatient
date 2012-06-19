package com.scala.impatient

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable.Stack
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ExampleSuite extends FunSuite with BeforeAndAfter {
  var stack: Stack[Int] = _
  
  before {
    stack = new Stack[Int]
  }
  
  test("pop is invoked on a non-empty stack") {
    val stack = new Stack[Int]
    stack.push(1)
    stack.push(2)
    val oldSize = stack.size
    val result = stack.pop()
    assert(result === 2)
    assert(stack.size === oldSize - 1)
  }
  
  test("pop is invoked on an empty stack") {
    val emptyStack = new Stack[Int]
    intercept[NoSuchElementException] {
      emptyStack.pop()
    }
    assert(emptyStack.isEmpty)
  }
}