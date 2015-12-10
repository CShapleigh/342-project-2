import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging
object BodyScan {

  //case class RequestPersonBody(bagCheck: ActorRef)

  case class BodyReport(currentPerson: ActorRef, passed: Boolean)

 // case object PleaseGiveId
  case class Body(currentPerson : ActorRef)
  case class SendID(personID: Int)

  def props(queueNum: Int, security: ActorRef): Props = Props(new BodyScan(queueNum, security))

}
class BodyScan(queueNum: Int, securityGuy: ActorRef) extends Actor {
  import BodyScan._
  val log = Logging(context.system, this)

  def receive = {
    case Body(currentPerson) =>
      currentPerson ! Person.PleaseGiveId
      val r = scala.util.Random
      if (r.nextInt(100) <= 20) {
        securityGuy ! Security.BodyReport(currentPerson, false)
      }
      securityGuy ! Security.BodyReport(currentPerson, true)
      sender ! Queue.RequestPersonBody(self)

    case SendID(personID) =>
      log.info("Body scan " + queueNum + " scanning passenger " + personID + " luggage.Sending report")

  }
}
