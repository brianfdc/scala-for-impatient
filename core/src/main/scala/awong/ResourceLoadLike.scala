package awong

import java.io.{File => JFile}

import scala.io.Source
import scala.io.BufferedSource
import com.typesafe.config.ConfigFactory

trait ResourceLoadLike extends LoggingLike {
	/**
	 * Load resource as a stream which is located in the
	 * same package as the concrete koan spec
	 * 
	 * TODO : not quite the same as the full strength Spring
	 * version that searches the entire class path
	 */
	def getSource(resourceName: String):Option[BufferedSource] = {
		try {
			val klazz = getClass()
			var stream = klazz.getResourceAsStream(resourceName)
			val src = Source.fromInputStream(stream)
			if (!src.isEmpty) {
				Some(src)
			} else {
				None
			}
		} catch  {
			case th:Throwable => None
		}
	}
	
	
	/**
	 * Get a child of a file. For example,
	 * 
	 *   subFile(homeDir, "b", "c")
	 * 
	 * corresponds to ~/b/c
	 */
	private def subFile(file: JFile, children: String*): JFile = {
		children.foldLeft(file)((file, child) => new JFile(file, child))
	}
	
	private def directoriesOfOtherModules(parentDir: JFile, currentModule: JFile): List[JFile] = {
		import collection.immutable.ListSet
		val uris = ListSet("core", "akka")
		val files = uris map { new JFile(parentDir, _) }
		val otherModules = files - currentModule
		otherModules.toList
	}
	
	private def getResourceFiles(moduleDir: JFile, resourcePath: List[String]): List[JFile] = {
		def getResourceFileImpl(moduleDir: JFile, dirs: List[String], resourcePath: List[String]): JFile = {
			subFile(moduleDir, (dirs ::: resourcePath): _*)
		}
		val resources = List("src"::"main"::"resources"::Nil, "src"::"test"::"resources"::Nil)
		
		val resourceFiles = for (r <- resources) yield getResourceFileImpl(moduleDir, r, resourcePath)
		resourceFiles
	}
	
	
	/**
	 * Get a resource from the `src/main/resources` directory. Eclipse does not copy
	 * resources to the output directory, then the class loader cannot find them.
	 */
	private def resourceAsFileFromSrc(resourcePath: List[String]): Option[JFile] = {
		val uri = getClass.getResource(".").toURI
		logger.debug("Accessing uri {}.", uri)
		val classesDir = new JFile(uri)
		val moduleDir = classesDir.getParentFile.getParentFile.getParentFile.getParentFile.getParentFile
		logger.debug("Accessing moduleDir {}", moduleDir.toString)
		val parentDir = moduleDir.getParentFile
		val resourceFiles = getResourceFiles(moduleDir, resourcePath)
		resourceFiles.find( _.exists ) match {
			case Some(f) => Some(f)
			case None =>
				val otherModules = directoriesOfOtherModules(parentDir, moduleDir)
				val listOfOtherResouceFiles = for (m <- otherModules) yield getResourceFiles(m, resourcePath)
				val allFiles = for (resourceFiles <- listOfOtherResouceFiles; file <- resourceFiles if file.exists) yield file
				allFiles.headOption
		}
	}

	def resourceAsStreamFromSrc(resourcePath: List[String]): Option[Source] = {
		resourceAsFileFromSrc(resourcePath) match {
			case Some(file) =>
				Some(Source.fromFile(file, Defaults.defaultEncoding))
			case None =>
				None
		}
	}
	
	def readInts(resourcePath: List[String]): Stream[Int] = {
		readStrings(resourcePath) map { _.toInt }
	}
	
	def readDoubles(resourcePath: List[String]): Stream[Double] = {
		readStrings(resourcePath) map { _.toDouble }
	}
	
	def readStrings(resourcePath: List[String]): Stream[String] = {
		val maybeStream = for (src <- resourceAsStreamFromSrc(resourcePath)) yield src.getLines.toStream
		maybeStream match {
			case Some(stream) => stream
			case None => Stream[String]()
		}
	}
	
	def resourceAsString(resourcePath: List[String]): Option[String] = {
		resourceAsStreamFromSrc(resourcePath) match {
			case Some(source) =>
				Some(source.mkString)
			case None =>
				None
		}
	}
}