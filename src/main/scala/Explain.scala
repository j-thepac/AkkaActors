import akka.actor.{Actor, ActorSystem, Props}

class SimpleActor extends Actor{
  def receive={
    case s:String =>{Thread.sleep(10);print(s)}
  }
}

object Explain extends App{
val system=ActorSystem("ActorSystem")
val actor=   system.actorOf(Props[SimpleActor],"SimpleActor")
  print("1")
  actor ! "2"
  print("3")
  actor ! "4"
system.terminate()
}
