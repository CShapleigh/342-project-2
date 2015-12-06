import akka.actor.{ Actor, Props, ActorSystem, ActorRef }


object Driver extends App {
    val personCount = 7
    val queueCount = 2

    val tsaScreenSystem = ActorSystem("TSA")
    val jail = system.actorOf(Props[Jail], name = "jail")
    val plane = system.actorOf(Props[Gate], name = "plane")
    val securityGuy = system.actorOf(Props[Security], name = "joe")

    val queues: Array[ActorRef] = new Array(queueCount)
    val documentCheck = system.actorOf(Props(new DocumentCheck(queueCount)), name = "documentCheck")

    for (i <- 0 util queueCount) {
      val tempBodyScan = system.actorOf(Props(new BodyScan(i, securityGuy)), name = "bodyScan " + i)
      val tempBaggageScan = system.actorOf(Props(new BaggageCheck(i, securityGuy)), name = "bodyScan " + i)
      queues(i) = system.actorOf(Props(new Queue(i)), name = "queue number: " + i)
    }

    for (i <- 0 util personCount) {
      val person = system.actorOf(Props(new Person(i, documentCheck, queues)), name = "person: " + i)
    }
}
