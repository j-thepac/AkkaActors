import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, TimeoutException}
import scala.util.{Failure, Success, Try}

class TestApp extends Actor{
  def receive={
    case i:Int=>
      Thread.sleep(i)
      sender ! s"Waited for $i"
  }
}

object SingleThread {
  def main(args:Array[String]):Unit={
    implicit val timeout = Timeout(5.seconds)
    val system = ActorSystem("MySystem")
    val act = system.actorOf(Props[TestApp], "actor1")
    val res: Future[Any] = (act ? 1)
    //res.onComplete {case Success(response) => println(s"Received response: $response") }
    print(Await.result(res, timeout.duration))
    system.terminate()
  }}
