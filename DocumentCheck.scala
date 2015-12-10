import akka.actor.{Props, ActorRef, Actor}
import akka.event.Logging
object DocumentCheck {

  //case class ValidDocument(id: Int)

  case object InvalidDocument
  case class Document(id: Int)
  def props(queueNum: Int): Props = Props(new DocumentCheck(queueNum))
}
class DocumentCheck(numQueues: Int) extends Actor {
  import DocumentCheck._
  val log = Logging(context.system, this)

  def receive = {
    case Document(id) =>
      log.info( id + " documents are good")
      val r = scala.util.Random
      if (r.nextInt(100) <= 20) {
        sender ! InvalidDocument
      }
      sender ! Person.ValidDocument(id % numQueues)

    case _ => log.info("received unknown message")
  }
}
