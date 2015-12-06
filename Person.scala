import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case class Document(id: Int)
case class Ticket(currentPerson: ActorRef)

class Person(id: Int,  documentCheck: ActorRef, queues: Array[ActorRef]) extends Actor {

  val log = Logging(context.system, this)

  def preStart() = {
    log.info(id + " sends document to check")
    documentCheck ! Document(id)
  }

  def receive = {
    case ValidDocument(queueNum) =>
      log.info(id + "goes to queue " + queueNum)
      queues(queueNum) ! Ticket(self)

    case InvalidDocument() =>
      log.info(id + "has invalid documents and goes home")
  }

}
