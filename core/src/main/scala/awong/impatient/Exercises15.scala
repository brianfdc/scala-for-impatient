package awong.impatient

import scala.annotation.varargs
/**
 * Annotations
 */
object Exercises15 {
  /**
   * (15.2)
   */
  @deprecated(message="This class is Deprecated", since="0.0.2")
  class Deprecated(var name: String) {
    @deprecated(message="description member bar is obsolete", since="0.0.2")
    var description = name;
    
    @deprecated(message="this method is obsolete", since="0.0.2")
    def whoAmI: String = {
      name
    }
    def print(@deprecated(message="something parameter is obsolete", since="0.0.2") something: String):String  = {
      val x = (name, description, something)
      x.toString
    }
    
    def sayHi(@deprecatedName('world) sender: String) {
      @deprecated(message="greetee local var is obsolete", since="0.0.2")
      var greetee = sender
      println("Hello " + sender)
    }
    /**
     * (15.4)
     */
    def sum(ints: java.util.List[java.lang.Integer]) : java.lang.Integer = {
      sum(ints)
    }
    // Exercises15JUnitTest can't invoke this w/o compile errors :(
    private def sum(@varargs ints: Int*): Int = {
      ints.toList.sum
    }
    /**
     * (15.5)
     */
     def linesOfFilename(filename: String): String = {
       Exercises04.readTokens(filename).mkString("\n")
     }
  }
}