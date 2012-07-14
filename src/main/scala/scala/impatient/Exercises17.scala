package scala.impatient

/**
 * Type Parameters
 */
object Exercises17 {
  /**
   * (17.1-3)
   */
  class ImmutablePair[T,S](val first: T, val second: S) {
    def swap: ImmutablePair[S,T] = {
      new ImmutablePair(second, first)
    }
  }
  class MutablePair[T](var first: T, var second: T) {
    def swap() = {
      var temp = this.first
      this.first = second
      this.second = temp
    }
    // uses a lower bound
    def replaceFirst[R >: T](newFirst: R): MutablePair[R] = {
      new MutablePair(newFirst, second)
    }
    def replaceFirstWithoutLB[R](newFirst: R) = {
      new MutablePair(newFirst, second)
    }
  }
  
  class Pair[T,S](val first: T, val second:S) {
    def swap(pair:Pair[T,S]): Pair[S,T] = {
      new Pair(pair.second, pair.first)
    }
  }
  /**
   * (17.4)
   */
  
}