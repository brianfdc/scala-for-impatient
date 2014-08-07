package awong.impatient

import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import awong.impatient.Exercise08._
import awong._


@RunWith(classOf[JUnitRunner])
class Exercises08Spec extends AbstractFlatSpec {
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
    
    expectResult (100.00) {
      oldBalance
    }
    
    expectResult(200.00, "deposit") {
      bank.deposit(100)
    }
    
    expectResult(100.00,"withdraw") {
      bank.withdraw(100)
    }
  }

}