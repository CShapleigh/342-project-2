import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable

case class PersonGoingToJail(guiltyPerson: ActorRef)

class Security(jail: ActorRef) extends Actor {
  val log = Logging(context.system, this)
  val   baggageReports = mutable.ArrayBuffer[Boolean]()
  val   personReports = mutable.ArrayBuffer[Boolean]()
  val   PeopleWaiting = mutable.ArrayBuffer[ActorRef]()
  def receive = {
    case BodyReport(person, status) =>
      if(person == PeopleWaiting.head){
        guiltyorNot()
      }
      else{
        PeopleWaiting.append(person)
        personReports.append(status)
      }

    case BagReport(person, status) =>
      if(person == PeopleWaiting.head){
        guiltyorNot()
      }
      else{
        PeopleWaiting.append(person)
        baggageReports.append(status)
      }
  }
  def guiltyorNot() = {
    if (baggageReports.head && personReports.head){
      log.info("Go directly to the plane")
      PeopleWaiting -= PeopleWaiting.head
      personReports -= personReports.head
      baggageReports -= baggageReports.head

    }
    else{
      jail ! PersonGoingToJail(PeopleWaiting.head)
      log.info("Go directly to jail")
      PeopleWaiting -= PeopleWaiting.head
      personReports -= personReports.head
      baggageReports -= baggageReports.head
    }
  }
}
