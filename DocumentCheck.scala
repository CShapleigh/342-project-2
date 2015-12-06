import akka.actor.{ Actor, Props }
import akka.event.Logging

Object DocumentCheck {
  case class Person
}

class DocumentCheck extends Actor {
  val log = Logging(context.system, this)

  def recieve = {
    case Person => log.info("Recieved a person")

    case _ => log.info("received unknown message")
  }
}
