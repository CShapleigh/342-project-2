import akka.actor.{ Actor, ActorRef }

case object Start
case class Document(id: Int)
case class Ticket(id: Int)
case class ValidDocument(queueNum: Int)
case object InvalidDocument

class Person(id: Int,  documentCheck: ActorRef, queues: Array[ActorRef]) extends Actor {

  //TODO: Send to document check first, need to override the start call?
  val log = Logging(context.system, this)

  def preStart() = {
    log.info(id + " sends document to check")
    documentCheck ! Document(id)
  }



  def recieve = {
    case ValidDocument(queueNum) =>
    log.info(id + "goes to queue " + queueNum)
      queues(queueNum) ! Ticket(id)

    case InvalidDocument() =>
    log.info(id + "has invalid documents and goes home")
  }


}
