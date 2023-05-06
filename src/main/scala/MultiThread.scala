import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, TimeoutException}
import scala.util.{Failure, Success, Try}

class MyActor extends Actor {
  def receive = {
    case message: String =>
      println(s"${Thread.currentThread().getName}: Received: $message")
      Thread.sleep(500) // Add a delay of 1 second
      sender() ! s"Response to message $message"
    case _ => println("Unknown message")
  }
}

object MultiThread extends App {
  implicit val timeout = Timeout(5.seconds)
val config = ConfigFactory.parseString( """
    fixed-thread-pool {
         type = Dispatcher
         executor = "thread-pool-executor"
         thread-pool-executor {
           fixed-pool-size = 5
         }
       throughput = 2}""") //ConfigFactory.load("akka.conf")
  val system = ActorSystem("MySystem", config)
  val act = system.actorOf(Props[MyActor].withDispatcher("fixed-thread-pool"), "myActor")
  (1 to 10).map(i => (act ! i)) //tell
  val futures = (1 to 10).map(i => (act ? s"Message $i").mapTo[String]) //ask = receive
  wait(futures)
//  noWait(futures)
  system.terminate()



  def wait(futures:Seq[Future[String]])={
    val futureSeq = Future.sequence(futures)
    val result = Await.result(futureSeq, timeout.duration)
    print(result) }

  def noWait(futures:Seq[Future[String]])={
    futures.foreach { future =>
      future.onComplete {
        case Success(response) => println(s"Received response: $response")
        case Failure(_: TimeoutException) => println("Timed out")
        case Failure(exception) => println(s"Error occurred: $exception")
      }}}
}
