package scala.awong


class Hello {

  def main(args: Array[String]) = {
    println("hi")
    //var i = 0
    //while (i < args.length) {
    //  println(args[i])
    //  i++
    //}
  }
  
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