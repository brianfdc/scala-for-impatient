package scala.impatient

import org.scalatest.FlatSpec
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable.Stack
import org.junit.runner.RunWith
import scala.impatient.Exercise08._

@RunWith(classOf[JUnitRunner])
class Exercises08Spec extends scala.awong.AbstractFlatSpec {
  var checking: CheckingAccount = _
  var bank: BankAccount = _
  var savings: SavingsAccount = _
  
  before {
    bank = new BankAccount(100.00)
    checking = new CheckingAccount(200.00)
    savings = new SavingsAccount(300.00)
  }
  
  "bank account" should "transact" in {
    val oldBalance = bank.deposit(0);
    val str = bank.toString()
    
    expect (100.00) {
      oldBalance
    }
    
    expect(200.00, "deposit") {
      bank.deposit(100)
    }
    
    expect(100.00,"withdraw") {
      bank.withdraw(100)
    }
  }

}