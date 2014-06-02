package scala.awong

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import net.liftweb.json._
import net.liftweb.json.Serialization.{read,write}


@RunWith(classOf[JUnitRunner])
class LiftJsonSpec extends AbstractFlatSpec {
  /**
   * For fully reified type info seraialization
   * See http://www.scalafied.com/105/default-and-customized-lift-json-type-hints
   */
  "Lift" should "produce JSON" in {
    implicit val formats = Serialization.formats( NoTypeHints )
    case class Somebody(id: Long, name: String, isin: Option[String], from: java.util.Date, active: Boolean)
    
    val xf = write(List(
        Somebody(1, "alan", Some("where"), new java.util.Date(), false),
        Somebody(2, "alan", None, new java.util.Date(), true),
        Somebody(2, "alan", None, new java.util.Date(), false)
        ))
    println(xf)
  }


}