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
  }
  
  class Pair[S,T](val first: T, val second:S) {
    def swap(pair:Pair[S,T]): Pair[T,S] = {
      new Pair(pair.second, pair.first)
    }
  }
  
}