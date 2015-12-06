import akka.actor.Actor
import akka.event.Logging

case object RequestPersonsInJail

class Plane extends Actor {
  var peopleOnPlane = 0
  var peopleInJail = 0
  val log = Logging(context.system, this)

  def receive = {
    case PersonForPlane(currentPerson) =>
      log.info( currerPerson.id + " boards plane")
      peopleOnPlane += 1
      Driver.jail ! RequestPersonsInJail
      if (peopleOnPlane + peopleInJail == Driver.personCount) {
        log.info("Plane takes off")
        context.system.shutdown
      }
    case JailCount(numPersons) =>
     log.info(numPersons + " persons in jail")
     if (peopleOnPlane + peopleInJail == Driver.personCount) {
       log.info("Plane takes off")
       context.system.shutdown
     }
  }
}
