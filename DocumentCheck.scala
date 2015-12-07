import akka.actor.Actor
import akka.event.Logging

case class ValidDocument(id: Int)
case object InvalidDocument

class DocumentCheck(numQueues: Int) extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case Document(id) =>
      log.info( id + " documents are good")
      val r = scala.util.Random
      if (r.nextInt(100) <= 20) {
        sender ! InvalidDocument
      }
      sender ! ValidDocument(id % numQueues)

    case _ => log.info("received unknown message")
  }
}
