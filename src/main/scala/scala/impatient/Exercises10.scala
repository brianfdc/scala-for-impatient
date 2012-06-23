package scala.impatient

object Exercises10 {
  /**
   * (10.1)
   */
  trait IRectangle {
    def setFrame(x:Double, y:Double, w:Double, h:Double) 
    def getX:Double
    def getY:Double
    def getWidth:Double
    def getHeight:Double
  }
  trait RectangleLike extends IRectangle {
    def translate(x: Double, y: Double) = {
      setFrame(getX + x, getY + y, getWidth, getHeight)
    }
    
    def grow(w: Double, h: Double) = {
      setFrame(getX, getY, getWidth + w, getHeight + h)
    }
  }
  /**
   * (10.2)
   */
  trait OrderedPoint extends java.awt.Point with scala.math.Ordered[java.awt.Point] {
    override def compareTo(that:java.awt.Point): Int = {
      var result = 1
      if (this.getX == that.getX && this.getY == that.getY) {
        result = 0
      } else if (this.getX == that.getX && this.getY < that.getY) {
        result = -1
      } else if (this.getX == that.getX && this.getY > that.getY) {
        result = 1
      } else if (this.getX < that.getX){
        result = -1
      } else {
        // this.getX > that.getX
        result = 1
      }
      
      result
    }
  }
  
  /**
   * (10.3) BitSet class
   * 
   * trait BitSet extends Set[Int] with BitSetLike[BitSet] 
   * 
   * l(BitSet) = BitSet >> l(Set >> BitSetLike)
   *   = BitSet >> Set >> l( (Int) ⇒ Boolean >> Iterable >> GenSet >> GenericSetTemplate )
   *     >> BitSetLike >> l(SetLike)
   *     
   *   = BitSet >> l( (Int) ⇒ Boolean >> Iterable >> GenSet )
   *      >> GenericSetTemplate >> l(GenericTraversibleTemplate)
   *      >> BitSetLike >> SetLike >> l(IterableLike >> GenSetLike >> Subtractable >> Parallelizable )
   *      
   *   = BitSet >> (Int) ⇒ Boolean >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder)
   *      >> BitSetLike >> SetLike >> l(IterableLike >> GenSetLike >> Subtractable >> Parallelizable )
   *      
   *   = BitSet >> (Int) ⇒ Boolean >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder)
   *      >> BitSetLike >> SetLike >> l(IterableLike)
   *      >> GenSetLike >>  l(GenIterableLike >> (Int) => Boolean >> Equals)
   *      >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder)
   *      >> BitSetLike >> SetLike >> l(IterableLike)
   *      >> GenSetLike >> GenIterableLike >> l(GenTraversibleLike >> (Int) => Boolean >> Equals)
   *      >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder)
   *      >> BitSetLike >> SetLike >> l(IterableLike)
   *      >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> l(GenTraversibleOnce)
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder) >> BitSetLike >> SetLike
   *      >> IterableLike >> l(Equals >> TraversibleLike)
   *      >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable >> GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> l(HasNewBuilder) >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> l(HasNewBuilder >> FilterMonadic >> TraversibleOnce)
   *      >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable) >> l(GenSet)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> l(TraversibleOnce)
   *      >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable) >> GenSet >> l(GenSetLike >> GenIterable >> GenericSetTemplate)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> l(GenTraversibleOnce)
   *      >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable) >> GenSet >> l(GenSetLike) >> l(GenIterable)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> l(Iterable) >> GenSet
   *      >> GenSetLike >> l(GenIterableLike >> (Int) => Boolean >> Equals >> Parallelizable)
   *      >> GenIterable >> l(GenIterableLike >> GenTraversable >> GenericTraversibleTemplate)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> Iterable >> l(Traversable >> GenIterable >> GenericTraversibleTemplate >> IterableLike)
   *      >> GenSet >> GenSetLike 
   *      >> GenIterable >> l(GenIterableLike >> GenTraversable >> GenericTraversibleTemplate)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> Iterable >> l(Traversable)
   *      >> GenSet >> GenSetLike 
   *      >> GenIterable >> l(GenTraversable)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   * 
   *   = BitSet >> Iterable >> Traversable >> l(TraversableLike >> GenTraversable >> TraversableOnce >> GenericTraversibleTemplate)
   *      >> GenSet >> GenSetLike 
   *      >> GenIterable >> GenTraversable >> l(GenTraversableLike >> GenTraversibleOnce >> GenericTraversibleTemplate)
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversibleLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   *      
   *   = BitSet >> Iterable >> Traversable
   *      >> GenSet >> GenSetLike 
   *      >> GenIterable >> GenTraversable
   *      >> GenericSetTemplate >> GenericTraversibleTemplate >> BitSetLike >> SetLike
   *      >> IterableLike >> Equals >> TraversableLike >> HasNewBuilder >> FilterMonadic
   *      >> TraversibleOnce >> GenSetLike >> GenIterableLike >> GenTraversibleLike >> GenTraversibleOnce
   *      >> (Int) => Boolean >> Equals >> Subtractable >> Parallelizable >> AnyRef
   */
  
  /**
   * (10.4)
   */
  trait CryptoLogger {
    val key = 3
    def log(msg: String) {
      def ceasarCipher(msg: String): String = {
        msg.map( x => (x + key).toChar )
      }
      println(ceasarCipher(msg))
    }
  }
}