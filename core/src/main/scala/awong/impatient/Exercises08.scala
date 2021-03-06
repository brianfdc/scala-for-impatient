package awong.impatient

import awong.ReflectionToString

/**
 * Inheritance
 */
package Exercise08 {
  class BankAccount(initialBalance: Double) extends ReflectionToString {
    protected var balance = initialBalance
    
    def deposit(amount: Double): Double = {
      //transaction(amount)
      balance = balance + amount
      balance
    }
    def withdraw(amount: Double): Double = {
      // transaction(-1 * amount)
      balance = balance - amount
      balance
    }
    protected def transaction(amount:Double) : Double = {
      if (amount > 0) {
        balance = balance + amount
      } else {
        balance = balance - amount
      }
      balance
    } 
  }
  /**
   * (8.1)
   */
  class CheckingAccount(initialBalance: Double) extends BankAccount(initialBalance) {
    override def deposit(amount: Double): Double = {
      super.deposit(amount)
      transaction(-1.00)
    }
    override def withdraw(amount: Double): Double = {
      super.withdraw(amount)
      transaction(-1.00)
    }
  }
  /**
   * (8.2)
   */
  class SavingsAccount(initialBalance: Double) extends BankAccount(initialBalance) {
    private var numberOfMonthlyTransactions = 0;
    
    def shouldHaveServiceCharge : Boolean = {
      ( numberOfMonthlyTransactions > 3 )
    }
    override def deposit(amount: Double): Double = {
      super.deposit(amount)
      if (numberOfMonthlyTransactions > 3 ) {
        transaction(-1.00);
      }
      numberOfMonthlyTransactions = numberOfMonthlyTransactions + 1;
      ( balance )
    }
    override def withdraw(amount: Double): Double = {
      super.withdraw(amount);
      if(shouldHaveServiceCharge) {
        transaction(-1.00);
      }
      numberOfMonthlyTransactions = numberOfMonthlyTransactions + 1
      ( balance )
    }
    
    private def isBeginningOfMonth : Boolean = {
      val now = new java.util.Date()
      var cal = java.util.Calendar.getInstance()
      cal.setTime(now)
      val dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH)
      (dayOfMonth == 1)
    }
    
    private def earnMonthlyInterest(): Double = {
      val rate = 1.05
      val interest = rate * balance
      if (isBeginningOfMonth) {
        numberOfMonthlyTransactions = 0
      }
      super.deposit(interest)
    }
  }
  /**
   * (8.3)
   */
  abstract class Item {
    def price: Double
    def description: String
  }
  
  class SimpleItem(val price: Double, val description: String) extends Item {
    override def toString : String = {
      ("description: " + description + ", price: " + price)
    }
  }
  class Bundle(aDescription : String) extends Item {
    val description = aDescription;
    var items = List[SimpleItem]()
    def price : Double = {
      val prices = items.map(_.price)
      var total = prices.reduceLeft(_ + _)
      total
    }
    def add(item:SimpleItem) = {
      items = item :: items
    }
  }
  /**
   * (8.5)
   */
  class Point(val x: Int, val y: Int) {
    override def toString : String = {
      ("x: " + x + ", y: " + y)
    }
  }
  class LabeledPoint(x: Int, y: Int, val label: String) extends Point(x,y) {
    override def toString : String = {
      val str = super.toString();
      (str + ", label: " + label)
    }
  }
  /**
   * (8.8)
   * 
   * scala> javap -p Person
   * Compiled from "<console>"
   * public class Person extends java.lang.Object implements scala.ScalaObject{
   *     private final java.lang.String name;
   *     public java.lang.String name();
   *     public java.lang.String toString();
   *     public Person(java.lang.String);
   * }
   * 
   * scala> javap -p SecretAgent
   * Compiled from "<console>"
   * public class SecretAgent extends Person implements scala.ScalaObject{
   *     private final java.lang.String name;
   *     private final java.lang.String toString;
   *     public java.lang.String name();
   *     public java.lang.String toString();
   *     public SecretAgent(java.lang.String);
   * }
   */
  class Person(val name: String) {
    override def toString : String = {
      (getClass.getName + "[name: " + name + "]")
    }
  }
  class SecretAgent(codename: String) extends Person(codename) {
    override val name = "*******"    // don't reveal name
    override val toString = "******" // don't reveal class name
  }
}
