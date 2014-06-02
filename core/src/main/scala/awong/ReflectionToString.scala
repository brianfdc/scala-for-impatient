package awong

import scala.reflect.NameTransformer

trait ReflectionToString {
  /**
   * Repeatedly run `f` until it returns None, and assemble results in a Stream.
   */
  private def unfold[A](a: A, f: A => Option[A]): Stream[A] = {
    Stream.cons(a, f(a).map(unfold(_, f)).getOrElse(Stream.empty))
  }
  
  private def get[T](f: java.lang.reflect.Field, a: AnyRef): T = {
    f.setAccessible(true)
    f.get(a).asInstanceOf[T]
  }
  /**
   * @return None if t is null, Some(t) otherwise.
   */
  private def optNull[T <: AnyRef](t: T): Option[T] = {
    if (t eq null) None else Some(t)
  }
  
  /**
   * @return a Stream starting with the class c and continuing with its superclasses.
   */
  private def classAndSuperClasses(c: Class[_]): Stream[Class[_]] = {
    unfold[Class[_]](c, (c) => optNull(c.getSuperclass))
  }
  
  override def toString: String = {
    val fields = classAndSuperClasses(this.getClass).flatMap(_.getDeclaredFields).filter(!_.isSynthetic)
    val str = fields.map((f) => NameTransformer.decode(f.getName) + "=" + get(f, this)).mkString(",")
    this.getClass().getCanonicalName() + "@[" + str + "]"
  }
}