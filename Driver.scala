import akka.actor.{ Actor, Props, ActorSystem, ActorRef }


object Driver extends App {
    val personCount = 7
    val queueCount = 2

    val tsaScreenSystem = ActorSystem("TSA")
    val jail = tsaScreenSystem.actorOf(Props[Jail], name = "jail")
    val plane = tsaScreenSystem.actorOf(Props[Gate], name = "plane")
    val securityGuy = tsaScreenSystem.actorOf(Props[Security], name = "joe")

    val queues: Array[ActorRef] = new Array(queueCount)
    val documentCheck = tsaScreenSystem.actorOf(Props(new DocumentCheck(queueCount)), name = "documentCheck")

    for (i <- 0 until queueCount) {
      val tempBodyScan = tsaScreenSystem.actorOf(Props(new BodyScan(i, securityGuy)), name = "bodyScan " + i)
      val tempBaggageScan = tsaScreenSystem.actorOf(Props(new BaggageCheck(i, securityGuy)), name = "bodyScan " + i)
      queues(i) = system.actorOf(Props(new Queue(i, tempBodyScan, tempBaggageScan)), name = "queue number: " + i)
    }

    for (i <- 0 until personCount) {
      val person = tsaScreenSystem.actorOf(Props(new Person(i, documentCheck, queues)), name = "person: " + i)
    }
}
