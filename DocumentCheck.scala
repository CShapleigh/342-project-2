import akka.actor.{ Actor, Props }
import akka.event.Logging

Object DocumentCheck {
  case class Person
}

class DocumentCheck(numQueues: Int) extends Actor {
  val log = Logging(context.system, this)

  def recieve = {
    case Document(id) =>
      log.info( id + " documents are good")
      sender ! ValudDocument(id % numQueues)

    case _ => log.info("received unknown message")
  }
}
