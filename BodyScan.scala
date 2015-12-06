import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case class RequestPersonBody(bagCheck: ActorRef)
case class BodyReport(currentPerson: ActorRef, passed: Boolean)

class BodyScan(queueNum: Int, securityGuy: ActorRef) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Body(currentPerson) =>
      log.info("Body scan " + queueNum + " scanning passenger " + currentPerson.id + " luggage.Sending report")
      if (1 % 5 == 1) {
        securityGuy ! BodyReport(currentPerson, false)
      }
      securityGuy ! BodyReport(currentPerson, true)
      sender ! RequestPersonBody(self)
  }
}
