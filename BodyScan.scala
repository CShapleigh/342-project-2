import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case class RequestPersonBody(bagCheck: ActorRef)
case class BodyReport(currentPerson: ActorRef, passed: Boolean)
case object PleaseGiveId

class BodyScan(queueNum: Int, securityGuy: ActorRef) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Body(currentPerson) =>
      currentPerson ! PleaseGiveId
      val r = scala.util.Random
      if (r.nextInt(100) <= 20) {
        securityGuy ! BodyReport(currentPerson, false)
      }
      securityGuy ! BodyReport(currentPerson, true)
      sender ! RequestPersonBody(self)

    case SendID(personID) =>
      log.info("Body scan " + queueNum + " scanning passenger " + personID + " luggage.Sending report")

  }
}
