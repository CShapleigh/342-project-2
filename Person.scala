import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case object Start
case class Document(id: Int)
case class Ticket(id: Int)
case class ValidDocument(queueNum: Int)
case object InvalidDocument

class Passenger(id: Int,  documentCheck: ActorRef, queues: Array[ActorRef]) extends Actor {

  //TODO: Send to document check first, need to override the start call?

  val log = Logging(context.system, this)

  def receive = {
    case ValidDocument(queueNum) =>
    log.info(id + "goes to queue " + queueNum)
      queues(queueNum) ! self
  }
}
