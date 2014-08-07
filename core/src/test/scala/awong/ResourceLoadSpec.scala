package awong

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class ResourceLoadSpec extends AbstractWordSpec {
	import ResourceLoadSpec._
	
	
	"A resource loader" should {
		"work for simple files" in {
			val loader = new TestResourceLoader()
			val src = loader.resourceAsStreamFromSrc("logback.xml")
			src should not be (None)
			val fileString = src.get.mkString
			fileString should startWith ("<configuration>")
		}
	}
}


object ResourceLoadSpec {
	class TestResourceLoader extends ResourceLoadLike
}