import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

/**
  * Created by axhm3a on 22.05.17.
  */
trait HttpTrait {
  implicit val system = ActorSystem("scala-event-sourcing")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
}
