package awong
import scala.util.Random

/**
 * Fun with collections using functional
 * programming
 */
object FunctionalCollections {

  def shuffle[T](xs:List[T]): List[T] = {
    Random.shuffle(xs)
  }
  
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
}