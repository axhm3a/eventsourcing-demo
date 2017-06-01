package BankAccount

import akka.persistence.PersistentActor

import scala.util.{Failure, Success}

/**
  * Created by axhm3a on 23.05.17.
  */
class BankAccountService extends PersistentActor {
  override def persistenceId: String = "bankAccounts"
  var state : BankAccountState = BankAccountState()

  def updateState(event : Event) = {
    println (s"event => $event")
    state = state.update(event)
  }

  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
  }

  override def receiveCommand: Receive = {
    case _: AllBankAccountsQuery =>
      sender ! state.getAllBankAccountIds
    case sbq: BankAccountQuery =>
      sender ! state.getBankAccount(sbq.bankAccountId)
    case command: Command =>
      println (s"command => $command")

      Event(command, state) match {
        case Success(event) =>
          persist(event) {
            event =>
              updateState(event)
              sender ! event
          }
        case Failure(e) =>
          println (s"invalid => $e")
          sender ! e
      }


    case _ => println ("unknown")
  }
}
