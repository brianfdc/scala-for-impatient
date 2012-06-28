package scala.awong

/**
 * Fun with collections using functional
 * programming
 */
class FunctionalCollections {

  def insertionSort[T <: Ordered[T]](xs:List[T]): List[T] = {
    def insert(x:T, xs:List[T]): List[T] = {
      xs match {
        case List() => List(x)
        case y::ys => {
          
          if (x <=  y) {
            x::xs
          } else {
            y:: insert(x,ys)
          }
        }
      }
    }
    xs match {
      case List() => List()
      case x::xs1 => insert(x,insertionSort(xs1)) 
    }
  }
  
  def mergeSort[T <: Ordered[T]](xs:List[T]): List[T] = {
    def merge(xs:List[T], ys:List[T]): List[T] = {
      (xs,ys) match {
        case (Nil,_) => ys
        case (_,Nil) => xs
        case (x::xs1, y::ys1) => {
          if (x < y) { 
            x::merge(xs1,ys) 
          } else {
            y::merge(xs,ys1)
          }
        }
      }
    }
    if (xs.isEmpty) {
      xs
    } else {
    val n = xs.length / 2
      val (ys,zs) = xs.splitAt(n)
      merge(mergeSort(ys), mergeSort(zs))
    }
  }
  
  def mapping = {
    val list = List(1,2,3)
    list.map( _ + 1) // (2,3,4)
    val words = List("the","quick", "brown", "fox")
    words.map(_.toList) // List(List(t,h,e), List(q,u,i,c,k),List(b,r,o,w,n),List(f,o,x))
    words.flatMap(_.toList) // List(t,h,e,q,u,i,c,k,b,r,o,w,n,f,o,x)
    var sum = 0
    
    list.foreach( sum += _) // 6
  }
  
  def filtering = {
    
  }
}