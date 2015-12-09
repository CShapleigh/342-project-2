import akka.actor.{ Actor, ActorRef, Props }
import akka.event.Logging

object Plane {

  case object RequestPersonsInJail
  case object PleaseGiveId
  case class PersonForPlane(currentPerson: ActorRef)
  case class JailCount(numPersons: Int)
  case class SendID(personID: Int)
  def props(jailref: ActorRef,people: Int): Props = Props(new Plane(jailref, people))

}
class Plane(jail: ActorRef, personCount: Int ) extends Actor {
  import Plane._
  var peopleOnPlane = 0
  var peopleInJail = 0
  val log = Logging(context.system, this)

  def receive = {
    case PersonForPlane(currentPerson) =>
      currentPerson ! PleaseGiveId
      peopleOnPlane += 1
      jail ! RequestPersonsInJail
      if (peopleOnPlane + peopleInJail == personCount) {
        log.info("Plane takes off")
        context.system.shutdown
      }
    case JailCount(numPersons) =>
     log.info(numPersons + " persons in jail")
     if (peopleOnPlane + peopleInJail == personCount) {
       log.info("Plane takes off")
       context.system.shutdown
     }

     case SendID(personID) =>
      log.info( personID + " boards plane")
  }
}
