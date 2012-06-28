package scala.awong

/**
 * Here, object <code>HolaMundo</code> inherits the main method of <code>App</code>.
 * 
 * {@code args} returns the current command line argument
 * 
 */
object HolaMundo extends App {
  println("Hello World: " + args.mkString(", ") )
}