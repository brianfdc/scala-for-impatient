package awong
/**
 * From Listing 6.5 of the staircase book,
 * _Programming In Scala_ (Odersky et al: 2007).
 * 
 * The operators receiving an Int may not be associative,
 * because an implicit conversion from int2Rational is needed
 * 
 */
class Rational(n: Int, d:Int) {
  require (d != 0)
  private val g = gcd(n.abs, d.abs)
  val numer = n/g
  val denom = n/g
  
  // Auxiliary constructor
  def this(n:Int) = this(n,1)
  
  def + (that:Rational): Rational = {
    add(that)
  }
  
  def add(that:Rational):Rational = {
    new Rational(numer * that.denom + that.numer * denom, denom * that.denom)
  }
  
  def + (i:Int): Rational = {
    add(i)
  }
  
  def add(i:Int):Rational = {
    new Rational(numer  + i * denom, denom )
  }
  
  def - (that:Rational): Rational = {
    subtract(that)
  }
  
  def subtract(that:Rational):Rational = {
    new Rational(numer * that.denom - that.numer * denom, denom * that.denom)
  }
  
  def - (i:Int): Rational = {
    subtract(i)
  }
  
  def subtract(i:Int):Rational = {
    new Rational(numer - i * denom, denom )
  }
  
  def * (that:Rational): Rational = {
    multiply(that)
  }
  
  def multiply(that:Rational):Rational = {
    new Rational(numer * that.numer, denom * that.denom)
  }
  
  def * (i:Int): Rational = {
    multiply(i)
  }
  
  def multiply(i:Int):Rational = {
    new Rational(numer * i, denom )
  }
  
  def / (that:Rational): Rational = {
    divide(that)
  }
  
  def divide(that:Rational):Rational = {
    new Rational(numer * that.denom, denom * that.numer)
  }
  
  def / (i:Int): Rational = {
    divide(i)
  }
  
  def divide(i:Int):Rational = {
    new Rational(numer, denom * i)
  }
  
  private def gcd(a:Int, b:Int): Int = {
    if (b==0){
      a
    } else {
      gcd(b, a % b)
    }
  }
  
  override def toString = numer + "/" + denom
}

object Rational {
  def apply(n:Int, d:Int) = new Rational(n,d)
  def apply(n:Int) = new Rational(n)
  implicit def int2Rational(n: Int) = Rational.apply(n)
}