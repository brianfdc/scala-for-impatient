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
    oldBalance should be (100.00)
    bank.deposit(100) should be (200.00)
    bank.withdraw(100) should be (100.00)
  }

}