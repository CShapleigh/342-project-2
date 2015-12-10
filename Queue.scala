import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable
object Queue {

  //case class Body(currentPeron: ActorRef)

 // case class Bag(currentPeron: ActorRef)

  case class Ticket(newGuy: ActorRef)

  case class RequestPersonBaggage(baggageCheck: ActorRef)

  case class RequestPersonBody(BodyCheck: ActorRef)
  def props(id: Int, bscanner: ActorRef, bagcheck: ActorRef): Props = Props(new Queue(id, bscanner,bagcheck))
}
class Queue(id: Int, bscanner: ActorRef, bagcheck: ActorRef) extends Actor {
  import Queue._
  val log = Logging(context.system, this)
  val baggageLine = mutable.ArrayBuffer[ActorRef]()
  var bagReady = true
  val SecurityLine = mutable.ArrayBuffer[ActorRef]()
  var scanReady = true

  def receive = {
    case Ticket(newGuy) => log.info("A new person had entered queue " + id)
      if (bagReady) {
        bagcheck ! BaggageCheck.Bag(newGuy)
        bagReady = false
      }
      else baggageLine.append(newGuy)

      if (scanReady) {
        bscanner ! BodyScan.Body(newGuy)
        bagReady = false
      }
      else SecurityLine.append(newGuy)


    case RequestPersonBaggage(baggageCheck) =>
      if (baggageLine.isEmpty) {
        bagReady = true
      }
      else {
        sender ! BaggageCheck.Bag(baggageLine.head)
        baggageLine -= baggageLine.head
      }

    case  RequestPersonBody(bodyCheck) =>
      if (SecurityLine.isEmpty) {
        scanReady = true
      }
      else {
        sender ! BodyScan.Body(SecurityLine.head)
        SecurityLine -= SecurityLine.head
      }

  }

}
