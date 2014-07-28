package awong.starbucks

import akka.actor._

import scala.concurrent.duration.Duration
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
object Store extends App {
	run

	def run: Unit = {
		val system = ActorSystem("StarbucksStore")
		
		val employeeInfo = EmployeeInfo(1, "Smith")
		val customerInfo = CustomerInfo(1, "Juily")
		val store = createStore(system, employeeInfo, customerInfo)
		
		
		var order = createOrder(1L, customerInfo)
		order = order + OrderItem(1L, 1, 2, Catalog.findByCode(1L).get)
		order = order + OrderItem(2L, 2, 1, Catalog.findByCode(2L).get)
		
		
		store ! PlaceOrder(order)
	}

	
	def createOrder(orderCode: Long, customer: CustomerInfo): Order = {
		val order = new Order(orderCode, customer)
		order
	}
	
	
	def createStore(system: ActorSystem, employeeInfo: EmployeeInfo, customerInfo: CustomerInfo): ActorRef = {
		system.actorOf(Props(new Store(employeeInfo, customerInfo)),
				name = "starbucks-store")
	}
}

/**
 * Below are actor classes and their messages
 */
class Store(employeeInfo: EmployeeInfo, customerInfo: CustomerInfo) extends Actor with ActorLogging {
	lazy val customer: ActorRef = createCustomer(customerInfo)
	lazy val barista : ActorRef = createBarista(employeeInfo)
	
	import collection.mutable.PriorityQueue
	
	// this ought to be assigned to supervisor actor which has singleton scope
	var unassignedOrderQueue = PriorityQueue[Order]()(Order.orderingByDateTime)
	  
	def common: Receive = {
		case _ =>
			log.error("unhandleable message for Store actor")
	}
	def receive : Receive = {
		case m: CustomerMessage =>
			customer ! m
		case m: BaristaMessage =>
			barista ! m
		case m : StoreMessage =>
			handleStoreMessage
		case _ =>
			common
	}
	
	def handleStoreMessage: Receive = {
		case EnqueueOrder(order) =>
			log.info("store has an unassigned order {}", order.code)
			order.status = OrderStatus.Unassigned
			unassignedOrderQueue.enqueue(order)
		case RequestOrder() =>
			if (!unassignedOrderQueue.isEmpty) {
				val orderToProcess = unassignedOrderQueue.dequeue
				barista ! AssignedOrder(orderToProcess)
				customer ! ReportOrder(orderToProcess)
			}
		case _ =>
			common
		
	}
	
	def createCustomer(info: CustomerInfo): ActorRef = {
		context.system.actorOf(Props(new CustomerActor(info, self)),
				name = "customer-" + info.code + "-" + info.name)
	}
	def createBarista(info: EmployeeInfo): ActorRef = {
		context.system.actorOf(Props(new BaristaActor(info, self)),
				name = "customer-" + info.code + "-" + info.name)
	}
  
}

class CustomerActor(customer: CustomerInfo, store: ActorRef) extends Actor with ActorLogging {
	def common: Receive = {
		case GetStatus =>
			sender ! ReportStatus(customer.customerStatus)
		case ReportOrder(order) =>
			log.info("customer {} knows that order {} is in {}", customer.name, order.code, order.status)
		case _ =>
			log.error("unhandleable message for {} in {} state", customer.name, customer.customerStatus.toString())
	}
	
	def receive: Receive = {
		created
	}
	
	
	def created: Receive = {
		case PlaceOrder(order) =>
			log.info("customer {} placing order {}", customer.name, order.code)
			store ! EnqueueOrder(order)
			become(CustomerStatus.OrderPlaced)
		case _ =>
			common
	}
	
	def orderPlaced: Receive = {
		case UpdateOrder(order) =>
			log.info("customer updating order")
			become(CustomerStatus.OrderUpdated)
		case PayOrder(order) =>
			log.info("customer paying order")
			become(CustomerStatus.OrderPaid)
		case _ =>
			common
	}
	
	def orderUpdated: Receive = {
		case UpdateOrderRejected(order) =>
			log.info("customer had updated order rejected")
			become(CustomerStatus.OrderPlaced)
		case UpdateOrderAccepted(order) =>
			log.info("customer had udpated order accepted")
			become(CustomerStatus.OrderPlaced)
		case _ =>
			common
	}
	
	def orderPaid: Receive = {
		case PickupOrder(order) =>
			log.info("customer picking up order")
			become(CustomerStatus.OrderReceived)
		case _ =>
			common
	}
	
	def orderReceived: Receive = {
		case _ =>
			common
	}
	
	/**
	 * Convenience pattern match to change state and behavior
	 */
	private def become(customerStatus: CustomerStatus) = {
		customerStatus match {
			case customerStatus @ CustomerStatus.Created =>
				customer.customerStatus = customerStatus
				context.become(created)
			case customerStatus @ CustomerStatus.OrderPlaced =>
				customer.customerStatus = customerStatus
				context.become(orderPlaced)
			case customerStatus @ CustomerStatus.OrderUpdated =>
				customer.customerStatus = customerStatus
				context.become(orderUpdated)
			case customerStatus @ CustomerStatus.OrderPaid =>
				customer.customerStatus = customerStatus
				context.become(orderPaid)
			case customerStatus @ CustomerStatus.OrderReceived =>
				customer.customerStatus = customerStatus
				context.become(orderReceived)
		}
	}
}

trait Status

sealed trait CustomerStatus extends CustomerStatus.Value with Status
object CustomerStatus extends EnumerationModel[CustomerStatus] {
	case object Created extends CustomerStatus
	case object OrderPlaced extends CustomerStatus
	case object OrderUpdated extends CustomerStatus
	case object OrderPaid extends CustomerStatus
	case object OrderReceived extends CustomerStatus
}


trait Message
case class GetStatus() extends Message
case class ReportStatus(status: Status) extends Message
case class ReportOrder(order: Order) extends Message

sealed trait CustomerMessage extends Message
case class PlaceOrder(order: Order) extends CustomerMessage
case class UpdateOrder(order: Order) extends CustomerMessage
case class UpdateOrderRejected(order: Order) extends CustomerMessage
case class UpdateOrderAccepted(order: Order) extends CustomerMessage
case class PayOrder(order: Order) extends CustomerMessage
case class PickupOrder(order: Order) extends CustomerMessage


sealed trait BaristaMessage extends Message
case class LookupNextOrder(order: Order) extends BaristaMessage
case class MakeOrder(order: Order) extends BaristaMessage
case class TakePayment(order: Order) extends BaristaMessage
case class AssignedOrder(order: Order) extends BaristaMessage

sealed trait StoreMessage extends Message
case class EnqueueOrder(order: Order) extends StoreMessage
case class RequestOrder() extends StoreMessage


class BaristaActor(employee: EmployeeInfo, store: ActorRef) extends Actor with ActorLogging {
	  
	def common: Receive = {
		case GetStatus =>
			sender ! ReportStatus(employee.baristaStatus)
		case _ =>
			log.error("unhandleable message for {} in {} state", employee.name, employee.baristaStatus.toString())
	}

	def receive: Receive = {
		awaitingOrder
	}
	
	def awaitingOrder: Receive = {
		case AssignedOrder(order) =>
			log.info("barista {} assigned order {}", employee.name, order.code)
			order.status = OrderStatus.Assigned
			become(BaristaStatus.OrderChosen)
			self ! MakeOrder(order)
		case LookupNextOrder(order) =>
			log.info("barista {} looking up next order", employee.name)
			store ! RequestOrder
		case _ =>
			common
	}
	
	def orderChosen: Receive = {
		case MakeOrder(order) =>
			log.info("barista making order")
			become(BaristaStatus.OrderMade)
		case _ =>
			common
	
	}
	
	def orderMade: Receive = {
		case TakePayment(order) =>
			log.info("barista taking payment")
			become(BaristaStatus.AwaitingOrder)
		case _ =>
			common
	}
	
	/**
	 * Convenience pattern match to change state and behavior
	 */
	private def become(baristaStatus: BaristaStatus) = {
		baristaStatus match {
			case baristaStatus @ BaristaStatus.OrderChosen =>
				employee.baristaStatus = baristaStatus
				context.become(orderChosen)
			case baristaStatus @ BaristaStatus.OrderMade =>
				employee.baristaStatus = baristaStatus
				context.become(orderMade)
			case baristaStatus @ BaristaStatus.AwaitingOrder =>
				employee.baristaStatus = baristaStatus
				context.become(awaitingOrder)
		}
	}
}

sealed trait BaristaStatus extends BaristaStatus.Value with Status
object BaristaStatus extends EnumerationModel[BaristaStatus] {
	case object AwaitingOrder extends BaristaStatus
	case object OrderChosen extends BaristaStatus
	case object OrderMade extends BaristaStatus
}

/**
 * Below are domain classes
 */
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
case class CustomerInfo(code: Long, name: String, var customerStatus: CustomerStatus = CustomerStatus.Created) extends Nameable
case class EmployeeInfo(code: Long, name: String, var baristaStatus: BaristaStatus = BaristaStatus.AwaitingOrder) extends Nameable

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



sealed trait OrderStatus extends OrderStatus.Value with Status
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



