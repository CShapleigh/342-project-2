import akka.actor.{ Actor, Props }
import akka.event.Logging

case class BodyReport(currentPerson: ActorRef, passed: Boolean)

class BodyScan(queueNum: Int, securityGuy: ActorRef) extends Actor {
  def receive = {
    case Body(currentPerson) =>
      log.info("Body scan " + queueNum " scanning passenger " + currentPerson.id +"'s luggage.
        Sending report")
      if (1 % 5 == 1) {
        securityGuy ! BodyReport(currentPerson, false)
      }
      securityGuy ! BodyReport(currentPerson, true)
      sender ! self
  }
}
