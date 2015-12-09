import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging

// Messages received
object Jail {

  case class JailCount(peopleInJail: Int)
  case object PleaseGiveId
  case object GetJailStatus
  case class SendID(personID: Int)
  case object RequestPersonsInJail
  case class PersonGoingToJail(currentPerson : ActorRef)
  def props(): Props = Props(new Jail())
}
// Roles: knows people in jail and number of security stations that feed jail
class Jail() extends Actor {
  import Jail._
  val log = Logging(context.system, this)
  var peopleInJail = 0

  def receive = {
    case PersonGoingToJail(currentPerson) =>
      currentPerson ! PleaseGiveId
      peopleInJail += 1

    case RequestPersonsInJail =>
      sender ! JailCount(peopleInJail)

    case SendID(personID) =>
      log.info(personID + " is now in jail")
  }

}
