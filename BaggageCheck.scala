import akka.actor.{ Actor, Props }
import akka.event.Logging

case class BagReport(currentPerson: ActorRef, passed: Boolean)

class BaggageCheck(queueNum: Int, securityGuy: ActorRef) extends Actor {
  def receive = {
    case Bag(currentPerson) =>
      log.info("Bag scan " + queueNum " scanning passenger " + currentPerson.id +"'s luggage.
        Sending report")
      if (1 % 5 == 1) {
        securityGuy ! BagReport(currentPerson, false)
      }
      securityGuy ! BagReport(currentPerson, true)
  }
}
