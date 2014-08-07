package awong

import org.slf4j.LoggerFactory
import org.slf4j.Logger

trait LoggingLike {
	lazy val logger = LoggerFactory.getLogger(getClass)
	
	implicit def logging2Logger(anything: LoggingLike): Logger = anything.logger
}