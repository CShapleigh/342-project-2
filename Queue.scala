import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable

case class Body(currentPeron: ActorRef)
case class Bag(currentPeron: ActorRef)


class Queue(id: Int, bscanner: ActorRef, bagcheck: ActorRef) extends Actor {
  val log = Logging(context.system, this)
  val baggageLine = mutable.ArrayBuffer[ActorRef]()
  var bagReady = true
  val SecurityLine = mutable.ArrayBuffer[ActorRef]()
  var scanReady = true

  def receive = {
    case Ticket(newGuy) => log.info("A new person had entered queue " + id)
      if (bagReady) {
        bagcheck ! Bag(newGuy)
        bagReady = false
      }
      else baggageLine.append(newGuy)

      if (scanReady) {
        bscanner ! Body(newGuy)
        bagReady = false
      }
      else SecurityLine.append(newGuy)


    case RequestPersonBaggage() =>
      if (baggageLine.isEmpty) {
        bagReady = true
      }
      else {
        sender ! Bag(baggageLine.head)
        baggageLine -= baggageLine.head
      }

    case  RequestPersonBody() =>
      if (SecurityLine.isEmpty) {
        scanReady = true
      }
      else {
        sender ! Body(SecurityLine.head)
        SecurityLine -= SecurityLine.head
      }
  }

}
