package awong.adventure

import scala.util.{Try, Success, Failure}

import scala.concurrent.{Future, Promise, Await}
import scala.concurrent.ExecutionContext.Implicits.global

import akka.serialization._

trait Adventure {
	def collectCoins(): Try[List[Coin]]
	def buyTreasure(coins: List[Coin]): Try[Treasure]
}


class AdventureImpl extends Adventure {
	def eatenByMonster: Boolean = {
		true
	}
	
	
	def collectCoins(): Try[List[Coin]] = {
		if (eatenByMonster) {
			Try(List(Gold(), Gold(), Silver()))
		} else {
			Failure(new GameOverException)
		}
	}
	
	def buyTreasure(coins: List[Coin]): Try[Treasure] = {
		???
	}
}
trait Coin
case class Gold() extends Coin
case class Silver() extends Coin
case class Diamond() extends Coin

case class Treasure()

class GameOverException extends Exception


object Game {
	def run(): Unit = {
		val adventure: Adventure = new AdventureImpl()
		
		val treasure: Try[Treasure]  = for {
			coins <- adventure.collectCoins
			treasure <- adventure.buyTreasure(coins)
		} yield treasure
	}
}

