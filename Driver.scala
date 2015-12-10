import akka.actor.{ Actor, Props, ActorSystem, ActorRef }


object Driver {
  def main(args: Array[String]) {
    val personCount = 7
    val queueCount = 2

    val tsaScreenSystem = ActorSystem()
    val jail = tsaScreenSystem.actorOf(Props[Jail], name = "JAIL")
    val plane = tsaScreenSystem.actorOf(Props(new Plane(jail, personCount)), name = "Plane" )
    val securityGuy = tsaScreenSystem.actorOf(Props(new Security(jail, plane)), name = "Security")

    val queues: Array[ActorRef] = new Array(queueCount)
    val documentCheck = tsaScreenSystem.actorOf(Props(new DocumentCheck(queueCount)), name = "documentCheck")

    for (i <- 0 until queueCount) {
      val tempBodyScan = tsaScreenSystem.actorOf(Props(new BodyScan(i, securityGuy)))
      val tempBaggageScan = tsaScreenSystem.actorOf(Props(new BaggageCheck(i, securityGuy)))
      queues(i) = tsaScreenSystem.actorOf(Props(new Queue(i, tempBodyScan, tempBaggageScan)))
    }

    for (i <- 0 until personCount) {
      val person = tsaScreenSystem.actorOf(Props(new Person(i, documentCheck, queues)))
    }
  }
}
