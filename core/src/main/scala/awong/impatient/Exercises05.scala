package awong.impatient

/**
 * Classes
 */
package Exercise05 {
  /**
   * (5.1)
   */
  import math.BigInt
  class Counter {
    private var value = BigInt(0)
    def increment() {
      value += 1
    }
    def current() = value
  }
  /**
   * (5.2)
   */
  import math.BigDecimal
  class BankAccount {
    private var _balance = BigDecimal(0.0)
    private val ZERO = BigDecimal(0.00)
    
    def withdraw(amount:BigDecimal) = {
      if (amount >= ZERO) {
        var b = balance - amount
      }
    }
    def deposit(amount:BigDecimal) = {
      if (amount >= ZERO) {
        var b = balance + amount
      }
    }
    def balance = _balance
    def balance_=(amount:BigDecimal) {
      // do nothing
    }
  }
  
  /**
   * (5.3/4)
   */
  class Time(val hour:Int,val min:Int) {
    if ( !(0 to 23).contains(hour) ) {
      throw new IllegalArgumentException
    }
    if ( !(0 to 60).contains(min) ) {
      throw new IllegalArgumentException
    }
    
    def before(other:Time):Boolean = {
      if (hour == other.hour) {
        return compare(min, other.min)
      } else {
        return compare(hour, other.hour)
      }
    }
    
    def beforeOther(other:Time):Boolean = {
      ( minutesSinceMidnight < other.minutesSinceMidnight )
    }
    
    def minutesSinceMidnight : Int = {
      val sinceMidnight = (hour * 60) + min
      sinceMidnight
    }
    
    private def compare(x:Int,y:Int) : Boolean = {
      var result = false
      if (x < y) {
        result = true
      } else if (x > y) {
        result = false
      } else {
        result = false
      }
      result
    }
  }
  /**
   * (5.5)
   */
  import scala.reflect.BeanProperty
  class Student(@BeanProperty var id: Long, @BeanProperty var name: String) {
    override def toString : String = {
      ("id: " + id + ", name: " + name)
    }
  }
  /**
   * (5.6)
   */
  class Person(var age:Int) {
    if (age < 0) {
      age = 0
    }
    
    def age_(anAge:Int) = {
      if (anAge > age) {
        age = anAge
      }
    }
    override def toString : String = {
      ("age: " + age)
    }
  }
  
  /**
   * (5.7)
   */
  class Car(val manufacturer: String, modelName : String, modelYear: Int, licensePlate: String) {
    override def toString : String = {
      var m = Map[String,Any]();
      m = m + ("mfg" -> manufacturer);
      m = m + ("modelName" -> modelName);
      m = m + ("modelYear" -> modelYear);
      m = m + ("licensePlate" -> licensePlate);
      val list = List[String]();
      val str = m.foldLeft(list){
        (list,entry) => ( entry._1 + ": " + entry._2 ) :: list
      }
      list.mkString(", ")
    }
    
  }
}