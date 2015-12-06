import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable

case class Person()

case class BaggageCheck()

case class BodyScan()

class Queue(id: Int, bscanner: ActorRef, bagcheck: ActorRef) extends Actor {
  val log = Logging(context.system, this)
  val baggageLine = mutable.ArrayBuffer()
  var bagReady = true
  val SecurityLine = mutable.ArrayBuffer()
  var scanReady = true

  def receive = {
    case Person(newGuy) => log.info("A new person had entered queue " + id)
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


    case BaggageCheck =>
      if (baggageLine.head == None) {
        bagReady = true
      }
      else {
        sender ! baggageLine.head
        baggageLine.remove(baggageLine.head)
      }

    case BodyScan =>
      if (SecurityLine.head == None) {
        scanReady = true
      }
      else {
        sender ! SecurityLine.head
        SecurityLine.remove(SecurityLine.head)
      }
  }

}