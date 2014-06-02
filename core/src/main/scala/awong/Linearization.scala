package awong

object Linearization extends Function1[Class[_], List[Class[_]]] {
  private def join[T](x : List[T], y : List[T]) : List[T] = x match {
    case Nil => y;
    case x::xs => 
      if (y.contains(x)) x :: join(xs, y)
      else join(xs, y);
  }
  def apply(clazz : Class[_]) : List[Class[_]] = {
    clazz ::  (clazz.getInterfaces.toList ++ List(clazz.getSuperclass)).map(this).reduceRight(join(_, _))
  }
}


