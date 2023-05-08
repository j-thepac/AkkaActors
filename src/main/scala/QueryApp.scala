import akka.actor._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt


class DB extends Actor {
  def receive = {
    case s:String =>sender() ! Seq("res1","res2")
  }
}
object QueryApp extends App {
  val system = ActorSystem("system")
  val actor = system.actorOf(Props[DB], "db")
  val query = "SELECT * FROM table1"
  implicit val timeout = Timeout(5.seconds)
  val res: Future[Seq[String]] = (actor ? query).asInstanceOf[Future[Seq[String]]]
  res.onComplete { case Success(res) => print(res)} //Await.result(res, timeout.duration)
  system.terminate()
}
