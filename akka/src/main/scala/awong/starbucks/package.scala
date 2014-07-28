package awong

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