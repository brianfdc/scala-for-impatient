package scala.impatient
import java.beans.PropertyChangeListener

import scala.awong.ReflectionToString

/**
 * Traits
 */
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
    def log(msg: String):String = {
      def ceasarCipher(msg: String): String = {
        msg.map( x => (x + key).toChar )
      }
      val encrypted = ceasarCipher(msg)
      println(encrypted)
      ( encrypted )
    }
  }
  
  /**
   * (10.5)
   */
  trait PropertyChangeSupportLike extends ReflectionToString with Serializable {
    private var listeners = Seq[PropertyChangeListener]()
    private var propertyListeners = Map[String,Seq[PropertyChangeListener]]()
    val source:Object
    
    def addPropertyChangeListener(listener: PropertyChangeListener) = {
      listeners = listeners :+ listener
    }
    def removePropertyChangeListener(listener: PropertyChangeListener) = {
      listeners = listeners.filter( _ != listener)
    }
    
    def getPropertyChangeListeners: Seq[PropertyChangeListener] = {
      val result = Seq[PropertyChangeListener]()
      ( listeners ++ propertyListeners.values.flatMap( (x) => x ++ result) )
    }
    
    def addPropertyChangeListener(property:String, listener: PropertyChangeListener) = {
      val triggers = getPropertyChangeListeners(property) :+ listener
      propertyListeners = ( propertyListeners + (property ->  triggers ) )
    }
    def removePropertyChangeListener(property:String, listener: PropertyChangeListener) = {
      val triggers = getPropertyChangeListeners(property).filter( _ != listener)
      propertyListeners = ( propertyListeners + (property ->  triggers ) )
      
    }
    def getPropertyChangeListeners(property:String): Seq[PropertyChangeListener] = {
      val result = propertyListeners(property)
      (result)
    }
    def firePropertyChangeListeners[A](property:String, oldValue: A, newValue: A) = {
      val triggers = getPropertyChangeListeners(property) ++ listeners
      triggers.foreach{
        _.propertyChange(new java.beans.PropertyChangeEvent(source, property, oldValue, newValue) )
      }
    }
    def hasListeners: Boolean = {
      ( !listeners.isEmpty || !propertyListeners.isEmpty)
    }
    
  }
  
  object PointFactory {
    def get(x: Int, y:Int, src: java.lang.Object): java.awt.Point = {
      val point = new { val source = src } with java.awt.Point(x,y) with PropertyChangeSupportLike
      (point)
    }
  }
  /**
   * (10.6) 
   * Java: java.awt.Container extends java.awt.Component
   *       javax.swing.JComponent extends java.awt.Container (unintuitive: violates "is a")
   *       javax.swing.JPanel extends javax.swing.JComponent
   * Preferable: java.awt.Container inherits from java.awt.Component
   *             javax.swing.JComponent inherits from java.awt.Component
   *             javax.swing.JButton inherits from javax.swing.JComponent
   *             javax.swing.JContainer inherits from java.awt.Container and javax.swing.JComponent
   *             javax.swing.JPanel extends javax.swing.JContainer
   * The preferable design is not possible in Java due to the absence of mixins. A working version
   * of multiple inheritance was needed. But it is possible in Scala!
   * 
   * Let Container, Component, JContainer & JComponent be traits. Component is the 
   * root trait of all 4. Container extends Component. JComponent extends Component. Then
   * since the Swing library is a rewrite of AWT, JContainer extends JComponent with Container.
   * Let JButton be a class that extends JComponent and JPanel be a class that extends JContainer.
   */
  
  /**
   * (10.8) Replace the decorator java.io.BufferedInputStream 
   */
  trait BufferedInputStreamLike {
    val in: java.io.InputStream
    def available: Int = {
      in.available()
    }
    def close = {
      in.close
    }
    def mark(readLimit: Int) {
      in.mark(readLimit)
    }
    def markSupported: Boolean = {
      in.markSupported
    }
    def read: Int = {
      in.read()
    }
    def read(b: Array[Byte], off: Int, len: Int) : Int = {
      in.read(b,off,len)
    }
    def reset = {
      in.reset
    }
    def skip(n: Long) : Long = {
      in.skip(n)
    }
  }
}