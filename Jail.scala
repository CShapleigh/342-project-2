import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

// Messages received
case class ReceivingToJail(currentPerson: ActorRef)

// Roles: knows people in jail and number of security stations that feed jail
class Jail() extends Actor {

  val log = Logging(context.system, this)
  val peopleInJail = 0

  def receive = {
    // TODO: Add people to jailed list

    case GetJailStatus => sender ! peopleInJail
  }

}
