package scala.awong

import scala.io.Source
import scala.io.BufferedSource

trait ResourceLoader {
  /**
   * Load resource as a stream which is located in the
   * same package as the concrete koan spec
   * 
   * TODO : not quite the same as the full strength Spring
   * version that searches the entire class path
   */
  def getSource(resourceName: String):Option[BufferedSource] = {
    try {
      var stream = getClass().getResourceAsStream(resourceName)
      val src = Source.fromInputStream(stream)
      if (!src.isEmpty) {
        Some(src)
      } else {
        None
      }
    } catch  {
      case ex:Exception => None
    }
  }

}