package scala.monads

/** 
 *  Provides a standard interface for compsoing and sequencing operations on some
 *  contained value(s)
 *  @see https://www.youtube.com/watch?v=Mw_Jnn_Y5iA
 */
trait Monad[A] {
	/**
	 * Applies a 'regular' function to the contained value(s)
	 */
	def map[B](f: A => B): Monad[B]
	/**
	 * Applies a 'monadic' function to the contained value
	 */
	def flatMap[B](f: A => Monad[B]): Monad[B]
}


class Foo {
	def bar: Option[Bar] = {
		???
	}
}

class Bar {
	def baz: Option[Baz] = {
		???
	}
}

class Baz {
	def compute: Int = {
		???
	}
}

/**
 * Loosely based on `Validation` in scalaz library
 */
sealed trait Validation[E,A] {
	/**
	 * Applies a 'regular' function to the contained value(s)
	 */
	def map[B](f: A => B): Validation[E, B]
	/**
	 * Applies a 'monadic' function to the contained value
	 */
	def flatMap[B](f: A => Validation[E, B]): Validation[E, B]
	
	def liftFail[F](f: E => F): Validation[F, A]
}

case class Success[E,A](a: A) extends Validation[E,A] {
	def map[B](f: A => B): Validation[E, B] = new Success(f(a))
	def flatMap[B](f: A => Validation[E, B]): Validation[E, B] = f(a)
	def liftFail[F](f: E => F): Validation[F, A] = new Success(a)
}


case class Failure[E,A](e: E) extends Validation[E,A] {
	def map[B](f: A => B): Validation[E, B] = new Failure(e)
	def flatMap[B](f: A => Validation[E, B]): Validation[E, B] = new Failure(e)
	def liftFail[F](f: E => F): Validation[F, A] = new Failure(f(e))
}



object MonadWorksheet extends App {
	def computeWithOptionMonad(maybeFoo: Option[Foo]): Option[Int] = {
		maybeFoo.flatMap{ foo => 
			foo.bar.flatMap { bar => 
				bar.baz.map { baz =>
					baz.compute
				}
			}
		}
	}
}
