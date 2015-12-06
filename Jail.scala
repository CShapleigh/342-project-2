import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

// Messages received
case class ReceivingToJail(currentPerson: ActorRef)

// Roles: knows people in jail and number of security stations that feed jail
class Jail() extends Actor {

  val log = Logging(context.system, this)

  def printJailStatus() = {
    // TODO: implement
  }

  def receive() = {
    // TODO: Adds people to jailed list

  }
}
