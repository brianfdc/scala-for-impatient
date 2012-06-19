package com.scala.impatient

package Exercise08 {
  class BankAccount(initialBalance: Double) {
    protected var balance = initialBalance
    
    def deposit(amount: Double): Double = {
      transaction(amount)
    }
    def withdraw(amount: Double): Double = {
      transaction(-1 * amount)
    }
    protected def transaction(amount:Double) : Double = {
      if (amount > 0) {
        balance += amount
      } else {
        balance -= amount
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
  class Bundle {
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
}
