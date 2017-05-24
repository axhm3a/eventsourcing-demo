package BankAccount

import akka.persistence.PersistentActor

/**
  * Created by axhm3a on 23.05.17.
  */
class BankAccountService extends PersistentActor {
  override def persistenceId: String = "bankAccounts"
  var state : BankAccountState = BankAccountState()

  def updateState(event : Event) = {
    println ("processing event")
    state = state.update(event)
  }

  override def receiveRecover: Receive = {
    case event: Event => updateState(event)
  }

  override def receiveCommand: Receive = {
    case _: AllBankAccountsQuery =>
      sender ! state.getAllBankAccountIds
    case sbq: BankAccountQuery =>
      sender ! state.getBankAccountBalance(sbq.bankAccountId)
    case command: Command =>
      println ("processing command")

      persist(Event(command, state)) {
        event =>
          updateState(event)
          sender ! event
      }
    case _ => println ("error")
  }
}
