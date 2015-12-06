import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case class RequestPersonBaggage(bagCheck: ActorRef)
case class BagReport(currentPerson: ActorRef, passed: Boolean)

class BaggageCheck(queueNum: Int, securityGuy: ActorRef) extends Actor {

  val log = Logging(context.system, this)

  def receive = {
    case Bag(currentPerson) =>
      log.info("Bag scan " + queueNum + " scanning passenger " + currentPerson.id + "s luggage.Sending report")
      if (1 % 5 == 1) {
        securityGuy ! BagReport(currentPerson, false)
      }
      securityGuy ! BagReport(currentPerson, true)
      sender ! RequestPersonBaggage(self)
  }
}
