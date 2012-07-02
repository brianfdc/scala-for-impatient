package scala.impatient

/**
 * Operators
 */
object Exercises11 {
  /**
   * (11.1) According to the precedence rules (determined by the
   * 1st character of an operator
   * 
   * 3 + 4 -> 5 and 3 -> 4 + 5
   * 
   * Both are evaluated left to right, as '-' and '+' have the same precedence.
   * The latter is malformed as the range operator (->) does not expect an integer.
   */
  
  /**
   * (11.2) 5 ** 3 ** 2 
   * 5 ** (3 ** 2) ?= (5 ** 3) ** 2
   */
  def sample(bigInt:BigInt, n: Int) = {
    val power = bigInt.pow(n)
    println("""The power function on bigInt is not defined as an operator like in Pascal (^) or
        Fortran (**) since the type of exponent (Int) is not the same as the base.""")
  }
  
  /**
   * (11.3) For Fraction class, see scala.awong.Rational
   */
  
  /**
   * (11.4) Because of the Ordered[Money] trait, we get the comparison operators
   * for free when compareTo is overridden.
   * Don't recommend  
   */
  class Money(val d: BigInt, val c:BigInt) extends Ordered[Money] {
    private var ds = c /% 100;
    val cents = ds._2
    val dollars = d + ds._1
    
    def +(that:Money):Money = {
      new Money(0, this.totalCents + that.totalCents)
    }
    
    def -(that:Money):Money = {
      new Money(0, this.totalCents - that.totalCents)
    }
    
    def totalCents:BigInt = {
      (dollars * 100) + cents
    }
    
    def ==(that:Money):Boolean = {
      compareTo(that) == 0
    }
    
    def *(that:Int) = {
      new Money(0, this.totalCents * that)
    }
    def /(that:Int) = {
      new Money(0, this.totalCents / that)
    }
    
    override def compare(that: Money):Int = {
      totalCents.compare(that.totalCents)
    }
  }
  
  /**
   * (10.5)
   */
  class Table {
    val data = scala.collection.mutable.LinkedHashMap[Int,Seq[String]]()
    
    def ||(first:String):Table = {
      val row = Seq[String](first)
      data += ( (lastRowIndex + 1) -> row)
      this
    }
    private def lastRowIndex : Int = {
      data.keys.last
    }
    def |(next:String):Table = {
      if (data.isEmpty) {
        data += (0 -> Seq[String](next))
      } else {
        data(lastRowIndex) =  (data(lastRowIndex)) :+ next
      }
      this
    }
    
    def toHtml: String = {
      val trs = data.values.foldLeft(Seq[String]()){ (row,datum) => row :+ datum.map{"<td>" + _ + "</td>"}.mkString }
      val html = trs.map{"<tr>" + _ + "</tr>"}.mkString
      "<table>" + html + "</table>"
    }
  }
  /**
   * (10.7)
   */
  class BitSequence(val bits:Long) {
    private val one:Long = 1
    
    def apply(n:Long): Boolean = {
      // get the nth bit from bits
      (bits & (one << n)) != 0
    }
    
    def update(n:Int, value:Boolean): BitSequence = {
      // set the nth bit from bits using the value
      val mask = one << n
      if (value) {
        new BitSequence(bits | (mask))
      } else {
        new BitSequence(bits & ~(mask))
      }
    }
    def toggle(n:Int) = {
      // toggle the nth bit
      new BitSequence(bits ^ (1<<n) )
    }
    
    def isEven:Boolean = {
      (bits & one) == 0
    }
    
    private def multiplyByPowerOfTwo(n:Int): Long = {
      bits << n
    }
    private def divideByPowerOfTwo(n:Int): Long = {
      bits >> n
    }
    
    def isPositive:Boolean = {
      (bits >> 63) != 0
    }
    
    def negate:BitSequence = {
      new BitSequence(~bits + one)
    }
    
    def unsetRightMostOneBit:BitSequence = {
      new BitSequence(bits & (bits-one) )
    }
    def isPowerOfTwo:Boolean = {
      unsetRightMostOneBit.bits == 0
    }
    /*
    def swap(x:Long,y:Long) = {
      x = x ^ y
      y = x ^ y
      x = x ^ y
    }
    */
  }
  
  /**
   * (10.9/10)
   */
  class RichFile(val filepath:String, val name:String, val extension:String) {
    val file = new java.io.File(filepath, name + java.io.File.separator + extension)
  }
  object RichFile {
    def apply(filepath:String, name:String, extension:String): RichFile = {
      new RichFile(filepath, name, extension)
    }
    
    def unapply(input: RichFile):Tuple3[String,String,String] = {
      (input.filepath, input.name, input.extension)
    }
    def unapplySeq(input: RichFile):Option[Seq[String]] = {
      if ("" == input.filepath.trim) {
        None
      } else if ("" == input.name.trim) {
        None
      } else if ("" == input.extension.trim) {
        None
      } else {
        Some(Seq[String](input.filepath, input.name, input.extension))
      }
    }
  }

}