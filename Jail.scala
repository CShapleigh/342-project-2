import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

// Messages received
case class JailCount(peopleInJail: Int)

case object GetJailStatus

// Roles: knows people in jail and number of security stations that feed jail
class Jail() extends Actor {

  val log = Logging(context.system, this)
  var peopleInJail = 0

  def receive = {
    case PersonGoingToJail(currentPerson) =>
      log.info(currentPerson.id + " is now in jail")
      peopleInJail += 1

    case RequestPersonsInJail =>
      sender ! JailCount(peopleInJail)
  }

}
