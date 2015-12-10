import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging
object Person {

  //case class Document(id: Int)
  case object InvalidDocument
  case object PleaseGiveId
  //case class Ticket(currentPerson: ActorRef)
  case class ValidDocument(queueNum: Int)
  case class SendID(id: Int)
  def props(id: Int, documentCheck: ActorRef, queues: Array[ActorRef]): Props = Props(new Person(id, documentCheck, queues))

}
class Person(id: Int,  documentCheck: ActorRef, queues: Array[ActorRef]) extends Actor {
  import Person._
  val log = Logging(context.system, this)

  override def  preStart() = {
    log.info(id + " sends document to check")
    documentCheck ! DocumentCheck.Document(id)
  }

  def receive = {
    case ValidDocument(queueNum) =>
      log.info(id + "goes to queue " + queueNum)
      queues(queueNum) ! Queue.Ticket(self)

    case InvalidDocument =>
      log.info(id + "has invalid documents and goes home")

    case PleaseGiveId =>
      sender ! Person.SendID(id)
  }

}
