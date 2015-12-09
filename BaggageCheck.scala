import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging

object BaggageCheck {

  case class RequestPersonBaggage(bagCheck: ActorRef)

  case class BagReport(currentPerson: ActorRef, passed: Boolean)

  case object PleaseGiveId

  case class Bag(currentPerson: ActorRef)

  case class SendID(personID: Int)

  def props(queueNum: Int, security: ActorRef): Props = Props(new BaggageCheck(queueNum, security))
}

class BaggageCheck(queueNum: Int, securityGuy: ActorRef) extends Actor {
  import BaggageCheck._
  val log = Logging(context.system, this)

  def receive = {
    case Bag(currentPerson) =>
      currentPerson ! PleaseGiveId
      val r = scala.util.Random
      if (r.nextInt(100) <= 20) {
        securityGuy ! BagReport(currentPerson, false)
      }
      securityGuy ! BagReport(currentPerson, true)
      sender ! RequestPersonBaggage(self)

    case SendID(personID) =>
      log.info("Bag scan " + queueNum + " scanning passenger " + personID + "s luggage.Sending report")
  }
}
