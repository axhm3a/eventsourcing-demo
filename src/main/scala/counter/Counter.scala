package counter

import akka.persistence._

/**
  * Created by axhm3a on 22.05.17.
  */

class Counter extends PersistentActor {

  var counter = 0;

  override def persistenceId: String = "mutti"

  def handleEvent(event: CountEvent): Unit = {
    println("event handled!")
    counter += 1;
  }

  override def receiveCommand: Receive = {
    case c: CountCommand => {
      persist(CountEvent()) {
        event => {
          println("command executed!")
          handleEvent(event)
        }
      }
    }
    case q: CountQuery => sender ! counter
    case _ => println("error")
  }

  override def receiveRecover: Receive = {
    case event: CountEvent => handleEvent(event)
  }

}
