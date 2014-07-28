package awong

/**
 * This package models Gregor Hohpe's Starbucks example of order fulfillment
 * using Akka.
 * 
 * @see http://www.infoq.com/articles/webber-rest-workflow
 * @see http://www.enterpriseintegrationpatterns.com/ramblings/18_starbucks.html
 */
package object starbucks {

	trait EnumerationModel[A]  {
		self =>
		trait Value {
			self: A =>
			_values :+= this
		}
		private var _values = List.empty[A]
		def values = _values
	}

}