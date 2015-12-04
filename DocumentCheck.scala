import akka.actor.Actor
import akka.event.Logging

class DocumentCheck extends Actor {
  val log = Logging(context.system, this)

  def recieve = {

  }
}
