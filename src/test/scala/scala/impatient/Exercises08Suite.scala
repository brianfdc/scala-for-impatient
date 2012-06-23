package scala.impatient

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable.Stack
import org.junit.runner.RunWith
import scala.impatient.Exercise08._

@RunWith(classOf[JUnitRunner])
class Exercises08Suite extends FunSuite with BeforeAndAfter {
  var checking: CheckingAccount = _
  var bank: BankAccount = _
  var savings: SavingsAccount = _
  
  before {
    bank = new BankAccount(100.00)
    checking = new CheckingAccount(200.00)
    savings = new SavingsAccount(300.00)
  }
  
  test("bank account") {
    val oldBalance = bank.deposit(0);
    val str = bank.toString()
    
    assert(oldBalance === 100.00)
    val newBalance = bank.deposit(100);
    assert(newBalance === 200.00)
    val finalBalance = bank.withdraw(100);
    assert(finalBalance === 100.00)
  }

}