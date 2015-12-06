import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable

case class Ticket(newGuy: ActorRef)
case class RequestPersonBaggage(Baggage : ActorRef)
case class RequestPersonBody(bagCheck: ActorRef)


class Queue(id: Int, bscanner: ActorRef, bagcheck: ActorRef) extends Actor {
  val log = Logging(context.system, this)
  val baggageLine = mutable.ArrayBuffer[ActorRef]()
  var bagReady = true
  val SecurityLine = mutable.ArrayBuffer[ActorRef]()
  var scanReady = true

  def receive = {
    case Ticket(newGuy) => log.info("A new person had entered queue " + id)
      if (bagReady) {
        bagcheck ! newGuy
        bagReady = false
      }
      else baggageLine.append(newGuy)

      if (scanReady) {
        bscanner ! newGuy
        bagReady = false
      }
      else SecurityLine.append(newGuy)


    case RequestPersonBaggage() =>
      if (baggageLine.isEmpty) {
        bagReady = true
      }
      else {
        sender ! baggageLine.head
        baggageLine -= baggageLine.head
      }

    case  RequestPersonBody() =>
      if (SecurityLine.isEmpty) {
        scanReady = true
      }
      else {
        sender ! SecurityLine.head
        SecurityLine -= SecurityLine.head
      }
  }

}