package scala.impatient

/**
 * Advanced Types
 */
object Exercises18 {
  /**
   * (18.1)
   */
  class Bug {
    var position: Int = 0
    var direction: Boolean = true
    
    def move(amount: Int): this.type = {
      position = if (direction) { position + amount } else { position - amount }
      this
    }
    def turn(): this.type = {
      direction = !direction
      this
    }
    
    def show(): this.type = {
      println(position)
      this
    }
  }
  /**
   * (18.2)
   */
  sealed abstract class Action
  object show extends Action
  object then extends Action
  
  object around 
  
  class FluentBug {
    var position: Int = 0
    var direction: Boolean = true
    
    def move(amount: Int): this.type = {
      position = if (direction) { position + amount } else { position - amount }
      this
    }
    def and: this.type = {
      this
    }
    def turn: this.type = {
      direction = !direction
      this
    }
    
    def show: this.type = {
      print(position + " ")
      this
    }
  }
  
  /**
   * (18.3)
   */
  abstract class DocumentProperty
  object Title extends DocumentProperty
  object Author extends DocumentProperty
  
  class Document(var title: String, var author: String) {
    def this() = {
      this("","")
    }
    private var useNextArgAs: Any = null
    
    def set[T <: DocumentProperty](obj: T): this.type = {
      useNextArgAs = obj;
      this
    }
    def set(obj: Author.type): this.type = {
      useNextArgAs = obj;
      this
    }
    def to(arg: String): this.type = {
      if (useNextArgAs == Title) {
        title = arg;
      } else if (useNextArgAs == Author) {
        author = arg;
      }
      this
    }
  }
  /**
   * (18.4)
   */
  case class Network(val name: String) { outer =>
    import scala.collection.mutable.ArrayBuffer
    
    class Member(val name: String) {
      val contact = new ArrayBuffer[Member]
      
      def network: Network = {
        outer
      }
      def sameNetworkAs(other: Network#Member): Boolean = {
        outer == other.network
      }
      override def equals(that : Any): Boolean = {
        that match {
          case anyRef: AnyRef =>
            anyRef match {
              case anyNetworkMember: Network#Member =>
                if (sameNetworkAs(anyNetworkMember)) {
                  anyNetworkMember.name == this.name
                } else {
                  false
                }
              case _ => false
            }
          case _ => false
        }
      }
    }
    private val members = new ArrayBuffer[Member]()
    
    def join(name: String) = {
      val m = new Member(name)
      members += m
      m
    }
    
    /**
     * (18.6) 
     */
    type NetworkMember = n.Member forSome { val n: Network }
    def process(m1: NetworkMember, m2: NetworkMember) = {
      println("same functionally as processVerbosely")
      (m1, m2)
    }
    def processVerbosely[M <: n.Member forSome { val n: Network}](m1: M, m2: M) = {
      (m1,m2)
    }
  }
  
  /**
   * (18.6)
   */
  type &[A,B] = (A,B)
  def findClosests(sorted : Seq[Int], value: Int): Either[Int & Int,Int & Int] = {
    val indexed = sorted.zipWithIndex
    indexed.find( _._1 == value) match {
      case Some(x) => 
        Left(x._1,x._2)
      case None => {
        val closest = indexed.sortWith{ (a,b) =>
          ( (a._1-value).abs < (b._1-value).abs )
        }.head
        Right(closest._1,closest._2)
      }
    }
  }
  
  /**
   * (18.7)
   */
  def withClose[T <: { def close: Unit} ](target: T)( process: (T) => Any ) = {
    try {
      process(target)
    } catch {
      case iae: IllegalArgumentException =>
        "Illegal argument exception"
    } finally {
      target.close
    }
    
  }
  /**
   * (18.8)
   */
  def printValues(f: { def apply(x: Int) : Int}, from: Int, to:Int): List[Int] = {
    val range = if (to > from) { from to to } else {to to from }
    val results = range.map( f.apply(_) ).toList
    println(results.mkString(" "))
    results
  }
  /**
   * (18.9)
   */
  trait Dim[T] {
    this: T =>
    val value: Double
    val name: String
    protected def create(v: Double):T
    def +(other: Dim[T]) = create(value + other.value)
    override def toString = {
     "Dim(value: " + value + ",name: " + name + ")" 
    }
  }
  class Seconds(val value: Double) extends Dim[Seconds] {
    val name = "s"
    override def create(v: Double) = new Seconds(v)
  }
  class Meters(val value: Double) extends Dim[Meters] {
    val name = "m"
    override def create(v: Double) = new Meters(v)
  }
}