package scala.awong

/**
 * http://stackoverflow.com/questions/2705920/how-to-define-a-ternary-operator-in-scala-which-preserves-leading-tokens
 */
object Ternary {
  class IfTrue[A](b: => Boolean, t: => A) { def |(f: => A) = if (b) t else f }
  class MakeIfTrue(b: => Boolean) { def ?[A](t: => A) = new IfTrue[A](b,t) }
  implicit def autoMakeIfTrue(b: => Boolean) = new MakeIfTrue(b)
}