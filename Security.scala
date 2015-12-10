import akka.actor.{Props, Actor, ActorRef}
import akka.event.Logging
import scala.collection.mutable

object Security {

  case class PersonGoingToJail(guiltyPerson: ActorRef)

  case class PersonForPlane(goodPerson: ActorRef)

  case class BodyReport(person: ActorRef, status :Boolean)

  case class BagReport(person: ActorRef, status :Boolean)
  def props(jail: ActorRef, plane: ActorRef): Props = Props(new Security(jail, plane))
}
class Security(jail: ActorRef, plane: ActorRef) extends Actor {
  import Security._
  val log = Logging(context.system, this)
  val   baggageReports = mutable.ArrayBuffer[Boolean]()
  val   personReports = mutable.ArrayBuffer[Boolean]()
  val   PeopleWaiting = mutable.ArrayBuffer[ActorRef]()

  def receive = {
    case BodyReport(person, status) =>
      if (PeopleWaiting.isEmpty){
        PeopleWaiting += person
        personReports += status
      }
      else if(person == PeopleWaiting(0)){
        if (baggageReports(0) && personReports(0)){
          plane ! Plane.PersonForPlane(PeopleWaiting(0))
          log.info("Go directly to the plane")
          PeopleWaiting.remove(0) //-= PeopleWaiting(0)
          personReports.remove(0) //-= personReports(0)
          baggageReports.remove(0)// -= baggageReports(0)

        }
        else{
          jail ! Jail.PersonGoingToJail(PeopleWaiting(0))
          log.info("Go directly to jail")
          PeopleWaiting.remove(0)// -= PeopleWaiting(0)
          personReports.remove(0)// -= personReports(0)
          baggageReports.remove(0)// -= baggageReports(0)
        }
      }
      else{
        PeopleWaiting+= person
        personReports+= status
      }

    case BagReport(person, status) =>
      if (PeopleWaiting.isEmpty){
        PeopleWaiting += person
        baggageReports += status
      }
      else if(person == PeopleWaiting(0)){
        if (baggageReports(0) && personReports(0)){
          plane ! Plane.PersonForPlane(PeopleWaiting(0))
          log.info("Go directly to the plane")
          PeopleWaiting.remove(0) //-= PeopleWaiting(0)
          personReports.remove(0) //-= personReports(0)
          baggageReports.remove(0) //-= baggageReports(0)

        }
        else{
          jail ! Jail.PersonGoingToJail(PeopleWaiting(0))
          log.info("Go directly to jail")
          PeopleWaiting.remove(0) // -= PeopleWaiting(0)
          personReports.remove(0) //-= personReports(0)
          baggageReports.remove(0)// -= baggageReports(0)
        }
      }
      else{
        PeopleWaiting += person
        baggageReports += status
      }
  }
/**  def guiltyorNot() = {
    if (baggageReports(0) && personReports(0)){
      plane ! Plane.PersonForPlane(PeopleWaiting(0))
      log.info("Go directly to the plane")
      PeopleWaiting -= PeopleWaiting(0)
      personReports -= personReports(0)
      baggageReports -= baggageReports(0)

    }
    else{
      jail ! Jail.PersonGoingToJail(PeopleWaiting(0))
      log.info("Go directly to jail")
      PeopleWaiting -= PeopleWaiting(0)
      personReports -= personReports(0)
      baggageReports -= baggageReports(0)
    }
  }
  **/
}
