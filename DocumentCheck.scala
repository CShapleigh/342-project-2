import akka.actor.Actor
import akka.event.Logging

case class ValidDocument(id: Int)
case object InvalidDocument

class DocumentCheck(numQueues: Int) extends Actor {
  val log = Logging(context.system, this)

  def recieve = {
    case Document(id) =>
      log.info( id + " documents are good")
      if (1 % 5 == 1) {
        sender ! InvalidDocument
      }
      sender ! ValidDocument(id % numQueues)

    case _ => log.info("received unknown message")
  }
}
