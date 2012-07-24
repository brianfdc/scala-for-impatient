package scala.impatient


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

import scala.impatient.Exercises18._

@RunWith(classOf[JUnitRunner])
class Exercises18Spec extends scala.awong.AbstractFlatSpec {

  "(18.1/2) A bug" should "move" in {
    val bugsy = new Bug();
    bugsy.move(4).show.move(6).show.turn().move(5).show
    
    val aBugsy = new FluentBug();
    
    //aBugsy move 4 and show and then move 6 and show turn around move 5 and show
  }
  "(18.3) a fluent interface" should "sing" in {
    val doc = new Document()
    doc.set(Title).to("Great Expectations").set(Author).to("Dickens")
    
  }
  
  "18.4" should "implement equals correctly" in {
    val twitter = Network("twitter")
    val messi = twitter.join("@LeoMessi")
    val fcb = twitter.join("@FCBarcelona")
    
    val facebook = Network("facebook")
    val rf = facebook.join("Roger Federer")
    val ms = facebook.join("Maria Sharapova")
    rf.sameNetworkAs(ms) should be (true)
    messi.sameNetworkAs(fcb) should be (true)
    
    rf.sameNetworkAs(fcb) should be (false)
    fcb.sameNetworkAs(rf) should be (false)
    
    rf == rf should be (true)
    messi == fcb should be (false)
    messi == rf should be (false)
  }
  "(18.6)" should "find closests" in {
    val list = List(2, 3, 5, 10, 11, 20, 78).toIndexedSeq
    
    var result = Exercises18.findClosests(list, 10)
    result.isLeft should be(true)
    result.left.get._2 should be (3)
    
    result = Exercises18.findClosests(list, 9)
    result.isRight should be(true)
    result.right.get._2 should be (3)
    
    result = Exercises18.findClosests(list, 90)
    result.isRight should be(true)
    result.right.get._2 should be (6)
    
    result = Exercises18.findClosests(list, 4)
    result.isRight should be(true)
    result.right.get._2 should be (1)
  }
  "18.7" should "do the loan pattern" in {
    class Loaner(x: Int) {
      var msg = List[String]()
      def close: Unit = {
        msg = "closing time" :: msg
      }
      def process = {
        if (x < 0) {
          msg = "msg fail" :: msg
          throw new IllegalArgumentException
        }
      }
    }
    var loaner = new Loaner(2)
    Exercises18.withClose(loaner) { (x) => x.process }
    loaner.msg.size should be (1)
    loaner = new Loaner(-2)
    Exercises18.withClose(loaner) { (x) => x.process }
    loaner.msg.size should be (2)
  }
  "(18.8)" should "run apply a lot" in {
    Exercises18.printValues((x:Int) => x * x, 3,6) should be(List(9,16,25,36))
    Exercises18.printValues( Array(1,1,2,3,5,8,13,21,34,55), 3,6) should be (List(3,5,8,13))
  }
}