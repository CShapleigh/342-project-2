import akka.actor.{ Actor, ActorRef }
import akka.event.Logging

case object RequestPersonsInJail

class Plane(jail: ActorRef, personCount: Int ) extends Actor {
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
