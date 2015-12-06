import akka.actor.Actor
import akka.event.Logging

case Object requestPersonsInJail

class Plane extends Actor {
  var peopleOnPlane = 0
  var peopleInJail = 0

  def receive = {
    case Person(currentPerson) =>
      log.info( currerPerson.id + " boards plane")
      peopleOnPlane += 1
      Driver.jail ! requestPersonsInJail
      if (peopleOnPlane + peopleInJail == Driver.personCount) {
        log.info("Plane takes off")
        context.system.shotdown
      }
    case JailCount(numPersons) =>
     log.info(numPersons + " persons in jail")
     if (peopleOnPlane + peopleInJail == Driver.personCount) {
       log.info("Plane takes off")
       context.system.shotdown
     }
  }
}
