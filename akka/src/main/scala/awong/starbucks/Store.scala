package awong.starbucks

import akka.actor._
import scala.concurrent.duration.Duration
import scala.util.{Try, Success, Failure}
import scala.concurrent.Future
import java.util.concurrent.TimeUnit
import org.joda.time.DateTime
import akka.event.Logging

/**
 * This package models Gregor Hohpe's Starbucks example of order fulfillment
 * using Akka. For now, it assumes a very naive 1-to-1 relationship between
 * a customer and an employee. So there is no mature version of actor supervision, 
 * nor is their an actors-all-the-way down approach where even domain
 * entities like orders themselves are actors.
 * 
 * @see http://www.infoq.com/articles/webber-rest-workflow
 * @see http://www.enterpriseintegrationpatterns.com/ramblings/18_starbucks.html
 */
object StoreClient extends App {
	
	run

	def run: Unit = {
		val system = ActorSystem("StarbucksStore")
		
		val employeeInfo = EmployeeInfo(1, "Smith")
		val customerInfo = CustomerInfo(1, "Juily")
	}
}

trait State
trait Data

trait Message
case class InitializeCustomer(val order: Order, val barista: ActorRef) extends Message
case class UpdateOrder(val updateOrder: Order, val barista: ActorRef) extends Message
case class RejectUpdatedOrder(val barista: ActorRef) extends Message
case class AcceptUpdatedOrder(val barista: ActorRef) extends Message
case class PayOrder(val barista: ActorRef) extends Message
case class PickupOrder(val barista: ActorRef) extends Message

sealed trait CustomerStatus extends State
case object CustomerCreated extends CustomerStatus
case object OrderPlaced extends CustomerStatus
case object OrderUpdated extends CustomerStatus
case object OrderPaid extends CustomerStatus
case object OrderReceived extends CustomerStatus


sealed trait CustomerData extends Data
case object CustomerUninitialized extends CustomerData
case class OrderPlaced(val placedOrder: Order) extends CustomerData
case class OrderUpdated(val updatedOrder: Order, val originalOrder: Order) extends CustomerData
case class OrderPaid(val paidOrder: Order) extends CustomerData
case class OrderReceived(val receivedOrder: Order) extends CustomerData

class Customer(customer: CustomerInfo, store: ActorRef) extends BaseActor with FSM[CustomerStatus, CustomerData] {

	startWith(CustomerCreated, CustomerUninitialized)
	initialize()
	
	when(CustomerCreated) {
		case Event(InitializeCustomer(order, barista), CustomerUninitialized) =>
			goto(OrderPlaced) using OrderPlaced(order)
	}
	
	when(OrderPlaced) {
		case Event(UpdateOrder(updatedOrder, barista), OrderPlaced(placedOrder)) =>
			goto(OrderUpdated) using OrderUpdated(updatedOrder, placedOrder)
		case Event(PayOrder(barista), OrderPlaced(placedOrder)) =>
			goto(OrderPaid) using OrderPaid(placedOrder)
	}
	
	when(OrderUpdated) {
		case Event(RejectUpdatedOrder(barista), OrderUpdated(updatedOrder, originalOrder)) =>
			goto(OrderPlaced) using OrderPlaced(originalOrder)
		case Event(AcceptUpdatedOrder(barista), OrderUpdated(updatedOrder, originalOrder)) =>
			goto(OrderPlaced) using OrderPlaced(updatedOrder)
	}
	
	when(OrderPaid) {
		case Event(PickupOrder(barista), OrderPaid(paidOrder)) =>
			goto(OrderReceived) using OrderReceived(paidOrder)
	}
	
	onTransition {
		case CustomerCreated -> OrderPlaced =>
			log.debug("transitioning from {} to {}", CustomerCreated, OrderPlaced)
		case OrderPlaced -> OrderUpdated =>
			log.debug("transitioning from {} to {}", OrderPlaced, OrderUpdated)
		case OrderUpdated -> OrderPlaced =>
			log.debug("transitioning from {} to {}", OrderUpdated, OrderPlaced)
		case OrderPlaced -> OrderPaid =>
			log.debug("transitioning from {} to {}", OrderPlaced, OrderPaid)
		case OrderPaid -> OrderReceived =>
			log.debug("transitioning from {} to {}", OrderPaid, OrderReceived)
	}
	
	// common code for all states
	whenUnhandled {
		case Event(e,s) =>
			log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
			stay
	}
	
}



case class LookupNextOrder(customer: ActorRef) extends Message
case class MakeOrder(customer: ActorRef) extends Message
case class TakePayment(customer: ActorRef) extends Message

case class PushOrder(order: Order) extends Message

sealed trait BaristaStatus extends State
case object BaristaCreated extends BaristaStatus
case object OrderChosen extends BaristaStatus
case object OrderMade extends BaristaStatus
case object OrderReleased extends BaristaStatus

sealed trait BaristaData extends Data
case object BaristaUninitialized extends BaristaData
case class OrderChosen(val orderChosen: Order) extends BaristaData
case class OrderMade(val orderPrepared: Order) extends BaristaData
case class OrderReleased(val orderReleased: Order) extends BaristaData

import scala.collection.mutable.PriorityQueue

class Barista(employee: EmployeeInfo, orderQueue: PriorityQueue[Order]) extends BaseActor with FSM[BaristaStatus, BaristaData] {
	import context.dispatcher
	
	startWith(BaristaCreated, BaristaUninitialized)
	initialize()
	
	when(BaristaCreated) {
		case Event(LookupNextOrder(customer), BaristaUninitialized) =>
			goto(OrderChosen) using OrderChosen(chooseOrder())
	}
	
	when(OrderChosen) {
		case Event(MakeOrder(customer), OrderChosen(order)) =>
			goto(OrderMade) using OrderMade(order)
	}
	when(OrderMade) {
		case Event(TakePayment(customer), OrderMade(order)) =>
			goto(OrderReleased) using OrderReleased(order)
	}
	when(OrderReleased) {
		case Event(LookupNextOrder(customer), OrderReleased(orderJustReleased)) =>
			goto(OrderChosen) using OrderChosen(chooseOrder())
	}
	
//	onTransition {
//		
//	}
	whenUnhandled {
		case Event(PushOrder(order), _) =>
			orderQueue.enqueue(order)
			stay
		case Event(e,s) =>
			log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
			stay
	}
	
	
	/*
	 * this is incorrect ... in real life, use a routing strategy from
	 * a risk delegator/supervisor
	 * 
	 * @see http://doc.akka.io/docs/akka/2.3.4/scala/routing.html
	 */
	private def chooseOrder(): Order = {
		orderQueue.dequeue
	}
}


trait BaseActor extends akka.actor.Actor with ActorLogging



object Catalog {
	lazy val products = Set(Product(1, "drip coffee", 2.00), Product(2, "hot mocha", 4.01))
	
	def findByCode(code: Long): Option[Product] = products.find( _.code == code)
}

trait Codeable {
	def code: Long
}

trait Nameable extends Codeable {
	def name: String
}

case class Product(code: Long, name: String, unitPrice: Double) extends Nameable
case class CustomerInfo(code: Long, name: String) extends Nameable
case class EmployeeInfo(code: Long, name: String) extends Nameable

case class Order(code: Long,
		customer: CustomerInfo,
		dateTime : DateTime = new DateTime(),
		items: Seq[OrderItem] = Seq[OrderItem](),
		var status: OrderStatus = OrderStatus.Created) extends Codeable
{
	def addItem(orderItem: OrderItem): Order = {
		val newItems = orderItem +: items
		Order(code, customer, dateTime, newItems, status)
	}
	def +(orderItem: OrderItem) = addItem(orderItem)
	
}

object Order {
	implicit val defaultOrdering: Ordering[Order] = Ordering.by(o => o.code)
	
	val orderingByDateTime: Ordering[Order] = Ordering.by(o => o.dateTime.getMillis)
}

case class OrderItem(code: Long,
		entryNumber: Int,
		quantity: Int,
		product: Product) extends Codeable



sealed trait OrderStatus extends OrderStatus.Value with State
object OrderStatus extends EnumerationModel[OrderStatus] {
	case object Created extends OrderStatus
	case object Unassigned extends OrderStatus
	case object Assigned extends OrderStatus
	case object Paid extends OrderStatus
	case object Completed extends OrderStatus
	case object Shipped extends OrderStatus
	case object Failed extends OrderStatus
	case object Cancelled extends OrderStatus
}



