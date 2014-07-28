package awong.starbucks

import akka.actor._

import scala.concurrent.duration.Duration
import java.util.concurrent.TimeUnit 
import org.joda.time.DateTime
import akka.event.Logging

object Store extends App {
	run

	def run: Unit = {
		val employees = Map(1 -> "Smith", 2 -> "Jones", 3 -> "Lee", 4 -> "Gates").map(entry  => Employee(entry._1, entry._2) ).toSeq
		val supervisor = employees.find( _.name == "Gates").getOrElse(Employee(5, "Bezos"))
		val workers = employees.filterNot( _.code == supervisor.code )
		
		val customers = Map(1 -> "Alan",
							2 -> "Mary",
							3 -> "John",
							4 -> "Audrey",
							5 -> "Nik",
							6 -> "Judy",
							7 -> "Jesse",
							8 -> "Dagmar",
							9 -> "Jerome",
							10 -> "Christian").map(entry  => Customer(entry._1, entry._2) ).toSeq
		val system = ActorSystem("StarbucksStore")
		
		val supervisorActor = system.actorOf(
				Props(new SupervisorActor(supervisor, workers)),
				name = "supervisor")
		val customerActors = createCustomerActors(system, supervisorActor, customers)
		supervisorActor ! OpenStore(Catalog.products)
	}

	
	def createCustomerActors(system: ActorSystem, supervisor: ActorRef, customers: Seq[Customer]): Map[Customer, ActorRef] = {
		import scala.collection.breakOut
		val customerActors = for (c <- customers) yield createCustomerActor(system, c, supervisor)
		val customerMap = (customers zip customerActors)(breakOut): Map[Customer, ActorRef]
		customerMap
	}
	
	def createCustomerActor(system: ActorSystem, customer: Customer, supervisor: ActorRef): ActorRef = {
		system.actorOf(Props(new CustomerActor(customer, supervisor)),
				name = "customer-" + customer.code + "-" + customer.name)
	}
}

/**
 * Below are actor classes and their messages
 */
class CustomerActor(customer: Customer, supervisor: ActorRef) extends Actor with ActorLogging {
	def receive: Receive = {
		case PlaceOrder(order) =>
			log.info("customer placing order")
			context.become(orderPlaced)
		case _ =>
			log.error("unhandleable message for CustomerActor in initial state")
	}
	
	def orderPlaced: Receive = {
		case UpdateOrder(order) =>
			log.info("customer updating order")
			context.become(orderUpdated)
		case PayOrder(order) =>
			log.info("customer paying order")
			context.become(orderPaid)
		case _ =>
			log.error("unhandleable message for CustomerActor in orderPlaced state")
	}
	
	def orderUpdated: Receive = {
		case UpdateOrderRejected(order) =>
			log.info("customer had updated order rejected")
			context.become(orderPlaced)
		case UpdateOrderAccepted(order) =>
			log.info("customer had udpated order accepted")
			context.become(orderPlaced)
		case _ =>
			log.error("unhandleable message for CustomerActor in orderUpdated state")
	}
	
	def orderPaid: Receive = {
		case PickupOrder(order) =>
			log.info("customer picking up order")
			context.become(orderReceived)
		case _ =>
			log.error("unhandleable message for CustomerActor in orderPaid state")
	}
	
	def orderReceived: Receive = {
		case _ =>
			log.error("unhandleable message for CustomerActor in orderReceived state")
	}
}


sealed trait CustomerMessage
case class PlaceOrder(order: Order) extends CustomerMessage
case class UpdateOrder(order: Order) extends CustomerMessage
case class UpdateOrderRejected(order: Order) extends CustomerMessage
case class UpdateOrderAccepted(order: Order) extends CustomerMessage
case class PayOrder(order: Order) extends CustomerMessage
case class PickupOrder(order: Order) extends CustomerMessage


sealed trait BaristaMessage
case class LookupNextOrder(order: Order) extends BaristaMessage
case class MakeOrder(order: Order) extends BaristaMessage
case class TakePayment(order: Order) extends BaristaMessage

sealed trait StoreMessage
case class OpenStore(products: Set[Product]) extends StoreMessage

class SupervisorActor(supervisor: Employee, employees: Seq[Employee]) extends Actor with ActorLogging {
	import scala.collection.breakOut
	var orderQueue = Set[Order]()
	var catalog = Set[Product]()
	var baristasMap = Map[Employee,ActorRef]()
	
	val codeGenerationActor = createCodeGenerator()
	
	def receive: Receive = {
		case OpenStore(products) => 
			log.info("supervisor opening store")
			catalog = products
			val baristas = for (e <- employees) yield createBarista(e)
			baristasMap = (employees zip baristas)(breakOut): Map[Employee, ActorRef]
		case m: CustomerMessage =>
			log.info("supervisor handling customer message")
		case m: BaristaMessage => 
			log.info("supervisor handling barista message")
		case _ =>
			log.error("unhandleable message for SupervisorActor in initial state")
	}
	
	def createCodeGenerator(): ActorRef = {
		context.system.actorOf(Props(new CodeGenerationActor("codeGenerationActor")),
				name = "codeGenerationActor")
	}
	def createBarista(employee: Employee): ActorRef = {
		context.system.actorOf(Props(new BaristaActor(employee, self)),
				name = "barista-" + employee.code + "-" + employee.name)
	}
}

class BaristaActor(employee: Employee, supervisor: ActorRef) extends Actor with ActorLogging {
	def receive: Receive = {
		case LookupNextOrder(order) =>
			log.info("barista looking up next order")
			context.become(orderChosen)
		case _ =>
			log.error("unhandleable message for BaristaActor in initial state")
	}
	
	def orderChosen: Receive = {
		case MakeOrder(order) =>
			log.info("barista making order")
			context.become(orderMade)
		case _ =>
			log.error("unhandleable message for BaristaActor in orderChosen state")
	
	}
	
	def orderMade: Receive = {
		case TakePayment(order) =>
			log.info("barista taking payment")
			context.become(orderReleased)
		case _ =>
			log.error("unhandleable message for BaristaActor in orderMade state")
	}
	
	def orderReleased: Receive = {
		case LookupNextOrder(order) =>
			log.info("barista looking up next order")
			context.become(orderChosen)
		case _ =>
			log.error("unhandleable message for BaristaActor in orderReleased state")
	}
}

class CodeGenerationActor(name: String) extends Actor with ActorLogging {
	import collection.mutable.{Map => MMap}
	
	var codes: MMap[String, Long] = MMap[String, Long]()
	
	def receive: Receive = {
		case obj: Order =>
			sender ! getCode(obj.getClass)
		case obj: OrderItem =>
			sender ! getCode(obj.getClass)
		case obj: Consignment =>
			sender ! getCode(obj.getClass)
		case obj: ConsignmentItem =>
			sender ! getCode(obj.getClass)
		case _ =>
			log.error("unhandleable message for CodeGenerationActor")
	}
	
	def getCode[T](klazz: Class[T]): Long = {
		val keyName = klazz.getSimpleName()
		codes.get(keyName) match {
			case Some(code) => {
				val newCode = code + 1
				codes += keyName -> newCode
				code
			}
			case None => {
				val code = 1
				codes += keyName -> code
				code
			}
		}
	}
}

/**
 * Below are domain classes
 */
object Catalog {
	lazy val products = Set(Product(1, "drip coffee"), Product(2, "hot mocha"))
}

trait Codeable {
	def code: Long
}

case class Employee(code: Long, name: String) extends Codeable
case class Customer(code: Long, name: String, orders: Seq[Order] = Seq[Order]()) extends Codeable

case class Product(code: Long, name: String) extends Codeable

case class Order(code: Long,
		customer: Customer,
		items: Seq[OrderItem],
		status: OrderStatus = OrderStatus.Created,
		paymentStatus: PaymentStatus = PaymentStatus.Created,
		deliveryStatus: DeliveryStatus = DeliveryStatus.NotShipped,
		consignments: Seq[Consignment] = Seq[Consignment]()) extends Codeable
{
	def addItem(orderItem: OrderItem): Order = {
		val newItems = orderItem +: items
		Order(code, customer, newItems, status, paymentStatus, deliveryStatus, consignments)
	}
	def +(orderItem: OrderItem) = addItem(orderItem)
	
	def addConsignment(consignment: Consignment): Order = {
		val newConsignments = consignment +: consignments
		Order(code, customer, items, status, paymentStatus, deliveryStatus, newConsignments)
	}
}

case class OrderItem(code: Long,
		entryNumber: Int,
		quantity: Int,
		product: Product,
		price: Double,
		order: Order) extends Codeable

case class Consignment(code: Long,
		order: Order,
		trackingId: String,
		items: Seq[ConsignmentItem],
		targetedDeliveryDate: DateTime,
		shippingDate: Option[DateTime] = None,
		consignmentStatus: ConsignmentStatus = ConsignmentStatus.Created) extends Codeable
{
	def addItem(consignmentItem: ConsignmentItem): Consignment = {
		val newItems = consignmentItem +: items
		Consignment(code, order, trackingId, newItems, targetedDeliveryDate, shippingDate, consignmentStatus)
	}
	def +(consignmentItem: ConsignmentItem): Consignment = addItem(consignmentItem)

}

case class ConsignmentItem(code: Long,
		orderItem: OrderItem,
		consignment: Consignment,
		deliveryStatus: DeliveryStatus = DeliveryStatus.NotShipped) extends Codeable
{
	def order: Order = consignment.order
}

sealed trait OrderStatus extends OrderStatus.Value
object OrderStatus extends EnumerationModel[OrderStatus] {
	case object Created extends OrderStatus
	case object OnValidation extends OrderStatus
	case object Validated extends OrderStatus
	case object Completed extends OrderStatus
	case object PartiallyShipped extends OrderStatus
	case object Shipped extends OrderStatus
	case object Failed extends OrderStatus
	case object Cancelled extends OrderStatus
}

sealed trait PaymentStatus extends PaymentStatus.Value
object PaymentStatus extends EnumerationModel[PaymentStatus] {
	case object Created extends PaymentStatus
	case object CheckedValid extends PaymentStatus
	case object CheckedInvalid extends PaymentStatus
	case object PaymentAuthorized extends PaymentStatus
	case object PaymentNotAuthorized extends PaymentStatus
	case object PaymentAmountReserved extends PaymentStatus
	case object PaymentNotAmountReserved extends PaymentStatus
	case object FraudChecked extends PaymentStatus
}


sealed trait ConsignmentStatus extends ConsignmentStatus.Value
object ConsignmentStatus extends EnumerationModel[ConsignmentStatus] {
	case object Created extends ConsignmentStatus
	case object PickPack extends ConsignmentStatus
	case object Ready extends ConsignmentStatus
	case object Waiting extends ConsignmentStatus
	case object Shipped extends ConsignmentStatus
	case object Cancelled extends ConsignmentStatus
}

sealed trait DeliveryStatus extends DeliveryStatus.Value
object DeliveryStatus extends EnumerationModel[DeliveryStatus] {
	case object NotShipped extends DeliveryStatus
	case object PartiallyShipped extends DeliveryStatus
	case object Completed extends DeliveryStatus
}

