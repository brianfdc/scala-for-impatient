package scala.awong

/**
 * Here, object <code>HolaMundo</code> inherits the main method of <code>App</code>.
 * 
 * {@code args} returns the current command line argument
 * 
 */
object HolaMundo extends App {
  println("Hello World: " + args.mkString(", ") )
  
  def factorial(x: BigInt): BigInt = {
    if (x == 0) {
      1
    } else {
      x * factorial(x - 1)
    }
  }
  
  def haveSideEffect = {
    println("Hello, world")
  }
  
  def things : String = {
    val x = "Hello"
    var y = "H"
    y = x
    
    return x;
  }


  def mainOther(args: Array[String]) = {
    args.foreach(arg => println(arg))
  }
}